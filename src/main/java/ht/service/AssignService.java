package ht.service;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.ChatContents;
import ht.domain.ChatContents.Section;
import ht.domain.ChatContents.Section.SectionType;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatCommand;
import ht.domain.ChatMessage.ChatMessageType;
import ht.domain.chatbot.ChatBotRequestMessageType;
import ht.domain.ChatRoom;
import ht.persistence.BatchDao;
import ht.persistence.ChatDao;
import ht.service.channel.ChannelService;
import ht.service.chatbot.ChatBotService;
import ht.util.DateUtil;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.*;
import java.util.stream.Collectors;

import static ht.constants.CommonConstants.*;
import static ht.constants.MessageConstants.*;
import static ht.constants.MessageConstants.ASSIGN_NOT_ACCEPT;

@Service
@Slf4j
public class AssignService {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private SimpMessageSendingOperations messagingTemplate;

	@Resource
	private ChatDao chatDao;
	@Resource
	private BatchDao batchDao;
	@Resource
	private MemberService memberService;
	@Resource
	private SettingService settingService;
	@Resource
	private CommonService commonService;
	@Resource
	private ManageService manageService;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private AssignService assignService;
	@Resource
	private ChannelService channelService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private CustomerService customerService;
	@Resource
	private AuthService authService;

	@Resource
	@Qualifier("HappyBotServiceImpl")
	private ChatBotService chatBotService;
	//	@Resource
	//	private WebSocketController webSocketController;
	@Resource
	private ObjectMapper objectMapper;

	private String getCtgNumForKakao(@NotNull ChatRoom chatRoom) throws Exception {
		String ctgNum = "";
		Map<String, Object> customer = customerService.selectCustomerByChatRoom(chatRoom.getChatRoomUid());
		int cnt = authService.selectCheckAuth(chatRoom.getChatRoomUid());

		String kakaoCrtfc = "";
		log.info("Kakao===>>> customer : {}, cnt: {}" , customer, cnt);
		if((cnt > 0) && customer != null && !Strings.isNullOrEmpty((String)customer.get("KAKAO_CRTFC"))) {
			kakaoCrtfc = "" + customer.get("KAKAO_CRTFC");
		}

		log.info("Kakao cert code , kakaoCrtfc : {}", kakaoCrtfc);
		if(kakaoCrtfc != null && !"".equals(kakaoCrtfc)&& !"N|N".equals(kakaoCrtfc)) {
				Map<String, Object> defaultCategory = new HashMap<>();
				if("Y|N".equals(kakaoCrtfc)) {
					 defaultCategory = categoryService.selectDefaultCategoryByChannel(chatRoom.getCstmLinkDivCd(), "DC");
				}else {
					 defaultCategory = categoryService.selectDefaultCategoryByChannel(chatRoom.getCstmLinkDivCd(), "FM");
				}

				ctgNum = (String) defaultCategory.get("ctg_num");

				log.info("Kakao reqeust counselor, defaultCategory : {}", defaultCategory);

		}else {
			log.info("Kakao 인증코드 값이 N|N, kakaoCrtfc : {}, ctgNum : {}", kakaoCrtfc, ctgNum);
		}

		return ctgNum;
	}

	/**
	 * 채팅중 배정 변경
	 */
	@Transactional
	public ChatRoom assignChatRoom(@NotNull ChatRoom chatRoom, @NotEmpty Map<String, Object> siteSetting)
			throws Exception
	{
		return assignChatRoom(chatRoom, siteSetting, true, false);
	}

	/**
	 * 배정
	 *
	 * @param isChatting 채팅 진행중 (true: 소켓에 연결 되어있을 경우 소켓으로 메세지 전송, false: 페이지 로드시에는 DB 에만 반영)
	 */
	@Transactional
	public ChatRoom assignChatRoom(@NotNull ChatRoom chatRoom, @NotEmpty Map<String, Object> siteSetting, boolean isChatting)
			throws Exception
	{
		return assignChatRoom(chatRoom, siteSetting, isChatting, false);
	}

