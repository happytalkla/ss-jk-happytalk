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
import org.springframework.web.servlet.ModelAndView;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.service.MemberAuthService;
import ht.service.AppService;
import ht.util.DateUtil;
import ht.util.ExcelView;
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
@RequestMapping(path = "/appMgr")
@Slf4j
public class AppController {

	@Resource
	private AppService appService;
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
	@RequestMapping(value = "/screenList", method = { RequestMethod.GET, RequestMethod.POST })
	public String screenList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			Locale locale, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		try {
			String schText = StringUtil.nvl(param.get("schText"));
			schText = StringEscapeUtils.escapeHtml4(schText);
			
			log.info("*******************************" + schText);
			if("I".equals(param.get("schType"))){
				param.put("screenNum", schText);
			}
			if("T".equals(param.get("schType"))){
				param.put("screenName", schText);
			}
			
			//  조회
			List<Map<String, Object>> screenList = appService.selectScreenList(param);

			model.put("schType", StringUtil.nvl(param.get("schType"), "ALL"));
			model.put("schText", schText);
			model.put("screenList", screenList);


		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}
		return "appmgr/screenList";
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
	@RequestMapping(value = "/addScreen", method = { RequestMethod.GET, RequestMethod.POST })
	public String addScreen(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "appmgr/addScreenPop";
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
	@RequestMapping(value = "/viewScreen", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewScreen(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		log.debug("screenNum : {}", param.get("screenNum"));
		// 용어사전 상세 조회
		Map<String, Object> screenDetail = appService.selectScreenDetail(param);
		
		model.addAttribute("screenDetail", screenDetail);
		
		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "appmgr/viewScreenPop";
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
	
	@RequestMapping(value = "/editScreen", method = { RequestMethod.GET, RequestMethod.POST })
	public String editScreen(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		log.info("screenNum : {}", param.get("screenNum"));
		
		// 용어사전 상세 조회
		Map<String, Object> screenDetail = appService.selectScreenDetail(param);
		
		model.addAttribute("updateChk", "Y");
		model.addAttribute("screenDetail", screenDetail);
		
		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "appmgr/addScreenPop";
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
	@RequestMapping(value = "/deleteScreen", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteScreen(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ScreenController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		log.debug("screen_num : {}", param.get("screenNum"));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			log.debug("screen_num : {}", param.get("screenNum"));
			log.debug("sessionMemberUid : {}", param.get("sessionMemberUid"));
			// 용어사전 삭제
			if (param.get("screenNum") != null) {
				appService.deleteScreen(param);
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
	@RequestMapping(value = "/saveScreen", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> saveScreen(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ScreenController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		try {
			
			param.put("screenNum", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("screenNum"), "")) );
			Map<String, Object> screenDetail = appService.selectScreenDetail(param);

			param.put("screenName", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("screenName"), "")) );
			param.put("screenId", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("screenId"), "")) );
			
			// 등록
			if(StringUtil.isEmpty(param.get("updateChk"))) {
				if(screenDetail.size() == 0) {
					appService.insertScreen(param);
				}
				else {
					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
					rtnMap.put("rtnMsg", "동일한 화면번호가 있습니다.");
					return rtnMap;
				}
					
			}else {
				appService.updateScreen(param);
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
