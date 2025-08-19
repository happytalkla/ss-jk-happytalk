package ht.controller;

import static ht.constants.CommonConstants.API_DIV_CD_OUTSIDE;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_ASSIGN;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_WAIT_CNSR;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_WAIT_REPLY;
import static ht.constants.CommonConstants.CHNG_DIV_CD_COUNSEL;
import static ht.constants.CommonConstants.CHNG_DIV_CD_MANAGER;
import static ht.constants.CommonConstants.CNSR_DIV_CD_C;
import static ht.constants.CommonConstants.CONT_DIV_CD_T;
import static ht.constants.CommonConstants.END_DIV_CD_BOT_CSTM;
import static ht.constants.CommonConstants.LOG_CODE_ASSIGN_COUNSELOR;
import static ht.constants.CommonConstants.LOG_CODE_ASSIGN_DEPART;
import static ht.constants.CommonConstants.LOG_CODE_END_CHATBOT;
import static ht.constants.CommonConstants.LOG_CODE_MANAGER_COUNSEL;
import static ht.constants.CommonConstants.LOG_CODE_REQUEST_CHANGE_COUNSELOR;
import static ht.constants.CommonConstants.LOG_CODE_REQUEST_CHANGE_DEPART;
import static ht.constants.CommonConstants.LOG_CODE_REQUEST_COUNSELOR;
import static ht.constants.CommonConstants.LOG_DIV_CD_D;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_C;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_M;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_U;
import static ht.constants.CommonConstants.MSG_STATUS_CD_READ;
import static ht.constants.CommonConstants.SENDER_DIV_CD_S;
import static ht.constants.MessageConstants.ALREADY_END_CHATROOM;
import static ht.constants.MessageConstants.ASSIGNED;
import static ht.constants.MessageConstants.ASSIGN_CHANGED;
import static ht.constants.MessageConstants.ASSIGN_FAILED;
import static ht.constants.MessageConstants.ASSIGN_SUCCEED;
import static ht.constants.MessageConstants.CHANGE_COUNSELOR_ALREADY;
import static ht.constants.MessageConstants.CHANGE_COUNSELOR_FAILED;
import static ht.constants.MessageConstants.CHANGE_COUNSELOR_REQUEST;
import static ht.constants.MessageConstants.CHANGE_COUNSELOR_SUCCEED;
import static ht.constants.MessageConstants.CHANGE_MANAGER_ALREADY;
import static ht.constants.MessageConstants.CHANGE_MANAGER_FAILED;
import static ht.constants.MessageConstants.CHANGE_MANAGER_SUCCEED;
import static ht.constants.MessageConstants.CUSTOMER_GRADE_ALREADY;
import static ht.constants.MessageConstants.CUSTOMER_GRADE_FAILED;
import static ht.constants.MessageConstants.CUSTOMER_GRADE_SUCCEED;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatCommand;
import ht.domain.ChatMessage.ChatMessageType;
import ht.domain.ChatRoom;
import ht.domain.ChatRoom.AssignResult;
import ht.domain.MemberVO;
import ht.domain.NoticeMessage;
import ht.domain.NoticeMessage.NoticeMessageType;
import ht.domain.chatbot.ChatBotRequestMessageType;
import ht.service.AssignService;
import ht.service.CategoryService;
import ht.service.ChatRoomService;
import ht.service.ChatService;
import ht.service.CommonService;
import ht.service.CustomerService;
import ht.service.EndCtgService;
import ht.service.GenesysService;
import ht.service.McaService;
import ht.service.MemberAuthService;
import ht.service.MemberService;
import ht.service.SettingService;
import ht.service.chatbot.ChatBotService;
import ht.util.CipherUtils;
import ht.util.DateUtil;
import ht.util.ExcelView;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RestChatController {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private SimpMessageSendingOperations messagingTemplate;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private AssignService assignService;
	@Resource
	private CommonService commonService;
	@Resource
	private SettingService settingService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private EndCtgService endCtgService;
	@Resource
	private CustomerService customerService;
	@Resource
	private MemberService memberService;
//	@Resource
//	private LegacyService legacyService;
	@Resource
	@Qualifier("HappyBotServiceImpl")
	private ChatBotService chatBotService;
	@Resource
	private McaService mcaService;
	@Resource
	private GenesysService genesysService;
	@Resource
	private CipherUtils cipherUtils;
	/**
	 * 채팅방 정보(채팅 내역 포함)
	 */
	@GetMapping(path = "/api/chat/room/{chatRoomUid}")
	@ResponseBody
	public ResponseEntity<ChatRoom> getChatRoom(@PathVariable("chatRoomUid") String chatRoomUid,
			@RequestParam(value = "rollType", required = false) String rollType,
			@RequestParam(value = "withChatMessageList", required = false, defaultValue = "false") Boolean withChatMessageList,
			@RequestParam(value = "withMetaInfo", required = false, defaultValue = "false") Boolean withMetaInfo,
			@RequestParam(value = "ctgNum", required = false) String ctgNum,
			@RequestParam(value = "chatNumGt", required = false) String chatNumGt,
			@RequestParam(value = "chatNumLt", required = false) String chatNumLt,
			@RequestParam(value = "sorter", required = false, defaultValue = "chat_num+desc") String sorter,
			@RequestParam(value = "size", required = false) Integer size)
					throws Exception
	{
		log.debug("GET CHATROOM{}, {}", withChatMessageList ? " WITH CHAT MESSAGE LIST" : "", chatRoomUid);

		// 채팅방 정보
		ChatRoom chatRoom = withMetaInfo ? chatRoomService.selectChatRoomWithMetaInfoByChatRoomUid(chatRoomUid, ctgNum)
				: chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);

		if (chatRoom == null) {
			return new ResponseEntity<>(new ChatRoom(), HttpStatus.NOT_FOUND);
		}

		log.debug("CHATROOM: {}", chatRoom);

		// 채팅 내용
		if (withChatMessageList) {
			boolean withMemo = MEMBER_DIV_CD_C.equals(rollType) || MEMBER_DIV_CD_M.equals(rollType);
			boolean withLog = MEMBER_DIV_CD_M.equals(rollType);

			List<ChatMessage> chatMessageList = chatService.selectChatMessageListByChatRoomUid(chatRoomUid, chatNumGt,
					chatNumLt, size, sorter, withMemo, withLog);
			if (chatMessageList != null && chatMessageList.size() > 0) {
				for (int idx=0; idx < chatMessageList.size(); idx++) {
					ChatMessage currentMessage = chatMessageList.get(idx);
					String cont = currentMessage.getCont();
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> aaa = mapper.readValue(cont, Map.class);
					ArrayList<Map<String, Object>> balloons = (ArrayList<Map<String, Object>>) aaa.get("balloons");
					balloons.get(0);
				}
			}

			//고객 채팅일 경우 이전채팅기록을 가져온다( size = 30 )
			if(MEMBER_DIV_CD_U.equals(rollType)) {
				String cstmUid = chatRoom.getCstmUid();
				//cstmUid로 이전채팅 가져오기
				List<ChatMessage> cahtPrevMessageList = chatService.selectChatPrevMassageListBycstmUid(cstmUid,chatRoomUid);
				if(cahtPrevMessageList.size() > 0) {
					if (cahtPrevMessageList != null && cahtPrevMessageList.size() > 0) {
						for (int idx=0; idx < cahtPrevMessageList.size(); idx++) {
							ChatMessage currentMessage = cahtPrevMessageList.get(idx);
							String cont = currentMessage.getCont();
							ObjectMapper mapper = new ObjectMapper();
							Map<String, Object> aaa = mapper.readValue(cont, Map.class);
							ArrayList<Map<String, Object>> balloons = (ArrayList<Map<String, Object>>) aaa.get("balloons");
							balloons.get(0);
						}
					}
				}
				chatMessageList.addAll(cahtPrevMessageList);
			}
			chatRoom.setChatMessageList(chatMessageList);
		}
		if (withMetaInfo) {
			log.debug("{}", chatRoom);
		}

		return new ResponseEntity<>(chatRoom, HttpStatus.OK);
	}

	/**
		if (!Strings.isNullOrEmpty(cocId)) {
	 * 채팅방 정보(채팅 내역 포함) HTML 페이지
	 */
	@GetMapping(path = "/api/html/chat/room/{chatRoomUid}")
	public String getHtmlChatRoom(@PathVariable("chatRoomUid") String chatRoomUid,
			@RequestParam(value = "rollType", required = false) String rollType,
			@RequestParam(value = "withChatMessageList", required = false, defaultValue = "false") Boolean withChatMessageList,
			@RequestParam(value = "withMetaInfo", required = false, defaultValue = "false") Boolean withMetaInfo,
			@RequestParam(value = "ctgNum", required = false) String ctgNum,
			@RequestParam(value = "chatNumGt", required = false) String chatNumGt,
			@RequestParam(value = "chatNumLt", required = false) String chatNumLt,
			@RequestParam(value = "openType", required = false) String openType,
			@RequestParam(value = "sorter", required = false, defaultValue = "chat_num+desc") String sorter,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "statusMenu", required = false) String statusMenu, Model model)
					throws Exception
	{
		log.debug("GET CHATROOM{}, {}", withChatMessageList ? " WITH CHAT MESSAGE LIST" : "", chatRoomUid);

		// 채팅방 정보
		ChatRoom chatRoom = withMetaInfo ? chatRoomService.selectChatRoomWithMetaInfoByChatRoomUid(chatRoomUid, ctgNum)
				: chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
		log.info("================== chatroom : " + chatRoom);
		if (chatRoom != null) {
			model.addAttribute("chatRoom", chatRoom);
			// 채팅 내용
			if (withChatMessageList) {
				boolean withMemo = MEMBER_DIV_CD_C.equals(rollType) || MEMBER_DIV_CD_M.equals(rollType);
				boolean withLog = MEMBER_DIV_CD_M.equals(rollType);
				List<ChatMessage> chatMessageList = chatService.selectChatMessageListByChatRoomUid(chatRoomUid,
						chatNumGt, chatNumLt, size, sorter, withMemo, withLog);
				Collections.sort(chatMessageList);
				chatRoom.setChatMessageList(chatMessageList);
			}
		} else {
			model.addAttribute("chatRoom", new ChatRoom());
		}
		
		
		// 후처리 정보 조회
		Map<String, Object> chatEndInfo = chatService.selectChatEndInfoByChatRoomUid(chatRoomUid);
		if(chatEndInfo.get("updater")==null) {
			model.addAttribute("endYn", "N");
		}
		else {
			model.addAttribute("endYn", "Y");
			model.addAttribute("chatEndInfo", chatEndInfo);
		}
		// 민감정보 조회
		Map<String, Object> cstmSinfo = customerService.selectCstmSinfo(chatRoom.getChatRoomUid());
		model.addAttribute("cstmSinfo", cstmSinfo);
		log.debug("cstmSinfo: {}", cstmSinfo);

		// 기관계 내역조회
		List<Map<String, Object>> infraLogList = Collections.emptyList();
		log.debug("infraLogList: {}", infraLogList);
		model.addAttribute("infraLogList", infraLogList);

		//채널별 기본분류 조회
		/**
		 * IPCC_MCH ARS 채널 추가 관련 부서 코드 세팅
		 */
		if (CommonConstants.CSTM_LINK_DIV_CD_E.equals(chatRoom.getCstmLinkDivCd())) {
			model.addAttribute("defaultCtgNum", categoryService.selectDefaultCategoryByChannel(chatRoom.getCstmLinkDivCd(), chatRoom.getDepartCd()));
		} else {
			model.addAttribute("defaultCtgNum", categoryService.selectDefaultCategoryByChannel(chatRoom.getCstmLinkDivCd()));
		}

		log.debug("chatRoom: {}", chatRoom);
		if (StringUtil.nvl(openType, "").equals(API_DIV_CD_OUTSIDE)) {
			return "counselor/restChatRoomAPI";
		} else if (MEMBER_DIV_CD_M.equals(rollType)) {
			return "chatManage/restChatRoom";
		} else {
			return "counselor/restChatRoom";
		}
	}

	/**
	 * 채팅방 정보(다른쪽에서 페이지보기)
	 */
	@GetMapping(path = "/chatRoomView/{chatRoomUid}")
	public String getHtmlChatRoomPop(@PathVariable("chatRoomUid") String chatRoomUid,
			@RequestParam(value = "rollType", required = false) String rollType,
			@RequestParam(value = "withChatMessageList", required = false, defaultValue = "false") Boolean withChatMessageList,
			@RequestParam(value = "withMetaInfo", required = false, defaultValue = "false") Boolean withMetaInfo,
			@RequestParam(value = "ctgNum", required = false) String ctgNum,
			@RequestParam(value = "chatNumGt", required = false) String chatNumGt,
			@RequestParam(value = "chatNumLt", required = false) String chatNumLt,
			@RequestParam(value = "openType", required = false) String openType,
			@RequestParam(value = "sorter", required = false, defaultValue = "chat_num+desc") String sorter,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "statusMenu", required = false) String statusMenu, Model model)
					throws Exception
	{
		log.debug("GET CHATROOM{}, {}", withChatMessageList ? " WITH CHAT MESSAGE LIST" : "", chatRoomUid);
		rollType="C";
		withChatMessageList = true;
		withMetaInfo = true;


		// 채팅방 정보
		ChatRoom chatRoom = withMetaInfo ? chatRoomService.selectChatRoomWithMetaInfoByChatRoomUid(chatRoomUid, ctgNum)
				: chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);

		if (chatRoom != null) {
			model.addAttribute("chatRoom", chatRoom);
			// 채팅 내용
			if (withChatMessageList) {
				boolean withMemo = MEMBER_DIV_CD_C.equals(rollType) || MEMBER_DIV_CD_M.equals(rollType);
				boolean withLog = MEMBER_DIV_CD_M.equals(rollType);
				List<ChatMessage> chatMessageList = chatService.selectChatMessageListByChatRoomUid(chatRoomUid,
						chatNumGt, chatNumLt, size, sorter, withMemo, withLog);
				Collections.sort(chatMessageList);
				chatRoom.setChatMessageList(chatMessageList);
			}
		} else {
			model.addAttribute("chatRoom", new ChatRoom());
		}

		// 민감정보 조회
		Map<String, Object> cstmSinfo = customerService.selectCstmSinfo(chatRoom.getChatRoomUid());
		model.addAttribute("cstmSinfo", cstmSinfo);
		log.debug("cstmSinfo: {}", cstmSinfo);

		//기관계 내역조회
		List<Map<String, Object>> infraLogList = Collections.emptyList();
		log.debug("infraLogList: {}", infraLogList);
		model.addAttribute("infraLogList", infraLogList);

		log.debug("chatRoom: {}", chatRoom);
		return "counselor/restChatRoomPop";
	}

	/**
	 * 채팅방 변경
	 */
	@PutMapping(path = "/api/chat/room/{chatRoomUid}")
	@ResponseBody
	public ResponseEntity<NoticeMessage> putChatRoom(@PathVariable("chatRoomUid") String chatRoomUid,
			@RequestParam(value = "command") ChatCommand command,
			@RequestParam(value = "managerUid", required = false) String managerUid,
			@RequestParam(value = "memberUid", required = false) String memberUid,
			@RequestParam(value = "changeMemberUid", required = false) String changeMemberUid,
			@RequestParam(value = "departCd", required = false) String departCd,
			@RequestParam(value = "departNm", required = false) String departNm,
			@RequestParam(value = "contents", required = false) String contents,
			@RequestParam(value = "reviewReqNum", required = false) String reviewReqNum,
			@RequestParam(value = "chngReqNum", required = false) String chngReqNum,
			@RequestParam(value = "flagName", required = false) String flagName,
			@RequestParam(value = "memo", required = false) String memo,
			@RequestParam Map<String, Object> params)
					throws Exception
	{
		log.info("UPDATE CHATROOM: {}, PARAMS: {}", chatRoomUid, params);

		// 세션 사용자 정보 조회
		MemberVO sessionMemberVO = MemberAuthService.getCurrentUser();

		// 결과
		NoticeMessage noticeMessage = new NoticeMessage();
		noticeMessage.setType(NoticeMessageType.COMMAND);

		// 채팅방 정보
		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
		if (chatRoom == null) {
			return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_FOUND);
		}
		noticeMessage.setChatRoom(chatRoom);

		// 상담직원 정보
		Map<String, Object> currentMember = memberService.selectMemberByMemberUid(chatRoom.getMemberUid());
		String currentMemeberName = currentMember != null ? (String) currentMember.get("name") : chatRoom.getMemberUid();
		Map<String, Object> changeMember = null;
		if (changeMemberUid != null) {
			changeMember = memberService.selectMemberByMemberUid(changeMemberUid);
		}

		// 사이트 세팅
		Map<String, Object> siteSetting = settingService.selectSiteSetting();

		if (ChatCommand.REQUEST_COUNSELOR.equals(command)) { // 챗봇상담 중 상담직원 요청

			// 예외: 종료된 채팅방
			if ("Y".equals(chatRoom.getEndYn())) {
				log.error("END ROOM: {}", chatRoom.getChatRoomUid());
				noticeMessage.setReturnCode("ALREADY END CHATROOM");
				noticeMessage.setReturnMessage(ALREADY_END_CHATROOM);
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

			// 로그 채팅 메세지 저장
			if (customProperty.getHappyBotMemeberUid().equals(chatRoom.getMemberUid())) {
				chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_END_CHATBOT));
			}
			chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_REQUEST_COUNSELOR));

			// 상담직원 배정
			log.debug("CHAT ROOM: {}", chatRoom);
			chatRoom.setCnsrDivCd(CNSR_DIV_CD_C);
			chatRoom.setManagerUid(customProperty.getDefaultAssignMemeberUid());
			chatRoom.setMemberUid(customProperty.getDefaultAssignMemeberUid());
			assignService.assignChatRoom(chatRoom, siteSetting, true, false);

			return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);

		} else if (ChatCommand.ASSIGN_CUSTOMER_GRADE.equals(command)) { // 코끼리 등록 요청

			log.debug("ASSIGN_CUSTOMER_GRADE");
			//			Map<String, Map<String, Object>> customerGradeMap = settingService.selectCustomerGradeMap();
			//			Map<String, Object> customerGrade = customerGradeMap.get(flagName);
			//			chatRoom.setGradeNm((String) customerGrade.get("grade_nm"));
			//			chatRoom.setGradeImgClassName((String) customerGrade.get("img_class_name"));
			/* 20.05.29 상담종료 후 코끼리 등록 가능하도록 변경. yak
			 * if ("Y".equals(chatRoom.getEndYn())) { // 예외: 종료된 채팅방
			 * log.error("END ROOM: {}", chatRoom.getChatRoomUid());
			 * noticeMessage.setReturnCode("ALREADY END CHATROOM");
			 * noticeMessage.setReturnMessage(ALREADY_END_CHATROOM); return new
			 * ResponseEntity<NoticeMessage>(noticeMessage, HttpStatus.NOT_ACCEPTABLE); }
			 */
			// 로그 메세지
			String logCont = "코끼리 등록 요청 (" + chatRoom.getChatRoomUid() + "): " + currentMemeberName;

			if (assignService.hasNotPermitedChatCnsrChngReq(chatRoomUid, command)) {
				noticeMessage.setReturnCode("ALREADY REQUEST");
				noticeMessage.setReturnMessage(CUSTOMER_GRADE_ALREADY);
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}
			// 코끼리 등록 요청 이력 저장
			int result = assignService.insertChatCnsrChngReq(chatRoomUid, command, managerUid, memberUid,
					null, null, memo, flagName);
			if (result == 1) {
				noticeMessage.setReturnCode("SUCCEED");
				noticeMessage.setReturnMessage(CUSTOMER_GRADE_SUCCEED);
				chatRoomService.noticeChatRoom(chatRoom);
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
				return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
			} else {
				noticeMessage.setReturnCode("FAILED");
				noticeMessage.setReturnMessage(CUSTOMER_GRADE_FAILED);
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

		} else if (ChatCommand.MARK_CHATROOM_FLAG.equals(command)) { // 채팅방 Flag 등록

			if ("icon_flag_cancel".equals(flagName)) {
				chatRoom.setRoomMarkNum(null);
				chatRoom.setMarkImgClassName(null);
				// 채팅방 저장 및 변경 발행
				chatRoomService.updateRoomMark(chatRoom);
				chatRoomService.noticeChatRoom(chatRoom);
			} else {
				Map<String, String> roomMarkMap = settingService.selectRoomMarkMap();
				String roomMarkNum = roomMarkMap.get(flagName);
				chatRoom.setRoomMarkNum(roomMarkNum);
				chatRoom.setMarkImgClassName(flagName);
				// 채팅방 저장 및 변경 발행
				chatRoomService.saveAndNoticeChatRoom(chatRoom);
			}

			return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);

		} else if (ChatCommand.CHANGE_COUNSELOR.equals(command)) { // 상담직원 변경 요청

			if ("Y".equals(chatRoom.getEndYn())) { // 예외: 종료된 채팅방
				log.error("END ROOM: {}", chatRoom.getChatRoomUid());
				noticeMessage.setReturnCode("ALREADY END CHATROOM");
				noticeMessage.setReturnMessage(ALREADY_END_CHATROOM);
				return new ResponseEntity<NoticeMessage>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

			// 상담직원 변경, 부서별 이관 구분
			if (Strings.isNullOrEmpty(departCd)) {
				// 로그 메세지
				String logCont = "상담직원 변경 요청 (" + chatRoom.getChatRoomUid() + "): " + currentMemeberName;
				if (changeMember != null) {
					logCont +=  " > " + (String) changeMember.get("name");
				}

				// '자동 변경 승인'상태일 경우 바로 적용
				if (settingService.isAutoPermitForRequestChangeCounselor(siteSetting)) {

					log.info("REQUEST CHANGE COUNSELOR, WITH AUTO PERMIT");

					if (Strings.isNullOrEmpty(changeMemberUid)) {
						noticeMessage.setReturnCode(AssignResult.FAILED.name());
						noticeMessage.setReturnMessage("'자동 변경 승인'상태에서는 변경할 상담직원을 지정해야 합니다.");
						return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
					}

					// 상담직원 변경 요청 이력 저장 (승인 포함)
					assignService.insertChatCnsrChngReqWithApprove(chatRoomUid, command, managerUid, memberUid,
							departCd, changeMemberUid, memo);
					// 로그 채팅 메세지 저장
					chatService.insertChatMessage(
							chatService.buildLogChatMessage(chatRoom, LOG_CODE_REQUEST_CHANGE_COUNSELOR));

					AssignResult assignResult = assignService.assignCounselorDirect(chatRoom, changeMemberUid, memberUid,siteSetting);
					log.info("//////////////////////직접///////////////" + assignResult);
					noticeMessage.setReturnCode(assignResult.name());

					if (AssignResult.SUCCEED.equals(assignResult) || AssignResult.ASSIGN_CHANGED.equals(assignResult)) {

						// 상담직원 변경 이력 저장
						int result = assignService.insertChatCnsrChngHis(chatRoomUid, command, managerUid, memberUid,
								null, chatRoom.getMemberUid(), CHNG_DIV_CD_COUNSEL, MEMBER_DIV_CD_C);
						if (result == 1) {
							noticeMessage.setReturnCode("SUCCEED");
							noticeMessage.setReturnMessage(ASSIGN_SUCCEED);
							// 로그 채팅 메세지 저장
							chatService.insertChatMessage(
									chatService.buildLogChatMessage(chatRoom, LOG_CODE_ASSIGN_COUNSELOR));
							// 채팅 메세지
							ChatMessage chatMessage = chatService.saveChatMessage(chatRoom,	ChatMessage.buildChatMessageText(ASSIGN_CHANGED));

							// 채팅방 마지막 메세지 세팅
							chatRoomService.setLastChatMessage(chatRoom, chatMessage);
							//chatService.sendMessage(chatRoom, chatRoomUid, chatMessage);
							// 채팅방 미접수로 변경
							log.debug("RestChatController - putChatRoom -  CHAT_ROOM_STATUS_CD_ASSIGN");
							chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_ASSIGN);
							chatRoomService.saveAndNoticeChatRoom(chatRoom);

							// 로그 등록
							commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
							return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
						} else {
							noticeMessage.setReturnCode("FAILED");
							noticeMessage.setReturnMessage(ASSIGN_FAILED);
							// 로그 등록
							commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
							return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
						}
					} else {
						log.info("FAILED ASSIGN: {}", assignResult);
						noticeMessage.setReturnCode(assignResult.name());
						noticeMessage.setReturnMessage(ASSIGN_FAILED);
						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
						return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
					}
				} else {
					log.info("REQUEST CHANGE COUNSELOR, WITH NO AUTO PERMIT");

					if (assignService.hasNotPermitedChatCnsrChngReq(chatRoomUid, command)) {
						noticeMessage.setReturnCode("ALREADY REQUEST");
						noticeMessage.setReturnMessage(CHANGE_COUNSELOR_ALREADY);
						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
						return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
					}

					// 상담직원 변경 요청 이력 저장
					int result = assignService.insertChatCnsrChngReq(chatRoomUid, command, managerUid, memberUid,
							departCd, changeMemberUid, memo, flagName);
					// 로그 채팅 메세지 저장
					chatService.insertChatMessage(
							chatService.buildLogChatMessage(chatRoom, LOG_CODE_REQUEST_CHANGE_COUNSELOR));
					// 채팅 메세지
					ChatMessage chatMessage = chatService.saveChatMessage(chatRoom,
							ChatMessage.buildChatMessageText(CHANGE_COUNSELOR_REQUEST));
					// 채팅방 마지막 메세지 세팅
					chatRoomService.setLastChatMessage(chatRoom, chatMessage);
					chatService.sendMessage(chatRoom, chatRoomUid, chatMessage);

					if (result == 1) {
						noticeMessage.setReturnCode("SUCCEED");
						noticeMessage.setReturnMessage(CHANGE_COUNSELOR_SUCCEED);
						chatRoomService.saveAndNoticeChatRoom(chatRoom);
						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
						return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
					} else {
						noticeMessage.setReturnCode("FAILED");
						noticeMessage.setReturnMessage(CHANGE_COUNSELOR_FAILED);
						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
						return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
					}
				}
			} else {
				// 로그 메세지
				String logCont = "상담 부서 이관 요청 (" + chatRoom.getChatRoomUid() + "): " + currentMemeberName;
				if (departNm != null) {
					logCont +=  " > "+ departNm;
				}

				// '자동 변경 승인'상태일 경우 바로 적용
				if (settingService.isAutoPermitForRequestChangeCounselor(siteSetting)) {

					log.info("REQUEST CHANGE DEPART, WITH AUTO PERMIT");

					if (Strings.isNullOrEmpty(departCd)) {
						noticeMessage.setReturnCode(AssignResult.FAILED.name());
						noticeMessage.setReturnMessage("'자동 변경 승인'상태에서는 변경할 부서를 지정해야 합니다.");
						return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
					}

					String ctgNum = categoryService.selectDefaultCtgNum(departCd);
					chatRoom.setCtgNum(ctgNum);
					chatRoom.setDepartCd(departCd);
					chatRoom.setManagerUid(customProperty.getDefaultAssignMemeberUid());
					chatRoom.setMemberUid(customProperty.getDefaultAssignMemeberUid());

					// 부서 변경 요청 이력 저장 (승인 포함)
					assignService.insertChatCnsrChngReqWithApprove(chatRoomUid, command, managerUid, memberUid,
							departCd, changeMemberUid, memo);
					// 로그 채팅 메세지 저장
					chatService.insertChatMessage(
							chatService.buildLogChatMessage(chatRoom, LOG_CODE_REQUEST_CHANGE_DEPART));
					// 채팅 메세지
					ChatMessage chatMessage = chatService.saveChatMessage(chatRoom,
							ChatMessage.buildChatMessageText(CHANGE_COUNSELOR_REQUEST));
					// 채팅방 마지막 메세지 세팅
					chatRoomService.setLastChatMessage(chatRoom, chatMessage);
					chatService.sendMessage(chatRoom, chatRoomUid, chatMessage);
					// 채팅방 미배정으로 변경
					chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_CNSR);
					// 채팅방 반영
					chatRoomService.saveAndNoticeChatRoom(chatRoom);

					noticeMessage.setReturnCode("SUCCEED");
					noticeMessage.setReturnMessage(ASSIGN_SUCCEED);
					// 부서 변경 이력 저장
					assignService.insertChatCnsrChngHis(chatRoomUid, command, managerUid, memberUid,
							departCd, changeMemberUid, CHNG_DIV_CD_COUNSEL, MEMBER_DIV_CD_C);
					// 로그 채팅 메세지 저장
					chatService.insertChatMessage(
							chatService.buildLogChatMessage(chatRoom, LOG_CODE_ASSIGN_DEPART));
					// 로그 등록
					commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());

					// 배정 스케줄 등록
					settingService.saveAssignScheduler(chatRoom);

					return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);

				} else {
					log.info("REQUEST CHANGE DEPART, WITH NO AUTO PERMIT");

					if (assignService.hasNotPermitedChatCnsrChngReq(chatRoomUid, command)) {
						noticeMessage.setReturnCode("ALREADY REQUEST");
						noticeMessage.setReturnMessage(CHANGE_COUNSELOR_ALREADY);
						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
						return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
					}

					// 로그 채팅 메세지 저장
					chatService.insertChatMessage(
							chatService.buildLogChatMessage(chatRoom, LOG_CODE_REQUEST_CHANGE_DEPART));

					// 채팅 메세지
					ChatMessage chatMessage = chatService.saveChatMessage(chatRoom,
							ChatMessage.buildChatMessageText(CHANGE_COUNSELOR_REQUEST));
					// 채팅방 마지막 메세지 세팅
					chatRoomService.setLastChatMessage(chatRoom, chatMessage);
					chatService.sendMessage(chatRoom, chatRoomUid, chatMessage);
					// 상담직원 변경 요청 이력 저장
					int result = assignService.insertChatCnsrChngReq(chatRoomUid, command, managerUid, memberUid,
							departCd, changeMemberUid, null, flagName);

					if (result == 1) {
						noticeMessage.setReturnCode("SUCCEED");
						noticeMessage.setReturnMessage(CHANGE_COUNSELOR_SUCCEED);
						chatRoomService.saveAndNoticeChatRoom(chatRoom);
						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
						return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
					} else {
						noticeMessage.setReturnCode("FAILED");
						noticeMessage.setReturnMessage(CHANGE_COUNSELOR_FAILED);
						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
						return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
					}
				}
			}

		} else if (ChatCommand.MANAGER_COUNSEL.equals(command)) { // 매니저 상담 요청

			if ("Y".equals(chatRoom.getEndYn())) { // 예외: 종료된 채팅방
				log.error("END ROOM: {}", chatRoom.getChatRoomUid());
				noticeMessage.setReturnCode("ALREADY END CHATROOM");
				noticeMessage.setReturnMessage(ALREADY_END_CHATROOM);
				return new ResponseEntity<NoticeMessage>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

			// 로그 메세지
			String logCont = "매니저 상담 요청 (" + chatRoom.getChatRoomUid() + "): " + currentMemeberName;
			if (changeMember != null) {
				logCont +=  " > "+ changeMember.get("name");
			}

			if (assignService.hasNotPermitedChatCnsrChngReq(chatRoomUid, command)) {
				noticeMessage.setReturnCode("ALREADY REQUEST");
				noticeMessage.setReturnMessage(CHANGE_MANAGER_ALREADY);
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}
			// 매니저 상담 요청 이력 저장
			int result = assignService.insertChatCnsrChngReq(chatRoomUid, command, managerUid, memberUid,
					null, null, memo, flagName);
			if (result == 1) {
				noticeMessage.setReturnCode("SUCCEED");
				noticeMessage.setReturnMessage(CHANGE_MANAGER_SUCCEED);
				chatRoomService.noticeChatRoom(chatRoom);
				// 로그 채팅 메세지 저장
				chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_MANAGER_COUNSEL));
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
				return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
			} else {
				noticeMessage.setReturnCode("FAILED");
				noticeMessage.setReturnMessage(CHANGE_MANAGER_FAILED);
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

		} else if (ChatCommand.LEAVE_MANAGER_COUNSEL.equals(command)) { // 매니저 상담 나가기

			if ("Y".equals(chatRoom.getEndYn())) { // 예외: 종료된 채팅방
				log.error("END ROOM: {}", chatRoom.getChatRoomUid());
				noticeMessage.setReturnCode("ALREADY END CHATROOM");
				noticeMessage.setReturnMessage(ALREADY_END_CHATROOM);
				return new ResponseEntity<NoticeMessage>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

			assignService.updateChatCnsrChngReq(chatRoomUid, ChatCommand.MANAGER_COUNSEL, managerUid);
			chatRoomService.noticeChatRoom(chatRoom);
			return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);

		} else if (ChatCommand.REQUEST_REVIEW.equals(command)) { // 검토 요청

			if (chatService.hasNotCompletedChatContReview(chatRoomUid)) {
				noticeMessage.setReturnCode("ALREADY REQUEST");
				noticeMessage.setReturnMessage("이미 검토 요청을 했습니다.");
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			} else {
				int result = chatService.insertChatContReview(chatRoomUid, managerUid, memberUid);
				if (result == 1) {
					chatRoomService.noticeChatRoom(chatRoom);
					noticeMessage.setReturnMessage("검토 요청을 완료했습니다.");
					return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
				} else {
					noticeMessage.setReturnMessage("검토 요청을 하는데 문제가 생겼습니다. 관리자에게 문의하세요.");
					return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
				}
			}
		} else if (ChatCommand.COMPLETE_REVIEW.equals(command)) { // 검토 완료, 반려

			int result = 0;
			if (!Strings.isNullOrEmpty(reviewReqNum)) { // 검토 완료
				result = chatService.updateChatContReview(reviewReqNum, managerUid);
			} else if (!Strings.isNullOrEmpty(chngReqNum)) { // 반려
				result = assignService.updateChatCnsrChngReq(chngReqNum, managerUid);
			}
			chatRoomService.noticeChatRoom(chatRoom);
			if (result == 1) {
				if (!Strings.isNullOrEmpty(reviewReqNum)) { // 검토 완료
					noticeMessage.setReturnMessage("검토완료 처리되었습니다.");
				} else if (!Strings.isNullOrEmpty(chngReqNum)) { // 반려
					noticeMessage.setReturnMessage("요청이 반려되었습니다.");
				}
				return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_MODIFIED);
			}
		} else if (ChatCommand.HIDE_BY_CUSTOMER.equals(command)) { // 고객 채팅창 목록에서 감춤

			int result = chatRoomService.hideChatRoomByCustomer(chatRoomUid);
			if (result == 1) {
				return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_MODIFIED);
			}
		} else if (ChatCommand.SUBMIT_CHATROOM.equals(command)) { // 상담직원이 접수, 사용안함

			if ("Y".equals(chatRoom.getEndYn())) { // 예외: 종료된 채팅방
				log.error("END ROOM: {}", chatRoom.getChatRoomUid());
				noticeMessage.setReturnCode("ALREADY END CHATROOM");
				noticeMessage.setReturnMessage("이미 종료된 채팅방입니다.");
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

			if (chatRoom.getMemberUid().equals(memberUid)) {
				chatRoom.setCnsrLinkDt(commonService.selectSysdate());
				log.debug("PREV: {}", chatRoom.getChatRoomStatusCd());
				chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_REPLY);
				log.debug("NEXT: {}", chatRoom.getChatRoomStatusCd());
				if (chatRoomService.saveAndNoticeChatRoom(chatRoom)) {
					return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
				} else {
					return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_MODIFIED);
				}
			} else {
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			noticeMessage.setReturnCode("UNKNOWN COMMAND");
			noticeMessage.setReturnMessage("관리자에게 문의하세요.");
			log.error("UNKNOWN COMMAND: {}", command);
			return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_IMPLEMENTED);
		}
	}

	/**
	 * 채팅방 리스트 변경
	 */
	@PutMapping(path = "/api/chat/room")
	@ResponseBody
	public ResponseEntity<NoticeMessage> putChatRoomList(
			@RequestParam(value = "chatRoomUid[]") String[] chatRoomUidList,
			@RequestParam(value = "command") ChatCommand command,
			@RequestParam(value = "memberUid", required = false) String memberUid,
			@RequestParam(value = "changeMemberUid", required = false) String changeMemberUid,
			@RequestParam(value = "departCd", required = false) String departCd,
			@RequestParam(value = "departNm", required = false) String departNm) {

		log.debug("UPDATE CHATROOM LIST: {}", Arrays.asList(chatRoomUidList));

		// 세션 사용자 정보 조회
		MemberVO sessionMemberVO = MemberAuthService.getCurrentUser();

		// 결과
		NoticeMessage noticeMessage = new NoticeMessage();
		noticeMessage.setType(NoticeMessageType.COMMAND);
		Set<String> chatRoomUidSet = new HashSet<>(Arrays.asList(chatRoomUidList));

		// 사이트 세팅
		Map<String, Object> siteSetting = settingService.selectSiteSetting();
		// 매니저가 상담직원 변경, 매니저 상담 처리, 코끼리 등록
		if (ChatCommand.ASSIGN_COUNSELOR.equals(command)) {
			int chatRoomUidSize = chatRoomUidSet.size();
			int updateCount = 0;
			for (String chatRoomUid : chatRoomUidSet) {

				// 채팅방 정보
				ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
				if (chatRoom == null) {
					log.error("NO CHAT ROOM: {}", chatRoomUid);
					continue;
				} else if ("Y".equals(chatRoom.getEndYn())) { // 예외: 종료된 채팅방
					log.error("END CHAT ROOM: {}", chatRoom.getChatRoomUid());
					continue;
				}

				// 상담직원 정보
				Map<String, Object> currentMember = memberService.selectMemberByMemberUid(chatRoom.getMemberUid());
				String currentMemeberName = currentMember != null ? (String) currentMember.get("name") : chatRoom.getMemberUid();
				Map<String, Object> changeMember = null;
				if (changeMemberUid != null) {
					changeMember = memberService.selectMemberByMemberUid(changeMemberUid);
				}

				if (Strings.isNullOrEmpty(departCd)) { // 상담직원 변경
					log.info("REQUEST CHANGE COUNSELOR");

					// 로그 메세지
					String logCont = "상담직원 변경 승인 (" + chatRoom.getChatRoomUid() + "): " + currentMemeberName;
					if (changeMember != null) {
						logCont += " > " + changeMember.get("name");
					}

					// 배정 변경
					AssignResult assignResult = assignService.assignCounselorDirect(chatRoom, changeMemberUid, memberUid,siteSetting);
					log.info("///////////////////매니저//////////////////////////"+assignResult);
					if (AssignResult.SUCCEED.equals(assignResult) || AssignResult.ASSIGN_CHANGED.equals(assignResult)) {

						// 로그 채팅 메세지 저장
						chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_ASSIGN_COUNSELOR));

						// 채팅 메세지
						// OK 저축은행 요청으로 변경 채팅메세지 전송 차단
						  ChatMessage chatMessage = chatService.saveChatMessage(chatRoom,
						  ChatMessage.buildChatMessageText(ASSIGNED)); // 채팅방 마지막 메세지 세팅
						  chatRoomService.setLastChatMessage(chatRoom, chatMessage); // 메세지 전달
						/*
						 * chatService.sendMessage(chatRoom, chatRoomUid, chatMessage);
						 * messagingTemplate.convertAndSend( customProperty.getWsTopicPath() + "/" +
						 * chatMessage.getChatRoomUid(), chatMessage);
						 */

						// 채팅방 미접수로 변경
						log.debug("RestChatContoller - putChatRoomList - CHAT_ROOM_STATUS_CD_ASSIGN");
						chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_ASSIGN);
						/*********************************************************************************************
						 * 메세지 타입 ASSING(배정) 메세지 전송
						 *********************************************************************************************/
						ChatMessage message = new ChatMessage();
						message.setChatRoomUid(chatRoom.getChatRoomUid());
						message.setSenderDivCd(SENDER_DIV_CD_S);
						message.setSenderUid(customProperty.getSystemMemeberUid());
						message.setCnsrMemberUid(chatRoom.getMemberUid());
						message.setType(ChatMessageType.ASSIGN);
						message.setMsgStatusCd(MSG_STATUS_CD_READ);
						message.setContDivCd(CONT_DIV_CD_T);
						message.setCont(ChatMessage.buildChatMessageSectionText(Strings.nullToEmpty("ASSIGN")));
						chatService.sendMessage(chatRoom, message.getChatRoomUid(), message);
						/*********************************************************************************************/

						// 상담직원 변경 요청이 있었을 경우 완료 처리
						chatRoomService.saveAndNoticeChatRoom(chatRoom);
						if (assignService.hasNotPermitedChatCnsrChngReq(chatRoomUid, ChatCommand.CHANGE_COUNSELOR)) {
							assignService.updateChatCnsrChngReq(chatRoomUid, ChatCommand.CHANGE_COUNSELOR, memberUid);
						}
						// 채팅방 반영
						chatRoomService.saveAndNoticeChatRoom(chatRoom);
						// 상담직원 변경 이력 저장
						updateCount += assignService.insertChatCnsrChngHis(chatRoom.getChatRoomUid(), ChatCommand.CHANGE_COUNSELOR,
								memberUid, memberUid, null, chatRoom.getMemberUid(), CHNG_DIV_CD_MANAGER, MEMBER_DIV_CD_C);

						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
					} else {
						log.error("FAILED ASSIGN: {}, {}", assignResult, chatRoomUid);
						// 로그 등록
						commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, sessionMemberVO.getMemberUid());
					}
				} else { // 부서 변경
					log.info("REQUEST CHANGE DEPART");

					// 로그 메세지
					String logCont = "상담 부서 이관 승인 (" + chatRoom.getChatRoomUid() + "): " + currentMemeberName
							+ " > " + departNm;

					if (Strings.isNullOrEmpty(departCd)) {
						noticeMessage.setReturnCode(AssignResult.FAILED.name());
						noticeMessage.setReturnMessage("변경할 부서를 지정해주세요.");
						return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
					}

					String ctgNum = categoryService.selectDefaultCtgNum(departCd);
					chatRoom.setCtgNum(ctgNum);
					chatRoom.setDepartCd(departCd);
					// 채팅방 미배정으로 변경
					chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_CNSR);
					chatRoom.setManagerUid(customProperty.getDefaultAssignMemeberUid());
					chatRoom.setMemberUid(customProperty.getDefaultAssignMemeberUid());
					// 채팅방 반영
					chatRoomService.saveAndNoticeChatRoom(chatRoom);

					noticeMessage.setReturnCode("SUCCEED");
					//					noticeMessage.setReturnMessage(ASSIGN_SUCCEED);
					// 부서 변경 이력 저장
					updateCount += assignService.insertChatCnsrChngHis(chatRoomUid, ChatCommand.CHANGE_COUNSELOR, memberUid, memberUid,
							departCd, null, CHNG_DIV_CD_COUNSEL, MEMBER_DIV_CD_C);
					// 로그 채팅 메세지 저장
					chatService.insertChatMessage(
							chatService.buildLogChatMessage(chatRoom, LOG_CODE_ASSIGN_DEPART));
					// 로그 등록
					commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
					// 스케줄 등록
					settingService.saveAssignScheduler(chatRoom);
				}
			}
			if (chatRoomUidSize == updateCount) {
				noticeMessage.setReturnCode("SUCCEED");
				noticeMessage.setReturnMessage("상담직원 배정을 완료헸습니다.");
				return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
			} else {
				noticeMessage.setReturnCode("FAILED");
				noticeMessage.setReturnMessage("상담직원 배정을 완료하지 못했습니다.");
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}
		} else if (ChatCommand.MANAGER_COUNSEL.equals(command)) {
			int chatRoomUidSize = chatRoomUidSet.size();
			int updateCount = 0;
			for (String chatRoomUid : chatRoomUidSet) {

				// 채팅방 정보
				ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
				if (chatRoom != null) {
					if ("Y".equals(chatRoom.getEndYn())) { // 예외: 종료된 채팅방
						log.error("END ROOM: {}", chatRoom.getChatRoomUid());
						continue;
					}

					// 상담직원 정보
					Map<String, Object> currentMember = memberService.selectMemberByMemberUid(chatRoom.getMemberUid());
					String currentMemeberName = currentMember != null ? (String) currentMember.get("name") : chatRoom.getMemberUid();
					Map<String, Object> changeMember = null;
					if (changeMemberUid != null) {
						changeMember = memberService.selectMemberByMemberUid(changeMemberUid);
					}

					// 로그 메세지
					String logCont = "매니저 상담 승인 (" + chatRoom.getChatRoomUid() + "): " + currentMemeberName;
					if (changeMember != null) {
						logCont += " > " + currentMemeberName;
					}
					// 로그 등록
					commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
				}
			}

			if (chatRoomUidSize == updateCount) {
				noticeMessage.setReturnCode("SUCCEED");
				noticeMessage.setReturnMessage("매니저 상담요청을 수락헸습니다.");
				return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
			} else {
				noticeMessage.setReturnCode("FAILED");
				noticeMessage.setReturnMessage("매니저 상담요청을 수락할 수 없습니다.");
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

		} else if (ChatCommand.ASSIGN_CUSTOMER_GRADE.equals(command)) { // 코끼리 등록

			int chatRoomUidSize = chatRoomUidList.length;
			int updateCount = 0;

			for (String chatRoomUid : chatRoomUidList) {

				// 채팅방 정보
				ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
				Map<String, Object> request = assignService.selectChatCnsrChngReq(chatRoomUid, ChatCommand.ASSIGN_CUSTOMER_GRADE);
				log.info("CSTM GRADE REQUEST: {}", request);

				if (chatRoom != null) {
					//					if ("Y".equals(chatRoom.getEndYn())) { // 예외: 종료된 채팅방
					//						log.error("END ROOM: {}", chatRoom.getChatRoomUid());
					//						continue;
					//					} else
					if (request == null) {
						log.error("NO CSTM GRADE REQUEST: {}", chatRoom.getChatRoomUid());
						continue;
					}

					Map<String, Map<String, Object>> customerGradeMap = settingService.selectCustomerGradeMap();
					Map<String, Object> customerGrade = customerGradeMap.get(request.get("grade_nm"));
					// 고객 정보 저장
					Map<String, Object> customer = new HashMap<>();
					customer.put("cstmUid", chatRoom.getCstmUid());
					customer.put("gradeNum", customerGrade.get("grade_num"));
					customer.put("gradeMemo", request.get("memo"));
					customer.put("gradeRegMemberUid", request.get("creater")); // 요청 상담직원
					customer.put("creater", request.get("manager_uid")); // 요청시 매니저
					customer.put("cstmLinkDivCd", chatRoom.getCstmLinkDivCd()); // 인입채널 세팅
					customerService.saveCustomer(customer);

					// 고객 정보 변경 이력 저장
					Map<String, Object> customerGradeHistory = new HashMap<>();
					customerGradeHistory.put("cstmUid", chatRoom.getCstmUid());
					customerGradeHistory.put("gradeNum", customerGrade.get("grade_num"));
					//					customerGradeHistory.put("gradeMemo", gradeMemoArr[i]);
					customerGradeHistory.put("creater", request.get("manager_uid"));
					//updateCount = customerService.saveCustomerGradeHistory(customerGradeHistory);
					if(customerService.saveCustomerGradeHistory(customerGradeHistory) == 1) {
						updateCount++;
					}

					// 채팅방 반영 및 변경 발행
					//					chatRoom.setGradeMemo(gradeMemoArr[i]);

					chatRoomService.saveAndNoticeChatRoom(chatRoom);
					if (assignService.hasNotPermitedChatCnsrChngReq(chatRoomUid, command)) {
						assignService.updateChatCnsrChngReq(chatRoomUid, command, (String) request.get("manager_uid"));
					}

					// 상담직원 정보
					Map<String, Object> currentMember = memberService.selectMemberByMemberUid(chatRoom.getMemberUid());
					String currentMemeberName = currentMember != null ? (String) currentMember.get("name") : chatRoom.getMemberUid();
					Map<String, Object> changeMember = null;
					if (changeMemberUid != null) {
						changeMember = memberService.selectMemberByMemberUid(changeMemberUid);
					}

					// 로그 메세지
					String logCont = "코끼리 등록 승인 (" + chatRoom.getChatRoomUid() + "): " + currentMemeberName;
					if (changeMember != null) {
						logCont += " > " + changeMember.get("name");
					}

					// 로그 등록
					commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, sessionMemberVO.getMemberUid());
				}
				//i++;
			}

			if (chatRoomUidSize == updateCount) {
				noticeMessage.setReturnCode("SUCCEED");
				noticeMessage.setReturnMessage("코끼리 등록을 완료헸습니다.");
				return new ResponseEntity<>(noticeMessage, HttpStatus.ACCEPTED);
			} else {
				noticeMessage.setReturnCode("FAILED");
				noticeMessage.setReturnMessage("코끼리 등록을 완료하지 못했습니다.");
				return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
			}

		} else {
			noticeMessage.setReturnCode("UNKNOWN COMMAND");
			noticeMessage.setReturnMessage("관리자에게 문의하세요.");
			log.error("UNKNOWN COMMAND: {}", command);
			return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_IMPLEMENTED);
		}
	}

	/**
	 * 채팅방 목록
	 */
	@PostMapping(path = "/api/chat/room")
	@ResponseBody
	public ResponseEntity<List<ChatRoom>> getChatRoomList(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "sort", required = false, defaultValue = "chat+desc") String sort,
			@RequestParam(value = "since", required = false) String since,
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "withMetaInfo", required = false, defaultValue = "false") Boolean withMetaInfo,
			@RequestParam(value = "rollType") String rollType, HttpSession session // , @RequestParam("assigned") String
			// assigned
			) {
		log.debug("GET CHATROOM LIST: {}, {}", userId, rollType);
		List<ChatRoom> chatRoom = new ArrayList<>();

		// 채팅방 목록
		if (MEMBER_DIV_CD_U.equals(rollType)) { // 고객별 채팅방 목록
			chatRoom = chatRoomService.selectChatRoomListByCustomer(userId, true, withMetaInfo);
		} else if (MEMBER_DIV_CD_C.equals(rollType)) { // 상담사별 채팅방 목록
			chatRoom = chatRoomService.selectChatRoomListByCounselor(userId, status, page, sort, q);
		} else if (MEMBER_DIV_CD_M.equals(rollType)) { // 매니저별 채팅방 목록
			// chatRoom = chatService.selectChatRoomListByManager(userId,
			// Longs.tryParse(since));
		} else {
			// throw new UnsupportedOperationException("NO USER TYPE");
			new ResponseEntity<>(chatRoom, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(chatRoom, HttpStatus.OK);
	}

	/**
	 * 채팅방 목록 카운트
	 */
	@PostMapping(path = "/api/chat/room/count")
	@ResponseBody
	public ResponseEntity<Integer> getChatRoomListCount(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "since", required = false) String since,
			@RequestParam(value = "q", required = false) String q, @RequestParam(value = "rollType") String rollType,
			HttpSession session // , @RequestParam("assigned") String assigned
			) {
		log.debug("GET CHATROOM LIST: {}, {}", userId, rollType);

		// 채팅방 목록
		if (MEMBER_DIV_CD_U.equals(rollType)) { // 고객별 채팅방 목록
			return new ResponseEntity<>(chatRoomService.selectChatRoomListCountByCustomer(userId, true), HttpStatus.OK);
			// } else if (MEMBER_DIV_CD_C.equals(rollType)) { // 상담사별 채팅방 목록
			// // chatRoom = chatService.selectChatRoomListByCounselor(userId, status, q);
			// } else if (MEMBER_DIV_CD_M.equals(rollType)) { // 매니저별 채팅방 목록
			// // chatRoom = chatService.selectChatRoomListByManager(userId,
			// Longs.tryParse(since));
		} else {
			// throw new UnsupportedOperationException("NO USER TYPE");
			return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
		}
	}

	// @PostMapping(path = "/api/chat/room")
	// @ResponseBody
	// public ResponseEntity<ChatRoom> createChatRoom(@RequestParam(value =
	// "cstmUid") String cstmUid,
	// @RequestParam(value = "ctgNum", required = false) String ctgNum) {
	//
	// log.debug("CREATE CHATROOM");
	//
	// ChatRoom chatRoom = chatService.createChatRoom(cstmUid, ctgNum, null, true);
	// if (chatRoom == null) {
	// new ResponseEntity<List<ChatRoom>>(HttpStatus.NOT_ACCEPTABLE);
	// }
	// return new ResponseEntity<ChatRoom>(chatRoom, HttpStatus.CREATED);
	// }

	@GetMapping(path = "/api/chat/room/status/counter")
	@ResponseBody
	public ResponseEntity<Map<String, Integer>> getStatusCounter(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "rollType") String rollType,
			@RequestParam(value = "departCd") String departCd) {
		// , @RequestParam(value = "since", required = false) String since) {

		log.debug("GET CHATROOM STATUS COUNTER, {}, {}", userId, rollType);
		String pageType = null;
		if(Integer.parseInt(userId) < 1999999991 ) {
			// 매니저 페이지인 경우 실제 사용자 MEMBER_DIV_CD 값 필요 (관리자)
			if (MEMBER_DIV_CD_M.equals(rollType)) {
				MemberVO memberVO = MemberAuthService.getCurrentUser();
				if (memberVO == null) {
					log.error("NO SESSION USER, NEED LOGIN: {}", userId);
					return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.NO_CONTENT);
				} else if (!Strings.isNullOrEmpty(memberVO.getMemberDivCd())) {
					rollType = memberVO.getMemberDivCd();
				} else {
					log.error("INVALID ROLL TYPE: {}", memberVO.getMemberDivCd());
				}
			}else {
				pageType = "In";
			}
			if(!Strings.isNullOrEmpty(userId)) {
				Map<String, Object> currentMember = memberService.selectMemberByMemberUid(userId);
				departCd = currentMember.get("depart_cd").toString();
				rollType = currentMember.get("member_div_cd").toString();
			}
			
			Map<String, Integer> statusCounter = chatRoomService.getStatusCounter(userId, rollType, departCd, pageType);
			log.debug("statusCounter: {}", statusCounter);
			if (statusCounter.isEmpty()) {
				return new ResponseEntity<>(statusCounter, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(statusCounter, HttpStatus.OK);
			}
		}
		else {
			return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.NO_CONTENT);
		}
	}

	@PostMapping("/api/chat/room/upload/{chatRoomUid}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile file,
			@PathVariable("chatRoomUid") String chatRoomUid, @RequestParam("userId") String userId) {

		Map<String, Object> uploadFile = commonService.upload(file, chatRoomUid, userId, "chat");

		if (uploadFile == null) {
			uploadFile = new HashMap<>();
			uploadFile.put("resultMessage", "UNKNOWN ISSUE");
			return new ResponseEntity<>(uploadFile, HttpStatus.NOT_ACCEPTABLE);
		}

		// return new ResponseEntity<Map<String, Object>>(uploadFile,
		// HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return new ResponseEntity<>(uploadFile, HttpStatus.CREATED);
	}

	@PostMapping("/api/chat/room/review/{chatRoomUid}")
	@ResponseBody
	public ResponseEntity<Integer> review(@PathVariable("chatRoomUid") String chatRoomUid,
			@RequestParam("userId") String userId, @RequestParam("star") Integer star,
			@RequestParam("statement") String statement) {

		int result = chatService.saveCnsEvl(chatRoomUid, userId, star, statement);

		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	/**
	 * 상담내역 후처리 팝업 페이지
	 * @param modifyChk
	 */
	@GetMapping("/api/chat/room/endReview")
	public String getEndReview(String jSessionId, HttpServletResponse response, HttpSession session,
			Model model, @RequestParam Map<String, Object> param) throws Exception {

		log.info("getEndReview: {}", param);

		// TODO: 임시, 종료저장되기전 가져오는 경우 에러
		TimeUnit.SECONDS.sleep(1L);

		// 고객 정보
		Map<String, Object> customer = customerService.selectCustomerByCstmUid(param.get("cstmUid").toString());
		model.addAttribute("customer", customer);

		String chatRoomUid = param.get("chatRoomUid").toString();
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		param.put("departCd", memberVO.getDepartCd());
		String memberDivCd = memberVO.getMemberDivCd();
		// 매니저 등록(R) 수정(E)
		// 상담직원 등록(R) 조회(V)
		String endReviewMode = "W";
		String modifyChk = (String) param.get("modifyChk");

		// 기존 정보 있으면 조회
		Map<String, Object> chatEndInfo = chatService.selectChatEndInfoPop(chatRoomUid);
		// TODO: 임시, 종료저장되기전 가져오는 경우 오류 방지
		for (int i = 0; i < 4; i++) {
			if (chatEndInfo == null) {
				TimeUnit.MILLISECONDS.sleep(500L);
				chatEndInfo = chatService.selectChatEndInfoPop(chatRoomUid);
			}
		}
		//List<Map<String, Object>> categoryList1 = Collections.emptyList();
		//List<Map<String, Object>> categoryList2 = Collections.emptyList();
		//List<Map<String, Object>> categoryList3 = Collections.emptyList();
		//List<Map<String, Object>> categoryList4 = Collections.emptyList();
		if(chatEndInfo.get("updater") != null) {		// 기존등록정보 있음
			if("M".equals(memberDivCd)) endReviewMode = "E";	//매니저일 경우 수정
			else endReviewMode = "V";
		}

		if(!"V".equals(endReviewMode) ) {
			// 분류 (1-Depth) 초기 리스트 2분류초기리스트로 변경 고정은 오키톡코드값
			// categoryList1 = categoryService.selectEndCategoryList("1");
			// 분류 (2-Depth)
			//String dep_1_ctg_cd = "00017";

			//categoryList2 = categoryService.selectEndCategoryListByCtgCd(dep_1_ctg_cd, "2");


			if ("E".equals(endReviewMode)) {
				// 분류 (3-Depth)
				/*
				 * if(chatEndInfo.get("dep_2_ctg_cd") != null) { categoryList3 =
				 * categoryService.selectEndCategoryListByCtgCd(chatEndInfo.get("dep_2_ctg_cd").
				 * toString(), "3"); } // 분류 (4-Depth) if(chatEndInfo.get("dep_3_ctg_cd") !=
				 * null) { categoryList4 =
				 * categoryService.selectEndCategoryListByCtgCd(chatEndInfo.get("dep_3_ctg_cd").
				 * toString(), "4"); }
				 */

				log.info("chatEndInfo: {}", chatEndInfo);

				if (chatEndInfo.get("updater") != null) { // 후처리 내역 존재시
					if (!"E".equals(modifyChk) ) { // 상담직원 읽기모드
						modifyChk = "B";
					}
				}
				else { // 후처리 내역 미 존재시
					modifyChk = "W";
				}
			}
		}
		System.out.println("*************************************************************************************"+endReviewMode);
		/*
		 * model.addAttribute("categoryList1", categoryList1);
		 * model.addAttribute("categoryList2", categoryList2);
		 * model.addAttribute("categoryList3", categoryList3);
		 * model.addAttribute("categoryList4", categoryList4);
		 */
		model.addAttribute("endReviewMode", endReviewMode);
		model.addAttribute("chatEndInfo", chatEndInfo);

		return "counselor/restEndReview";
	}

	/**
	 * 상담내역 후처리 등록
	 */
	@PutMapping("/api/chat/room/endReview")
	public @ResponseBody Map<String, Object>  putEndReview(@RequestParam Map<String, Object> params) {

		log.info("END REVIEW: {}", params);
		String memo = "" + params.get("memo");
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		String chatRoomUid = (String) params.get("chatRoomUid");
		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
		//후처리 등록자 정보
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		params.put("creater", memberVO.getMemberUid());
		params.put("updater", memberVO.getMemberUid());

		// 후처리 있는지 확인
		//boolean isExist = false;
		Map<String, Object> endInfo = chatService.selectChatEndInfoPop(chatRoomUid);
		log.info("END REVIEW, EXIST: {}", endInfo);
		//if (endInfo != null && endInfo.get("dep_2_ctg_cd") != null) {
		//	isExist = true;
		//}
		
		int result = 0;
		//후처리 기간계 저장 시작
		String entityId = "미로그인".equals(chatRoom.getCstmCocId())? "" : chatRoom.getCstmCocId();
		Map<String, Object> resMap = new HashMap<>();
		try {
			if (endInfo.get("CNSL_PRHS_ID") == null) {
				//sectCode I : 추가
				params.put("sectCode", "I");
			}else {
				//sectCode U : 수정
				params.put("sectCode", "U");
				params.put("mcaNum", endInfo.get("CNSL_PRHS_ID"));
			}

			params.put("memo", memo);
			resMap = sendReviewToRegacy(chatRoom.getCstmLinkDivCd(), entityId, params);
			params.put("mcaNum", resMap.get("mcaNum"));
			log.info(" putEndReview END REVIEW, resMap: {}", resMap);

			/**
			 * IPCC_ADV 고객 여정 I/F
			 * 기간계 상담이력ID, 처리구분코드 추가로 인해 기간계 전송 완료 후 전송
			 * 채팅내용 포함
			 */
			params.put("entityId", entityId);
			params.put("cstmLinkDivCdNm", chatRoom.getCstmLinkDivCdNm());
			params.put("cnsrNm", chatRoom.getMemberName());
			List<Map<String, Object>> chatList = genesysService.selectChatList(params);
			String tempChat = "";
			int tempChatLen = 0;
			if(chatList.isEmpty()) {
				params.put("chatContText", "");
			}else {
				for (Map<String, Object> tempChatList : chatList) {
					tempChatLen = tempChat.getBytes("utf-8").length; // 이전 채팅 길이
					tempChatLen += tempChatList.get("reg_dt").toString().length(); // 시간 길이
					tempChatLen +=  tempChatList.get("cont_text").toString().getBytes("utf-8").length; // 채팅 길이
					if(tempChatLen > 3998) { // 글자 4000byte 제한 추가
						break;
					}else {
						if(!tempChat.isEmpty()) {
							tempChat += "|";
						}
						tempChat += tempChatList.get("reg_dt");
						tempChat += "|";
						tempChat += tempChatList.get("cont_text");						
					}
				}
				tempChat += "||";
				params.put("chatContText", tempChat);
			}

			if (!genesysService.sendGenesys(params)) {
				result = -20;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("ERROR ON SAVING REVIEW TO LEGACY :  {}", e.toString());
			result = -10;
		}
		//후처리 기간계 저장 종료
		
		// 후처리 디비 저장
		result = chatService.saveChatEndInfo(params);
		assert (result == 1);

		if (result > 0) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 저장되었습니다.");
		} else if (result == -10) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "기간계 처리중 오류가 발생하였습니다.");
		} else if (result == -20) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "고객여정 처리중 오류가 발생하였습니다.");
		} else {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
		}
		return rtnMap;
	}

	private Map<String, Object> sendReviewToRegacy(String channel, String entityId, Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<>();

		if (CommonConstants.CSTM_LINK_DIV_CD_B.equals(channel)) { // 카카오 채널인 경우
			// 내용에 대분류 / 중분류 / 소분류 추가하여 보냄

			String dash = " / ";

			/*
			 * String cont = "" +params.get("dep1CtgNm") + dash; cont +=
			 * params.get("dep2CtgNm") + dash; cont += params.get("dep3CtgNm") + dash; cont
			 * += "\n" + params.get("memo");
			 *
			 * params.put("memo" , cont);
			 */

			result = mcaService.sgd0038p(entityId, params);
		} else {
			// 카테고리 번호가 3자리이상인 경우 잘라서 사용함 , 기간계는 2자리만 받음
			String dep1 = "" + StringUtil.nvl(params.get("dep1CtgNm"));
			String dep2 = "" + StringUtil.nvl(params.get("dep2CtgNm"));
			String dep3 = "" + StringUtil.nvl(params.get("dep3CtgNm"));

			// 카테고리 이름을 .을 기준으로 잘라서 사용함, 기간계는 2자리만 받음
			if (!Strings.isNullOrEmpty(dep1)) {
				dep1 = dep1.split("\\.")[0];
			}
			if (!Strings.isNullOrEmpty(dep2)) {
				dep2 = dep2.split("\\.")[0];
			}
			if (!Strings.isNullOrEmpty(dep3)) {
				dep3 = dep3.split("\\.")[0];
			}

			params.put("dep1CtgCd", dep1);
			params.put("dep2CtgCd", dep2);
			params.put("dep3CtgCd", dep3);
			params.put("channel", channel);

			log.info("new params ====>>>>>>>>>>> {}" + params);
			result = mcaService.sge1011p(entityId, params);
		}

		return result;
	}

	/**
	 * 챗봇에 메세지 요청 (고객 메세지 없이)
	 */
	@PostMapping("/api/chat/withbot")
	@ResponseBody
	public ResponseEntity<NoticeMessage> chatWithBot(@RequestParam("chatRoomUid") String chatRoomUid,
			@RequestParam("userId") String userId, @RequestParam("keyword") String keyword,
			@RequestParam("page") Integer page)
					throws Exception
	{
		// 결과
		NoticeMessage noticeMessage = new NoticeMessage(NoticeMessageType.CHAT_MESSAGE);

		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
		log.debug("chatRoom: {}", chatRoom);
		if (chatRoom == null) {
			log.error("NOT FOUND ROOM: {}", chatRoomUid);
			noticeMessage.setReturnCode("NOT FOUND ROOM");
			noticeMessage.setReturnMessage("채팅방을 찾을 수 없습니다.");
			return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
		} else if ("Y".equals(chatRoom.getEndYn())) {
			log.error("END ROOM: {}", chatRoom.getChatRoomUid());
			noticeMessage.setReturnCode("END ROOM");
			noticeMessage.setReturnMessage("종료된 채팅방입니다.");
			return new ResponseEntity<>(noticeMessage, HttpStatus.NOT_ACCEPTABLE);
		}

		// 메세지 요청
		ChatMessage requestChatMessage = new ChatMessage();
		requestChatMessage.setChatRoomUid(chatRoomUid);
		requestChatMessage.setSenderUid(userId);
		requestChatMessage.setCont(ChatMessage.buildChatMessageText(keyword));
		ChatMessage message = chatBotService.chatWithRobot(chatRoom, requestChatMessage,
				ChatBotRequestMessageType.QUESTION.name(), page).get(0);

		// 메세지 저장
		chatService.insertChatMessage(message);
		chatRoomService.saveLastChatMessage(chatRoom, message);
		chatRoomService.saveChatRoom(chatRoom);

		// 메세지 전달
		messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatRoomUid, message);

		noticeMessage.setChatMessage(message);
		return new ResponseEntity<>(noticeMessage, HttpStatus.OK);
	}

	/**
	 * 메세지 편집
	 */
	@PutMapping("/api/chat/{chatNum}")
	@ResponseBody
	public ResponseEntity<Integer> putChatMessage(@PathVariable("chatNum") @NotNull @Positive Long chatNum,
			@RequestParam(value = "type") ChatMessageType type, @RequestParam(value = "userId") String userId,
			@RequestParam(value = "cont", required = false) String cont)
					throws Exception
	{
		if (ChatMessageType.EDIT.equals(type)) { // 메세지 수정
			// 메세지 저장
			ChatMessage chatMessage = chatService.selectChatMessageByChatNum(chatNum);
			if (chatMessage == null) {
				return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
			}
			chatMessage.setCont(ChatMessage.buildChatMessageText(cont));
			chatMessage.setCommandContents(cont);
			int result = chatService.updateChatMessage(chatMessage);
			assert (result == 1);

			// 메세지 전달
			chatMessage.setType(ChatMessageType.EDIT);
			messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatMessage.getChatRoomUid(),
					chatMessage);

			// 로그 메세지
			String logCont = "메세지(" + chatNum + ") 변경: " + cont;

			if (result > 0) {
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, userId);
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, userId);
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}
		} else if (ChatMessageType.REMOVE.equals(type)) {
			// 메세지 삭제
			ChatMessage chatMessage = chatService.selectChatMessageByChatNum(chatNum);
			if (chatMessage == null) {
				return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
			}
			int result = chatService.deleteChatMessage(chatNum);

			// 메세지 전달
			chatMessage.setType(ChatMessageType.REMOVE);
			messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatMessage.getChatRoomUid(),
					chatMessage);

			// 로그 메세지
			String logCont = "메세지(" + chatNum + ") 삭제";

			if (result > 0) {
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "성공", logCont, userId);
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				// 로그 등록
				commonService.insertLog(LOG_DIV_CD_D, "실패", logCont, userId);
				return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			return new ResponseEntity<>(0, HttpStatus.NOT_ACCEPTABLE);
		}
	}

	/**
	 * 종료 분류
	 */
	@GetMapping("/api/category/end/{upperCtgNum}")
	public String getEndCategory(@PathVariable("upperCtgNum") String upperCtgNum, @RequestParam("depth") String depth,
			Model model)
					throws Exception
	{
		String depthnm = "대";
		if("2".equals(depth)) {
			depthnm = "중";
		}else if("3".equals(depth)) {
			depthnm = "소";
		}

		// 종료 분류
		Map<String, Object> params = new HashMap<>();
		params.put("upperCtgNum", upperCtgNum);
		params.put("ctgDpt", depth);
		List<Map<String, Object>> categoryList = endCtgService.selectCategoryList(params);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("depth", depth);
		model.addAttribute("depthnm", depthnm);
		return "counselor/restEndCategoryList";
	}
	/**
	 * 종료 분류 이름검색
	 */
	@GetMapping("/api/category/end/search")
	public String getEndCategorySearch(@RequestParam("srcKeyword") String srcKeyword, @RequestParam("depth") String depth, Model model)
					throws Exception
	{
		// 종료 분류
		List<Map<String, Object>> categoryList = endCtgService.selectEndCategoryListByCtgNm(srcKeyword, Integer.parseInt(depth));
		model.addAttribute("categoryList", categoryList);


		return "counselor/restEndCategorySrcList";
	}

	/**
	 * TODO: DELETEME, 임시오픈시만 사용 챗봇 답변 평가
	 */
	@PostMapping("/api/chat/evl/{chatNum}")
	@ResponseBody
	public ResponseEntity<Integer> evlChatMessage(@PathVariable("chatNum") Long chatNum,
			@RequestParam(value = "evl") String evl)
					throws Exception
	{
		int result = chatService.evlChatMessage(chatNum, evl);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * 챗봇 대화 목록
	 */
	@RequestMapping(value = { "/api/html/chatWithBot", "/api/html/chatWithBot2" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String chatWithBot(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param)
					throws Exception
	{
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {

			Map<String, Object> nowDate = commonService.selectNowDate();
			String nowYear = String.valueOf(nowDate.get("now_year"));
			String nowMonth = String.valueOf(nowDate.get("now_month"));
			String nowDay = String.valueOf(nowDate.get("now_day"));
			String strNowDate = nowYear + "-" + nowMonth + "-" + nowDay;

			// 파라미터 초기화
			String startDate = StringUtil.nvl(param.get("startDate"), strNowDate);
			String endDate = StringUtil.nvl(param.get("endDate"), strNowDate);

			String typeMEMBER = StringUtil.nvl(param.get("typeMEMBER"), "N");
			String typeFP = StringUtil.nvl(param.get("typeFP"), "N");
			if ("N".equals(typeMEMBER) && "N".equals(typeFP)) {
				typeMEMBER = "Y";
				typeFP = "Y";
			}

			String intentDefault = StringUtil.nvl(param.get("intentDefault"), "N");
			String intentOther = StringUtil.nvl(param.get("intentOther"), "N");
			if ("N".equals(intentDefault) && "N".equals(intentOther)) {
				intentDefault = "Y";
				intentOther = "Y";
			}

			param.put("startDate", startDate);
			param.put("endDate", endDate);
			param.put("typeMEMBER", typeMEMBER);
			param.put("typeFP", typeFP);
			param.put("intentDefault", intentDefault);
			param.put("intentOther", intentOther);

			model.put("startDate", startDate);
			model.put("endDate", endDate);
			model.put("typeMEMBER", typeMEMBER);
			model.put("typeFP", typeFP);
			model.put("intentDefault", intentDefault);
			model.put("intentOther", intentOther);

			model.put("chatList", chatService.selectChatWithBot2(param));

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "reporting/chatWithBot2";
	}

	/**
	 * 챗봇과 대화 이력 (엑셀용)
	 */
	@GetMapping("/api/html/chatWithBot/export")
	public ModelAndView chatWithBotForExport(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate, HttpServletResponse response,
			ModelMap model, Map<String, Object> ModelMap)
					throws Exception
	{
		if (Strings.isNullOrEmpty(startDate)) {
			startDate = new DateTime().toString("yyyy-MM-dd");
		}

		if (Strings.isNullOrEmpty(endDate)) {
			endDate = startDate;
		}

		List<Map<String, Object>> list = chatService.selectChatWithBotForExcel(startDate, endDate);
		log.info("list size: {}", list.size());

		try {
			// 엑셀파일명
			String target = "CHAT_WITH_BOT_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, StandardCharsets.UTF_8.name());

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<>();

			ModelMap.put("sheetName", "상담직원 통계");
			Map<String, Object> map = new HashMap<>();
			map.put("title", "질문시간");
			map.put("data", "reg_dt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "인텐트");
			map.put("data", "bot_intent");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "평가");
			map.put("data", "evl_type_cd");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "질문");
			map.put("data", "cont_text");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "답변");
			map.put("data", "cont");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			ModelMap.put("dataList", list);

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return new ModelAndView(new ExcelView(), model);
	}
	/**
	 * 카카오 인증정보 삭제 랜딩 페이지
	 */
	@CrossOrigin
	@GetMapping(path = "/api/chat/room/kakao/cstmExpire")
	@ResponseBody
	public ResponseEntity<Integer> cstmInfoExpire(@RequestParam("cstmUid") @NotEmpty String cstmUid	, @RequestParam("chatRoomUid") @NotEmpty String chatRoomUid	, Model model) {
		int result = 0;
		try {
			log.info("********************** chatRoomUid : " + chatRoomUid);
			
			// 고객정보 삭제
			if (cstmUid != null) {
				result = customerService.deleteCustomerKakao(cstmUid);
				log.info("cstmExpire > cstmUid : " + cstmUid);
			}
			if (chatRoomUid != null) {
				ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
				chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_BOT_CSTM, customProperty.getSystemMemeberUid());
				log.info("endChatRoom > chatRoomUid : " + chatRoomUid);
			}

			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		}
	}
}
