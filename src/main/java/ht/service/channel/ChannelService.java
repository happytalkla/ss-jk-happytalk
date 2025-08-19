package ht.service.channel;

import static ht.constants.CommonConstants.CHANNEL_ID_MAP;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_BOT;
import static ht.constants.CommonConstants.CONT_DIV_CD_T;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_C;
import static ht.constants.CommonConstants.CSTM_LINK_MAP;
import static ht.constants.CommonConstants.CSTM_OS_DIV_CD_MOBILE;
import static ht.constants.CommonConstants.DEPART_CD_NONE;
import static ht.constants.CommonConstants.MSG_STATUS_CD_READ;
import static ht.constants.CommonConstants.MSG_STATUS_CD_SEND;
import static ht.constants.CommonConstants.SENDER_DIV_CD_R;
import static ht.constants.CommonConstants.SENDER_DIV_CD_S;
import static ht.constants.CommonConstants.SENDER_DIV_CD_U;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.controller.CustomerController;
import ht.controller.WebSocketController;
import ht.domain.ChatContents;
import ht.domain.ChatContents.Section;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatMessageType;
import ht.domain.ChatRoom;
import ht.domain.channel.BizRequest;
import ht.domain.channel.ChannelRoom;
import ht.persistence.ChannelDao;
import ht.service.AssignService;
import ht.service.AuthService;
import ht.service.CategoryService;
import ht.service.ChatRoomService;
import ht.service.ChatService;
import ht.service.CustomerService;
import ht.service.MemberService;
import ht.service.SettingService;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChannelService {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private SimpMessageSendingOperations messagingTemplate;
	@Resource
	private ChannelDao channelDao;

	@Resource
	private CustomerService customerService;
	@Resource
	private MemberService memberService;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private AssignService assignService;
	@Resource
	private SettingService settingService;
	@Resource
	private CategoryService categoryService;
//	@Resource
//	private LegacyService legacyService;
	@Resource
	private WebSocketController webSocketController;
	@Resource
	private CustomerController customerController;
	@Resource
	private AuthService authService;
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private ObjectMapper objectMapper;

	private static final long SLEEP_FOR_WAIT_REFERENCE_MILLISECONDS = 500L;
	private static final int MAX_WAIT_CHATROOM = 4;

	public ChannelRoom selectChannelRoom(@NotEmpty String channelId, @NotEmpty String channelServiceId, @NotEmpty String channelCustomerId)
	{
		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("channelId", channelId);
		sqlParams.put("channelServiceId", channelServiceId);
		sqlParams.put("channelCustomerId", channelCustomerId);

		return channelDao.selectChannelRoom(sqlParams);
	}

	public void insertChannelRoom(ChannelRoom channelRoom) {
		channelDao.insertChannelRoom(channelRoom);
	}

	public void updateChannelRoom(ChannelRoom channelRoom) {
		channelDao.updateChannelRoom(channelRoom);
	}

	/**
	 * 채팅방 선택 및 생성
	 */
	public ChatRoom getChatRoom(@NotNull @Positive Long trackKey, @Valid BizRequest bizRequest, @NotEmpty Map<String, Object> customer
			, boolean isRelayBot, boolean fromMessageEvent)
			throws Exception
	{
		ChatRoom chatRoom = chatRoomService.selectChatRoomByChannel(customer);
		if (fromMessageEvent) {
			for (int i = 0; i < MAX_WAIT_CHATROOM; i++) {
				if (chatRoom == null) { // 메세지로 인입 되었을 경우 (카카오 상담톡, Reference 이벤트 없이)
					log.info("FIRST MESSAGE FROM CHANNEL, WAIT REFERENCE EVENT FOR {} MILLISECONDS", SLEEP_FOR_WAIT_REFERENCE_MILLISECONDS);
					TimeUnit.MILLISECONDS.sleep(SLEEP_FOR_WAIT_REFERENCE_MILLISECONDS);
					chatRoom = chatRoomService.selectChatRoomByChannel(customer);
				}
			}
		}

		// 사이트 세팅
		Map<String, Object> siteSetting = settingService.selectSiteSetting();

		// ////////////////////////////////////////////////////////////////////
		// 채널 파라미터
		// ////////////////////////////////////////////////////////////////////
		// 분류
		String ctgNum = null;
		// 부서
		String departCd = DEPART_CD_NONE;
		// 로그인 여부
		String loginYn = null;

		// 파라미터 있을 경우 사용
		if (bizRequest.getChannelParams() != null) {
			if (bizRequest.getChannelParams().get("entranceCode") != null) {
				ctgNum = (String) bizRequest.getChannelParams().get("entranceCode");
			}
			if (bizRequest.getChannelParams().get("loginYn") != null) {
				loginYn = (String) bizRequest.getChannelParams().get("loginYn");
			}
		}

		// 유효성 검증
		if (!Strings.isNullOrEmpty(ctgNum)) {
			Map<String, Object> category = settingService.selectCategory(ctgNum, false);
			if (category == null) {
				ctgNum = null;
				departCd = DEPART_CD_NONE;
			} else {
				ctgNum = (String) category.get("ctg_num");
				departCd = (String) category.get("depart_cd");
			}
		}

		// 미분류로 세팅
		if (Strings.isNullOrEmpty(ctgNum)) {
			Map<String, Object> category = categoryService.selectDefaultCategoryByChannel(CommonConstants.CSTM_LINK_DIV_CD_B);
			ctgNum = "" + category.get("ctg_num");			
			departCd = DEPART_CD_NONE;
		}

		log.info("GET CHAT ROOM: {}", chatRoom);

		// ////////////////////////////////////////////////////////////////////
		// 채팅방 선택 및 생성
		// ////////////////////////////////////////////////////////////////////
		if (chatRoom == null) {
			chatRoom = chatRoomService.createChatRoom(customer, departCd, ctgNum, null, siteSetting, isRelayBot, null, null, CSTM_OS_DIV_CD_MOBILE, loginYn, "");
			chatRoom.setIsCreatedChatRoom(true);
			chatRoom = assignService.assignChatRoom(chatRoom, siteSetting, false, isRelayBot);
		}

		log.info("GET OR CREATED CHAT ROOM: {}", chatRoom);

		return chatRoom;
	}

	/**
	 * 고객 인입 이벤트 수신
	 */
	public boolean open(@NotNull @Positive Long trackKey, @NotNull @Valid BizRequest bizRequest, @NotEmpty Map<String, Object> customer)
			throws Exception
	{
		// 채팅방 선택
		ChatRoom chatRoom = getChatRoom(trackKey, bizRequest, customer, false, false);

		// ////////////////////////////////////////////////////////////////////
		// 기간계 정보
		// ////////////////////////////////////////////////////////////////////
		String token = null;

		// 토큰 파라미터 있을 경우 사용
		if (bizRequest.getChannelParams() != null) {
			if (bizRequest.getChannelParams().get("token") != null) {
				token = (String) bizRequest.getChannelParams().get("token");
			}
		}

		if (!Strings.isNullOrEmpty(token) // 토큰이 있을 경우
				&& chatRoom.getIsCreatedChatRoom()) { // 채팅방이 새로 생성됐을 경우

//			Map<String, Object> cert = legacyService.ggggggggetCertWithToken(chatRoom, deviceType, token);
//			List<Map<String, Object>> certList = legacyService.getCertWithToken(chatRoom, CSTM_OS_DIV_CD_MOBILE, token);
//			customerController.setLegacyCustomerInfo(certList, chatRoom, customer, null, "L");
//			if (!certList.isEmpty()) {
//				Map<String, Object> legacyCustomerInfo = legacyService.getCustomerInfo(chatRoom, certList);
//				// 고객 정보 저장
//				if (legacyCustomerInfo != null
//						&& legacyCustomerInfo.get("CUST_MNNO") != null
//						&& legacyCustomerInfo.get("CUST_NM") != null) {
//					customer = customerService.updateCustomer(customer, (String) legacyCustomerInfo.get("CUST_MNNO")
//							, (String) legacyCustomerInfo.get("CUST_NM"));
//					chatRoom.setRoomCocId((String) legacyCustomerInfo.get("CUST_MNNO"));
//					chatRoom.setRoomCocNm((String) legacyCustomerInfo.get("CUST_NM"));
//					chatRoom.setAuthType("L");
//					chatRoomService.saveChatRoom(chatRoom);
//				} else {
//					log.info("LEGACY CUSTOMER, INVALID LEGACY CUSTOMER INFO: {}", legacyCustomerInfo);
//				}
//			} else {
//				log.info("LEGACY CUSTOMER, NO ACCOUNT LIST");
//			}
		} else {
			log.info("LEGACY CUSTOMER, NO TOKEN OR EXIST CHAT ROOM");
		}
		// ////////////////////////////////////////////////////////////////////
		// END: 기간계 정보
		// ////////////////////////////////////////////////////////////////////

		// 카카오톡은 바로 상담원 배정 시도
		/*if (CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
			//				&& customProperty.getIBotMemeberUid().equals(chatRoom.getMemberUid())) { // 채널 봇 (오픈빌더)과 대화중인 채팅방이면

			// 로그 채팅 메세지 저장
			if (customProperty.getIBotMemeberUid().equals(chatRoom.getMemberUid())) {
				chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_END_CHATBOT));
			}

			// 해피톡 '상담원 연결' 버튼 사용한 것 처럼 메세지 발송
			ChatContents chatContents = new ChatContents();
			chatContents.add(ChatContents.Section.builder()
					.type(ChatContents.Section.SectionType.text)
					.data("채널 고객이 상담직원 연결 요청을 했습니다.")
					.extra("HappyTalk/RequestCounselor")
					.build());

			try {
				webSocketController.message(ChatMessage.builder()
						.signature("ChatMessage")
						.type(ChatMessage.ChatMessageType.MESSAGE)
						.chatRoomUid(chatRoom.getChatRoomUid())
						.senderUid(chatRoom.getCstmUid())
						.senderDivCd(SENDER_DIV_CD_U)
						.contDivCd(CONT_DIV_CD_T)
						.cont(objectMapper.writeValueAsString(chatContents))
						.msgStatusCd(MSG_STATUS_CD_SEND)
						.build());
			} catch (UnsupportedOperationException e) {
				log.error("{}", e.getLocalizedMessage());
				return false;
			} catch (Exception e) {
				HTUtils.batmanNeverDie(e);
				log.error("{}", e.getLocalizedMessage(), e);
				return false;
			}
		}*/

		return true;
	}

	/**
	 * 고객 메세지 수신
	 */
	public boolean receiveMessage(@NotNull @Positive Long trackKey, @NotNull @Valid BizRequest bizRequest, @NotEmpty Map<String, Object> customer)
			throws Exception
	{
		// 채팅방 선택
		ChatRoom chatRoom = getChatRoom(trackKey, bizRequest, customer, false, true);

		try {
			webSocketController.message(ChatMessage.builder()
					.signature("ChatMessage")
					.type(ChatMessage.ChatMessageType.MESSAGE)
					.chatRoomUid(chatRoom.getChatRoomUid())
					.senderUid(chatRoom.getCstmUid())
					.senderDivCd(SENDER_DIV_CD_U)
					.contDivCd(CONT_DIV_CD_T)
					.cont(objectMapper.writeValueAsString(bizRequest.getChatContents()))
					.msgStatusCd(MSG_STATUS_CD_SEND)
					.build());
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
			return false;
		}

		return true;
	}

	/**
	 * 발송 메세지에 대한 콜백 수신
	 */
	public void callbackMessage(@NotNull @Positive Long trackKey, @NotEmpty String channelEventId, @NotEmpty Boolean result)
			throws Exception
	{
		if (result) {
			try {
				Long chatNum = Long.parseLong(channelEventId);
				ChatMessage chatMessage = chatService.selectChatMessageByChatNum(chatNum);
				if (chatMessage != null) {
					ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatMessage.getChatRoomUid());
					if (!MSG_STATUS_CD_READ.equals(chatMessage.getMsgStatusCd())) {
						chatMessage.setType(ChatMessage.ChatMessageType.STATUS);
						chatMessage.setSenderUid(chatRoom.getCstmUid()); // 고객이 받은 메세지를
						chatMessage.setMsgStatusCd(MSG_STATUS_CD_READ); // 읽음 처리
						webSocketController.status(chatMessage);
					}
				}
			} catch (NumberFormatException e) {
				log.error("{}", e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * 봇 대화 이력 (카카오 오픈빌더 등)
	 */
	public boolean relayBotMessage(@NotNull @Positive Long trackKey, @NotNull @Valid BizRequest bizRequest, @NotEmpty Map<String, Object> customer)
			throws Exception
	{
		// 채팅방 선택 및 생성
		ChatRoom chatRoom = getChatRoom(trackKey, bizRequest, customer, true, false);

		// 고객 메세지 저장
		if (bizRequest.getChatContents() != null) {
			ChatMessage chatMessage = ChatMessage.builder()
					.signature("ChatMessage")
					.type(ChatMessage.ChatMessageType.MESSAGE)
					.chatRoomUid(chatRoom.getChatRoomUid())
					.senderUid(chatRoom.getCstmUid())
					.senderDivCd(SENDER_DIV_CD_U)
					.contDivCd(CONT_DIV_CD_T)
					.cont(objectMapper.writeValueAsString(bizRequest.getChatContents()))
					.msgStatusCd(MSG_STATUS_CD_READ)
					.build();
			chatService.insertChatMessage(chatMessage);
			// 채팅방 마지막 메세지 세팅
			chatRoomService.setLastChatMessage(chatRoom, chatMessage);
		}

		// 봇 메세지 저장
		if (bizRequest.getBotChatContentsList() != null) {
			for (ChatContents botChatContents : bizRequest.getBotChatContentsList()) {
				ChatMessage chatMessage = ChatMessage.builder()
						.signature("ChatMessage")
						.type(ChatMessage.ChatMessageType.MESSAGE)
						.chatRoomUid(chatRoom.getChatRoomUid())
						.senderUid(chatRoom.getMemberUid())
						.cnsrMemberUid(chatRoom.getMemberUid())
						.senderDivCd(SENDER_DIV_CD_R)
						.contDivCd(CONT_DIV_CD_T)
						.cont(objectMapper.writeValueAsString(botChatContents))
						.msgStatusCd(MSG_STATUS_CD_READ)
						.build();
				chatService.insertChatMessage(chatMessage);
				// 채팅방 마지막 메세지 세팅
				chatRoomService.setLastChatMessage(chatRoom, chatMessage);
			}
		}

		// 채팅방 반영
		chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_BOT);
		chatRoomService.saveAndNoticeChatRoom(chatRoom);

		return true;
	}

	/**
	 * 세션 종료 수신
	 */
	public boolean close(@NotNull @Positive Long trackKey, @Valid BizRequest bizRequest, @NotEmpty Map<String, Object> customer)
			throws Exception
	{
		// 채팅방 선택 및 생성
		ChatRoom chatRoom = chatRoomService.selectChatRoomByChannel(customer);

		if (chatRoom != null) {
			// 메세지 전달
			ChatMessage chatMessage = ChatMessage.builder()
					.signature("ChatMessage")
					.type(ChatMessage.ChatMessageType.END)
					.chatRoomUid(chatRoom.getChatRoomUid())
					.senderUid(chatRoom.getCstmUid())
					.senderDivCd(SENDER_DIV_CD_U)
					.contDivCd(CONT_DIV_CD_T)
					.msgStatusCd(MSG_STATUS_CD_SEND)
					.build();
			try {
				webSocketController.end(chatMessage, (String) customer.get("cstm_uid"));
			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage(), e);
				return false;
			}
		}

		return true;
	}

	/**
	 * 채널로 메세지 전송
	 */
	public void sendMessage(@NotNull @Valid ChatRoom chatRoom, @NotNull @Valid ChatMessage chatMessage)
			throws RestClientException, IOException
	{
		// 오픈빌더사용중 메세지 전송 방지
		if (customProperty.getIBotMemeberUid().equals(chatRoom.getMemberUid())) {
			return;
		}
		
		Map<String, Object> customer = customerService.selectCustomerByCstmUid(chatRoom.getCstmUid());
		
		log.info("customer > > > > > > >"+customer.toString());
		
		/*if((cnt == 0) &&customer.get("cstm_link_div_cd").equals("B")) {*/
			if (customer != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.add("Channel-Id", CHANNEL_ID_MAP.get(StringUtil.nvl(customer.get("cstm_link_div_cd"))));
				headers.add("Channel-Service-Id", StringUtil.nvl(customer.get("cstm_link_service_uid")));
				headers.add("Channel-Customer-Id", StringUtil.nvl(customer.get("cstm_link_customer_uid")));
				headers.add("Channel-Event-Id", StringUtil.nvl(chatMessage.getChatNum()));
				log.info(">>>>>>>>>>>>chatMessage : {}", chatMessage);
				ChatContents chatContents = objectMapper.readValue(chatMessage.getCont(), ChatContents.class);
				//normalizeForChannel(chatContents);
				HttpEntity<ChatContents> request = new HttpEntity<>(chatContents, headers);
				
				String requestUrl ="";
				int cnt = authService.selectCheckAuth(chatRoom.getChatRoomUid().toString());
				log.info(">>>>>>>>>>>>cnt : {}", cnt);
				if((cnt > 0) && customer.get("cstm_link_div_cd").equals("B") &&("GREETING".equals(chatMessage.getBotIntent()))) {
					requestUrl = "Not Send";
				}else {
					requestUrl = customProperty.getWebhookApiUrl() + "/message";
					log.info("REQUEST URL: {}", requestUrl);
				}
				
				log.info("SEND MESSAGE, HEADER: {}, BODY: {}", headers.toString(), chatContents);
				log.info("BODY: {}", objectMapper.writeValueAsString(chatContents));
				ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
				log.info("SEND MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
			}
//		}else if(chatMessage.getBotIntent().equals("GREETING")){
//			if (customer != null) {
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				headers.add("Channel-Id", CHANNEL_ID_MAP.get(StringUtil.nvl(customer.get("cstm_link_div_cd"))));
//				headers.add("Channel-Service-Id", StringUtil.nvl(customer.get("cstm_link_service_uid")));
//				headers.add("Channel-Customer-Id", StringUtil.nvl(customer.get("cstm_link_customer_uid")));
//				headers.add("Channel-Event-Id", StringUtil.nvl(chatMessage.getChatNum()));
//				//			if (CSTM_LINK_DIV_CD_C.equals(StringUtil.nvl(customer.get("cstm_link_div_cd")))) {
//				//				// TODO: 관리
//				//				//				headers.add("Api-Key", "cNssMmoHQPC36mZ5LUy5");
//				//				headers.add("Api-Key", "x0OpGA8YRGyo9NPa62jj"); // wc6lg9
//				//			}
//				log.info(">>>>>>>>>>>>chatMessage : {}", chatMessage);
//				ChatContents chatContents = objectMapper.readValue(chatMessage.getCont(), ChatContents.class);
//				//normalizeForChannel(chatContents);
//				HttpEntity<ChatContents> request = new HttpEntity<>(chatContents, headers);
//				String requestUrl = customProperty.getWebhookApiUrl() + "/message";
//				log.debug("REQUEST URL: {}", requestUrl);
//				
//				log.info("SEND MESSAGE, HEADER: {}, BODY: {}", headers.toString(), chatContents);
//				log.info("BODY: {}", objectMapper.writeValueAsString(chatContents));
//				ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
//				log.info("SEND MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
//			}
//		}else if((cnt != 0) && (customer.get("cstm_link_div_cd").equals("B"))) {
//			return;
//		}
	}

	/**
	 * 세션 종료 송신
	 */
	public void sendClose(@NotNull @Valid ChatRoom chatRoom, @NotNull ChatContents.Command command)
			throws Exception
	{
		log.info(">> SEND CLOSE TO CHANNEL: {}", chatRoom.getCstmLinkDivCdNm());
		Map<String, Object> customer = customerService.selectCustomerByCstmUid(chatRoom.getCstmUid());
		if (customer != null) {
			// 네이버 톡톡은 메세지 전송
			if (CSTM_LINK_DIV_CD_C.equals(customer.get("cstm_link_div_cd"))
					&& !ChatContents.Command.end_slient.equals(command)) {
				Map<String, Object> siteSetting = settingService.selectSiteSetting();
				ChatMessage endMessage = chatService.buildChatMessage(chatRoom,
						ChatMessage.buildChatMessageText((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_" + "unsocial_msg")));
				endMessage.setType(ChatMessageType.END);
				endMessage.setSenderDivCd(SENDER_DIV_CD_S);
				endMessage.setSenderUid(customProperty.getSystemMemeberUid());
				sendMessage(chatRoom, endMessage);
				return;
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Channel-Id", CHANNEL_ID_MAP.get(StringUtil.nvl(customer.get("cstm_link_div_cd"))));
			headers.add("Channel-Service-Id", StringUtil.nvl(customer.get("cstm_link_service_uid")));
			headers.add("Channel-Customer-Id", StringUtil.nvl(customer.get("cstm_link_customer_uid")));

			ChatContents chatContents = new ChatContents();
			chatContents.add(Section.builder()
					.type(ChatContents.Section.SectionType.command)
					.command(command)
					.build());

			HttpEntity<ChatContents> request = new HttpEntity<>(chatContents, headers);
			String requestUrl = customProperty.getWebhookApiUrl() + "/close";
			log.info("REQUEST URL: {}", requestUrl);

			log.info("SEND CLOSE, HEADER: {}, BODY: {}", headers.toString(), chatContents);
			log.info("BODY: {}", objectMapper.writeValueAsString(chatContents));
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
			log.debug("SEND CLOSE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
		}
	}

	public Map<String, Object> getCustomer(@NotNull @Valid BizRequest bizRequest) {

		// 사용자 식별
		Map<String, Object> customer = customerService.selectCustomerByChannel(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
				bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		// 고객 식별이 불가능 할 경우 생성 및 저장
		if (customer == null) {
			customer = customerService.createCustomer(CSTM_LINK_MAP.get(bizRequest.getChannelId()),
					bizRequest.getChannelServiceId(), bizRequest.getChannelCustomerId());
		}

		return customer;
	}
}
