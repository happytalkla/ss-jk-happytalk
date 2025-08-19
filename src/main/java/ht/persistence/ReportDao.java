package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 대시보드 기본 조회
	 */
	public List<Map<String, Object>> selectTotalReport(Map<String, Object> param){
		return sqlSession.selectList("report.selectTotalReport", param);
	}
	/**
	 * 대시보드 카테고리별 대기 진행 조회
	 */
	public List<Map<String, Object>> selectCTGWaitIng(Map<String, Object> param){
		return sqlSession.selectList("report.selectCTGWaitIng", param);
	}

	/**
	 * 대시보드 상담원별 처리 현황
	 */
	public List<Map<String, Object>> selectCnsrReport(Map<String, Object> param){
		return sqlSession.selectList("report.selectCnsrReport", param);
	}

	/**
	 * 대시보드 일 기본 조회
	 */
	public List<Map<String, Object>> selectTodayTotalReport(Map<String, Object> param){
		return sqlSession.selectList("report.selectTodayTotalReport", param);
	}

	/**
	 * 대시보드 일 기본 조회 두번째 줄
	 */
	public List<Map<String, Object>> selectLinkDivReport(Map<String, Object> param){
		return sqlSession.selectList("report.selectLinkDivReport", param);
	}
	/**
	 * 대시보드 카테고리별 after 조회
	 */
	public List<Map<String, Object>> selectCTGAfter(Map<String, Object> param){
		return sqlSession.selectList("report.selectCTGAfter", param);
	}
	
	/**
	 * 대시보드 mci 처리 현황
	 */
	public List<Map<String, Object>> selectMCICnt(Map<String, Object> param){
		return sqlSession.selectList("report.selectMCICnt", param);
	}
	
	/**
	 * DB 시간 가져오기
	 */
	public String selectNowTime(){
		return sqlSession.selectOne("report.selectNowTime");
	}
	
	
	
	
	
		
	

	/**
	 * 대시보드 어제 분류별 조회
	 */
	public List<Map<String, Object>> selectLinkDivReportYesterday(Map<String, Object> param){
		return sqlSession.selectList("report.selectLinkDivReportYesterday", param);
	}
	
	/**
	 * 대시보드 카테고리 조회
	 */
	public List<Map<String, Object>> selectCtgReport(Map<String, Object> param){
		return sqlSession.selectList("report.selectCtgReport", param);
	}

	/**
	 * 대시보드 어제 카테고리 조회
	 */
	public List<Map<String, Object>> selectCtgReportYesterday(Map<String, Object> param){
		return sqlSession.selectList("report.selectCtgReportYesterday", param);
	}

	
	/**
	 * 대시보드 월 분류별 조회
	 */
	public List<Map<String, Object>> selectMonthlyLinkDivReport(Map<String, Object> param){
		return sqlSession.selectList("report.selectMonthlyLinkDivReport", param);
	}


	/**
	 * 대시보드 상담원별 어제 처리 현황
	 */
	public List<Map<String, Object>> selectTotalGroupReportYesterday(Map<String, Object> param){
		return sqlSession.selectList("report.selectTotalGroupReportYesterday", param);
	}
	
	

	

	/**
	 * 일배치 TB_STATS_DATE
	 */
	public void insertStatsDate(Map<String, Object> param) {
		sqlSession.insert("report.insertStatsDate", param);
	}

	/**
	 * 일배치 TB_STATS_CNSR
	 */
	public void insertStatsCnsr(Map<String, Object> param) {
		sqlSession.insert("report.insertStatsCnsr", param);
	}

	/**
	 * 일배치 TB_STATS_TIME
	 */
	public void insertStatsTime(Map<String, Object> param) {
		sqlSession.insert("report.insertStatsTime", param);
	}

	/**
	 * 일배치 TB_STATS_CNSR_TIME
	 */
	public void insertStatsCnsrTime(Map<String, Object> param) {
		sqlSession.insert("report.insertStatsCnsrTime", param);
	}

	/**
	 * 일배치 TB_STATS_ROBOT
	 */
	public void insertStatsRobot(Map<String, Object> param) {
		sqlSession.insert("report.insertStatsRobot", param);
	}
	
	/**
	 * 일배치 TB_STATS_LINK_ROBOT
	 */
	public void insertStatsLinkRobot(Map<String, Object> param) {
		sqlSession.insert("report.insertStatsLinkRobot", param);
	}

	/**
	 * 일배치 TB_STATS_INTENT
	 */
	public void insertStatsIntent(Map<String, Object> param) {
		sqlSession.insert("report.insertStatsIntent", param);
	}

	/**
	 * 일배치 TB_STATS_CTG_DATE
	 */
	public void insertStatsCtgDate(Map<String, Object> param) {
		sqlSession.insert("report.insertStatsCtgDate", param);
	}
	
	/**
	 * 일배치 TB_STATS_SQI
	 */
	public void insertSQILIst(Map<String, Object> param) {
		sqlSession.insert("report.insertSQILIst", param);
	}

	
	/* ===================수동배치============================ */
	/**
	 * 일배치 TB_STATS_DATE
	 */
	public int deleteStatsDate(Map<String, Object> param) {
		return sqlSession.insert("report.deleteStatsDate", param);
	}

	/**
	 * 일배치 TB_STATS_CNSR
	 */
	public int deleteStatsCnsr(Map<String, Object> param) {
		return sqlSession.insert("report.deleteStatsCnsr", param);
	}

	/**
	 * 일배치 TB_STATS_TIME
	 */
	public int deleteStatsTime(Map<String, Object> param) {
		return sqlSession.insert("report.deleteStatsTime", param);
	}

	/**
	 * 일배치 TB_STATS_CNSR_TIME
	 */
	public int deleteStatsCnsrTime(Map<String, Object> param) {
		return sqlSession.insert("report.deleteStatsCnsrTime", param);
	}

	/**
	 * 일배치 TB_STATS_ROBOT
	 */
	public int deleteStatsRobot(Map<String, Object> param) {
		return sqlSession.insert("report.deleteStatsRobot", param);
	}

	/**
	 * 일배치 TB_STATS_LINK_ROBOT
	 */
	public int deleteStatsLinkRobot(Map<String, Object> param) {
		return sqlSession.insert("report.deleteStatsLinkRobot", param);
	}

	/**
	 * 일배치 TB_STATS_INTENT
	 */
	public int deleteStatsIntent(Map<String, Object> param) {
		return sqlSession.insert("report.deleteStatsIntent", param);
	}

	/**
	 * 일배치 TB_STATS_CTG_DATE
	 */
	public int deleteStatsCtgDate(Map<String, Object> param) {
		return sqlSession.insert("report.deleteStatsCtgDate", param);
	}

	/**
	 * 일배치 TB_STATS_SQI
	 */
	public int deleteSQILIst(Map<String, Object> param) {
		return sqlSession.insert("report.deleteSQILIst", param);
	}
}
