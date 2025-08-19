package ht.persistence;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class ScreenDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 조회
	 */
	public Map<String, Object> selectScreenDetail(Map<String, Object> param) {
		return sqlSession.selectOne("screen.selectScreenDetail", param);
	}

	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectScreenList(Map<String, Object> param) {
		return sqlSession.selectList("screen.selectScreenList", param);
	}
	
	/**
	 * 전체 목록 조회
	 */
	public List<Map<String, Object>> selectAllScreenList(Map<String, Object> param) {
		return sqlSession.selectList("screen.selectAllScreenList", param);
	}

	/**
	 * 삭제
	 */
	public void deleteScreen(Map<String, Object> param) {
		sqlSession.update("screen.deleteScreen", param);
	}

	/**
	 * 저장
	 */
	public void insertScreen(Map<String, Object> param) {
		sqlSession.insert("screen.insertScreen", param);
	}

	/**
	 * 수정
	 */
	public void updateScreen(Map<String, Object> param) {
		sqlSession.update("screen.updateScreen", param);
	}
	
	/**
	 * 용어 자동완성
	 */
	public List<Map<String, Object>> selectAutoScreenListAjax(Map<String, Object> param) {
		return sqlSession.selectList("screen.selectAutoScreenListAjax", param);
	}
	
	/**
	 * 용어검색 저장
	 */
	public void insertReportingScreen(Map<String, Object> param) {
		sqlSession.insert("screen.insertReportingScreen", param);
	}
}
