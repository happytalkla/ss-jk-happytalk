package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 분류 조회
	 */
	public Map<String, Object> selectCategory(Map<String, Object> param) {
		return sqlSession.selectOne("category.selectCategory", param);
	}

	/**
	 * 분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryList(Map<String, Object> param) {
		return sqlSession.selectList("category.selectCategory", param);
	}

	/**
	 * 분류 상담직원 매핑 초기화
	 */
	public void deleteAllMapping() {
		sqlSession.delete("category.deleteAllMapping");
	}

	/**
	 * 분류 등록
	 */
	public void insertCategory(Map<String, Object> param) {
		sqlSession.insert("category.insertCategory", param);
	}

	/**
	 * 분류 수정
	 */
	public void updateCategory(Map<String, Object> param) {
		sqlSession.update("category.updateCategory", param);
	}

	/**
	 * 분류 삭제
	 */
	public void deleteCategory(Map<String, Object> param) {
		sqlSession.update("category.deleteCategory", param);
	}

	/**
	 * 상담직원 매핑용 분류 목록 조회 2 depth
	 */
	public List<Map<String, Object>> selectAllCategoryDpt2List() {
		return sqlSession.selectList("category.selectAllCategoryDpt2List");
	}

	/**
	 * 카테고리 분류 정보 2 depth
	 */

	public List<Map<String, Object>> selectAllCategoryDpt21List(Map<String, Object> param) {
		return sqlSession.selectList("category.selectAllCategoryDpt21List", param);
	}

	public List<Map<String, Object>> selectCtgMemberMapping(Map<String, Object> param) {
		return sqlSession.selectList("member.selectCtgMemberMapping", param);
	}

	/**
	 * 상담직원 매핑용 분류 목록 조회 3 depth
	 */
	public List<Map<String, Object>> selectAllCategoryDpt3List() {
		return sqlSession.selectList("category.selectAllCategoryDpt3List");
	}

	/**
	 * 분류 매핑용 상담직원 목록 조회
	 */
	public List<Map<String, Object>> selectMatchCnsrList(Map<String, Object> param) {
		return sqlSession.selectList("category.selectMatchCnsrList", param);
	}

	/**
	 * 기존 분류에 매칭된 상담직원 삭제
	 */
	public void deleteMatchCnsr(Map<String, Object> param) {
		sqlSession.update("category.deleteMatchCnsr", param);
	}

	/**
	 * 신규 매칭된 상담직원 등록
	 */
	public void insertMatchCnsr(Map<String, Object> param) {
		sqlSession.update("category.insertMatchCnsr", param);
	}

	/**
	 * 종료 분류 조회
	 */
	public Map<String, Object> selectEndCategory(Map<String, Object> params) {

		List<Map<String, Object>> endCategoryList = sqlSession.selectList("category.selectEndCategory", params);
		if (endCategoryList.isEmpty()) {
			return null;
		} else {
			return endCategoryList.get(0);
		}
	}

	/**
	 * 종료 분류 목록 조회
	 */
	public List<Map<String, Object>> selectEndCategoryList(Map<String, Object> params) {

		return sqlSession.selectList("category.selectEndCategory", params);
	}

	/**
	 * 종료 분류 목록 검색
	 */
	public List<Map<String, Object>> selectEndCategorySrcList(Map<String, Object> params) {

		return sqlSession.selectList("category.selectEndCategorySrc", params);
	}
}
