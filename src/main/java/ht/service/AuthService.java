package ht.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import ht.persistence.AuthDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스
 */
@Service
public class AuthService {

	@Resource
	private AuthDao authDao;

	/**
	 * 만료안된 인증 조회
	 */
	public Map<String, Object> isAuth(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		sqlParams.put("expiredMinutes", 30);

		return authDao.selectAuth(sqlParams);
	}

	/**
	 * 인증 조회
	 */
	public Map<String, Object> selectAuth(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);

		return authDao.selectAuth(sqlParams);
	}

	/**
	 * 인증 저장
	 */
	@Transactional
	public int saveAuth(Map<String, Object> params, @NotNull @Positive Integer expiredMinutes) {

		params.put("expiredMinutes", expiredMinutes);

		return authDao.saveAuth(params);
	}

	/**
	 * 인증 삭제
	 */
	@Transactional
	public int deleteAuth(@NotNull @Positive Long authId) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("authId", authId);

		return authDao.deleteAuth(sqlParams);
	}

	/**
	 * 인증 삭제
	 */
	@Transactional
	public int deleteAuth(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);

		return authDao.deleteAuth(sqlParams);
	}

	/**
	 * 인증 조회
	 */
	public int selectCheckAuth(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);

		return authDao.selectCheckAuth(sqlParams);
	}
}
