package ht.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Joiner;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.exception.BizException;
import ht.service.CategoryService;
import ht.service.CommonService;
import ht.service.ManageService;
import ht.service.McaService;
import ht.service.MemberAuthService;
import ht.service.MemberService;
import ht.service.ReportService;
import ht.service.SettingService;
import ht.service.UserService;
import ht.util.DateUtil;
import ht.util.ExcelFileType;
import ht.util.ExcelView;
import ht.util.HTUtils;
import ht.util.LogUtil;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 셋팅 관련 controller
 * 서비스 이용 내역
 * 계정 관리
 * 휴무일 관리(상담직원 근무 관리)
 * 코끼리 관리
 * 로그 관리
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(path = "/manage")
@Slf4j
public class ManageController {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private CommonService commonService;
	@Resource
	private ManageService manageService;
	@Resource
	private SettingService settingService;
	@Resource
	private UserService userService;
	@Resource
	private MemberService memberService;
	@Resource
	private ReportService reportService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private McaService mcaService;
	@Resource
	private HTUtils htUtils;

	/**
	 * 휴무일 관리/근무관리 화면 조회
	 */
	@RequestMapping(value = "/workManage", method = { RequestMethod.GET, RequestMethod.POST })
	public String workManage(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("workManage: {}", param);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("memberDivCd", memberVO.getMemberDivCd());
			//param.put("departCd", memberVO.getDepartCd());

			///기본 채널값 세팅
			String channel = null;
			if( StringUtil.isEmpty(param.get("channel")) ){
				channel = "E";
				param.put("channel", "E");
			}
			else channel =  param.get("channel").toString();
			
			log.info("channel" + channel); 
			Map<String, Object> defaultSet = settingService.selectDefaultSet();
			param.put("siteId", defaultSet.get("site_id").toString());
			model.put("unsocialAcceptYn", defaultSet.get(channel + "_" +"UNSOCIAL_ACCEPT_YN").toString());
			
			
			
			// 해당 월
			String schYear = StringUtil.nvl(param.get("schYear"));
			String schMonth = StringUtil.nvl(param.get("schMonth"));

			// PARAMETER LOG
			log.debug("schYear : {}", schYear);
			log.debug("schMonth : {}", schMonth);

			Map<String, Object> nowDate = commonService.selectNowDate();
			String nowYear = String.valueOf(nowDate.get("now_year"));
			String nowMonth = String.valueOf(nowDate.get("now_month"));
			String nowDay = String.valueOf(nowDate.get("now_day"));

			if (StringUtil.isEmpty(schYear) || StringUtil.isEmpty(schMonth)) {
				schYear = nowYear;
				schMonth = nowMonth;
			}

			if (schMonth.length() < 2) {
				schMonth = "0" + schMonth;
			}

			param.put("schYear", schYear);
			param.put("schMonth", schMonth);

			model.put("schYear", schYear);
			model.put("schMonth", schMonth);

			model.put("nowYear", nowYear);
			model.put("nowMonth", nowMonth);
			model.put("nowDay", nowDay);
			
			model.put("channel", param.get("channel").toString());

			// 해당 년도의 휴일 LIST
			List<Map<String, Object>> holidayList = manageService.selectHolidayList(schYear);
			model.put("holidayList", holidayList);

			// 해당 월의 휴일 LIST
			List<Map<String, Object>> holidayMonthList = manageService.selectHolidayMonthList(schYear, schMonth);
			Map<String, Object> holidayMap = new HashMap<String, Object>();
			if (holidayMonthList != null && holidayMonthList.size() > 0) {
				for (Map<String, Object> map : holidayMonthList) {
					holidayMap.put(String.valueOf(map.get("day")), map);
				}
			}

			model.put("holidayMap", holidayMap);

			// 해당 월의 달력
			List<Map<String, Object>> monthCalendar = manageService.selectMonthCalendar(schYear, schMonth);
			model.put("monthCalendar", monthCalendar);

			// 해당 월의 스케줄
			List<Map<String, Object>> monthSchList = manageService.selectMonthSchList(param);
			model.put("monthSchList", monthSchList);

			// 근무시간 설정 정보 조회
			Map<String, Object> workTime = settingService.selectWorkTime(param);
			model.put("workTime", workTime);

			/*
			 * IPCC_MCH 채널 리스트
			 */
			List<Map<String, Object>> channelList = commonService.selectCodeList(CommonConstants.COMM_CD_CSTM_LINK_DIV_CD);
			model.put("channelList", channelList);

			// }catch(BizException be){
			// //be.printStackTrace();
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "manage/workManage";
	}

