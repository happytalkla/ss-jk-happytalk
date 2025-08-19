package ht.controller;

import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_A;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_B;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_C;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_D;
import static ht.constants.CommonConstants.CSTM_LINK_DIV_CD_E;
import static ht.constants.CommonConstants.LOG_DIV_CD_C;

import java.math.BigDecimal;
import java.net.URLEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.persistence.BatchDao;
import ht.service.CommonService;
import ht.service.MemberAuthService;
import ht.service.UserService;
import ht.service.ReportService;
import ht.service.ReportingService;
import ht.service.CategoryService;
import ht.service.BatchService;
import ht.util.DateUtil;
import ht.util.ExcelView;
import ht.util.HTUtils;
import ht.util.StringUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 통계
 *
 * 월간 리포트
 * 일간 리포트
 * 상담 내역
 * SQI
 * 봇 리포트
 */
@Controller
@RequestMapping(path = "/reporting")
@Slf4j
public class ReportingController {

	@Resource
	private ReportingService reportingService;
	@Resource
	private ReportService reportService;
	@Resource
	private CommonService commonService;
	@Resource
	private UserService memInfo;
	@Resource
	private CategoryService categoryService;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private BatchDao batchDao;

	/**
	 * Monthly Report
	 */
	@RequestMapping(value = "/monthly", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectMonthlyAll(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			// 금월 1일부터 금일까지 날짜 정보 등

			String startDate;
			String endDate;
			String monthpicker = "";

			String linkCdA =  " checked";
			String linkCdB =  " checked";
			String linkCdC =  " checked";
			String linkCdD =  " checked";
			String linkCdE =  " checked"; // IPCC_MCH
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");

			param.put("cnsr_div_cd", null);
			param.put("cstm_link_div_cd", null);
			param.put("cnsr_div_cd_week", null);
			param.put("cstm_link_div_cd_week", null);
			param.put("cnsr_div_cd_ctg", null);
			param.put("cstm_link_div_cd_ctg", null);
			param.put("member_uid", null);
			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}


			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDayType", "last");
				map = commonService.selectCustomDate(paramMap);
				endDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "MONTH");
				paramMap.put("schDayType", "add");
				paramMap.put("schAdd", 0);
				map = commonService.selectCustomDate(paramMap);
				monthpicker = StringUtil.nvl(map.get("sel_day"));

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));
				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));
				monthpicker = param.get("monthpicker").toString();

				log.info("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			}

			log.info(">>>>>>>>> monthpicker : " + monthpicker);

			// 페이징 정보
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			//int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int totPage = 1;
			int totCount = 0;

			if(linkCdArr != null) {
				linkCdA = null;
				linkCdB = null;
				linkCdC = null;
				linkCdD = null;
				linkCdE = null;
				param.put("linkCd", linkCdArr);
				for(int i = 0 ; i < linkCdArr.length;i++) {

					if(linkCdArr[i].equals("A")) linkCdA = " checked";
					if(linkCdArr[i].equals("B")) linkCdB = " checked";
					if(linkCdArr[i].equals("C")) linkCdC = " checked";
					if(linkCdArr[i].equals("D")) linkCdD = " checked";
					if(linkCdArr[i].equals("E")) linkCdE = " checked";
				}
			}


			log.info("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);

			////param.put("departCd",  memberVO.getDepartCd());
			param.put("jobDt", monthpicker.replaceAll("-", ""));

			List<Map<String, Object>> basicList = reportingService.selectMonthlyBasicStatcs(param);
			Map<String, Object> 	kakaoDivCnt = reportingService.selectMonthlyBasicKakao(param);
			List<Map<String, Object>> dayList = reportingService.selectMonthlyDayStatcs(param);
			List<Map<String, Object>> weekList = reportingService.selectMonthlyWeekStatcs(param);
			List<Map<String, Object>> adviserList = reportingService.selectMonthlyAdviserStatcs(param);
			List<Map<String, Object>> scoreList = reportingService.selectMonthlyScoreStatcs(param);
			List<Map<String, Object>> ctgList = reportingService.selectCtgStatcs(param);
			param.put("type", "Z");
			List<Map<String, Object>> TermRankMonZList = reportingService.selectTermRankMon(param);
			param.put("type", "U");
			List<Map<String, Object>> TermRankMonUList = reportingService.selectTermRankMon(param);


			String ctgDept = reportingService.selectCtgDept(param);

			if (scoreList != null && scoreList.size() > 0) {
				Map<String, Object> map = scoreList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}

			log.debug("ctgList : {}", ctgList.toString());
			model.put("linkCdA", linkCdA);
			model.put("linkCdB", linkCdB);
			model.put("linkCdC", linkCdC);
			model.put("linkCdD", linkCdD);
			model.put("linkCdE", linkCdE);

			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);

			model.put("basicList", basicList);
			model.put("kakaoDivCnt", kakaoDivCnt.get("kakao_div_cnt"));
			model.put("dayList", dayList);
			model.put("weekList", weekList);
			model.put("adviserList", adviserList);
			model.put("scoreList", scoreList);
			model.put("ctgList", ctgList);
			model.put("TermRankMonZList", TermRankMonZList);
			model.put("TermRankMonUList", TermRankMonUList);
			model.put("ctgDept", ctgDept);
			model.put("startDate", startDate);
			model.put("endDate", endDate);
			model.put("monthpicker", monthpicker);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "reporting/monthly";
	}



	@RequestMapping(value = "/selectDayReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectDayReport(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd", memberVO.getDepartCd());
			String linkCdA =  " checked";
			String linkCdB =  " checked";
			String linkCdC =  " checked";
			String linkCdD =  " checked";
			String linkCdE =  " checked";
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");


			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));
			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}
			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			if (param.get("cnsr_div_cd").toString().equals("0")) {
				param.put("cnsr_div_cd", null);
			}

			/*
			 * if (param.get("cstm_link_div_cd").toString().equals("0")) {
			 * param.put("cstm_link_div_cd", null); }
			 */
			if(linkCdArr != null) {
				linkCdA = null;
				linkCdB = null;
				linkCdC = null;
				linkCdD = null;
				linkCdE = null;
				param.put("linkCd", linkCdArr);
				for(int i = 0 ; i < linkCdArr.length;i++) {

					if(linkCdArr[i].equals("A")) linkCdA = " checked";
					if(linkCdArr[i].equals("B")) linkCdB = " checked";
					if(linkCdArr[i].equals("C")) linkCdC = " checked";
					if(linkCdArr[i].equals("D")) linkCdD = " checked";
					if(linkCdArr[i].equals("E")) linkCdE = " checked";
				}
			}

			List<Map<String, Object>> dayList = reportingService.selectMonthlyDayStatcs(param);

			model.put("cnsr_div_cd", param.get("cnsr_div_cd"));
			//model.put("cstm_link_div_cd", param.get("cstm_link_div_cd"));

			model.put("dayList", dayList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxDayReport";
	}

	@RequestMapping(value = "/selectWeekReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectWeekReport(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd", memberVO.getDepartCd());

			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			if (param.get("cnsr_div_cd_week").toString().equals("0")) {
				param.put("cnsr_div_cd_week", null);
			}
			if (param.get("cstm_link_div_cd_week").toString().equals("0")) {
				param.put("cstm_link_div_cd_week", null);
			}

			List<Map<String, Object>> weekList = reportingService.selectMonthlyWeekStatcs(param);

			model.put("cnsr_div_cd_week", param.get("cnsr_div_cd_week"));
			model.put("cstm_link_div_cd_week", param.get("cstm_link_div_cd_week"));

			model.put("weekList", weekList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxWeekReport";
	}

	@RequestMapping(value = "/selectAdviserDetailList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectAdviserDetailList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd", memberVO.getDepartCd());

			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			Map<String, Object> adviserInfo = reportingService.selectMemberInfo(param.get("member_uid").toString());

			List<Map<String, Object>> adviserList = reportingService.selectMonthlyAdviserStatcs(param);

			log.debug("uid : {}", param.get("member_uid"));
			log.debug("startDate : {}", param.get("startDate"));
			log.debug("endDate : {}", param.get("endDate"));

			model.put("adviserInfo", adviserInfo);
			model.put("adviserList", adviserList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxAdviserReport";
	}

	@RequestMapping(value = "/selectScoreList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectScoreList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd",  memberVO.getDepartCd());

			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			// 페이징 정보
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int totPage = 1;
			int totCount = 0;

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);

			log.debug("startDate > : {}", param.get("startDate"));
			log.debug("endDate > : {}", param.get("endDate"));
			log.debug("nowPage > : {}", param.get("nowPage"));
			log.debug("totCount > : {}", param.get("totCount"));

			List<Map<String, Object>> scoreList = reportingService.selectMonthlyScoreStatcs(param);

			if (scoreList != null && scoreList.size() > 0) {
				Map<String, Object> map = scoreList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}

			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);
			model.put("scoreList", scoreList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxScoreList";
	}

	@RequestMapping(value = "/selectCtgReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCtgReport(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd",  memberVO.getDepartCd());

			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			List<Map<String, Object>> ctgList = reportingService.selectCtgStatcs(param);
			String ctgDept = reportingService.selectCtgDept(param);

			model.put("ctgList", ctgList);
			model.put("ctgDept", ctgDept);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxCtgReport";
	}

	@RequestMapping(value = "/selectCtgMonthlyReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCtgMonthlyReport(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd", memberVO.getDepartCd());

			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			log.debug("cnsr_div_cd_ctg : {}, cstm_link_div_cd_ctg : {}", param.get("cnsr_div_cd_ctg"),
					param.get("cstm_link_div_cd_ctg"));

			if (param.get("cnsr_div_cd_ctg").toString().equals("0")) {
				param.put("cnsr_div_cd_ctg", null);
			}

			param.put("cstm_link_div_cd_ctg", null);
			List<Map<String, Object>> ctgList = reportingService.selectCtgStatcs(param);
			String ctgDept = reportingService.selectCtgDept(param);

			model.put("cnsr_div_cd_ctg", param.get("cnsr_div_cd_ctg"));
			model.put("cstm_link_div_cd_ctg", param.get("cstm_link_div_cd_ctg"));

			model.put("ctgList", ctgList);
			model.put("ctgDept", ctgDept);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxMonthlyCtgReport";
	}

	@RequestMapping(value = "/selectCtgDetailReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCtgDetailReport(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd", memberVO.getDepartCd());

			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			log.debug("cnsr_div_cd_ctg : {}, cstm_link_div_cd_ctg : {}", param.get("cnsr_div_cd_ctg"),
					param.get("cstm_link_div_cd_ctg"));
			param.put("cnsr_div_cd_ctg", StringUtil.nvl(param.get("cnsr_div_cd_ctg"), "0"));

			if (param.get("cnsr_div_cd_ctg").toString().equals("0")) {
				param.put("cnsr_div_cd_ctg", null);
			}

			param.put("cstm_link_div_cd_ctg", null);
			List<Map<String, Object>> ctgList = reportingService.selectCtgMonthlyStatcs(param);
			String ctgDept = reportingService.selectCtgDept(param);

			model.put("cnsr_div_cd_ctg", param.get("cnsr_div_cd_ctg"));
			model.put("cstm_link_div_cd_ctg", param.get("cstm_link_div_cd_ctg"));

			model.put("ctgList", ctgList);
			model.put("ctgDept", ctgDept);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxCtgDetailReport";
	}

	@RequestMapping(value = "/downloadCtgReportExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadCtgReportExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			Map<String, Object> ModelMap, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			// 엑셀파일명
			String target = "분류별_통계_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", "대분류");
			map.put("data", "ctg1");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "중분류");
			map.put("data", "ctg2");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "소분류");
			map.put("data", "ctg3");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담 수");
			map.put("data", "cns_request_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원당 평균 상담 수");
			map.put("data", "avg_end_cnt");
			map.put("width", 0);
			titleList.add(map);
			map = new HashMap<String, Object>();
			map.put("title", "평균 상담 시간(초)");
			map.put("data", "avg_end_time");
			map.put("width", 0);
			titleList.add(map);

			/*
			 * map = new HashMap<String, Object>(); map.put("title", "평가건수");
			 * map.put("data", "total_evl_cnt"); map.put("width", 0); titleList.add(map);
			 * 
			 * map = new HashMap<String, Object>(); map.put("title", "평균별점");
			 * map.put("data", "avg_evl_score"); map.put("width", 0); titleList.add(map);
			 */



			ModelMap.put("titleList", titleList);

			ModelMap.put("sheetName", "분류별 통계");

			// 엑셀파일에 저장할 리스트를 가져온다.
			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			////param.put("departCd", memberVO.getDepartCd());

			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			if (param.get("cnsr_div_cd_ctg").toString().equals("0")) {
				param.put("cnsr_div_cd_ctg", null);
			}
			param.put("cstm_link_div_cd_ctg", null);

			List<Map<String, Object>> ctgList = reportingService.selectCtgStatcs(param);


			ModelMap.put("dataList", ctgList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), ModelMap);
	}

	/**
	 * 엑셀 Monthly 일별 통계 다운로드
	 */
	@RequestMapping(value = "/downloadMonthlyDayReportExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadMonthlyDayReportExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			Map<String, Object> ModelMap, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd", memberVO.getDepartCd());

			// 엑셀파일명
			String target = "일별데이터_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", "상담일자");
			map.put("data", "day");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담인입수");
			map.put("data", "cns_request_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담포기수");
			map.put("data", "cns_quit_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담종료수");
			map.put("data", "cns_end_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평균 상담 시간(초)");
			map.put("data", "avg_end_time_excel");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원 수");
			map.put("data", "cnsr_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원 평균 상담 수");
			map.put("data", "avg_end_cnt");
			map.put("width", 0);
			titleList.add(map);


			ModelMap.put("titleList", titleList);

			ModelMap.put("sheetName", "일자별 통계");

			// 엑셀파일에 저장할 리스트를 가져온다.
			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			/*
			 * if (param.get("cnsr_div_cd").toString().equals("0")) {
			 * param.put("cnsr_div_cd", null); }
			 */
			/*
			 * if (param.get("cstm_link_div_cd").toString().equals("0")) {
			 * param.put("cstm_link_div_cd", null); }
			 */

			List<Map<String, Object>> dayList = reportingService.selectMonthlyDayStatcs(param);

			ModelMap.put("dataList", dayList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), ModelMap);
	}

	/**
	 * 엑셀 Monthly 요일별 통계 다운로드
	 */
	@RequestMapping(value = "/downloadMonthlyWeekReportExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadMonthlyWeekReportExcel(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, Map<String, Object> ModelMap,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			////param.put("departCd", memberVO.getDepartCd());

			// 엑셀파일명
			String target = "요일별_통계_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			ModelMap.put("sheetName", "요일별 통계");
			map.put("title", "요일");
			map.put("data", "day");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담인입수");
			map.put("data", "cns_request_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담포기수");
			map.put("data", "cns_quit_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담종료수");
			map.put("data", "cns_end_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평균 상담 시간(초)");
			map.put("data", "avg_end_time");
			map.put("width", 0);
			titleList.add(map);



			map = new HashMap<String, Object>();
			map.put("title", "상담직원 수");
			map.put("data", "cnsr_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원 평균 상담 종료");
			map.put("data", "avg_end_cnt");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			// 엑셀파일에 저장할 리스트를 가져온다.
			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			if (param.get("cnsr_div_cd_week").toString().equals("C")) {
				param.put("cnsr_div_cd_week", null);
			}
			if (param.get("cstm_link_div_cd_week").toString().equals("0")) {
				param.put("cstm_link_div_cd_week", null);
			}

			List<Map<String, Object>> weekList = reportingService.selectMonthlyWeekStatcs(param);

			ModelMap.put("dataList", weekList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 엑셀 Monthly 상담직원 통계 다운로드
	 */
	@RequestMapping(value = "/downloadMonthlyAdviserReportExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadMonthlyAdviserReportExcel(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, Map<String, Object> ModelMap,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			// 엑셀파일명
			String target = "상담직원별_통계_"+StringUtil.nvl(param.get("member_uid"))+"_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "상담직원 통계");
			if (StringUtil.nvl(param.get("member_uid")) != null){
				map.put("title", "상담 일자");
				map.put("data", "reg_date");
				map.put("width", 0);
				titleList.add(map);
			}else {
				map.put("title", "상담직원");
				map.put("data", "name");
				map.put("width", 0);
				titleList.add(map);
			}
			map = new HashMap<String, Object>();
			map.put("title", "상담 진행수");
			map.put("data", "cns_request_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담 후처리 수");
			map.put("data", "after_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평균 상담 시간(초)");
			map.put("data", "evl_end_time");
			map.put("width", 0);
			titleList.add(map);

			/*
			 * map = new HashMap<String, Object>(); map.put("title", "평가건수");
			 * map.put("data", "evl_cnt"); map.put("width", 0); titleList.add(map);
			 * 
			 * map = new HashMap<String, Object>(); map.put("title", "평균별점");
			 * map.put("data", "avg_evl_score"); map.put("width", 0); titleList.add(map);
			 */

			ModelMap.put("titleList", titleList);

			// 엑셀파일에 저장할 리스트를 가져온다.
			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			List<Map<String, Object>> adviserList = reportingService.selectMonthlyAdviserStatcs(param);

			ModelMap.put("dataList", adviserList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 엑셀 Monthly 상담직원 일자별 통계 다운로드
	 */
	@RequestMapping(value = "/downloadMonthlyAdviserReportAllExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadMonthlyAdviserReportAllExcel(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, Map<String, Object> ModelMap,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			// 엑셀파일명
			String target = "상담직원_일자별_통계_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			ModelMap.put("sheetName", "상담직원 통계");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("title", "담당매니저");
			map.put("data", "manager_name");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원");
			map.put("data", "name");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담신청");
			map.put("data", "cns_request_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담후처리수");
			map.put("data", "after_cnt");
			map.put("width", 0);
			titleList.add(map);
			map = new HashMap<String, Object>();


			map = new HashMap<String, Object>();
			map.put("title", "근무일수");
			map.put("data", "work_yn");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평균 상담 시간(초)");
			map.put("data", "evl_end_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "일 평균 상담수");
			map.put("data", "avg_evl_end_cnt");
			map.put("width", 0);
			titleList.add(map);



			/*
			 * map = new HashMap<String, Object>(); map.put("title", "평가건수");
			 * map.put("data", "evl_cnt"); map.put("width", 0); titleList.add(map);
			 * 
			 * map = new HashMap<String, Object>(); map.put("title", "평균별점");
			 * map.put("data", "avg_evl_score"); map.put("width", 0); titleList.add(map);
			 */
			ModelMap.put("titleList", titleList);

			// 엑셀파일에 저장할 리스트를 가져온다.
			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			//if (param.get("dayFlag").toString().equals("day")) {
			param.put("member_uid", null);
			//}

			List<Map<String, Object>> adviserList = reportingService.selectMonthlyAdviserStatcs(param);

			ModelMap.put("dataList", adviserList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 엑셀 Monthly 별점 통계 다운로드
	 */
	@RequestMapping(value = "/downloadScoreListExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadScoreListExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			Map<String, Object> ModelMap, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			//param.put("departCd", memberVO.getDepartCd());
			// 엑셀파일명
			String target = "평가_통계_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "평가 통계");
			map.put("title", "N");
			map.put("data", "create_dt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평가입력 날짜");
			map.put("data", "create_dt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담종료 날짜");
			map.put("data", "room_end_dt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "별점");
			map.put("data", "evl_score");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평가내용");
			map.put("data", "evl_cont");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원");
			map.put("data", "name");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "대화방");
			map.put("data", "chat_room_uid");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			// 엑셀파일에 저장할 리스트를 가져온다.
			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			if (param.get("cnsr_div_cd_week").toString().equals("0")) {
				param.put("cnsr_div_cd_week", null);
			}
			if (param.get("cstm_link_div_cd_week").toString().equals("0")) {
				param.put("cstm_link_div_cd_week", null);
			}

			List<Map<String, Object>> weekList = reportingService.selectScoreListAll(param);

			ModelMap.put("dataList", weekList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * Daily Report
	 */
	@RequestMapping(value = "/daily", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectDailyAll(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			// 금월 1일부터 금일까지 날짜 정보 등

			String regDate;
			String linkCdA =  " checked";
			String linkCdB =  " checked";
			String linkCdC =  " checked";
			String linkCdD =  " checked";
			String linkCdE =  " checked";
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");


			if (param.get("regDate") == null) {

				regDate = commonService.selectDateToStringFormat(-1);
				param.put("regDate", regDate.replaceAll("-", ""));
				log.info("commonService.selectDateToStringFormat(-1) : {} ", regDate);
			} else {
				regDate = param.get("regDate").toString();
				param.put("regDate", regDate.replaceAll("-", ""));

			}

			//param.put("departCd",  memberVO.getDepartCd());
			param.put("cnsr_div_cd", null);
			param.put("cstm_link_div_cd", null);
			param.put("member_uid", null);
			param.put("cnsr_div_cd_ctg", null);
			param.put("cstm_link_div_cd_ctg", null);
			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}


			log.debug("member_uid : {}", param.get("member_uid"));

			param.put("startDate", param.get("regDate"));
			param.put("endDate", param.get("regDate"));

			log.debug("startDate : {}", param.get("startDate"));
			log.debug("endDate : {}", param.get("endDate"));
			if(linkCdArr != null) {
				linkCdA = null;
				linkCdB = null;
				linkCdC = null;
				linkCdD = null;
				linkCdE = null;
				param.put("linkCd", linkCdArr);
				for(int i = 0 ; i < linkCdArr.length;i++) {

					if(linkCdArr[i].equals("A")) linkCdA = " checked";
					if(linkCdArr[i].equals("B")) linkCdB = " checked";
					if(linkCdArr[i].equals("C")) linkCdC = " checked";
					if(linkCdArr[i].equals("D")) linkCdD = " checked";
					if(linkCdArr[i].equals("E")) linkCdE = " checked";
				}
			}
			List<Map<String, Object>> basicList = reportingService.selectDailyBasicStatcs(param);
			Map<String, Object> kakaoDivCnt = reportingService.selectDailyBasicKakao(param);
			List<Map<String, Object>> timeList = reportingService.selectDailyTimeStatcs(param);
			List<Map<String, Object>> adviserList = reportingService.selectMonthlyAdviserStatcs(param);
			List<Map<String, Object>> ctgList = reportingService.selectCtgStatcs(param);
			param.put("type", "Z");
			List<Map<String, Object>> TermRankDayZList = reportingService.selectTermRankDay(param);
			param.put("type", "U");
			List<Map<String, Object>> TermRankDayUList = reportingService.selectTermRankDay(param);

			String ctgDept = reportingService.selectCtgDept(param);

			int totCount = 0;
			if (adviserList != null && adviserList.size() > 0) {

				totCount = adviserList.size();
			}

			log.info("======================== kakaoDivCnt : {}", kakaoDivCnt);
			model.put("linkCdA", linkCdA);
			model.put("linkCdB", linkCdB);
			model.put("linkCdC", linkCdC);
			model.put("linkCdD", linkCdD);
			model.put("linkCdE", linkCdE);
			model.put("basicList", basicList);
			model.put("kakaoDivCnt", kakaoDivCnt.get("kakao_div_cnt"));
			model.put("timeList", timeList);
			model.put("adviserList", adviserList);

			model.put("ctgList", ctgList);
			model.put("TermRankDayZList", TermRankDayZList);
			model.put("TermRankDayUList", TermRankDayUList);

			model.put("ctgDept", ctgDept);
			model.put("regDate", regDate);
			model.put("totCount", totCount);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/daily";
	}

	/**
	 * Daily 시간별 통계
	 */

	@RequestMapping(value = "/selectTimeReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectTimeReport(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			//param.put("departCd", memberVO.getDepartCd());

			String regDate;

			if (param.get("regDate") == null) {
				regDate = commonService.selectDateToString(0);
				param.put("regDate", regDate.replaceAll("-", ""));

			} else {
				regDate = param.get("regDate").toString();
				param.put("regDate", regDate.replaceAll("-", ""));

			}

			if (param.get("cnsr_div_cd").toString().equals("0")) {
				param.put("cnsr_div_cd", null);
			}
			if (param.get("cstm_link_div_cd").toString().equals("0")) {
				param.put("cstm_link_div_cd", null);
			}

			log.debug("regDate : {}", regDate);

			List<Map<String, Object>> timeList = reportingService.selectDailyTimeStatcs(param);
			model.put("timeList", timeList);
			model.put("regDate", regDate);
			model.put("cnsr_div_cd", param.get("cnsr_div_cd"));
			model.put("cstm_link_div_cd", param.get("cstm_link_div_cd"));

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxTimeReport";
	}

	/**
	 * Daily 상담직원 Report
	 */

	@RequestMapping(value = "/selectAdviserDailyList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectAdviserDailyList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String regDate = param.get("regDate").toString();
			param.put("regDate", regDate.replaceAll("-", ""));
			//param.put("departCd", memberVO.getDepartCd());

			List<Map<String, Object>> adviserList = reportingService.selectDailyAdviserStatcs(param);
			//			List<Map<String, Object>> adviserGraphList = reportingService.selectDailyAdviserStatcsGraph(param);

			log.debug("uid : {}", param.get("member_uid"));
			log.debug("regDate : {}", param.get("regDate"));

			model.put("adviserList", adviserList);

			//			model.put("adviserGraphList", adviserGraphList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/ajaxAdviserDailyReport";
	}

	/**
	 * 엑셀 daily 시간별 통계 다운로드
	 */
	@RequestMapping(value = "/downloadTimeReportExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadTimeReportExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			Map<String, Object> ModelMap, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			//param.put("departCd", memberVO.getDepartCd());


			// 엑셀파일명
			String target = "시간별_통계_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "시간별 통계");
			map.put("title", "상담시간");
			map.put("data", "reg_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담인입수");
			map.put("data", "cns_request_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담포기수");
			map.put("data", "quit_total_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담종료수");
			map.put("data", "cns_end_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평균상담시간(초)");
			map.put("data", "evl_end_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원당 평균 상담수");
			map.put("data", "avg_per_cnsr");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			// 엑셀파일에 저장할 리스트를 가져온다.

			String regDate = param.get("regDate").toString();
			param.put("regDate", regDate.replaceAll("-", ""));
			param.put("cstm_link_div_cd", null);
			//param.put("departCd", memberVO.getDepartCd());

			log.debug("uid : {}", param.get("member_uid"));
			log.debug("regDate : {}", param.get("regDate"));

			List<Map<String, Object>> adviserList = reportingService.selectDailyTimeStatcs(param);

			ModelMap.put("dataList", adviserList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 엑셀 Daily 상담직원별 시간 통계 다운로드
	 */
	@RequestMapping(value = "/downloadTimeAdviserReportExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadTimeAdviserReportExcel(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, Map<String, Object> ModelMap,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			// 엑셀파일명
			String target = "상담직원_일자별_통계_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "상담직원 통계");

			map.put("title", "담당매니저");
			map.put("data", "manager_name");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			if (param.get("dayFlag").toString().equals("day")) {
				map.put("title", "상담직원");
				map.put("data", "name");
				map.put("width", 0);
				titleList.add(map);
			} else {
				map.put("title", "일자");
				map.put("data", "reg_date");
				map.put("width", 0);
				titleList.add(map);
			}


			map.put("title", "상담 진행수");
			map.put("data", "cns_request_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담 후처리 수");
			map.put("data", "cns_end_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평균 상담 시간(초)");
			map.put("data", "evl_end_time");
			map.put("width", 0);
			titleList.add(map);

			/*
			 * map = new HashMap<String, Object>(); map.put("title", "평가건수");
			 * map.put("data", "evl_cnt"); map.put("width", 0); titleList.add(map);
			 * 
			 * map = new HashMap<String, Object>(); map.put("title", "평균별점");
			 * map.put("data", "avg_evl_score"); map.put("width", 0); titleList.add(map);
			 */

			ModelMap.put("titleList", titleList);

			// 엑셀파일에 저장할 리스트를 가져온다.
			String startDate = param.get("startDate").toString();
			String endDate = param.get("endDate").toString();
			param.put("startDate", startDate.replaceAll("-", ""));
			param.put("endDate", endDate.replaceAll("-", ""));

			log.debug("startDate : {}, endDate : {}", startDate, endDate);

			if (param.get("dayFlag").toString().equals("day")) {
				param.put("member_uid", null);
			}

			List<Map<String, Object>> adviserList = reportingService.selectMonthlyAdviserStatcs(param);

			ModelMap.put("dataList", adviserList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 상담내역 메인
	 */

	@RequestMapping(value = "/counseling", method = { RequestMethod.GET, RequestMethod.POST })
	public String counseling(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param			) {


		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String startDate;
			String endDate;

			String linkCdA =  " checked";
			String linkCdB =  " checked";
			String linkCdC =  " checked";
			String linkCdD =  " checked";
			String linkCdE =  " checked";

			String[] linkCdArr = request.getParameterValues("cstm_link_cd");

			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "last");
				map = commonService.selectCustomDate(paramMap);
				endDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "MONTH");
				paramMap.put("schDayType", "add");
				paramMap.put("schAdd", 0);
				map = commonService.selectCustomDate(paramMap);

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("*****************************" +endDate);
				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));


				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			}


			String customerType = (String) param.get("customerType");
			String customerText = (String) param.get("customerText");

			if(customerType == null) {
				customerType ="CID";
			}
			if(customerText == null) {
				customerText ="";
			}

			log.debug("nowPage : {} ", param.get("nowPage"));

			// 페이징 정보
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			//int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int pageListCount = 20;
			int totPage = 1;
			int totCount = 0;


			if(linkCdArr != null) {
				linkCdA = null;
				linkCdB = null;
				linkCdC = null;
				linkCdD = null;
				linkCdE = null;
				param.put("linkCd", linkCdArr);
				for(int i = 0 ; i < linkCdArr.length;i++) {

					if(linkCdArr[i].equals("A")) linkCdA = " checked";
					if(linkCdArr[i].equals("B")) linkCdB = " checked";
					if(linkCdArr[i].equals("C")) linkCdC = " checked";
					if(linkCdArr[i].equals("D")) linkCdD = " checked";
					if(linkCdArr[i].equals("E")) linkCdE = " checked";
				}
			}

			//param.put("departCd", memberVO.getDepartCd());

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);
			/*
			 * List<Map<String, Object>> selectCounselingCnt =
			 * reportingService.selectCounselingCnt(param);
			 */

			List<Map<String, Object>> counselingList = reportingService.selectCounseling(param);

			if (counselingList != null && counselingList.size() > 0) {
				Map<String, Object> map = counselingList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}
			model.put("linkCdA", linkCdA);
			model.put("linkCdB", linkCdB);
			model.put("linkCdC", linkCdC);
			model.put("linkCdD", linkCdD);
			model.put("linkCdE", linkCdE);

			model.put("startDate", startDate);
			model.put("endDate", endDate);
			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);

			model.put("customerType", customerType);
			model.put("customerText", customerText);

			model.put("counselingList", counselingList);
			/* model.put("selectCounselingCnt", selectCounselingCnt); */
			
			// 20211101 LKJ 상담내역 Reporting 고객명/고객번호 검색시 history 기록 추가
			if ( StringUtils.isNotEmpty( customerText ) && ( "CID".equals(customerType) || "CNM".equals(customerType) )  ) {
				String logCont = "";
				if ( "CID".equals(customerType) ) {
					logCont = "상담내역조회 (고객ID:" + customerText + ")";					
				} else if ( "CNM".equals(customerType) ) {
					logCont = "상담내역조회 (고객명:" + customerText + ")";					
				}				
				commonService.insertLog(LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/counseling";
	}

	/**
	 * 상담내역 엑셀 다운로드
	 */
	@RequestMapping(value = "/counselingExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadCounselingExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			Map<String, Object> ModelMap, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String startDate;
			String endDate;
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");

			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				endDate = commonService.selectDateToString(0);

				log.debug("startDate : {}, endDate : {}", startDate, endDate);

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("startDate : {}, endDate : {}", startDate, endDate);
			}

			String ctgMgtDpt = reportingService.selectCtgMgtDpt(param);
			param.put("ctgMgtDpt", ctgMgtDpt);

			log.debug("ctgMgtDpt : {}", ctgMgtDpt);

			// 엑셀파일명
			String target = "상담내역_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "상담내역");

			map.put("title", "No.");
			map.put("data", "rnum");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담방");
			map.put("data", "chat_room_uid");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "채팅방 생성일시");
			map.put("data", "room_create_dt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원배정일시");
			map.put("data", "cnsr_assign_dt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "채팅방 종료일시");
			map.put("data", "room_end_dt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담 시작일시");
			map.put("data", "cnsr_submit_dt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담이용시간(초)");
			map.put("data", "use_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "채널");
			map.put("data", "cstm_link_div_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "고객번호");
			map.put("data", "cstm_uid");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "고객명");
			map.put("data", "c_name");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "고객 User-key");
			map.put("data", "cstm_user_key");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "2차분류");
			map.put("data", "ctg1");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "3차분류");
			map.put("data", "ctg2");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "4차분류");
			map.put("data", "ctg3");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "종료구분");
			map.put("data", "end_div_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원");
			map.put("data", "m_name");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}

			//param.put("departCd", memberVO.getDepartCd());
			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}
			// 엑셀파일에 저장할 리스트를 가져온다.
			List<Map<String, Object>> counselingList = reportingService.selectCounselingExcel(param);

			ModelMap.put("dataList", counselingList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * SQI
	 */
	@RequestMapping(value = "/sqireport", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectSqiAll(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session);
		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String startDate;
			String endDate;

			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "last");
				map = commonService.selectCustomDate(paramMap);
				endDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "MONTH");
				paramMap.put("schDayType", "add");
				paramMap.put("schAdd", 0);
				map = commonService.selectCustomDate(paramMap);

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("*****************************" +endDate);
				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));


				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			}
			log.info("startDate : {}, endDate : {}", startDate, startDate);

			log.debug("nowPage : {} ", param.get("nowPage"));

			// 페이징 정보
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			//int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int pageListCount = 20;
			int totPage = 1;
			int totCount = 0;

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
			Date dateStart = formatter.parse(startDate);
			SimpleDateFormat formatterE = new SimpleDateFormat("yyyy-mm-dd");
			Date dateEnd = formatterE.parse(endDate);
			long diff = dateEnd.getTime() - dateStart.getTime();
			long diffDays = diff / ( 86400 * 1000) + 1;
			param.put("diffDays", Long.toString(diffDays) );

			//param.put("departCd", memberVO.getDepartCd());

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);

			param.put("ctg_Num", StringUtil.nvl(param.get("ctg2")));


			param.put("ctgNum", null);
			param.put("ctgDpt", "1");
			param.put("useYn", "Y");
			List<Map<String, Object>> categoryList2;
			List<Map<String, Object>> categoryQuery;
			List<Map<String, Object>> categoryList = categoryService.selectCategoryListManager(param);
			if(StringUtil.nvl(param.get("ctg2")) != "") {
				param.put("upperCtgNum", param.get("ctg1"));
				param.put("ctgDpt", "2");
				param.put("useYn", "Y");
				categoryList2 = categoryService.selectCategoryListManager(param);
				model.put("categoryList2", categoryList2);

				param.put("upperCtgNum", param.get("ctg2"));
				param.put("ctgDpt", "3");
				param.put("useYn", "Y");
				categoryQuery = categoryService.selectCategoryListManager(param);
				int idx = 0;
				String[] searchCtg3 = new String[categoryQuery.size() + 1];
				for (Map<String, Object> item : categoryQuery) {
					searchCtg3[idx] = item.get("ctg_num").toString();
					idx++;
				}
				if(StringUtil.nvl(param.get("ctg2")) != "")	 searchCtg3[categoryQuery.size()] = StringUtil.nvl(param.get("ctg2"));
				else searchCtg3 = null;
				param.put("categoryArray", searchCtg3);
				log.info("*********************{}", param.get("ctg2"));
			}

			List<Map<String, Object>> sqiList = reportingService.selectSQIList(param);
			List<Map<String, Object>> basicCnt = reportingService.selectSQICnt(param);




			if (sqiList != null && sqiList.size() > 0) {
				Map<String, Object> map = sqiList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}

			model.put("ctg1", param.get("ctg1"));
			model.put("ctg2", param.get("ctg2"));

			model.put("startDate", startDate);
			model.put("endDate", endDate);
			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);

			model.put("sqiList", sqiList);
			model.put("categoryList", categoryList);
			model.put("basicCnt", basicCnt);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/sqireport";
	}

	/**
	 * SQI 엑셀 다운로드
	 */
	@RequestMapping(value = "/downloadSelectSqiAllExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadSelectSqiAllExcel(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, Map<String, Object> ModelMap,
			@RequestParam Map<String, Object> param) {


		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String startDate;
			String endDate;
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");

			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDayType", null);
				paramMap.put("schAdd", "-1");
				map = commonService.selectCustomDate(paramMap);
				endDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "MONTH");
				paramMap.put("schDayType", "add");
				paramMap.put("schAdd", 0);
				map = commonService.selectCustomDate(paramMap);

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));


				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			}

			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}


			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
			Date dateStart = formatter.parse(startDate);
			SimpleDateFormat formatterE = new SimpleDateFormat("yyyy-mm-dd");
			Date dateEnd = formatterE.parse(endDate);
			long diff = dateEnd.getTime() - dateStart.getTime();
			long diffDays = diff / ( 86400 * 1000) + 1;
			param.put("diffDays", Long.toString(diffDays) );

			//param.put("departCd", memberVO.getDepartCd());

			param.put("ctg_Num", StringUtil.nvl(param.get("ctg2")));


			param.put("ctgNum", null);
			param.put("ctgDpt", "1");
			param.put("useYn", "Y");
			List<Map<String, Object>> categoryList2;
			List<Map<String, Object>> categoryQuery;
			if(StringUtil.nvl(param.get("ctg2")) != "") {
				param.put("upperCtgNum", param.get("ctg1"));
				param.put("ctgDpt", "2");
				param.put("useYn", "Y");
				categoryList2 = categoryService.selectCategoryListManager(param);
				model.put("categoryList2", categoryList2);

				param.put("upperCtgNum", param.get("ctg2"));
				param.put("ctgDpt", "3");
				param.put("useYn", "Y");
				categoryQuery = categoryService.selectCategoryListManager(param);
				int idx = 0;
				String[] searchCtg3 = new String[categoryQuery.size() + 1];
				for (Map<String, Object> item : categoryQuery) {
					searchCtg3[idx] = item.get("ctg_num").toString();
					idx++;
				}
				if(StringUtil.nvl(param.get("ctg2")) != "")	 searchCtg3[categoryQuery.size()] = StringUtil.nvl(param.get("ctg2"));
				else searchCtg3 = null;
				param.put("categoryArray", searchCtg3);
				log.info("*********************{}", param.get("ctg2"));
			}

			List<Map<String, Object>> sqiList = reportingService.selectSQIListExcel(param);
			// 엑셀파일명
			String target = "SQI_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "SQI");
			map.put("title", "담당매니저");
			map.put("data", "manager_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원");
			map.put("data", "member_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "검색기간내 상담종료건수");
			map.put("data", "s_cnsr_end_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "전기간 대비 상담종료건수");
			map.put("data", "cmp_end_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "검색기간내 배정 후 최초 응대");
			map.put("data", "xsl_s_assign_fst_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "전기간 대비  배정 후 최초 응대");
			map.put("data", "cmp_s_assign_fst_time");
			map.put("width", 0);
			titleList.add(map);
			map = new HashMap<String, Object>();
			map.put("title", "검색기간내 대화중 고객대기");
			map.put("data", "xsl_s_cstm_wait_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "전기간 대비  대화중 고객대기");
			map.put("data", "cmp_s_cstm_wait_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "검색기간내 대화중 상담직원대기	");
			map.put("data", "xsl_s_cnsr_wait_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "전기간 대비 대화중 상담직원대기(초)");
			map.put("data", "cmp_s_cnsr_wait_time");
			map.put("width", 0);
			titleList.add(map);
			map = new HashMap<String, Object>();
			map.put("title", "검색기간내 총대화시간(초)");
			map.put("data", "xsl_s_chat_total_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "전기간 대비 총대화시간");
			map.put("data", "cmp_s_chat_total_time");
			map.put("width", 0);
			titleList.add(map);
			map = new HashMap<String, Object>();
			map.put("title", "검색기간내 상담후처리시간(초)");
			map.put("data", "xsl_s_after_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "전기간 대비 상담후처리시간(초)");
			map.put("data", "cmp_s_after_time");
			map.put("width", 0);
			titleList.add(map);
			ModelMap.put("titleList", titleList);
			ModelMap.put("dataList", sqiList);


		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}
	/**
	 * 봇상담내역
	 */
	@RequestMapping(value = "/botreport", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectbotAll(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String startDate;
			String endDate;
			/**채널 추가시 linkCdArr, linkCd 추가**/
			String linkCdK = " checked";
			String linkCdN = " checked";
			String searchTypeM = null;
			String searchTypeD = " checked";
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");
			int spanNum = 2;
			param.put("botMemberUidList", Arrays.asList(customProperty.getHappyBotMemeberUid(), customProperty.getCategoryBotMemeberUid()));

			if(param.get("searchType") != null && param.get("searchType").toString().equals("M")) {
				searchTypeD = null;
				searchTypeM =  " checked";
			}
			else {
				searchTypeM = null;
				searchTypeD =  " checked";
			}
			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDayType", "DAY");
				paramMap.put("schDayType", "last");
				map = commonService.selectCustomDate(paramMap);
				endDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "MONTH");
				paramMap.put("schDayType", "add");
				paramMap.put("schAdd", 0);
				map = commonService.selectCustomDate(paramMap);

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));


				log.info("***************** null startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));


				log.info("***************** not null startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			}

			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
				linkCdK = null;
				linkCdN = null;
				spanNum = linkCdArr.length;
				for(int i = 0 ; i < spanNum; i++) {
					if(linkCdArr[i].equals("B")) linkCdK = " checked";
					if(linkCdArr[i].equals("C")) linkCdN = " checked";
				}
			}else {
				linkCdArr =new String[2];
				linkCdArr[0]="B";
				linkCdArr[1]="C";
				linkCdK = " checked";
				linkCdN = " checked";
				spanNum = linkCdArr.length;
				param.put("linkCd", linkCdArr);
			}

			// 페이징 정보
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			//int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int pageListCount = 20;

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);

			log.info("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			List<Map<String, Object>> selectBotBasicCnt = reportingService.selectBotBasicCnt(param);
			List<Map<String, Object>> selectBotChannelCnt = reportingService.selectBotChannelCnt(param);
			List<Map<String, Object>> channelList = reportingService.selectBotChannelDate(param);
			List<Map<String, Object>> channelListGraph = reportingService.selectBotChannelDateGraph(param);
			//List<Map<String, Object>> botRoomList = reportingService.selectBotRoomList(param);
			List<Map<String, Object>> blockList = reportingService.selectBlockCnt(param);			/// block 사용 카운트
			List<Map<String, Object>> mciList = reportingService.selectMCIcnt(param);

/*			if (botRoomList != null && botRoomList.size() > 0) {
				Map<String, Object> map = botRoomList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}
		*/
			model.put("blockList", blockList);
			int sumBlockCnt = blockList.stream().map(m -> ((BigDecimal)m.get("block_cnt")).intValue()).collect(Collectors.toList()).stream().collect(Collectors.summingInt(Integer::intValue));
			model.put("sumBlockCnt", sumBlockCnt);
			model.put("spanNum", spanNum);
			model.put("linkCdK", linkCdK);
			model.put("linkCdN", linkCdN);
			model.put("searchTypeM", searchTypeM);
			model.put("searchTypeD", searchTypeD);

			model.put("startDate", startDate);
			model.put("endDate", endDate);
//			model.put("nowPage", nowPage);
//			model.put("totPage", totPage);
//			model.put("totCount", totCount);

			model.put("channelList", channelList);
			model.put("channelListGraph", channelListGraph);
//			model.put("botRoomList", botRoomList);
			model.put("selectBotBasicCnt", selectBotBasicCnt);
			model.put("selectBotChannelCnt", selectBotChannelCnt);

			model.put("mciList", mciList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/botreport";
	}

	/**
	 * 방문추이 엑셀 다운로드
	 */
	@RequestMapping(value = "/downloadLinkDateExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadLinkDateExcel(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, Map<String, Object> ModelMap,
			@RequestParam Map<String, Object> param) {


		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String startDate;
			String endDate;
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");


			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDayType", "");
				paramMap.put("schAdd",-1);
				map = commonService.selectCustomDate(paramMap);
				endDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "MONTH");
				paramMap.put("schDayType", "add");
				paramMap.put("schAdd", 0);
				map = commonService.selectCustomDate(paramMap);

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			}

			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}


			// 엑셀파일명
			String target = "방문고객추이_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "방문고객추이");
			map.put("title", "방문일");
			map.put("data", "reg_date");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "채널");
			map.put("data", "link_div_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "총 방문자 수");
			map.put("data", "all_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "신규 방문자 수");
			map.put("data", "new_visit_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "재 방문자 수");
			map.put("data", "re_visit_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "챗봇 이용건수");
			map.put("data", "chatbot_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "평균 이용시간(초)");
			map.put("data", "elapse_time");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			List<Map<String, Object>> channelList = reportingService.selectBotChannelDate(param);
			ModelMap.put("dataList", channelList);


		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 블럭별 엑셀 다운로드
	 */
	@RequestMapping(value = "/downloadBlockExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadBlockExcel(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, Map<String, Object> ModelMap,
			@RequestParam Map<String, Object> param) {


		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String startDate;
			String endDate;
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");

			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDayType", "");
				paramMap.put("schAdd",-1);
				map = commonService.selectCustomDate(paramMap);
				endDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "MONTH");
				paramMap.put("schDayType", "add");
				paramMap.put("schAdd", 0);
				map = commonService.selectCustomDate(paramMap);

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			}

			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}


			// 엑셀파일명
			String target = "챗봇블럭당이용건수_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "챗봇블럭당이용건수");
			map.put("title", "블록명");
			map.put("data", "name");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "호출건수");
			map.put("data", "block_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "사용비율");
			map.put("data", "avg_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "시작건수");
			map.put("data", "str_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원 연결 수");
			map.put("data", "req_cnt");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			List<Map<String, Object>> blockList = reportingService.selectBlockCnt(param);
			ModelMap.put("dataList", blockList);


		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 봇 대화방목록 엑셀 다운로드
	 */
	@RequestMapping(value = "/downloadBotRoomExcel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadBotRoomExcel(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, Map<String, Object> ModelMap,
			@RequestParam Map<String, Object> param) {


		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String startDate;
			String endDate;
			String[] linkCdArr = request.getParameterValues("cstm_link_cd");
			param.put("botMemberUidList", Arrays.asList(customProperty.getHappyBotMemeberUid(), customProperty.getCategoryBotMemeberUid()));

			if (param.get("startDate") == null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();

				paramMap.put("schDateType", "DAY");
				paramMap.put("schDayType", "first");
				Map<String, Object> map = commonService.selectCustomDate(paramMap);
				startDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDayType", "");
				paramMap.put("schAdd",-1);
				map = commonService.selectCustomDate(paramMap);
				endDate = StringUtil.nvl(map.get("sel_day"));

				paramMap.put("schDateType", "MONTH");
				paramMap.put("schDayType", "add");
				paramMap.put("schAdd", 0);
				map = commonService.selectCustomDate(paramMap);

				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));

			} else {
				startDate = param.get("startDate").toString();
				endDate = param.get("endDate").toString();
				param.put("startDate", startDate.replaceAll("-", ""));
				param.put("endDate", endDate.replaceAll("-", ""));

				log.debug("startDate : {}, endDate : {}", param.get("startDate"), param.get("endDate"));
			}

			if(linkCdArr != null) {
				param.put("linkCd", linkCdArr);
			}


			// 엑셀파일명
			String target = "봇대화방리스트_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			ModelMap.put("sheetName", "봇대화방리스트");
			map.put("title", "No");
			map.put("data", "rnum");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담방");
			map.put("data", "CHAT_ROOM_UID");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "챗봇시작일시");
			map.put("data", "ROOM_CREATE_DT");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "챗봇종료일시");
			map.put("data", "ROOM_END_DT");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "챗봇이용시간(초)");
			map.put("data", "elapse_time");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "채널");
			map.put("data", "chan_nm");
			map.put("width", 0);
			titleList.add(map);

			/*			map = new HashMap<String, Object>();
			map.put("title", "블록호출수");
			map.put("data", "block_cnt");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "시작블록명");
			map.put("data", "fst_blk_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "상담직원 연결 블록명");
			map.put("data", "req_cns_blk");
			map.put("width", 0);
			titleList.add(map);
			 */
			map = new HashMap<String, Object>();
			map.put("title", "고객번호");
			map.put("data", "coc_id");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "고객명");
			map.put("data", "name");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "고객 User-key	");
			map.put("data", "cstm_uid");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "본인인증수단");
			map.put("data", "cert_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<String, Object>();
			map.put("title", "방문 구분");
			map.put("data", "visit_type");
			map.put("width", 0);
			titleList.add(map);

			ModelMap.put("titleList", titleList);

			List<Map<String, Object>> botRoomList = reportingService.selectBotRoomListXsl(param);
			ModelMap.put("dataList", botRoomList);


		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}
	/**
	 * 배치 수동
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param ModelMap
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/batchreport", method = { RequestMethod.GET, RequestMethod.POST })
	public String batchAll(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {



		try {
			String batchDate = StringUtil.nvl(param.get("batchDate")).replaceAll("-", "");			
			/*
			 * IPCC_MCH 채널 추가 관련 수정
			 * 기존 하드코딩 방식에서 공통코드 등록 기준으로 변경
			 */
			//String[] departArr = {"F1", "F2", "M1", "M2" , "DC", "DM"};		//부서 F1 F2	M2	DC	M1	DM
			//String[] ctgnumArr = {"9999999998", "9999999999"};	//기본분류 설정
			//String[] linkdivCd = { CSTM_LINK_DIV_CD_A, CSTM_LINK_DIV_CD_B, CSTM_LINK_DIV_CD_C, CSTM_LINK_DIV_CD_D, CSTM_LINK_DIV_CD_E};  	//인입채널 설정

			List<Map<String, Object>> commonCdList = commonService.selectCodeList(CommonConstants.COMM_CD_DEPART_CD);
			List<String> departCdList = new ArrayList<String>();
			for (Map<String, Object> map : commonCdList) {
				departCdList.add((String) map.get("cd"));
			}
			String[] departArr = new String[departCdList.size()];
			departArr = (String[]) departCdList.toArray();

			commonCdList = commonService.selectCodeList(CommonConstants.COMM_CD_CSTM_LINK_DIV_CD);
			List<String> cstmLinkDivCdList = new ArrayList<String>();
			for (Map<String, Object> map : commonCdList) {
				cstmLinkDivCdList.add((String) map.get("cd"));
			}
			String[] linkdivCd = new String[cstmLinkDivCdList.size()];
			linkdivCd = (String[]) cstmLinkDivCdList.toArray();

			String msgText = "";
			///기간 구하기
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
			Date startDate = format.parse(param.get("startDate").toString());
			Date endDate = format.parse(param.get("endDate").toString());
			long calDate = endDate.getTime() - startDate.getTime();
			long maxNum = Math.abs(calDate / (86400000));
			
			model.put("startDate", param.get("startDate").toString());
			model.put("endDate", param.get("endDate").toString());
			
			param.put("schDateType", "DAY");
			param.put("startDate", param.get("startDate").toString().replaceAll("-",""));
			param.put("endDate",  param.get("endDate").toString().replaceAll("-",""));
			for (int j = 0; j< maxNum+1 ; j++) {
				param.put("schAdd",j);
				Map<String, Object> customDate = commonService.selectCustomDate(param);
				batchDate = customDate.get("sel_day").toString().replace("-", "");

				if( !"".equals(batchDate)) {
					log.info("*****************batchDate****************"+batchDate);
					param.put("regDate", batchDate);
					param.put("botMemberUidList", Arrays.asList(customProperty.getHappyBotMemeberUid(), customProperty.getCategoryBotMemeberUid()));
					param.put("regDateKakao", batchDate+" 04:00:00");
					/* 상담통계 내역 초기화 */
					for (int i=0; i < departArr.length; i++) {
						param.put("departCd", departArr[i]);
						//param.put("ctgNum", ctgnumArr[i]);
						int deleteSD = 0;
						int deleteST = 0;
						log.info("*****************param****************"+param);
						deleteSD += reportService.deleteStatsDate(param); // Stats_date 삭제
						deleteST += reportService.deleteStatsTime(param); // stats_time 삭제
					}

					reportService.deleteStatsCnsr(param); // stats_cnsr 삭제
					reportService.deleteStatsCnsrTime(param); // stats_cnsr_time 삭제
					reportService.deleteStatsCtgDate(param); // stats_ctg_date	삭제
					reportService.deleteSQILIst(param) ;


					/* 상담통계 내역 배치 */
					for (int i=0; i < departArr.length; i++) {
						param.put("departCd", departArr[i]);
//						param.put("ctgNum", ctgnumArr[i]);
						reportService.insertStatsDate(param); // Stats_date 저장
						reportService.insertStatsTime(param); // stats_time 저장
					}

					reportService.insertStatsCnsr(param); // stats_cnsr 저장
					reportService.insertStatsCnsrTime(param); // stats_cnsr_time 저장
					reportService.insertStatsCtgDate(param); // stats_ctg_date저장
					reportService.insertSQILIst(param) ;

					/* 봇통계 내역 초기화 */
					for(int i = 0; i < linkdivCd.length; i++) {
						param.put("cstmLinkCd", linkdivCd[i]);
						reportService.deleteStatsLinkRobot(param); // Stats_date 저장
					}
					reportService.deleteStatsRobot(param); // Stats_date 저장

					/* 봇통계 내역 배치 */
					for(int i = 0; i < linkdivCd.length; i++) {
						param.put("cstmLinkCd", linkdivCd[i]);
						reportService.insertStatsLinkRobot(param); // Stats_date 저장
					}
					reportService.insertStatsRobot(param); // Stats_date 저장

					
					/* 일별 검색어 결과 */
					Map<String, Object> sqlParams = new HashMap<>();
					sqlParams.put("termType", "D");
					sqlParams.put("jobDt", batchDate);
					sqlParams.put("type", "Z");
					log.info("*****************batchDate****************"+batchDate);
					batchDao.deleteTermRankDay(sqlParams);
					sqlParams.put("type", "A");
					List<Map<String, Object>> zeroList = batchDao.selectTermRank(sqlParams);
					sqlParams.put("type", "U");
					batchDao.deleteTermRankDay(sqlParams);
					sqlParams.put("type", "B");
					List<Map<String, Object>> upperList = batchDao.selectTermRank(sqlParams);

					log.info(">>> zeroList : " + zeroList);
					for(Map<String, Object> zeroMap : zeroList) {
						log.info(">>> zeroMap : " + zeroMap);
						zeroMap.put("type",	"Z");
						zeroMap.put("jobDt",batchDate);
						batchDao.insertTermRankDay(zeroMap);
					}

					for(Map<String, Object> upperMap : upperList) {
						upperMap.put("type", "U");
						upperMap.put("jobDt",batchDate);
						batchDao.insertTermRankDay(upperMap);
					}
					
					
					
					msgText += batchDate + " 배치 처리 완료</br>";
					model.put("msgText", msgText);
					
				}
				
			}  // for end
			Map<String, Object> sqlParamsM = new HashMap<>();
			/* 월별 검색어 결과 */
			sqlParamsM.put("termType", "M");
			sqlParamsM.put("jobDt", batchDate.substring(0, 6));
			log.info("*****************batchDate****************"+batchDate);
			sqlParamsM.put("type", "Z");
			batchDao.deleteTermRankMon(sqlParamsM);
			sqlParamsM.put("type", "A");
			List<Map<String, Object>> zeroListM = batchDao.selectTermRank(sqlParamsM);
			sqlParamsM.put("type", "U");
			batchDao.deleteTermRankMon(sqlParamsM);
			sqlParamsM.put("type", "B");
			List<Map<String, Object>> upperListM = batchDao.selectTermRank(sqlParamsM);

			log.info(">>> zeroList : " + zeroListM);

			for(Map<String, Object> zeroMap : zeroListM) {
				log.info(">>> zeroMap : " + zeroMap);
				zeroMap.put("type",	"Z");
				zeroMap.put("jobDt",batchDate.substring(0, 6));
				batchDao.insertTermRankMon(zeroMap);
			}

			for(Map<String, Object> upperMap : upperListM) {
				upperMap.put("type", "U");
				upperMap.put("jobDt",batchDate.substring(0, 6));
				batchDao.insertTermRankMon(upperMap);
			}
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}
		model.put("batchDate", param.get("batchDate"));
		return "reporting/bResult";
	}
	/**
	 * 배치test bot
	 */

	@RequestMapping(value = "/batchreport2", method = { RequestMethod.GET, RequestMethod.POST })
	public String batchbotAll(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		try {
			/*
			 * IPCC_MCH ARS 채널 추가
			 * 기존 하드코딩 방식에서 공통코드 등록 기준으로 변경
			 */
			//String[] linkdivCd = { CSTM_LINK_DIV_CD_A, CSTM_LINK_DIV_CD_B, CSTM_LINK_DIV_CD_C, CSTM_LINK_DIV_CD_D};
			List<Map<String, Object>> commonCdList = commonService.selectCodeList(CommonConstants.COMM_CD_CSTM_LINK_DIV_CD);
			List<String> linkDivCdList = new ArrayList<String>();
			for (Map<String, Object> map : commonCdList) {
				linkDivCdList.add((String) map.get("cd"));
			}
			String[] linkdivCd = new String[linkDivCdList.size()];
			linkdivCd = (String[]) linkDivCdList.toArray();

			String maxNum = StringUtil.nvl(param.get("days"), "0");
			log.info("{}*********************************"+maxNum);
			for (int j = 1; j< Integer.parseInt(maxNum) ; j++) {
				String batchdate = commonService.selectDateToString(j * (-1));
				log.info("{}*********************************"+batchdate);
				param.put("botMemberUidList", Arrays.asList(customProperty.getHappyBotMemeberUid(), customProperty.getCategoryBotMemeberUid()));

				param.put("regDate", batchdate);

				for(int i = 0; i < linkdivCd.length; i++) {
					param.put("cstmLinkCd", linkdivCd[i]);
					reportService.insertStatsLinkRobot(param); // Stats_date 저장
				}
				reportService.insertStatsRobot(param); // Stats_date 저장
				//reportService.insertStatsIntent(param); // stats_cnsr 저장
			}

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/batchTest";
	}

	/**
	 * 배치test gateway
	 */
	@Resource
	private BatchService gatewayService;
	@RequestMapping(value = "/batchgateway", method = { RequestMethod.GET, RequestMethod.POST })
	public String batchgateway(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		try {
			gatewayService.runMemberReset();
			model.put("result", "test");
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "reporting/batchTest";
	}
}
