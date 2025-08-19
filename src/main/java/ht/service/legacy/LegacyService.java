package ht.service.legacy;

import static ht.constants.CommonConstants.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.domain.CMap;
import ht.domain.ChatRoom;
import ht.service.ChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Service
@Slf4j
public class LegacyService {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private ChatRoomService chatRoomService;

	/**
	 * 토큰으로 계좌 목록 요청
	 */
	@NotNull
	public List<Map<String, Object>> getCertWithToken(@NotNull ChatRoom chatRoom, String deviceType, @NotEmpty String token) {

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("roomId", chatRoom.getChatRoomUid());
		requestBody.put("uuid", chatRoom.getCstmUid());
		if (CSTM_OS_DIV_CD_WEB.equals(deviceType)) {
			requestBody.put("chnlDvcd", "HOM");
		} else {
			requestBody.put("chnlDvcd", "MW0"); // MW0: 모바일웹, MA0: 모바일앱
		}
		requestBody.put("token", token);
		return getCert(chatRoom, requestBody);
	}

	/**
	 * 'CI'로 계좌 목록 요청
	 */
	@NotNull
	public List<Map<String, Object>> getCertWithCI(@NotNull ChatRoom chatRoom, String deviceType, @NotEmpty String ci) {

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("roomId", chatRoom.getChatRoomUid());
		requestBody.put("uuid", chatRoom.getCstmUid());
		if (CSTM_OS_DIV_CD_WEB.equals(deviceType)) {
			requestBody.put("chnlDvcd", "HOM");
		} else {
			requestBody.put("chnlDvcd", "MW0"); // MW0: 모바일웹, MA0: 모바일앱
		}
		requestBody.put("ci", ci);
		return getCert(chatRoom, requestBody);
	}

	/**
	 * 기간계에 고객 기본정보 요청
	 */
	@NotNull
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getCert(@NotNull ChatRoom chatRoom, @NotNull @NotEmpty Map<String, Object> requestBody) {

		String requestUrl = customProperty.getLegacyApiUrl() + "/legacy/cert";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

		try {
			log.info("LEGACY CERT, URL: {}, HEADER: {}", requestUrl, headers.toString());
			log.info("LEGACY CERT, REQUEST BODY: {}", objectMapper.writeValueAsString(requestBody));
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
			log.info("LEGACY CERT, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
			//ParameterizedTypeReference<Map<String, Object>> map = new ParameterizedTypeReference<Map<String, Object>>() {};
			//JavaType map = objectMapper.getTypeFactory().constructParametricType(HashMap.class, String.class, String.class);
			//JavaType list = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, map.getClass());
			if (responseEntity.getBody() != null) {
				Map<String, Object> response = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>(){});
				log.info("LEGACY CERT, RESPONSE: {}", response);
				if (response != null) {
					return (List<Map<String, Object>>) response.get("resultMessage");
				}
			}
		} catch (RestClientException e) {
			log.info("LEGACY CERT, REST ERROR: {}", e.getLocalizedMessage());
		} catch (Exception e) {
			log.error("LEGACY CERT, {}", e.getLocalizedMessage(), e);
		}

		return Collections.emptyList();
	}

