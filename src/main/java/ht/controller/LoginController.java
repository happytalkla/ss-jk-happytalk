package ht.controller;

import static ht.constants.CommonConstants.LOG_DIV_CD_A;
import static ht.constants.CommonConstants.LOG_DIV_CD_C;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_A;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_C;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_M;
import static ht.constants.CommonConstants.MEMBER_DIV_CD_S;
import static ht.constants.CommonConstants.RESULT_CD_ERROR;
import static ht.constants.CommonConstants.RESULT_CD_FAILURE;
import static ht.constants.CommonConstants.RESULT_CD_LOGIN_DEFAULT_PASSWD;
import static ht.constants.CommonConstants.RESULT_CD_LOGIN_USED_PASSWD;
import static ht.constants.CommonConstants.RESULT_CD_LOGIN_WRONG_CURRENT_PASSWD;
import static ht.constants.CommonConstants.RESULT_CD_SUCCESS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.IpManage;
import ht.domain.MemberVO;
import ht.persistence.MemberDao;
//import ht.security.SsoConfig;
import ht.service.CommonService;
import ht.service.McaService;
import ht.service.MemberAuthService;
import ht.service.ScpAgentService;
import ht.service.UserService;
import ht.service.chatbot.HappyBotService;
import ht.util.HTUtils;
import ht.util.StringUtil;
//import kbli.seed.service.Decrypt;
//import kbli.seed.service.Encrypt;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 셋팅 관련 controller
 * 공지사항 관리
 *
 * @author wizard
 *
 */
@Controller
@Slf4j
public class LoginController {

	//	@Resource
	//	private SsoConfig ssoConfig;
	@Resource
	private CommonService commonService;
	@Resource
	private HTUtils htUtils;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private UserService userService;
	@Resource
	private HappyBotService happyBotService;
	@Resource
	private McaService mcaService;

	@Resource
	private ScpAgentService scpagentService;


	@Resource
	private MemberDao memberDao;
	/**
	 * 홈 페이지
	 */
	@RequestMapping(value = {"/"}, method = { RequestMethod.GET })
	public String homePage(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) throws Exception {

		log.info("LOGIN PAGE: {}, JSESSIONID: {}, SESSION ID: {}", request.getRequestURI(), jSessionId, session.getId());

		return "redirect:/loginPage";
	}

	/**
	 * 로그인 페이지
	 */
	@RequestMapping(value = {"/loginPage"}, method = { RequestMethod.GET })
	public String loginPage(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) throws Exception {

		log.info("LOGIN PAGE: {}, JSESSIONID: {}, SESSION ID: {}", request.getRequestURI(), jSessionId, session.getId());

		// 관리하는 IP와 일치하는게 없으면 에러페이지로 이동시킨다. 차후 ip나오면 적용해야 함.
		// if(!IpCheck()) return "error/error";

		return "loginPage";
	}

	/**
	 * 접속한 IP가 관리하는 IP가 맞는지 체크한다.
	 * @return
	 * @throws UnknownHostException
	 */
	private boolean IpCheck() throws UnknownHostException {
		boolean check = false;

		InetAddress ip = null;
		ip = InetAddress.getLocalHost();
		log.info("ip : " + ip.getHostAddress());

		IpManage im = new IpManage();
		String[] ipList = im.getIpList().split(",");

		for(int i=0; i < ipList.length; i++) {
			if(ipList[i].equals(ip.getHostAddress())){
				check = true;
			}
		}

		return check;
	}

