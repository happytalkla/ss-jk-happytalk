package ht.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.service.MemberAuthService;
import ht.service.CodeService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 화면번호 관련 controller
 * 화면번호 조회
 *
 * @author wizard
 *
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(path = "/codeMgr")
@Slf4j
public class CodeController {

	@Resource
	private CodeService codeService;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private HTUtils htUtils;

	/**
	 * 용어사전 조회
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/codeList", method = { RequestMethod.GET, RequestMethod.POST })
	public String codeList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			Locale locale, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		try {
			String schText = StringUtil.nvl(param.get("schText"));
			schText = StringEscapeUtils.escapeHtml4(schText);
			Map<String, Object> sparam = new HashMap<String, Object>();;
			if("I".equals(param.get("schType"))){
				sparam.put("code", schText);
			}
			if("T".equals(param.get("schType"))){
				sparam.put("cdNm", schText);
			}
			if("D".equals(param.get("schType"))){
				sparam.put("cdDesc", schText);
			}
			log.info("schText ******************************* " + schText);
			//  조회
			List<Map<String, Object>> codeList = codeService.selectCodeList(sparam);

			model.put("schType", StringUtil.nvl(param.get("schType"), "ALL"));
			model.put("schText", schText);
			model.put("codeList", codeList);


		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}
		return "codemgr/codeList";
	}

	/**
	 * 용어사전 등록화면
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/addCode", method = { RequestMethod.GET, RequestMethod.POST })
	public String addCode(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "codemgr/addCodePop";
	}

	/**
	 * 용어사전 상세 조회
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/viewCode", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewCode(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		log.debug("code : {}", param.get("code"));
		// 용어사전 상세 조회
		Map<String, Object> codeDetail = codeService.selectCodeDetail(param);
		
		model.addAttribute("codeDetail", codeDetail);
		
		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "codemgr/viewCodePop";
	}

	/**
	 * 용어 사전 수정 화면
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	
	@RequestMapping(value = "/editCode", method = { RequestMethod.GET, RequestMethod.POST })
	public String editCode(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		log.info("code : {}", param.get("code"));
		
		// 용어사전 상세 조회
		Map<String, Object> codeDetail = codeService.selectCodeDetail(param);
		
		model.addAttribute("updateChk", "Y");
		model.addAttribute("codeDetail", codeDetail);
		
		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "codemgr/addCodePop";
	}
	/**
	 * 용어사전 삭제
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/deleteCode", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteCode(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("CodeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		log.debug("code_num : {}", param.get("cd"));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			log.debug("code : {}", param.get("cd"));
			log.debug("sessionMemberUid : {}", param.get("sessionMemberUid"));
			// 용어사전 삭제
			if (param.get("cd") != null) {
				codeService.deleteCode(param);
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
	 * 용어사전 저장, 수정
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/saveCode", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> saveCode(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("CodeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		try {
			
			param.put("code", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("code").toString().toUpperCase(), "")) );
			List<Map<String, Object>> codeDetail = codeService.selectCodeList(param);

			param.put("cdNm", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("cdNm"), "")) );
			param.put("cdDesc", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("cdDesc"), "")) );
			
			// 등록
			if(StringUtil.isEmpty(param.get("updateChk"))) {
				if(codeDetail.size() == 0) {
					codeService.insertCode(param);
				}
				else {
					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
					rtnMap.put("rtnMsg", "동일한 코드가 있습니다.");
					return rtnMap;
				}
					
			}else {
				codeService.updateCode(param);
			}

			
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 저장되었습니다.");
			
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
		}
		
		return rtnMap;
	}

}
