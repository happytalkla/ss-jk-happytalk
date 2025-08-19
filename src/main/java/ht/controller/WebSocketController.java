package ht.controller;

import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_ASSIGN;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_WAIT_CNSR;
import static ht.constants.CommonConstants.CNSR_DIV_CD_C;
import static ht.constants.CommonConstants.CNSR_DIV_CD_R;
import static ht.constants.CommonConstants.CONT_DIV_CD_T;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_A;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_B;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_C;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_D;
import static ht.constants.CommonConstants.END_DIV_CD_BOT_CSTM;
import static ht.constants.CommonConstants.END_DIV_CD_CENTER_OFF_DUTY;
import static ht.constants.CommonConstants.END_DIV_CD_CNSR_CNSR;
import static ht.constants.CommonConstants.END_DIV_CD_CNSR_CSTM;
import static ht.constants.CommonConstants.END_DIV_CD_CNSR_CSTM_ASSIGN_CNSR;
import static ht.constants.CommonConstants.END_DIV_CD_CNSR_CSTM_WAIT_CNSR;
import static ht.constants.CommonConstants.END_DIV_CD_NORMAL;
import static ht.constants.CommonConstants.LOG_CODE_END_CHATBOT;
import static ht.constants.CommonConstants.LOG_CODE_REQUEST_COUNSELOR;
import static ht.constants.CommonConstants.LOG_CODE_SUBMIT_COUNSELOR;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_C;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_R;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_U;
import static ht.constants.CommonConstants.MSG_STATUS_CD_READ;
import static ht.constants.CommonConstants.MSG_STATUS_CD_SEND;
import static ht.constants.CommonConstants.SENDER_DIV_CD_C;
import static ht.constants.CommonConstants.SENDER_DIV_CD_S;
import static ht.constants.CommonConstants.SENDER_DIV_CD_U;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.ChatContents;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatMessageType;
import ht.domain.ChatRoom;
import ht.domain.NoticeMessage;
import ht.domain.chatbot.ChatBotRequestMessageType;
import ht.persistence.CertificationDao;
import ht.service.AssignService;
import ht.service.CategoryService;
import ht.service.ChatRoomService;
import ht.service.ChatService;
import ht.service.CommonService;
import ht.service.CustomerService;
import ht.service.McaService;
import ht.service.SettingService;
import ht.service.channel.ChannelService;
import ht.service.chatbot.ChatBotService;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 웹소켓 메세지 처리 컨트롤러, client.js: send*() 와 대응됨
 */
@RestController
@Slf4j
public class WebSocketController {

	@Resource
	private CustomProperty customProperty;

	@Resource
	private SimpMessageSendingOperations messagingTemplate;

	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private ChannelService channelService;
	@Resource
	@Qualifier("HappyBotServiceImpl")
	private ChatBotService chatBotService;
	@Resource
	@Qualifier("CategoryBotServiceImpl")
	private ChatBotService categoryBotService;
	@Resource
	private CustomerService customerService;
	@Resource
	private AssignService assignService;
	@Resource
	private CommonService commonService;
	@Resource
	private SettingService settingService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private CertificationDao certificationDao;
	@Resource
	private McaService mcaService;

	/**
	 * 채팅 메세지 처리 (client.js: sendMessage() 와 대응)
	 *
	 * TODO: 고객용, 상담직원용 분리
	 */
	@SuppressWarnings("unchecked")
	@MessageMapping("/message")
	public ChatMessage message(@Payload ChatMessage chatMessage) throws Exception {

		log.info("::::::::::::::::::::::::: chatMessage: {}", chatMessage);
		
		chatMessage.setCont(StringUtil.replaceHtmlTag(chatMessage.getCont()));

		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatMessage);
		log.info("************** message ************** chatRoom: {}", chatRoom);
		
		if (chatRoom == null) {
			log.warn("NOT FOUND ROOM: {}", chatMessage.getChatRoomUid());
			return chatMessage;
			//throw new UnsupportedOperationException("NOT FOUND ROOM");
		} else if ("Y".equals(chatRoom.getEndYn())) {
			log.warn("END ROOM: {}", chatRoom.getChatRoomUid());
			return chatMessage;
			//throw new UnsupportedOperationException("END ROOM");
		}
		
		chatMessage.setMemberName(chatRoom.getSenderName());

