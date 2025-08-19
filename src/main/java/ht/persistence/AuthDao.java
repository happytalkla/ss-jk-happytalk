package ht.persistence;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 인증 조회
	 */
	public Map<String, Object> selectAuth(Map<String, Object> sqlParams) {

		return sqlSession.selectOne("auth.selectAuth", sqlParams);
	}
	
	/**
	 * 인증 조회
	 */
	public int selectCheckAuth(Map<String, Object> sqlParams) {
		
		return sqlSession.selectOne("auth.selectCheckAuth", sqlParams);
	}

	/**
	 * 인증 저장
	 */
	public int saveAuth(Map<String, Object> sqlParams) {

		return sqlSession.update("auth.saveAuth", sqlParams);
	}

	/**
	 * 인증 삭제
	 */
	public int deleteAuth(Map<String, Object> sqlParams) {

		return sqlSession.delete("auth.deleteAuth", sqlParams);
	}
}
