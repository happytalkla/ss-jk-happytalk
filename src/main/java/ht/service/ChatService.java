package ht.service;

import static ht.constants.CommonConstants.CONT_DIV_CD_L;
import static ht.constants.CommonConstants.CONT_DIV_CD_M;
import static ht.constants.CommonConstants.CONT_DIV_CD_T;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_C;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_R;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_S;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_U;
import static ht.constants.CommonConstants.MSG_STATUS_CD_READ;
import static ht.constants.CommonConstants.MSG_STATUS_CD_RECEIVE;
import static ht.constants.CommonConstants.MSG_STATUS_CD_SEND;
import static ht.constants.CommonConstants.SENDER_DIV_CD_R;
import static ht.constants.CommonConstants.SENDER_DIV_CD_S;
import static ht.constants.CommonConstants.SENDER_DIV_CD_U;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.controller.WebSocketController;
import ht.domain.ChatContents;
import ht.domain.ChatContents.Section;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatMessageType;
import ht.domain.ChatRoom;
import ht.domain.chatbot.ChatBotRequestMessageType;
import ht.persistence.ChatDao;
import ht.service.channel.ChannelService;
import ht.service.chatbot.ChatBotService;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatService {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private SimpMessageSendingOperations messagingTemplate;

	@Resource
	private ChatDao chatDao;
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
	private CommonService commonService;
	@Resource
	private WebSocketController webSocketController;
	@Resource
	private ObjectMapper objectMapper;

	/**
	 * 채팅 메세지 조회
	 */
	public ChatMessage selectChatMessageByChatNum(@NotNull @Positive Long chatNum) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatNum", chatNum);
		Map<String, Object> map = chatDao.selectChatMessage(params);
		if (map != null) {
			ChatMessage chatMessage = new ChatMessage();
			return chatMessage.fromMap(map);
		} else {
			return null;
		}
	}

	/**
	 * 채팅 내역 조회
	 */
	public List<ChatMessage> selectChatMessageListByChatRoomUid(String chatRoomUid, String chatNumGt, String chatNumLt,
			Integer size, String sorter, boolean withMemo, boolean withLog) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		if (chatNumLt != null) { // 이전 메세지 목록
			params.put("chatNumLt", chatNumLt);
		} else { // 이후 메세지 목록
			params.put("chatNumGt", chatNumGt);
		}
		params.put("size", size);
		switch (sorter) {
			case "chat_num+asc":
				params.put("sorter", "chat_num asc");
				break;
			case "chat_num+desc":
			default:
				params.put("sorter", "chat_num desc");
				break;
		}
		if (withMemo && withLog) {
			;
		} else {
			if (!withMemo && !withLog) {
				params.put("contDivCd", CONT_DIV_CD_T);
			} else if (!withMemo) {
				params.put("contDivCdNot", CONT_DIV_CD_M);
			} else if (!withLog) {
				params.put("contDivCdNot", CONT_DIV_CD_L);
			}
		}

		List<Map<String, Object>> list = chatDao.selectChatMessageList(params);
		List<ChatMessage> chatMessageList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			ChatMessage chatMessage = new ChatMessage();
			chatMessageList.add(chatMessage.fromMap(map));
		}

		return chatMessageList;
	}

	/**
	 * 채팅 내역 조회
	 */
	public List<ChatMessage> selectChatMessageListByChatRoomUid(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		List<Map<String, Object>> list = chatDao.selectChatMessageList(params);
		List<ChatMessage> chatMessageList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			ChatMessage chatMessage = new ChatMessage();
			chatMessageList.add(chatMessage.fromMap(map));
		}

		return chatMessageList;
	}

	/**
	 * 이전 채팅 내역 조회
	 */
	public List<ChatMessage> selectChatPrevMassageListBycstmUid(String cstmUid, String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		int size = 50;			//이전채팅 내용 개수
		params.put("chatRoomUid", chatRoomUid);
		params.put("cstmUid", cstmUid);
		params.put("size", size);

		List<Map<String, Object>> list = chatDao.selectChatPrevMessageList(params);
		List<ChatMessage> chatMessageList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			ChatMessage chatMessage = new ChatMessage();
			chatMessageList.add(chatMessage.fromMap(map));
		}

		return chatMessageList;
	}

	/**
	 * 채팅 메세지 생성
	 */
	@Transactional
	public int insertChatMessage(ChatMessage chatMessage) {

		chatMessage.setRegDt(commonService.selectSysdate());
		Map<String, Object> map = chatMessage.toMap();
		int d = chatDao.insertChatMessage(map);
		// log.error("{}", map);
		// chatMessage.setChatNum(((BigDecimal) map.get("chatNum")).longValue());
		chatMessage.setChatNum((Long) map.get("chatNum"));

		assert (d == 1);
		return d;
	}

	/**
	 * 채팅 메세지 변경
	 */
	@Transactional
	public int updateChatMessage(ChatMessage chatMessage) {

		return chatDao.updateChatMessage(chatMessage.toMap());
	}

	/**
	 * 채팅 메세지 상태 변경
	 */
	@Transactional
	public int updateChatMessageStatus(ChatMessage chatMessage) {

		assert (chatMessage.getType() == ChatMessageType.STATUS);

		Map<String, Object> params = new HashMap<>();
		params.put("chatNum", chatMessage.getChatNum());
		params.put("msgStatusCd", chatMessage.getMsgStatusCd());

		log.debug("msgStatusCd: {}", params.get("msgStatusCd"));

		return chatDao.updateChatMessage(params);
	}

	/**
	 * 채팅 메세지 읽음 표시, 상대방이 보낸 채팅 메세지의 상태를 모두 읽음으로 업데이트 함
	 */
	@Transactional
	public int updateChatMessageStatusAsRead(ChatMessage chatMessage) {

		assert (chatMessage.getType() == ChatMessageType.STATUS);

		Map<String, Object> params = new HashMap<>();
		params.put("shrChatRoomUid", chatMessage.getChatRoomUid());
		params.put("shrSenderUidNot", chatMessage.getSenderUid());
		params.put("shrChatNumLessEqual", chatMessage.getChatNum());
		params.put("shrMsgStatusCdList", Arrays.asList(MSG_STATUS_CD_SEND, MSG_STATUS_CD_RECEIVE));
		params.put("msgStatusCd", MSG_STATUS_CD_READ);

		log.debug("msgStatusCd: {}", params.get("msgStatusCd"));

		return chatDao.updateChatMessage(params);
	}

	/**
	 * 채팅 메세지 컨텐츠 변경
	 */
	@Transactional
	public int updateChatMessageCont(ChatMessage chatMessage) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatNum", chatMessage.getChatNum());
		params.put("cont", chatMessage.getCont());
		return chatDao.updateChatMessage(params);
	}

	/**
	 * 채팅 메세지 삭제
	 */
	@Transactional
	public int deleteChatMessage(Long chatNum) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatNum", chatNum);
		return chatDao.deleteChatMessage(params);
	}

	/**
	 * 채팅 메세지 삭제
	 */
	@Transactional
	public int deleteChatMessage(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		return chatDao.deleteChatMessage(params);
	}

	/**
	 * 텍스트 타입 채팅 메세지 생성 및 저장
	 */
	public ChatMessage saveChatMessage(ChatRoom chatRoom, String buildMessageContents) {

		ChatMessage chatMessage = buildChatMessage(chatRoom, buildMessageContents);
		insertChatMessage(chatMessage);
		return chatMessage;
	}

	/**
	 * 채팅 메세지 생성 및 저장
	 */
	@Transactional
	public ChatMessage saveChatMessage(ChatRoom chatRoom, String buildMessageContents, String contentsType) {

		ChatMessage chatMessage = buildChatMessage(chatRoom, buildMessageContents, contentsType);
		insertChatMessage(chatMessage);
		return chatMessage;
	}

	/**
	 * 채팅 메세지 생성 및 저장
	 */
	public ChatMessage buildChatMessage(ChatRoom chatRoom, String buildMessageContents) {

		return buildChatMessage(chatRoom, buildMessageContents, CONT_DIV_CD_T);
	}

	/**
	 * 채팅 메세지 생성 및 저장
	 */
	public ChatMessage buildChatMessage(ChatRoom chatRoom, String buildMessageContents, String contentsType) {

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setChatRoomUid(chatRoom.getChatRoomUid());
		chatMessage.setCont(buildMessageContents);
		chatMessage.setContDivCd(contentsType);
		chatMessage.setMsgStatusCd(MSG_STATUS_CD_SEND);
		if (chatRoom.getMemberUid() == null) {
			chatMessage.setSenderDivCd(MEMBER_DIV_CD_C);
			chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
		} else {
			chatMessage.setSenderDivCd(chatRoom.getCnsrDivCd());
			chatMessage.setSenderUid(chatRoom.getMemberUid());
		}
		chatMessage.setType(ChatMessageType.MESSAGE);

		return chatMessage;
	}

	/**
	 * 로그타입 채팅 메세지 생성 및 저장
	 */
	public ChatMessage buildLogChatMessage(ChatRoom chatRoom, String logCode) {

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setChatRoomUid(chatRoom.getChatRoomUid());
		chatMessage.setBotIntent(logCode);
		chatMessage.setCont(ChatMessage.buildChatMessageText(CommonConstants.getLogChatMessage(logCode)));
		chatMessage.setContDivCd(CONT_DIV_CD_L);
		chatMessage.setMsgStatusCd(MSG_STATUS_CD_SEND);
		//		if (chatRoom.getMemberUid() == null) {
		//			chatMessage.setSenderDivCd(MEMBER_DIV_CD_C);
		//			chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
		//		} else {
		//			chatMessage.setSenderDivCd(chatRoom.getCnsrDivCd());
		//			chatMessage.setSenderUid(chatRoom.getMemberUid());
		//		}
		chatMessage.setSenderDivCd(MEMBER_DIV_CD_S);
		chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
		chatMessage.setType(ChatMessageType.MESSAGE);

		return chatMessage;
	}

	/**
	 * 인사말 매세지 생성 및 저장
	 */
	public List<ChatMessage> addGreetMessage(ChatRoom chatRoom, Map<String, Object> siteSetting, String cnsrDivCd)
			throws Exception
	{
		return addGreetMessage(chatRoom, siteSetting, cnsrDivCd, false);
	}

	/**
	 * 인사말 매세지 생성 및 저장
	 *
	 * @param withSaveRepository true: DB 저장, false: 소켓으로 발송
	 */
	public List<ChatMessage> addGreetMessage(ChatRoom chatRoom, Map<String, Object> siteSetting, String cnsrDivCd,
			boolean withSaveRepository)
					throws Exception
	{
		List<ChatMessage> chatMessageList = new ArrayList<>();

		if (MEMBER_DIV_CD_C.equals(cnsrDivCd)) { // 상담 대상이 상담원이면

			ChatContents chatContents = new ChatContents();
			boolean isUseGreetingMessage = false;

			//부서별 사용 안함
			if ("Y".equals(siteSetting.get(chatRoom.getCstmLinkDivCd()+ "_" + "cns_frt_msg_text_use_yn"))) {
				isUseGreetingMessage = true;
			}


//			if (DEPART_CD_TM.equals(chatRoom.getDepartCd())) {
//				if ("Y".equals(siteSetting.get("cns_frt_msg_text_use_yn"))) {
//					isUseGreetingMessage = true;
//				}
//			} else {
//				if ("Y".equals(siteSetting.get("cs_cns_frt_msg_text_use_yn"))) {
//					isUseGreetingMessage = true;
//				}
//			}

			String greetingImage = Strings.nullToEmpty((String) siteSetting.get(chatRoom.getCstmLinkDivCd()+ "_" + "cns_frt_msg_img"));
			String greetingMessage = Strings.nullToEmpty((String) siteSetting.get(chatRoom.getCstmLinkDivCd()+ "_" + "cns_frt_msg"));
			
//			if (DEPART_CD_TM.equals(chatRoom.getDepartCd())) {
//				greetingImage = Strings.nullToEmpty((String) siteSetting.get("cns_frt_msg_img"));
//				greetingMessage = Strings.nullToEmpty((String) siteSetting.get("cns_frt_msg"));
//			}

			// 상담 인사말 메세지 저장
			if (isUseGreetingMessage) {
				if (!Strings.isNullOrEmpty(greetingImage)) {
					chatContents.add(ChatContents.Section.builder()
							.type(ChatContents.Section.SectionType.file)
							.data(greetingImage)
							.display("image/png")
							.build());
				}
				if (!Strings.isNullOrEmpty(greetingMessage)) {
					chatContents.add(ChatContents.Section.builder()
							.type(ChatContents.Section.SectionType.text)
							.data(greetingMessage)
							.build());
				}

				ChatMessage chatMessage = buildChatMessage(chatRoom, new Gson().toJson(chatContents));
				chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
				chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
				chatMessageList.add(chatMessage);

				// 채팅방 마지막 메세지 저장
				if (chatMessage != null) {
					if (withSaveRepository) {
						insertChatMessage(chatMessage);
						chatRoomService.setLastChatMessage(chatRoom, chatMessage);
						chatRoomService.saveAndNoticeChatRoom(chatRoom);
					}
				}
			}

		} else if (MEMBER_DIV_CD_R.equals(cnsrDivCd)) { // 상담 대상이 챗봇이면

			if (customProperty.getHappyBotMemeberUid().equals(chatRoom.getMemberUid())) { // 해피봇
				ChatMessage requestChatMessage = new ChatMessage();
				requestChatMessage.setChatRoomUid(chatRoom.getChatRoomUid());
				requestChatMessage.setSenderUid(chatRoom.getCstmUid());
				//				List<ChatMessage> chatMessageList = chatBotService.chatWithRobot(chatRoom, requestChatMessage,
				//						ChatBotRequestMessageType.GREETING.name());
				//				if (withSaveRepository) {
				//					for (ChatMessage message : chatMessageList) {
				//						chatMessage = message;
				//						insertChatMessage(chatMessage);
				//					}
				//				}

				chatMessageList.addAll(chatBotService.chatWithRobot(chatRoom, requestChatMessage,
						ChatBotRequestMessageType.GREETING.name()));
				if (withSaveRepository) {
					for (ChatMessage chatMessage : chatMessageList) {
						insertChatMessage(chatMessage);
						// 채팅방 마지막 메세지 저장
						chatRoomService.saveLastChatMessage(chatRoom, chatMessage);
						chatRoomService.saveAndNoticeChatRoom(chatRoom);
					}
				}
			} else if (customProperty.getCategoryBotMemeberUid().equals(chatRoom.getMemberUid())) { // 분류봇

				chatMessageList = categoryBotService.chatWithRobot(
						chatRoom,
						ChatMessage.builder()
						.type(ChatMessage.ChatMessageType.MESSAGE)
						.chatRoomUid(chatRoom.getChatRoomUid())
						.senderUid(chatRoom.getCstmUid())
						.senderDivCd(MEMBER_DIV_CD_R)
						.msgStatusCd(MSG_STATUS_CD_READ)
						.contDivCd(CONT_DIV_CD_T)
						.cnsrMemberUid(chatRoom.getCnsrDivCd())
						.build(),
						ChatBotRequestMessageType.QUESTION.name());

				if (withSaveRepository) {
					for (ChatMessage chatMessage : chatMessageList) {
						insertChatMessage(chatMessage);
						// 채팅방 마지막 메세지 저장
						chatRoomService.saveLastChatMessage(chatRoom, chatMessage);
						chatRoomService.saveAndNoticeChatRoom(chatRoom);
					}
				}
			} else {
				log.error("NOT FOUND BOT MEMBER: {}", chatRoom.getMemberUid());
			}
		} else {
			log.error("NO COUNSELOR TYPE: {}", cnsrDivCd);
			throw new UnsupportedOperationException("NO COUNSELOR TYPE: " + cnsrDivCd);
		}

		return chatMessageList;
	}

	/**
	 * 고객 채팅 종료시 평가
	 */
	@Transactional
	public int saveCnsEvl(String chatRoomUid, String cstmUid, int star, String statement) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("cstmUid", cstmUid);
		params.put("evlCont", statement);
		params.put("evlScore", star);

		return chatDao.saveCnsEvl(params);
	}

	/**
	 * 종료 정보 저장
	 */
	@Transactional
	public int saveChatEndInfo(Map<String, Object> params) {

		//chatDao.saveChatRoomCtgNum(params);
		return chatDao.saveChatEndInfo(params);
	}

	/**
	 * 상담종료 후처리 정보 조회
	 */
	public Map<String, Object> selectChatEndInfoByChatRoomUid(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);

		return chatDao.selectChatEndInfo(params);
	}

	/**
	 * 상담종료 후처리 팝업용 정보 조회
	 */
	public Map<String, Object> selectChatEndInfoPop(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);

		return chatDao.selectChatEndInfoPop(params);
	}

	/**
	 * 상담내용 검토 요청 조회, 기존에 있는지 확인
	 */
	public boolean hasNotCompletedChatContReview(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("compYn", "N");
		boolean ret= true;
		List<Map<String, Object>> retData = chatDao.selectChatContReview(params);
		if (retData.size()==0) {
			ret= false;
		}
		//ret = chatDao.selectChatContReview(params).isEmpty();
		return ret;
	}

	/**
	 * 상담내용 검토 요청 등록
	 */
	@Transactional
	public int insertChatContReview(String chatRoomUid, String managerUid, String commiter) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("managerUid", managerUid);
		params.put("compYn", "N");
		params.put("creater", commiter);

		return chatDao.insertChatContReview(params);
	}

	/**
	 * 상담내용 검토 완료
	 */
	@Transactional
	public int updateChatContReview(String reviewReqNum, String updater) {

		Map<String, Object> params = new HashMap<>();
		params.put("reviewReqNum", reviewReqNum);
		params.put("compYn", "Y");
		params.put("updater", updater);

		return chatDao.updateChatContReview(params);
	}

	/**
	 * 챗봇과 대화 이력
	 */
	public List<Map<String, Object>> selectChatWithBot(String startDate, String endDate, String id, String div,
			String name, String intent) {

		List<Map<String, Object>> chatRoomList = chatDao.selectChatRoomWithBot(startDate, endDate, id, div, name,
				intent);
		if (!chatRoomList.isEmpty()) {

			Set<String> chatRoomUidList = chatRoomList.stream().map(map -> (String) map.get("chat_room_uid"))
					.collect(Collectors.toSet());

			int respCnt = 0;
			int chatCnt = 0;
			List<Map<String, Object>> chatList = chatDao.selectChatWithBot(chatRoomUidList, intent);
			for (Map<String, Object> chatRoom : chatRoomList) {
				List<Map<String, Object>> projectionChatList = new ArrayList<>();
				Map<String, Object> currentChatSet = null;
				for (Map<String, Object> chat : chatList) {

					if ((chatRoom.get("chat_room_uid")).equals(chat.get("chat_room_uid"))) {
						if ((intent.equals("1")
								&& (StringUtil.nvl(chat.get("bot_intent")).equals("Default_Fallback_Intent")))
								||
								(intent.equals("2")
										&& !(StringUtil.nvl(chat.get("bot_intent")).equals("Default_Fallback_Intent")))
								||
								(intent.equals("3"))) {

							if (SENDER_DIV_CD_U.equals(chat.get("SENDER_DIV_CD"))) {
								if (!(intent.equals("2") && StringUtil.nvl(chatList.get(chatCnt + 1).get("bot_intent"))
										.equals("Default_Fallback_Intent"))) {
									currentChatSet = new HashMap<>();
									projectionChatList.add(currentChatSet);
									currentChatSet.put("cstm_chat_num", chat.get("chat_num"));
									currentChatSet.put("cstm_uid", chat.get("sender_uid"));
									currentChatSet.put("reg_date", chat.get("reg_dt"));

									try {
//										Clob clob = (Clob) chat.get("cont_text");
//										currentChatSet.put("cstm_cont", StringUtil.clobToString(clob));
										currentChatSet.put("cstm_cont", chat.get("cont_text"));
									} catch (Exception e) {
										log.error(e.getLocalizedMessage());
									}
								}

							} else if (SENDER_DIV_CD_R.equals(chat.get("SENDER_DIV_CD"))) {

								if ((intent.equals("1") && (StringUtil.nvl(chat.get("bot_intent"))
										.equals("Default_Fallback_Intent")))) {
									currentChatSet = new HashMap<>();
									projectionChatList.add(currentChatSet);

									currentChatSet.put("cstm_chat_num", chatList.get(chatCnt - 1).get("chat_num"));
									currentChatSet.put("cstm_uid", chatList.get(chatCnt - 1).get("sender_uid"));

									try {
//										Clob clob = (Clob) chatList.get(chatCnt - 1).get("cont_text");
//										currentChatSet.put("cstm_cont", StringUtil.clobToString(clob));
										currentChatSet.put("cstm_cont", chatList.get(chatCnt - 1).get("cont_text"));
									} catch (Exception e) {
										log.error(e.getLocalizedMessage());
									}

								}
								if (currentChatSet != null) {
									currentChatSet.put("bot_chat_num", chat.get("chat_num"));
									currentChatSet.put("bot_uid", chat.get("sender_uid"));
									currentChatSet.put("evl_type_cd", chat.get("evl_type_cd"));
									try {
//										Clob clob = (Clob) chat.get("cont_text");
//										currentChatSet.put("bot_cont", StringUtil.clobToString(clob));
										currentChatSet.put("bot_cont", chat.get("cont_text"));
//										Clob clobRaw = (Clob) chat.get("cont");
//										currentChatSet.put("bot_cont_raw", StringUtil.clobToString(clobRaw));
										currentChatSet.put("bot_cont_raw", chat.get("cont"));
									} catch (Exception e) {
										log.error("{}", e.getLocalizedMessage(), e);
									}
									currentChatSet.put("bot_intent", chat.get("bot_intent"));
									respCnt++;
								}

							}
						}
					}
					chatCnt++;
				}

				currentChatSet = null;

				chatRoom.put("chat_list", projectionChatList);

				chatRoom.put("respCnt", respCnt);

				respCnt = 0;
				chatCnt = 0;

			}

			return chatRoomList;
		}

		return Collections.emptyList();
	}

	/**
	 * 챗봇과 대화 이력 (엑셀용)
	 */
	public List<Map<String, Object>> selectChatWithBotForExcel(String startDate, String endDate) {

		List<Map<String, Object>> listMap = chatDao.selectChatWithBotForExcel(startDate, endDate);
		for (Map<String, Object> map : listMap) {
			try {
//				Clob clobQuestion = (Clob) map.get("cont");
//				map.put("cont", StringUtil.clobToString(clobQuestion));
				map.put("cont", map.get("cont"));
//				Clob clobReply = (Clob) map.get("cont_text");
//				map.put("cont_text", StringUtil.clobToString(clobReply));
				map.put("cont_text", map.get("cont_text"));
			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage(), e);
			}
		}

		return listMap;
	}

	/**
	 * 챗봇과 대화 이력
	 */
	public List<Map<String, Object>> selectChatWithBot2(Map<String, Object> param) {
		List<Map<String, Object>> rtnList = chatDao.selectChatWithBot2(param);
		if(rtnList != null && rtnList.size() > 0) {
			for(Map<String, Object> map : rtnList) {
				try {
//					Clob clobQuestion = (Clob) map.get("question");
//					map.put("question_text", StringUtil.clobToString(clobQuestion));
					map.put("question_text", map.get("question"));
//					Clob clobReply = (Clob) map.get("answer");
//					map.put("answer_text", StringUtil.clobToString(clobReply));
					map.put("answer_text", map.get("answer"));
				} catch (Exception e) {
					log.error("{}", e.getLocalizedMessage(), e);
				}
			}
		}

		return rtnList;
	}

	/**
	 * TODO: DELETEME, 임시오픈시만 사용
	 * 챗봇 답변 평가 버튼
	 */
	@Transactional
	public int evlChatMessage(Long chatNum, String evl) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatNum", chatNum);
		params.put("evl", evl);

		return chatDao.evlChatMessage(params);
	}

	/**
	 * 소켓과 채널로 메세지 전송
	 */
	@Transactional
	public void sendMessage(@NotNull @Valid ChatRoom chatRoom, @NotEmpty String chatRoomUid, @NotNull @Valid ChatMessage chatMessage) {

		// 소켓으로 메세지 발송
		messagingTemplate.convertAndSend(customProperty.getWsTopicPath() + "/" + chatRoomUid, chatMessage);
		log.info("******************** chatRoom:{}, chatMessage:" , chatRoom, chatMessage);
		// 채널로 메세지 발송
//		if (!CSTM_LINK_DIV_CD_A.equals(chatRoom.getCstmLinkDivCd())
//				&& !MEMBER_DIV_CD_U.equals(chatMessage.getSenderDivCd())) {
//			try {
//				channelService.sendMessage(chatRoom, chatMessage);
//			} catch (Exception e) {
//				log.error("{}", e.getLocalizedMessage(), e);
//			}
//		}
		if (!MEMBER_DIV_CD_U.equals(chatMessage.getSenderDivCd())) {
			try {
				channelService.sendMessage(chatRoom, chatMessage);
			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * 텍스트 명령어 변환 (상담원 연결 등)
	 * TODO: 형태소 분석
	 */
	public String parseNatureLanguageMessage(@NotEmpty String cont) {

		log.debug("NatureLanguageMessage: {}", cont);
		try {
			ChatContents chatContents = objectMapper.readValue(cont, ChatContents.class);
			if (chatContents.isTextOnlyMessage()) {
				String summary = chatContents.getSummary();
				String noWhiteSpaceSummary = summary.replaceAll("\\+s", "");
				log.debug("noWhiteSpaceSummary: {}", noWhiteSpaceSummary);
				if (noWhiteSpaceSummary.startsWith("상담직원연결")) {
					Section section = chatContents.getLastBalloon().getSections().get(0);
					if (Strings.isNullOrEmpty(section.getExtra())) {
						section.setExtra("HappyTalk/RequestCounselor");
						return objectMapper.writeValueAsString(chatContents);
					}
				}
			}
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return cont;
	}
}
