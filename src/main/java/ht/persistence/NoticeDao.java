package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

import ht.domain.NoticeVO;

@Repository
public class NoticeDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 조회
	 */
	public NoticeVO selectNoticeDetail(Map<String, Object> param) {
		return sqlSession.selectOne("notice.selectNoticeDetail", param);
	}

	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> param) {
		return sqlSession.selectList("notice.selectNoticeList", param);
	}

	/**
	 * 분류 조회
	 */
	public List<Map<String, Object>> selectNoticeDivList(Map<String, Object> param) {
		return sqlSession.selectList("notice.selectNoticeDivList", param);
	}

	/**
	 * 삭제
	 */
	public void deleteNotice(Map<String, Object> param) {
		sqlSession.update("notice.deleteNotice", param);
	}

	/**
	 * 저장
	 */
	public void insertNotice(Map<String, Object> param) {
		sqlSession.insert("notice.insertNotice", param);
	}

	/**
	 * 수정
	 */
	public void updateNotice(Map<String, Object> param) {
		sqlSession.update("notice.updateNotice", param);
	}
}
