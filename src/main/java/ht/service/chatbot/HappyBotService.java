package ht.service.chatbot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;

import ht.persistence.chatbot.HappyBotDao;

@Service
public class HappyBotService {

	@Resource
	private HappyBotDao happyBotDao;

	/**
	 * 블록 조회
	 */
	public Map<String, Object> selectBotBlockById(@NotNull @Positive Long id) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("id", id);

		return happyBotDao.selectBotBlock(sqlParams);
	}

	/**
	 * 블록 조회
	 */
	public Map<String, Object> selectBotBlockByTriggerCode(@NotEmpty String triggerCode) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("triggerCode", triggerCode);

		return happyBotDao.selectBotBlock(sqlParams);
	}

	/**
	 * 빌더 접속 인증
	 */
	public int insertHappybotAuthCode(Map<String, Object> params) {

		return happyBotDao.insertHappybotAuthCode(params);
	}

	/**
	 * 봇세션 조회
	 */
	public Map<String, Object> selectBotSessionById(@NotEmpty String sessionId) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("id", sessionId);

		return happyBotDao.selectBotSession(sqlParams);
	}
	
	/**
	 * IPCC_MCH GREETING 블록 조회
	 */
	public Map<String, Object> selectGreetingBotBlock(@NotEmpty String code) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("code", code);

		return happyBotDao.selectGreetingBotBlock(sqlParams);
	}
}
