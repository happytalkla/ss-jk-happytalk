package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class AutoCmpDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 자동완성 목록 조회
	 */
	public List<Map<String, Object>> selectAutoCmpList(Map<String, Object> sqlParams) {

		return sqlSession.selectList("autoCmp.selectAutoCmpList", sqlParams);
	}

	/**
	 * 자동완성 카운트
	 */
	public int selectAutoCmpCnt(Map<String, Object> sqlParams) {

		return sqlSession.selectOne("autoCmp.selectAutoCmpCnt", sqlParams);
	}

	/**
	 * 자동완성 등록
	 */
	public int insertAutoCmp(Map<String, Object> sqlParams) {

		return sqlSession.insert("autoCmp.insertAutoCmp", sqlParams);
	}

	/**
	 * 자동완성 수정
	 */
	public int updateAutoCmp(Map<String, Object> sqlParams) {

		return sqlSession.update("autoCmp.updateAutoCmp", sqlParams);
	}

	/**
	 * 자동완성 삭제
	 */
	public int deleteAutoCmp(Map<String, Object> sqlParams) {

		return sqlSession.delete("autoCmp.deleteAutoCmp", sqlParams);
	} 
}
