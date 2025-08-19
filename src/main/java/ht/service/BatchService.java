package ht.service;

import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_WAIT_CNSR;
import static ht.constants.CommonConstants.CHNG_DIV_CD_AUTO;
import static ht.constants.CommonConstants.CNSR_DIV_CD_C;
import static ht.constants.CommonConstants.CNSR_DIV_CD_R;
import static ht.constants.CommonConstants.CONT_DIV_CD_T;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_B;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_C;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_D;
import static ht.constants.CommonConstants.END_DIV_CD_BOT_TIME;
import static ht.constants.CommonConstants.END_DIV_CD_CNSR_TIME;
import static ht.constants.CommonConstants.LOG_CODE_ASSIGN_COUNSELOR;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_C;
import static ht.constants.CommonConstants.MSG_STATUS_CD_READ;
import static ht.constants.CommonConstants.MSG_STATUS_CD_SEND;
import static ht.constants.CommonConstants.SENDER_DIV_CD_S;
import static ht.constants.CommonConstants.SENDER_DIV_CD_U;
import static ht.constants.MessageConstants.ASSIGNED;
import static ht.constants.MessageConstants.CHATBOT_END_CHATROOM;
import static ht.constants.MessageConstants.KAKABOT_END_CHATROOM;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.joda.time.DateTime;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatCommand;
import ht.domain.ChatMessage.ChatMessageType;
import ht.domain.ChatRoom;
import ht.domain.ChatRoom.AssignResult;
import ht.domain.NoticeMessage;
import ht.exception.BizException;
import ht.persistence.BatchDao;
import ht.persistence.ChatDao;
import ht.service.chatbot.HappyBotService;
import ht.util.CipherUtils;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

@Component("batchService")
@Slf4j
public class BatchService {

	@Resource
	private CustomProperty customProperty;

	@Resource
	private SimpMessageSendingOperations messagingTemplate;
	@Resource
	private ChatDao chatDao;
	@Resource
	private BatchDao batchDao;
	@Resource
	private SettingService settingService;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private AssignService assignService;
	@Resource
	private ReportService reportService;
	@Resource
	private MemberService memberService;
	@Resource
	private CommonService commonService;
	@Resource
	private EaiUmsService eaiUmsService;
	@Resource
	private HappyBotService happyBotService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private EndCtgService endCtgService;
	@Resource
	private GenesysService genesysService;
	@Resource
	private HTUtils htUtils;
	@Resource
	private CipherUtils cipherUtils;

