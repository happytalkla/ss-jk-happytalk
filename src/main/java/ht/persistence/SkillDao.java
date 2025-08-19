package ht.persistence;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class SkillDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 조회
	 */
	public Map<String, Object> selectO2appName(Map<String, Object> param) {
		return sqlSession.selectOne("skill.selectO2appName", param);
	}

}
