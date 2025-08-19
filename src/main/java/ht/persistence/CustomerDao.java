package ht.persistence;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 기간계 고객 조회
	 */
	public Map<String, Object> selectCustomerSinfo(Map<String, Object> param) {
		return sqlSession.selectOne("customer.selectCustomerSinfo", param);
	}

	/**
	 * 콜상담이력 조회
	 */
	public List<Map<String, Object>> selectCallHistList(Map<String, Object> param) {
		return sqlSession.selectList("customer.selectCallHistList", param);
	}

	/**
	 * 계약정보 목록 조회
	 */
	public List<Map<String, Object>> selectCustInfoList(Map<String, Object> param) {
		return sqlSession.selectList("customer.selectCustInfoList", param);
	}

}
