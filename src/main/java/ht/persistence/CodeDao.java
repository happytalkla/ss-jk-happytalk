package ht.persistence;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CodeDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 상세 조회
	 */
	public Map<String, Object> selectCodeDetail(Map<String, Object> param) {
		return sqlSession.selectOne("codemgr.selectCodeDetail", param);
	}

	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectCodeList(Map<String, Object> param) {
		log.info("============= param : " + param);
		
		return sqlSession.selectList("codemgr.selectCodeList", param);
	}
	
	/**
	 * 전체 목록 조회
	 */
	public List<Map<String, Object>> selectAllCodeList(Map<String, Object> param) {
		return sqlSession.selectList("codemgr.selectAllCodeList", param);
	}

	/**
	 * 삭제
	 */
	public void deleteCode(Map<String, Object> param) {
		sqlSession.update("codemgr.deleteCode", param);
	}

	/**
	 * 저장
	 */
	public void insertCode(Map<String, Object> param) {
		sqlSession.insert("codemgr.insertCode", param);
	}

	/**
	 * 수정
	 */
	public void updateCode(Map<String, Object> param) {
		sqlSession.update("codemgr.updateCode", param);
	}
}