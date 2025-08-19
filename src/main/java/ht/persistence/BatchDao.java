package ht.persistence;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@Repository
public class BatchDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 민감정보 목록 조회
	 */
	public List<Map<String, Object>> selectCstmSinfo(Map<String, Object> sqlParams) {

		return sqlSession.selectList("batch.selectCstmSinfo", sqlParams);
	}

	/**
	 * 민감정보 수정
	 */
	public int updateCstmSinfo(Map<String, Object> sqlParams) {

		return sqlSession.update("batch.updateCstmSinfo", sqlParams);
	}

	/**
	 * 후처리 메모 목록 조회
	 */
	public List<Map<String, Object>> selectEndMemo(Map<String, Object> sqlParams) {

		return sqlSession.selectList("batch.selectEndMemo", sqlParams);
	}

	/**
	 * 후처리 메모 수정
	 */
	public int updateEndMemo(Map<String, Object> sqlParams) {

		return sqlSession.update("batch.updateEndMemo", sqlParams);
	}

	/**
	 * 2020/03/05 15:00:00 이후 종료 정보 BSP 로 전달
	 */
	public List<Map<String, Object>> selectChatRoomForBSP(Map<String, Object> sqlParams) {

		return sqlSession.selectList("batch.selectChatRoomForBSP", sqlParams);
	}

	/**
	 * 배치잡 목록 조회
	 */
	public List<Map<String, Object>> selectBatchJob(Map<String, Object> sqlParams) {

		return sqlSession.selectList("batch.selectBatchJob", sqlParams);
	}

	/**
	 * 배치잡 변경
	 */
	public int updateBatchJob(Map<String, Object> sqlParams) {

		return sqlSession.update("batch.updateBatchJob", sqlParams);
	}
	
	/**
	 * 용어 검색어 순위 조회
	 */
	public List<Map<String, Object>> selectTermRank(Map<String, Object> sqlParams) {

		return sqlSession.selectList("batch.selectTermRank", sqlParams);
	}
	
	/**
	 * 용어 검색어 순위 일별 저장
	 */
	public void insertTermRankDay(Map<String, Object> param) {
		sqlSession.insert("batch.insertTermRankDay", param);
	}
	
	/**
	 * 용어 검색어 순위 월별 저장
	 */
	public void insertTermRankMon(Map<String, Object> param) {
		sqlSession.insert("batch.insertTermRankMon", param);
	}
	
	/**
	 * 용어 검색어 순위 일별 삭제
	 */
	public void deleteTermRankDay(Map<String, Object> param) {
		sqlSession.delete("batch.deleteTermRankDay", param);
	}
	
	/**
	 * 용어 검색어 순위 월별 tkrwp
	 */
	public void deleteTermRankMon(Map<String, Object> param) {
		sqlSession.delete("batch.deleteTermRankMon", param);
	}
	
}
