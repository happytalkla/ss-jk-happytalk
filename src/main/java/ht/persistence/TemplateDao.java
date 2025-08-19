package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 템플릿 카테고리 목록 조회
	 */
	public List<Map<String, Object>> selectTplCategoryList(Map<String, Object> param) {
		return sqlSession.selectList("template.selectTplCategoryList", param);
	}

	/**
	 * 템플릿 카테고리 등록
	 */
	public void insertTplCtg(Map<String, Object> param) {
		sqlSession.insert("template.insertTplCtg", param);
	}

	/**
	 * 템플릿 카테고리 수정
	 */
	public void updateTplCtg(Map<String, Object> param) {
		sqlSession.update("template.updateTplCtg", param);
	}

	/**
	 * 카테고리의 템플릿 개수 조회
	 */
	public int selectTplCtgCnt(Map<String, Object> param) {
		return sqlSession.selectOne("template.selectTplCtgCnt", param);
	}

	/**
	 * 카테고리의 템플릿 삭제
	 */
	public void deleteCtgTemplate(Map<String, Object> param) {
		sqlSession.delete("template.deleteCtgTemplate", param);
	}

	/**
	 * 템플릿 카테고리 삭제
	 */
	public void deleteTplCtg(Map<String, Object> param) {
		sqlSession.delete("template.deleteTplCtg", param);
	}

	/**
	 * 템플릿 목록 조회
	 */
	public List<Map<String, Object>> selectTemplateList(Map<String, Object> param) {
		return sqlSession.selectList("template.selectTemplateList", param);
	}

	/**
	 * 등록 대기중인 템플릿 목록 조회
	 */
	public List<Map<String, Object>> selectConfirmTplList(Map<String, Object> param) {
		return sqlSession.selectList("template.selectConfirmTplList", param);
	}

	/**
	 * 템플릿 등록
	 */
	public void insertTemplate(Map<String, Object> param) {
		sqlSession.insert("template.insertTemplate", param);
	}

	/**
	 * 템플릿 수정
	 */
	public void updateTemplate(Map<String, Object> param) {
		sqlSession.update("template.updateTemplate", param);
	}

	/**
	 * 키워드 삭제
	 */
	public void deleteTplKwd(Map<String, Object> param) {
		sqlSession.update("template.deleteTplKwd", param);
	}

	/**
	 * 키워드 등록
	 */
	public void insertTplKwd(Map<String, Object> param) {
		sqlSession.insert("template.insertTplKwd", param);
	}

	/**
	 * 템플릿 삭제
	 */
	public void deleteTemplate(Map<String, Object> param) {
		sqlSession.delete("template.deleteTemplate", param);
	}

	/**
	 * 템플릿 공유 신청
	 */
	public void insertShareReq(Map<String, Object> param) {
		sqlSession.insert("template.insertShareReq", param);
	}

	/**
	 * 템플릿 공유 신청 삭제
	 */
	public void deleteShareReq(Map<String, Object> param) {
		sqlSession.delete("template.deleteShareReq", param);
	}

	/**
	 * 템플릿 공유 수정
	 */
	public void updateShareReq(Map<String, Object> param) {
		sqlSession.update("template.updateShareReq", param);
	}

	/**
	 * 템플릿 목록 조회
	 */
	public Map<String, Object> selectTemplate(Map<String, Object> param) {
		return sqlSession.selectOne("template.selectTemplate", param);
	}

	/**
	 * 공유요청 정보 조회
	 */
	public Map<String, Object> selectShareReq(Map<String, Object> param) {
		return sqlSession.selectOne("template.selectShareReq", param);
	}

}
