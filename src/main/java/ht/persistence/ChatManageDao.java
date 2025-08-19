package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

import ht.util.CipherUtils;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ChatManageDao {

	@Resource
	private SqlSession sqlSession;
	@Resource
	private CipherUtils cipherUtils;

	/**
	 * 상담관리 - 상담 status별 목록 조회
	 */
	public List<Map<String, Object>> selectStatusRoomList(Map<String, Object> param) {
		return sqlSession.selectList("chatManage.selectStatusRoomList", param);
	}

	/**
	 * 상담 이력 조회
	 */
	public List<Map<String, Object>> selectCnsHisList(Map<String, Object> param) {

		List<Map<String, Object>> list = sqlSession.selectList("chatManage.selectCnsHisList", param);
		for (Map<String, Object> item : list) {
			if (item.get("memo") != null) {
				try {
					item.put("memo", cipherUtils.decrypt((String) item.get("memo")));
				} catch (Exception e) {
					log.error("{}", e.getLocalizedMessage());
				}
			}
		}
		return list;
	}

	/**
	 * 상담관리 - 지식화 관리 목록 - 프로젝트 목록
	 */
	public List<Map<String, Object>> selectProjectList(Map<String, Object> param) {
		return sqlSession.selectList("chatManage.selectProjectList", param);
	}

	/**
	 * 상담관리 - 지식화 관리 목록
	 */
	public List<Map<String, Object>> selectKnowList(Map<String, Object> param) {
		return sqlSession.selectList("chatManage.selectKnowList", param);
	}

	/**
	 * 상담관리 - 지식화 관리 조회
	 */
	public Map<String, Object> selectKnowledge(Map<String, Object> param) {
		return sqlSession.selectOne("chatManage.selectKnowledge", param);
	}

	/**
	 * 상담관리 - 지식화 관리 등록
	 */
	public void insertKnowMgt(Map<String, Object> param) {
		sqlSession.insert("chatManage.insertKnowMgt", param);
	}

	/**
	 * 상담관리 - 지식화 관리 수정
	 */
	public void updateKnowMgt(Map<String, Object> param) {
		sqlSession.update("chatManage.updateKnowMgt", param);
	}

	/**
	 * 상담관리 - 지식화 관리 삭제
	 */
	public void deleteKnowMgt(Map<String, Object> param) {
		sqlSession.update("chatManage.deleteKnowMgt", param);
	}
}
