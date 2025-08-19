package ht.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import ht.persistence.ReportingDao;

@Service
public class ReportingService {

	@Resource
	private ReportingDao reportingDao;

	/**
	 * Monthly 통계 기본 조회
	 */
	public List<Map<String, Object>> selectMonthlyBasicStatcs(Map<String, Object> param){
		return reportingDao.selectMonthlyBasicStatcs(param);
	}
	
	/**
	 * Monthly 통계 기본 조회 - kakao
	 */
	public Map<String, Object> selectMonthlyBasicKakao(Map<String, Object> param){
		return reportingDao.selectMonthlyBasicKakao(param);
	}

	/**
	 * Monthly 일자별 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyDayStatcs(Map<String, Object> param){
		return reportingDao.selectMonthlyDayStatcs(param);
	}

	/**
	 * Monthly 요일별 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyWeekStatcs(Map<String, Object> param){
		return reportingDao.selectMonthlyWeekStatcs(param);
	}

	/**
	 * Monthly 상담원 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyAdviserStatcs(Map<String, Object> param){
		return reportingDao.selectMonthlyAdviserStatcs(param);
	}

	/**
	 * Monthly 상담원 차트 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyAdviserStatcsGraph(Map<String, Object> param){
		return reportingDao.selectMonthlyAdviserStatcsGraph(param);
	}

	/**
	 * Monthly 별점 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyScoreStatcs(Map<String, Object> param){
		return reportingDao.selectMonthlyScoreStatcs(param);
	}

	/**
	 * Monthly 별점 평균 조회
	 */
	public int selectMonthlyScoreAvg(Map<String, Object> param){
		return reportingDao.selectMonthlyScoreAvg(param);
	}

	/**
	 * Monthly 별점 통계 조회 엑셀용
	 */
	public List<Map<String, Object>> selectScoreListAll(Map<String, Object> param){
		return reportingDao.selectScoreListAll(param);
	}

	/**
	 * 카테고리 통계
	 */
	public List<Map<String, Object>> selectCtgStatcs(Map<String, Object> param){
		return reportingDao.selectCtgStatcs(param);
	}

	/**
	 * 카테고리 월별 상세 통계
	 */
	public List<Map<String, Object>> selectCtgMonthlyStatcs(Map<String, Object> param){
		return reportingDao.selectCtgMonthlyStatcs(param);
	}

	/**
	 * 카테고리 월별 상세 통계
	 */
	public String selectCtgDept(Map<String, Object> param){
		return reportingDao.selectCtgDept(param);
	}

	/**
	 * daily 요약 통계
	 */
	public List<Map<String, Object>> selectDailyBasicStatcs(Map<String, Object> param){
		return reportingDao.selectDailyBasicStatcs(param);
	}
	
	/**
	 * daily 요약 통계 - kakao
	 */
	public Map<String, Object> selectDailyBasicKakao(Map<String, Object> param){
		return reportingDao.selectDailyBasicKakao(param);
	}


	/**
	 * daily 시간별 통계
	 */
	public List<Map<String, Object>> selectDailyTimeStatcs(Map<String, Object> param){
		return reportingDao.selectDailyTimeStatcs(param);
	}
	/**
	 * daily 상담원 시간별 통계
	 */
	public List<Map<String, Object>> selectDailyAdviserTimeStatcs(Map<String, Object> param){
		return reportingDao.selectDailyAdviserTimeStatcs(param);
	}

	/**
	 * Daily 상담원 통계 조회
	 */
	public List<Map<String, Object>> selectDailyAdviserStatcs(Map<String, Object> param){
		return reportingDao.selectDailyAdviserStatcs(param);
	}

	/**
	 * Daily 상담원 차트 통계 조회
	 */
	public List<Map<String, Object>> selectDailyAdviserStatcsGraph(Map<String, Object> param){
		return reportingDao.selectDailyAdviserStatcsGraph(param);
	}
	/**
	 * 일간 로봇챗 통계
	 * */
	public List<Map<String, Object>> selectRobotBasicStatics(Map<String, Object> param){
		return reportingDao.selectRobotBasicStatics(param);
	}
	/**
	 * 상담내역 리포트
	 */
	public List<Map<String, Object>> selectCounseling(Map<String, Object> param){
		return reportingDao.selectCounseling(param);
	}

	/**
	 * 상담내역 요약 카운트
	 */
	public List<Map<String, Object>> selectCounselingCnt(Map<String, Object> param){
		return reportingDao.selectCounselingCnt(param);
	}

	/**
	 * 카테고리 depth 설정 조회
	 */
	public String selectCtgMgtDpt(Map<String, Object> param){
		return reportingDao.selectCtgMgtDpt(param);
	}

	/**
	 * 상담내역 엑셀 다운로드
	 */
	public List<Map<String, Object>> selectCounselingExcel(Map<String, Object> param){
		return reportingDao.selectCounselingExcel(param);
	}
	/**
	 * 봇리포트 기본
	 */
	public List<Map<String, Object>> selectBotBasicCnt(Map<String, Object> param){
		return reportingDao.selectBotBasicCnt(param);
	}
	/**
	 * 봇리포트 기본
	 */
	public List<Map<String, Object>> selectBotChannelCnt(Map<String, Object> param){
		return reportingDao.selectBotChannelCnt(param);
	}
	/**
	 * 봇리포트 채널별
	 */
	public List<Map<String, Object>> selectBotChannelDate(Map<String, Object> param){
		return reportingDao.selectBotChannelDate(param);
	}
	/**
	 * 봇리포트 채널별 그래프
	 */
	public List<Map<String, Object>> selectBotChannelDateGraph(Map<String, Object> param){
		return reportingDao.selectBotChannelDateGraph(param);
	}
	/**
	 * 봇리포트 블럭
	 */
	public List<Map<String, Object>> selectBlockCnt(Map<String, Object> param){
		return reportingDao.selectBlockCnt(param);
	}
	/**
	 * 봇리포트 mci
	 */
	public List<Map<String, Object>> selectMCIcnt(Map<String, Object> param){
		return reportingDao.selectMCIcnt(param);
	}
	/**
	 * 봇리포트 roomlist
	 */
	public List<Map<String, Object>> selectBotRoomList(Map<String, Object> param){
		return reportingDao.selectBotRoomList(param);
	}

	/**
	 * 봇리포트 roomlist excel
	 */
	public List<Map<String, Object>> selectBotRoomListXsl(Map<String, Object> param){
		return reportingDao.selectBotRoomListXsl(param);
	}

	/**
	 * 상담원정보조회용
	 */
	public Map<String, Object> selectMemberInfo(String memberUid){
		return reportingDao.selectMemberInfo(memberUid);
	}
	/**
	 * sqi list
	 */
	public List<Map<String, Object>> selectSQIList(Map<String, Object> param){
		return reportingDao.selectSQIList(param);
	}

	/**
	 * sqi exceldown
	 */
	public List<Map<String, Object>> selectSQIListExcel(Map<String, Object> param){
		return reportingDao.selectSQIListExcel(param);
	}

	/**
	 * sqi 카운트
	 */
	public List<Map<String, Object>> selectSQICnt(Map<String, Object> param){
		return reportingDao.selectSQICnt(param);
	}

	/**
	 * daily 용어 검색어순위 통계 조회
	 */
	public List<Map<String, Object>> selectTermRankDay(Map<String, Object> param){
		return reportingDao.selectTermRankDay(param);
	}

	/**
	 * Monthly 용어 검색어순위 통계 조회
	 */
	public List<Map<String, Object>> selectTermRankMon(Map<String, Object> param){
		return reportingDao.selectTermRankMon(param);
	}
}
