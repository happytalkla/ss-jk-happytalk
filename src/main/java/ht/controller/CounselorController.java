package ht.controller;

import static ht.constants.CommonConstants.LOG_CODE_MANAGER_COUNSEL_JOIN;
import static ht.constants.CommonConstants.SENDER_DIV_CD_S;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.constants.MessageConstants;
import ht.domain.ChatMessage;
import ht.domain.ChatMessage.ChatCommand;
import ht.domain.ChatRoom;
import ht.domain.MemberVO;
import ht.exception.BizException;
import ht.service.AssignService;
import ht.service.AuthService;
import ht.service.AutoCmpService;
import ht.service.ChatManageService;
import ht.service.ChatRoomService;
import ht.service.ChatService;
import ht.service.CommonService;
import ht.service.CustomerService;
import ht.service.McaService;
import ht.service.MemberAuthService;
import ht.service.MemberService;
import ht.service.NoticeService;
import ht.service.SettingService;
import ht.service.TemplateService;
import ht.service.UserService;
import ht.service.legacy.LegacyService;
import ht.service.legacy.MappingCustomerService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 상담직원 우측 메뉴 관리 controller
 * 상담이력
 * 상담템플릿 관리
 * 지식화 관리
 * 공지사항
 *
 * @author wizard
 *
 */
@Controller
@Slf4j
public class CounselorController {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private CommonService commonService;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private AssignService assignService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private ChatManageService chatManageService;
	@Resource
	private SettingService settingService;
	@Resource
	private TemplateService templateService;
	@Resource
	private MemberService memberService;
	@Resource
	private AutoCmpService autoCmpService;
	@Resource
	private AuthService authService;
	@Resource
	private CustomerService customerService;
//	@Resource
//	private MappingCustomerService mappingCustomerService;
//	@Resource
//	private LegacyService legacyService;
	@Resource
	private HTUtils htUtils;
	@Resource
	private SimpMessageSendingOperations messagingTemplate;
	@Resource
	private McaService mcaService;
	@Resource
	private UserService userService;
	
