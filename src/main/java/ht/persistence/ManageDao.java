package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;

import ht.constants.CommonConstants;
import ht.util.StringUtil;

@Repository
public class ManageDao {

	@Resource
	private SqlSession sqlSession;


	/**
	 * 해당년도의 휴일 목록 조회
	 */
	public List<Map<String, Object>> selectHolidayList(String year) {
		return sqlSession.selectList("manage.selectHolidayList", year);
	}

	/**
	 * 해당년도의 휴일 목록 조회
	 */
	public List<Map<String, Object>> selectHolidayMonthList(String yearMonth) {
		return sqlSession.selectList("manage.selectHolidayMonthList", yearMonth);
	}

	/**
	 * 해당월의 달력
	 */
	public List<Map<String, Object>> selectMonthCalendar(String yearMonth) {
		return sqlSession.selectList("manage.selectMonthCalendar", yearMonth);
	}

	/**
	 * 해당월의 스케줄 조회
	 */
	public List<Map<String, Object>> selectMonthSchList(Map<String, Object> param) {
		return sqlSession.selectList("manage.selectMonthSchList", param);
	}

	/**
	 * 휴일 조회
	 */
	public Map<String, Object> selectHoliday(Map<String, Object> param) {
		return sqlSession.selectOne("manage.selectHoliday", param);
	}

	/**
	 * 휴일 등록
	 */
	public void insertHoliday(Map<String, Object> param) {
		sqlSession.insert("manage.insertHoliday", param);
	}

	/**
	 * 휴일 삭제
	 */
	public void deleteHoliday(Map<String, Object> param) {
		sqlSession.delete("manage.deleteHoliday", param);
	}

	/**
	 * 휴일의 스케줄 삭제
	 */
	public void deleteHolidaySchedule(Map<String, Object> param) {
		sqlSession.delete("manage.deleteHolidaySchedule", param);
	}

	/**
	 * 휴일의 스케줄 등록
	 */
	public void insertHolidaySchedule(Map<String, Object> param) {
		sqlSession.delete("manage.insertHolidaySchedule", param);
	}

	/**
	 * 매니저의 상담원 목록
	 */
	public List<Map<String, Object>> selectCounselorHoliday(Map<String, Object> param) {
		return sqlSession.selectList("manage.selectCounselorHoliday", param);
	}
	
	/**
	 * 매니저의 상담원 목록(상담최대건수)
	 */
	public List<Map<String, Object>> selectChatCntSetting(Map<String, Object> param) {
		return sqlSession.selectList("manage.selectChatCntSetting", param);
	}	
	

	/**
	 * 해당일의 근무 여부 조회
	 */
	public Map<String, Object> selectWorkYnCheck(Map<String, Object> param) {
		
		return sqlSession.selectOne("manage.selectWorkYnCheck", param);
	}

	/**
	 * 스케줄 등록 - CHANNEL : A
	 * 기본 A, 나머지는 받아와서 등록
	 */
	public void insertSchedule(Map<String, Object> param) {
		param.put("channel", StringUtil.nvl(param.get("channel"), CommonConstants.CSTM_LINK_DIV_CD_A));
		sqlSession.delete("manage.insertSchedule", param);
	}
	/**
	 * 스케줄 등록 - CHANNEL : B
	 */
	public void insertBSchedule(Map<String, Object> param) {
		param.put("channel", "B");
		sqlSession.delete("manage.insertSchedule", param);
	}
	/**
	 * 스케줄 등록 - CHANNEL : C
	 */
	public void insertCSchedule(Map<String, Object> param) {
		param.put("channel", "C");
		sqlSession.delete("manage.insertSchedule", param);
	}
	/**
	 * 스케줄 등록 - CHANNEL : D
	 */
	public void insertDSchedule(Map<String, Object> param) {
		param.put("channel", "D");
		sqlSession.delete("manage.insertSchedule", param);
	}
	/*
	 * IPCC_MCH ARS 스케줄 등록 - CHANNEL : E
	 */
	public void insertESchedule(Map<String, Object> param) {
		param.put("channel", CommonConstants.CSTM_LINK_DIV_CD_E);
		sqlSession.delete("manage.insertSchedule", param);
	}

	/**
	 * 오늘 이후 스케줄 삭제
	 */
	public void deleteAllSchedule() {
		sqlSession.delete("manage.deleteAllSchedule");
	}

	/**
	 * 스케줄 삭제
	 */
	public void deleteSchedule(Map<String, Object> param) {
		sqlSession.insert("manage.deleteSchedule", param);
	}

	/**
	 * 상담원 휴일 설정 변경 - 근무여부
	 */
	public void updateCnsrHolidayWorkYn(Map<String, Object> param) {
		sqlSession.update("manage.updateCnsrHolidayWorkYn", param);
	}

	/**
	 * 상담원 휴일 설정 변경 - 근무시간
	 */
	public void updateCnsrHolidayTime(Map<String, Object> param) {
		sqlSession.update("manage.updateCnsrHolidayTime", param);
	}
	
	/**
	 * 개별 상담수 update
	 */
	public void updateChatCntSetting(Map<String, Object> param) {
		sqlSession.update("manage.updateChatCntSetting", param);
	}

	/**
	 * 코끼리 등록 목록 조회
	 */
	public List<Map<String, Object>> selectCstmGradeList(Map<String, Object> param) {
		return sqlSession.selectList("manage.selectCstmGradeList", param);
	}

	/**
	 * 코끼리 관리 조회 - 고객 1건
	 */
	public Map<String, Object> selectCstmGrade(Map<String, Object> param) {
		return sqlSession.selectOne("manage.selectCstmGrade", param);
	}

	/**
	 * 고객 등급 수정
	 */
	public void updateUserGrade(Map<String, Object> param) {
		sqlSession.update("manage.updateUserGrade", param);
	}

	/**
	 * 고객 등급 삭제
	 */
	public void deleteUserGrade(Map<String, Object> param) {
		sqlSession.update("manage.deleteUserGrade", param);
	}
	
	/**
	 * 고객사 아이디 업데이트
	 */
	public void updateUserCocId(Map<String, Object> param) {
		sqlSession.update("manage.updateUserCocId", param);
	}
	
	/**
	 * 고객사 아이디 업데이트 (룸)
	 */
	public void updateRoomCocId(Map<String, Object> param) {
		sqlSession.update("manage.updateRoomCocId", param);
	}

	/**
	 * 고객사 아이디 호출
	 */
	public Map<String, Object> selectUserCocId(Map<String, Object> param) {
		return sqlSession.selectOne("manage.selectUserCocId", param);
	}
}