	//	/**
	//	 * 로그인 페이지로 이동
	//	 * SSO V3 체크
	//	 *
	//	 * @param jSessionId
	//	 * @param request
	//	 * @param response
	//	 * @param session
	//	 * @param model
	//	 * @param param
	//	 * @return
	//	 * @throws Exception
	//	 */
	//	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	//	public String login(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
	//			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
	//			@RequestParam Map<String, Object> param) throws Exception {
	//
	//		log.debug("MainController > : {}", request.getRequestURI());
	//		log.debug("session : {}", session.getId());
	//
	//		/**
	//		 * sso 관련 처리
	//		 * 사번을 받아서 로그인 페이지로 이동 후 로그인 처리
	//		 */
	//		boolean loginCheck = true;
	//		String ssoId = null;
	//		try {
	//			ssoId = ssoConfig.getSsoId(request);
	//			log.debug("ssoId : {}", ssoId);
	//
	//			if (ssoId == null || ssoId.equals("")) {
	//				log.debug("[ssoId is null]");
	//
	//				loginCheck = false;
	//			}
	//
	//			// 2. SSO 인증토큰의 유효성 검증 (정상 : 0)
	//			String retCode = ssoConfig.readNexessCookie(request, response);
	//			log.debug("[SSO Login] retCode = " + retCode);
	//
	//			// SSO 인증토큰이 유효하지 않은 경우 에러페이지로 Redirect
	//			if (!retCode.equals("0")) {
	//				loginCheck = false;
	//			}
	//		} catch (Exception e) {
	//			log.error("{}", e.getLocalizedMessage(), e);
	//			loginCheck = false;
	//		}
	//
	//		if (!loginCheck) {
	//			ssoConfig.goLoginPage(response);
	//			return null;
	//		} else {
	//			model.put("username", ssoId);
	//			return "login";
	//		}
	//	}