	/**
	 * 기간계에 고객 정보 요청 (실제는 계좌 목록 요청 후 첫번째 정보)
	 */
	public Map<String, Object> getCustomerInfo(@NotNull ChatRoom chatRoom, @NotNull List<Map<String, Object>> certList) {

		String requestUrl = customProperty.getLegacyApiUrl() + "/legacy/account_list";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> requestBody = new HashMap<>();
		List<String> rcvNoList = new ArrayList<>();
		List<String> lonNoList = new ArrayList<>();
		if (certList != null) {
			for (Map<String, Object> item : certList) {
				requestBody.put("custMnno", item.get("CUST_MNNO"));
				requestBody.put("mltLdgrYn", item.get("MLT_LDGR_YN"));
				lonNoList.add((String) item.get("LON_NO"));
				rcvNoList.add((String) item.get("LON_RCV_NO"));
			}
		}

		requestBody.put("roomId", chatRoom.getChatRoomUid());
		requestBody.put("uuid", chatRoom.getCstmUid());
		requestBody.put("custUuid", chatRoom.getCstmUid());
		requestBody.put("rcvNoList", rcvNoList);
		requestBody.put("lonNoList", lonNoList);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

		try {
			log.info("LEGACY CUSTOMER INFO, URL: {}, HEADER: {}", requestUrl, headers.toString());
			log.info("LEGACY CUSTOMER INFO, REQUEST BODY: {}", objectMapper.writeValueAsString(requestBody));
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
			log.info("LEGACY CUSTOMER INFO, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());

			if (responseEntity.getBody() != null) {
				Map<String, Object> response = objectMapper.readValue(responseEntity.getBody(), new TypeReference<CMap<String, Object>>(){});
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> accountList = (List<Map<String, Object>>) response.get("resultMessage");
				if (accountList != null && !accountList.isEmpty()) {
					Map<String, Object> legacyCustomerInfo = accountList.get(0);
					log.info("LEGACY CUSTOMER INFO, RESULT: {}", legacyCustomerInfo);
					return legacyCustomerInfo;
				}
			}
			return Collections.emptyMap();
		} catch (RestClientException e) {
			log.info("LEGACY CUSTOMER INFO, REST ERROR: {}", e.getLocalizedMessage());
		} catch (Exception e) {
			log.error("LEGACY CUSTOMER INFO, {}", e.getLocalizedMessage(), e);
		}

		return null;
	}

	/**
	 * 토큰으로 계좌 목록 요청
	 */
	public Map<String, Object> ggggggggetCertWithToken(@NotNull ChatRoom chatRoom, String deviceType, @NotEmpty String token) {

		log.info("LEGACY ACCOUNT LIST");
		return null;
	}

	/**
	 * 채팅방 종료시 기간계에 데이터 제공
	 */
	public ChatRoom postEndChatRoom(@NotNull ChatRoom chatRoom) {

		log.info("LEGACY END CHAT ROOM");

		chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoom.getChatRoomUid(), true, true);

		String requestUrl = customProperty.getLegacyApiUrl() + "/legacy/chat/end/" + chatRoom.getChatRoomUid();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ChatRoom> request = new HttpEntity<>(chatRoom, headers);

		try {
			log.info("LEGACY END CHAT ROOM, URL: {}, HEADER: {}", requestUrl, headers.toString());
			log.info("LEGACY END CHAT ROOM, REQUEST BODY: {}", objectMapper.writeValueAsString(chatRoom));
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
			log.info("LEGACY END CHAT ROOM, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
		} catch (RestClientException e) {
			log.info("LEGACY END CHAT ROOM, REST ERROR: {}", e.getLocalizedMessage(), e);
		} catch (Exception e) {
			log.error("LEGACY END CHAT ROOM, {}", e.getLocalizedMessage(), e);
		}

		return chatRoom;
	}

	/**
	 * 고객 기본정보 Mock (getCustomerInfo 태우기 위한 정규화)
	 */
	@NotNull
	public List<Map<String, Object>> getCertListFromMappingCustomerList(List<Map<String, Object>> custInfoList) {

		List<Map<String, Object>> certList = new ArrayList<>();

		for (Map<String, Object> custInfo : custInfoList) {
			Map<String, Object> cert = new HashMap<>();
			cert.put("CUST_MNNO", custInfo.get("cust_mnno"));
			cert.put("MLT_LDGR_YN", custInfo.get("mlt_ldgr_yn"));
			cert.put("LON_NO",  custInfo.get("lon_no"));
			cert.put("LON_RCV_NO",  custInfo.get("lon_rcv_no"));
			certList.add(cert);
		}

		return certList;
	}

	public String getAuthMessage(String authCode, String cnsrDivCd) {

		if (Strings.isNullOrEmpty(authCode)) {
			authCode = "00";
		}

		switch (authCode) {
		case "00":
		case "02":
		case "03":
			return "인증이 완료되었습니다.";

		case "01":
			return "고객님 죄송합니다. 당사 대출 기준과 맞지 않아 대출 진행이 어렵습니다."
					+ " 도움드리지 못해 죄송합니다.";
		case "10":
			return "인증에 실패했습니다. 다시 시도해 주세요.";

//		case "80":
//			return "신용조회동의가 완료 되었습니다.";
//		case "89":
//			return "신용조회동의가 완료되지 않았습니다.";

		case "25":
//		case "50":
//			return "대출(상담)신청이 완료되었습니다.";

		case "59": // 조회실패
			if (CNSR_DIV_CD_R.equals(cnsrDivCd)) {
				return "고객님 죄송합니다. 상담직원 연결 후 진행상태를 확인해 주세요.";
			} else {
				return "고객님 죄송합니다. 상담직원이 이어서 안내해 드리겠습니다.";
			}

//		case "98":
//			return "본인인증이 정상적으로 완료되지 않았거나 상성증권의 대출상품을"
//					+ " 이용(신청) 중인 고객님이 아닙니다.";
		case "99":
			return "인증에 실패했습니다. 다시 시도해 주세요.";

		default:
			return "해당 업무가 정상처리 되지 않았습니다.";
		}
	}
}