	/**
	 * 배정
	 */
	@Transactional
	public ChatRoom assignChatRoom(@NotNull ChatRoom chatRoom, @NotEmpty Map<String, Object> siteSetting, boolean isChatting, boolean isRelayBot)
			throws Exception
	{
		//채널별 기본분류 조회
//		Map<String, Object> defaultCategory = categoryService.selectDefaultCategory();
		Map<String, Object> defaultCategory = new HashMap<String, Object>();
		if (CommonConstants.CSTM_LINK_DIV_CD_E.equals(chatRoom.getCstmLinkDivCd())) {
			defaultCategory = categoryService.selectDefaultCategoryByChannel(chatRoom.getCstmLinkDivCd(), chatRoom.getDepartCd());
		} else {
			defaultCategory = categoryService.selectDefaultCategoryByChannel(chatRoom.getCstmLinkDivCd());
		}

		Assert.notNull(defaultCategory, "NO DEFAULT CATEGORY");
		String defaultCtgNum = (String) defaultCategory.get("ctg_num");
		Assert.notNull(defaultCategory, "NO CATEGORY SEQUENCE");

//		Map<String, Object> tmDefaultCategory = categoryService.selectDefaultCategory(DEPART_CD_TM);
//		Assert.notNull(tmDefaultCategory, "NO DEFAULT CATEGORY");
//		String tmDefaultCtgNum = (String) tmDefaultCategory.get("ctg_num");
//		Assert.notNull(tmDefaultCategory, "NO CATEGORY SEQUENCE");

		log.debug("CNSR DIV CD: {}", chatRoom.getCnsrDivCd());
		log.debug(chatRoom.getCstmLinkDivCd() + " DEFAULT CTGNUM: {}", defaultCtgNum);
//		log.debug("TM DEFAULT CTGNUM: {}", tmDefaultCtgNum);

		// ////////////////////////////////////////////////////////////////////
		// 분류봇 배정
		// ////////////////////////////////////////////////////////////////////
		/*
		if (CNSR_DIV_CD_C.equals(chatRoom.getCnsrDivCd()) // 분류봇 배정 (상담원 연결시 분류가 미분류 이면)
				&& (defaultCtgNum.equals(chatRoom.getCtgNum())
						// || tmDefaultCtgNum.equals(chatRoom.getCtgNum()) // TM 미분류 포함
						)
				) {
			//				&& isChatting) {

			log.info("CONDITION: ASSIGN CATEGORY BOT");

			// 근무시간 판별
			ChatRoom.AssignResult assignResult = assignService.tryAssignCounselorByCustomer(chatRoom, siteSetting);
			if (!ChatRoom.AssignResult.SUCCEED.equals(assignResult)) {

				// 메세지 전달
				ChatMessage endMessage = chatService.buildChatMessage(chatRoom,
						ChatMessage.buildChatMessageText((String) siteSetting.get("unsocial_msg")));
				endMessage.setType(ChatMessageType.END);
				endMessage.setSenderDivCd(SENDER_DIV_CD_S);
				endMessage.setSenderUid(customProperty.getSystemMemeberUid());
				chatService.insertChatMessage(endMessage);

				// chatService.sendMessage(chatRoom, chatRoom.getChatRoomUid(), endMessage);
				// 소켓으로 메세지 발송 (채널은 과금될 수 있으므로 발송안함)
				messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatRoom.getChatRoomUid(), endMessage);
				// 채팅방 종료
				chatRoomService.endChatRoom(chatRoom, END_DIV_CD_CENTER_OFF_DUTY, customProperty.getSystemMemeberUid());
			} else {
				assignResult = assignChatBot(chatRoom, customProperty.getCategoryBotMemeberUid());
				List<ChatMessage> responseChatMessageList = new ArrayList<>();

				if (ChatRoom.AssignResult.SUCCEED.equals(assignResult)) {
					// 인사말 메세지 (분류 선택 메세지)
					responseChatMessageList = chatService.addGreetMessage(chatRoom, siteSetting, chatRoom.getCnsrDivCd());
					// 변경 이력 저장
					insertChatCnsrChngHis(chatRoom.getChatRoomUid(), ChatCommand.CHANGE_COUNSELOR, customProperty.getSystemMemeberUid(), customProperty.getSystemMemeberUid(),
							null, chatRoom.getMemberUid(), CHNG_DIV_CD_AUTO, MEMBER_DIV_CD_R);
				} else {
					log.error("ASSIGN FAILED: {}", assignResult);
					responseChatMessageList.add(chatService.buildChatMessage(chatRoom,
							ChatMessage.buildChatMessageText((String) siteSetting.get("not_cns_msg"))));
				}

				for (ChatMessage chatMessage : responseChatMessageList) {
					// 메세지 저장
					chatService.insertChatMessage(chatMessage);
					// 채팅방 마지막 메세지 세팅
					chatRoomService.setLastChatMessage(chatRoom, chatMessage);
					// 채팅방 반영
					chatRoomService.saveAndNoticeChatRoom(chatRoom);
					// 메세지 전달
					chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), chatMessage);
				}
			}

		} else */
		// ////////////////////////////////////////////////////////////////////
		// 챗봇 배정
		// ////////////////////////////////////////////////////////////////////
		if (MEMBER_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) {

			log.info("CONDITION: ASSIGN CHAT BOT");

			String chatBotMemberUid = customProperty.getHappyBotMemeberUid(); // 해피봇
			if (isRelayBot) { // 채널봇
				if (CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) { // 오픈빌더
					chatBotMemberUid = customProperty.getIBotMemeberUid();
				}
			}
			ChatRoom.AssignResult assignResult = assignChatBot(chatRoom, chatBotMemberUid);
			log.info("ASSIGNED CHAT BOT: {}", chatBotMemberUid);

			// 해피봇이면 인사말 메세지 발송
			if (chatRoom.getMemberUid().equals(customProperty.getHappyBotMemeberUid())) {

				List<ChatMessage> responseChatMessageList = new ArrayList<>();

				/**
				 * IPCC_MCH null 오류 관련 수정
				 */
				if (!StringUtil.isEmpty(chatRoom.getRoomTitle()) && chatRoom.getRoomTitle().startsWith("SIMULATION")) { // 시뮬레이션일때 블록 부터 시작
					String [] titles = chatRoom.getRoomTitle().split("/");
					String extra = "HappyBot/Block/" + titles[2].trim();
					if (chatRoom.getRoomTitle().contains("SKILL")) {
						extra = "HappyTalk/Skill/" + titles[2].trim();
					}
					ChatContents chatContents = new ChatContents(Section.builder()
							.type(SectionType.text)
							.data("시뮬레이션 시작")
							.extra(extra)
							.build());
					ChatMessage chatMessage = ChatMessage.builder()
							.chatRoomUid(chatRoom.getChatRoomUid())
							.cont(objectMapper.writeValueAsString(chatContents))
							.build();
					responseChatMessageList = chatBotService.chatWithRobot(chatRoom, chatMessage,
							ChatBotRequestMessageType.QUESTION.name());
				} else {
					responseChatMessageList.addAll(chatService.addGreetMessage(chatRoom, siteSetting, chatRoom.getCnsrDivCd()));
					if (ChatRoom.AssignResult.SUCCEED.equals(assignResult)) {
						// 변경 이력 저장
						insertChatCnsrChngHis(chatRoom.getChatRoomUid(), ChatCommand.CHANGE_COUNSELOR, customProperty.getSystemMemeberUid(), customProperty.getSystemMemeberUid(),
								null, chatRoom.getMemberUid(), CHNG_DIV_CD_AUTO, MEMBER_DIV_CD_R);
					} else {
						log.error("ASSIGN FAILED: {}", assignResult);
						responseChatMessageList.add(chatService.buildChatMessage(chatRoom,
								ChatMessage.buildChatMessageText((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_"+ "not_cns_msg"))));
					}
				}

				for (ChatMessage responseChatMessage : responseChatMessageList) {
					log.info("responseChatMessage: {}", responseChatMessage);
					chatService.insertChatMessage(responseChatMessage);
					// 채팅방 마지막 메세지 세팅
					chatRoomService.setLastChatMessage(chatRoom, responseChatMessage);
				}
				// 채팅방 반영
				chatRoomService.saveAndNoticeChatRoom(chatRoom);
				// 메세지 전달
				for (ChatMessage responseChatMessage : responseChatMessageList) {
					chatService.sendMessage(chatRoom, responseChatMessage.getChatRoomUid(), responseChatMessage);
				}
			}
		}

		// ////////////////////////////////////////////////////////////////////
		// 상담원 배정
		// ////////////////////////////////////////////////////////////////////
		else {

			log.info("CONDITION: ASSIGN COUNSELOR : {}", chatRoom);

			// 인사말 메세지 저장
			List<ChatMessage> responseChatMessageList = chatService.addGreetMessage(chatRoom, siteSetting, chatRoom.getCnsrDivCd());

			ChatMessage chatMessage = new ChatMessage();
			if(responseChatMessageList.size() > 0) {
				chatMessage = responseChatMessageList.get(0);
				if (chatMessage != null) {
					chatService.insertChatMessage(chatMessage);

					// 채팅방 반영
					chatRoomService.saveChatRoom(chatRoom);
				
					// 메세지 전달
					chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), chatMessage);
				}
			}

			// 카카오톡의 상담원 배정인 경우 ctgnum 값을 설정하여 해당 상담원이 연결되도록 한다.
			if(CommonConstants.CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
				String ctgnum = getCtgNumForKakao(chatRoom);
				if(!Strings.isNullOrEmpty(ctgnum)) {
					chatRoom.setCtgNum(ctgnum);
					chatRoomService.saveChatRoom(chatRoom);
				}else {
					//회원인증이 아닌 경우 chatRoom 종료
					//String message = (String) siteSetting.get("not_cns_msg");
					String message = "인증정보가 없습니다.\r\n" +
							"우수고객인증 후 채팅상담이 가능합니다.\r\n" +
							"\r\n" +
							"인증이 안되시나요?  \r\n" +
							"우수고객 핫라인 ☎ 1577-4100에서 궁금증을 해결해드리겠습니다";
					chatMessage = chatService.buildChatMessage(chatRoom,
							ChatMessage.buildChatMessageText(message));
					chatMessage.setType(ChatMessageType.END);
					chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
					chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
					chatService.insertChatMessage(chatMessage);

					// 채팅방 마지막 메세지 세팅
					chatRoomService.setLastChatMessage(chatRoom, chatMessage);
					// 채팅방 반영
					chatRoomService.endChatRoom(chatRoom, chatMessage, END_DIV_CD_NORMAL, customProperty.getSystemMemeberUid());
					log.info("kakao ======>>>>>>>chatRoom : {}" , chatRoom);
					return chatRoom;
				}
			}

			
			ChatRoom.AssignResult assignResult = assignCounselorByCustomer(chatRoom, siteSetting, "C");

			// 배정 결과 메세지
			if (ChatRoom.AssignResult.SUCCEED.equals(assignResult)) {

				// 결과 메세지 (배정 완료 메세지)
				if (settingService.isAutoAssign(siteSetting)) {
					chatMessage = chatService.buildChatMessage(chatRoom,
							ChatMessage.buildChatMessageText(ASSIGNED));
//					chatMessage = chatService.buildChatMessage(chatRoom,
//							ChatMessage.buildChatMessageText(chatRoom.getMemberName() + " " + ASSIGNED));
				} else {
					chatMessage = chatService.buildChatMessage(chatRoom,
							ChatMessage.buildChatMessageText(ASSIGN_SOON));
				}
				chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
				chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
				chatService.insertChatMessage(chatMessage);

				// 로그 메세지 저장
				chatService.insertChatMessage(
						chatService.buildLogChatMessage(chatRoom, LOG_CODE_ASSIGN_COUNSELOR));

				// 상담원 변경 이력 저장
				insertChatCnsrChngHis(chatRoom.getChatRoomUid(), ChatCommand.CHANGE_COUNSELOR, customProperty.getSystemMemeberUid(), customProperty.getSystemMemeberUid(),
						null, chatRoom.getMemberUid(), CHNG_DIV_CD_AUTO, MEMBER_DIV_CD_C);

				// 채팅방 마지막 메세지 세팅
				chatRoomService.setLastChatMessage(chatRoom, chatMessage);
				// 채팅방 반영
				chatRoomService.saveAndNoticeChatRoom(chatRoom);
				// 메세지 전달
				chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), chatMessage);

			} else if (ChatRoom.AssignResult.SCHEDULED.equals(assignResult)) {
				// 결과 메세지 (바쁜시간 메세지)
				//				if (settingService.isBusyMessage(siteSetting)) {
				//					chatMessage = chatService.buildChatMessage(chatRoom,
				//							ChatMessage.buildChatMessageText((String) siteSetting.get("busy_msg")));
				//				} else {
				// 배정 대기 스케줄 개수
				Integer scheduledCount = settingService.selectAssignSchedulerCount(chatRoom.getCtgNum().toString());
				// 상담원이 진행중인 채팅방 개수
				// Integer counselingCount = memberService.selectCounselingCount(chatRoom.getMemberUid());
				String message = (String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_"+ "cns_wait_msg");
				message = message.replaceFirst("\\{COUNT\\}", scheduledCount.toString());
				chatMessage = chatService.buildChatMessage(chatRoom,
						ChatMessage.buildChatMessageText(message)); // ASSIGN_SCHEDULED
				//				}
				chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
				chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
				chatService.insertChatMessage(chatMessage);

				// 채팅방 마지막 메세지 세팅
				chatRoomService.setLastChatMessage(chatRoom, chatMessage);
				// 채팅방 반영
				chatRoomService.saveAndNoticeChatRoom(chatRoom);
				// 메세지 전달
				chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), chatMessage);

				// 로그 메세지 저장
				chatService.insertChatMessage(
						chatService.buildLogChatMessage(chatRoom, LOG_CODE_ASSIGN_SCHEDULED));

			} else if (ChatRoom.AssignResult.NO_COUNSELOR.equals(assignResult)) {
				// 결과 메세지 (상담불가 메세지)
				String message = (String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_"+ "not_cns_msg");
//				if (DEPART_CD_TM.equals(chatRoom.getDepartCd())) {
//					message = (String) siteSetting.get("not_cns_msg");
//				}
				chatMessage = chatService.buildChatMessage(chatRoom,
						ChatMessage.buildChatMessageText(message));
				chatMessage.setType(ChatMessageType.END);
				chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
				chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
				chatService.insertChatMessage(chatMessage);

				// 채팅방 마지막 메세지 세팅
				chatRoomService.setLastChatMessage(chatRoom, chatMessage);
				// 채팅방 반영
				chatRoomService.endChatRoom(chatRoom, chatMessage, END_DIV_CD_COUNSELOR_POOL_OVER, customProperty.getSystemMemeberUid());
				//				chatRoomService.saveAndNoticeChatRoom(chatRoom);

			} else if (ChatRoom.AssignResult.COUNSELOR_POOL_OVER.equals(assignResult)) {
				// 결과 메세지 (미배정 개수 초과 메세지)
				chatMessage = chatService.buildChatMessage(chatRoom,
						ChatMessage.buildChatMessageText((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_"+ "cns_wait_pers_msg")));
				chatMessage.setType(ChatMessageType.END);
				chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
				chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
				chatService.insertChatMessage(chatMessage);

				// 채팅방 마지막 메세지 세팅
				chatRoomService.setLastChatMessage(chatRoom, chatMessage);
				// 채팅방 반영
				chatRoomService.endChatRoom(chatRoom, chatMessage, END_DIV_CD_COUNSELOR_POOL_OVER, customProperty.getSystemMemeberUid());
				//				chatRoomService.saveAndNoticeChatRoom(chatRoom);

			} else if (ChatRoom.AssignResult.CENTER_OFF_DUTY.equals(assignResult)) {
				// 결과 메세지 (근무시간외 메세지)
				chatMessage = chatService.buildChatMessage(chatRoom,
						ChatMessage.buildChatMessageText((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_"+ "unsocial_msg")));
				chatMessage.setType(ChatMessageType.END);
				chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
				chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
				chatService.insertChatMessage(chatMessage);

				// 채팅방 마지막 메세지 세팅
				chatRoomService.setLastChatMessage(chatRoom, chatMessage);
				// 채팅방 반영
				chatRoomService.endChatRoom(chatRoom, chatMessage, END_DIV_CD_CENTER_OFF_DUTY, customProperty.getSystemMemeberUid());
				//				chatRoomService.saveAndNoticeChatRoom(chatRoom);

			} else {
				// 결과 메세지 (배정 불가)
				chatMessage = chatService.buildChatMessage(chatRoom,
						ChatMessage.buildChatMessageText(ASSIGN_NOT_ACCEPT));
				chatMessage.setType(ChatMessageType.END);
				chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
				chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
				chatService.insertChatMessage(chatMessage);

				// 채팅방 마지막 메세지 세팅
				chatRoomService.setLastChatMessage(chatRoom, chatMessage);
				// 채팅방 반영
				chatRoomService.endChatRoom(chatRoom, chatMessage, END_DIV_CD_NORMAL, customProperty.getSystemMemeberUid());
				//				chatRoomService.saveAndNoticeChatRoom(chatRoom);
			}

			log.info("ASSIGN RESULT: {}", assignResult);
		}

		log.info("ASSIGNED CHAT ROOM");
		return chatRoom;
	}

	/**
	 * 챗봇 배정
	 */
	@Transactional
	public ChatRoom.AssignResult assignChatBot(@NotNull ChatRoom chatRoom, @NotEmpty String memberUid) {

		log.debug("ASSIGN CHATBOT");

		chatRoom.setCnsrDivCd(MEMBER_DIV_CD_R);
		chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_BOT);
		chatRoom.setManagerUid(memberUid);
		chatRoom.setMemberUid(memberUid);

		return ChatRoom.AssignResult.SUCCEED;
	}

	/**
	 * 상담원 배정: 방 생성시, by 스케줄러
	 */
	@Transactional
	public ChatRoom.AssignResult tryAssignCounselorByCustomer(ChatRoom chatRoom, Map<String, Object> siteSetting) {

		log.info(">>> ASSIGN, TRY ASSIGN COUNSELOR: {}", chatRoom.getChatRoomUid());
		
		Map<String, Object> workMap = manageService.selectWorkYnCheck(DateUtil.getCurrentDate("yyyyMMdd"), chatRoom.getCstmLinkDivCd().toString());
		boolean isSiteOn = settingService.isSiteOn(siteSetting);
		boolean isWorkDay = settingService.isWorkDay(workMap);

		// 예외 처리: 미배정, 미접수 상태
		if (MEMBER_DIV_CD_C.equals(chatRoom.getCnsrDivCd())
				&& !customProperty.getDefaultAssignMemeberUid().equals(chatRoom.getManagerUid())
				&& !customProperty.getDefaultAssignMemeberUid().equals(chatRoom.getMemberUid())) {
			log.info(">>> ASSIGN, ALREADY ASSIGNED: {}", chatRoom.getChatRoomUid());
			return ChatRoom.AssignResult.ALREADY_ASSIGNED;
		}

		//		chatRoom.setCnsrDivCd(MEMBER_DIV_CD_C);
		//		chatRoom.setManagerUid(customProperty.getDefaultAssignMemeberUid()); // 미배정
		//		chatRoom.setMemberUid(customProperty.getDefaultAssignMemeberUid()); // 미배정
		//		chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_CNSR);

		// 상담 센터 OFF (전 상담원 상담가능 설정)
		if (!isSiteOn) {
			log.info(">>> ASSIGN, ASSIGN FAILED: NO_COUNSELOR: {}", chatRoom.getChatRoomUid());
			chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_END);
			chatRoom.setEndYn("Y");
			chatRoom.setEndDivCd(END_DIV_CD_CENTER_OFF_DUTY);
			chatRoom.setRoomEndDt(commonService.selectSysdate());
			return ChatRoom.AssignResult.NO_COUNSELOR;
		}

		// 근무일이 아님
		if ("N".equals(workMap.get("work_yn"))) {
			log.info(">>> ASSIGN, ASSIGN FAILED: CENTER_OFF_DUTY: {}", chatRoom.getChatRoomUid());
			chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_END);
			chatRoom.setEndYn("Y");
			chatRoom.setEndDivCd(END_DIV_CD_CENTER_OFF_DUTY);
			chatRoom.setRoomEndDt(commonService.selectSysdate());
			return ChatRoom.AssignResult.CENTER_OFF_DUTY;
		}

		// 근무시간이 아님
		if (!isWorkDay) {
			// 근무시간외 접수 가능
			if (settingService.isAcceptOffDuty(siteSetting, chatRoom.getCstmLinkDivCd().toString())) {
				;
			}
			// 근무시간외 접수 불능
			if (!settingService.isAcceptOffDuty(siteSetting, chatRoom.getCstmLinkDivCd().toString())) {
				log.info(">>> ASSIGN, ASSIGN FAILED: CENTER_OFF_DUTY: {}", chatRoom.getChatRoomUid());
				chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_END);
				chatRoom.setEndYn("Y");
				chatRoom.setEndDivCd(END_DIV_CD_CENTER_OFF_DUTY);
				chatRoom.setRoomEndDt(commonService.selectSysdate());
				return ChatRoom.AssignResult.CENTER_OFF_DUTY;
			}
		}

		return ChatRoom.AssignResult.SUCCEED;
	}


	/**
	 * 상담원 배정: 방 생성시, by 스케줄러
	 */
	@Transactional
	public ChatRoom.AssignResult assignCounselorByCustomer(ChatRoom chatRoom, Map<String, Object> siteSetting, String mName) {

		log.info(">>> ASSIGN, ASSIGN COUNSELOR: {}", chatRoom.getChatRoomUid());

		Map<String, Object> workMap = manageService.selectWorkYnCheck(DateUtil.getCurrentDate("yyyyMMdd"), chatRoom.getCstmLinkDivCd().toString());
		boolean isSiteOn = settingService.isSiteOn(siteSetting);
		boolean isWorkDay = settingService.isWorkDay(workMap);
		boolean isAutoAssign = settingService.isAutoAssign(siteSetting);
		boolean isDirectAssign = settingService.isDirectAssign(siteSetting);
		boolean isWorkOutAssign = false;
		// 스케줄러 배정 및 근무시간외 , 대기자 존재
		boolean isScheduled = settingService.selectAssignSchedulerRoomUid(chatRoom.getChatRoomUid());
		if(!isWorkDay && "B".equals(mName) && isScheduled == true) {
			isWorkOutAssign = true;
		}
		
		
		// 예외 처리: 미배정, 미접수 상태
		if (MEMBER_DIV_CD_C.equals(chatRoom.getCnsrDivCd())
				&& !customProperty.getDefaultAssignMemeberUid().equals(chatRoom.getManagerUid())
				&& !customProperty.getDefaultAssignMemeberUid().equals(chatRoom.getMemberUid())) {
			log.info(">>> ASSIGN, ALREADY ASSIGNED: {}", chatRoom.getChatRoomUid());
			return ChatRoom.AssignResult.ALREADY_ASSIGNED;
		}

		chatRoom.setCnsrDivCd(MEMBER_DIV_CD_C);
		chatRoom.setManagerUid(customProperty.getDefaultAssignMemeberUid()); // 미배정
		chatRoom.setMemberUid(customProperty.getDefaultAssignMemeberUid()); // 미배정
		chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_CNSR);

		// 상담 센터 OFF (전 상담원 상담가능 설정)
		if (!isSiteOn) {
			log.info("업무시간 외 ************ >>> ASSIGN, ASSIGN FAILED: NO_COUNSELOR: {}", chatRoom.getChatRoomUid());
			chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_END);
			chatRoom.setEndYn("Y");
			chatRoom.setEndDivCd(END_DIV_CD_CENTER_OFF_DUTY);
			chatRoom.setRoomEndDt(commonService.selectSysdate());
			return ChatRoom.AssignResult.CENTER_OFF_DUTY;
		}

		// 근무일이 아님
		if ("N".equals(workMap.get("work_yn"))) {
			log.info("휴일 *********** >>> ASSIGN, ASSIGN FAILED: CENTER_OFF_DUTY: {}", chatRoom.getChatRoomUid());
			chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_END);
			chatRoom.setEndYn("Y");
			chatRoom.setEndDivCd(END_DIV_CD_CENTER_OFF_DUTY);
			chatRoom.setRoomEndDt(commonService.selectSysdate());
			return ChatRoom.AssignResult.CENTER_OFF_DUTY;
		}