	//	/**
	//	 * bridge 페이지로 이동
	//	 * SSO V2 체크
	//	 *
	//	 * @param jSessionId
	//	 * @param request
	//	 * @param response
	//	 * @param session
	//	 * @param model
	//	 * @param param
	//	 * @return
	//	 * @throws Exception
	//	 */
	//	@RequestMapping(value = "/loginV2", method = { RequestMethod.GET, RequestMethod.POST })
	//	public String loginV2(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
	//			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
	//			@RequestParam Map<String, Object> param) throws Exception {
	//
	//		log.debug("MainController > : {}", request.getRequestURI());
	//		log.debug("session : {}", session.getId());
	//
	//		model.put("loginUrl", customProperty.getLoginUrl());
	//
	//		// ssoMap 관련 정보
	//
	//		// 서버시간 체크
	//		Date date = new Date();
	//		long serverTime = date.getTime() / 1000;
	//		String serverTimeString = new Long(serverTime).toString();
	//
	//		// 서버타임 암호화
	//		String encryptedServerTime = null;
	//		try {
	//			encryptedServerTime = Encrypt.encryptSSOTokenServerTime(serverTimeString);
	//		} catch (Exception e) {
	//			log.error("{}", e.getLocalizedMessage(), e);
	//			model.put("loginUrl", customProperty.getLoginUrl());
	//			model.put("rtnMsg", "SSO 로그인 서버 오류 입니다.");
	//			return "error/loginError";
	//		}
	//
	//		model.put("encryptedServerTime", encryptedServerTime);
	//
	//		return "loginV2";
	//	}
	//
	//	/**
	//	 * 로그인 페이지로 이동
	//	 * SSO V2 체크
	//	 *
	//	 * @param jSessionId
	//	 * @param request
	//	 * @param response
	//	 * @param session
	//	 * @param model
	//	 * @param param
	//	 * @return
	//	 * @throws Exception
	//	 */
	//	@RequestMapping(value = "/loginV2Next", method = { RequestMethod.GET, RequestMethod.POST })
	//	public String loginV2Next(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
	//			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
	//			@RequestParam Map<String, Object> param) throws Exception {
	//
	//		log.debug("MainController > : {}", request.getRequestURI());
	//		log.debug("session : {}", session.getId());
	//
	//		// ezMap 관련 정보
	//
	//		String eztoken = request.getParameter("eztoken");
	//		String eztokentime = request.getParameter("eztokentime");
	//		// String enc_param = request.getParameter("enc_param");
	//
	//		eztoken = StringUtil.nvl(eztoken);
	//		eztokentime = StringUtil.nvl(eztokentime);
	//		// enc_param = StringUtil.nvl(WebUtil.checkWeb(enc_param));
	//
	//		if (eztoken == null) {
	//			eztoken = "";
	//		}
	//
	//		if (eztokentime == null) {
	//			eztokentime = "";
	//		}
	//
	//		// if( enc_param != null && !"".equals(enc_param) ) {
	//		// enc_param = StringUtil.filteringXSS(enc_param);
	//		// } else {
	//		// enc_param = "";
	//		// }
	//
	//		model.put("loginUrl", customProperty.getLoginUrl());
	//
	//		if ("".equals(eztoken) || "".equals(eztokentime)) {
	//			log.debug("sso login parameter : eztoken : {}", eztoken);
	//			log.debug("sso login parameter : eztokentime : {}", eztokentime);
	//			model.put("rtnMsg", "SSO 로그인 ID가 존재하지 않습니다.");
	//			return "error/loginError";
	//		}
	//
	//		Map<String, Object> ezMap = null;
	//		try {
	//			ezMap = Decrypt.decryptEZToken(eztoken, eztokentime);
	//		} catch (Exception e) {
	//			log.error("{}", e.getLocalizedMessage(), e);
	//			log.debug("sso login ezMap not found");
	//			model.put("rtnMsg", "SSO 로그인 ID가 존재하지 않습니다.");
	//			return "error/loginError";
	//		}
	//
	//		if (ezMap == null || ezMap.get("ID") == null) {
	//			log.debug("sso login ezMap not found");
	//			model.put("rtnMsg", "SSO 로그인 ID가 존재하지 않습니다.");
	//			return "error/loginError";
	//		}
	//
	//		Long ezLocalTime = Long.parseLong((String) ezMap.get("TIME"));
	//		String ezId = StringUtil.nvl(ezMap.get("ID"));
	//
	//		if ("".equals(ezId)) {
	//			log.debug("sso login ezMap ID not found");
	//			model.put("rtnMsg", "SSO 로그인 ID가 존재하지 않습니다.");
	//			return "error/loginError";
	//		}
	//
	//		// ssoMap 관련 정보
	//		String ssotoken = StringUtil.nvl(param.get("ssotoken"));
	//		String ssotokentime = StringUtil.nvl(param.get("encryptedServerTime"));
	//
	//		Map<String, Object> ssoMap = null;
	//		try {
	//			ssoMap = Decrypt.decryptSSOToken(ssotoken, ssotokentime);
	//		} catch (Exception e) {
	//			log.error("{}", e.getLocalizedMessage(), e);
	//			log.debug("sso login ezMap not found");
	//			model.put("rtnMsg", "SSO 로그인 ID가 존재하지 않습니다.");
	//			return "error/loginError";
	//		}
	//
	//		if (ssoMap == null || ssoMap.get("ID") == null) {
	//			log.debug("sso login ssoMap not found");
	//			model.put("rtnMsg", "SSO 로그인 ID가 존재하지 않습니다.");
	//			return "error/loginError";
	//		}
	//
	//		Long ssoLocalTime = Long.parseLong((String) ssoMap.get("TIME"));
	//		String ssoId = StringUtil.nvl(ssoMap.get("ID"));
	//
	//		if ("".equals(ssoId)) {
	//			log.debug("sso login ssoMap ID not found");
	//			model.put("rtnMsg", "SSO 로그인 ID가 존재하지 않습니다.");
	//			return "error/loginError";
	//		}
	//
	//		if (!ssoId.equals(ezId)) {
	//			log.debug("sso login ID not equal");
	//			model.put("rtnMsg", "SSO 로그인 ID 인증에 실패하였습니다.");
	//			return "error/loginError";
	//		}
	//
	//		model.put("username", ssoId);
	//		return "login";
	//	}

