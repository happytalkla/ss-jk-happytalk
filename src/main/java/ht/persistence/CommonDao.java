package ht.persistence;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CommonDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 공통 코드 목록 조회
	 */
	public List<Map<String, Object>> selectCommonCodeList(String cdGroup) {
		return sqlSession.selectList("common.selectCommonCodeList", cdGroup);
	}

	/**
	 * 특정 년-월 조회
	 * 특정 년-월-일 조회
	 * schDateType : MONTH, DAY
	 * schDayType : first(해당월의 첫날), last(해당월의 마지막날), add(해당일자에 특정일자를 더한다)
	 * schAdd : 현재 일자에 더하고 뺄 날짜, 혹은 해당 월에 더하고 뺄 월 : -3, -1, 0, 2, 6
	 */
	public Map<String, Object> selectCustomDate(Map<String, Object> param) {
		return sqlSession.selectOne("common.selectCustomDate", param);
	}

	/**
	 * 현재 DB 시스템 시간 조회
	 */
	public Timestamp selectSysdate() {
		return sqlSession.selectOne("common.selectSysdate");
	}

	/**
	 * 현재 년, 월, 일 조회
	 */
	public Map<String, Object> selectNowDate() {
		return sqlSession.selectOne("common.selectNowDate");
	}

	/**
	 * 과거인지 현재인지 미래인지 체크
	 */
	public int selectNowDateCheck(String schDate) {
		return sqlSession.selectOne("common.selectNowDateCheck", schDate);
	}

	/**
	 * 공통 로그 등록
	 */
	public void insertLog(Map<String, Object> param) {
		sqlSession.insert("common.insertLog", param);
	}

	/**
	 * 공통 로그 목록 조회
	 */
	public List<Map<String, Object>> selectLogList(Map<String, Object> param) {
		return sqlSession.selectList("common.selectLogList", param);
	}

	public void insertTest() {
		sqlSession.insert("common.insertTest");
	}

	/**
	 * 업로드 파일 등록
	 */
	public int insertChatAttFile(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.insert("common.insertChatAttFile", params);
	}

	/**
	 * 스킨 정보 조회
	 */
	public Map<String, Object> selectCustomerSkin() {
		return sqlSession.selectOne("setting.selectCustomerSkin");
	}

	/**
	 * 배치 시작 여부 판단
	 */
	public String checkBatchStart(Map<String, Object> params) {
		return sqlSession.selectOne("common.checkBatchStart", params);
	}

	/**
	 * 배치 시작 update
	 */
	public void updateBatchStart(Map<String, Object> params) {
		sqlSession.update("common.updateBatchStart", params);
	}
}