	/**
	 * 배정 스케줄러
	 */
	@Scheduled(initialDelay = 500 * 60, fixedDelay = 1000 * 10)
	@Transactional
	public void runAssignScheduler() {

		if (htUtils.isActiveProfile("local")) { return; }

		long now = System.currentTimeMillis();
		log.info(">>> RUN ASSIGN SCHEDULER, {}", now);

		try {
			if (htUtils.isActiveProfile("live2")) { Thread.sleep(10000); }  // 중복실행방지용 2번was 지연처리
			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_ASSIGN, "NOT_DAY");
			log.info(">>> START ASSIGN SCHEDULER CHECK, {}", check);
			if (check) {
				log.info(">>> START ASSIGN SCHEDULER, {}", now);
				Map<String, Object> siteSetting = settingService.selectSiteSetting();
				List<Map<String, Object>> jobList = settingService.selectAssignSchedulerList(null);
				for (Map<String, Object> job : jobList) {
					
					String chatRoomUid = (String) job.get("chat_room_uid");
					log.info("ASSIGN SCHEDULER, {}, JOB: {}", now, chatRoomUid);
					ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
					
					//배정 스케줄 정보가 있으면 임시로 근무시간외 접수 허용
					siteSetting.put(chatRoom.getCstmLinkDivCd() + "_unsocial_accept_yn", "Y");
					log.info("=========================== : " + siteSetting);
					
					//채널별로 분기가 되기 때문에 강제 세팅한 값 삭제 2020.04.21 tobe
//					chatRoom.setDepartCd(DEPART_CD_CS); // TODO: DELETEME, 혹시 몰라서 강제 세팅 < asis

					try {
						if (chatRoom != null && CHAT_ROOM_STATUS_CD_WAIT_CNSR.equals(chatRoom.getChatRoomStatusCd())) {
							AssignResult assignResult = assignService.assignCounselorByCustomer(chatRoom, siteSetting, "B");
							log.info("ASSIGN SCHEDULER, {}, RESULT: {}, {}", now, chatRoomUid, assignResult);

							// ////////////////////////////////////////////////
							// 정상 배정됨
							if (AssignResult.SUCCEED.equals(assignResult)) {
								// 스케줄 삭제
								settingService.deleteAssignScheduler((String) job.get("chat_room_uid"));
								// 결과 메세지
								// ChatMessage chatMessage = null;
								// if (MEMBER_DIV_CD_R.equals(chatRoom.getFrtCnsrDivCd())) { // 로봇과 대화중 상담원 배정이면
								// 인사말
								// chatMessage = chatService.addGreetMessage(chatRoom, siteSetting,
								// CNSR_DIV_CD_C, true);
								// }

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


								ChatMessage chatMessage = chatService.saveChatMessage(chatRoom,
										ChatMessage.buildChatMessageText(ASSIGNED));
								chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
								chatMessage.setSenderUid(customProperty.getSystemMemeberUid());

								// 로그 메세지 저장
								chatService.insertChatMessage(
										chatService.buildLogChatMessage(chatRoom, LOG_CODE_ASSIGN_COUNSELOR));

								// 상담원 변경 이력 저장
								assignService.insertChatCnsrChngHis(chatRoom.getChatRoomUid(),
										ChatCommand.CHANGE_COUNSELOR, customProperty.getSystemMemeberUid(),
										customProperty.getSystemMemeberUid(), null, chatRoom.getMemberUid(),
										CHNG_DIV_CD_AUTO, MEMBER_DIV_CD_C);

								// 채팅방 마지막 메세지 세팅
								chatRoomService.setLastChatMessage(chatRoom, chatMessage);
								// 채팅방 반영
								log.info("ASSIGN SCHEDULER, {}, SUCCEED ASSIGN, JOB: {}", now, chatRoomUid);

								/////  중복배정 방지
								boolean checkDup = commonService.checkBatchStart(CommonConstants.BATCH_JOB_DIRECTASSIGN, "NOT_DAY");

								if (checkDup) {
									// 채팅방 반영
									log.info(">>>>>>>>>>>>>>>>>> SCHEDULER LOCK START ");
									chatRoomService.saveAndNoticeChatRoom(chatRoom);
								}
								/////  중복배정 방지 LOCK 해제
								log.info(">>>>>>>>>>>>>>>>>> SCHEDULER PASTEN ");
								commonService.checkBatchEnd(CommonConstants.BATCH_JOB_DIRECTASSIGN, "NOT_DAY");
								// 메세지 전달
								chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), chatMessage);

								// push msessage 전송
								
								if( !chatRoom.getCstmCocId().isEmpty()) {				/// entity_id 필수
									if(chatRoom.getCstmLinkDivCd().equals(CSTM_LINK_DIV_CD_C) || chatRoom.getCstmLinkDivCd().equals(CSTM_LINK_DIV_CD_D)) { 	// O2, mPOP 전용
										Map<String, Object> eaiParam = new HashMap<String, Object>();
										eaiParam.put("cstmLinkDivCd", chatRoom.getCstmLinkDivCd());
										eaiParam.put("entityId", chatRoom.getCstmCocId());
										eaiParam.put("pushType", "대기고객 상담원 배정");
										eaiParam.put("pushMsg", "상담직원이 배정되었습니다.");
										eaiUmsService.throwEai(eaiParam);
									}
								}
								
							}
							// ////////////////////////////////////////////////
							// 상담불가
							/*
							 * else if (AssignResult.NO_COUNSELOR.equals(assignResult)) { // 스케줄 삭제
							 * settingService.deleteAssignScheduler((String) job.get("chat_room_uid")); //
							 * 결과 메세지 (상담불가 메세지) ChatMessage chatMessage =
							 * chatService.buildChatMessage(chatRoom,
							 * ChatMessage.buildChatMessageText((String)
							 * siteSetting.get("cs_not_cns_msg")));
							 * chatMessage.setType(ChatMessageType.END);
							 * chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
							 * chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
							 * chatService.insertChatMessage(chatMessage);
							 *
							 * // 채팅방 마지막 메세지 세팅 chatRoomService.setLastChatMessage(chatRoom, chatMessage);
							 * // 채팅방 반영 chatRoomService.endChatRoom(chatRoom, chatMessage,
							 * END_DIV_CD_NORMAL, customProperty.getSystemMemeberUid());
							 *
							 * log.info("ASSIGN SCHEDULER, {}, FAILED NO COUNSELOR, END CHATROOM, JOB: {}",
							 * now, chatRoomUid); }
							 */
							// ////////////////////////////////////////////////
							// 근무시간외
							/*
							 * else if (AssignResult.CENTER_OFF_DUTY.equals(assignResult)) { // 스케줄 삭제
							 * settingService.deleteAssignScheduler((String) job.get("chat_room_uid")); //
							 * 결과 메세지 (근무시간외 메세지) ChatMessage chatMessage =
							 * chatService.buildChatMessage(chatRoom,
							 * ChatMessage.buildChatMessageText((String) siteSetting.get("unsocial_msg")));
							 * chatMessage.setType(ChatMessageType.END);
							 * chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
							 * chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
							 * chatService.insertChatMessage(chatMessage);
							 *
							 * // 채팅방 마지막 메세지 세팅 chatRoomService.setLastChatMessage(chatRoom, chatMessage);
							 * // 채팅방 반영 chatRoomService.endChatRoom(chatRoom, chatMessage,
							 * END_DIV_CD_CENTER_OFF_DUTY, customProperty.getSystemMemeberUid());
							 *
							 * log.
							 * info("ASSIGN SCHEDULER, {}, FAILED CENTER OFF DUTY, END CHATROOM, JOB: {}",
							 * now, chatRoomUid); }
							 */
							// ////////////////////////////////////////////////
							// 다음 스케줄
							else {
								log.info("ASSIGN SCHEDULER, {}, FAILED ASSIGN, MAYBE NEXT TIME, JOB: {}", now,
										chatRoomUid);
							}
						} else {
							// 스케줄 삭제
							log.info("ASSIGN SCHEDULER, {}, SKIP JOB: {}", now, chatRoomUid);
							settingService.deleteAssignScheduler(chatRoomUid);
						}
					} catch (Exception e1) {
						log.error("FAILED ASSIGN, {}, ERROR, SKIP JOB: {}, {}", now, chatRoomUid,
								e1.getLocalizedMessage(), e1);
					}
				}
			} else {
				log.info(">>> FAILED ASSIGN SCHEDULER, LOCK, {}", now);
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error(">>> FAILED ASSIGN SCHEDULER, {}, ERROR, {}", now, e.getLocalizedMessage(), e);
		}

		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_ASSIGN, "NOT_DAY");
		log.info(">>> END ASSIGN SCHEDULER, {}", now);
	}

	/**
	 * 자동 종료 스케줄러
	 */
	@Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 30)
	@Transactional
	public void runAutoEndChatRoom() {

		if (htUtils.isActiveProfile("local")) { return; }
		long now = System.currentTimeMillis();
		log.info(">>> RUN AUTO END SCHEDULER, {}", now);

		try {
			if (htUtils.isActiveProfile("live2")) { Thread.sleep(10000); }  // 중복실행방지용 2번was 지연처리
			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_AUTOSTOP, "NOT_DAY");

			if (check) {
				log.info(">>> START AUTO END  SCHEDULER, {}", now);
				runAutoEndChatRoom(CNSR_DIV_CD_C);
				runAutoEndChatRoom(CNSR_DIV_CD_R);		///봇 자동종료!
			} else {
				log.info(">>> FAILED AUTO END SCHEDULER, LOCK, {}", now);
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.info(">>> FAILED AUTO END SCHEDULER, {}, {}", now, e.getLocalizedMessage(), e);
		}

		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_AUTOSTOP, "NOT_DAY");
		log.info(">>> END AUTO END SCHEDULER, {}", now);
	}

	/**
	 * 일별 용어검색 저장 배치
	 */
	@Scheduled(cron = "0 20 1 * * *")
	@Transactional
	public void runStatisTermDay() {

		if (htUtils.isActiveProfile("local")) { return; }

		log.info(">>> RUN STATISTERMDAY SCHEDULER");
		String flagText = "성공";
		String logCont = "일별 용어검색 저장 배치";

		try {
			if (htUtils.isActiveProfile("live2")) { Thread.sleep(10000); }  // 중복실행방지용 2번was 지연처리
			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_STATISTERMDAY, "DAY");
			if (check) {
				log.info(">>> START BATCH_JOB_STATISTERMDAY SCHEDULER");

				Map<String, Object> param = new HashMap<String, Object>();
				String batchdate = commonService.selectDateToString(-1);

				param.put("regDate", batchdate);
				log.info(">>> batchdate : " + batchdate);


				Map<String, Object> sqlParams = new HashMap<>();
				sqlParams.put("termType", "D");
				sqlParams.put("jobDt", batchdate);
				sqlParams.put("type", "A");
				List<Map<String, Object>> zeroList = batchDao.selectTermRank(sqlParams);
				sqlParams.put("type", "B");
				List<Map<String, Object>> upperList = batchDao.selectTermRank(sqlParams);

				log.info(">>> zeroList : " + zeroList);

				for(Map<String, Object> zeroMap : zeroList) {
					log.info(">>> zeroMap : " + zeroMap);
					zeroMap.put("type",	"Z");
					zeroMap.put("jobDt",batchdate);
					batchDao.insertTermRankDay(zeroMap);
				}

				for(Map<String, Object> upperMap : upperList) {
					upperMap.put("type", "U");
					upperMap.put("jobDt",batchdate);
					batchDao.insertTermRankDay(upperMap);
				}

			} else {
				log.info(">>> FAILED STATISTERMDAY SCHEDULER, LOCK");
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.info(">>> FAILED STATISTERMDAY SCHEDULER, {}", e.getLocalizedMessage(), e);
			flagText = "실패";
			logCont += ", ERROR: " + e.getLocalizedMessage();
		}

		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_STATISTERMDAY, "DAY");

		// 로그 정보 등록
		commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont, customProperty.getSystemMemeberUid());

		log.info(">>> END STATISTERMDAY SCHEDULER");
	}

	/**
	 * 월별 용어검색 저장 배치
	 */
	@Scheduled(cron = "0 30 1 1 * *")
	@Transactional
	public void runStatisTermMon() {

		if (htUtils.isActiveProfile("local")) { return; }

		log.info(">>> RUN STATISTERMMON SCHEDULER");
		String flagText = "성공";
		String logCont = "월별 용어검색 저장 배치";

		try {
			if (htUtils.isActiveProfile("live2")) { Thread.sleep(10000); }  // 중복실행방지용 2번was 지연처리
			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_STATISTERMMON, "DAY");
			if (check) {
				log.info(">>> START BATCH_JOB_STATISTERMMON SCHEDULER");

				Map<String, Object> param = new HashMap<String, Object>();
				String batchdate = commonService.selectDateToString(-1);

				param.put("regDate", batchdate);
				log.info(">>> batchdate : " + batchdate.substring(0, 6));


				Map<String, Object> sqlParams = new HashMap<>();
				sqlParams.put("termType", "M");
				sqlParams.put("jobDt", batchdate.substring(0, 6));
				sqlParams.put("type", "A");
				List<Map<String, Object>> zeroList = batchDao.selectTermRank(sqlParams);
				sqlParams.put("type", "B");
				List<Map<String, Object>> upperList = batchDao.selectTermRank(sqlParams);

				log.info(">>> zeroList : " + zeroList);

				for(Map<String, Object> zeroMap : zeroList) {
					log.info(">>> zeroMap : " + zeroMap);
					zeroMap.put("type",	"Z");
					zeroMap.put("jobDt",batchdate.substring(0, 6));
					batchDao.insertTermRankMon(zeroMap);
				}

				for(Map<String, Object> upperMap : upperList) {
					upperMap.put("type", "U");
					upperMap.put("jobDt",batchdate.substring(0, 6));
					batchDao.insertTermRankMon(upperMap);
				}

			} else {
				log.info(">>> FAILED STATISTERMDAY SCHEDULER, LOCK");
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.info(">>> FAILED STATISTERMMON SCHEDULER, {}", e.getLocalizedMessage(), e);
			flagText = "실패";
			logCont += ", ERROR: " + e.getLocalizedMessage();
		}

		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_STATISTERMMON, "NOT_DAY");

		// 로그 정보 등록
		commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont, customProperty.getSystemMemeberUid());

		log.info(">>> END STATISTERMMON SCHEDULER");
	}

	public void runAutoEndChatRoom(@NotEmpty String cnsrDivCd) {

		try {
			/*
			 * IPCC_MCH ARS 채널 추가에 따른 로직 수정
			 */
			List<Map<String, Object>> cstmLinkDivCdList = commonService.selectCodeList(CommonConstants.COMM_CD_CSTM_LINK_DIV_CD);
			for (Map<String, Object> map : cstmLinkDivCdList) {
				runAutoEndChatRoomByChannel((String) map.get("cd"), cnsrDivCd);
			}
			// 하드코딩 로직 삭제
			//채널별 자동종료 스ㅁ케줄러
			//runAutoEndChatRoomByChannel(CSTM_LINK_DIV_CD_A, cnsrDivCd);
			//runAutoEndChatRoomByChannel(CSTM_LINK_DIV_CD_B, cnsrDivCd);
			//runAutoEndChatRoomByChannel(CSTM_LINK_DIV_CD_C, cnsrDivCd);
			//runAutoEndChatRoomByChannel(CSTM_LINK_DIV_CD_D, cnsrDivCd);
			//runAutoEndChatRoomByChannel(CSTM_LINK_DIV_CD_E, cnsrDivCd);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}
	}

	public void runAutoEndChatRoomByChannel (String channel, String cnsrDivCd) {

		try {

			log.info("channel: {}, cnsrDivCd : {}", channel, cnsrDivCd);
			Map<String, Object> siteSetting = settingService.selectSiteSetting();
			String delayGuideUseYnKey = "";
			String delayGuideTimeKey = "";
			String delayStopUseYnKey = "";
			String delayStopTimeKey = "";

			//채널별 키값 분류 A:웹, B:카카오, C:O2, D:mPOP
			/*
			 * switch (channel) { case CSTM_LINK_DIV_CD_A : delayGuideUseYnKey =
			 * "delay_guide_use_yn"; delayGuideTimeKey = "delay_guide_time";
			 * delayStopUseYnKey = "delay_stop_use_yn"; delayStopTimeKey =
			 * "delay_stop_time"; break; case CSTM_LINK_DIV_CD_B : delayGuideUseYnKey =
			 * "delay_guide_ko_use_yn"; delayGuideTimeKey = "delay_guide_ko_time";
			 * delayStopUseYnKey = "delay_stop_ko_use_yn"; delayStopTimeKey =
			 * "delay_stop_ko_time"; break; case CSTM_LINK_DIV_CD_C : delayGuideUseYnKey =
			 * "delay_guide_o2_use_yn"; delayGuideTimeKey = "delay_guide_o2_time";
			 * delayStopUseYnKey = "delay_stop_o2_use_yn"; delayStopTimeKey =
			 * "delay_stop_o2_time"; break; case CSTM_LINK_DIV_CD_D : delayGuideUseYnKey =
			 * "delay_guide_mpop_use_yn"; delayGuideTimeKey = "delay_guide_mpop_time";
			 * delayStopUseYnKey = "delay_stop_mpop_use_yn"; delayStopTimeKey =
			 * "delay_stop_mpop_time"; break; }
			 */
			delayGuideUseYnKey = channel +"_"+"delay_guide_use_yn";
			delayGuideTimeKey =  channel +"_"+"delay_guide_time";
			delayStopUseYnKey =  channel +"_"+"delay_stop_use_yn";
			delayStopTimeKey =  channel +"_"+"delay_stop_time";
			// 고객 답변 지연 안내
			String delayGuideUseYn = (String) siteSetting.get(delayGuideUseYnKey);
			int delayGuideTime = 0;

			if (CNSR_DIV_CD_C.equals(cnsrDivCd) && "Y".equals(delayGuideUseYn)) {	///// 상담원

				log.info("AUTO DELAY START");
				if ((BigDecimal) siteSetting.get(delayGuideTimeKey) == null) {
					throw new UnsupportedOperationException("NO DELAY_GUIDE_TIME");
				}
				delayGuideTime = ((BigDecimal) siteSetting.get(delayGuideTimeKey)).intValue();

				DateTime dateTime = new DateTime(commonService.selectSysdate());
				dateTime = dateTime.minusMinutes(delayGuideTime);

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("cnsrDivCd", cnsrDivCd);
				params.put("senderDivCdNot", SENDER_DIV_CD_U);
				//params.put("lastChatDtLt", new Timestamp(dateTime.getMillis()));
				params.put("todayYn", "Y");
				params.put("endYn", "N");
				params.put("cnsrLinkDtNotNull", "cnsrLinkDtNotNull");
				params.put("cstmReplyDelayYn", "N");
				params.put("cstmLinkDivCd", channel);
				params.put("lastChatDtLt", delayGuideTime);

				log.info("========================== "+channel+" 방목록 start =======================");
				log.info("*************************************** delay time : " + dateTime);
				List<Map<String, Object>> listMap = chatDao.selectChatRoomList(params);
				log.info("AUTO DELAY chat room SIZE: {}", listMap.size());
				log.info("=========================="+channel+" 방목록 end =======================");
				int cnt = listMap.size();
				for (Map<String, Object> map : listMap) {

					//채팅방과 인입채널이 같을 경우 로직 수행
					if (channel.equals(String.valueOf(map.get("cstm_link_div_cd")))) {

						log.info(channel + " CHANNEL AUTO END GUIDE SCHEDULER CHECK");

						// 채팅방 고객 답변 지연 상태 저장
						ChatRoom chatRoom = new ChatRoom().fromMap(map);
						log.info("AUTO DELAY, CSTM: {}, CHANNEL: {}, {} < {}", chatRoom.getCstmName(),
								chatRoom.getCstmLinkDivCd(), new DateTime(chatRoom.getLastChatDt()), dateTime);

						chatRoom.setCstmReplyDelayYn("Y");
						int d = chatDao.saveChatRoom(chatRoom.toMap());
						
						//assert (d == 1);
						if (d == 1) {
							// 채팅방 정보 발행
							messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new NoticeMessage(chatRoom));

							// 메세지 전달
							ChatMessage chatMessage = new ChatMessage();
							chatMessage.setChatRoomUid(chatRoom.getChatRoomUid());
							chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
							chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
							chatMessage.setCnsrMemberUid(chatRoom.getMemberUid());
							// chatMessage.setSenderDivCd(chatRoom.getCnsrDivCd());
							// chatMessage.setSenderUid(chatRoom.getMemberUid());
							chatMessage.setType(ChatMessageType.MESSAGE);
							chatMessage.setMsgStatusCd(MSG_STATUS_CD_SEND);
							chatMessage.setContDivCd(CONT_DIV_CD_T);
							chatMessage.setCont(ChatMessage
									.buildChatMessageText(Strings.nullToEmpty((String) siteSetting.get(channel+"_"+"delay_guide_msg"))));
							chatService.insertChatMessage(chatMessage);
							chatService.sendMessage(chatRoom, chatRoom.getChatRoomUid(), chatMessage);
							
							// push msessage 전송
							if( !chatRoom.getCstmCocId().isEmpty()) {				/// entity_id 필수
								if(channel.equals(CSTM_LINK_DIV_CD_C) || channel.equals(CSTM_LINK_DIV_CD_D)) { 	// O2, mPOP 전용
									Map<String, Object> eaiParam = new HashMap<String, Object>();
									eaiParam.put("cstmLinkDivCd", channel);
									eaiParam.put("entityId", chatRoom.getCstmCocId());
									eaiParam.put("pushType", "고객 문의 지연 안내");
									eaiParam.put("pushMsg", "고객님 상담이 곧 종료될 예정입니다.");
									eaiUmsService.throwEai(eaiParam);
								}
							}
						}
					}
					cnt--;
				}

			}

			log.debug("AUTO STOP START");

			// 고객 답변 지연 종료
			String delayStopUseYn = (String) siteSetting.get(delayStopUseYnKey);

			// 고객 답변 지연시 자동 종료
			if ("Y".equals(delayStopUseYn)) {
				//int delayStopTime = ((BigDecimal) siteSetting.get("bot_delay_stop_time")).intValue();
				int delayStopTime = ((BigDecimal) siteSetting.get(delayStopTimeKey)).intValue();

				/*
				 * if (CNSR_DIV_CD_C.equals(cnsrDivCd)) { delayStopTime = ((BigDecimal)
				 * siteSetting.get(delayStopTimeKey)).intValue(); }
				 */
				//자동종료 Y 일 경우만 체크
				if (!siteSetting.get(delayStopUseYnKey).equals("Y")) {
					throw new UnsupportedOperationException("NO DELAY_STOP_TIME AS CHANNEL IS " + channel);
				}

				DateTime dateTime = new DateTime(commonService.selectSysdate());
				dateTime = dateTime.minusMinutes(delayStopTime);

				Map<String, Object> params = new HashMap<String, Object>();
				// params.put("chatRoomStatusCdList", Arrays.asList(CHAT_ROOM_STATUS_CD_BOT,
				// CHAT_ROOM_STATUS_CD_WAIT_REPLY));
				params.clear();
				params.put("cnsrDivCd", cnsrDivCd);
				params.put("senderDivCdNot", SENDER_DIV_CD_U);
				params.put("todayYn", "Y");
				params.put("endYn", "N");
				params.put("cstmLinkDivCd", channel);
				if(CNSR_DIV_CD_C.equals(cnsrDivCd)) {
					params.put("cstmReplyDelayYn", "Y");
				}
				else params.put("cstmReplyDelayYn", "N");
				params.put("lastChatDtLt", delayStopTime);
				if (CNSR_DIV_CD_C.equals(cnsrDivCd)) {
					params.put("cnsrLinkDtNotNull", "cnsrLinkDtNotNull");
				}
				//params.put("lastChatDtLt", new Timestamp(dateTime.getMillis()));
				List<Map<String, Object>> listMap = chatDao.selectChatRoomList(params);

				for (Map<String, Object> map : listMap) {

					log.debug(channel + " CHANNEL AUTO END SCHEDULER CHECK");

					//채팅방과 인입채널이 같을 경우 로직 수행
					if (channel.equals(String.valueOf(map.get("cstm_link_div_cd")))) {

						// 채팅방 종료
						ChatRoom chatRoom = new ChatRoom().fromMap(map);
						log.debug("AUTO STOP, CSTM: {}, CHANNEL: {}, {} < {}", chatRoom.getCstmName(),
								chatRoom.getCstmLinkDivCd(), new DateTime(chatRoom.getLastChatDt()), dateTime);

						if (!customProperty.getIBotMemeberUid().equals(chatRoom.getMemberUid())) { // 오픈빌더는 제외
							// 메세지 전달
							ChatMessage chatMessage = new ChatMessage();
							chatMessage.setChatRoomUid(chatRoom.getChatRoomUid());
							chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
							chatMessage.setSenderUid(customProperty.getSystemMemeberUid());
							// chatMessage.setSenderDivCd(chatRoom.getCnsrDivCd());
							// chatMessage.setSenderUid(chatRoom.getMemberUid());
							chatMessage.setCnsrMemberUid(chatRoom.getMemberUid());
							chatMessage.setType(ChatMessageType.END);
							chatMessage.setMsgStatusCd(MSG_STATUS_CD_READ);
							chatMessage.setContDivCd(CONT_DIV_CD_T);
							String textMessage = (String) siteSetting.get(channel+"_"+"delay_stop_msg");
							if (CNSR_DIV_CD_R.equals(cnsrDivCd)) {
								/////카톡 전용 메세지 분기
								if ("B".equals(channel) ) {
									textMessage = KAKABOT_END_CHATROOM;
								}
								else textMessage = CHATBOT_END_CHATROOM;
							}

							//채널별 메시지 빌드 분기
							if (CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
								//카카오
								chatMessage.setCont(
										ChatMessage.buildChatMessageSectionText(Strings.nullToEmpty(textMessage)));
							} else {
								//그외
								chatMessage.setCont(
										ChatMessage.buildChatMessageHotKeyNewChatRoom(Strings.nullToEmpty(textMessage)));
							}

//							if (CSTM_LINK_DIV_CD_A.equals(chatRoom.getCstmLinkDivCd())) {
//								chatMessage.setCont(
//										ChatMessage.buildChatMessageHotKeyNewChatRoom(Strings.nullToEmpty(textMessage)));
//							} else {
//								chatMessage
//										.setCont(ChatMessage.buildChatMessageSectionText(Strings.nullToEmpty(textMessage)));
//							}
							chatService.insertChatMessage(chatMessage);
							chatService.sendMessage(chatRoom, chatRoom.getChatRoomUid(), chatMessage);
						}

						// 채팅방 종료
						chatRoom = chatRoomService.endChatRoom(chatRoom,
								CNSR_DIV_CD_C.equals(cnsrDivCd) ? END_DIV_CD_CNSR_TIME : END_DIV_CD_BOT_TIME,
								customProperty.getSystemMemeberUid());

						// 채팅방 정보 발행
						messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new NoticeMessage(chatRoom));
						
						// 20210303 LKJ 챗봇 시나리오방 자동 종료시 PUSH 메시지 전송하지 않도록 주석 처리
						// push msessage 전송
//						if( !chatRoom.getCstmCocId().isEmpty()) {				/// entity_id 필수
//							if(channel.equals(CSTM_LINK_DIV_CD_C) || channel.equals(CSTM_LINK_DIV_CD_D)) { 	// O2, mPOP 전용
//								Map<String, Object> eaiParam = new HashMap<String, Object>();
//								eaiParam.put("cstmLinkDivCd", channel);
//								eaiParam.put("entityId", chatRoom.getCstmCocId());
//								eaiParam.put("pushType", "고객 문의 지연 종료");
//								eaiParam.put("pushMsg", "상담이 종료되었습니다.");
//								eaiUmsService.throwEai(eaiParam);
//							}
//						}					
						
					}
				}
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 통계 배치 일별 통계 저장
	 */
	@Scheduled(cron = "0 0 1 * * *")
	@Transactional
	public void runSaveStatistics() {

		log.info(">>> RUN STATISTICS SCHEDULER");
		String flagText = "성공";
		String logCont = "일별 통계 저장 배치";

		try {
			if (htUtils.isActiveProfile("live2")) { Thread.sleep(10000); }  // 중복실행방지용 2번was 지연처리
			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_STATISTICS, "DAY");

			if (check) {
				log.info(">>> START STATISTICS SCHEDULER");
				log.info("htUtils.isActiveProfile : " + htUtils.isActiveProfile("dev"));
				Map<String, Object> param = new HashMap<String, Object>();
				String batchdate = commonService.selectDateToString(-1);
				// String[] departArr = {"TM", "CS"};
				// String[] ctgnumArr = {"9999999998", "9999999999"};
				//채널별? 부서별 통계 정책 결정 필요.
				//String[] departArr = { "CS" };
				//String[] ctgnumArr = { "9999999999" };

				/*
				 * IPCC_MCH ARS 채널 추가에 따른 로직 수정
				 */
				// 하드코딩 로직 삭제
				//String[] departArr = { "F1","F2","DC","FM","DM", "CT" };
                //
				//param.put("regDate", batchdate);
                //
				//for (int i = 0; i < departArr.length; i++) {
				//	param.put("departCd", departArr[i]);
				//	//param.put("ctgNum", ctgnumArr[i]);
				//	reportService.insertStatsDate(param); // Stats_date 저장
				//	reportService.insertStatsTime(param); // stats_time 저장
				//}

				param.put("regDate", batchdate);
				
				List<Map<String, Object>> departCdList = commonService.selectCodeList(CommonConstants.COMM_CD_DEPART_CD);
				for (Map<String, Object> map : departCdList) {
					param.put("departCd", (String) map.get("cd"));
					//log.info(">>> IN STATISTICS SCHEDULER -- departCd : " + map.get("cd").toString());
					reportService.insertStatsDate(param); // Stats_date 저장
					reportService.insertStatsTime(param); // stats_time 저장
				}

				reportService.insertStatsCnsr(param); // stats_cnsr 저장
				reportService.insertStatsCnsrTime(param); // stats_cnsr_time 저장
				reportService.insertStatsCtgDate(param); // stats_ctg_date저장
				reportService.insertSQILIst(param); // SQI저장
				/* 상담원 전용 */
			} else {
				log.info(">>> FAILED STATISTICS SCHEDULER, LOCK");
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.info(">>> FAILED STATISTICS SCHEDULER, {}", e.getLocalizedMessage(), e);
			flagText = "실패";
			logCont += ", ERROR: " + e.getLocalizedMessage();
		}

		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_STATISTICS, "DAY");

		// 로그 정보 등록
		commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont, customProperty.getSystemMemeberUid());

		log.info(">>> END STATISTICS SCHEDULER");
	}

	/**
	 * 통계 배치 챗봇 이력통계 저장
	 */
	@Scheduled(cron = "0 10 1 * * *")
	@Transactional
	public void runSaveStatChat() {

		log.info(">>> RUN ROBOT STATISTICS SCHEDULER");

		String flagText = "성공";
		String logCont = "챗봇 이력통계 저장 배치";

		try {
			if (htUtils.isActiveProfile("live2")) { Thread.sleep(10000); }  // 중복실행방지용 2번was 지연처리
			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_STATISCHATBOT, "DAY");

			if (check) {
				log.info(">>> START ROBOT STATISTICS SCHEDULER");
				//통계 정책 결정 필요. 삼성증권은 채널별로 기준함
				/*
				 * IPCC_MCH ARS 채널 추가에 따른 로직 수정
				 */
				//String[] linkdivCd = { CSTM_LINK_DIV_CD_A, CSTM_LINK_DIV_CD_B, CSTM_LINK_DIV_CD_C, CSTM_LINK_DIV_CD_D, CSTM_LINK_DIV_CD_E };
				String batchdate = commonService.selectDateToString(-1);

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("botMemberUidList", Arrays.asList(customProperty.getHappyBotMemeberUid(), customProperty.getCategoryBotMemeberUid()));
				param.put("regDate", batchdate);

				// 하드코딩 로직 삭제
				//for (int i = 0; i < linkdivCd.length; i++) {
				//	param.put("cstmLinkCd", linkdivCd[i]);
				//	reportService.insertStatsLinkRobot(param); // Stats_date 저장
				//}

				List<Map<String, Object>> cstmLinkDivCdList = commonService.selectCodeList(CommonConstants.COMM_CD_CSTM_LINK_DIV_CD);
				for (Map<String, Object> map : cstmLinkDivCdList) {
					param.put("cstmLinkCd", (String) map.get("cd"));
					reportService.insertStatsLinkRobot(param); // Stats_date 저장
				}
				reportService.insertStatsRobot(param); // Stats_date 저장
				// reportService.insertStatsIntent(param); // stats_cnsr 저장

			} else {
				log.info(">>> FAILED ROBOT STATISTICS SCHEDULER, LOCK");
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error(">>> FAILED ROBOT STATISTICS SCHEDULER, {}", e.getLocalizedMessage(), e);
			flagText = "실패";
			logCont += ", ERROR: " + e.getLocalizedMessage();
		}

		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_STATISCHATBOT, "DAY");

		// 로그 정보 등록
		commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont, customProperty.getSystemMemeberUid());

		log.info(">>> END ROBOT STATISTICS SCHEDULER");
	}

	/**
	 * 채팅방 정리
	 *
	 * 1. 미접수, 미배정, 상담원과 대화중인 채팅방 종료 2. 배정 스케줄러 삭제
	 */
	@Scheduled(cron = "0 30 0 * * *")
	@Transactional
	public void runCleanChatRoom() {

		log.info(">>> RUN CLEAN CHATROOM SCHEDULER");

		String flagText = "성공";
		String logCont = "채팅방 정리 배치";

		try {
			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_CLEANROOM, "DAY");

			if (check) {
				log.info(">>> START CLEAN CHATROOM SCHEDULER");
				/*
				 * IPCC_MCH ARS 채널 추가에 따른 로직 수정
				 */
				List<Map<String, Object>> cstmLinkDivCdList = commonService.selectCodeList(CommonConstants.COMM_CD_CSTM_LINK_DIV_CD);
				for (Map<String, Object> map : cstmLinkDivCdList) {
					chatRoomService.endChatRoomByScheduler((String) map.get("cd"));
				}
				// 하드코딩 로직 삭제
				//chatRoomService.endChatRoomByScheduler("A");
				//chatRoomService.endChatRoomByScheduler("B");		/// 30일 이후 자동 종료
				//chatRoomService.endChatRoomByScheduler("C");
				//chatRoomService.endChatRoomByScheduler("D");
				settingService.deleteAssignScheduler();

			} else {
				log.info(">>> FAILED CLEAN CHATROOM SCHEDULER, LOCK");
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error(">>> FAILED CLEAN CHATROOM SCHEDULER, {}", e.getLocalizedMessage(), e);
			flagText = "실패";
			logCont += ", ERROR: " + e.getLocalizedMessage();
		}
		
		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_CLEANROOM, "DAY");

		// 로그 정보 등록
		commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont, customProperty.getSystemMemeberUid());
		
		// 카카오 인증정보 30일 초과 삭제
		memberService.deleteCustomerKakao();

		log.info(">>> END CLEAN CHATROOM SCHEDULER");
	}

	/**
	 * 로그인 여부 30일이상, 패스워드 변경 90일이상 비활성화
	 */
	// @Scheduled(cron = "0 10 0 * * *")
	@Transactional
	public void runLoginMember() {

		boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_LOGIN30CHECK, "DAY");

		if (check) {
			log.info(">>> RUN LOGIN PERIOD SCHEDULER");
			try {
				String flagText = "성공";
				String logCont = "로그인 여부 30일이상 체크 배치";
				// String logCont = "로그인 여부 30일이상, 패스워드 변경 90일이상 체크 배치";
				try {
					memberService.loginMemberCheck();
				} catch (Exception e) {
					HTUtils.batmanNeverDie(e);
					log.error("{}", e.getLocalizedMessage(), e);
					flagText = "실패";
				}

				// 로그 정보 등록
				commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont,
						customProperty.getSystemMemeberUid());

				log.info(">>> END LOGIN PERIOD SCHEDULER");

			} catch (Exception e2) {
				log.error("{}", e2.getLocalizedMessage(), e2);
			}
			commonService.checkBatchEnd(CommonConstants.BATCH_JOB_LOGIN30CHECK, "DAY");
		} else {
			log.info(">>> FAILED LOGIN PERIOD SCHEDULER, LOCK");
		}
	}

	/**
	 * 근무 시작 시간에 전체 상담원 근무 가능으로 변경 매시 10분, 40분에 돌다
	 */
	// @Scheduled(cron = "0 10,40 * * * *")
	@Transactional
	public void runCounselorBreakTime() {

		if (htUtils.isActiveProfile("local")) {
			return;
		}

		boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_BREAKTIME, "NOT_DAY");

		if (check) {
			log.info(">>> RUN COUNSELOR START BREAKTIME SCHEDULER");
			try {
				String flagText = "성공";
				String logCont = "상담직원 근무 시작 변경 배치";
				boolean logInsert = settingService.runCounselorBreakTime();

				// 로그 정보 등록
				if (logInsert) {
					commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont,
							customProperty.getSystemMemeberUid());
				}

				log.info(">>> END COUNSELOR START BREAKTIME SCHEDULER");

			} catch (Exception e) {
				HTUtils.batmanNeverDie(e);
				log.error("{}", e.getLocalizedMessage(), e);
			}

			commonService.checkBatchEnd(CommonConstants.BATCH_JOB_BREAKTIME, "NOT_DAY");
		} else {
			log.info(">>> FAILED COUNSELOR START BREAKTIME SCHEDULER, LOCK");
		}
	}

	/**
	 * IPCC_ADV 고객여정 I/F 실패 건 재송신
	 */
	@Scheduled(cron = "0 0/5 * * * *")
	@Transactional
	public void runResendGenesys() {
		//if (htUtils.isActiveProfile("local")) {
		//	return;
		//}

		long now = System.currentTimeMillis();
		log.info(">>> RUN RESEND GENESYS, {}", now);

		try {
			// 중복실행방지용 2번was 지연처리
			if (htUtils.isActiveProfile("live2")) {
				Thread.sleep(10000);
			}

			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_RESEND_GENESYS, "NOT_DAY");
			log.info(">>> START RESEND GENESYS SCHEDULER CHECK, {}", check);

			if (check) {
				log.info(">>> START RESEND GENESYS SCHEDULER, {}", now);
				// 재전송
				genesysService.resendFailedList();
			} else {
				log.info(">>> FAILED RESEND GENESYS, LOCK, {}", now);
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error(">>> FAILED RESEND GENESYS, {}, ERROR, {}", now, e.getLocalizedMessage(), e);
		}

		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_RESEND_GENESYS, "NOT_DAY");
		log.info(">>> END RESEND GENESYS, {}", now);
	}
	
	/**
	 * IPCC_IST 통합통계 파일 생성 및 FTP 서버 전송
	 */
	@Scheduled(cron = "0 0 3 * * *")
	@Transactional
	public void runIstFileSend() {
		//if (htUtils.isActiveProfile("local")) {
		//	return;
		//}

		long now = System.currentTimeMillis();
		log.info(">>> RUN IST FILE SEND, {}", now);
		
		try {
			// 중복실행방지용 2번was 지연처리
			if (htUtils.isActiveProfile("live2")) {
				Thread.sleep(10000);
			}

			boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_FILE_SEND_IST, "DAY");
			log.info(">>> START IST FILE SEND SCHEDULER CHECK, {}", check);

			if (check) {
				log.info(">>> START IST FILE SEND SCHEDULER, {}", now);
				Calendar cal = Calendar.getInstance(Locale.KOREAN);
				cal.add(Calendar.DATE, -1);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String strDate = sdf.format(cal.getTime());


				List<String> istList = genesysService.selectIstList(strDate);
				StringBuilder sb = new StringBuilder();
				
				for (int i = 0; i < istList.size(); i++) {
					sb.append(istList.get(i));
					if (i < istList.size() - 1) {
						sb.append("\n");
					}
				}
				String hostName = customProperty.getIstFtp().getHost();
				Integer port = customProperty.getIstFtp().getPort();
				String user = customProperty.getIstFtp().getUser();
				String passwd = customProperty.getIstFtp().getPswd();
				String remotePath = customProperty.getIstFtp().getPath();
				String fileName = strDate + "_chat.data";
				String contents = sb.toString();

				htUtils.sftpFlieUpload(hostName, port, user, passwd, remotePath, fileName, contents);
			} else {
				log.info(">>> FAILED IST FILE SEND, LOCK, {}", now);
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error(">>> FAILED IST FILE SEND, {}, ERROR, {}", now, e.getLocalizedMessage(), e);
		}
		
		commonService.checkBatchEnd(CommonConstants.BATCH_JOB_FILE_SEND_IST, "NOT_DAY");
		log.info(">>> END IST FILE SEND, {}", now);
	}

	@Transactional
	public void testrunIstFileSend() {
		//if (htUtils.isActiveProfile("local")) {
		//	return;
		//}

		long now = System.currentTimeMillis();
		log.info(">>> RUN TEST IST FILE SEND, {}", now);
		
		try {
			// 중복실행방지용 2번was 지연처리
			if (htUtils.isActiveProfile("live2")) {
				Thread.sleep(10000);
			}

			boolean check = true;
			log.info(">>> START TEST IST FILE SEND SCHEDULER CHECK, {}", check);

			if (check) {
				log.info(">>> START TEST IST FILE SEND SCHEDULER, {}", now);
				Calendar cal = Calendar.getInstance(Locale.KOREAN);
				cal.add(Calendar.DATE, -1);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String strDate = sdf.format(cal.getTime());


				List<String> istList = genesysService.selectIstList(strDate);
				StringBuilder sb = new StringBuilder();
				
				for (int i = 0; i < istList.size(); i++) {
					sb.append(istList.get(i));
					if (i < istList.size() - 1) {
						sb.append("\n");
					}
				}
				String hostName = customProperty.getIstFtp().getHost();
				Integer port = customProperty.getIstFtp().getPort();
				String user = customProperty.getIstFtp().getUser();
				String passwd = customProperty.getIstFtp().getPswd();
				String remotePath = customProperty.getIstFtp().getPath();
				String fileName = strDate + "_chat.data";
				String contents = sb.toString();

				htUtils.sftpFlieUpload(hostName, port, user, passwd, remotePath, fileName, contents);
			} else {
				log.info(">>> FAILED TEST IST FILE SEND, LOCK, {}", now);
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error(">>> FAILED TEST IST FILE SEND, {}, ERROR, {}", now, e.getLocalizedMessage(), e);
		}
		
		log.info(">>> END IST FILE SEND, {}", now);
	}
	
	/**
	 * 회원 정보 수정
	 *
	 * <li>일 배치로 회원 정보 수정</li>
	 * <li>존재하는 회원만 제공</li>
	 * <li>존재하지 않는 회원은 탈퇴 처리</li>
	 */
	// @Scheduled(cron = "0 0 5 * * *")
	@Transactional
	public void runMemberReset() {

		log.info(">>> RUN MEMBER UPDATE SCHEDULER");

		String flagText = "";
		String logCont = "";

		boolean check = false;

		boolean checkMember = false;
		File memberFile = null;
		try {
			memberFile = new File(customProperty.getMemberFile());
			checkMember = memberFile.exists();
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		boolean checkMemberFp = false;
		File memberFileFp = null;
		try {
			memberFileFp = new File(customProperty.getMemberFileFp());
			checkMemberFp = memberFileFp.exists();
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		boolean checkMemberCnsr = false;
		File memberFileCnsr = null;
		try {
			memberFileCnsr = new File(customProperty.getMemberFileCnsr());
			checkMemberCnsr = memberFileCnsr.exists();
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		if (checkMember && checkMemberFp && checkMemberCnsr) {
			check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_MEMBER, "DAY");
			if (!check) {
				flagText = "실패";
				logCont = "회원 정보 일배치 : 완료된 배치.";
			}
		} else {
			flagText = "실패";
			logCont = "회원 정보 일배치 : 파일 미존재";
		}

		if (check) {
			try {
				try {
					Map<String, Object> mapCnsr = memberService.insertTempMemberCnsr(memberFileCnsr);
					Map<String, Object> map = memberService.insertTempMember(memberFile, memberFileFp);

					int insertCnt = 0;
					int errorCnt = 0;
					int delFp = 0;
					if (mapCnsr != null && mapCnsr.get("insertCnt") != null) {
						insertCnt += (int) mapCnsr.get("insertCnt");
					}
					if (map != null && map.get("insertCnt") != null) {
						insertCnt += (int) map.get("insertCnt");
					}

					if (mapCnsr != null && mapCnsr.get("errorCnt") != null) {
						errorCnt += (int) mapCnsr.get("errorCnt");
					}
					if (map != null && map.get("errorCnt") != null) {
						errorCnt += (int) map.get("errorCnt");
					}

					if (map != null && map.get("delFp") != null) {
						delFp += (int) map.get("delFp");
					}

					flagText = "성공";
					logCont = "회원 정보 일배치 성공 : " + insertCnt + ", 실패 : " + errorCnt + ", 삭제FP : " + delFp;

					// 회원 정보 update
					memberService.updateMember(customProperty.getSystemMemeberUid());
				} catch (BizException be) {
					flagText = "실패";
					logCont = be.getMessage();
				} catch (Exception e) {
					flagText = "실패";
					logCont = "회원 정보 일배치 : 처리중 오류";
				}

				// 로그 정보 등록
				commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont,
						customProperty.getSystemMemeberUid());

				log.info(">>> END MEMBER UPDATE SCHEDULER");

			} catch (Exception e) {
				HTUtils.batmanNeverDie(e);
				log.error("{}", e.getLocalizedMessage(), e);
				logCont += ", ERROR: " + e.getLocalizedMessage();
				// 로그 정보 등록
				commonService.insertLog(CommonConstants.LOG_DIV_CD_E, "실패", logCont,
						customProperty.getSystemMemeberUid());
			}

			commonService.checkBatchEnd(CommonConstants.BATCH_JOB_MEMBER, "DAY");
		} else {
			log.info(">>> FAILED MEMBER UPDATE SCHEDULER, LOCK");
			// 로그 정보 등록
			commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont,
					customProperty.getSystemMemeberUid());
		}
	}

	/**
	 * 로그 파일 삭제
	 */
	// @Scheduled(cron = "0 10 0 * * *")
	@Transactional
	public void runDeleteLogs() {

		boolean check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_DELETE_LOG, "DAY");

		if (check) {
			try {
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime sevenDaysAgo = now.minusDays(7L);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String sevenDaysAgoDate = sevenDaysAgo.format(dateTimeFormatter);

				log.info(">>> RUN DELETE LOG SCHEDULER, MATCH juli.{}.log", sevenDaysAgoDate);

				// TODO: 경로, 설정으로
				File path = new File("/LOG/wasadmin/tomcat8");
				if (path.exists() && path.isDirectory() && path.canWrite()) {
					File[] files = path.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return name.matches("juli." + sevenDaysAgoDate + ".log");
						}
					});

					for (File file : files) {
						if (!file.delete()) {
							log.error("CANNOT DELETE FILE: {}", file.getAbsolutePath());
						}
					}
				}

				log.info(">>> END DELETE LOG SCHEDULER", sevenDaysAgoDate);

			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage(), e);
			}

			commonService.checkBatchEnd(CommonConstants.BATCH_JOB_DELETE_LOG, "DAY");

		} else {
			log.info(">>> FAILED DELETE LOG SCHEDULER, LOCK");
		}
	}

	/**
	 * 민감정보 암호화 마이그레이션
	 */
	@Transactional
	public void runEncryptSInfo(String dateTime) {

		try {
			log.info(">>> RUN ENCRYPT SINFO");

			Map<String, Object> sqlParams = new HashMap<>();
			sqlParams.put("createDt", dateTime);
			List<Map<String, Object>> list = batchDao.selectCstmSinfo(sqlParams);
			for (Map<String, Object> item : list) {
				item.put("chatRoomUid", item.get("chat_room_uid"));
				if (item.get("tel_no1") != null) {
					item.put("telNo1", cipherUtils.encrypt((String) item.get("tel_no1")));
				}
				if (item.get("tel_no2") != null) {
					item.put("telNo2", cipherUtils.encrypt((String) item.get("tel_no2")));
				}
				if (item.get("tel_no3") != null) {
					item.put("telNo3", cipherUtils.encrypt((String) item.get("tel_no3")));
				}
				if (item.get("account_no") != null) {
					item.put("accountNo", cipherUtils.encrypt((String) item.get("account_no")));
				}
				if (item.get("etc") != null) {
					item.put("etc", cipherUtils.encrypt((String) item.get("etc")));
				}
				if (item.get("card_no1") != null) {
					item.put("cardNo1", cipherUtils.encrypt((String) item.get("card_no1")));
				}
				if (item.get("card_no2") != null) {
					item.put("cardNo2", cipherUtils.encrypt((String) item.get("card_no2")));
				}
				if (item.get("card_no3") != null) {
					item.put("cardNo3", cipherUtils.encrypt((String) item.get("card_no3")));
				}
				if (item.get("card_no4") != null) {
					item.put("cardNo4", cipherUtils.encrypt((String) item.get("card_no4")));
				}
				batchDao.updateCstmSinfo(item);
			}
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 후처리 메모 암호화 마이그레이션
	 */
	@Transactional
	public void runEncryptEndMemo(String dateTime) {

		log.info(">>> RUN ENCRYPT END MEMO");

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("createDt", dateTime);
		List<Map<String, Object>> list = batchDao.selectEndMemo(sqlParams);
		for (Map<String, Object> item : list) {
			log.debug("item: {}", item);
			item.put("chatRoomUid", item.get("chat_room_uid"));
			if (item.get("memo") != null) {
				try {
					// if (cipherUtils.isEncrypted((String) item.get("memo"))) {
					// log.info("ALREADY ENCRYPTED: {}", item.get("memo"));
					// log.info("DECRYPTED MEMO: {}", cipherUtils.decrypt((String)
					// item.get("memo")));
					// } else {
					item.put("memo", cipherUtils.encrypt((String) item.get("memo")));
					batchDao.updateEndMemo(item);
					// }
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
					continue;
				}
			}
		}
	}

	/**
	 * 배치잡 상태
	 */
	public List<Map<String, Object>> selectBatchJob() {

		Map<String, Object> sqlParams = new HashMap<>();
		return batchDao.selectBatchJob(sqlParams);
	}

	/**
	 * 배치잡 활성/비활성
	 */
	@Transactional
	public int updateBatchJob(@NotEmpty @Pattern(regexp = "[Y|N]") String startYn) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("startYn", startYn);
		return batchDao.updateBatchJob(sqlParams);
	}
	
}
