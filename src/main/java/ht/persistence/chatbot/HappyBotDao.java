package ht.persistence.chatbot;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class HappyBotDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 블록 조회
	 */
	public Map<String, Object> selectBotBlock(Map<String, Object> sqlParams) {

		return sqlSession.selectOne("happyBot.selectBotBlock", sqlParams);
	}

	/**
	 * 빌더 접속 인증
	 */
	public int insertHappybotAuthCode(Map<String, Object> param) {

		return sqlSession.insert("happyBot.insertHappybotAuthCode", param);
	}

	/**
	 * 세션 조회
	 */
	public Map<String, Object> selectBotSession(Map<String, Object> sqlParams) {

		return sqlSession.selectOne("happyBot.selectBotSession", sqlParams);
	}
	
	/**
	 * IPCC_MCH GREETING 블록 조회
	 */
	public Map<String, Object> selectGreetingBotBlock(Map<String, Object> sqlParams) {

		return sqlSession.selectOne("happyBot.selectGreetingBotBlock", sqlParams);
	}
}