	/**
	 * 상담직원 페이지
	 */
	@GetMapping(path = "/counselor")
	public String counselor(
			Model model, HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "openChatRoom", required = false) String chatRoomUid,
			@RequestParam(value = "openChatRoomWithSystemMsg", required = false) Boolean openChatRoomWithSystemMsg) throws Exception {

		// ////////////////////////////////////////////////////////////////////
		// 사용자 정보
		// ////////////////////////////////////////////////////////////////////
		log.info("SESSION INFO, SESSION ID: {}, SESSION :{}", session.getId(), session);
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		Map<String, Object> user = memberService.selectMemberByMemberUid(memberVO.getMemberUid());
		if (user == null) { // 사용자 정보가 없을 경우
			log.error("NOT FOUND MEMBER: memberUid: {}", memberVO.getMemberUid());
			throw new UnsupportedOperationException("NOT FOUND MEMBER: " + memberVO.getCocId());
		}
		model.addAttribute("user", user);

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkHostName", customProperty.getHappyTalkHostName());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));
		model.addAttribute("openChatRoom", chatRoomUid);
		model.addAttribute("openChatRoomWithSystemMsg", openChatRoomWithSystemMsg);
		// 매니저 대화 진입
		if (chatRoomUid != null) {
			if (assignService.hasNotPermitedChatCnsrChngReq(chatRoomUid, ChatCommand.MANAGER_COUNSEL)) {

				ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
				// 로그 채팅 메세지 저장
				chatService.insertChatMessage(chatService.buildLogChatMessage(chatRoom, LOG_CODE_MANAGER_COUNSEL_JOIN));

				// 시스템 메세지 여부
				if (openChatRoomWithSystemMsg) {
					ChatMessage chatMessage = chatService.buildChatMessage(chatRoom,
							ChatMessage.buildChatMessageText(MessageConstants.MANAGER_INTERVENE_JOIN));
					chatMessage.setSenderDivCd(SENDER_DIV_CD_S);
					chatMessage.setSenderUid(customProperty.getSystemMemeberUid());

					chatService.insertChatMessage(chatMessage);
					messagingTemplate.convertAndSend(
							customProperty.getWsTopicPath() + "/" + chatRoom.getChatRoomUid(),
							chatMessage);
				}
			}
		}

		return "counselor/home";
	}

	/**
	 * 상담이력, 템플릿, 자동완성 목록
	 */
	@RequestMapping(value = "/chatCsnr/selectCnsInfoAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCnsInfoAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			String chatRoomUid = StringUtil.nvl(param.get("chatRoomUid"));
			if ("".equals(chatRoomUid)) {
				return "chatCsnr/cnsHisList";
			}

			// 세션 사용자 정보
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());
			param.put("departCd", memberVO.getDepartCd());
			model.put("departCd", memberVO.getDepartCd());

			// 기본 설정 정보
			Map<String, Object> defaultSet = settingService.selectDefaultSet();
			model.put("defaultSet", defaultSet);

			// 상담이력 목록 조회
			ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
			String roomCocId = chatRoom.getRoomCocId();
			// String cstmCocId = chatRoom.getCstmCocId();
			String cstmUid = chatRoom.getCstmUid();
			
			if (!Strings.isNullOrEmpty(cstmUid)) {
				param.put("cstmUid", cstmUid);
				// param.put("cstmCocId", cstmCocId);
			} else {
				param.put("roomCocId", roomCocId);
			}
			
			log.info("sukee chatRoom > : {}", chatRoom);
			log.info("sukee param > : {}", param);
			List<Map<String, Object>> chatHisList = chatManageService.selectCnsHisList(param);
			model.put("chatHisList", chatHisList);

			// 고객 정보
			Map<String, Object> customer = customerService.selectCustomerByCstmUid(cstmUid);
			customer.put("room_coc_id", chatRoom.getRoomCocId());
			customer.put("room_coc_nm", chatRoom.getRoomCocNm());
			model.put("customer", customer);

			// 템플릿 카테고리 목록 조회
			List<Map<String, Object>> categoryList = templateService.selectTplCategoryList(param);
			model.put("categoryList", categoryList);

			if (categoryList != null && categoryList.size() > 0) {
				String schTplCtgNum = StringUtil.nvl((categoryList.get(0).get("tpl_ctg_num")));
				param.put("schTplCtgNum", schTplCtgNum);
				model.put("schTplCtgNum", schTplCtgNum);
			}

			//자동완성 목록
			Map<String, Object> autoCmpParams = new HashMap<>();
			autoCmpParams.put("memberUid", memberVO.getMemberUid());
			autoCmpParams.put("departCd", memberVO.getDepartCd());
			autoCmpParams.put("autoCmpDiv", "C");
			List<Map<String, Object>> selectAutoCmpList = autoCmpService.selectAutoCmpListForCounselor(autoCmpParams);
			model.put("autoCmpList", selectAutoCmpList);
			model.put("schAutoCmpDiv", param.get("schAutoCmpDiv"));

			// 조회 조건 초기화
			param.put("schDeviceType", "WEB");
			model.put("schDeviceType", "WEB");

			// 템플릿 목록 조회
			// 상담하기에서는 관리자도 상담직원과 동일하게 목록 표시
			param.put("tplDivCd", CommonConstants.TPL_DIV_CD_P);
			List<Map<String, Object>> templateList = templateService.selectTemplateList(param);
			log.debug("templateList: {}", templateList);
			model.put("templateList", templateList);

			model.put("templateEditYn", "N");

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "chatCsnr/cnsHisList";
	}

	/**
	 * 템플릿 조회
	 */
	@RequestMapping(value = "/chatCsnr/selectTemplateOne", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectTemplateOne(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("departCd", memberVO.getDepartCd());

			Map<String, Object> template = templateService.selectTemplate(param);
			String org_img_path = (String) template.get("org_img_path");
			String cns_frt_msg_img ="";
			if (org_img_path != null) {
				cns_frt_msg_img = org_img_path.replaceAll(customProperty.getStoragePath(), customProperty.getUploadUrlBase());
				template.put("cns_frt_msg_img", cns_frt_msg_img);
			}

			rtnMap.put("template", template);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return rtnMap;
	}

	/**
	 * 템플릿 저장
	 */
	@RequestMapping(value = "/chatCsnr/saveTemplate", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> saveTemplate(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param,
			@RequestParam MultiValueMap<String, String> lparam,
			@RequestParam("tplImgUrl") MultipartFile file, @RequestParam("tplPdfUrl") MultipartFile file2) {

		log.debug("CounselorController: Params: {}", param);
		log.debug("CounselorController: MultiPartParams: {}", lparam);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("departCd", memberVO.getDepartCd());

			templateService.saveTemplate(param, lparam, file, file2);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			//rtnMap.put("rtnMsg", "정상적으로 저장 되었습니다.");
			rtnMap.put("rtnMsg", "등록이 요청되었습니다. 승인 후 사용할 수 있습니다.");
		} catch (BizException be) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_FAILURE);
			rtnMap.put("rtnMsg", be.getMessage());
			//be.printStackTrace();
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 템플릿 삭제
	 */
	@RequestMapping(value = "/chatCsnr/deleteTemplate", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteTemplate(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			//			MemberVO memberVO = MemberAuthService.getCurrentUser();

			templateService.deleteTemplate(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 삭제 되었습니다.");
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 상담이력 템플릿 목록
	 */
	@RequestMapping(value = "/chatCsnr/selectTemplateAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectTemplateAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectTemplateAjax > : {}", param);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());
			param.put("departCd", memberVO.getDepartCd());

			// 템플릿 목록 조회
			param.put("tplDivCd", CommonConstants.TPL_DIV_CD_P);
			List<Map<String, Object>> templateList = templateService.selectTemplateList(param);
			model.put("templateList", templateList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "chatCsnr/cnsTplListAjax";
	}

	/**
	 * 상담직원 - 템플릿 관리 페이지 - 목록
	 */
	@RequestMapping(value = "/chatCsnr/selectCnsrTplPageAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCnsrTplPageAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {

			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());
			param.put("departCd", memberVO.getDepartCd());

			// 템플릿 카테고리 목록 조회
			List<Map<String, Object>> categoryList = templateService.selectTplCategoryList(param);
			model.put("categoryList", categoryList);

			if (categoryList != null && categoryList.size() > 0) {
				String schTplCtgNum = StringUtil.nvl((categoryList.get(0).get("tpl_ctg_num")));
				param.put("schTplCtgNum", schTplCtgNum);
				model.put("schTplCtgNum", schTplCtgNum);
			}

			// 템플릿 메세지 구분 코드 목록 조회
			List<Map<String, Object>> tplMsgDivCdList = commonService
					.selectCodeList(CommonConstants.COMM_CD_TPL_MSG_DIV_CD);
			model.put("tplMsgDivCdList", tplMsgDivCdList);

			// 조회 조건 초기화
			param.put("schDeviceType", "WEB");
			model.put("schDeviceType", "WEB");

			// 템플릿 목록 조회
			param.put("tplDivCd", CommonConstants.TPL_DIV_CD_P);

			List<Map<String, Object>> templateList = templateService.selectTemplateList(param);
			model.put("templateList", templateList);
			log.debug("templateList: {}", templateList);

			model.put("templateEditYn", "Y");
			model.put("tplDivCd", CommonConstants.TPL_DIV_CD_P);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "chatCsnr/cnsrTplPage";
	}
/**
 * 고객정보 가져오기
 * @param model
 * @param entityId
 */
	
	private void getMCACustomerInfo(ModelMap model, String entityId) {
		log.info("CUSTOMER INFO entityid: {}", entityId);

		String[] boldText = new String[] { "신투정" };
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		try {

			// 고객기본정보
			Map<String, Object> customerSinfo = mcaService.sgd1611p(entityId, CommonConstants.MCA_CHANNEL_CODE);
			// 고객 유형 정보
			Map<String, Object> custTypeInfo = new HashMap<String, Object>();
			Map<String, Object> custContInfo = new HashMap<>();
			// 계좌 정보
			ArrayList<Map<String, Object>> custAccntList = new ArrayList<Map<String, Object>>();
			// 접촉정보
			ArrayList<Map<String, Object>> callHistList = new ArrayList<>();

			if (customerSinfo.size() == 0 || (boolean) customerSinfo.get("errFlag")) {
				model.put("customerSinfo", customerSinfo);
				model.put("custTypeInfo", custTypeInfo);
				model.put("custContInfo", custContInfo);
				model.put("custAccntCnt", custAccntList.size());
				model.put("custAccntList", custAccntList);
				model.put("callHistList", callHistList);
				return;
			}
			String age = "" + customerSinfo.get("AGE");
			if (!StringUtil.isEmpty(age)) {
				age = Integer.parseInt(age) + "세";
				customerSinfo.put("AGE", age);
			}
			customerSinfo.put("ROOM_ENTITY_ID", entityId);
			model.put("customerSinfo", customerSinfo);

			// 고객유형정보
			custTypeInfo.putAll(mcaService.mah0368p(entityId, CommonConstants.MCA_CHANNEL_CODE));
			custTypeInfo.putAll(mcaService.sdb1624p(entityId, CommonConstants.MCA_CHANNEL_CODE));
			custTypeInfo.putAll(mcaService.mah0363p(entityId, CommonConstants.MCA_CHANNEL_CODE));

			// 고객 투자정보 유형이 신투정이 아닌 경우 배경색상을 가짐
			String clintPopnCtnt = "" + custTypeInfo.get("CLNT_POPN_CTNT");

			custTypeInfo.put("BG_COLOR", Arrays.asList(boldText).contains(clintPopnCtnt) ? "N" : "Y");

			model.put("custTypeInfo", custTypeInfo);

			// 계좌정보

			// 계좌리스트
			Map<String, Object> accInfo = mcaService.sgd1616p(entityId, CommonConstants.MCA_CHANNEL_CODE);
			ArrayList<Map<String, Object>> accntList = (ArrayList<Map<String, Object>>) accInfo.get("outRec2");

			for (int idx = 0; idx < accntList.size(); idx++) {
				Map<String, Object> accntInfo = accntList.get(idx);
				String acntNo = String.valueOf(accntInfo.get("ACNT_NO"));
				accntInfo.putAll(mcaService.maa0421p(entityId, CommonConstants.MCA_CHANNEL_CODE, acntNo));

				custAccntList.add(accntInfo);
				if (idx == 0) {
					Map<String, Object> mag0081p = mcaService.mag0081p(entityId, acntNo);
					custContInfo = (Map<String, Object>) mag0081p.get("outRec1");
					List<Map<String, Object>> list = (List<Map<String, Object>>) mag0081p.get("outRec2");

					String hts_yn = "X";
					for (Map<String, Object> item : list) {
						if ("Y".equals("" + item.get("A_HTS_CTRT_YN"))) {
							hts_yn = "O";
							break;
						}
					}
					custContInfo.put("A_HTS_CTRT_YN", hts_yn);
					model.put("custContInfo", custContInfo);
				}
			}
			model.put("custAccntCnt", custAccntList.size());
			model.put("custAccntList", custAccntList);

			// 접촉정보
			callHistList = mcaService.sgd0027p(entityId, CommonConstants.MCA_CHANNEL_CODE);
			model.put("callHistList", callHistList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 기간계 고객 조회
	 */
	@PostMapping(value = "/chatCsnr/selectCustomerInfoAjax")
	public String selectCustomerInfoAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
									 HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
									 @RequestParam("chatRoomUid") @NotEmpty String chatRoomUid,
									 @RequestParam(value = "refresh", required = false, defaultValue = "false") @NotEmpty Boolean refresh
									 ,@RequestParam Map<String, Object> param) {

		log.info("selectCustomerInfoAjax: {}, refresh: {}", chatRoomUid, refresh);

		try {
			
			ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
			String entityId = chatRoom.getCstmCocId();
			if (refresh) {
				// 계약정보 목록 조회 -> CERT LIST 빌드
//				List<Map<String, Object>> custInfoList = mappingCustomerService.selectMappingCustomerList(chatRoomUid);
//				List<Map<String, Object>> certList = legacyService.getCertListFromMappingCustomerList(custInfoList);
//				if (!certList.isEmpty()) {
//					Map<String, Object> legacyCustomerInfo = legacyService.getCustomerInfo(chatRoom, certList);
//					log.info("LEGACY CUSTOMER INFO: {}", legacyCustomerInfo);
//				}
			}
			
			model.put("chatRoomUid", chatRoomUid);
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());
			param.put("departCd", memberVO.getDepartCd());
			model.put("departCd", memberVO.getDepartCd());
			
			Map<String, Object> member = new HashMap<>();
			member.put("id", memberVO.getId());
			
			//TODO Honors-net 계정 skip 
			int skipUser = userService.isSkipUserYn(member);
			if(skipUser < 1) {
				Map<String, Object> mMap = new HashMap<>();
				mMap.put("id", memberVO.getId());
				mMap.put("memberUid", memberVO.getMemberUid());
				
				int intHoners = userService.isHonorsnetYn(mMap, memberVO.getId());
				if(intHoners == 2) {
					request.setAttribute("errMsg", "아너스넷 비밀번호가 일치하지 않습니다.");
				}
			}
			
			getMCACustomerInfo(model, entityId);
			
			// 템플릿 카테고리 목록 조회
			List<Map<String, Object>> categoryList = templateService.selectTplCategoryList(param);
			model.put("categoryList", categoryList);

			if (categoryList != null && categoryList.size() > 0) {
				String schTplCtgNum = StringUtil.nvl((categoryList.get(0).get("tpl_ctg_num")));
				param.put("schTplCtgNum", schTplCtgNum);
				model.put("schTplCtgNum", schTplCtgNum);
			}

			// 자동완성 목록
			Map<String, Object> autoCmpParams = new HashMap<>();
			autoCmpParams.put("memberUid", memberVO.getMemberUid());
			autoCmpParams.put("departCd", memberVO.getDepartCd());
			autoCmpParams.put("autoCmpDiv", "C");
			List<Map<String, Object>> selectAutoCmpList = autoCmpService.selectAutoCmpListForCounselor(autoCmpParams);
			model.put("autoCmpList", selectAutoCmpList);
			model.put("schAutoCmpDiv", param.get("schAutoCmpDiv"));

			// 조회 조건 초기화
			param.put("schDeviceType", "WEB");
			model.put("schDeviceType", "WEB");

			// 템플릿 목록 조회
			// 상담하기에서는 관리자도 상담직원과 동일하게 목록 표시
			param.put("tplDivCd", CommonConstants.TPL_DIV_CD_P);
			List<Map<String, Object>> templateList = templateService.selectTemplateList(param);
			log.debug("templateList: {}", templateList);
			model.put("templateList", templateList);

			model.put("templateEditYn", "N");
			
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "chatCsnr/customerInfoAjax" ;
	}
	/**
	 * 기간계 고객 조회 팝업용
	 */
	@RequestMapping(value = "/chatCsnr/selectCustomerInfoAjaxManager", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCustomerInfoAjaxManager(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
									 HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
									 @RequestParam("chatRoomUid") @NotEmpty String chatRoomUid
									 , @RequestParam(value = "refresh", required = false, defaultValue = "false") @NotEmpty Boolean refresh) {

		log.info("selectCustomerInfoAjaxManager: {}, refresh: {}", chatRoomUid, refresh);

		try {
			ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
			String entityId = chatRoom.getCstmCocId();
			
			if (refresh) {
				// 계약정보 목록 조회 -> CERT LIST 빌드
//				List<Map<String, Object>> custInfoList = mappingCustomerService.selectMappingCustomerList(chatRoomUid);
//				List<Map<String, Object>> certList = legacyService.getCertListFromMappingCustomerList(custInfoList);
//				if (!certList.isEmpty()) {
//					Map<String, Object> legacyCustomerInfo = legacyService.getCustomerInfo(chatRoom, certList);
//					log.info("LEGACY CUSTOMER INFO: {}", legacyCustomerInfo);
//				}
			}
			model.put("chatRoomUid", chatRoomUid);
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("departCd", memberVO.getDepartCd());			

			getMCACustomerInfo(model, entityId);
			
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "chatCsnr/customerInfoAjaxManager";
	}
	/**
	 * 지식화 목록
	 */
	@RequestMapping(value = "/chatCsnr/selectKnowAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectKnowAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			param.put("nowPage", nowPage);
			param.put("pageCount", CommonConstants.PAGE_KNOW_LIST_COUNT);
			model.put("nowPage", nowPage);

			// 프로젝트 목록
			List<Map<String, Object>> projectList = chatManageService.selectProjectList(param);
			model.put("projectList", projectList);

			// 지식화 목록
			List<Map<String, Object>> knowList = chatManageService.selectKnowList(param);
			model.put("knowList", knowList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "chatCsnr/knowList";
	}

	/**
	 * 지식화 목록
	 */
	@RequestMapping(value = "/chatCsnr/selectKnowListAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectKnowListAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			param.put("nowPage", nowPage);
			param.put("pageCount", CommonConstants.PAGE_KNOW_LIST_COUNT);
			model.put("nowPage", nowPage);

			// 지식화 목록
			List<Map<String, Object>> knowList = chatManageService.selectKnowList(param);
			model.put("knowList", knowList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "chatCsnr/knowListAjax";
	}

	/**
	 * 지식화 조회
	 */
	@RequestMapping(value = "/chatCsnr/selectKnowledgeAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectKnowledgeAjax(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());

			// 지식화 목록
			Map<String, Object> knowledge = chatManageService.selectKnowledge(param);
			rtnMap.put("knowledge", knowledge);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return rtnMap;
	}

	/**
	 * 지식화 관리 등록
	 */
	@RequestMapping(value = "/chatCsnr/saveKnowledge", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> saveKnowledge(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			// 지식화 관리 등록
			chatManageService.saveKnowMgt(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 저장 되었습니다.");
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 지식화 관리 삭제
	 */
	@RequestMapping(value = "/chatCsnr/deleteKnowledge", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteKnowledge(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			// 지식화 관리 등록
			chatManageService.deleteKnowMgt(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 삭제 되었습니다.");
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 공지사항 목록
	 */
	@RequestMapping(value = "/chatCsnr/selectNoticeListAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectNoticeListAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("CounselorController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("memberDivCd", memberVO.getMemberDivCd());
			param.put("upperMemberUid", memberVO.getUpperMemberUid());
			param.put("departCd", memberVO.getDepartCd());
			param.put("nowPage", 1);
			param.put("pageListCount", 3);

			// 공지사항 목록 조회
			List<Map<String, Object>> noticeList = noticeService.selectNoticeList(param);
			model.put("noticeList", noticeList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "chatCsnr/noticeList";
	}

	/**
	 * 유효시간내 인증한 사용자인지 판별
	 */
	@GetMapping("/api/chatCsnr/isCertificated")
	@ResponseBody
	public ResponseEntity<String> isCertificated(
			@RequestParam("chatRoomUid") @NotEmpty String chatRoomUid) throws Exception {

//		if (htUtils.isActiveProfile("local")) {
//			return new ResponseEntity<>("", HttpStatus.OK);
//		} else {
			if (authService.selectAuth(chatRoomUid) != null) {
				return new ResponseEntity<>("", HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
//		}
	}
}
