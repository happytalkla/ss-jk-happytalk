package ht.controller.mock;

import static ht.constants.CommonConstants.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import javax.annotation.Resource;

import ht.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.controller.WebSocketController;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatMessageType;
import ht.domain.ChatRoom;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 성능 테스트용 메세지 컨트롤러 ({@link WebSocketController} 대응)
 *
 */
@RestController
@Slf4j
public class MockWebSocketController {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private WebSocketController webSocketController;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private AssignService assignService;
	@Resource
	private CommonService commonService;
	@Resource
	private SettingService settingService;
	@Resource
	private MemberService memberService;
	@Resource
	private CustomerService customerService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private HTUtils htUtils;

	@PostMapping(path = "/api/mock/cstm")
	public ResponseEntity<ChatMessage> customer(
//			@RequestParam("chatRoomUid") String chatRoomUid,
//			@RequestParam("message") String message,
//			@RequestParam("cstmUid") String cstmUid,
//			@RequestParam("memberUid") String memberUid,
			@RequestBody @NotEmpty String requestBody) throws Exception {

		log.info("MOCK CUSTOMER, BODY: {}", requestBody);

		Map<String, String> response = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>(){});
		String chatRoomUid = response.get("chatRoomUid");
		String message = response.get("message");
		String cstmUid = response.get("cstmUid");
		String memberUid = response.get("memberUid");

		// SEND MESSAGE
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setType(ChatMessageType.MESSAGE);
		chatMessage.setChatRoomUid(chatRoomUid);
		chatMessage.setSenderUid(cstmUid);
		chatMessage.setSenderDivCd("U");
		chatMessage.setCont(ChatMessage.buildChatMessageText(message));
		chatMessage.setContDivCd(CONT_DIV_CD_T);
		chatMessage.setMsgStatusCd(MSG_STATUS_CD_SEND);

		chatMessage = webSocketController.message(chatMessage);

		// READ MESSAGE
		ChatMessage statusMessage = new ChatMessage();
		statusMessage.setType(ChatMessageType.STATUS);
		statusMessage.setChatNum(chatMessage.getChatNum());
		statusMessage.setChatRoomUid(chatRoomUid);
		statusMessage.setSenderUid(memberUid);
		statusMessage.setMsgStatusCd(MSG_STATUS_CD_READ);

		webSocketController.status(statusMessage);

		return new ResponseEntity<>(chatMessage, HttpStatus.OK);
	}

	@PostMapping(path = "/api/mock/cnsr")
	public ResponseEntity<ChatMessage> counselor(
//			@RequestParam("chatRoomUid") String chatRoomUid,
//			@RequestParam("message") String message,
//			@RequestParam("cstmUid") String cstmUid,
//			@RequestParam("memberUid") String memberUid,
			@RequestBody @NotEmpty String requestBody) throws Exception {

		log.info("MOCK COUNSELOR, BODY: {}", requestBody);

		Map<String, String> response = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>(){});
		String chatRoomUid = response.get("chatRoomUid");
		String message = response.get("message");
		String cstmUid = response.get("cstmUid");
		String memberUid = response.get("memberUid");

		// SEND MESSAGE
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setType(ChatMessageType.MESSAGE);
		chatMessage.setChatRoomUid(chatRoomUid);
		chatMessage.setSenderUid(memberUid);
		chatMessage.setSenderDivCd("C");
		chatMessage.setCont(ChatMessage.buildChatMessageText(message));
		chatMessage.setContDivCd(CONT_DIV_CD_T);
		chatMessage.setMsgStatusCd(MSG_STATUS_CD_SEND);

		chatMessage = webSocketController.message(chatMessage);

		// READ MESSAGE
		ChatMessage statusMessage = new ChatMessage();
		statusMessage.setType(ChatMessageType.STATUS);
		statusMessage.setChatNum(chatMessage.getChatNum());
		statusMessage.setChatRoomUid(chatRoomUid);
		statusMessage.setSenderUid(cstmUid);
		statusMessage.setMsgStatusCd(MSG_STATUS_CD_READ);

		webSocketController.status(statusMessage);

		return new ResponseEntity<>(chatMessage, HttpStatus.OK);
	}

	@GetMapping(path = "/api/mock/room")
	public ResponseEntity<ChatRoom> room(
			@RequestHeader HttpHeaders headers,
			Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request)
					throws Exception {

		Map<String, Object> siteSetting = settingService.selectSiteSetting();

		// ////////////////////////////////////////////////////////////////////
		// 고객 생성
		// ////////////////////////////////////////////////////////////////////
		Map<String, Object> customer = customerService.createCustomer(CSTM_LINK_DIV_CD_A, customProperty.getSiteId(), null
				, "LOADTEST", "로드테스터", CSTM_OS_DIV_CD_WEB, "4.4.4.4");

		// ////////////////////////////////////////////////////////////////////
		// 채팅방 생성
		// ////////////////////////////////////////////////////////////////////
		// 채팅방 선택 및 채팅방 생성
		ChatRoom chatRoom = chatRoomService.createChatRoom(customer, DEPART_CD_NONE, "393", null, siteSetting, false, null, "N", CSTM_OS_DIV_CD_MOBILE, "N", "");
		chatRoom = assignService.assignChatRoom(chatRoom, siteSetting, false, false);
		assignService.assignCounselorByCustomer(chatRoom, siteSetting, "C");
		if (chatRoom == null || Strings.isNullOrEmpty(chatRoom.getChatRoomUid())) {
			log.error("FAIL TO CREATE CHAT ROOM");
			return new ResponseEntity<>(new ChatRoom(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(chatRoom, HttpStatus.OK);
	}
}
