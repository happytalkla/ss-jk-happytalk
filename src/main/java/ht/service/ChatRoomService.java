package ht.service;

import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_ASSIGN;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_BOT;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_END;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_MANAGER_COUNSEL;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_NEED_ANSWER;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_WAIT_CNSR;
import static ht.constants.CommonConstants.CHAT_ROOM_STATUS_CD_WAIT_REPLY;
import static ht.constants.CommonConstants.CNSR_DIV_CD_C;
import static ht.constants.CommonConstants.CNSR_DIV_CD_R;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_B;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_C;
import static ht.constants.CommonConstants.END_DIV_CD_BOT_BAT;
import static ht.constants.CommonConstants.END_DIV_CD_CENTER_OFF_DUTY;
import static ht.constants.CommonConstants.END_DIV_CD_CNSR_BAT;
import static ht.constants.CommonConstants.END_DIV_CD_CNSR_CNSR;
import static ht.constants.CommonConstants.END_DIV_CD_CNSR_CSTM;
import static ht.constants.CommonConstants.END_DIV_CD_COUNSELOR_POOL_OVER;
import static ht.constants.CommonConstants.LOG_CODE_MANAGER_COUNSEL;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_A;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_C;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_M;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_S;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_U;
import static ht.constants.CommonConstants.SENDER_DIV_CD_U;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.ChatContents;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatCommand;
import ht.domain.ChatRoom;
import ht.domain.MemberVO;
import ht.domain.NoticeMessage;
import ht.persistence.ChatDao;
import ht.service.channel.ChannelService;
import ht.service.chatbot.ChatBotService;
import ht.service.chatbot.HappyBotService;
import ht.service.legacy.LegacyService;
import ht.service.MemberService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatRoomService {

	@Resource
	private CustomProperty customProperty;

	@Resource
	private SimpMessageSendingOperations messagingTemplate;
	@Resource
	private ChatDao chatDao;
	@Resource
	@Qualifier("HappyBotServiceImpl")
	private ChatBotService chatBotService;
	@Resource
	private CustomerService customerService;
	@Resource
	private SettingService settingService;
	@Resource
	private CommonService commonService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private ChannelService channelService;
	@Resource
	private ChatService chatService;
	@Resource
	private AssignService assignService;
	@Resource
	private AuthService authService;
//	@Resource
//	private LegacyService legacyService;
	@Resource
	private HappyBotService happyBotService;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private McaService mcaService;
	@Resource
	private MemberService memberService;
	/**
	 * 웹채팅 고객 분류별 채팅방 선택, 해당 고객의 진행중인 마지막 채팅방
	 */
	public ChatRoom selectChatRoomByCtgNum(Map<String, Object> customer, String ctgNum, Map<String, Object> siteSetting) {

		log.info("SELECT CHAT ROOM BY CATEGORY: {}", ctgNum);

		// 최근에 생성된 채팅방 선택
		// 분류 조건 추가 (사용자-분류별로 진행중인 채팅방은 한 개)
		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", customer.get("cstm_uid"));
		params.put("endYn", "N");
		params.put("todayYn", "Y");
		params.put("cstmDelYn", "N");
		params.put("ctgNum", ctgNum);
		params.put("page", 1);
		params.put("size", 1);
		params.put("sorter", "room_create_dt desc");

		Map<String, Object> chatRoom = chatDao.selectChatRoom(params);

		// 채팅방 정보 발행
		//		 messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new
		//		NoticeMessage(chatRoom));

		return chatRoom != null
				? new ChatRoom().fromMap(chatRoom)
						: null;
	}

	/**
	 * 채널 고객 채팅방 선택, 해당 고객의 진행중인 마지막 채팅방
	 */
	public ChatRoom selectChatRoomByChannel(Map<String, Object> customer) {

		log.info("SELECT CHAT ROOM BY CHANNEL: {}", customer.get("cstm_link_div_cd"));

		// 최근에 생성된 채팅방 선택
		// 분류 조건 추가 (사용자-분류별로 진행중인 채팅방은 한 개)
		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", customer.get("cstm_uid"));
		params.put("endYn", "N");
		params.put("todayYn", "Y");
		params.put("cstmDelYn", "N");
		params.put("cstmLinkDivCd", customer.get("cstm_link_div_cd"));
		params.put("page", 1);
		params.put("size", 1);
		params.put("sorter", "room_create_dt desc");

		Map<String, Object> chatRoom = chatDao.selectChatRoom(params);

		// 채팅방 정보 발행
		// messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new
		// NoticeMessage(chatRoom));

		return chatRoom != null
				? new ChatRoom().fromMap(chatRoom)
						: null;
	}

	/**
	 * 채팅방 UID 생성
	 */
	@Transactional
	public String createChatRoomUid() {

		String uuid = UUID.randomUUID().toString();
		for (int i = 0; i < 10; i++) {
			if (selectChatRoomByChatRoomUid(uuid) == null) {
				return uuid;
			}
		}

		throw new UnsupportedOperationException();
	}

	/**
	 * 채팅방 생성
	 * @throws Exception
	 */
	@Transactional
	public ChatRoom createChatRoom(@NotEmpty Map<String, Object> customer, @NotEmpty String departCd, @NotEmpty String ctgNum,
			@NotEmpty Map<String, Object> siteSetting, String roomTitle, String deviceType) throws Exception {

		return createChatRoom(customer, departCd, ctgNum, null, siteSetting, false, roomTitle, null, deviceType, "N", "");
	}

	/**
	 * 채팅방 생성
	 * @throws Exception
	 */
	@Transactional
	public ChatRoom createChatRoom(@NotEmpty Map<String, Object> customer, @NotEmpty String departCd,
			@NotEmpty String ctgNum, @NotEmpty String entranceCode, @NotEmpty Map<String, Object> siteSetting,
			boolean isRelayBot, String roomTitle, String withBotYn, String deviceType, String loginYn, String goodCode) throws Exception {

		ChatRoom chatRoom = new ChatRoom(createChatRoomUid(), customer, ctgNum,
				Strings.isNullOrEmpty(roomTitle) ? (String) siteSetting.get("cns_title") : roomTitle, customProperty.getDefaultAssignMemeberUid());
		chatRoom.setEntranceCode(entranceCode);
		chatRoom.setCstmOsDivCd(deviceType);
		chatRoom.setIsCreatedChatRoom(true);
		chatRoom.setGoodCode(goodCode);

		// 부서 세팅
		/*Map<String, Object> category = categoryService.selectCategory(ctgNum);
		if ("Y".equals(category.get("dft_ctg_yn"))) {
			chatRoom.setDepartCd(DEPART_CD_NONE);
			// 기본 분류이지만 부서 코드가 있을 경우, 부서 세팅
			if (DEPART_CD_TM.equals(departCd) || DEPART_CD_CS.equals(departCd)) {
				chatRoom.setDepartCd(departCd);
			}
		} else {
			chatRoom.setDepartCd((String) category.get("depart_cd"));
		}*/
		/* chatRoom.setDepartCd(DEPART_CD_CS); */

		/**
		 * IPCC_MCH ARS 추가 관련 부서 코드 세팅
		 */
		String cstmLinkDivCd = (String) customer.get("cstm_link_div_cd");
		if (CommonConstants.CSTM_LINK_DIV_CD_E.equals(cstmLinkDivCd)) {
			chatRoom.setDepartCd(departCd);
		}

		//기간계 연동 고객이름,고객아이디 저장
		String cocId = String.valueOf(customer.get("coc_id"));
		Map<String, Object> customerSinfo = mcaService.sgd1611p(cocId, CommonConstants.MCA_CHANNEL_CODE);
		if(customerSinfo.get("CLNT_NAME") != null) {
			String cocNm = String.valueOf(customerSinfo.get("CLNT_NAME"));
			chatRoom.setCstmCocNm(cocNm);
			chatRoom.setCstmCocId(cocId);
			chatRoom.setRoomCocNm(cocNm);
			chatRoom.setRoomCocId(cocId);
		}

		chatRoom.setCstmLinkDivCd(cstmLinkDivCd);
		/*
		 * IPCC_MCH 기본 설정에 따라 챗봇 사용하도록 수정
		 */
		Map<String, Object> blockCheck = happyBotService.selectGreetingBotBlock(chatRoom.getCstmLinkDivCd());
		boolean withChatBot = settingService.isUseChatbot(siteSetting, chatRoom.getCstmLinkDivCd().toString());
		if(withChatBot) { // 챗봇사용여부 체크
			if(Integer.parseInt(blockCheck.get("FIRST_BLOCK_ID").toString()) > 0 && "Y".equals(blockCheck.get("USE_YN"))) { // 챗봇사용여부와 시나리오 사용여부는 다름(카카오는 본인인증으로 넘어감)
				withChatBot = true;
			}else {
				withChatBot = false;
			}			
		}
		// 해피봇 사용 여부 위의 blockcheck 이후로 변경 
		/*
		 * boolean withChatBot = settingService.isUseChatbot(siteSetting,
		 * chatRoom.getCstmLinkDivCd().toString()); if ("Y".equals(withBotYn)) {
		 * withChatBot = true; } else if ("N".equals(withBotYn)) { withChatBot = false;
		 * }
		 */

		//디지털 자산관리 C 에서만 봇상담 시나리오 사용 // 카카오톡 사용
		// 배정 대상 (챗봇, 상담원)
		//if ( (withChatBot && chatRoom.getCstmLinkDivCd().equals(CSTM_LINK_DIV_CD_C)) || CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd()) ) {
		/*
		 * IPCC_MCH 기본 설정에 따라 챗봇 사용하도록 수정
		 */
		if (withChatBot) {
				//&& !CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) { // 카카오 채널은 오픈빌더 사용하므로 해피봇 사용 안함
			chatRoom.setFrtCnsrDivCd(CNSR_DIV_CD_R);
			chatRoom.setCnsrDivCd(CNSR_DIV_CD_R);
		} else {
			chatRoom.setFrtCnsrDivCd(CNSR_DIV_CD_C);
			chatRoom.setCnsrDivCd(CNSR_DIV_CD_C);
		}

		// 채널 봇(오픈 빌더 등) 상담이력으로 채팅방 생성
		//		if (!CSTM_LINK_DIV_CD_A.equals(chatRoom.getCstmLinkDivCd())) { // 채널 사용
		if (isRelayBot) { // 채널 봇 사용
			chatRoom.setFrtCnsrDivCd(CNSR_DIV_CD_R);
			chatRoom.setCnsrDivCd(CNSR_DIV_CD_R);
		}
		//		}

		// 채팅방 반영
		saveChatRoom(chatRoom);
		log.info("SAVED NEW CHATROOM");

		return chatRoom;
	}

	/**
	 * 채팅방 삭제 (테스트용)
	 */
	public int deleteChatRoom(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);

		return chatDao.deleteChatRoom(params);
	}

	/**
	 * 채팅방 조회
	 */
	public ChatRoom selectChatRoomByChatRoomUid(String chatRoomUid) {

		return selectChatRoomByChatRoomUid(chatRoomUid, false, false);
	}
	
	/**
	 * 채팅방 조회 (senderName 포함)
	 */
	public ChatRoom selectChatRoomByChatRoomUid(ChatMessage chatMessage) {

		return selectChatRoomByChatRoomUid(chatMessage, false, false);
	}

	/**
	 * 채팅방 조회
	 */
	public ChatRoom selectChatRoomByChatRoomUid(String chatRoomUid, boolean withMetaInfo, boolean withCustomerLastChat) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		if (withMetaInfo) {
			params.put("withMetaInfo", "withMetaInfo");
		}
		if (withCustomerLastChat) {
			params.put("withCustomerLastChat", "withCustomerLastChat");
		}
		log.debug("SELECT CHATROOM");
		Map<String, Object> chatRoomMap = chatDao.selectChatRoom(params);
		if (chatRoomMap != null) {
			return new ChatRoom().fromMap(chatRoomMap);
		} else {
			return null;
		}
	}
	
	/**
	 * 채팅방 조회 (senderName 포함)
	 */
	public ChatRoom selectChatRoomByChatRoomUid(ChatMessage chatMessage, boolean withMetaInfo, boolean withCustomerLastChat) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatMessage.getChatRoomUid());
		params.put("senderUid", chatMessage.getSenderUid());
		if (withMetaInfo) {
			params.put("withMetaInfo", "withMetaInfo");
		}
		if (withCustomerLastChat) {
			params.put("withCustomerLastChat", "withCustomerLastChat");
		}
		log.debug("SELECT CHATROOM");
		Map<String, Object> chatRoomMap = chatDao.selectChatRoom(params);
		if (chatRoomMap != null) {
			return new ChatRoom().fromMap(chatRoomMap);
		} else {
			return null;
		}
	}

	/**
	 * 채팅방 조회
	 */
	public ChatRoom selectChatRoomWithMetaInfoByChatRoomUid(String chatRoomUid, String ctgNum) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("withMetaInfo", "withMetaInfo");
		params.put("ctgNum", ctgNum);
		log.debug("SELECT CHATROOM WITH META INFO");
		Map<String, Object> chatRoomMap = chatDao.selectChatRoom(params);
		log.debug("CHATROOM MAP: {}", chatRoomMap);
		if (chatRoomMap != null) {
			return new ChatRoom().fromMap(chatRoomMap);
		} else {
			return null;
		}
	}

	/**
	 * 채팅방 조회 (고객의 마지막 대화순 최근 채팅방)
	 */
	public ChatRoom selectChatRoomByCstmUid(String cstmUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", cstmUid);
		params.put("sorter", "r.last_chat_dt desc");
		params.put("page", "1");
		params.put("size", "1");
		log.debug("SELECT CHATROOM BY CSTM_UID");
		Map<String, Object> chatRoomMap = chatDao.selectChatRoom(params);
		if (chatRoomMap != null) {
			return new ChatRoom().fromMap(chatRoomMap);
		} else {
			return null;
		}
	}

	/**
	 * 고객별 채팅방 목록
	 */
	public List<ChatRoom> selectChatRoomListByCustomer(String userId, Boolean isTodayOnly, Boolean withMetaInfo) {

		List<ChatRoom> chatRoomList = new ArrayList<ChatRoom>();

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", userId);
		if (isTodayOnly) {
			params.put("todayYn", "Y");
		}
		params.put("cstmDelYn", "N");
		params.put("sorter", "r.room_create_dt desc");
		params.put("withCustomerLastChat", "withCustomerLastChat");
		// 최근 n개
		// params.put("page", 1);
		// params.put("size", 5);
		log.debug("SELECT CHATROOM LIST");
		List<Map<String, Object>> list = chatDao.selectChatRoomList(params);

		for (Map<String, Object> map : list) {
			ChatRoom chatRoom = new ChatRoom();
			chatRoom.fromMap(map);
			chatRoomList.add(chatRoom);
		}

		return chatRoomList;
	}

	/**
	 * 고객별 채팅방 목록 카운트
	 */
	public Integer selectChatRoomListCountByCustomer(String userId, Boolean endYn) {

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", userId);
		if (endYn) {
			params.put("todayYn", "Y");
		}

		log.debug("SELECT CHATROOM LIST COUNT BY CUSTOMER");
		Integer count = chatDao.selectChatRoomListCount(params);

		return count;
	}

	/**
	 * 고객별 채팅방 목록 카운트
	 */
	public Map<String, Object> selectChatRoomGoodCode(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);


		log.debug("SELECT selectChatRoomGoodCode");
		Map<String, Object> goodInfo = chatDao.selectChatRoomGoodCode(params);

		return goodInfo;
	}

	/**
	 * 상담원 페이지, 채팅방 목록
	 */
	public List<ChatRoom> selectChatRoomListByCounselor(String memberUid, String status, Integer page, String sort,
			String q) {

		Map<String, Object> siteSetting = settingService.selectSiteSetting();

		// 쿼리 조건
		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomStatusCd", status);
		if (CHAT_ROOM_STATUS_CD_WAIT_CNSR.equals(status) || status == null) { // 미배정 || 전체
			if (settingService.isAutoAssign(siteSetting)) { // 자동 배정이면, 자신에게 배정된 목록
				params.put("memberUid", memberUid);
			} else { // 수동 배정이면
				// 상담원 직접 배정 가능 이면, 자신에게 배정된 목록 + 미배정 목록
				if (settingService.isSelfAssign(siteSetting)) {
					// TODO: 전체 목록 = 미배정 + 자기 배정
					params.put("memberUidList", Arrays.asList(memberUid, customProperty.getDefaultAssignMemeberUid()));
				} else {
					params.put("memberUid", memberUid);
				}
			}
		} else {
			params.put("memberUid", memberUid);
		}
		params.put("todayYn", "Y"); // 상담원은 오늘자 목록만

		// 내용 검색이 있을 경우
		if (!Strings.isNullOrEmpty(q)) {
			params.put("q", q);
			log.error("SELECT CHATROOM LIST BY COUNSELOR");
			List<String> chatRoomUidList = selectChatRoomUidListBySearch(params);
			if (chatRoomUidList.isEmpty()) {
				return Collections.emptyList();
			}
			// 조건 변경
			params = new HashMap<>();
			params.put("chatRoomUidList", chatRoomUidList);
		}

		// 페이징, 정렬
		params.put("page", page);
		params.put("size", customProperty.getChatRoomPageSize());
		if ("chat+asc".equals(sort)) {
			params.put("sorter", "r.last_chat_dt asc");
		} else if ("chat+desc".equals(sort)) {
			params.put("sorter", "r.last_chat_dt desc");
		} else if ("cstm+asc".equals(sort)) {
			params.put("sorter", "r.coc_id asc, r.cstm_uid asc");
		} else if ("cstm+desc".equals(sort)) {
			params.put("sorter", "r.coc_id desc, r.cstm_uid desc");
		} else if ("cnsr_time+asc".equals(sort)) {
			params.put("sorter", "r.cnsr_link_dt asc");
		} else if ("cnsr_time+desc".equals(sort)) {
			params.put("sorter", "r.cnsr_link_dt desc");
		} else if ("end_review+asc".equals(sort)) {
			params.put("sorter", "r.end_review_dt asc");
		} else if ("end_review+desc".equals(sort)) {
			params.put("sorter", "r.end_review_dt desc");
		} else if ("channel+asc".equals(sort)) {
			params.put("sorter", "r.cstm_link_div_cd asc");
		} else if ("channel+desc".equals(sort)) {
			params.put("sorter", "r.cstm_link_div_cd desc");
		} else {
			params.put("sorter", "r.last_chat_dt desc");
		}

		// 결과 채팅방 목록
		List<ChatRoom> chatRoomList = new ArrayList<ChatRoom>();
		log.debug("SELECT CHATROOM LIST BY COUNSELOR");

		if (CHAT_ROOM_STATUS_CD_MANAGER_COUNSEL.equals(status)) { // 매니저 관여 요청 목록
			Map<String, Object> managerReqParams = new HashMap<>();
			managerReqParams.put("managerUid", memberUid);
			managerReqParams.put("reqType", LOG_CODE_MANAGER_COUNSEL);
			managerReqParams.put("todayYn", "Y");
			List<Map<String, Object>> managerReqList = chatDao.selectChatCnsrChngReqList(managerReqParams);

			List<String> chatRoomUidList = new ArrayList<>();
			for (Map<String, Object> managerReq : managerReqList) {
				chatRoomUidList.add((String) managerReq.get("chat_room_uid"));
			}
			if (!chatRoomUidList.isEmpty()) {
				params.remove("chatRoomStatusCd");
				params.remove("memberUid");
				params.put("chatRoomUidList", chatRoomUidList);
			}
		}
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		String departCd = StringUtil.nvl(memberVO.getDepartCd(), "NN");
		if("DC".equals(departCd.toString()) || "M1".equals(departCd.toString()) || "M2".equals(departCd.toString()) ) {
			params.put("dateSrch" , "N");
		}
		List<Map<String, Object>> list = chatDao.selectChatRoomList(params);
		for (Map<String, Object> map : list) {
			ChatRoom chatRoom = new ChatRoom();
			chatRoom.fromMap(map);
			chatRoomList.add(chatRoom);
		}

		return chatRoomList;
	}

	/**
	 * 채팅 내용 검색
	 */
	private List<String> selectChatRoomUidListBySearch(Map<String, Object> params) {

		log.error("SELECT CHATROOM LIST BY SEARCH");
		return chatDao.selectChatRoomUidListBySearch(params);
	}

	/**
	 * 채팅방 목록 (매니저 페이지)
	 */
	@Deprecated
	public List<ChatRoom> selectChatRoomListByManager(String memberUid, long sinceMilliseconds) {

		Map<String, Object> params = new HashMap<>();
		params.put("managerUidList", Arrays.asList(memberUid, customProperty.getDefaultAssignMemeberUid()));
		params.put("roomCreateDtGt", new Timestamp(sinceMilliseconds));
		params.put("sorter", "r.manager_uid desc, r.room_create_dt desc");
		List<Map<String, Object>> list = chatDao.selectChatRoomList(params);

		List<ChatRoom> chatRoomList = new ArrayList<ChatRoom>();
		for (Map<String, Object> map : list) {
			ChatRoom chatRoom = new ChatRoom();
			chatRoom.fromMap(map);
			chatRoomList.add(chatRoom);
		}

		return chatRoomList;
	}

	/**
	 * 채팅방에 마지막 채팅 내역 세팅 (채팅방 상태 변경함)
	 */
	public ChatRoom setLastChatMessage(ChatRoom chatRoom, ChatMessage chatMessage) {

		Assert.notNull(chatMessage.getChatNum(), "NO CHAT NUM");

		// 대화 내용 저장
		chatRoom.setLastChatNum(chatMessage.getChatNum().toString());
		chatRoom.setLastChatCont(ChatMessage.bulidForLastChatMessage(chatMessage.getCont()));
		chatRoom.setLastChatDt(commonService.selectSysdate());
		chatRoom.setSenderDivCd(chatMessage.getSenderDivCd());

		// 채팅방 상태 변경
		if (MEMBER_DIV_CD_C.equals(chatRoom.getCnsrDivCd())) { // 상담원과 대화중
			// 진행중이면 상태 변경
			if (CHAT_ROOM_STATUS_CD_NEED_ANSWER.equals(chatRoom.getChatRoomStatusCd()) // 응대필요
					|| CHAT_ROOM_STATUS_CD_WAIT_REPLY.equals(chatRoom.getChatRoomStatusCd())) { // 답변대기
				if (MEMBER_DIV_CD_U.equals(chatRoom.getSenderDivCd())) { // 고객 메세지
					chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_NEED_ANSWER);
				} else {
					chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_REPLY);
				}
			} else if (CHAT_ROOM_STATUS_CD_ASSIGN.equals(chatRoom.getChatRoomStatusCd())) { // 미접수
				if (MEMBER_DIV_CD_C.equals(chatRoom.getSenderDivCd())) { // 상담원 메세지
					chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_WAIT_REPLY);
				}
			}
		}

		return chatRoom;
	}

	/**
	 * 채팅방에 마지막 채팅 내역 저장
	 */
	@Transactional
	public ChatRoom saveLastChatMessage(ChatRoom chatRoom, ChatMessage chatMessage) {

		chatRoom = setLastChatMessage(chatRoom, chatMessage);

		// Map<String, Object> params = new HashMap<>();
		// params.put("chatRoomUid", chatRoom.getChatRoomUid());
		// params.put("lastChatNum", chatRoom.getLastChatNum());
		// params.put("lastChatCont", chatRoom.getLastChatCont());
		// params.put("lastChatDt", chatRoom.getLastChatDt());
		// params.put("senderDivCd", chatRoom.getSenderDivCd());
		// params.put("chatRoomStatusCd", chatRoom.getChatRoomStatusCd());
		// int d = chatDao.saveChatRoom(params);
		int d = chatDao.saveChatRoom(chatRoom.toMap());
		assert (d == 1);

		// 채팅방 정보 발행
		// log.debug("Publish to {} chatRoom: {}", customProperty.getWsNoticePath(), new
		// NoticeMessage(chatRoom));
		// messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new
		// NoticeMessage(chatRoom));

		return chatRoom;
	}

	/**
	 * 채팅방 저장
	 */
	@Transactional
	public boolean saveChatRoom(ChatRoom chatRoom) {
		if (chatDao.saveChatRoom(chatRoom.toMap()) == 1) {
			return true;
		} else {
			log.error("NOT SAVED CHATROOM");
			throw new UnsupportedOperationException("NOT SAVED CHATROOM");
		}
	}

	/**
	 * 채팅방 수정, NULL 값으로 업데이트시 필요
	 */
	@Transactional
	public boolean updateRoomMark(ChatRoom chatRoom) {
		if (chatDao.updateRoomMark(chatRoom.toMap()) == 1) {
			return true;
		} else {
			log.error("NOT SAVED CHATROOM");
			throw new UnsupportedOperationException("NOT SAVED CHATROOM");
		}
	}

	/**
	 * 채팅방 저장 및 소켓 메세지 발행
	 */
	@Transactional
	public boolean saveAndNoticeChatRoom(ChatRoom chatRoom) {
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>saveAndNoticeChatRoom : {}", chatRoom);

		if (chatDao.saveChatRoom(chatRoom.toMap()) == 1) {
			// 채팅방 정보 발행
			messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new NoticeMessage(chatRoom));
			return true;
		} else {
			log.error("NOT SAVED CHATROOM");
			return false;
		}
	}

	/**
	 * 채팅방 소켓 메세지 발행
	 */
	public void noticeChatRoom(ChatRoom chatRoom) {

		messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new NoticeMessage(chatRoom));
	}

	/**
	 * 메세지 발송 후 채팅방 종료 상태 저장 및 발행
	 */
	@Transactional
	public ChatRoom endChatRoom(ChatRoom chatRoom, ChatMessage chatMessage, String endRoomStatus, String commiter) throws Exception {

		chatService.sendMessage(chatRoom, chatMessage.getChatRoomUid(), chatMessage);
		return endChatRoom(chatRoom, endRoomStatus, commiter);
	}

	/**
	 * 채팅방 종료 상태 저장 및 발행
	 */
	@Transactional
	public ChatRoom endChatRoom(ChatRoom chatRoom, String endRoomStatus, String commiter) throws Exception {

		// 채팅방 저장
		chatRoom.setEndYn("Y");
		chatRoom.setEndDivCd(endRoomStatus);
		chatRoom.setRoomEndDt(commonService.selectSysdate());
		chatRoom.setChatRoomStatusCd(CHAT_ROOM_STATUS_CD_END);

		int d = chatDao.saveChatRoom(chatRoom.toMap());
		assert (d == 1);

		// 종료 정보 저장
		Map<String, Object> customer = customerService.selectCustomerByCstmUid(chatRoom.getCstmUid());
		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoom.getChatRoomUid());
		params.put("cocId", customer.get("coc_id"));
		params.put("linkIp", customer.get("link_ip"));
		params.put("cstmDivCd", customer.get("cstm_div_cd"));
		params.put("loginYn", customer.get("login_yn"));
		params.put("name", customer.get("name"));
		params.put("creater", commiter);

		log.debug(">>>>>>>>>>>endChatRoom<<<<<<<<<<<");
		log.debug("params >>>>>>>>>>>"+params);
		log.debug(">>>>>>>>>>>endChatRoom<<<<<<<<<<<");

		d = chatDao.saveChatEndInfo(params);
		assert (d == 1);

		// 종료시 데이터 정규화
		normalizeEndChatRoom(chatRoom);

		// 챗봇과 대화 중인 경우 종료 API 호출
		if (CNSR_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) {
			// 해피봇
			if (customProperty.getCategoryBotMemeberUid().equals(chatRoom.getMemberUid())) {
				chatBotService.endSession(chatRoom);
			}
		}

		// 외부 채널 사용일 경우, 종료 API 호출
		if (CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
			if (END_DIV_CD_CNSR_CNSR.equals(endRoomStatus)) {
				channelService.sendClose(chatRoom, ChatContents.Command.end_by_counselor);
			} else if (END_DIV_CD_CNSR_CSTM.equals(endRoomStatus)) {
				channelService.sendClose(chatRoom, ChatContents.Command.end_by_customer);;
			} else if (END_DIV_CD_CENTER_OFF_DUTY.equals(endRoomStatus) ) {
				channelService.sendClose(chatRoom, ChatContents.Command.center_off_duty);
			} else if (END_DIV_CD_COUNSELOR_POOL_OVER.equals(endRoomStatus) ) {
				channelService.sendClose(chatRoom, ChatContents.Command.no_counselor);
			} else { //if (END_DIV_CD_NORMAL.equals(endRoomStatus)) {
				channelService.sendClose(chatRoom, ChatContents.Command.end_slient);
			}
		}

		// 매니저 요청 삭제
		assignService.updateChatCnsrChngReq(chatRoom.getChatRoomUid(), ChatCommand.CHANGE_COUNSELOR, customProperty.getSystemMemeberUid());
		assignService.updateChatCnsrChngReq(chatRoom.getChatRoomUid(), ChatCommand.MANAGER_COUNSEL, customProperty.getSystemMemeberUid());
		
		// 카카오일경우 채팅방 사용자의 인증정보 삭제
		if(CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
			authService.deleteAuth(chatRoom.getChatRoomUid());
		}

		return chatRoom;
	}

	/**
	 * 날짜 자동 종료
	 */
	@Transactional
	public void endChatRoomByScheduler(String channel) {

		// 상담원 배정 채팅방 종료
		Map<String, Object> selectParams = new HashMap<>();
		//selectParams.put("cnsrDivCd", CNSR_DIV_CD_C);
		//selectParams.put("endYn", "N");
		//selectParams.put("oldYn", "Y");
		selectParams.put("channel", channel);
		List<Map<String, Object>> roomList = chatDao.selectChatRoomByScheduler(selectParams);
		for (Map<String, Object> room : roomList) {
			try {
				ChatRoom chatRoom = new ChatRoom();
				chatRoom.fromMap(room);

				// 종료시 데이터 정규화
				normalizeEndChatRoom(chatRoom);

				// 외부 채널 사용일 경우, 종료 API 호출
				if (CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
					channelService.sendClose(chatRoom, ChatContents.Command.end_slient);
				}

				// 매니저 요청 삭제
				assignService.updateChatCnsrChngReq(chatRoom.getChatRoomUid(), ChatCommand.CHANGE_COUNSELOR, customProperty.getSystemMemeberUid());
				assignService.updateChatCnsrChngReq(chatRoom.getChatRoomUid(), ChatCommand.MANAGER_COUNSEL, customProperty.getSystemMemeberUid());

				// 채팅방 정보 발행
				//messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), new NoticeMessage(chatRoom));

				// 카카오 채팅방 사용자의 인증정보 삭제
				if(CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
					int delete = memberService.deleteCustomerKakao();
					//authService.deleteAuth(chatRoom.getChatRoomUid());
				}

			} catch (Exception e) {
				HTUtils.batmanNeverDie(e);
				log.error(e.getLocalizedMessage(), e);
			}
		}

		Map<String, Object> updateParams = new HashMap<>();
		updateParams.put("shrCnsrDivCd", CNSR_DIV_CD_C);
		updateParams.put("shrEndYn", "N");
		updateParams.put("chatRoomStatusCd", CHAT_ROOM_STATUS_CD_END);
		updateParams.put("endYn", "Y");
		updateParams.put("endDivCd", END_DIV_CD_CNSR_BAT);
		updateParams.put("channel", channel);

		chatDao.endChatRoomByScheduler(updateParams);

		// 분류봇 배정 채팅방 종료
		updateParams = new HashMap<>();
		//updateParams.put("shrMemeberUid", customProperty.getCategoryBotMemeberUid());
		updateParams.put("shrCnsrDivCd", CNSR_DIV_CD_R);
		updateParams.put("shrEndYn", "N");
		updateParams.put("chatRoomStatusCd", CHAT_ROOM_STATUS_CD_END);
		updateParams.put("endYn", "Y");
		updateParams.put("endDivCd", END_DIV_CD_BOT_BAT);
		updateParams.put("channel", channel);

		chatDao.endChatRoomByScheduler(updateParams);
	}

	/**
	 * 채팅방 종료시, 데이터 정리
	 */
	public void normalizeEndChatRoom(@NotNull ChatRoom chatRoom) {

		// 고객 메세지, 개인 정보 블라인드 처리
		List<ChatMessage> chatMessageList = chatService.selectChatMessageListByChatRoomUid(chatRoom.getChatRoomUid());
		for (ChatMessage chatMessage : chatMessageList) {
			try {
				if (!Strings.isNullOrEmpty(chatMessage.getCont())
						&& SENDER_DIV_CD_U.equals(chatMessage.getSenderDivCd())) {
					log.debug("cont: {}", chatMessage.getCont());
					String blindCont = StringUtil.getMaskedString(chatMessage.getCont());
					log.debug("blindCont: {}", chatMessage.getCont());
					if (!chatMessage.getCont().equals(blindCont)) {
						chatMessage.setCont(blindCont);
						chatService.updateChatMessageCont(chatMessage);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}

		// 챗봇, 기본상담원 종료시 자동 후처리
//
//		if (CNSR_DIV_CD_R.equals(chatRoom.getCnsrDivCd())
//				|| (CNSR_DIV_CD_C.equals(chatRoom.getCnsrDivCd())
//						&& customProperty.getDefaultAssignMemeberUid().equals(chatRoom.getMemberUid()))) {
//			try {
//				log.info("SEND END INFO TO LEGACY");
//				// 기본 분류
//				String joinedEndCategoryCode = "00017|00003|00005|00145"; // 00017|오키톡|00003|일반문의|00005|요청|00145|무응답
//
//				// 봇 세션 선택
//				Map<String, Object> botSession = happyBotService.selectBotSessionById(chatRoom.getChatRoomUid());
//				if (botSession != null) {
//					if (botSession.get("last_green_block_id") != null) {
//						// 봇 세션 중, 마지막 성공 블록
//						BigDecimal lastGreenBlockId = (BigDecimal) botSession.get("last_green_block_id");
//						log.info("botSession : {}", botSession);
//						log.info("lastGreenBlockId : {}", lastGreenBlockId);
//						Map<String, Object> botBlock = happyBotService.selectBotBlockById(lastGreenBlockId.longValue());
//						// 마지막 성공 블록에 세팅된 종료 분류
//						if (botBlock != null && !Strings.isNullOrEmpty((String) botBlock.get("end_category_id"))) {
//							joinedEndCategoryCode = (String) botBlock.get("end_category_id");
//						}
//					}
//					/*
//					 * // 자동 후처리 Map<String, Object> endCategory =
//					 * categoryService.selectEndCategory(joinedEndCategoryCode); if (endCategory !=
//					 * null) { log.info("endCategory: {}", endCategory); Map<String, Object>
//					 * chatEndInfo = new HashMap<>(); chatEndInfo.put("updater",
//					 * customProperty.getSystemMemeberUid()); chatEndInfo.put("chatRoomUid",
//					 * chatRoom.getChatRoomUid()); chatEndInfo.put("memberDivCd", CNSR_DIV_CD_R);
//					 * chatEndInfo.put("cocId", chatRoom.getRoomCocId()); chatEndInfo.put("name",
//					 * chatRoom.getRoomCocNm()); chatEndInfo.put("dep1CtgCd",
//					 * endCategory.get("depth1_cd")); chatEndInfo.put("dep1CtgNm",
//					 * endCategory.get("depth1_cd_nm")); chatEndInfo.put("dep2CtgCd",
//					 * endCategory.get("depth2_cd")); chatEndInfo.put("dep2CtgNm",
//					 * endCategory.get("depth2_cd_nm")); chatEndInfo.put("dep3CtgCd",
//					 * endCategory.get("depth3_cd")); chatEndInfo.put("dep3CtgNm",
//					 * endCategory.get("depth3_cd_nm")); chatEndInfo.put("dep4CtgCd",
//					 * endCategory.get("depth4_cd")); chatEndInfo.put("dep4CtgNm",
//					 * endCategory.get("depth4_cd_nm")); chatEndInfo.put("memo", "자동상담 중 고객종료");
//					 * chatService.saveChatEndInfo(chatEndInfo);
//					 *
//					 * // 기간계로 종료 정보 제공 chatRoom.setEndCtgCd1("00017");
//					 * chatRoom.setEndCtgCd2((String) endCategory.get("dep2_ctg_cd"));
//					 * chatRoom.setEndCtgCd3((String) endCategory.get("dep3_ctg_cd"));
//					 * chatRoom.setEndCtgCd4((String) endCategory.get("dep4_ctg_cd"));
//					 * legacyService.postEndChatRoom(chatRoom); }
//					 */
//				}
//			} catch (Exception e) {
//				log.error(e.getLocalizedMessage(), e);
//			}
//		}
	}

	/**
	 * 고객창 > 채팅방 목록에서 감춤
	 */
	@Transactional
	public int hideChatRoomByCustomer(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		params.put("cstmDelYn", "Y");

		return chatDao.saveChatRoom(params);
	}

	/**
	 * 채팅방 상태별 카운터
	 */
	public Map<String, Integer> getStatusCounter(String memberUid, String rollType, String departCd, String pageType) {

		Map<String, Integer> result = new HashMap<String, Integer>();
		log.debug("SYSDATE: {}", new DateTime(commonService.selectSysdate()));

		// 챗봇 관련 카운터
		if (MEMBER_DIV_CD_S.equals(rollType)
				|| MEMBER_DIV_CD_A.equals(rollType)
				|| MEMBER_DIV_CD_M.equals(rollType)) {
			Map<String, Object> params = new HashMap<>();
			params.put("statusCdAssignBot", CHAT_ROOM_STATUS_CD_BOT);
			params.put("statusCdEnd", CHAT_ROOM_STATUS_CD_END);
			params.put("botMemberUidList", Arrays.asList(customProperty.getHappyBotMemeberUid(), customProperty.getCategoryBotMemeberUid()));

			Map<String, Object> resultChatBot = chatDao.selectChatRoomStatusCounter(params);
			int assign_bot = ((BigDecimal) resultChatBot.get("assign_bot")).intValue();
			int end_bot = ((BigDecimal) resultChatBot.get("end_bot")).intValue();
			result.put("assign_robot", assign_bot);
			result.put("end_robot", end_bot);
		}

		// 상담원 관련 카운터
		{
			Map<String, Object> siteSetting = settingService.selectSiteSetting();
			Map<String, Object> params = new HashMap<>();
			params.put("defaultMemberUid", customProperty.getDefaultAssignMemeberUid());
			params.put("rollType", rollType);
			
			// 슈퍼어드민, 관리자
			if (MEMBER_DIV_CD_S.equals(rollType)
					|| MEMBER_DIV_CD_A.equals(rollType)) {

				//params.put("departCd", departCd);
			}

			// 매니저
			else if (MEMBER_DIV_CD_M.equals(rollType)) {

				params.put("managerUid", memberUid);
				params.put("pageType", pageType);
				//params.put("departCd", departCd);


				if (!settingService.isAutoAssign(siteSetting) // 수동 배정이면
						&& settingService.isSelfAssign(siteSetting)) { // 상담원 직접 배정 가능 이면, 자신에게 배정된 목록 + 미배정 목록
					params.put("managerUidList",
							Arrays.asList(memberUid, customProperty.getDefaultAssignMemeberUid()));
				}
			}

			// 상담원
			else if (MEMBER_DIV_CD_C.equals(rollType)) {

				params.put("memberUid", memberUid);

				if (!settingService.isAutoAssign(siteSetting) // 수동 배정이면
						&& settingService.isSelfAssign(siteSetting)) { // 상담원 직접 배정 가능 이면, 자신에게 배정된 목록 + 미배정 목록
					params.put("memberUidList",
							Arrays.asList(memberUid, customProperty.getDefaultAssignMemeberUid()));
				}
			}

			params.put("statusCdWaitCounselor", CHAT_ROOM_STATUS_CD_WAIT_CNSR);
			params.put("statusCdAssign", CHAT_ROOM_STATUS_CD_ASSIGN);
			params.put("statusCdNeedAnswer", CHAT_ROOM_STATUS_CD_NEED_ANSWER);
			params.put("statusCdWaitReply", CHAT_ROOM_STATUS_CD_WAIT_REPLY);
			params.put("statusCdEnd", CHAT_ROOM_STATUS_CD_END);

			//params.put("endDivCdList", Arrays.asList(END_DIV_CD_CNSR_CSTM, END_DIV_CD_CNSR_CNSR, END_DIV_CD_CNSR_TIME, END_DIV_CD_CNSR_BAT, END_DIV_CD_NORMAL));
			/*
			 * if("DC".equals(departCd.toString()) || "M1".equals(departCd.toString()) ||
			 * "M2".equals(departCd.toString()) ) { params.put("dateSrch" , "N"); }
			 */

			Map<String, Object> resultByMemeberUid = chatDao.selectChatRoomStatusCounterByMemberUid(params);
			//log.info("******************************* {}" , params);
			int wait_counselor = ((BigDecimal) resultByMemeberUid.get("wait_counselor")).intValue();
			int assign_counselor = ((BigDecimal) resultByMemeberUid.get("assign_counselor")).intValue();
			int need_answer = ((BigDecimal) resultByMemeberUid.get("need_answer")).intValue();
			int wait_reply = ((BigDecimal) resultByMemeberUid.get("wait_reply")).intValue();
			int end_counselor = ((BigDecimal) resultByMemeberUid.get("end_counselor")).intValue();
			int all = wait_counselor + assign_counselor + need_answer + wait_reply + end_counselor;

			result.put("wait_counselor", wait_counselor);
			result.put("assign_counselor", assign_counselor);
			result.put("need_answer", need_answer);
			result.put("wait_reply", wait_reply);
			result.put("end_counselor", end_counselor);
			result.put("all", all);
		}

		// 매니저 관련 카운터
		if (MEMBER_DIV_CD_M.equals(rollType)) {
			Map<String, Object> params = new HashMap<>();
			params.put("managerUid", memberUid);
			//if (MEMBER_DIV_CD_S.equals(rollType)) {
			//	params.put("departCd", departCd);
			//}

			Map<String, Object> resultExtra = chatDao.selectChatRoomExtraStatusCounterByMemberUid(params);
			int request_change_counselor = ((BigDecimal) resultExtra.get("request_change_counselor")).intValue();
			int request_manager_counsel = ((BigDecimal) resultExtra.get("request_manager_counsel")).intValue();
			int request_cstm_grade = ((BigDecimal) resultExtra.get("request_cstm_grade")).intValue();
			int request_review = ((BigDecimal) resultExtra.get("request_review")).intValue();
			result.put("request_change_counselor", request_change_counselor);
			result.put("request_manager_counsel", request_manager_counsel);
			result.put("request_cstm_grade", request_cstm_grade);
			result.put("request_review", request_review);
		}

		return result;
	}
}