	/**
	 * 휴일 등록
	 */
	@RequestMapping(value = "/insertHoliday", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> insertHoliday(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("insertHoliday: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("workYn", "N");

			manageService.insertHoliday(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "insertHoliday");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
		} catch (BizException be) {
			rtnMap.put("rtnCd", be.getErrCode());
			rtnMap.put("rtnMsg", be.getMessage());
			//be.printStackTrace();

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "insertHoliday");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "insertHoliday");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 휴일 삭제
	 */
	@RequestMapping(value = "/deleteHoliday", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteHoliday(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param,
			@RequestParam MultiValueMap<String, String> lparam) {

		log.info("deleteHoliday: {}", lparam);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		List<String> delHolidayDateList = StringUtil.nvlList(lparam.get("delHolidayDate"));

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			if (delHolidayDateList.size() < 1) {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_FAILURE);
				rtnMap.put("rtnMsg", "선택된 휴일이 없습니다.");
				return rtnMap;
			}

			manageService.deleteHoliday(delHolidayDateList);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 삭제 되었습니다.");

			// 로그 정보 등록
			param.put("delHolidayDateList", delHolidayDateList);
			String logCont = LogUtil.getCont(param, "deleteHoliday");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getMessage());
			//			//be.printStackTrace();
			//
			//			// 로그 정보 등록
			//			param.put("delHolidayDateList", delHolidayDateList);
			//			String logCont = LogUtil.getCont(param, "deleteHoliday");
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			param.put("delHolidayDateList", delHolidayDateList);
			String logCont = LogUtil.getCont(param, "deleteHoliday");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 특정일의 상담직원 목록 팝업창 열기
	 */
	@RequestMapping(value = "/selectCounselorHoliday", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCounselorHoliday(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectCounselorHoliday: {}", param);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("memberDivCd", memberVO.getMemberDivCd());
			param.put("departCd", memberVO.getDepartCd());

			// 매니저의 상담직원 목록
			List<Map<String, Object>> counselorHoliday = manageService.selectCounselorHoliday(param);
			model.put("counselorHoliday", counselorHoliday);
			log.info("counselorHoliday: \n{}", Joiner.on("\n").join(counselorHoliday));

			int dateCheck = commonService.selectNowDateCheck(String.valueOf(param.get("schDate")));
			model.put("dateCheck", dateCheck);

			// 근무 여부 체크
			Map<String, Object> workMap = manageService.selectWorkYnCheck(String.valueOf(param.get("schDate")), param.get("channel").toString());
			model.put("workMap", workMap);
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "manage/dateSchListPop";
	}

	/**
	 * 개별 상담수 설정 팝업창 열기
	 */
	@RequestMapping(value = "/selectChatCntSetting", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectChatCntSetting(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectChatCntSetting: {}", param);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("memberDivCd", memberVO.getMemberDivCd());

			// 매니저의 상담직원 목록
			List<Map<String, Object>> chatCntSetting = manageService.selectChatCntSetting(param);
			model.put("counselorHoliday", chatCntSetting);

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "manage/chatCntSettingPop";
	}

	/**
	 * 기본 스케쥴 관리 팝업창 열기
	 */
	@RequestMapping(value = "/selectDefaultSchedule", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectDefaultSchedule(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectDefaultSchedule: {}", param);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("departCd", memberVO.getDepartCd());
			model.put("departCd", memberVO.getDepartCd());

			// TM, CS의 근무시간 설정 정보 조회
			Map<String, Object> workTime = new HashMap<>();
			workTime = settingService.selectCSWorkTime();
//			String departCd = (String) param.get("departCd");
//			
//			if (departCd.equals("TM") ) {
//				workTime = settingService.selectTMWorkTime();
//			}else {
//				workTime = settingService.selectCSWorkTime();
//			}
			model.put("workTime", workTime);

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}


		return "manage/defaultSchedulePop";
	}


	/**
	 * 스케줄 등록
	 */
	@RequestMapping(value = "/insertSchedule", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> insertSchedule(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("insertSchedule: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		param.put("siteId", customProperty.getSiteId());

		String logCont = "상담직원 배정 스케줄 등록 : " + param.get("schYear") + param.get("schMonth");
		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("departCd", memberVO.getDepartCd());
			manageService.insertSchedule(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");

			// 로그 정보 등록
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getMessage());
			//			//be.printStackTrace();
			//			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");
			//
			//			// 로그 정보 등록
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 스케줄 삭제
	 */
	@RequestMapping(value = "/deleteSchedule", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteSchedule(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("deleteSchedule: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		String logCont = "스케줄 삭제";
		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			manageService.deleteSchedule(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");

			// 로그 정보 등록
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getMessage());
			//			//be.printStackTrace();
			//			rtnMap.put("rtnMsg", "정상적으로 삭제 되었습니다.");
			//
			//			// 로그 정보 등록
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 상담직원 휴일 설정 변경
	 */
	@RequestMapping(value = "/updateCnsrHoliday", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateCnsrHoliday(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("updateCnsrHoliday: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());

			String updateType = StringUtil.nvl(param.get("updateType"));
			if ("workYn".equals(updateType)) {
				manageService.updateCnsrHolidayWorkYn(param);
				// 로그 정보 등록
				String logCont = LogUtil.getCont(param, "updateCnsrHolidayWorkYn");
				commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			} else if ("time".equals(updateType)) {
				manageService.updateCnsrHolidayTime(param);

				// 로그 정보 등록
				String logCont = LogUtil.getCont(param, "updateCnsrHolidayTime");
				commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			} else {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_FAILURE);
				rtnMap.put("rtnMsg", "입력값이 정확하지 않습니다.");
				return rtnMap;
			}
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getMessage());
			//			//be.printStackTrace();
			//
			//			// 로그 정보 등록
			//			String logCont = "상담직원 휴일 설정 변경";
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "상담직원 휴일 설정 변경";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}


	/**
	 * 개별 상담수 update
	 */
	@RequestMapping(value = "/updateChatCntSetting", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateChatCntSetting(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("updateChatCntSetting: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			manageService.updateChatCntSetting(param);
			log.info("***************************{}", param);
			String logCont = LogUtil.getCont(param, "updateChatCntSetting");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "개별 상담수 update";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}




	/**
	 * 휴무일 양식 다운로드
	 */
	@RequestMapping(value="/downloadManageExcel" , method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView downloadExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, Map<String, Object> ModelMap
			, @RequestParam Map<String, Object> param) {

		log.info("downloadManageExcel: {}", param);

		try {
			// 엑셀파일명
			String target = "sample_holiday_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<>();
			Map<String, Object> map = new HashMap<>();
			map.put("title", "휴일 일자");
			map.put("data", "holiday_date");
			map.put("width", 40);
			titleList.add(map);
			/*
			map = new HashMap<>();
			map.put("title", "근무 여부(근무:Y, 휴무:N)");
			map.put("data", "work_yn");
			map.put("width", 100);
			titleList.add(map);
  시작시간 종료시간 제거 -- qq
			map = new HashMap<>();
			map.put("title", "시작 시간");
			map.put("data", "start_time");
			map.put("width", 40);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "종료 시간");
			map.put("data", "end_time");
			map.put("width", 40);
			titleList.add(map);
			 */
			map = new HashMap<>();
			map.put("title", "메모");
			map.put("data", "memo");
			map.put("width", 120);
			titleList.add(map);

			model.put("titleList", titleList);

			// 해당 년,월
			String schYear = StringUtil.nvl(param.get("schYear"));
			String schMonth = StringUtil.nvl(param.get("schMonth"));

			Map<String, Object> nowDate = commonService.selectNowDate();




			String nowYear = String.valueOf(nowDate.get("now_year"));
			String nowMonth = String.valueOf(nowDate.get("now_month"));
			String nowDay = String.valueOf(nowDate.get("now_day"));

			if (StringUtil.isEmpty(schYear) || StringUtil.isEmpty(schMonth)) {
				schYear = nowYear;
				schMonth = nowMonth;
			}

			if (schMonth.length() < 2) {
				schMonth = "0" + schMonth;
			}

			param.put("schYear", schYear);
			param.put("schMonth", schMonth);

			model.put("schYear", schYear);
			model.put("schMonth", schMonth);

			model.put("nowYear", nowYear);
			model.put("nowMonth", nowMonth);
			model.put("nowDay", nowDay);

			// 해당 년도의 휴일 LIST
			List<Map<String, Object>> holidayList = manageService.selectHolidayList(schYear);
			log.debug("***************************************"+holidayList.size());
			if(holidayList.size() == 0) {
				List<Map<String, Object>> examMap = new ArrayList<>();
				map = new HashMap<>();
				map.put("holiday_date", "20190101");
				/*map.put("work_yn", "N");*/
				map.put("memo", "메모입력란입니다");
				examMap.add(map);

				model.put("dataList", examMap);
			}
			else model.put("dataList", holidayList);
			model.put("sheetName","휴무일 양식");
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 휴무일 일괄 등록
	 */
	@RequestMapping(value="/updateManageExcel" , method = {RequestMethod.POST})
	public @ResponseBody Map<String, Object> updateTplExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param
			, @RequestParam("manageUploadFile") MultipartFile file) {

		log.info("updateManageExcel: {}", param);

		Map<String, Object> rtnMap = new HashMap<>();

		try{
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			Workbook wb = ExcelFileType.getWorkBook(file);

			Sheet sheet = wb.getSheetAt(0);
			int rowCnt = sheet.getPhysicalNumberOfRows();

			Row row;

			List<Map<String, Object>> dataList = new ArrayList<>();
			Map<String, Object> dataMap;

			for (int rowIndex = 1; rowIndex < rowCnt; rowIndex++) {
				row = sheet.getRow(rowIndex);
				if(row != null) {
					dataMap = new HashMap<>();
					dataMap.put("sessionMemberUid", memberVO.getMemberUid());
					dataMap.put("holidayDate", String.valueOf((int) row.getCell(0).getNumericCellValue()).replaceAll(" ", ""));
					dataMap.put("workYn", "N");
					dataMap.put("startTime", "0000");
					dataMap.put("endTime", "2359");
					// dataMap.put("workYn", String.valueOf(row.getCell(1)).replaceAll(" ", ""));
					// dataMap.put("startTime", String.valueOf(row.getCell(2)).replaceAll(" ", ""));
					// dataMap.put("endTime", String.valueOf(row.getCell(3)).replaceAll(" ", ""));
					dataMap.put("memo", row.getCell(1).getStringCellValue().replaceAll(" ", ""));
					dataList.add(dataMap);
				}
			}

			if (dataList.size() > 0) {

				String result = manageService.insertHolidayAll(dataList);
				if ("".equals(result)) {
					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
					rtnMap.put("rtnMsg", "정상적으로 저장 되었습니다.");
				} else {
					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_FAILURE);
					rtnMap.put("rtnMsg", result + "행이 등록되지 않았습니다.");
				}
			} else {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_FAILURE);
				rtnMap.put("rtnMsg", "등록된 내용이 없습니다.");
			}
		} catch(Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}




	/**
	 * 코끼리 관리 화면 조회
	 */
	@RequestMapping(value = "/selectUserGradeList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectUserGradeList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectUserGradeList: {}", param);

		try {
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int totPage = 1;
			int totCount = 0;

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);
			
			String schText = StringUtil.nvl(param.get("schText"));
			schText = StringEscapeUtils.escapeHtml4(schText);
			
			model.put("schText", schText);
			param.put("schText", schText);

			// 코끼리 등록 목록 조회
			List<Map<String, Object>> gradeList = manageService.selectCstmGradeList(param);
			model.put("gradeList", gradeList);

			if (gradeList != null && gradeList.size() > 0) {
				Map<String, Object> map = gradeList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}

			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "manage/userGradeList";
	}

	/**
	 * 코끼리 관리 조회 - 고객 1건
	 */
	@RequestMapping(value = "/selectUserGrade", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectUserGrade(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectUserGrade: {}", param);

		try {
			// 코끼리 등록 조회
			Map<String, Object> userGrade = manageService.selectCstmGrade(param);
			model.put("userGrade", userGrade);

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "manage/userGradePop";
	}

	/**
	 * 고객 등급 수정
	 */
	@RequestMapping(value = "/updateUserGrade", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateUserGrade(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("updateUserGrade: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());

			manageService.updateUserGrade(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

			// 로그 정보 등록
			String logCont = "코끼리 메모 수정 고객 번호 : " + StringUtil.nvl(param.get("cstmUid"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getMessage());
			//			//be.printStackTrace();
			//
			//			// 로그 정보 등록
			//			String logCont = "코끼리 메모 수정 고객 번호 : " + StringUtil.nvl(param.get("cstmUid"));
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "코끼리 메모 수정 고객 번호 : " + StringUtil.nvl(param.get("cstmUid"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 고객 등급 삭제
	 */
	@RequestMapping(value = "/deleteUserGrade", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteUserGrade(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());

			manageService.deleteUserGrade(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 삭제 되었습니다.");

			// 로그 정보 등록
			String logCont = "코끼리 삭제 고객 번호 : " + StringUtil.nvl(param.get("cstmUid"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getMessage());
			//			//be.printStackTrace();
			//
			//			// 로그 정보 등록
			//			String logCont = "코끼리 삭제 고객 번호 : " + StringUtil.nvl(param.get("cstmUid"));
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "코끼리 삭제 고객 번호 : " + StringUtil.nvl(param.get("cstmUid"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 로그 목록 조회
	 */
	@RequestMapping(value = "/selectLogList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectLogList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			//MemberVO memberVO = MemberAuthService.getCurrentUser();

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			int pageListCount = CommonConstants.PAGE_LOG_LIST_COUNT;
			int totPage = 1;
			int totCount = 0;

			//param.put("departCd", memberVO.getDepartCd());
			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);
			
			String schText = StringUtil.nvl(param.get("schText"));
			schText = StringEscapeUtils.escapeHtml4(schText);
			model.put("schText", schText);
			param.put("schText", schText);

			// 로그 목록 조회
			List<Map<String, Object>> logList = commonService.selectLogList(param);
			model.put("logList", logList);

			// 로그 구분 코드 목록
			List<Map<String, Object>> logDivCdList = commonService.selectCodeList(CommonConstants.COMM_CD_LOG_DIV_CD);
			model.put("logDivCdList", logDivCdList);

			if (logList != null && logList.size() > 0) {
				Map<String, Object> map = logList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}

			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "manage/logList";
	}

	/**
	 * 계정관리 회원 목록 조회
	 */
	@RequestMapping(value = "/selectMemberList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectMemberList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			log.info("memberVO: {}", memberVO);
			model.put("member", memberVO);

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int totPage = 1;
			int totCount = 0;

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);
			param.put("memberDivCd", memberVO.getMemberDivCd());
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("pageListYn", "Y");
			
			//슈퍼 어드민은 부서 상관없이 모든 회원 조회가 가능하도록 수정
			if (!CommonConstants.MEMBER_DIV_CD_S.equals(memberVO.getMemberDivCd())) {
				param.put("departCd", memberVO.getDepartCd());
			}
			param.put("schOpt", StringUtil.nvl(param.get("schOpt"), "A"));
			param.put("schType", StringUtil.nvl(param.get("schType"), "NAME"));
			
			String schText = StringUtil.nvl(param.get("schText"));
			schText = StringEscapeUtils.escapeHtml4(schText);
			model.put("schText", schText);
			param.put("schText", schText);

			// 회원 목록 조회
			List<Map<String, Object>> memberList = userService.selectMemberList(param);
			model.put("memberList", memberList);

			// 조건별 회원수 조회
			Map<String, Object> memberTypeCount = userService.selectMemberTypeCount(param);
			model.put("memberTypeCount", memberTypeCount);

			if (memberList != null && memberList.size() > 0) {
				Map<String, Object> map = memberList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}

			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);

			// 회원 목록 조회 - 관리자, 매니저 조회 - 팝업 셀렉트박스 용
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("managerList", "Y");
			param1.put("departList", "Y");
			
			//슈퍼어드민의 경우 부서 조건을 제외한다.
			if (!CommonConstants.MEMBER_DIV_CD_S.equals(memberVO.getMemberDivCd())) {
				param1.put("departCd", memberVO.getDepartCd());
			}
			List<Map<String, Object>> managerList = userService.selectMemberList(param1);
			List<Map<String, Object>> departList = userService.selectDepartList(param1);
			model.put("managerList", managerList);
			model.put("departList", departList);
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "manage/memberList";
	}

	/**
	 * 회원 관리
	 * 1. 배치작업으로 전체 회원 정보를 TB_TEMP_MEMBER_INFO에 등록
	 * 2. 회원 등록 시 해당 테이블의 회원 정보를 검색하여 존재 시 등록
	 * 3. 추가 배치작업 시 TB_TEMP_MEMBER_INFO 사번이 존재하지 않거나 이름, 부서가 다르면 인증 취소 처리.
	 * 4. 회원 인증 취소, 삭제 처리는 TB_TEMP_MEMBER_INFO 테이블과 상관없이 가능
	 * 5. 회원 인증 승인 시는 TB_TEMP_MEMBER_INFO의 사번, 이름, 부서가 동일해야지만 가능
	 * 6. 삭제된 회원이 재 등록시는 삭제여부 N으로 변경
	 *
	 * TB_TEMP_MEMBER_INFO 테이블의 필드값 확인.
	 * 부서목록은 별도로 오는건지 해당 테이블 정보를 가지고 만드는것인지?
	 * 배치 주기?
	 */

	/**
	 * 계정관리 회원 조회
	 */
	@RequestMapping(value = "/selectMember", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectMember(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 기본 설정 정보 조회
			Map<String, Object> defaultSet = settingService.selectDefaultSet();
			model.put("defaultSet", defaultSet);

			String ctgMgtDpt = StringUtil.nvl(defaultSet.get("ctg_mgt_dpt"), CommonConstants.DEFAULT_CTG_MGT_DPT);
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
//       		param.put("departCd", memberVO.getDepartCd());
			param.put("ctgMgtDpt", ctgMgtDpt);

			// 회원 조회
			if (!StringUtil.isEmpty(param.get("memberUid"))) {
				Map<String, Object> member = userService.selectMember(param);
				model.put("member", member);
			} else {
				model.put("depart_nm", memberVO.getDepartNm());
				model.put("depart_cd", memberVO.getDepartCd());
			}

			// 카테고리 정보
			List<Map<String, Object>> categoryList = categoryService.selectAllCategoryDpt21List(param);
			model.put("categoryList", categoryList);
			// 매핑된 분류정보
			List<Map<String, Object>> ctgMemberMapping = categoryService.selectCtgMemberMapping(param);
			model.put("ctgMemberMapping", ctgMemberMapping);
			model.put("rollType", param.get("rollType"));

			model.put("memberUid", param.get("memberUid"));

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "manage/memberPop";
	}

	/**
	 * 아이디 중복 체크
	 */
	@RequestMapping(value = "/checkIdDuplication", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> checkIdDuplication(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());

			Map<String, Object> useMap = userService.selectUserById(StringUtil.nvl(param.get("id")));

			if (useMap != null && useMap.get("id") != null) {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_FAILURE);
				rtnMap.put("rtnMsg", "등록된 아이디입니다. \n다른 아이디를 입력하세요.");
			} else {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
				rtnMap.put("rtnMsg", "등록 가능한 아이디 입니다.");
			}

		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "회원 등록 : " + StringUtil.nvl(param.get("name"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 기간계 계정 동기화 테이블 정보 조회
	 */
	@RequestMapping(value = "/selectTempMember", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectTempMember(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, String> param) {

		log.info("selectTempMember: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			String rtnMsg = "";
			param.put("searchText", param.get("searchText"));

			Map<String, String> userMap = manageService.selectTempMember(param);
			if(userMap == null) {
				rtnMsg = "사원 정보가 없습니다.";
				rtnMap.put("rtnMsg", rtnMsg);
			}
			else {
				rtnMap.put("data", userMap);
			}
		} catch (Exception e) {
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "회원 등록 : " + StringUtil.nvl(param.get("name"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 회원 등록
	 */
	@RequestMapping(value = "/insertMember", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> insertMember(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param,
			@RequestParam(value="ctgNumArr", required =false) String[] ctgNumArr)
	{
		log.info("insertMember: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		// 로그 등록 파라미터 시작
		String memberDivCdNm = "";
		String cnsPossibleYnNm = "";
		String uppMemNm = "";
		String id = "";
		String name = "";
		try {
			String memberDivCd = StringUtil.nvl(param.get("memberDivCd"));
			//String cnsPossibleYn = StringUtil.nvl(param.get("cnsPossibleYn"));

			if (CommonConstants.MEMBER_DIV_CD_S.equals(memberDivCd)) {
				memberDivCdNm = "슈퍼관리자";
			} else if (CommonConstants.MEMBER_DIV_CD_A.equals(memberDivCd)) {
				memberDivCdNm = "사이트관리자";
			} else if (CommonConstants.MEMBER_DIV_CD_M.equals(memberDivCd)) {
				memberDivCdNm = "매니저";
			} else if (CommonConstants.MEMBER_DIV_CD_C.equals(memberDivCd)) {
				memberDivCdNm = "상담직원";
				Map<String, Object> upperParam = new HashMap<String, Object>();
				upperParam.put("memberUid", param.get("upperMemberUid"));
				upperParam.put("departCd", memberVO.getDepartCd());
				Map<String, Object> upperMem = userService.selectMember(upperParam);
				uppMemNm = ", [담당매니저 : "+ upperMem.get("name").toString() + "]";
			}
			/*
			if ("Y".equals(cnsPossibleYn)) {
				cnsPossibleYnNm = "상담진행";
			} else if ("N".equals(cnsPossibleYnNm)) {
				cnsPossibleYnNm = "상담 업무 안함";
			}
			 */
			name = "이름 : " + StringUtil.nvl(param.get("name"));
			memberDivCdNm = ", 권한 : " + memberDivCdNm;
			id = ", 사번 : " + StringUtil.nvl(param.get("id"));

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}
		// 로그 등록 파라미터 끝

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());

			String memberUid = userService.insertMember(param);
			param.put("memberUid", memberUid);
			
			userService.updateCtgMemberMapping(param, ctgNumArr);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");

			// 로그 정보 등록
			String logCont = "회원 등록 : " + name + " (" + id + memberDivCdNm + cnsPossibleYnNm + uppMemNm + ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
		}  catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "회원 등록 : " + name + " (" + id + memberDivCdNm + cnsPossibleYnNm + uppMemNm + ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 회원 수정
	 */
	@RequestMapping(value = "/updateMember", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateMember(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param,
			@RequestParam(value="ctgNumArr", required =false) String[] ctgNumArr)
	{
		log.info("updateMember: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		// 로그 등록 파라미터 시작
		String memberDivCdNm = "";
		String cnsPossibleYnNm = "";
		String uppMemNm = "";
		String id = "";
		String name = "";
		try {
			String memberDivCd = StringUtil.nvl(param.get("memberDivCd"));
			//String cnsPossibleYn = StringUtil.nvl(param.get("cnsPossibleYn"));

			if (CommonConstants.MEMBER_DIV_CD_S.equals(memberDivCd)) {
				memberDivCdNm = "슈퍼관리자";
			} else if (CommonConstants.MEMBER_DIV_CD_A.equals(memberDivCd)) {
				memberDivCdNm = "사이트관리자";
			} else if (CommonConstants.MEMBER_DIV_CD_M.equals(memberDivCd)) {
				memberDivCdNm = "매니저";
			} else if (CommonConstants.MEMBER_DIV_CD_C.equals(memberDivCd)) {
				memberDivCdNm = "상담직원";
				Map<String, Object> upperParam = new HashMap<String, Object>();
				upperParam.put("memberUid", param.get("upperMemberUid"));
				upperParam.put("departCd", memberVO.getDepartCd());
				Map<String, Object> upperMem = userService.selectMember(upperParam);
				uppMemNm = ", [담당매니저 : "+ upperMem.get("name").toString() + "]";
			}

			/*		if ("Y".equals(cnsPossibleYn)) {
				cnsPossibleYnNm = "상담진행";
			} else if ("N".equals(cnsPossibleYn)) {
				cnsPossibleYnNm = "상담 업무 안함";
			}*/
			name = "이름 : " + StringUtil.nvl(param.get("name"));
			memberDivCdNm = ", 권한 : " + memberDivCdNm;
			id = ", 사번 : " + StringUtil.nvl(param.get("id"));

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}
		// 로그 등록 파라미터 끝


		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());

			userService.updateMember(param);
			userService.updateCtgMemberMapping(param, ctgNumArr);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

			// 사용자 계정 상세내역 세분화

			String logCont = "회원 수정 : " + name + " (" + id +  memberDivCdNm + cnsPossibleYnNm + uppMemNm + ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "회원 수정 : " + name + " (" + id + memberDivCdNm + cnsPossibleYnNm +  uppMemNm + ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 배정 수정
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/matchMemberCategory")
	public @ResponseBody Map<String, Object> matchMemberCategory(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param,
			@RequestParam(value="ctgNumArr", required =false) String[] ctgNumArr) {

		log.info("matchMemberCategory, param: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		param.put("creater", memberVO.getMemberUid());
		param.put("updater", memberVO.getMemberUid());

		userService.updateCtgMemberMapping(param, ctgNumArr);
		rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
		rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

		return rtnMap;
	}





	/**
	 * 비밀번호 변경 (로그인 된상태)
	 */
	@RequestMapping(value = "/changePasswd", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> changePasswd(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam(required = false, defaultValue = "false") Boolean withValidate,
			@RequestParam Map<String, Object> param,
			@RequestParam(value="ctgNumArr", required =false) String[] ctgNumArr){
		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());

			String rtnCd = userService.changePasswd(param, true, withValidate);
			log.info("rtnCd: {}", rtnCd);
			if (CommonConstants.RESULT_CD_SUCCESS.equals(rtnCd)) {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
				rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

				// 로그 정보 등록
				String logCont = "비밀번호 변경 : " + StringUtil.nvl(param.get("name")) + " (" + StringUtil.nvl(param.get("id"))
				+ ")";
				commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			} else {
				rtnMap.put("rtnCd", rtnCd);
				rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
				// 로그 정보 등록
				String logCont = "비밀번호 변경 : " + StringUtil.nvl(param.get("name")) + " (" + StringUtil.nvl(param.get("id"))
				+ ")";
				commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
			}
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			// 로그 정보 등록
			String logCont = "비밀번호 변경 : " + StringUtil.nvl(param.get("name")) + " (" + StringUtil.nvl(param.get("id"))
			+ ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());

		}
		return rtnMap;
	}




	/**
	 * 계정 활성, 회원 승인
	 */
	@RequestMapping(value = "/updateMemberValid", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateMemberValid(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) throws Exception {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		String memNm = "";
		String memUid = "";
		String memValid ="";
		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			userService.updateMemberValid(param);
			////사용내역 저장용
			memNm = "이름 : " + StringUtil.nvl(param.get("name"));
			memUid = "사번 : " + StringUtil.nvl(param.get("id"));
			memValid =("Y".equals(StringUtil.nvl(param.get("validYn")))) ? ", 계정활성" : ", 계정비활성";

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

			// 로그 정보 등록
			String logCont = "회원 승인 처리 : " + memNm + " (" + memUid+ memValid + ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
		} catch (BizException e) {
			log.error("{}", e.getLocalizedMessage(), e);
			rtnMap.put("rtnCd", e.getErrCode());
			rtnMap.put("rtnMsg", e.getMessage());

			// 로그 정보 등록
			String logCont = "회원 승인 처리 : " + memNm + " (" + memUid+ memValid + ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");

			// 로그 정보 등록
			String logCont = "회원 승인 처리 : " + memNm + " (" + memUid+ memValid + ")";

			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 회원 삭제
	 */
	@RequestMapping(value = "/deleteMember", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteMember(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());

			userService.deleteMember(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

			// 로그 정보 등록
			String logCont = "회원 삭제 : " + StringUtil.nvl(param.get("name")) + " (" + StringUtil.nvl(param.get("id"))
			+ ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "회원 삭제 : " + StringUtil.nvl(param.get("name")) + " (" + StringUtil.nvl(param.get("id"))
			+ ")";
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 계정관리 임시 회원 조회
	 */
	@RequestMapping(value = "/selectTmpMemberList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectTmpMemberList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			//MemberVO memberVO = MemberAuthService.getCurrentUser();

			List<Map<String, Object>> tmpMemberList = userService.selectTmpMemberList(param);
			model.put("tmpMemberList", tmpMemberList);
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return "manage/memberPopTmpList";
	}

	@RequestMapping(value = "/batchMember", method = { RequestMethod.GET })
	public @ResponseBody Map<String, Object> batchMember(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		String flagText = "";
		String logCont = "";

		boolean check = false;

		boolean checkMember = false;
		File memberFile = null;
		try {
			memberFile = new File(customProperty.getMemberFile());
			checkMember = memberFile.exists();
		}catch(Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		boolean checkMemberFp = false;
		File memberFileFp = null;
		try {
			memberFileFp = new File(customProperty.getMemberFileFp());
			checkMemberFp = memberFileFp.exists();
		}catch(Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		boolean checkMemberCnsr = false;
		File memberFileCnsr = null;
		try {
			memberFileCnsr = new File(customProperty.getMemberFileCnsr());
			checkMemberCnsr = memberFileCnsr.exists();
		}catch(Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		if (checkMember && checkMemberFp && checkMemberCnsr) {
			check = commonService.checkBatchStart(CommonConstants.BATCH_JOB_MEMBER, "DAY");
			if(!check) {
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
			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage(), e);
			}
			commonService.checkBatchEnd(CommonConstants.BATCH_JOB_MEMBER, "NOT_DAY");
		} else {
			// 로그 정보 등록
			commonService.insertLog(CommonConstants.LOG_DIV_CD_E, flagText, logCont,
					customProperty.getSystemMemeberUid());
		}

		rtnMap.put("flagText", flagText);
		rtnMap.put("logCont", logCont);

		return rtnMap;
	}

	@RequestMapping(value = "/batchStatistics", method = { RequestMethod.GET })
	public @ResponseBody Map<String, Object> batchStatistics(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		try {

			reportService.insertStatsDate(param); // Stats_date 저장
			reportService.insertStatsCnsr(param); // stats_cnsr 저장
			reportService.insertStatsTime(param); // stats_time 저장
			reportService.insertStatsCnsrTime(param); // stats_cnsr_time 저장
			reportService.insertStatsCtgDate(param); // stats_ctg_date저장

			rtnMap.put("result", CommonConstants.RESULT_CD_SUCCESS);
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
			rtnMap.put("result", CommonConstants.RESULT_CD_ERROR);
		}

		return rtnMap;
	}

}
