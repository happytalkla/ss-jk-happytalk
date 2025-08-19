package ht.persistence;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GenesysDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * IPCC_ADV 인터페이스 조회
	 */
	public Map<String, Object> selectIfResult(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.selectOne("genesys.selectIfResult", params);
	}

	/**
	 * IPCC_ADV 인터페이스 목록 조회
	 */
	public List<Map<String, Object>> selectIfResultList(Map<String, Object> params) {

		return sqlSession.selectList("genesys.selectIfResult", params);
	}

	/**
	 * IPCC_ADV 인터페이스 다음 ID 조회
	 */
	public Long selectIfResultNextId() {
		return sqlSession.selectOne("genesys.selectIfResultNextId");
	}

	/**
	 * IPCC_ADV 인터페이스 등록(시작)
	 */
	public int insertIfResultForStart(Map<String, Object> params) {

		return sqlSession.insert("genesys.insertIfResultForStart", params);
	}

	/**
	 * IPCC_ADV 인터페이스 변경(종료)
	 */
	public int updateIfResultForEnd(Map<String, Object> params) {

		return sqlSession.update("genesys.updateIfResultForEnd", params);
	}

	/**
	 * IPCC_ADV 인터페이스 변경(재전송)
	 */
	public int updateIfResultForResend(Map<String, Object> params) {
		
		return sqlSession.update("genesys.updateIfResultForResend", params);
	}

	/**
	 * IPCC_ADV 인터페이스 실패 목록 조회
	 */
	public List<Map<String, Object>> selectFailedList() {
		return sqlSession.selectList("genesys.selectFailedList");
	}
	
	/**
	 * IPCC_ADV 고객여정 고객 채팅 내역 조회
	 */
	public List<Map<String, Object>> selectChatList(Map<String, Object> params) {
		return sqlSession.selectList("genesys.selectChatList", params);
	}
	
	/**
	 * IPCC_IST 통합통계 목록 조회
	 */
	public List<String> selectIstList(@NotNull String strDate) {
		return sqlSession.selectList("genesys.selectIstList", strDate);
	}
}
