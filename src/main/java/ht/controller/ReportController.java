package ht.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.service.MemberAuthService;
import ht.service.CommonService;
import ht.service.ReportService;
import ht.service.ReportingService;
import ht.service.SettingService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 상담관리 > 대시보드
 */
@Controller
@RequestMapping(path = "/report")
@Slf4j
public class ReportController {
	@Resource
	private CustomProperty customProperty;

	@Resource
	private ReportService reportService;

	@Resource
	private ReportingService reportingService;

	@Resource
	private SettingService settingService;

	@Resource
	private CommonService commonService;



	/**
	 * 매니저 메인 대시보드
	 */
	@RequestMapping(value = "/managerMain", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectManagerMain(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			param.put("member", memberVO);
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());
			param.put("member_uid", memberVO.getMemberUid());
			param.put("memberDivCd", memberVO.getMemberDivCd());
			param.put("departCd", memberVO.getDepartCd());
			param.put("nHnet", memberVO.getNHnet());
			param.put("sessionVo", memberVO);

			// 기본 설정 정보 조회
			Map<String, Object> defaultSet = settingService.selectDefaultSet();
			model.put("defaultSet", defaultSet);

			String startDate = "";
			String endDate = "";

			String nowDate = commonService.selectDateToString(0);
			startDate = nowDate;
			endDate = nowDate;

			param.put("startDate", startDate);
			param.put("endDate", endDate);

			String selectNowTime = reportService.selectNowTime();

			param.put("botMemberUidList", Arrays.asList(customProperty.getHappyBotMemeberUid(), customProperty.getCategoryBotMemeberUid()));

			List<Map<String, Object>> basicList = reportService.selectTotalReport(param);
			List<Map<String, Object>> ctgWiList = reportService.selectCTGWaitIng(param);
			List<Map<String, Object>> cnsrList = reportService.selectCnsrReport(param); /// 상담직원 목록
			List<Map<String, Object>> basicTodayList = reportService.selectTodayTotalReport(param);
			List<Map<String, Object>> linkDivList = reportService.selectLinkDivReport(param);
			List<Map<String, Object>> ctgList = reportService.selectCTGafter(param);
			List<Map<String, Object>> mciList = reportService.selectMCICnt(param);

			////매니저 전용 대시 보드
			if("M".equals(memberVO.getMemberDivCd().toString())){
				param.put("managerUid", memberVO.getMemberUid());
				List<Map<String, Object>> basicListManager = reportService.selectTotalReport(param);
				List<Map<String, Object>> ctgWiListManager = reportService.selectCTGWaitIng(param);
				List<Map<String, Object>> cnsrListManager = reportService.selectCnsrReport(param); /// 상담직원 목록
				List<Map<String, Object>> basicTodayListManager = reportService.selectTodayTotalReport(param);
				List<Map<String, Object>> linkDivListManager = reportService.selectLinkDivReport(param);
				model.put("basicListManager", basicListManager);
				model.put("ctgWiListManager", ctgWiListManager);
				model.put("cnsrListManager", cnsrListManager);
				model.put("basicTodayListManager", basicTodayListManager);
				model.put("linkDivListManager", linkDivListManager);
			}

			model.put("nowDate", nowDate.substring(0,4)+"-"+nowDate.substring(4, 6)+"-"+nowDate.substring(6,8));
			model.put("selectNowTime", selectNowTime);
			model.put("cnsrList", cnsrList);
			model.put("basicList", basicList);
			model.put("linkDivList", linkDivList);
			model.put("ctgList", ctgList);
			model.put("ctgWiList", ctgWiList);
			model.put("basicTodayList", basicTodayList);
			model.put("mciList", mciList);



		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "report/managerMain";

	}

	/**
	 * 상담직원 선택에 따른 우측 그레프
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/selectCounseler", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCounseler(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			if (StringUtil.nvl(param.get("isToday"), "true").equals("true")) {

//				List<Map<String, Object>> selectTotalGroupReport = reportService.selectTotalGroupReport(param);

//				model.put("selectTotalGroupReport", selectTotalGroupReport);

			} else {

				List<Map<String, Object>> selectTotalGroupReport = reportService.selectTotalGroupReportYesterday(param);

				model.put("selectTotalGroupReport", selectTotalGroupReport);

			}

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "report/ajaxGraphArea";
	}

	/**
	 * 상담만족도
	 */
	@RequestMapping(value = "/selectScore", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectScore(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			String startDate = "";
			String endDate = "";

			// 페이징 정보
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int totPage = 1;
			int totCount = 0;

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);

			String ctgMgtDpt = reportingService.selectCtgMgtDpt(param);

			param.put("ctgMgtDpt", ctgMgtDpt);

			if (StringUtil.nvl(param.get("isToday"), "true").equals("true")) {
				String nowDate = commonService.selectDateToString(0);
				startDate = nowDate;
				endDate = nowDate;

				param.put("startDate", startDate);
				param.put("endDate", endDate);

			} else {
				startDate = commonService.selectDateToString(-1);
				endDate = commonService.selectDateToString(-1);

			}
			log.debug("nowPage : {}", param.get("nowPage"));
			log.debug("totPage : {}", param.get("totPage"));
			log.debug("pageListCount : {}", pageListCount);
			log.debug("startDate : {}", startDate);
			log.debug("endDate : {}", endDate);

			List<Map<String, Object>> scoreList = reportingService.selectMonthlyScoreStatcs(param);
			int scoreAvg = reportingService.selectMonthlyScoreAvg(param);

			if (scoreList != null && scoreList.size() > 0) {
				totCount = scoreList.size();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}

			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);

			model.put("scoreList", scoreList);
			model.put("scoreAvg", scoreAvg);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "report/ajaxScoreArea";
	}

}
