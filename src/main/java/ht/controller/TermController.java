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
import ht.service.TermService;
import ht.util.DateUtil;
import ht.util.ExcelView;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 용어사전 관련 controller
 * 용어사전 조회
 *
 * @author wizard
 *
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(path = "/term")
@Slf4j
public class TermController {

	@Resource
	private TermService termService;
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
	@RequestMapping(value = "/termList", method = { RequestMethod.GET, RequestMethod.POST })
	public String termList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			Locale locale, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("TermController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		log.debug("termDivCd : {}", param.get("termDivCd"));
		log.debug("termDivCdPageVal : {}", param.get("termDivCdPageVal"));
		log.debug("searchVal : {}", param.get("searchVal"));

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			log.debug("memberDivCd : {}", memberVO.getMemberDivCd());

			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("memberDivCd", memberVO.getMemberDivCd());
			param.put("upperMemberUid", memberVO.getUpperMemberUid());
			param.put("termDivCd", param.get("termDivCdPageVal"));

			// 페이징 정보
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			int pageListCount = Integer.parseInt(StringUtil.nvl(param.get("pageListCount"), "20"));
			int totPage = 1;
			int totCount = 0;

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);
			param.put("schType", StringUtil.nvl(param.get("schType"), "ALL"));
			
			String schText = StringUtil.nvl(param.get("schText"));
			schText = StringEscapeUtils.escapeHtml4(schText);
			
			model.put("schText", schText);
			param.put("schText", schText);
			
			// 용어사전 조회
			List<Map<String, Object>> termList = termService.selectTermList(param);

			JSONObject jsonObj = new JSONObject();

			log.debug("jsonObj : {}", jsonObj.toString());

			if (termList != null && termList.size() > 0) {
				Map<String, Object> map = termList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}
			
			model.put("schType", StringUtil.nvl(param.get("schType"), "ALL"));
			model.put("termList", termList);
			model.put("pageListCount", StringUtil.nvl(param.get("pageListCount"), "20"));
			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "term/termList";
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
	@RequestMapping(value = "/addTerm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTerm(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "term/addTermPop";
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
	@RequestMapping(value = "/viewTerm", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewTerm(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		log.debug("termNum : {}", param.get("termNum"));
		// 용어사전 상세 조회
		Map<String, Object> termDetail = termService.selectTermDetail(param);
		
		model.addAttribute("termDetail", termDetail);
		
		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "term/viewTermPop";
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
	
	@RequestMapping(value = "/editTerm", method = { RequestMethod.GET, RequestMethod.POST })
	public String editTerm(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		log.info("termNum : {}", param.get("termNum"));
		log.debug("termNum : {}", param.get("termNum"));
		
		// 용어사전 상세 조회
		Map<String, Object> termDetail = termService.selectTermDetail(param);
		
		model.addAttribute("termDetail", termDetail);
		
		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "term/addTermPop";
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
	@RequestMapping(value = "/deleteTerm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteTerm(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("TermController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		log.debug("term_num : {}", param.get("termNum"));
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			log.debug("term_num : {}", param.get("termNum"));
			log.debug("sessionMemberUid : {}", param.get("sessionMemberUid"));
			// 용어사전 삭제
			if (param.get("termNum") != null) {
				termService.deleteTerm(param);
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
	@RequestMapping(value = "/saveTerm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> saveTerm(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("TermController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			log.debug("term_num : {}", param.get("termNum"));
			log.debug("termDivNm : {}", param.get("termDivNm"));
			log.debug("title : {}", param.get("title"));
			log.debug("cont : {}", param.get("cont"));
			log.debug("termTag : {}", param.get("termTag"));
			log.debug("termDivVal : {}", param.get("termDivVal"));
			;
			
			param.put("termDivNm", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("termDivNm"), "")) );
			param.put("title", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("title"), "")) );
			param.put("cont", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("cont"), "")) );
			param.put("termTag", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("termTag"), "")) );
			
			// 용어사전 등록
			if(StringUtil.isEmpty(param.get("termNum"))) {
				termService.insertTerm(param);	
			}else {
				termService.updateTerm(param);
			}

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 저장되었습니다.");
			
			model.put("nowPage", nowPage);
			model.put("searchVal", param.get("searchVal"));
			model.put("termDivNm", param.get("termDivNm"));
			model.put("sessionMemberUid", memberVO.getMemberUid());
			//model.put("sessionMemberDivCd", memberVO.getMemberDivCd());
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
		}
		
		return rtnMap;
	}
	

	/**
	 * 용어사전 엑셀다운로드
	 */
	@RequestMapping(value = "/downloadTermExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadTermExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			Map<String, Object> ModelMap, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 엑셀파일명
			String target = "용어사전_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "용어 사전");
			
			map.put("title", "ID");
			map.put("data", "term_num");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "구분");
			map.put("data", "term_div_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "제목");
			map.put("data", "title");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "내용");
			map.put("data", "cont");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "업데이트 일자");
			map.put("data", "update_dt");
			map.put("width", 0);
			titleList.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "태그");
			map.put("data", "term_tag");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			// 용어사전 조회
			List<Map<String, Object>> termList = termService.selectAllTermList(param);

			ModelMap.put("dataList", termList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}
}
