package ht.controller;

import static ht.constants.CommonConstants.CNSR_DIV_CD_R;
import static ht.constants.CommonConstants.CONT_DIV_CD_T;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_B;
import static ht.constants.CommonConstants.CSTM_OS_DIV_CD_WEB;
import static ht.constants.CommonConstants.DEPART_CD_NONE;
import static ht.constants.CommonConstants.END_DIV_CD_BOT_CSTM;
import static ht.constants.CommonConstants.MSG_STATUS_CD_SEND;
import static ht.constants.CommonConstants.SENDER_DIV_CD_R;
import static ht.constants.CommonConstants.SENDER_DIV_CD_U;
import static ht.constants.MessageConstants.VIEW_CATE_ERROR_INVALID_USER;
import static ht.constants.MessageConstants.VIEW_ENTER_ERROR_COMMON;
import static ht.constants.MessageConstants.VIEW_ENTER_ERROR_INVALID_USER;
import static ht.constants.MessageConstants.VIEW_ROOM_ERROR_NOT_GRANTED_USER;
import static ht.constants.MessageConstants.VIEW_ROOM_ERROR_NO_CHATROOM;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.ApiItemWrapper;
import ht.domain.ChannelVO;
import ht.domain.ChatMessage;
import ht.domain.ChatRoom;
import ht.service.AssignService;
import ht.service.AuthService;
import ht.service.CategoryService;
import ht.service.ChatRoomService;
import ht.service.ChatService;
import ht.service.CommonService;
import ht.service.CustomerService;
import ht.service.ManageService;
import ht.service.McaService;
import ht.service.MemberService;
import ht.service.ScpAgentService;
import ht.service.SettingService;
import ht.service.TermService;
import ht.service.channel.ChannelService;
import ht.util.DateUtil;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class CustomerController {

	private static final String COOKIE_NAME_CUSTOMER_ID = "CUSTOMER_ID";
	private static final String COOKIE_NAME_SIMULATE_CUSTOMER_ID = "SIMULATE_CUSTOMER_ID";

	@Resource
	private CustomProperty customProperty;
	@Resource
	private MemberService memberService;
	@Resource
	private CustomerService customerService;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private AssignService assignService;
	@Resource
	private SettingService settingService;
	@Resource
	private HTUtils htUtils;
	@Resource
	private CommonService commonService;
	@Resource
	private ManageService manageService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private ChannelService channelService;
	@Resource
	private AuthService authService;
//	@Resource
//	private LegacyService legacyService;
//	@Resource
//	private MappingCustomerService mappingCustomerService;
	@Resource
	private WebSocketController webSocketController;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private TermService termService;
	@Resource
	private McaService mcaService;
	@Resource
	private ScpAgentService scpagentService;

	private String getSkillParams(ChannelVO vo) {

		String skillParams = "";
		if(vo != null) {
			String goodCode = Strings.isNullOrEmpty(vo.getGoodCode())? "" : vo.getGoodCode();
			String userId = Strings.isNullOrEmpty(vo.getEntity_ID())? "" : vo.getEntity_ID();
			String screenNum = Strings.isNullOrEmpty(vo.getAppCode())? "" : vo.getAppCode();

			skillParams += "goodCode="+ goodCode;
			skillParams += "&userId="+ userId;
			skillParams += "&screenNum="+ screenNum;

		}

		return skillParams;
	}
	/**
	 * 웹 채팅 고객 진입점
	 */
	@RequestMapping(path = "/customer", method = { RequestMethod.GET, RequestMethod.POST })
	public String customer(@CookieValue(value = COOKIE_NAME_CUSTOMER_ID, required = false) String cstmUid,
						   //@RequestParam(value = "withBotYn", required = false, defaultValue = "Y") String withBotYn, // 챗봇 사용 여부
							/*
							 * IPCC_MCH 기본 정보 설정 적용에 따른 defaultValue 제거
							 */
						   @RequestParam(value = "withBotYn", required = false) String withBotYn, // 챗봇 사용 여부
						   @RequestParam(value = "goodCode", required = false) String goodCode, // 상품 정보
						   @RequestParam(value = "chkSurvey", required = false) String chkSurvey, // 투자정보확인 flag
						   @RequestParam(value = "refActCd", required = false) String refActCd, // 이전 액티비티 code
						   @RequestParam(value = "entranceCode", required = false) String entranceCode, // 진입 경로
						   @RequestParam(value = "ctgNum", required = false) String ctgNum, // 분류
						   @RequestParam(value = "hideEndBtn", required = false) boolean hideEndBtn, // 종료 버튼 노출 여부
						   @RequestParam(value = "hideCallBtn", required = false) boolean hideCallBtn, // 상담전화
						   @RequestParam(value = "userId", required = false) String userId, // 고객 식별자
						   @RequestParam(value = "userName", required = false) String userName, // 고객 이름
						   @RequestParam(value = "title", required = false) String title, // 채팅방 제목
						   @RequestParam(value = "useFrame", required = false) String useFrame, // 프레임 모드
						   @RequestParam(value = "channel", required = false) String channel, // OK: 채널 구분 ///////////////////
						   @RequestParam(value = "login_yn", required = false, defaultValue = "n") String loginYn, // OK: 로그인 여부
						   @RequestParam(value = "token", required = false) String token, // OK: 토근
						   @RequestParam(value = "simulation", required = false) boolean simulation, // 시뮬레이션, 시뮬레이션 여부
						   @RequestParam(value = "blockId", required = false) Long blockId, // 시뮬레이션, 블록 식별자
						   @RequestParam(value = "appCode", required = false) String appCode, // O2 인앱 화면번호
						   /**
						    * IPCC_MCH ARS 팀추가 관련 파라미터 추가
						    */
						   @RequestParam(value = "departCd", required = false) String departCd, // 부서코드
						   @RequestHeader HttpHeaders headers,
						   @RequestHeader(value = "User-Agent") String userAgent,
						   @RequestParam(value = "crypto", required = false) String crypto, // DAMO
						   HttpSession session, HttpServletResponse response, HttpServletRequest request, RedirectAttributes attributes, Model model,
						   @RequestParam Map<String, String> params,
						   @RequestBody(required = false) String requestBody) throws Exception {

		log.info("crypto ==>" + crypto);
		/* TODO: 최종 운영계 반영시 활성
		if (htUtils.isActiveProfile("live1") ||htUtils.isActiveProfile("live2") ) {
			log.info("live");
			if( (userId != null || channel != null) ) {
				return "error/error";
			}
		}*/

		if (crypto != null) {
			String params1 = scpagentService.getDecString(crypto);

			Gson gson = new Gson();
			ChannelVO channelVo = gson.fromJson(params1, ChannelVO.class);

			//crypto
			if(channelVo == null) {
				log.info("Crypto not decoding");
				return "error/error";
			}
			log.info("channelVo ==>" + channelVo.toString());

			String ch = channelVo.getCha_Code();
			if (ch != null) {
				switch (ch) {
					case CommonConstants.CHANNEL_CODE_A: // Homepage
						channel = CommonConstants.CSTM_LINK_DIV_CD_A;
						break;
					case CommonConstants.CHANNEL_CODE_B: // KAKAO
						channel = CommonConstants.CSTM_LINK_DIV_CD_B;
						break;
					case CommonConstants.CHANNEL_CODE_C: // O2
						channel = CommonConstants.CSTM_LINK_DIV_CD_C;
						break;
					case CommonConstants.CHANNEL_CODE_D: // mPop
						channel = CommonConstants.CSTM_LINK_DIV_CD_D;
						break;
					/*
					 * IPCC_MCH ARS 채널 추가
					 */
					case CommonConstants.CHANNEL_CODE_E: // ARS
						channel = CommonConstants.CSTM_LINK_DIV_CD_E;
						departCd = channelVo.getDepartCd();
						break;
					default:
						channel = null;
						break;
				}
			}
			
			if(channel == null) {
				log.info("channel null");
				return "error/error";
			}

			//고객 ID
			if(!channelVo.getEntity_ID().toString().isEmpty()) {
				userId =  channelVo.getEntity_ID().toString();
			}
			// LKJ 20210422 자릿수 초과로 인해 형변환 타입 변경 (int->long)
//			int intEntityId = (int) Double.parseDouble(userId.toString());
//			String entityId = Integer.toString(intEntityId);
			long intEntityId = (long) Double.parseDouble(userId.toString());
			String entityId = Long.toString(intEntityId);
			//appCode = channelVo.getAppCode();
			//goodCode = channelVo.getGoodCode();

			if (entityId != null) {
				userId = entityId;
				log.info("crypto userId ==>" + userId);
			}
		}

		if (simulation != true && blockId == null) {
			if ((channel.equals("") || CommonConstants.CSTM_LINK_STRING_MAP.get(channel) == null) && !simulation) {
				return "error/error";
			}
		}

		/// 앱에서 넘어오는 코드 Zerofill 처리
		if (!Strings.isNullOrEmpty(goodCode)) {
			if (goodCode.indexOf(".") == 0) {
				goodCode = Integer.toString(Integer.parseInt(goodCode));
			}
		} else {
			goodCode = null;
		}

		if (!Strings.isNullOrEmpty(appCode)) {
			appCode = Integer.toString(Integer.parseInt(appCode));
		} else {
			appCode = null;
		}

		// 삼성증권 진입 경로 goodCode와 appcode를 entranceCode 로 변환
		if (goodCode == null && appCode != null) { // 상품코드 없는 화면 처리
			entranceCode = appCode;
		} else if (goodCode != null && appCode != null) {
			entranceCode = appCode + "|" + goodCode;
		} else {
			entranceCode = null;
		}
		
		log.info("appCode|goodCode = entranceCode : " + entranceCode);

		if (!Strings.isNullOrEmpty(loginYn)) {
			loginYn = loginYn.toUpperCase();
		}
		if (!Strings.isNullOrEmpty(token)) {
			token = token.replaceAll(" ", "+");
		}

		boolean isUserExist = false;
		// 유저 아이디 정보 확인
		if (userId != null) {
			isUserExist = true;
		}

		//아이디가 있을 경우 기간계연동->Name 가져오기
		if (isUserExist) {
			log.info("channel : >>>" + channel + "<<< //// userName :>>>" + userName + "<<<");

			Map<String, Object> customerSinfo = mcaService.sgd1611p(userId, CommonConstants.MCA_CHANNEL_CODE);
			if (customerSinfo.get("CLNT_NAME") != null) {
				userName = String.valueOf(customerSinfo.get("CLNT_NAME"));
				log.info("channel : >>>" + channel + "<<< //// userName :>>>" + userName + "<<<");
			}
		}

		if (CommonConstants.CSTM_LINK_DIV_CD_A.equals(channel)) { // 홈페이지

		} else if (CommonConstants.CSTM_LINK_DIV_CD_B.equals(channel)) { // 카카오
			if (isUserExist) {
				return openKakaoCounsel(loginYn, token, model);
			}
		} else if (CommonConstants.CSTM_LINK_DIV_CD_C.equals(channel)) { // O2 다자관

		} else if (CommonConstants.CSTM_LINK_DIV_CD_D.equals(channel)) { // mPOP

		/*
		 * IPCC_MCH ARS 채널 추가
		 */
		} else if (CommonConstants.CSTM_LINK_DIV_CD_E.equals(channel)) { // ARS

		}

		// 사이트 세팅
		Map<String, Object> siteSetting = settingService.selectSiteSetting();
		// 디바이스 구분
		String deviceType = htUtils.getDeviceType(userAgent, false);

		// pc 접근 또는 mPop 접근시 전화걸기 ICON 미노출
		if (!CommonConstants.CSTM_LINK_DIV_CD_C.equals(channel)) {
			hideCallBtn = true;
		} else {
			hideCallBtn = false;
		}

		// ////////////////////////////////////////////////////////////////////
		// 사용자 식별
		// ////////////////////////////////////////////////////////////////////
		Map<String, Object> customer = null;

		if (simulation) {
			// customer = customerService.createCustomer(userId, userName, channel,
			// deviceType, headers, request);
			customer = customerService.selectCustomerByCstmUid(customProperty.getSimulatorUid());
		} else {
			// 고객 식별
			if (!Strings.isNullOrEmpty(userId)) {
				// 채널 User
				if (!Strings.isNullOrEmpty(channel)) {
					Map<String, Object> inParam = new HashMap<>();
					inParam.put("cocId", userId);
					inParam.put("cstmLinkDivCd", channel);
					customer = customerService.selectCustomerByCocId(inParam);
				} else {
					// userId 파라미터가 있을 경우 사용
					customer = customerService.selectCustomerByCocId(userId);
				}
			} else if (!Strings.isNullOrEmpty(cstmUid)) {
				// cstmUid 쿠키값이 있을 경우 사용
				customer = customerService.selectCustomerByCstmUid(cstmUid);
			}
			// 고객 식별이 불가능 할 경우 생성 및 저장
			if (customer == null) {
				customer = customerService.createCustomer(userId, userName, channel, deviceType, headers, request);
			} else { // 기존 고객 정보가 있을 경우 업데이트
				customer = customerService.updateCustomer(customer, userId, userName, deviceType, headers, request);
			}
		}

		log.info("CUSTOMER: {}", customer);

		// 고객 정보 세션 저장
		session.setAttribute("user", customer);
		// 고객 정보 쿠키 저장
		setCustomerCookie(response, customer, deviceType, COOKIE_NAME_CUSTOMER_ID);

		// ////////////////////////////////////////////////////////////////////
		// 채팅방 옵션
		// ////////////////////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////////////////////
		// 분류
		// 유효성 검증
		if (!Strings.isNullOrEmpty(ctgNum) && !Strings.isNullOrEmpty(channel)) {
			Map<String, Object> category = new HashMap<String, Object>();
			category = settingService.selectCategory(ctgNum, channel, false);
//			if (!Strings.isNullOrEmpty(channel)) {
//				category = settingService.selectCategory(ctgNum, channel, false);
//			} else {
//				category = settingService.selectCategory(ctgNum, false);
//			}
			if (category == null) {
				ctgNum = null;
			}
		}

		// 분류 ctg num 이 없을 경우 각 채널별 default category setting
		if (Strings.isNullOrEmpty(ctgNum)) {
			Map<String, Object> defaultCtgMap = new HashMap<String, Object>();
			if (CommonConstants.CSTM_LINK_DIV_CD_E.equals(channel)) {
				defaultCtgMap = categoryService.selectDefaultCategoryByChannel(channel, departCd);
			} else {
				defaultCtgMap = categoryService.selectDefaultCategoryByChannel(channel);
			}
			ctgNum = (String) defaultCtgMap.get("ctg_num");
		}

		// ////////////////////////////////////////////////////////////////////
		// User Agent
		customer.put("cstm_os_div_cd", deviceType);

		// ////////////////////////////////////////////////////////////////////
		// 채팅방 선택 및 생성
		// ////////////////////////////////////////////////////////////////////
		// 채널별
		ChatRoom chatRoom = null;

		if (!simulation) {
			chatRoom = chatRoomService.selectChatRoomByChannel(customer);
		} else {
			chatRoom = null;
		}
		boolean isCreatedChatRoom = false;
		// 분류별
		// chatRoom = chatRoomService.selectChatRoomByCtgNum(customer, ctgNum, siteSetting);
		if (chatRoom == null) {
			// 해피봇 시뮬레이션
			if (simulation) {
				if (blockId == null) {
					throw new UnsupportedOperationException("시뮬레이션, 블록 식별자 파라미터가 존재하지 않습니다.");
				} else {
					title = "SIMULATION / BLOCK / " + blockId;
					// 고객 정보 쿠키 저장
					//setCustomerCookie(response, customer, deviceType, COOKIE_NAME_SIMULATE_CUSTOMER_ID);
					// 해피봇 사용으로 설정
					withBotYn = "Y";
					siteSetting.put("chatbot_use_yn", "Y");
				}
				siteSetting.put("chatbot_use_yn", "Y");
			}
			/*
			 * IPCC_MCH 파라미터에 값이 있는 경우에만 해당 값 사용 외의 경우 기본 설정 값 사용
			 */
			withBotYn = StringUtil.nvl(withBotYn, (settingService.isUseChatbot(siteSetting, channel) ? "Y" : "N"));
			/*
			 * IPCC_MCH ARS 채널 추가 관련 부서 코드 세팅
			 */
			chatRoom = chatRoomService.createChatRoom(customer, departCd, ctgNum, entranceCode, siteSetting, false, title, withBotYn, deviceType, "Y", goodCode);
			chatRoom = assignService.assignChatRoom(chatRoom, siteSetting, false, false);		/// 상담원 배정 시도
			isCreatedChatRoom = true;
			hideEndBtn = false;					//상담직원 연결시 종료버튼 hide
		}

		log.info("CHAT ROOM: {}", chatRoom);

		// 기간계 정보
		if (!Strings.isNullOrEmpty(token) // 토큰이 있을 경우
				&& isCreatedChatRoom) { // 채팅방이 새로 생성됐을 경우

//			Map<String, Object> cert = legacyService.ggggggggetCertWithToken(chatRoom, deviceType, token);
//			List<Map<String, Object>> certList = legacyService.getCertWithToken(chatRoom, deviceType, token);
//			setLegacyCustomerInfo(certList, chatRoom, customer, session, "L");
		}

		// 선택 및 생성한 채팅방으로 리다이렉트
		//attributes.addAttribute("userId", userId);
		//		attributes.addAttribute("entranceCode", ctgNum);
		//		return new RedirectView(customProperty.getRedirectBasePath() + "/customer/" + chatRoom.getChatRoomUid());

		// 프레임 사용시 파라미터
		// attributes.addAttribute("hideEndBtn", hideEndBtn);
		// if (!Strings.isNullOrEmpty(useFrame))
		// 	attributes.addAttribute("useFrame", useFrame);

		//attributes.addAttribute("simulation", simulation);
		attributes.addAttribute("hideEndBtn", hideEndBtn);
		attributes.addAttribute("hideCallBtn", hideCallBtn);
//		attributes.addAttribute("appCode", 		appCode);
		return "redirect:/customer/" + chatRoom.getChatRoomUid();
//		return new RedirectView(customProperty.getRedirectBasePath() + "/customer/" + chatRoom.getChatRoomUid());
	}

	/**
	 * 카카오 상담톡 오픈
	 */
	public String openKakaoCounsel(String loginYn, String token, Model model) {

		model.addAttribute("kakaoCounselId", customProperty.getKakaoCounselId());
		model.addAttribute("extra", "loginYn=" + loginYn + "&token=" + token);

		return "customer/kakaoCounsel";
	}


	/**
	 * 네이버 톡톡 오픈
	 */
	public String openNaverTalkTalk(String loginYn, String token, Model model) {

		model.addAttribute("", customProperty.getNaverTalkTalkId());
		return "customer/naverTalkTalk";
	}

	/**
	 * 카카오 싱크 랜딩 페이지
	 */
	@GetMapping(path = "/customer/kakao/sync")
	public String postEvent(
			@RequestParam("chatRoomUid") @NotEmpty String chatRoomUid
			, Model model
	) {
		model.addAttribute("chatRoomUid", chatRoomUid);
		model.addAttribute("kakaoJavascriptApiKey", customProperty.getKakaoJavascriptApiKey());

		return "customer/kakaoSync";
	}
	
	

	/**
	 * 고객 채팅방 페이지
	 */
	@GetMapping(path = "/customer/{chatRoomUid}")
	public String customerRoom(@CookieValue(value = COOKIE_NAME_CUSTOMER_ID) String cstmUid,
			@CookieValue(value = COOKIE_NAME_SIMULATE_CUSTOMER_ID, required = false) String simulatorCstmUid,
			@PathVariable(value = "chatRoomUid") String chatRoomUid,
			@RequestParam(value = "hideEndBtn", required = false) boolean hideEndBtn, // 종료 버튼 노출 여부
			@RequestParam(value = "hideCallBtn", required = false) boolean hideCallBtn, // 상담전화
			@RequestParam(value = "userType", required = false) String userType, // 고객 타입
			@RequestParam(value = "userId", required = false) String userId, // 고객 식별자
			@RequestParam(value = "useFrame", required = false) String useFrame, // 고객 식별자
			@RequestParam(value = "simulation", required = false) boolean simulation, // 시뮬레이션, 시뮬레이션 여부
			@RequestHeader(value = "User-Agent") String userAgent,
			@RequestParam(value = "appCode", required = false) String appCode, 	// O2 인앱 화면번호
			Model model, HttpSession session,
			HttpServletResponse response) throws Exception {

		log.debug("CUSTOMER ENTER CHATROOM");
		//model.addAttribute("hideEndBtn", hideEndBtn);
		model.addAttribute("useFrame", useFrame);

		// 디바이스 구분
		String deviceType = htUtils.getDeviceType(userAgent, false);

		// 사용자 유효성 체크
		Map<String, Object> user = null;
		// 쿠키 사용
		if (cstmUid != null) {

			user = customerService.selectCustomerByCstmUid(cstmUid);
//			model.addAttribute("message", VIEW_ROOM_ERROR_NOT_GRANTED_USER);
//			return "/customer/info";
		}
		// 세션에 값이 있을 경우 사용
//		else if (session.getAttribute("user") != null) {
//			user = (Map<String, Object> ) session.getAttribute("user");
//		}
//		Map<String, Object> user = customerService.selectCustomer(session, userId, cstmUid);
		if (user == null) {
			log.error("INVALID USER TYPE: userType:{}, session: {}, userId: {}, cstmUid: {}", userType,
					session.getAttribute("user"), userId, cstmUid);
			model.addAttribute("message", VIEW_ROOM_ERROR_NOT_GRANTED_USER);
			return "/customer/info";
		}
		model.addAttribute("user", user);

		// 고객 정보 쿠키 저장
		setCustomerCookie(response, user, deviceType, COOKIE_NAME_CUSTOMER_ID);

		// 채팅방 유효성 검증
		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
		if (chatRoom == null) {
			log.error("NO CHAT ROOM: COOKIE CSTM: {}, CHATROOM UID: {}", cstmUid, chatRoomUid);
			model.addAttribute("message", VIEW_ROOM_ERROR_NO_CHATROOM);
			return "customer/info";
		} else if (!chatRoom.getCstmUid().equals(cstmUid)) {
			if (simulation) {
					//&& customProperty.getSimulatorUid().equals(simulatorCstmUid)) { // 시뮬레이션 유저는 통과
				;
			} else {
				log.error("WRONG CUSTOMER: COOKIE CSTM: {}, CHATROOM: {}", cstmUid, chatRoom);
				model.addAttribute("message", VIEW_ROOM_ERROR_NOT_GRANTED_USER);
				return "customer/info";
			}
		}
		model.addAttribute("chatRoom", chatRoom);


		if(!CommonConstants.CSTM_LINK_DIV_CD_C.equals(chatRoom.getCstmLinkDivCd())) {
			hideCallBtn = true;
		}else {
			hideCallBtn = false;
		}
		model.addAttribute("hideCallBtn", hideCallBtn);

		//상담직원 배정 후 종료버튼 X
		/*
		 * if(chatRoom.getChatRoomStatusCd().equals(CHAT_ROOM_STATUS_CD_BOT) ||
		 * chatRoom.getChatRoomStatusCd().equals(CHAT_ROOM_STATUS_CD_WAIT_CNSR) ) {
		 * hideEndBtn = false; }else { hideEndBtn = false; }
		 */
		hideEndBtn = false;
		model.addAttribute("hideEndBtn", hideEndBtn);

		// 사이트 세팅
		Map<String, Object> siteSetting = settingService.selectSiteSetting();
		Map<String, Object> workMap = manageService.selectWorkYnCheck(DateUtil.getCurrentDate("yyyyMMdd"), chatRoom.getCstmLinkDivCd().toString());
		if (workMap != null && !workMap.isEmpty()) {
			siteSetting.putAll(workMap);
		}
		model.addAttribute("siteSetting", siteSetting);

		///O2 화면번호
		model.addAttribute("appCode", appCode);
		// O2 고객아이디(entityId)
		// LKJ 20210422 자릿수 초과로 인해 형변환 타입 변경 (int->long)
//		int intEntityId = (int) Double.parseDouble(chatRoom.getCstmCocId().toString());
//		String entityId = Integer.toString(intEntityId);
		long intEntityId = (long) Double.parseDouble(chatRoom.getCstmCocId() != null ? chatRoom.getCstmCocId().toString() : "1006124397");
		String entityId = Long.toString(intEntityId);
		model.addAttribute("entityId", entityId);
		
		// 채팅방 스킨 조회
		model.addAttribute("skinCssUrl", getSkinCssUrl(chatRoom.getCstmLinkDivCd()));
		// Static 리소스용 파라미터
		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		// 빌더 URL
		model.addAttribute("happyBotBuilderUrl", customProperty.getHappyBotBuilderUrl());
		// 프로파일
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));
		model.addAttribute("isDevelope", htUtils.isActiveProfile("dev"));
		
		
		///특정일 이후 기능 차단
		DateTime now = new DateTime(commonService.selectSysdate().getTime());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		String chkChatDis = "N";
		DateTime deadLine = null;
		if(htUtils.isActiveProfile("devtom")) {
			deadLine = fmt.parseDateTime("2020-12-24 00:00:00");
		}
		else if(htUtils.isActiveProfile("live1") || htUtils.isActiveProfile("live2")) {
			deadLine = fmt.parseDateTime("2021-01-24 00:00:00");
		}
		if (now.isBefore(deadLine)) {
			chkChatDis = "N";
		}
		if(now.isAfter(deadLine)) {
			chkChatDis = "Y";
		}

		model.addAttribute("chkChatDis", chkChatDis);
		if(chatRoom.getCstmLinkDivCd().equals(CommonConstants.CSTM_LINK_DIV_CD_D)) {//mpop일경우 분리
			return "customer/home_mpop";
		}

		return "customer/home";
	}

	/**
	 * 고객 이벤트 -> 고객 메세지 자동 발화
	 */
	@CrossOrigin
	@PostMapping(path = "/api/customer/event/{chatRoomUid}")
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> postEvent(
			@PathVariable("chatRoomUid") @NotEmpty String chatRoomUid,
			@RequestParam("event_type") @NotNull CustomerEventType eventType,
			@RequestParam(value = "category_id", required = false) String categoryId,// 자동 상담직원 연결, TODO: to ChatContents.autoWTF
			@RequestParam(value="coc_id", required = false) String cocId, // 필수	// 인증 완료 이벤트 (홈페이지 -> 게이트웨이(EAI) -> 해피톡)
			@RequestParam(value="coc_nm", required = false) String cocNm, // 필수
			@RequestParam(value="auth_type", required = false, defaultValue = "phone") String authType,
			@RequestParam(value="auth_result", required = false, defaultValue = "00") String authResultCode,
			@RequestParam(value="kakao_crtfc", required = false) String kakaoCrtfc,
			@RequestParam Map<String, Object> params,
			@RequestBody(required = false) String requestBody) throws Exception
	{
		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
		if (chatRoom == null) {
			log.info("NO CHAT ROOM, CHAT ROOM UID: {}", chatRoomUid);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Map<String, Object> customer = customerService.selectCustomerByChatRoom(chatRoom.getChatRoomUid());
		if (customer == null) {
			log.info("NO CUSTOMER, CHAT ROOM UID: {}", chatRoom.getChatRoomUid());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// 종료 채팅방
		if ("Y".equals(chatRoom.getEndYn())) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		ChatMessage chatMessage;
		String textMessage;

		// ////////////////////////////////////////////////////////////////////
		// 인증 완료 (휴대폰 인증, 카카오 싱크 인증)
		// ////////////////////////////////////////////////////////////////////
		if (CustomerEventType.auth_complete.equals(eventType) || CustomerEventType.kakao_sync_complete.equals(eventType) || CustomerEventType.kakao_sync_failed.equals(eventType)) {
			// 인증 정보 저장
			if (!Strings.isNullOrEmpty(cocId)) {
				Map<String, Object> auth = authService.selectAuth(chatRoom.getChatRoomUid());
				if (auth != null) {
					auth.put("authId", auth.get("auth_id"));
					auth.put("cocId", cocId);
					auth.put("authType", authType);
					//auth.put("authPosition", authPosition);
					authService.saveAuth(auth, 5);
				} else {
					auth = new HashMap<>();
					auth.put("chatRoomUid", chatRoom.getChatRoomUid());
					auth.put("cstmUid", chatRoom.getCstmUid());
					auth.put("cocId", cocId);
					auth.put("authType", authType);
					//auth.put("authPosition", authPosition);
					authService.saveAuth(auth, 5);
				}

				// 고객 정보 저장
				if ("00".equals(authResultCode) && (kakaoCrtfc.equals("Y|Y") || kakaoCrtfc.equals("N|Y") || kakaoCrtfc.equals("Y|N") )) { // 본인 인증 정상
					customer = customerService.updateCustomer(customer, cocId, cocNm, kakaoCrtfc);
					chatRoom.setRoomCocId(cocId);
					chatRoom.setRoomCocNm(cocNm);
					chatRoom.setAuthType("P");
					
					if (CustomerEventType.kakao_sync_complete.equals(eventType)) {
						chatRoom.setAuthType("K");
						log.info("Set KAKAO ChatRoom : {}", chatRoom);
					}
					chatRoomService.saveChatRoom(chatRoom);
				}else {
					
					textMessage = "우수고객이 아닙니다.";
					chatMessage = chatService.buildChatMessage(chatRoom, ChatMessage.buildChatMessageText(textMessage));
					log.info(" KAKAO NOT VIP chatMessage : {}", chatMessage);
					return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.OK);
				}
			}

//			textMessage = legacyService.getAuthMessage(authResultCode, chatRoom.getCnsrDivCd());
			textMessage = "문의사항을 남겨주시면 업무시간 중 빠르게 답변드리겠습니다.\r\n" +
							"\r\n" +
							"*업무시간:평일 08:30~17:00";			//변경

			// 챗봇
			if (CNSR_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) {
				// 마지막 조건 블록
				chatMessage = chatService.buildChatMessage(chatRoom, ChatMessage.buildChatMessageText(textMessage, "HappyTalk/RequestCounselor")); // HappyTalk/RequestCounselor: 상담원 연결
				chatMessage.setSenderDivCd(SENDER_DIV_CD_U);
				chatMessage.setSenderUid(chatRoom.getCstmUid());
				webSocketController.message(chatMessage);

				// 카카오
				if (CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd())) {
					chatMessage = chatService.buildChatMessage(chatRoom, ChatMessage.buildChatMessageText(textMessage));
					chatMessage.setSenderDivCd(SENDER_DIV_CD_R);
					chatMessage.setSenderUid(customProperty.getHappyBotMemeberUid());
					channelService.sendMessage(chatRoom, chatMessage);
				}
			}
			// 상담직원
			else {
				throw new UnsupportedOperationException("상담원 채팅 중 인증 없음");
			}
		}
		// ////////////////////////////////////////////////////////////////////
		// 자동 상담직원 연결 (챗봇 메세지)
		// ////////////////////////////////////////////////////////////////////
		else if (CustomerEventType.auto_request_counselor.equals(eventType)) {
			if(Strings.isNullOrEmpty(kakaoCrtfc)) {
				kakaoCrtfc = "" + customer.get("KAKAO_CRTFC");
			}

			if(!"N|N".equals(kakaoCrtfc)) {
				if(Strings.isNullOrEmpty(categoryId)) {
					Map<String, Object> defaultCategory = new HashMap<>();
					if("Y|N".equals(kakaoCrtfc)) {
						 defaultCategory = categoryService.selectDefaultCategoryByChannel(chatRoom.getCstmLinkDivCd(), "DC");
					}else {
						 defaultCategory = categoryService.selectDefaultCategoryByChannel(chatRoom.getCstmLinkDivCd(), "FM");
					}
					 categoryId = (String) defaultCategory.get("ctg_num");
					 log.info("Kakao reqeust counselor, defaultCategory : {}", defaultCategory);

				}

				 log.info(">> Kakao reqeust counselor, kakaoCrft :  {}, DEFAULT_CATEGORY : {}" , kakaoCrtfc, categoryId);

				// 챗봇
				if (CNSR_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) {
					chatMessage = chatService.buildChatMessage(chatRoom
							, ChatMessage.buildChatMessageText("상담직원 연결", "HappyTalk/RequestCounselor/" + categoryId));
					chatMessage.setSenderDivCd(SENDER_DIV_CD_U);
					chatMessage.setSenderUid(chatRoom.getCstmUid());
					webSocketController.message(chatMessage);
				}
				// 상담직원
				else {
					throw new UnsupportedOperationException("상담원 채팅 중 인증 없음");
				}
			}else {		//방 종료, 카카오 종료 메시지 보내기
				chatRoom = chatRoomService.endChatRoom(chatRoom, END_DIV_CD_BOT_CSTM, customProperty.getSystemMemeberUid());
			}

		}

		return new ResponseEntity<>(new ApiItemWrapper<>(), HttpStatus.OK);
	}

	/*
	public void setLegacyCustomerInfo(List<Map<String, Object>> certList, ChatRoom chatRoom
			, Map<String, Object> customer, HttpSession session, String authType) {
		if (!certList.isEmpty()) {
			Map<String, Object> legacyCustomerInfo = legacyService.getCustomerInfo(chatRoom, certList);
			// 고객 정보 저장
			if (legacyCustomerInfo != null
					&& legacyCustomerInfo.get("CUST_MNNO") != null
					&& legacyCustomerInfo.get("CUST_NM") != null) {
				customer = customerService.updateCustomer(customer, (String) legacyCustomerInfo.get("CUST_MNNO")
						, (String) legacyCustomerInfo.get("CUST_NM"));
				chatRoom.setRoomCocId((String) legacyCustomerInfo.get("CUST_MNNO"));
				chatRoom.setRoomCocNm((String) legacyCustomerInfo.get("CUST_NM"));
				if (Strings.isNullOrEmpty(authType)) {
					authType = "P";
				}
				chatRoom.setAuthType(authType);

				// 인증 정보 저장
				Map<String, Object> auth = authService.selectAuth(chatRoom.getChatRoomUid());
				if (auth != null) {
					auth.put("authId", auth.get("auth_id"));
					auth.put("cocId", legacyCustomerInfo.get("CUST_MNNO"));
					auth.put("authType", "phone");
					//auth.put("authPosition", authPosition);
					authService.saveAuth(auth, 5);
				} else {
					auth = new HashMap<>();
					auth.put("chatRoomUid", chatRoom.getChatRoomUid());
					auth.put("cstmUid", chatRoom.getCstmUid());
					auth.put("cocId", legacyCustomerInfo.get("CUST_MNNO"));
					auth.put("authType", "phone");
					//auth.put("authPosition", authPosition);
					authService.saveAuth(auth, 5);
				}

				chatRoomService.saveChatRoom(chatRoom);
				// 고객 정보 세션 저장
				if (session != null) {
					session.setAttribute("user", customer);
				}
			} else {
				log.info("LEGACY CUSTOMER, INVALID LEGACY CUSTOMER INFO: {}", legacyCustomerInfo);
			}
		} else {
			log.info("LEGACY CUSTOMER, NO ACCOUNT LIST");
		}
	}*/

	/**
	 * 고객 이벤트
	 */
	public enum CustomerEventType {
		auth_complete, auto_request_counselor, kakao_sync_complete, kakao_sync_failed
	}

	/**
	 * 고객 정보 쿠키 저장
	 */
	private void setCustomerCookie(HttpServletResponse response, Map<String, Object> user, String deviceType, String cookieName) {

		final int defaultMobileMaxAge = 60 * 60 * 24 * 30;
		final int defaultMaxAge = 60 * 60 * 24;
		final int defaultSimulateMaxAge = 60 * 60 * 12;

		Cookie cookieUserId = new Cookie(cookieName, (String) user.get("cstm_uid"));

		// 쿠키 만료일
		if (CSTM_OS_DIV_CD_WEB.equals(deviceType)) { // PC는 24시간
			if (COOKIE_NAME_SIMULATE_CUSTOMER_ID.equals(cookieName)) {
				cookieUserId.setMaxAge(defaultSimulateMaxAge);
			} else {
				cookieUserId.setMaxAge(defaultMaxAge);
			}
		} else { // 30일
			if (COOKIE_NAME_SIMULATE_CUSTOMER_ID.equals(cookieName)) {
				cookieUserId.setMaxAge(defaultSimulateMaxAge);
			} else {
				cookieUserId.setMaxAge(defaultMobileMaxAge);
			}
		}
		cookieUserId.setPath("/");
		response.addCookie(cookieUserId);
	}

	// ////////////////////////////////////////////////////////////////////////
	// DEPRECATED
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 채팅방 목록 페이지
	 */
	@GetMapping(path = "/customer/list")
	public String customerList(@CookieValue(value = COOKIE_NAME_CUSTOMER_ID, required = false) String cstmUid,
			@RequestParam(value = "userType", required = false) String userType, // 고객 타입
			@RequestParam(value = "userId", required = false) String userId, // 고객 식별자
			@RequestParam(value = "previousChatRoomUid", required = false) String previousChatRoomUid, // 이전 채팅방
			Model model) throws Exception {

		log.debug("CUSTOMER CHATROOM LIST");

		if (!Strings.isNullOrEmpty(previousChatRoomUid)) {
			model.addAttribute("previousChatRoomUid", previousChatRoomUid);
		}

		// 사용자 유효성 체크
		Map<String, Object> user = customerService.selectCustomer(userId, cstmUid);
		if (user == null) {
			log.error("INVALID USER TYPE: {}", userType);
			model.addAttribute("message", VIEW_CATE_ERROR_INVALID_USER);
			return "/customer/info";
		}
		model.addAttribute("user", user);

		// 채팅룸
		List<ChatRoom> chatRoomList = chatRoomService.selectChatRoomListByCustomer(cstmUid, false, true);
		model.addAttribute("chatRoomList", chatRoomList);

		return "customer/list";
	}

	/**
	 * 분류 선택 페이지
	 */
	@GetMapping(path = "/customer/category")
	public String category(@CookieValue(value = COOKIE_NAME_CUSTOMER_ID, required = false) String cstmUid,
			@RequestParam(value = "userType", defaultValue = "CSTM") String userType, // 고객 타입
			@RequestParam(value = "userId", required = false) String userId, // 고객 식별자
			Model model) throws Exception {

		log.debug("CUSTOMER CATEGORY");

		// 사용자 유효성 체크
		Map<String, Object> user = customerService.selectCustomer(userId, cstmUid);
		if (user == null) {
			log.error("INVALID USER TYPE: {}", userType);
			model.addAttribute("message", VIEW_CATE_ERROR_INVALID_USER);
			return "/customer/info";
		}
		model.addAttribute("user", user);
		model.addAttribute("ctgList", settingService.selectCategoryForCustomer());

		return "customer/category";
	}

	/**
	 * 고객이 채팅방 종료 API
	 */
	@CrossOrigin
	@GetMapping(path = "/api/customer/endChatRoom")
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> endChatRoom(
			@RequestParam(value = "cstmUid", required = false) String cstmUid)
	//			@CookieValue(value = COOKIE_NAME_CUSTOMER_ID, required = false) String cstmUid)
					throws Exception
	{
		if (Strings.isNullOrEmpty(cstmUid)) {
			log.info("NO COOKIE");
			return new ResponseEntity<>(HttpStatus.OK);
		}

		Map<String, Object> customer = customerService.selectCustomerByCstmUid(cstmUid);
		if (customer == null) {
			log.info("NO CUSTOMER");
			return new ResponseEntity<>(HttpStatus.OK);
		}

		ChatRoom chatRoom = chatRoomService.selectChatRoomByCstmUid(cstmUid);
		log.info(">>>>>>>>>>>>> END CHAT ROOM: {}", chatRoom);
		if (chatRoom != null && !"Y".equals(chatRoom.getEndYn())) {
			ChatMessage chatMessage = ChatMessage.builder()
					.signature("ChatMessage")
					.type(ChatMessage.ChatMessageType.END)
					.chatRoomUid(chatRoom.getChatRoomUid())
					.senderUid(chatRoom.getCstmUid())
					.senderDivCd(SENDER_DIV_CD_U)
					.contDivCd(CONT_DIV_CD_T)
					.msgStatusCd(MSG_STATUS_CD_SEND)
					.build();
			webSocketController.end(chatMessage, cstmUid);

			//			if (CNSR_DIV_CD_R.equals(chatRoom.getCnsrDivCd())) {
			//				chatRoomService.endChatRoom(chatRoom, END_DIV_CD_BOT_CSTM, cstmUid);
			//			} else if (CNSR_DIV_CD_C.equals(chatRoom.getCnsrDivCd())) {
			//				chatRoomService.endChatRoom(chatRoom, END_DIV_CD_CNSR_CSTM, cstmUid);
			//			} else {
			//				chatRoomService.endChatRoom(chatRoom, END_DIV_CD_NORMAL, cstmUid);
			//			}
		} else {
			log.info("NO END CHAT ROOM");
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 고객이 카카오 채팅방 종료 API
	 */
	@CrossOrigin
	@GetMapping(path = "/api/customer/kakao/cstmExpire")
	public @ResponseBody Map<String, Object> endKakaoChatRoom(
			@RequestParam(value = "cstmUid", required = true) String cstmUid,
			@RequestParam(value = "chatRoomUid", required = true) String chatRoomUid
		)	throws Exception
	{
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		try {
			/// 우수고객 정보 삭제
			customerService.deleteCustomerKakaoByCstm(cstmUid);
			
			//// 방종료 처리
			ChatRoom chatRoom = chatRoomService.selectChatRoomByCstmUid(cstmUid);
			log.info(">>>>>>>>>>>>> END CHAT ROOM: {}", chatRoom);
			if (chatRoom != null && !"Y".equals(chatRoom.getEndYn())) {
				ChatMessage chatMessage = ChatMessage.builder()
						.signature("ChatMessage")
						.type(ChatMessage.ChatMessageType.END)
						.chatRoomUid(chatRoom.getChatRoomUid())
						.senderUid(chatRoom.getCstmUid())
						.senderDivCd(SENDER_DIV_CD_U)
						.contDivCd(CONT_DIV_CD_T)
						.msgStatusCd(MSG_STATUS_CD_SEND)
						.build();
				webSocketController.end(chatMessage, cstmUid);
			} else {
				log.info("NO END CHAT ROOM");
			}
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 삭제되었습니다.");
		
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
		}
	
		return rtnMap;

	}
	
	/**
	 * 고객 채팅창 스킨 조회 (CSS Url)
	 *
	 * @return
	 */
	private String getSkinCssUrl(String channel) {

		if(channel.equals(CommonConstants.CSTM_LINK_DIV_CD_D)) {
			return CommonConstants.MPOP_SKIN_CSS_URL;
		}else {
			return CommonConstants.DEFAULT_SKIN_CSS_URL;
		}
	}

	/**
	 * 특정 시간까지 오픈하지 않기 위해 만든 임시 랜딩 페이지
	 */
	@RequestMapping(value = "/customer/intro", method = { RequestMethod.GET, RequestMethod.POST })
	@Deprecated
	public String main(ModelMap model) throws Exception {

		DateTime now = new DateTime(commonService.selectSysdate().getTime());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime deadline = fmt.parseDateTime("2018-11-26 00:00:00");
		if (now.isBefore(deadline)) {
			log.info("NOW IS BEFORE DEAD LINE");
			model.addAttribute("readonly", true);
		} else {
			model.addAttribute("readonly", false);
		}

		return "/customer/entrance";
	}

	/**
	 * 고객사 아이디 입력 페이지
	 */
	@GetMapping(path = "/customer/entrance")
	public String customerEntrance(@RequestParam Map<String, Object> param, Model model) throws Exception {

		model.addAttribute("readonly", false);

		String returnCode = (String) param.get("returnCode");
		if (!Strings.isNullOrEmpty(returnCode)
				&& "INVALID_USER".equals(returnCode)) {
			model.addAttribute("message", VIEW_ENTER_ERROR_INVALID_USER);
		} else {
			model.addAttribute("message", VIEW_ENTER_ERROR_COMMON);
		}
		log.debug("requestParams: {}", param);

		return "/customer/entrance";
	}

	/*
	@RequestMapping(value = "/api/customer/MappingInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> MappingInfo(@RequestParam(required = false) String chatRoomUid,HttpServletRequest request) {

		List<Map<String, Object>> rtnList = mappingCustomerService.selectMappingCustomerNotlonList(chatRoomUid);
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("rtnCd", CommonConstants.RESULT_NOT_FOUND);

		for(Map<String, Object> m: rtnList) {
			if(m.containsKey("cust_nm")) {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
				rtnMap.put("rtnMap",m);
				String srch_rrno = "0001011";
				String cust_gndr = "1";
				if(m.containsKey("srch_rrno")) {srch_rrno = m.get("srch_rrno").toString();}

				cust_gndr = srch_rrno.substring(6, 7);
				srch_rrno = srch_rrno.substring(0, 6);

				if(m.containsKey("cust_nm")) {m.put("cust_nm", base64Encode(m.get("cust_nm").toString()));} else {m.put("cust_nm","");}
				if(m.containsKey("cust_no")) {m.put("cust_no", base64Encode(m.get("cust_no").toString()));} else {m.put("cust_no","");}

				m.put("cust_rrno", base64Encode(srch_rrno));
				m.put("cust_gndr", cust_gndr);
				break;
			}
		}
		return rtnMap;
	}*/

	public String base64Encode(String encyt) {
		String retStr = new String(Base64.encodeBase64(encyt.toString().getBytes(StandardCharsets.UTF_8)));
		try {
			retStr = URLEncoder.encode(retStr, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return retStr;
	}

	/**
	 * TM 웹 채팅 고객 진입점
	 */
//	@RequestMapping(path = "/customer/tm", method = { RequestMethod.GET, RequestMethod.POST })
//	public String customer(@CookieValue(value = COOKIE_NAME_CUSTOMER_ID, required = false) String cstmUid,
//						   @RequestParam(value = "title", required = false) String title, // 채팅방 제목
//						   @RequestParam(value = "entranceCode", required = false) String entranceCode, // 진입 경로
//						   Model model)
//			throws Exception {
//
//		log.debug("CUSTOMER ENTER TM CHATROOM");
//
//		model.addAttribute("title", title);
//		model.addAttribute("entranceCode", entranceCode);
//		model.addAttribute("skinCssUrl", getSkinCssUrl());
//		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
//		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));
//
//		return "customer/home_tm";
//	}

	/**
	 * 용어검색 자동완성 목록 조회
	 */
	@RequestMapping(value = "/api/customer/selectAutoTermListAjax", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Map<String, Object>> selectAutoTermListAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			Locale locale, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectAutoTermListAjax, {}", param);

		try {
			String schText = StringUtil.nvl(param.get("schText"));
			model.put("schText", schText);
			// 용어사전 조회
			return termService.selectAutoTermListAjax(param);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			return Collections.emptyList();
		}
	}

	/**
	 * 용어검색 로그
	 */
	@RequestMapping(value = "/api/customer/insertReportingTerm", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void insertReportingTerm(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			Locale locale, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("insertReportingTerm, {}", param);

		try {
			termService.insertReportingTerm(param);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}
	}
}