	/**
	 * 계정 활성화 체크
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/api/login/selectValidCheck")
	public @ResponseBody Map<String, Object> selectValidCheck(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param){
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			rtnMap = userService.selectValidCheck(param);

			if (rtnMap.containsKey("rtnCd") && RESULT_CD_LOGIN_DEFAULT_PASSWD.equals(rtnMap.get("rtnCd"))) {
				// 동일 패스워드일시 변경페이지 접근을 위하여 세션 추가
				String validUid = String.valueOf(UUID.randomUUID());
				session.setAttribute("validUid", validUid);
				rtnMap.put("validUid", validUid);
			}

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
			// 로그 정보 등록
			//			String logCont = "계정 활성화 체크: " + StringUtil.nvl(param.get("username")) ;
			//			commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, "");
		}
		return rtnMap;
	}

	/**
	 * 로그인 실패 6회이상 체크
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/api/login/selectFailCheck")
	public @ResponseBody Map<String, Object> selectFailCheck(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param){
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			int cnt = userService.selectFailCheck(param);
			if (cnt < 6) {
				rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			}else {
				rtnMap.put("rtnCd", RESULT_CD_ERROR);
				rtnMap.put("rtnMsg", "해당 아이디는 로그인 실패 6회 이상으로 계정 중지 상태입니다. 관리자에게 문의해 주시기 바랍니다.");
			}
			rtnMap.put("cnt", cnt);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
			// 로그 정보 등록
			//			String logCont = "로그인 실패여부 6회이상 체크: " + StringUtil.nvl(param.get("username")) ;
			//			commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, "");
		}
		return rtnMap;
	}


	/**
	 * id password 체크 및 아너스넷 비밀번호 체크
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/api/login/selectIdPwdCheck")
	public @ResponseBody Map<String, Object> selectIdPwdCheck(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param){
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {

			int cnt = userService.selectIdPwdCheck(param);
			if (cnt == 1) {
				rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			}else if(cnt == 2){
				rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
				rtnMap.put("rtnMsg", "아너스넷 비밀번호가 일치하지 않습니다.");
				String validUid = String.valueOf(UUID.randomUUID());
				rtnMap.put("validUid", validUid);
				session.setAttribute("validUid", validUid);
			}else {
				rtnMap.put("rtnCd", RESULT_CD_ERROR);
				rtnMap.put("rtnMsg", "로그인 정보가 잘못되었습니다.");
			}
			rtnMap.put("cnt", cnt);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
			// 로그 정보 등록
			//			String logCont = "아이디 비밀번호 체크: " + StringUtil.nvl(param.get("username")) ;
			//			commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, "");
		}
		return rtnMap;
	}

	/**
	 * 비밀번호 90일 이상 체크
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/api/login/passwd90Check", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> passwd90Check(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param){
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int cnt=0;
		try {
			rtnMap = userService.passwd90Check(param);
			cnt = (int) rtnMap.get("cnt");
			if (cnt == 1) {
				rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			}else {
				String validUid = String.valueOf(UUID.randomUUID());
				session.setAttribute("validUid", validUid);
				rtnMap.put("validUid", validUid);
				rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
				rtnMap.put("rtnMsg", "비밀번호 변경일자기준 90일이 경과하여 비밀번호를 변경하여 주십시오.");
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
			// 로그 정보 등록
//			String logCont = "패스워드 변경일자 90일이상: "
//					+ StringUtil.nvl(param.get("username")) + " ("
//					+ StringUtil.nvl(param.get("password")) + ")";
			//			commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, "");
		}
		return rtnMap;
	}

	/**
	 * 기존 비밀번호 체크
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/api/login/selectOldPwdCheck")
	public @ResponseBody Map<String, Object> selectOldPwdCheck(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param){
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			int cnt = userService.selectOldPwdCheck(param);
			if (cnt == 1) {
				rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			}else {
				rtnMap.put("rtnCd", RESULT_CD_ERROR);
				rtnMap.put("rtnMsg", "로그인 정보가 잘못되었습니다.");
				//				rtnMap.put("rtnMsg", "기존 비밀번호를 잘못 입력하였습니다.");
			}
			rtnMap.put("cnt", cnt);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
			// 로그 정보 등록
			//			String logCont = "기존 비밀번호 체크: " + StringUtil.nvl(param.get("username")) ;
			//			commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, "");
		}
		return rtnMap;
	}


	/**
	 * 패스워드 3회 중복체크
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/api/login/passwd3DuplCheck", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> passwd3DuplCheck(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param){
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			int cnt = userService.passwd3DuplCheck(param);
			rtnMap.put("cnt", cnt);

			if (cnt==0) {
				rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			}else {
				rtnMap.put("rtnCd", RESULT_CD_ERROR);
				rtnMap.put("rtnMsg", "3회 중복패스워드 입니다. 다른 패스워드를 사용해 주세요.");
			}

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
			// 로그 정보 등록
			//			String logCont = "패스워드 3회 중복체크: " + StringUtil.nvl(param.get("name")) + " (" + StringUtil.nvl(param.get("id"))
			//			+ ")";
			//			commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, "");
		}
		return rtnMap;
	}

	/**
	 * 접근PC IP체크
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/api/login/accessIPCheck", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> accessIPCheck(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			@RequestHeader HttpHeaders headers, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param){
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			String ipAddr = htUtils.getRemoteIpAddress(headers, request);
			param.put("linkIp", ipAddr);
			int cnt = userService.accessIPCheck(param);
			rtnMap.put("cnt", cnt);

			if (cnt==0) {
				rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			}else {
				rtnMap.put("rtnCd", RESULT_CD_ERROR);
				rtnMap.put("rtnMsg", "접속이 허용되지 않은 IP입니다.");
			}

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
			// 로그 정보 등록
			//			String logCont = "패스워드 3회 중복체크: " + StringUtil.nvl(param.get("name")) + " (" + StringUtil.nvl(param.get("id"))
			//			+ ")";
			//			commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, "");
		}
		return rtnMap;
	}
	

	/**
	 * 로그인 성공 후 페이지 이동
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param auth
	 * @param model
	 * @param param
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/login/success", method = { RequestMethod.GET, RequestMethod.POST })
	public String main(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			@RequestHeader HttpHeaders headers, HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Authentication auth, ModelMap model, @RequestParam Map<String, Object> param)
					throws IOException {

		log.debug("MainController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		param.put("memberUid", memberVO.getMemberUid());

		String ipAddr = htUtils.getRemoteIpAddress(headers, request);
		param.put("linkIp", ipAddr);

		userService.updateLoginDate(param);
		log.info("login success : {}" , memberVO);
		log.debug("getMemberUid : {}", memberVO.getMemberUid());
		log.debug("getMemberDivCd : {}", memberVO.getMemberDivCd());
		log.debug("getName : {}", memberVO.getName());

		// 로그 정보 등록 - 로그인
		String loginIp = htUtils.getRemoteIpAddress(headers, request);
		String logCont = memberVO.getName() + " (" + memberVO.getId() + ") 로그인, IP : " + loginIp;
		commonService.insertLog(LOG_DIV_CD_A, "로그인", logCont, memberVO.getMemberUid());

		String memberDivCd = memberVO.getMemberDivCd();
		if (MEMBER_DIV_CD_S.equals(memberDivCd)
				|| MEMBER_DIV_CD_A.equals(memberDivCd)) {
			session.setAttribute("topMenu", "serviceSet");
			response.sendRedirect(request.getContextPath() + "/set/selectSet");
		} else if (MEMBER_DIV_CD_M.equals(memberDivCd)) {
			session.setAttribute("topMenu", "counselManage");
			response.sendRedirect(request.getContextPath() + "/report/managerMain");
		} else if (MEMBER_DIV_CD_C.equals(memberDivCd)) {
			session.setAttribute("topMenu", "counselling");
			response.sendRedirect(request.getContextPath() + "/counselor");
		} else {
			response.sendRedirect(request.getContextPath() + "/logout");
		}

		return null;
	}

	/**
	 * 로그인 에러
	 * sso에서 설정한 통합 로그인으로 이동
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/error", method = { RequestMethod.GET })
	public String loginError(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) throws Exception {

		log.debug("MainController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		model.put("loginUrl", customProperty.getLoginUrl());

		return "error/loginError";
	}

	/**
	 * 비밀번호 변경페이지
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changePasswdView", method = { RequestMethod.GET })
	public String changePasswdView(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) throws Exception {

		if (session.getAttribute("validUid") != null && !"".equals(session.getAttribute("validUid"))) {
			// 세션정보가 있을 경우
			String validUid = String.valueOf(session.getAttribute("validUid"));
			// 세션 정보 초기화
			session.setAttribute("validUid", "");
			// 실제 세션정보의 데이터와 parameter 정보가 일치 하는지 확인
			if (!validUid.equals(param.get("validUid"))) return "error/session";
		} else {
			// 세션정보가 없을 경우 페이지 오류
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			if (memberVO == null || memberVO.getMemberUid() == null) {
				return "error/session";
			} else {
				if (!String.valueOf(param.get("memberUid")).equals(memberVO.getMemberUid())) {
					// param 데이터와 실제 로그인한 정보가 다를경우
					return "error/session";
				}
			}
		}

		log.debug("MainController > : {}", request.getRequestURI());


		model.put("memberUid", param.get("memberUid"));
		model.put("cocId", 	   param.get("cocId"));
		model.put("id", 	   param.get("id"));
		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("id", param.get("id"));
		Map<String, Object> member = memberDao.selectCMember(sqlParams);
		model.put("nonHnet", StringUtil.nvl(member.get("MEMBER_GROUP_DIV_CD"), "N"));
		log.info("**************************************************getNHnet(): {}", member.get("MEMBER_GROUP_DIV_CD"));
		return "changePasswd";
	}

	/**
	 * 비밀번호 변경 (로그인 안된상태)
	 */
	@RequestMapping(value = "/api/login/loginChangePasswd", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> loginChangePasswd
	(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) throws Exception {

		log.info("CHANGE PASSWORD: {}", param);

		model.put("pwd", param.get("newPassword"));
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		try {
			// 서버단에서 패스워드 validation을 한번 더 체크한다.
			String pwdCheck = pwdCheck(param);

			if(!"".equals(pwdCheck)) {
				rtnMap.put("rtnCd", RESULT_CD_FAILURE);
				rtnMap.put("rtnMsg", pwdCheck);
				return rtnMap;
			}

			String rtnCd = userService.changePasswd(param, false, false);

			if (RESULT_CD_SUCCESS.equals(rtnCd)) {
				rtnMap.put("rtnCd", rtnCd);
				rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");
				// 로그 정보 등록
				String id = StringUtil.nvl(param.get("memberUid") != null ? param.get("memberUid") : param.get("cocId"));
				String logCont = "비밀번호 변경: " + id;
				commonService.insertLog(LOG_DIV_CD_C, "성공", logCont, memberVO == null ? "" : memberVO.getMemberUid());
				return rtnMap;
			} else if (RESULT_CD_LOGIN_USED_PASSWD.equals(rtnCd)) {
				rtnMap.put("rtnCd", rtnCd);
				rtnMap.put("rtnMsg", "기존비밀번호와 같은 비밀번호는 사용하실 수 없습니다.");
				// 로그 정보 등록
				String id = StringUtil.nvl(param.get("memberUid") != null ? param.get("memberUid") : param.get("cocId"));
				String logCont = "비밀번호 변경: " + id;
				commonService.insertLog(LOG_DIV_CD_C, "실패, 기존 비밀번호 사용", logCont, memberVO == null ? "" : memberVO.getMemberUid());
				return rtnMap;
			} else if (RESULT_CD_LOGIN_WRONG_CURRENT_PASSWD.equals(rtnCd)) {
				rtnMap.put("rtnCd", rtnCd);
				rtnMap.put("rtnMsg", "현재 비밀번호가 맞지않습니다.");
				// 로그 정보 등록
				String id = StringUtil.nvl(param.get("memberUid") != null ? param.get("memberUid") : param.get("cocId"));
				String logCont = "비밀번호 변경: " + id;
				commonService.insertLog(LOG_DIV_CD_C, "실패, 현재 비밀번호 틀림", logCont, memberVO == null ? "" : memberVO.getMemberUid());
				return rtnMap;
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e, rtnMap);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		rtnMap.put("rtnCd", RESULT_CD_ERROR);
		rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
		// 로그 정보 등록
		String id = StringUtil.nvl(param.get("memberUid") != null ? param.get("memberUid") : param.get("cocId"));
		String logCont = "비밀번호 변경: " + id;
		commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, id);

		return rtnMap;
	}

	private String pwdCheck(Map<String, Object> param) {
		String pwd = StringUtil.nvl(param.get("newPassword"));

		if(StringUtil.isEmpty(pwd)) {
			return "신규 비밀번호를 입력해 주세요.";
		}

		Matcher mc  = null;
		String pattern1 = "^(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(?=.*[0-9]).{8,50}$";
		String pattern2 = "(\\w)\\1\\1";

		if(pwd.length() < 8) {
			return "비밀번호는 숫자, 영문자, 특수문자 조합 8자 이상이어야 합니다.";
		}

		mc = Pattern.compile(pattern1).matcher(pwd);

		if(!mc.find()) {
			return "비밀번호는 숫자, 영문자, 특수문자 조합 8자 이상이어야 합니다.";
		}

		mc = Pattern.compile(pattern2).matcher(pwd);

		if(mc.find()) {
			return "비밀번호에 3자 이상의 연속 또는 반복 문자 및 숫자를 사용하실 수 없습니다.";
		}

		return "";
	}

	@GetMapping(path = "/happybot/builder")
	public String happyBotBuilderLogin(
			HttpServletRequest request, RedirectAttributes attributes) {

		String authCode = UUID.randomUUID().toString();
		Map<String, Object> params = new HashMap<>();
		params.put("code", authCode);
		params.put("expiredMinutes", 60);
		happyBotService.insertHappybotAuthCode(params);

		String absoluteUrl = customProperty.getHappyBotBuilderUrl();
		log.info("REDIRECT URL TO HAPPY BOT: {}", absoluteUrl);
		if (absoluteUrl.startsWith("/")) {
			absoluteUrl = request.getRequestURL().toString();
			absoluteUrl = absoluteUrl.replace("/happytalk/happybot/builder", customProperty.getHappyBotBuilderUrl());
		}
		log.info("REDIRECT URL TO HAPPY BOT: {}", absoluteUrl);

		attributes.addAttribute("authCode", authCode);
		return "redirect:" + absoluteUrl;
	}

	/**
	 * 아너스넷 비밀번호 변경
	 */
	@RequestMapping(value = "/api/login/honorsChangePasswd", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> honorChangePasswd
	(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) throws Exception {

		log.info("CHANGE PASSWORD: {}", param);

		model.put("pwd", param.get("newPassword"));
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			log.info("==================memberVO============= " + memberVO);
			int nHnet;
			/*if (memberVO.equals(null)) {*/
				nHnet = memberDao.selectSkipUser(param);
				log.info("==================  selectSkipUser nHnet  ============= " + nHnet);
			/*
			 * } else { if ("Y".equals(memberVO.getNHnet())) {nHnet = 1;} else nHnet = 0;
			 * log.info("================== memberVO nHnet  ============= " + nHnet); }
			 */

			/**
			 * 아너스넷 비밀암호문
			 * */
			String honorsnetPw = String.valueOf(param.get("honorsPassword"));
			String memberUid = String.valueOf(param.get("memberUid"));
			String userId = String.valueOf(param.get("id"));
			if(nHnet == 0)
			{
				Map<String, Object> honorsnet = mcaService.pfdz102p(userId,  userId, honorsnetPw);
				String honorYn = String.valueOf(honorsnet.get("H_osucs_yn"));
				if(honorYn.equals("Y")) {
					param.put("honorsPwd", String.valueOf(honorsnet.get("H_opswd_cryp")));
					param.put("odtbrCode", String.valueOf(honorsnet.get("H_odtbr_code")));
				}else {
					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
					rtnMap.put("rtnMsg", "아너스넷 비밀번호가 일치하지 않습니다.");
					return rtnMap;
				}
			}
			String rtnCd = userService.changeHonorPasswd(param, false, false);

			if (RESULT_CD_SUCCESS.equals(rtnCd)) {
				rtnMap.put("rtnCd", rtnCd);
				rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");
				// 로그 정보 등록
				String id = StringUtil.nvl(param.get("memberUid") != null ? param.get("memberUid") :  param.get("id") != null ? param.get("id") : param.get("cocId") != null ? param.get("cocId") : null);
				String logCont = "아너스 비밀번호 변경: " + id;
				commonService.insertLog(LOG_DIV_CD_C, "성공", logCont, memberVO == null ? "" : memberVO.getMemberUid());
				return rtnMap;
			}else {
				rtnMap.put("rtnCd", RESULT_CD_ERROR);
				rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e, rtnMap);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		rtnMap.put("rtnCd", RESULT_CD_ERROR);
		rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
		// 로그 정보 등록
		String id = StringUtil.nvl(param.get("memberUid") != null ? param.get("memberUid") :  param.get("id") != null ? param.get("id") : param.get("cocId") != null ? param.get("cocId") : null);
		String logCont = "비밀번호 변경: " + id;
		commonService.insertLog(LOG_DIV_CD_C, "실패", logCont, id);

		return rtnMap;
	}
}
