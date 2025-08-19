package ht.service;

import static ht.constants.CommonConstants.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.constants.CommonConstants;
import ht.exception.BizException;
import ht.persistence.CommonDao;
import ht.persistence.ManageDao;
import ht.persistence.MemberDao;
import ht.persistence.SettingDao;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Service
@Slf4j
public class ManageService {

	@Resource
	private ManageDao manageDao;
	@Resource
	private MemberDao memberDao;
	@Resource
	private CommonDao commonDao;
	@Resource
	private SettingDao settingDao;



	@Resource
	private SettingService settingService;

	/**
	 * 해당 년도의 휴일 목록 조회
	 */
	public List<Map<String, Object>> selectHolidayList(String year) {
		List<Map<String, Object>> resultList = manageDao.selectHolidayList(year);
		
		for(Map<String, Object> map : resultList) {
			map.put("memo", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(map.get("memo"), "")) );
		}

		return resultList;
	}

	/**
	 * 해당 월의 휴일 목록 조회
	 */
	public List<Map<String, Object>> selectHolidayMonthList(String year, String month) {
		String yearMonth = year + month;
		return manageDao.selectHolidayMonthList(yearMonth);
	}

	/**
	 * 해당월의 달력
	 */
	public List<Map<String, Object>> selectMonthCalendar(String year, String schMonth) {
		if (schMonth.length() < 2) {
			schMonth = "0" + schMonth;
		}
		String yearMonth = year + schMonth;
		return manageDao.selectMonthCalendar(yearMonth);
	}

	/**
	 * 해당월의 스케줄 조회
	 */
	public List<Map<String, Object>> selectMonthSchList(Map<String, Object> param) {
		return manageDao.selectMonthSchList(param);
	}

	/**
	 * 휴일 등록
	 */
	@Transactional
	public void insertHoliday(Map<String, Object> param) throws Exception {
		// 등록된 휴일인지 확인.
		Map<String, Object> holidayMap = manageDao.selectHoliday(param);

		if (holidayMap != null && holidayMap.get("holiday_num") != null) {
			throw new BizException(CommonConstants.RESULT_CD_FAILURE, "이미 등록된 휴일 입니다.");
		}
		
		param.put("memo", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("memo"), "")) );

		// 휴일 등록
		manageDao.insertHoliday(param);

		// 해당일자의 스케줄 삭제
		manageDao.deleteHolidaySchedule(param);

		// 해당일자의 스케줄 등록
		manageDao.insertHolidaySchedule(param);
	}

	/**
	 * 휴일 일괄 등록
	 */
	@Transactional
	public String insertHolidayAll(List<Map<String, Object>> dataList) {

		String rtnMsg = "";
		int idx = 0;

		for(Map<String, Object> param : dataList) {
			try {
				// 등록된 휴일인지 확인.
				Map<String, Object> holidayMap = manageDao.selectHoliday(param);
				
				param.put("memo", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("memo"), "")) );
				
				if (holidayMap == null || holidayMap.get("holiday_num") == null) {
					// 휴일 등록
					manageDao.insertHoliday(param);
					// 해당일자의 스케줄 삭제
					manageDao.deleteHolidaySchedule(param);
					// 해당일자의 스케줄 등록
					manageDao.insertHolidaySchedule(param);
				}
				idx++;

			} catch (Exception e) {
				HTUtils.batmanNeverDie(e);
				if (!"".equals(rtnMsg)) {
					rtnMsg += ", ";
				}
				rtnMsg += idx;
			}
		}
		return rtnMsg;
	}

	/**
	 * 휴일 삭제
	 */
	@Transactional
	public void deleteHoliday(List<String> delHolidayDateList) {
		Map<String, Object> param = new HashMap<>();
		param.put("delHolidayDateList", delHolidayDateList);

		// 휴일 삭제
		manageDao.deleteHoliday(param);

		if (delHolidayDateList != null && delHolidayDateList.size() > 0) {
			for (String schDate : delHolidayDateList) {
				int chkDate = commonDao.selectNowDateCheck(schDate);
				// 오늘 포함 이후만 등록
				if (chkDate > 0) {
					param.put("holidayDate", schDate);
					// 해당일자의 스케줄 삭제
					manageDao.deleteHolidaySchedule(param);

					// 해당일자의 스케줄 등록
					// IPCC_MCH 휴일삭제 오류 수정
					param.put("channel", "A");
					manageDao.insertHolidaySchedule(param);
				}
			}
		}
	}

	/**
	 * 매니저의 상담원 목록
	 */
	public List<Map<String, Object>> selectCounselorHoliday(Map<String, Object> param) {
		return manageDao.selectCounselorHoliday(param);
	}

	/**
	 * 매니저의 상담원 목록(상담최대건수)
	 */
	public List<Map<String, Object>> selectChatCntSetting(Map<String, Object> param) {
		return manageDao.selectChatCntSetting(param);
	}


	/**
	 * 해당일의 근무여부 체크
	 */
	public Map<String, Object> selectWorkYnCheck(String schDate, String channel) {
		Map<String, Object> param = new HashMap<>();
		param.put("schDate", schDate);
		param.put("cstmLinkDivCd", channel);
		return manageDao.selectWorkYnCheck(param);
	}

	/**
	 * 스케줄 등록
	 */
	@Transactional
	public void insertSchedule(Map<String, Object> param) {
		
		//부서별 스케줄 관리 기존 cs 컬럼을 사용
		if (!StringUtil.isEmpty(param.get("departCd"))) {
			settingDao.updateSetWorkTime(param);
//			String departCd = (String) param.get("departCd");
//			if (DEPART_CD_TM.equals(departCd)) {
//				settingDao.updateTMSetWorkTime(param);
//			} else {
//				settingDao.updateCSSetWorkTime(param);
//			}
		}

		manageDao.deleteSchedule(param);
		manageDao.insertSchedule(param);
	}

	/**
	 * 스케줄 삭제
	 */
	@Transactional
	public void deleteSchedule(Map<String, Object> param) {
		manageDao.deleteSchedule(param);
	}

	/**
	 * 상담원 휴일 설정 변경 - 근무여부
	 */
	@Transactional
	public void updateCnsrHolidayWorkYn(Map<String, Object> param) {
		manageDao.updateCnsrHolidayWorkYn(param);
	}

	/**
	 * 상담원 휴일 설정 변경 - 근무시간
	 */
	@Transactional
	public void updateCnsrHolidayTime(Map<String, Object> param) {
		manageDao.updateCnsrHolidayTime(param);
	}


	/**
	 * 개별 상담수 update
	 */
	@Transactional
	public void updateChatCntSetting(Map<String, Object> param) {
		manageDao.updateChatCntSetting(param);
	}

	/**
	 * 코끼리 등록 목록 조회
	 */
	public List<Map<String, Object>> selectCstmGradeList(Map<String, Object> param) {
		return manageDao.selectCstmGradeList(param);
	}

	/**
	 * 코끼리 관리 조회 - 고객 1건
	 */
	public Map<String, Object> selectCstmGrade(Map<String, Object> param) {
		return manageDao.selectCstmGrade(param);
	}

	/**
	 * 고객 등급 수정
	 */
	@Transactional
	public void updateUserGrade(Map<String, Object> param) {
		manageDao.updateUserGrade(param);
	}

	/**
	 * 고객 등급 삭제
	 */
	@Transactional
	public void deleteUserGrade(Map<String, Object> param) {
		manageDao.deleteUserGrade(param);
	}

	/**
	 * 고객사 아이디 업데이드
	 */
	@Transactional
	public void updateUserCocId(Map<String, Object> param) {
		manageDao.updateUserCocId(param);
	}

	/**
	 * 고객사 아이디 업데이드
	 */
	@Transactional
	public void updateRoomCocId(Map<String, Object> param) {
		manageDao.updateRoomCocId(param);
	}

	/**
	 * 고객사 아이디 호출
	 */
	@Transactional
	public String selectUserCocId(Map<String, Object> param) {
		Map<String, Object> rs = new HashMap<>();
		rs = manageDao.selectUserCocId(param);

		if (rs!= null && rs.containsKey("coc_id") && String.valueOf(rs.get("coc_id")) != "null") {
			return String.valueOf(rs.get("coc_id"));
		} else {
			return "";
		}
	}

	public Map<String, Object> selectMemberByMemberUid(String memberUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("memberUid", memberUid);
		params.put("leaveYn", "N");
		params.put("validYn", "Y");
		return memberDao.selectCMember(params);
	}

	public Map<String, Object> selectMemberByCocId(String cocId) {

		Map<String, Object> params = new HashMap<>();
		params.put("cocId", cocId);
		params.put("leaveYn", "N");
		params.put("validYn", "Y");
		return memberDao.selectCMember(params);
	}

	public boolean isMemberOffDuty(String memberUid) {

		throw new UnsupportedOperationException();
	}

	public boolean isMemberBreakTime(String memberUid) {

		throw new UnsupportedOperationException();
	}

	public List<Map<String, Object>> selectCounselorList() {

		Map<String, Object> params = new HashMap<>();
		params.put("cnsPossibleYn", 'Y');
		params.put("leaveYn", "N");
		params.put("validYn", "Y");
		return memberDao.selectCMemberList(params);
	}

	public List<Map<String, Object>> selectAvailableCounselorList(List<Map<String, Object>> memberList) {

		Map<String, Object> params = new HashMap<>();
		params.put("memberList", memberList);
		return memberDao.selectAvailableCounselorList(params);
	}

	public List<Map<String, Object>> selectMemberUidAndAssignCount(List<Map<String, Object>> memberList) {

		// 상담자별 할당 채팅방 개수
		Map<String, Object> params = new HashMap<>();
		params.put("memberList", memberList);
		return memberDao.selectMemberUidAndAssignCount(params);
	}

	public List<Map<String, Object>> selectCounselorListByMinimumAssigned(List<Map<String, Object>> memberList) {

		// 상담자별 할당 채팅방 개수
		Map<String, Object> params = new HashMap<>();
		params.put("memberList", memberList);
		List<Map<String, Object>> list = memberDao.selectMemberUidAndAssignCount(params);

		// 상담원이 할당된 채팅방이 없는 경우, 전체 상담원(memberList) 리턴
		if (list.isEmpty()) {
			return memberList;
		}

		// 할당 채팅방 개수 중 최소값
		long minimumCount = Long.MAX_VALUE;
		for (Map<String, Object> map : list) {
			log.info("{} ({})", map.get("name"), map.get("assign_count"));
			BigDecimal assignCount = (BigDecimal) map.get("assign_count");
			if (assignCount.longValue() < minimumCount) {
				minimumCount = assignCount.longValue();
			}
		}
		log.debug("MIN ASSIGN NUM: {}", minimumCount);

		// 최소값으로 할당 중인 상담원
		List<Map<String, Object>> availableCounselorList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			BigDecimal assignCount = (BigDecimal) map.get("assign_count");
			if (assignCount.longValue() == minimumCount) {
				log.info("{} ({})", map.get("name"), map.get("assign_count"));
				availableCounselorList.add(map);
			}
		}

		return availableCounselorList;
	}

	@Transactional
	public int saveMemberSetting(String memberUid, Map<String, Object> requestParams) {

		// 상담원 휴식시간 이력 저장
		if (requestParams.get("workStatusCd") != null) {
			if (WORK_STATUS_CD_W.equals(requestParams.get("workStatusCd"))) {
				settingService.insertCnsrBreakHis(memberUid, memberUid, WORK_STATUS_CD_W);
			} else {
				settingService.insertCnsrBreakHis(memberUid, memberUid, WORK_STATUS_CD_R);
			}
		}
		return memberDao.saveMemberSetting(requestParams);
	}

	@Transactional
	public String createCstmUid() {

		return UUID.randomUUID().toString();
	}

	public Map<String, Object> selectCustomerByCstmUid(String cstmUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", cstmUid);
		return memberDao.selectCustomer(params);
	}

	public Map<String, Object> selectCustomerByCocId(String cocId) {

		Map<String, Object> params = new HashMap<>();
		params.put("cocId", cocId);
		return memberDao.selectCustomer(params);
	}

	/**
	 * 기간계 계정 동기화 테이블 정보 조회
	 */
	public Map<String, String> selectTempMember(Map<String, String> param) {

		return memberDao.selectTempMember(param);
	}

}
