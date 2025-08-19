package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class EndCtgDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 분류 조회
	 */
	public Map<String, Object> selectCategory(Map<String, Object> param) {
		return sqlSession.selectOne("endCtg.selectCategory", param);
	}

	/**
	 * 분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryList(Map<String, Object> param) {
		return sqlSession.selectList("endCtg.selectCategory", param);
	}

	/**
	 * 분류 등록
	 */
	public void insertCategory(Map<String, Object> param) {
		sqlSession.insert("endCtg.insertCategory", param);
	}

	/**
	 * 분류 수정
	 */
	public void updateCategory(Map<String, Object> param) {
		sqlSession.update("endCtg.updateCategory", param);
	}

	/**
	 * 분류 삭제
	 */
	public void deleteCategory(Map<String, Object> param) {
		sqlSession.update("endCtg.deleteCategory", param);
	}

}
