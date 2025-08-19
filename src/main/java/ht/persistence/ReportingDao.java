package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class ReportingDao {

	@Resource
	private SqlSession sqlSession;


	/**
	 * Monthly 통계 기본 조회
	 */
	public List<Map<String, Object>> selectMonthlyBasicStatcs(Map<String, Object> param){

		return sqlSession.selectList("reporting.selectMonthlyBasicStatcs", param);
	}

	/**
	 * Monthly 통계 기본 조회 - kakao
	 */
	public Map<String, Object> selectMonthlyBasicKakao(Map<String, Object> param){

		return sqlSession.selectOne("reporting.selectMonthlyBasicKakao", param);
	}
	
	/**
	 * Monthly 일자별 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyDayStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectMonthlyDayStatcs", param);
	}

	/**
	 * Monthly 요일별 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyWeekStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectMonthlyWeekStatcs", param);
	}

	/**
	 * Monthly 상담원 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyAdviserStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectMonthlyAdviserStatcs", param);
	}

	/**
	 * Monthly 상담원 차트 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyAdviserStatcsGraph(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectMonthlyAdviserStatcsGraph", param);
	}

	/**
	 * Monthly 별점 통계 조회
	 */
	public List<Map<String, Object>> selectMonthlyScoreStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectMonthlyScoreStatcs", param);
	}

	/**
	 * Monthly 별점 평균 조회
	 */
	public int selectMonthlyScoreAvg(Map<String, Object> param){
		return sqlSession.selectOne("reporting.selectMonthlyScoreAvg", param);
	}

	/**
	 * Monthly 별점 통계 조회 엑셀용
	 */
	public List<Map<String, Object>> selectScoreListAll(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectScoreListAll", param);
	}

	/**
	 * 카테고리 통계
	 */
	public List<Map<String, Object>> selectCtgStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectCtgStatcs", param);
	}

	/**
	 * 카테고리 월별 상세 통계
	 */
	public List<Map<String, Object>> selectCtgMonthlyStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectCtgMonthlyStatcs", param);
	}

	/**
	 * 카테고리 통계
	 */
	public  String selectCtgDept(Map<String, Object> param){
		List<Map<String, Object>> ctgList =  sqlSession.selectList("reporting.selectCtgDept", param);
		String boolenString = "";
		if(ctgList.size() == 0) {
			boolenString = "false";
		}else {
			boolenString = "true";
		}
		return boolenString;
	}

	/**
	 * daily 요약 통계
	 */
	public List<Map<String, Object>> selectDailyBasicStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectDailyBasicStatcs", param);
	}
	
	/**
	 * daily 요약 통계 - kakao
	 */
	public Map<String, Object> selectDailyBasicKakao(Map<String, Object> param){
		return sqlSession.selectOne("reporting.selectDailyBasicKakao", param);
	}

	
	/**
	 * daily 시간별 통계
	 */
	public List<Map<String, Object>> selectDailyTimeStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectDailyTimeStatcs", param);
	}

	/**
	 * daily 상담원 시간별 통계
	 */
	public List<Map<String, Object>> selectDailyAdviserTimeStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectDailyAdviserTimeStatcs", param);
	}

	/**
	 * daily 상담원 통계 조회
	 */
	public List<Map<String, Object>> selectDailyAdviserStatcs(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectDailyAdviserStatcs", param);
	}

	/**
	 * daily 상담원 차트 통계 조회
	 */
	public List<Map<String, Object>> selectDailyAdviserStatcsGraph(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectDailyAdviserStatcsGraph", param);
	}
	/**
	 * 일간 로봇챗 통계 기본 조회
	 */
	public List<Map<String, Object>> selectRobotBasicStatics(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectMonthlyBasicStatcs", param);
	}
	/**
	 * 상담내역 리포트
	 */
	public List<Map<String, Object>> selectCounseling(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectCounseling", param);
	}

	/**
	 * 상담내역 요약 카운트
	 */
	public List<Map<String, Object>> selectCounselingCnt(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectCounselingCnt", param);
	}

	/**
	 * 카테고리 depth 설정 조회
	 */
	public String selectCtgMgtDpt(Map<String, Object> param) {
		return sqlSession.selectOne("reporting.selectCtgMgtDpt", param);
	}

	/**
	 * 상담내역 엑셀
	 */
	public List<Map<String, Object>> selectCounselingExcel(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectCounselingExcel", param);
	}
	/**
	 * 봇리포팅 기본
	 */
	public List<Map<String, Object>> selectBotBasicCnt(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectBotBasicCnt", param);
	}
	/**
	 * 봇리포팅 기본 검색기간내 채널
	 */
	public List<Map<String, Object>> selectBotChannelCnt(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectBotChannelCnt", param);
	}
	/**
	 * 봇리포팅 채널별
	 */
	public List<Map<String, Object>> selectBotChannelDate(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectBotChannelDate", param);
	}

	/**
	 * 봇리포팅 채널별 그래프용
	 */
	public List<Map<String, Object>> selectBotChannelDateGraph(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectBotChannelDateGraph", param);
	}

	/**
	 * 봇리포팅 블럭별
	 */
	public List<Map<String, Object>> selectBlockCnt(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectBlockCnt", param);
	}

	/**
	 * 봇리포팅 mci
	 */
	public List<Map<String, Object>> selectMCIcnt(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectMCIcnt", param);
	}
	/**
	 * 봇리포팅 rommlist
	 */
	public List<Map<String, Object>> selectBotRoomList(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectBotRoomList", param);
	}
	/**
	 * 봇리포팅 rommlist excel
	 */
	public List<Map<String, Object>> selectBotRoomListXsl(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectBotRoomListXsl", param);
	}
	/**
	 * 상담원정보조회
	 */
	public Map<String, Object> selectMemberInfo(String memberUid){
		return sqlSession.selectOne("reporting.selectMemberInfo", memberUid);
	}
	/**
	 * SQI
	 */
	public List<Map<String, Object>> selectSQIList(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectSQIList", param);
	}
	/**
	 * SQI 엑셀
	 */
	public List<Map<String, Object>> selectSQIListExcel(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectSQIListExcel", param);
	}
	/**
	 * SQI 카운트
	 */
	public List<Map<String, Object>> selectSQICnt(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectSQICnt", param);
	}
	/**
	 * daily 용어 검색어순위 통계 조회
	 */
	public List<Map<String, Object>> selectTermRankDay(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectTermRankDay", param);
	}
	/**
	 * Monthly 용어 검색어순위 통계 조회
	 */
	public List<Map<String, Object>> selectTermRankMon(Map<String, Object> param){
		return sqlSession.selectList("reporting.selectTermRankMon", param);
	}
}