		// 고객 유효성 검사
		if (SENDER_DIV_CD_U.equals(chatMessage.getSenderDivCd())) {
			//			Map<String, Object> customer = customerService.selectCustomerByCstmUid(chatMessage.getSenderUid());
			//			// 존재하는 고객인지 확인
			//			if (customer == null) {
			//				log.error("NO CUSTOMER: CANNOT SEND MESSAGE: {}", chatMessage);
			//				return chatMessage;
			//			}
			// 자기가 만든 채팅방인지 확인
			if (!chatRoom.getCstmUid().equals(chatMessage.getSenderUid())) {
				log.error("NOT YOUR CHATROOM: {}", chatMessage);
				return chatMessage;
			}
		}
		log.info("**************************** SENDER_DIV_CD_C: {}", SENDER_DIV_CD_C);
		// 상담직원 유효성 검사
		if (SENDER_DIV_CD_C.equals(chatMessage.getSenderDivCd())) {
			// 로봇 채팅방 금지
			if (CNSR_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) {
				log.error("CHATROOM WITH CHATBOT: CANNOT SEND MESSAGE BY COUNSELOR: {}", chatMessage);
				return chatMessage;
			}
		}

		// 고객 메세지
		if (chatMessage.getSenderUid().equals(chatRoom.getCstmUid())) {
			// 개인정보 블라인드 처리
			String cont = chatMessage.getCont();

			String blindCont = ht.util.StringUtil.getMaskedString(cont);	//마스킹 처리

			if (!cont.equals(blindCont)) {
				chatMessage.setCont(blindCont);
				blindCont = "";						//메세지 초기화
			}
			// 챗봇과 대화중인 경우 읽음 상태로 전송
			if (CNSR_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) {
				chatMessage.setMsgStatusCd(MSG_STATUS_CD_READ);
			}
			// 텍스트 명령어 변환 (상담직원 연결 등)
			chatMessage.setCont(chatService.parseNatureLanguageMessage(chatMessage.getCont()));
			log.debug("parseNatureLanguageMessage: {}", chatMessage.getCont());
			// 고객 지연 상태 변경
			chatRoom.setCstmReplyDelayYn("N");
		}

		// 채팅 메세지에 상담직원 세팅
		if (chatMessage.getCnsrMemberUid() == null) {
			if (chatRoom.getMemberUid() != null) {
				chatMessage.setCnsrMemberUid(chatRoom.getMemberUid());
			}
		}

		// 채팅 메세지 저장
		chatService.insertChatMessage(chatMessage);
		log.debug("chatNum: {}", chatMessage.getChatNum());

