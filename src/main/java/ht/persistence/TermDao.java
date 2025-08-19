package ht.persistence;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class TermDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 조회
	 */
	public Map<String, Object> selectTermDetail(Map<String, Object> param) {
		return sqlSession.selectOne("term.selectTermDetail", param);
	}

	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectTermList(Map<String, Object> param) {
		return sqlSession.selectList("term.selectTermList", param);
	}
	
	/**
	 * 전체 목록 조회
	 */
	public List<Map<String, Object>> selectAllTermList(Map<String, Object> param) {
		return sqlSession.selectList("term.selectAllTermList", param);
	}

	/**
	 * 삭제
	 */
	public void deleteTerm(Map<String, Object> param) {
		sqlSession.update("term.deleteTerm", param);
	}

	/**
	 * 저장
	 */
	public void insertTerm(Map<String, Object> param) {
		sqlSession.insert("term.insertTerm", param);
	}

	/**
	 * 수정
	 */
	public void updateTerm(Map<String, Object> param) {
		sqlSession.update("term.updateTerm", param);
	}
	
	/**
	 * 용어 자동완성
	 */
	public List<Map<String, Object>> selectAutoTermListAjax(Map<String, Object> param) {
		return sqlSession.selectList("term.selectAutoTermListAjax", param);
	}
	
	/**
	 * 용어검색 저장
	 */
	public void insertReportingTerm(Map<String, Object> param) {
		sqlSession.insert("term.insertReportingTerm", param);
	}
}
