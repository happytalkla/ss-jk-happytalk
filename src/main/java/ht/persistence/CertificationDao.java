package ht.persistence;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CertificationDao {

	@Resource
	private SqlSession sqlSession;

	public void insertCertification(Map<String, String> param) {
		sqlSession.insert("certification.insertCertification", param);
	}

	public void insertCertificationCode(Map<String, String> param) {
		sqlSession.insert("certification.insertCertificationCode", param);
	}

	public void deleteCertificationRoomUid(String chatRoomUid) {
		sqlSession.delete("certification.deleteCertificationRoomUid", chatRoomUid);
	}

	public void resetCertificationRoomInfo(String chatRoomUid) {
		sqlSession.delete("certification.resetCertificationRoomInfo", chatRoomUid);
	}

	public void deleteCertification(Map<String, String> param) {
		sqlSession.delete("certification.deleteCertification", param);
	}

	public void updateExpireTimeCertification(Map<String, String> param) {
		sqlSession.update("certification.updateExpireTimeCertification", param);
	}

	public Map<String, Object> selectCertification(Map<String, Object> param) {
		return sqlSession.selectOne("certification.selectCertification", param);
	}

	public Map<String, Object> selectCertificationNoUseExpireDate(Map<String, Object> param) {
		return sqlSession.selectOne("certification.selectCertificationNoUseExpireDate", param);
	}

	public Map<String, Object> selectCertificationChecked(Map<String, Object> param) {
		return sqlSession.selectOne("certification.selectCertificationChecked", param);
	}
	/**
	 * 인증만료후 1시간 이내인지
	 * @param param
	 * @return
	 */
	public Map<String, Object> selectCertificationChecked1Hour(Map<String, Object> param) {
		return sqlSession.selectOne("certification.selectCertificationChecked1Hour", param);
	}

	public Map<String, Object> selectCertificationCode(Map<String, Object> param) {
		return sqlSession.selectOne("certification.selectCertificationCode", param);
	}

	public void updateCertificationCode(Map<String, String> param) {
		sqlSession.update("certification.updateCertificationCode", param);
	}
}