		// 근무시간이 아님 
		if (!isWorkDay) {
			// 근무시간외 접수 불능, 스케줄 예약 방 아님
			if (!settingService.isAcceptOffDuty(siteSetting, chatRoom.getCstmLinkDivCd().toString()) && isScheduled == false) {
				log.info("***** 근무시간외 >>> ASSIGN, ASSIGN FAILED: CENTER_OFF_DUTY: {}", chatRoom.getChatRoomUid());
				chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_END);
				chatRoom.setEndYn("Y");
				chatRoom.setEndDivCd(END_DIV_CD_CENTER_OFF_DUTY);
				chatRoom.setRoomEndDt(commonService.selectSysdate());
				return ChatRoom.AssignResult.CENTER_OFF_DUTY; // 
			}
		}

		// ////////////////////////////////////////////////////////////////////
		// 자동 배정
		if (isAutoAssign) {

			log.info(">>> ASSIGN, AUTO ASSIGN: {}", chatRoom.getChatRoomUid());

			// 대상 상담원 목록
			List<Map<String, Object>> availableCounselorList = new ArrayList<Map<String, Object>>();

			// ////////////////////////////////////////////////////////////////////
			// 분류별 배정
			if (isDirectAssign) {
				if (chatRoom.getCtgNum() != null) { // 분류 정보가 있을 경우: 분류-상담원 매칭 정보 사용
					log.info(">>> ASSIGN, DIRECT ASSIGN WITH CATEGORY: {}", chatRoom.getCtgNum());
					availableCounselorList = settingService.selectCounselorByCategory(chatRoom.getCtgNum());

				} else { // 분류정보가 없을 경우: 상담원 전체
					log.info(">>> ASSIGN, DIRECT ASSIGN WITHOUT CATEGORY: {}", chatRoom.getChatRoomUid());
					availableCounselorList = memberService.selectCounselorList(false);
				}

				log.info(">>> ASSIGN, AVAILABLE COUNSELORS: {}\n{}", chatRoom.getChatRoomUid(), availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));

				// ////////////////////////////////////////////////////////////////////
				// 가능 상담원이 없을 경우 상담불가
				if (availableCounselorList.isEmpty()) {
					log.info(">>> ASSIGN, COMPLETE, NO_COUNSELOR: {}", chatRoom.getChatRoomUid());
					return ChatRoom.AssignResult.NO_COUNSELOR;
				}
			}

			// ////////////////////////////////////////////////////////////////////
			// 분류 무시 배정
			if (!isDirectAssign) {
				log.info(">>> ASSIGN, INDIRECT ASSIGN, {}", chatRoom.getChatRoomUid());
				availableCounselorList = memberService.selectCounselorList(false);
			}

			// ////////////////////////////////////////////////////////////////////
			// 가능 상담원이 없을 경우 배정 스케줄러에 등록
			if (availableCounselorList.isEmpty()) {
				log.info(">>> ASSIGN, COMPLETE, NO_COUNSELOR: {}", chatRoom.getChatRoomUid());
				settingService.saveAssignScheduler(chatRoom);
				return ChatRoom.AssignResult.NO_COUNSELOR;
			}

			// ////////////////////////////////////////////////////////////////////
			// 상담원 스케줄 필터링
			log.info(">>> ASSIGN, AVAILABLE COUNSELORS: {}\n{}", chatRoom.getChatRoomUid(), availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));
			
			availableCounselorList = memberService.selectAvailableCounselorList(availableCounselorList, true, false, isScheduled);
			log.info(">>> ASSIGN, FILTERED COUNSELORS BY SCHEDULE: {}\n{}", chatRoom.getChatRoomUid()
					, availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet())
					, availableCounselorList.stream().map(map -> map.get("depart_nm")).collect(Collectors.toSet())
					, availableCounselorList.stream().map(map -> map.get("depart_cd")).collect(Collectors.toSet()));

			// ////////////////////////////////////////////////////////////////////
			// 가능 상담원이 없을 경우 배정 스케줄러에 등록
			if (availableCounselorList.isEmpty()) {
				log.info(">>> ASSIGN, COMPLETE, NO_COUNSELOR, {}", chatRoom.getChatRoomUid());
				settingService.saveAssignScheduler(chatRoom);
				return ChatRoom.AssignResult.NO_COUNSELOR;
			}

			// ////////////////////////////////////////////////////////////////////
			// 상담원 휴식시간 필터링
			log.info(">>> ASSIGN, AVAILABLE COUNSELORS: {}\n{}", chatRoom.getChatRoomUid()
					, availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));
			availableCounselorList = memberService.selectAvailableCounselorList(availableCounselorList, false, false, isScheduled);
			log.info(">>> ASSIGN, FILTERED COUNSELORS BY BREAKTIME: {}\n{}", chatRoom.getChatRoomUid()
					, availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));

			// ////////////////////////////////////////////////////////////////////
			// 가능 상담원이 없을 경우 배정 스케줄러에 등록
			if (availableCounselorList.isEmpty()) {
				log.info(">>> ASSIGN, COMPLETE, SCHEDULED, {}", chatRoom.getChatRoomUid());
				return assignSchedule(chatRoom, siteSetting);
			}

			// ////////////////////////////////////////////////////////////////////
			// 우선 순위 상담원 우선 배정 시도
			List<Map<String, Object>> priorityCounselorList = new ArrayList<>();
			if (isDirectAssign) {
				log.info(">>> ASSIGN, AVAILABLE COUNSELORS: {}\n{}", chatRoom.getChatRoomUid()						, availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));

				List<Map<String, Object>> mappingCounselorList = settingService.selectCounselorByCategory(chatRoom.getCtgNum());

				priorityCounselorList = memberService.selectCounselorListByPriority(availableCounselorList, mappingCounselorList);

				log.info(">>> ASSIGN, PRIORITY COUNSELORS BY PRIORITY: {}\n{}", chatRoom.getChatRoomUid()
						, priorityCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));
			}

			// ////////////////////////////////////////////////////////////////////
			// 상담가능 상담원 중 선택 (최소 배정 개수를 가진 상담원 중 랜덤 배정)
			// ex) 고객 처리량, 대응 시간별, 타이핑량...
			if (priorityCounselorList.isEmpty()) { // 우선 순위 상담원 없을 경우 전체 상담원이 대상
				log.info(">>> ASSIGN, MINIMUM COUNSELING BY ALL COUNSELORS: {}", chatRoom.getChatRoomUid());
				availableCounselorList = memberService.selectCounselorListByMinimumAssigned(availableCounselorList, chatRoom.getChatRoomUid());
			} else { // 우선 순위 상담원 있을 경우 우선 순위 상담원이 대상
				log.info(">>> ASSIGN, MINIMUM COUNSELING BY PRIORITY COUNSELORS: {}", chatRoom.getChatRoomUid());
				priorityCounselorList = memberService.selectCounselorListByMinimumAssigned(priorityCounselorList, chatRoom.getChatRoomUid());
				if (priorityCounselorList.isEmpty()) { // 우선 순위 상담원이 상담불가 일 경우 전체 상담원이 대상
					log.info(">>> ASSIGN, NO PRIORITY COUNSELORS, MINIMUM COUNSELING BY ALL COUNSELORS: {}", chatRoom.getChatRoomUid());
					availableCounselorList = memberService.selectCounselorListByMinimumAssigned(availableCounselorList, chatRoom.getChatRoomUid());
				} else {
					availableCounselorList = priorityCounselorList;
				}
			}

			log.info(">>> ASSIGN, FILTERD COUNSELORS BY MINIMUM COUNSELING: {}\n{}", chatRoom.getChatRoomUid()
					, availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));

			// ////////////////////////////////////////////////////////////////////
			// 가능 상담원이 없을 경우 배정 스케줄러에 등록
			if (availableCounselorList.isEmpty()) {
				log.info(">>> ASSIGN, COMPLETE, SCHEDULED: {}", chatRoom.getChatRoomUid());
				return assignSchedule(chatRoom, siteSetting);
			}
			else {
				Integer scheduledCountCtg = settingService.selectAssignSchedulerCountForCtg(chatRoom.getCtgNum());		//// 대기자 있는 경우 배정 안함
				log.info(" >>>>>>>>>>>>>> scheduledCountCtg " + scheduledCountCtg.toString());
				if(scheduledCountCtg == 0 || mName == "B") {
					log.info(">>> ASSIGN, PICK RANDOM COUNSELOR: {}\n{}", chatRoom.getChatRoomUid()	, availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));
					Random rand = new Random();
					int randNum = rand.nextInt(availableCounselorList.size());
					Map<String, Object> counselor = availableCounselorList.get(randNum);

					Map<String, Object> chkParam = new HashMap<>();
					chkParam.put("memberUid", counselor.get("member_uid").toString());
					Map<String, Object> chkCnsr = memberService.selectCountRoomByCounselor(chkParam);

					chatRoom.setMemberUid((String) counselor.get("member_uid"));
					chatRoom.setMemberName((String) counselor.get("name"));
					chatRoom.setDepartCd((String) counselor.get("depart_cd"));
					chatRoom.setDepartNm((String) counselor.get("depart_nm"));

					if (MEMBER_DIV_CD_M.equals(counselor.get("member_div_cd"))) { // 매니저가 상담원인 경우 매니저를 자신으로 세팅
						chatRoom.setManagerUid((String) counselor.get("member_uid"));
					} else {
						chatRoom.setManagerUid((String) counselor.get("upper_member_uid"));
					}

					log.info(">>> ASSIGN, COMPLETE, {}, AUTO ASSIGNED TO COUNSELOR: {}", chatRoom.getChatRoomUid(), counselor);
					log.debug("AssignCounselorDirect - assignCounselorByCustomer - CHAT_ROOM_STATUS_CD_ASSIGN");

					chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_ASSIGN);
				}
				else {
					log.info(">>> ASSIGN, COMPLETE, SCHEDULED: {}", chatRoom.getChatRoomUid());
					return assignSchedule(chatRoom, siteSetting);

				}
			}
		}

		// ////////////////////////////////////////////////////////////////////
		// 수동 배정
		if (!isAutoAssign) {

			log.info(">>> ASSIGN, MANUAL ASSIGN: {}", chatRoom.getChatRoomUid());

			// 대상 상담원 목록
			List<Map<String, Object>> availableCounselorList = memberService.selectCounselorList(false);

			// ////////////////////////////////////////////////////////////////////
			// 상담원 스케줄 확인
			log.info(">>> ASSIGN, AVAILABLE COUNSELORS: {}",
					availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));
			availableCounselorList = memberService.selectAvailableCounselorList(availableCounselorList);
			log.info(">>> ASSIGN, FILTERED COUNSELORS BY SCHEDULE: {}",
					availableCounselorList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));

			// ////////////////////////////////////////////////////////////////////
			// 가능 상담원이 없을 경우 근무시간이 아님
			if (availableCounselorList.isEmpty()) {
				log.info(">>> ASSIGN, COMPLETE, CENTER_OFF_DUTY");
				return ChatRoom.AssignResult.CENTER_OFF_DUTY;
				// return AssignResult.COUNSELOR_OFF_DUTY;
			}

			// 미배정 상태
			log.info(">>> ASSIGN, COMPLETE, MANUAL ASSIGNED: {}", chatRoom.getChatRoomUid());
			//			chatRoom.setDeparChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_CNSR);
			chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_CNSR);
		}

		log.info(">>> ASSIGN, COMPLETE, SUCCEED");
		return ChatRoom.AssignResult.SUCCEED;
	}

	private ChatRoom.AssignResult assignSchedule(@NotNull ChatRoom chatRoom, @NotEmpty Map<String, Object> siteSetting) {
		
		// 미배정 개수 제한
		if ("Y".equals(settingService.isLimitMaxWaitCount(siteSetting, chatRoom.getCstmLinkDivCd().toString()))) {
			//log.info("+++++++++++++++++ siteSetting : " + siteSetting);
			int counselorMaxCount = settingService.getMaxWaitCount(siteSetting, chatRoom.getCstmLinkDivCd().toString());
			String ctg_num = chatRoom.getCtgNum().toString();
			List<Map<String, Object>> jobList = settingService.selectAssignSchedulerList(ctg_num);
			if (counselorMaxCount <= jobList.size()) {
				log.info(">>> ASSIGN, ASSIGN FAILED: COUNSELOR_POOL_OVER (MAX: {} <= CURRENT: {}): {}", counselorMaxCount, jobList.size(), chatRoom.getChatRoomUid());
				chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_END);
				chatRoom.setEndYn("Y");
				chatRoom.setEndDivCd(END_DIV_CD_COUNSELOR_POOL_OVER);
				chatRoom.setRoomEndDt(commonService.selectSysdate());
				return ChatRoom.AssignResult.COUNSELOR_POOL_OVER;
			}
		}

		log.info(">>> ASSIGN, SAVE SCHEDULE: {}", chatRoom.getChatRoomUid());
		settingService.saveAssignScheduler(chatRoom);
		return ChatRoom.AssignResult.SCHEDULED;
	}

	/**
	 * 상담원 배정: 상담원을 직접 지정
	 */
	@Transactional
	public ChatRoom.AssignResult assignCounselorDirect(ChatRoom chatRoom, String memberUid, String creater,
			Map<String, Object> siteSetting) {

		ChatRoom.AssignResult assignResult = ChatRoom.AssignResult.ASSIGN_CHANGED;
		log.info("********************************" + assignResult);
		// 채팅방 상태 변경
		if (CHAT_ROOM_STATUS_CD_END.equals(chatRoom.getChatRoomStatusCd())) { // 종료 채팅방 예외
			log.warn("FAILED ASSIGN DIRECT, END CHAT ROOM: {}", chatRoom);
			assignResult = ChatRoom.AssignResult.FAILED;
			return assignResult;
		} else { // 미접수로 변경
			log.debug("assignCounselorDirect - AssignService - CHAT_ROOM_STATUS_CD_ASSIGN");
			chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_ASSIGN);
			assignResult = ChatRoom.AssignResult.SUCCEED;
		}

		// 상담원 변경
		Map<String, Object> map = memberService.selectMemberByMemberUid(memberUid);
		chatRoom.setMemberUid((String) map.get("member_uid"));
		if (MEMBER_DIV_CD_M.equals(map.get("member_div_cd"))) { // 매니저가 상담원인 경우 매니저를 자신으로 세팅
			chatRoom.setManagerUid((String) map.get("member_uid"));
		} else {
			chatRoom.setManagerUid((String) map.get("upper_member_uid"));
		}
		chatRoom.setCnsrDivCd(MEMBER_DIV_CD_C);

		log.info("ASSIGN DIRECT: {}, {}", chatRoom.getChatRoomUid(), map);

		return assignResult;
	}

	/**
	 * '매니저에게 요청' 조회, 기존 요청 있는지 확인
	 */
	public boolean hasNotPermitedChatCnsrChngReq(String chatRoomUid, ChatCommand chatCommand) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("reqType", chatCommand.name());
		params.put("apprYn", "N");
		return !chatDao.selectChatCnsrChngReqList(params).isEmpty();
	}

	/**
	 * '매니저에게 요청' 조회
	 */
	public Map<String, Object> selectChatCnsrChngReq(String chngReqNum) {

		Map<String, Object> params = new HashMap<>();
		params.put("chngReqNum", chngReqNum);
		return chatDao.selectChatCnsrChngReq(params);
	}

	/**
	 * '매니저에게 요청' 조회
	 */
	public Map<String, Object> selectChatCnsrChngReq(String chatRoomUid, ChatCommand chatCommand) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("reqType", chatCommand.name());
		params.put("apprYn", "N");
		List<Map<String, Object>> requestList = chatDao.selectChatCnsrChngReqList(params);
		if (requestList.isEmpty()) {
			return null;
		} else {
			return requestList.get(0);
		}
	}

	/**
	 * '매니저에게 요청' 저장
	 *
	 * @param chatRoomUid
	 * @param reqType 변경 구분 (상담원 변경, 매니저 채팅, 코끼리 변경)
	 * @param managerUid
	 * @param memberUid
	 * @param changeMemberUid 변경될 회원
	 * @param memo 변경될 회원
	 */
	@Transactional
	public int insertChatCnsrChngReq(String chatRoomUid, ChatCommand chatCommand,
			String managerUid, String memberUid, String departCd, String changeMemberUid, String memo, String flagName) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("reqType", chatCommand.name());
		params.put("managerUid", managerUid);
		params.put("creater", memberUid);
		params.put("departCd", departCd);
		params.put("changeMemberUid", changeMemberUid);
		params.put("memo", memo);
		params.put("apprYn", "N");
		params.put("flagName", flagName);
		return chatDao.insertChatCnsrChngReq(params);
	}

	/**
	 * '매니저에게 요청' 저장
	 *
	 * @param chatRoomUid
	 * @param reqType 변경 구분 (상담원 변경, 매니저 채팅, 코끼리 변경)
	 * @param managerUid
	 * @param memberUid
	 * @param changeMemberUid 변경될 회원
	 * @param memo 변경될 회원
	 */
	@Transactional
	public int insertChatCnsrChngReqWithApprove(String chatRoomUid, ChatCommand chatCommand,
			String managerUid, String memberUid, String departCd, String changeMemberUid, String memo) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("reqType", chatCommand.name());
		params.put("managerUid", managerUid);
		params.put("creater", memberUid);
		params.put("departCd", departCd);
		params.put("changeMemberUid", changeMemberUid);
		params.put("memo", memo);
		params.put("apprYn", "Y");
		return chatDao.insertChatCnsrChngReq(params);
	}

	/**
	 * '매니저에게 요청' 승인
	 *
	 * @param chatRoomUid 채팅방 식별자
	 * @param updater 승인 회원
	 * @param reqType 변경 구분 (상담원 변경, 매니저 채팅, 코끼리 변경)
	 */
	@Transactional
	public int updateChatCnsrChngReq(String chatRoomUid, ChatCommand chatCommand, String updater) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("reqType", chatCommand.name());
		params.put("updater", updater);
		params.put("apprYn", "Y");

		return chatDao.updateChatCnsrChngReq(params);
	}

	/**
	 * '매니저에게 요청' 반려
	 *
	 * @param chatRoomUid 채팅방 식별자
	 * @param updater 승인 회원
	 * @param reqType 변경 구분 (상담원 변경, 매니저 채팅, 코끼리 변경)
	 */
	@Transactional
	public int updateChatCnsrChngReq(String chngReqNum, String updater) {

		Map<String, Object> params = new HashMap<>();
		params.put("chngReqNum", chngReqNum);
		params.put("updater", updater);
		params.put("apprYn", "Y");

		return chatDao.updateChatCnsrChngReq(params);
	}

	/**
	 * '매니저에게 요청' 로그
	 *
	 * @param chatRoomUid
	 * @param reqType 변경 구분 (상담원 변경, 매니저 채팅, 코끼리 변경)
	 * @param managerUid
	 * @param memberUid
	 * @param changeMemberUid 상담원 변경시 변경할 상담원
	 * @param chngDivCd 상담원 변경 구분 코드 (자동, 매니저, 상담원, 본인)
	 * @param cnsrDivCd 상담원 구분 코드 (상담원, 챗봇)
	 */
	@Transactional
	public int insertChatCnsrChngHis(String chatRoomUid, ChatCommand chatCommand, String managerUid, String memberUid,
			String departCd, String changeMemberUid, String chngDivCd, String cnsrDivCd) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("reqType", chatCommand.name());
		params.put("managerUid", managerUid);
		params.put("departCd", departCd);
		params.put("changeMemUid", changeMemberUid);
		params.put("chngDivCd", chngDivCd);
		params.put("cnsrDivCd", chngDivCd);
		params.put("creater", memberUid);

		return chatDao.insertChatCnsrChngHis(params);
	}



}

