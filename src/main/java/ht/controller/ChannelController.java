package ht.controller;

import ht.domain.ApiItemWrapper;
import ht.domain.channel.BizRequest;
import ht.service.CustomerService;
import ht.service.MemberService;
import ht.service.channel.ChannelService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static ht.constants.CommonConstants.CSTM_LINK_MAP;

@RestController
@Slf4j
@RequestMapping("/channel/hook/{apiVersion}")
public class ChannelController {

	@Resource
	private ChannelService channelService;
	@Resource
	private MemberService memberService;
	@Resource
	private CustomerService customerService;

	/**
	 * 고객 인입 이벤트 수신
	 */
	@PostMapping(path = {"/open"})
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> open(
			@PathVariable(value = "apiVersion") @NotEmpty @ApiParam(value = "API 버전", required = true) String apiVersion,
			@RequestHeader(value = "Track-Key", required = false) @Positive @ApiParam(value = "트랙 키") Long trackKey,
			@RequestBody @Valid BizRequest bizRequest)
					throws Exception
	{
		if (trackKey == null) {
			LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
			trackKey = created.toInstant(ZoneOffset.UTC).toEpochMilli();
		}

		log.info("CHANNEL OPEN, TRACK KEY: {}, CHANNEL ID: {}, SERVICE ID: {}, CUSTOMER ID: {}, BODY: {}",
				trackKey, bizRequest.getChannelId(), bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId(), bizRequest);

		// 사용자 식별
		Map<String, Object> customer = customerService.selectCustomerByChannel(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
				bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		// 고객 식별이 불가능 할 경우 생성 및 저장
		if (customer == null) {
			customer = customerService.createCustomer(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
					bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		}

		if (channelService.open(trackKey, bizRequest, customer)) {
			return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.OK);
		}
		return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.NOT_FOUND);
	}

	/**
	 * 고객 메세지 수신
	 */
	@PostMapping(path = {"/message"})
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> message(
			@PathVariable(value = "apiVersion") @NotEmpty @ApiParam(value = "API 버전", required = true) String apiVersion,
			@RequestHeader(value = "Track-Key", required = false) @Positive @ApiParam(value = "트랙 키") Long trackKey,
			@RequestBody @Valid BizRequest bizRequest)
					throws Exception
	{
		if (trackKey == null) {
			LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
			trackKey = created.toInstant(ZoneOffset.UTC).toEpochMilli();
		}

		log.debug("CHANNEL MESSAGE, TRACK KEY: {}, CHANNEL ID: {}, SERVICE ID: {}, CUSTOMER ID: {}",
				trackKey, bizRequest.getChannelId(), bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		log.info("CHANNEL MESSAGE, TRACK KEY: {}, CHANNEL ID: {}, SERVICE ID: {}, CUSTOMER ID: {}, BODY: {}",
				trackKey, bizRequest.getChannelId(), bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId(), bizRequest);

		// 사용자 식별
		Map<String, Object> customer = customerService.selectCustomerByChannel(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
				bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		// 고객 식별이 불가능 할 경우 생성 및 저장
		if (customer == null) {
			customer = customerService.createCustomer(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
					bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		}

		if (channelService.receiveMessage(trackKey, bizRequest, customer)) {
			return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.OK);
		}
		return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.NOT_FOUND);
	}

	/**
	 * 메세지 송신 처리 여부
	 */
	@PostMapping(path = "/message-callback")
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> messageCallback(
			@PathVariable(value = "apiVersion") @NotEmpty @ApiParam(value = "API 버전", required = true) String apiVersion,
			@RequestHeader(value = "Track-Key", required = false) @Positive @ApiParam(value = "트랙 키") Long trackKey,
			@RequestHeader(value = "Channel-Event-Id") @NotEmpty @ApiParam(value = "이벤트 식별자", required = true) String channelEventId,
			@RequestBody @NotNull Boolean result)
					throws Exception
	{
		if (trackKey == null) {
			LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
			trackKey = created.toInstant(ZoneOffset.UTC).toEpochMilli();
		}

		log.info("CHANNEL MESSAGE CALLBACK, TRACK KEY: {}, CHANNEL EVENT ID: {}, BODY: {}"
				, trackKey, channelEventId, result);

		if (result) {
			channelService.callbackMessage(trackKey, channelEventId, result);
		}

		return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.OK);
	}

	/**
	 * 봇 대화 이력 (카카오 오픈빌더 등)
	 */
	@PostMapping(path = "/relay-bot-message")
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> relayBotMessage(
			@PathVariable(value = "apiVersion") @NotEmpty @ApiParam(value = "API 버전", required = true) String apiVersion,
			@RequestHeader(value = "Track-Key", required = false) @Positive @ApiParam(value = "트랙 키") Long trackKey,
			@RequestBody @Valid BizRequest bizRequest)
					throws Exception
	{
		if (trackKey == null) {
			LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);
			trackKey = created.toInstant(ZoneOffset.UTC).toEpochMilli();
		}

		log.info("CHANNEL RELAY BOT MESSAGE, TRACK KEY: {}, CHANNEL ID: {}, SERVICE ID: {}, CUSTOMER ID: {}",
				trackKey, bizRequest.getChannelId(), bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		log.debug("CHANNEL RELAY BOT MESSAGE, TRACK KEY: {}, CHANNEL ID: {}, SERVICE ID: {}, CUSTOMER ID: {}, BODY: {}",
				trackKey, bizRequest.getChannelId(), bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId(), bizRequest);

		// 사용자 식별
		Map<String, Object> customer = customerService.selectCustomerByChannel(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
				bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		// 고객 식별이 불가능 할 경우 생성 및 저장
		if (customer == null) {
			customer = customerService.createCustomer(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
					bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		}

		if (channelService.relayBotMessage(trackKey, bizRequest, customer)) {
			return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.OK);
		}
		return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.NOT_FOUND);
	}

	/**
	 * 세션 종료 수신
	 */
	@PostMapping(path = "/close")
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> close(
			@PathVariable(value = "apiVersion") @NotEmpty @ApiParam(value = "API 버전", required = true) String apiVersion,
			@RequestHeader(value = "Track-Key", required = false) @Positive @ApiParam(value = "트랙 키") Long trackKey,
			@RequestBody @Valid BizRequest bizRequest)
					throws Exception
	{
		log.info("CHANNEL CLOSE, TRACK KEY: {}, CHANNEL ID: {}, SERVICE ID: {}, CUSTOMER ID: {}, BODY: {}",
				trackKey, bizRequest.getChannelId(), bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId(), bizRequest);

		// 사용자 식별
		Map<String, Object> customer = customerService.selectCustomerByChannel(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
				bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		// 고객 식별이 불가능 할 경우 생성 및 저장
		if (customer != null) {
			channelService.close(trackKey, bizRequest, customer);
		} else {
			log.error("CHANNEL CLOSE, TRACK KEY: {}, CHANNEL ID: {}, SERVICE ID: {}, CUSTOMER ID: {}, NO CUSTOMER",
					trackKey, bizRequest.getChannelId(), bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		}

		return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> handleException(Exception e) {

		log.error("Channel Exception Handler: {}", e.getLocalizedMessage(), e);
		return new ResponseEntity<>(new ApiItemWrapper<>(e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
