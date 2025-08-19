package ht.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.persistence.ReportDao;

@Service
public class ReportService {

	@Resource
	private ReportDao reportDao;

	/**
	 * 전체상담
	 */
	public List<Map<String, Object>> selectTotalReport(Map<String, Object> param){
		return reportDao.selectTotalReport(param);
	}

	/**
	 * 대시보드 분류별 대기 진행 조회
	 */
	public List<Map<String, Object>> selectCTGWaitIng(Map<String, Object> param){
		return reportDao.selectCTGWaitIng(param);
	}
	/**
	 * 대시보드 상담원별 처리 현황
	 */
	public List<Map<String, Object>> selectCnsrReport(Map<String, Object> param){
		return reportDao.selectCnsrReport(param);
	}
	
	/**
	 * 대시보드 일 기본 조회 첫번째 줄
	 */
	public List<Map<String, Object>> selectTodayTotalReport(Map<String, Object> param){
		return reportDao.selectTodayTotalReport(param);
	}
	/**
	 * 대시보드 일 기본 조회 두번째 줄
	 */
	public List<Map<String, Object>> selectLinkDivReport(Map<String, Object> param){
		return reportDao.selectLinkDivReport(param);
	}
	
	/**
	 * 대시보드 분류별 후처리 조회
	 */
	public List<Map<String, Object>> selectCTGafter(Map<String, Object> param){
		return reportDao.selectCTGAfter(param);
	}

	/**
	 * 대시보드 mci 처리 현황
	 */
	public List<Map<String, Object>> selectMCICnt(Map<String, Object> param){
		return reportDao.selectMCICnt(param);
	}
	

	/**
	 * DB 시간 가져오기
	 */
	public String selectNowTime(){
		return reportDao.selectNowTime();
	}

	
	
	
	
	
	
	
	
	/**
	 * 대시보드 어제 분류별 조회
	 */
	public List<Map<String, Object>> selectLinkDivReportYesterday(Map<String, Object> param){
		return reportDao.selectLinkDivReportYesterday(param);
	}

	
	
	/**
	 * 대시보드 카테고리 조회
	 */
	public List<Map<String, Object>> selectCtgReport(Map<String, Object> param){
		return reportDao.selectCtgReport(param);
	}

	/**
	 * 대시보드 어제 카테고리 조회
	 */
	public List<Map<String, Object>> selectCtgReportYesterday(Map<String, Object> param){
		return reportDao.selectCtgReportYesterday(param);
	}

	

	/**
	 * 대시보드 월 분류별 조회
	 */
	public List<Map<String, Object>> selectMonthlyLinkDivReport(Map<String, Object> param){
		return reportDao.selectMonthlyLinkDivReport(param);
	}

	

	/**
	 * 대시보드 상담원별 어제 처리 현황
	 */
	public List<Map<String, Object>> selectTotalGroupReportYesterday(Map<String, Object> param){
		return reportDao.selectTotalGroupReportYesterday(param);
	}
	
	

	/**
	 * 일배치 TB_STATS_DATE
	 */
	@Transactional
	public void insertStatsDate(Map<String, Object> param) {
		reportDao.insertStatsDate(param);
	}

	/**
	 * 일배치 TB_STATS_CNSR
	 */
	@Transactional
	public void insertStatsCnsr(Map<String, Object> param) {
		reportDao.insertStatsCnsr(param);
	}

	/**
	 * 일배치 TB_STATS_TIME
	 */
	@Transactional
	public void insertStatsTime(Map<String, Object> param) {
		reportDao.insertStatsTime(param);
	}

	/**
	 * 일배치 TB_STATS_CNSR_TIME
	 */
	@Transactional
	public void insertStatsCnsrTime(Map<String, Object> param) {
		reportDao.insertStatsCnsrTime(param);
	}

	/**
	 * 일배치 TB_STATS_ROBOT
	 */
	@Transactional
	public void insertStatsRobot(Map<String, Object> param) {
		reportDao.insertStatsRobot(param);
	}

	/**
	 * 일배치 TB_STATS_LINK_ROBOT
	 */
	@Transactional
	public void insertStatsLinkRobot(Map<String, Object> param) {
		reportDao.insertStatsLinkRobot(param);
	}

	
	/**
	 * 일배치 TB_STATS_CNSR_INTENT
	 */
	@Transactional
	public void insertStatsIntent(Map<String, Object> param) {
		reportDao.insertStatsIntent(param);
	}

	/**
	 * 일배치 TB_STATS_CTG_DATE
	 */
	@Transactional
	public void insertStatsCtgDate(Map<String, Object> param) {
		reportDao.insertStatsCtgDate(param);
	}
	/**
	 * 일배치 TB_STATS_SQI
	 */
	@Transactional
	public void insertSQILIst(Map<String, Object> param) {
		reportDao.insertSQILIst(param);
	}
	
	
	/* *************************** 수동삭제 ****************************** */
	/**
	 * 일배치 TB_STATS_DATE
	 */
	@Transactional
	public int deleteStatsDate(Map<String, Object> param) {
		return reportDao.deleteStatsDate(param);
	}

	/**
	 * 일배치 TB_STATS_CNSR
	 */
	@Transactional
	public int deleteStatsCnsr(Map<String, Object> param) {
		return reportDao.deleteStatsCnsr(param);
	}

	/**
	 * 일배치 TB_STATS_TIME
	 */
	@Transactional
	public int deleteStatsTime(Map<String, Object> param) {
		return reportDao.deleteStatsTime(param);
	}

	/**
	 * 일배치 TB_STATS_CNSR_TIME
	 */
	@Transactional
	public int deleteStatsCnsrTime(Map<String, Object> param) {
		return reportDao.deleteStatsCnsrTime(param);
	}

	/**
	 * 일배치 TB_STATS_ROBOT
	 */
	@Transactional
	public int deleteStatsRobot(Map<String, Object> param) {
		return reportDao.deleteStatsRobot(param);
	}

	/**
	 * 일배치 TB_STATS_LINK_ROBOT
	 */
	@Transactional
	public int deleteStatsLinkRobot(Map<String, Object> param) {
		return reportDao.deleteStatsLinkRobot(param);
	}


	/**
	 * 일배치 TB_STATS_CNSR_INTENT
	 */
	@Transactional
	public int deleteStatsIntent(Map<String, Object> param) {
		return reportDao.deleteStatsIntent(param);
	}

	/**
	 * 일배치 TB_STATS_CTG_DATE
	 */
	@Transactional
	public int deleteStatsCtgDate(Map<String, Object> param) {
		return reportDao.deleteStatsCtgDate(param);
	}
	/**
	 * 일배치 TB_STATS_SQI
	 */
	@Transactional
	public int deleteSQILIst(Map<String, Object> param) {
		return reportDao.deleteSQILIst(param);
	}
}
