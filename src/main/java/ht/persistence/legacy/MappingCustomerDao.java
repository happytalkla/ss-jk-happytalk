package ht.persistence.legacy;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class MappingCustomerDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 조회
	 */
	public Map<String, Object> selectMappingCustomer(Map<String, Object> sqlParams) {

		//return sqlSession.selectOne("mappingCustomer.selectMappingCustomer", sqlParams);
		List<Map<String, Object>> list = selectMappingCustomerList(sqlParams);
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectMappingCustomerList(Map<String, Object> sqlParams) {

		return sqlSession.selectList("mappingCustomer.selectMappingCustomer", sqlParams);
	}

	/**
	 * 콜상담이력 조회
	 */
	public List<Map<String, Object>>selectMappingCustomerCallHistoryList(Map<String, Object> sqlParams) {

		return sqlSession.selectList("mappingCustomer.selectMappingCustomerCallHistoryList", sqlParams);
	}
}