		// 상담직원이 처음 메세지 보냈을 경우 접수
		if (SENDER_DIV_CD_C.equals(chatMessage.getSenderDivCd()) // 상담직원 메세지
				&& !customProperty.getSystemMemeberUid().equals(chatMessage.getSenderUid()) // 시스템 메세지가 아님
				// && chatRoom.getCnsrLinkDt() == null) { // 미접수 상태
				&& CHAT_ROOM_STATUS_CD_ASSIGN.equals(chatRoom.getChatRoomStatusCd())) { // 미접수 상태

			chatRoom.setCnsrLinkDt(commonService.selectSysdate());
			// 로그 채팅 메세지 저장
			chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_SUBMIT_COUNSELOR));
		}

		// 채팅방 마지막 메세지, 상태 저장
		chatRoom = chatRoomService.setLastChatMessage(chatRoom, chatMessage);
		if (CNSR_DIV_CD_C.equals(chatRoom.getCnsrDivCd())) { // 상담직원과 대화 중인 경우 저장 및 발행
			chatRoomService.saveAndNoticeChatRoom(chatRoom);
		} else {
			chatRoomService.saveChatRoom(chatRoom);
		}

		// 클라이언트로 메세지 송신
		chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), chatMessage);
		log.info("===========================================" );
		// ////////////////////////////////////////////////////////////////////
		// 수신 메세지 별 예외 처리
		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지
		if (chatMessage.getSenderUid().equals(chatRoom.getCstmUid())) {

			// TODO: REFACTORY, ChannelService.receiveMessage
			ChatContents chatContents = objectMapper.readValue(chatMessage.getCont(), ChatContents.class);
			String extra = chatContents.getExtra();
			log.info(">>>>>>>>>>>> extra >>> : " + extra);
			if (!Strings.isNullOrEmpty(extra) // 핫키 처리 (상담직원 연결 요청)
					&& extra.startsWith("HappyTalk/RequestCounselor")) {
				//					&& !CNSR_DIV_CD_C.equals(chatRoom.getCnsrDivCd())) {

				/*  인증정보 삭제 */
				try {
					certificationDao.deleteCertificationRoomUid(chatRoom.getChatRoomUid());
					certificationDao.resetCertificationRoomInfo(chatRoom.getChatRoomUid());
				} catch (Exception e1) {
					log.error("{}", e1.getLocalizedMessage(), e1);
				}

				// TODO: REFACTORY, 해피톡 핫키 처리 메소드
				Map<String, Object> siteSetting = settingService.selectSiteSetting();

				String[] extras = extra.split("/");
				if (extras.length >= 3) {
					Map<String, Object> category = categoryService.selectCategory(extras[2]);
					if (category != null) {
						chatRoom.setCtgNum((String) category.get("ctg_num"));
						chatRoom.setDepartCd((String) category.get("depart_cd"));
						log.info("SET CTG_NUM: {}", chatRoom.getCtgNum());
						chatRoomService.saveChatRoom(chatRoom);
					} else {
						log.error("NO CATEGORY, EXTRA: {}", extra);
					}
				}

				// 로그 채팅 메세지 저장
				if (customProperty.getHappyBotMemeberUid().equals(chatRoom.getMemberUid())) {
					chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_END_CHATBOT));
				}
				chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_REQUEST_COUNSELOR));

				// 근무시간 판별
				ChatRoom.AssignResult assignResult = assignService.tryAssignCounselorByCustomer(chatRoom, siteSetting);
				if (ChatRoom.AssignResult.ALREADY_ASSIGNED.equals(assignResult)) {
					log.info("IGNORE ALREADY ASSIGNED");
				} else if (!ChatRoom.AssignResult.SUCCEED.equals(assignResult)) {
					// 메세지 전달
					ChatMessage endMessage = chatService.buildChatMessage(chatRoom,
							ChatMessage.buildChatMessageText((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_" + "unsocial_msg")));
					endMessage.setType(ChatMessageType.END);
					endMessage.setSenderDivCd(SENDER_DIV_CD_S);
					endMessage.setSenderUid(customProperty.getSystemMemeberUid());
					chatService.insertChatMessage(endMessage);

					// chatService.sendMessage(chatRoom, chatRoom.getChatRoomUid(), endMessage);
					// 소켓으로 메세지 발송 (채널은 과금될 수 있으므로 발송안함)
					messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatRoom.getChatRoomUid(), endMessage);
					// 채팅방 종료
					chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_CENTER_OFF_DUTY, customProperty.getSystemMemeberUid());
					// 채팅방 정보 발행
					messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new NoticeMessage(chatRoom));
				} else {
					chatRoom.setCnsrDivCd(CNSR_DIV_CD_C);
					chatRoom.setManagerUid(customProperty.getDefaultAssignMemeberUid());
					chatRoom.setMemberUid(customProperty.getDefaultAssignMemeberUid());
					assignService.assignChatRoom(chatRoom, siteSetting, true, false);
				}
			} else if (!Strings.isNullOrEmpty(extra) // 새 채팅방 메세지 (채널에서 들어옴, 무시)
					&& extra.startsWith("HappyTalk/NewChatRoom")) {
				// TODO: REFACTORY, 해피톡 핫키 처리 메소드
				//				Map<String, Object> customer = customerService.selectCustomerByCstmUid(chatMessage.getSenderUid());

			} else if (!Strings.isNullOrEmpty(extra) // 핫키 처리 (고객 인증)
					&& extra.startsWith("HappyTalk/CompleteCustomerAuth")) {
				// TODO: REFACTORY, 해피톡 핫키 처리 메소드
				//				Map<String, Object> customer = customerService.selectCustomerByCstmUid(chatMessage.getSenderUid());
				
			} else if (!Strings.isNullOrEmpty(extra) && extra.startsWith("HappyTalk/EndChatRoomKakao")) {
				// TODO: REFACTORY, 카카오방 종료 및 인증 정보 삭제
				log.info("============== HappyTalk/EndChatRoomKakao : " + extra );
				String[] extraArray = extra.split("/");
				String appName = extraArray[0];		/// 어플리케이션명
				String hotkeyName = extraArray[1];		/// hotkey 명
				String chatRoomUid = extraArray[2];		///	chatRoomId
				
				chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
				customerService.deleteCustomer(chatRoom.getCstmUid());
				/// 종료 처리
				chatMessage = ChatMessage.builder()
						.signature("ChatMessage")
						.type(ChatMessage.ChatMessageType.END)
						.chatRoomUid(chatRoom.getChatRoomUid())
						.senderUid(chatRoom.getCstmUid())
						.senderDivCd(SENDER_DIV_CD_U)
						.contDivCd(CONT_DIV_CD_T)
						.msgStatusCd(MSG_STATUS_CD_SEND)
						.build();
				end(chatMessage, chatRoom.getCstmUid());
			} else if (!Strings.isNullOrEmpty(extra) // 핫키 처리 (상담직원과 상담 종료)
					&& extra.startsWith("HappyTalk/EndChatWithCounselor")) {
				// TODO: REFACTORY, 해피톡 핫키 처리 메소드

				chatMessage = ChatMessage.builder()
						.signature("ChatMessage")
						.type(ChatMessage.ChatMessageType.END)
						.chatRoomUid(chatRoom.getChatRoomUid())
						.senderUid(chatRoom.getCstmUid())
						.senderDivCd(SENDER_DIV_CD_U)
						.contDivCd(CONT_DIV_CD_T)
						.msgStatusCd(MSG_STATUS_CD_SEND)
						.build();
				end(chatMessage, chatRoom.getCstmUid());

			} else if (CNSR_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) { // 챗봇과 대화 중

				// 로봇에게 메세지 전달하고 결과 메세지 받음
				List<ChatMessage> responseChatMessageList;
				if (customProperty.getCategoryBotMemeberUid().equals(chatRoom.getMemberUid())) { // 분류봇
					responseChatMessageList = categoryBotService.chatWithRobot(chatRoom, chatMessage,
							ChatBotRequestMessageType.QUESTION.name());
				} else { // 해피봇
					responseChatMessageList = chatBotService.chatWithRobot(chatRoom, chatMessage,
							ChatBotRequestMessageType.QUESTION.name());
				}

				Map<String, Object> customerInfo = customerService.selectCustomerByCstmUid(chatRoom.getCstmUid());
				String entityId = String.valueOf(customerInfo.get("coc_id"));

				// 질문 채팅 메세지 시퀀스 세팅
				for (ChatMessage responseChatMessage : responseChatMessageList) {
					responseChatMessage.setRelChatNum(chatMessage.getChatNum());
					// 로봇 메세지 저장
					responseChatMessage.getCont();
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> cont = mapper.readValue(responseChatMessage.getCont(), Map.class);
					ArrayList<Map<String, Object>> chatMessageList = (ArrayList<Map<String, Object>>) cont.get("balloons");
					if (chatMessageList != null && chatMessageList.size() > 0) {
						for (int idx=0; idx < chatMessageList.size(); idx++) {
							ArrayList<Map<String, Object>> sections = (ArrayList<Map<String, Object>>) chatMessageList.get(idx).get("sections");

							for (int i=0; i < sections.size(); i++) {
								String contStr = (String) sections.get(i).get("data");

								if (contStr != null) {
									//고객기본정보
									Map<String, Object> sgd1611p = mcaService.sgd1611p(entityId, CommonConstants.MCA_CHANNEL_CODE);
									String userName = "익명";
									if (sgd1611p.get("CLNT_NAME") != null) {
										userName = String.valueOf(sgd1611p.get("CLNT_NAME"));
									}
									contStr = contStr.replaceFirst("\\{고객명\\}", userName);
									sections.get(i).put("data", contStr);
								}
							}
							chatMessageList.get(idx).put("sections", sections);
							cont.put("balloons", chatMessageList);
						}
					}
					responseChatMessage.setCont(mapper.writeValueAsString(cont));
					chatService.insertChatMessage(responseChatMessage);
					log.debug("chatNum: {}", responseChatMessage.getChatNum());
					// 채팅방 마지막 메세지, 상태 저장
					chatRoom = chatRoomService.setLastChatMessage(chatRoom, responseChatMessage);
					chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), responseChatMessage);
				}

				chatRoomService.saveChatRoom(chatRoom);

			} else if (CHAT_ROOM_STATUS_CD_WAIT_CNSR.equals(chatRoom.getChatRoomStatusCd())) { // 미배정
				// || CHAT_ROOM_STATUS_CD_ASSIGN.equals(chatRoom.getChatRoomStatusCd())) { // 미접수

				// 바쁜 시간 메세지 전달
				Map<String, Object> siteSetting = settingService.selectSiteSetting();
				if ("Y".equals((settingService.getSetting("busy_msg_use_yn", siteSetting)))) {
					String busyMessage = (String) settingService.getSetting("busy_msg", siteSetting);
					ChatMessage busyChatMessage = chatService.saveChatMessage(chatRoom,
							ChatMessage.buildChatMessageText(busyMessage));
					chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), busyChatMessage);

					// 채팅방 마지막 메세지, 상태 저장
					chatRoom = chatRoomService.setLastChatMessage(chatRoom, chatMessage);
					chatRoomService.saveAndNoticeChatRoom(chatRoom);
				}
			}
		}

		return chatMessage;
	}

	/**
	 * 메모 메세지 처리 (client.js: sendMemo() 와 대응)
	 */
	@MessageMapping("/memo")
	public void memo(@Payload ChatMessage chatMessage) throws Exception {

		log.debug("MEMO: {}", chatMessage);

		chatMessage.setCont(StringUtil.replaceHtmlTag(chatMessage.getCont()));

		// 메세지 저장
		chatService.insertChatMessage(chatMessage);

		// 메세지 전달
		if (Strings.isNullOrEmpty(chatMessage.getReceiver())) {
			log.warn("NO RECEIVER OF MEMO");
		}

		// 보낸 사람에게 메세지 전송
		messagingTemplate.convertAndSend(customProperty.getWsQueuePath() + "/" + chatMessage.getSenderUid(),
				chatMessage);

		// 받는 사람에게 메세지 전송 (발신인과 수신인이 같은 경우 건너뜀)
		if (!Strings.isNullOrEmpty(chatMessage.getReceiver())
				&& !chatMessage.getReceiver().equals(chatMessage.getSenderUid())) {
			messagingTemplate.convertAndSend(customProperty.getWsQueuePath() + "/" + chatMessage.getReceiver(),
					chatMessage);
		}
	}

	/**
	 * 메세지 상태 메세지 처리 (client.js: sendStatus() 와 대응)
	 */
	@MessageMapping("/status")
	public void status(@Payload ChatMessage chatMessage) throws Exception {

		log.debug("chatMessage: {}", chatMessage);

		// 메세지 status 필드 업데이트
		if (MSG_STATUS_CD_READ.equals(chatMessage.getMsgStatusCd())) { // 읽음 상태로 변경
			chatService.updateChatMessageStatusAsRead(chatMessage);
		} else { // 그 외 상태 변경
			chatService.updateChatMessageStatus(chatMessage);
		}

		messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatMessage.getChatRoomUid(),
				chatMessage);
	}

	/**
	 * 유저 활동 상태 메세지 처리 (client.js: sendActive() 와 대응)
	 */
	@MessageMapping("/active")
	public void active(@Payload ChatMessage chatMessage) throws Exception {

		log.debug("chatMessage: {}", chatMessage);

		messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatMessage.getChatRoomUid(),
				chatMessage);
	}

	/**
	 * 채팅방 입장 메세지 처리 (client.js: sendJoin() 와 대응)
	 */
	@MessageMapping("/join")
	public void join(@Payload ChatMessage chatMessage, @CookieValue(value = "USER_ID", required = false) String userId)
			throws Exception {

		log.debug("chatMessage: {}", chatMessage);

		messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatMessage.getChatRoomUid(),
				chatMessage);
	}

	/**
	 * 채팅방 종료 메세지 처리 (client.js: sendEnd() 와 대응)
	 */
	@MessageMapping("/end")
	public void end(@Payload ChatMessage chatMessage,
			@CookieValue(value = "USER_ID", required = false) String userId)
					throws Exception {

		log.info("chatMessage: {}, userId: {}", chatMessage, userId);

		Map<String, Object> siteSetting = settingService.selectSiteSetting(); // 사이트 세팅 목록

		log.info("GET CHATROOM BY END: {}", chatMessage.getChatRoomUid());
		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatMessage.getChatRoomUid());
		if (chatRoom == null) {
			log.warn("NOT FOUND ROOM: {}", chatMessage.getChatRoomUid());
			return;
			//throw new UnsupportedOperationException("NOT FOUND ROOM");
		} else if ("Y".equals(chatRoom.getEndYn())) {
			log.warn("END ROOM: {}", chatRoom.getChatRoomUid());
			return;
			//throw new UnsupportedOperationException("ALREADY END ROOM");
		}

		String originalStatus = chatRoom.getChatRoomStatusCd();

		// 종료 메세지
		String endMsgKey = chatRoom.getCstmLinkDivCd() + "_" + "cnsr_end_msg_use_yn";
		log.info("****************" + endMsgKey );
		log.info("****************" + siteSetting.get(endMsgKey) );
		if (SENDER_DIV_CD_C.equals(chatMessage.getSenderDivCd()) && "Y".equals(siteSetting.get(endMsgKey))) { // 상담직원 종료
			if (!CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
				chatMessage.setCont(
						ChatMessage.buildChatMessageHotKeyNewChatRoom((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_" + "cnsr_end_msg")));
			} else {
				chatMessage.setCont(
						ChatMessage.buildChatMessageSectionText((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_" + "cnsr_end_msg")));
			}
		} else if (SENDER_DIV_CD_U.equals(chatMessage.getSenderDivCd())) { // 고객 종료
			if (!CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
				chatMessage.setCont(
						ChatMessage.buildChatMessageHotKeyNewChatRoom((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_" + "cstm_end_msg")));
			} else {
				chatMessage.setCont(
						ChatMessage.buildChatMessageSectionText((String) siteSetting.get(chatRoom.getCstmLinkDivCd() + "_" + "cstm_end_msg")));
			}
		} else if (SENDER_DIV_CD_S.equals(chatMessage.getSenderDivCd())) {
		//상담종료 메시지 사용여부 옵션 추가에 따라 USE YN 'N' 체크
		} else if (SENDER_DIV_CD_C.equals(chatMessage.getSenderDivCd())
				&& "N".equals(siteSetting.get(endMsgKey))) {
			throw new UnsupportedOperationException("SENDER TYPE" + chatMessage.getSenderDivCd() + "IS DISABLED");
		} else if (SENDER_DIV_CD_U.equals(chatMessage.getSenderDivCd())
				&& "N".equals(siteSetting.get(endMsgKey))) {
			throw new UnsupportedOperationException("SENDER TYPE" + chatMessage.getSenderDivCd() + "IS DISABLED");
		} else {
			throw new UnsupportedOperationException("NOT FOUND SENDER TYPE : " + chatMessage.getSenderDivCd());
		}

		// 종료 메세지는 읽음 상태로 전송
		chatMessage.setMsgStatusCd(MSG_STATUS_CD_READ);

		// 채팅 메세지에 상담직원 세팅
		if (chatMessage.getCnsrMemberUid() == null) {
			if (chatRoom.getMemberUid() != null) {
				chatMessage.setCnsrMemberUid(chatRoom.getMemberUid());
			}
		}

		// 메세지 저장
		// 예외처리: 종료 메세지 발행자를 현재 상담직원(챗봇)으로 변경
		String originalSenderDivCd = chatMessage.getSenderDivCd();
		if (SENDER_DIV_CD_U.equals(chatMessage.getSenderDivCd())) {
			chatMessage.setSenderUid(chatRoom.getMemberUid());
			chatMessage.setSenderDivCd(chatRoom.getCnsrDivCd());
		}
		chatService.insertChatMessage(chatMessage);

		// 소켓으로 메세지 전달
		messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatMessage.getChatRoomUid(),
				chatMessage);

		// 채널별 메시지 전달
		if (!CSTM_LINK_DIV_CD_A.equals(chatRoom.getCstmLinkDivCd())) {
			//				&& MEMBER_DIV_CD_C.equals(chatMessage.getSenderDivCd())) {
			channelService.sendMessage(chatRoom, chatMessage);
			channelService.sendClose(chatRoom, ChatContents.Command.end_slient);
			log.info("??????????????????????? A만 아니면 ???? : " + chatRoom.getCstmLinkDivCd());
		}

		//카카오일 경우 메시지 전달
		/*
		 * if (CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
		 * channelService.sendMessage(chatRoom, chatMessage);
		 * channelService.sendClose(chatRoom, ChatContents.Command.end_slient); }
		 */

		// 채팅방 마지막 메세지, 상태 세팅
		chatRoom = chatRoomService.setLastChatMessage(chatRoom, chatMessage);
		log.info("================>>>>>>>>>>>>>>>>>>>>>> 종료 전, chatRoom: {} ", chatRoom);
		// 채팅방 종료 상태 저장
		if (MEMBER_DIV_CD_C.equals(originalSenderDivCd)) { // 상담직원 종료
			chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_CNSR_CNSR, chatMessage.getSenderUid());
		} else if (MEMBER_DIV_CD_U.equals(originalSenderDivCd)) { // 고객 종료
			if (MEMBER_DIV_CD_C.equals(chatRoom.getCnsrDivCd())) { // 상담직원과 대화중
				if (CHAT_ROOM_STATUS_CD_WAIT_CNSR.equals(originalStatus)) { // 미배정 종료
					chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_CNSR_CSTM_WAIT_CNSR, chatMessage.getSenderUid());
					log.info("================>>>>>>>>>>>>>>>>>>>>>> 미배정 종료 , chatRoom: {}, chatMessage: {} ", chatRoom, chatMessage);
				} else if (CHAT_ROOM_STATUS_CD_ASSIGN.equals(originalStatus)) { // 배정 종료
					chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_CNSR_CSTM_ASSIGN_CNSR, chatMessage.getSenderUid());
					log.info("================>>>>>>>>>>>>>>>>>>>>>> 배정 후 고객 종료 , chatRoom: {}, chatMessage: {} ", chatRoom, chatMessage);
				} else {
					chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_CNSR_CSTM, chatMessage.getSenderUid());
					log.info("================>>>>>>>>>>>>>>>>>>>>>> 그외 고객 종료 , chatRoom: {}, chatMessage: {} ", chatRoom, chatMessage);
				}
			} else if (MEMBER_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) { // 챗봇과 대화중
				chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_BOT_CSTM, chatMessage.getSenderUid());
			} else {
				log.error("NOT FOUND COUNSELOR TYPE: {}", chatRoom.getCnsrDivCd());
				throw new UnsupportedOperationException("NOT FOUND COUNSELOR TYPE" + chatRoom.getCnsrDivCd());
			}
		} else {
			chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_NORMAL, customProperty.getSystemMemeberUid());
		}
		// 채팅방 정보 발행
		messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new NoticeMessage(chatRoom));
	}

	/**
	 * 매니저, 채팅 메세지 수정
	 *
	 * <p>
	 * 클라이언트: Ajax 호출 -> 서블릿: 메세지 발행 -> ChatMessageController.edit()
	 */
	@MessageMapping("/edit")
	public void edit(@Payload ChatMessage chatMessage) throws Exception {

		log.debug("chatMessage: {}", chatMessage);

		ChatMessage originChatMessage = chatService.selectChatMessageByChatNum(chatMessage.getChatNum());
		originChatMessage.setCont(chatMessage.getCont());

		// 메세지 저장
		chatService.updateChatMessage(originChatMessage);

		// 메세지 전달
		chatMessage.setChatRoomUid(originChatMessage.getChatRoomUid());
		messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + originChatMessage.getChatRoomUid(),
				chatMessage);
	}

	/**
	 * 매니저, 채팅 메세지 삭제
	 *
	 * <p>
	 * 클라이언트: Ajax 호출 -> 서블릿: 메세지 발행 -> ChatMessageController.edit()
	 */
	@MessageMapping("/remove")
	public void remove(@Payload ChatMessage chatMessage) throws Exception {

		log.debug("chatMessage: {}", chatMessage);

		ChatMessage originChatMessage = chatService.selectChatMessageByChatNum(chatMessage.getChatNum());

		// 메세지 삭제
		chatService.deleteChatMessage(originChatMessage.getChatNum());

		// 메세지 전달
		chatMessage.setChatRoomUid(originChatMessage.getChatRoomUid());
		messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + originChatMessage.getChatRoomUid(),
				chatMessage);
	}

	/**
	 * 개인 메세지 처리, 서블릿단에서만 보냄 (클라이언트에서 보내는 경우는 없는 것으로 변경함)
	 */
	@MessageMapping("/whisper")
	public void whisper(@Payload ChatMessage chatMessage) throws Exception {

		log.debug("chatMessage: {}", chatMessage);

		messagingTemplate.convertAndSend(customProperty.getWsQueuePath() + "/" + chatMessage.getReceiver(),
				chatMessage);
	}
}
