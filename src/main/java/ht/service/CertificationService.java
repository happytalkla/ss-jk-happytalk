package ht.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import ht.persistence.CertificationDao;

@Service
@Deprecated
public class CertificationService {

	@Resource
	private CertificationDao certificationDao;

	@Resource
	private TimeService ts;

	@Resource
	private ManageService ms;

	public String[] is_auth_action = {
			"insurance_debit_change", // 보험료 자동이체 변경
			"insurance_debit_change_detail"  // 보험료 자동이체 변경 상세
	};
	/**
	 * 인증 유무를 검사
	 * @param user_key
	 * @param channel
	 * @param user_action
	 * @return
	 * @throws Exception
	 */
	public boolean certificationValidate(String user_key, String channel, String user_action) throws Exception {
		// 공인인증 필수 리스트
		List<String> is_auth_list = Arrays.asList(this.is_auth_action);

		// DB 의 인증 정보를 호출


		try {
			Map<String, Object> results = this.selectCertification(channel, user_key);

			if (results == null || !results.containsKey("uuid")) throw new Exception("인증정보 시간이 경과 하였거나 없습니다.");
			if ((results.get("auth_type").equals("P") || results.get("auth_type").equals("I")) && is_auth_list.contains(user_action))
				throw new Exception("공인인증 로그인이 필요한 서비스 입니다.");

			// 만료시간 연장
			this.updateExpireTimeCertification(channel, user_key, ts.expire_second);

		} catch (Exception e) {
			// 오류 발생

			is_auth_list = null;
			return false;
		}

		return true;
	}

	/**
	 * 사용가능한 공인인증 인지를 검사
	 * @param user_key
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	public boolean certificationAValidate(String user_key, String channel) throws Exception {
		// DB 의 인증 정보를 호출
		Map<String, Object> results = this.selectCertification(channel, user_key);

		try {
			if (results == null || !results.containsKey("uuid")) throw new Exception("인증정보 시간이 경과 하였거나 없습니다.");
			if (!results.get("auth_type").equals("A")) throw new Exception("공인인증정보 유효시간을 경과 하였거나 없습니다.");

			// 만료시간 연장
			this.updateExpireTimeCertification(channel, user_key, ts.expire_second);
		} catch (Exception e) {
			// 오류 발생

			results = null;
			return false;
		}

		return true;
	}

	/**
	 * 인증정보 조회
	 */
	public Map<String, Object> selectCertification(String channel, String uuid) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("channel", channel);
		params.put("uuid", uuid);
		params.put("expire_date", ts.expireToDate());

		return certificationDao.selectCertification(params);
	}

	/**
	 * 인증정보 조회 (만료시간 구분 없이 존재 유무만 판단)
	 */
	public Map<String, Object> selectCertificationExist(String channel, String uuid) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("channel", channel);
		params.put("uuid", uuid);

		return certificationDao.selectCertification(params);
	}

	public Map<String, Object> selectCertificationNoUseExpireDate(String channel, String uuid) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("channel", channel);
		params.put("uuid", uuid);

		return certificationDao.selectCertificationNoUseExpireDate(params);
	}

	/**
	 * 과거에 인증을 했는지의 유무
	 * @throws Exception
	 */
	public boolean selectCertificationChecked(String channel, String uuid) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("channel", channel);
		params.put("uuid", uuid);

		Map<String, Object> result = certificationDao.selectCertificationChecked(params);

		if (result != null) {
			if ("0".equals(String.valueOf(result.get("is_certification")))) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 1시간 이내 인증이 만료된 데이터가 있는지 확인
	 * @throws Exception
	 */
	public boolean selectCertificationChecked1Hour(String channel, String uuid) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("channel", channel);
		params.put("uuid", uuid);

		Map<String, Object> result = certificationDao.selectCertificationChecked1Hour(params);

		if (result != null) {
			if ("0".equals(String.valueOf(result.get("is_certification")))) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 인증코드 호출
	 * @param channel
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectCertificationCode(String channel, String uuid) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("channel", channel);
		params.put("uuid", uuid);
		params.put("expire_date", ts.expireToDate());

		return certificationDao.selectCertificationCode(params);
	}

	/**
	 * 인증정보 적용
	 * @param params
	 *
	 * 		cstm_link_customer_uid : 카카오톡 및 네이터 톡톡, 해피톡의 유니크 uuid
	 * 		cstm_link_div_cd : 채널 정보 (happytalk A, kakao B, naver C)
	 * 		name : 이름
	 * 		birth_date : 생년월일 19820504
	 * 		coc_id : 고객코드
	 *
	 * @return boolean
	 */
	public boolean setCertification(Map<String, String> params) {
		certificationDao.deleteCertification(params);
		certificationDao.insertCertification(params);

		// 고객사 사용자 아이디를 업데이트 한다.
		if (params.containsKey("coc_id") && !"".equals(params.get("coc_id"))) {
			Map<String, Object> setParams = new HashMap<>();
			setParams.put("cstm_link_customer_uid", String.valueOf(params.get("uuid")));
			setParams.put("cstm_link_div_cd", String.valueOf(params.get("channel")));
			setParams.put("name", String.valueOf(params.get("name")));
			setParams.put("birth_date", String.valueOf(params.get("birth_date")));
			setParams.put("coc_id", String.valueOf(params.get("coc_id")));
			setParams.put("vip_cust_yn",  String.valueOf(params.get("vip_cust_yn")));

			ms.updateUserCocId(setParams);
			ms.updateRoomCocId(setParams);
		} else {
			// 인증완료시 신규로 받은 데이터로 리셋
			Map<String, Object> setParams = new HashMap<>();
			setParams.put("cstm_link_customer_uid", String.valueOf(params.get("uuid")));
			setParams.put("cstm_link_div_cd", String.valueOf(params.get("channel")));
			setParams.put("name", "");
			setParams.put("birth_date", "");
			setParams.put("coc_id", "");
			setParams.put("vip_cust_yn", String.valueOf(params.get("vip_cust_yn")));

			ms.updateUserCocId(setParams);
			ms.updateRoomCocId(setParams);
		}

		return true;
	}

	public boolean insertCertification(Map<String, String> params) {
		certificationDao.insertCertification(params);
		return true;
	}

	public boolean insertCertificationCode(Map<String, String> params) {
		certificationDao.insertCertificationCode(params);
		return true;
	}

	public boolean updateExpireTimeCertification(String channel, String uuid, int expire_second) throws Exception {
		Map<String, String> data = new HashMap<>();
		data.put("channel", channel);
		data.put("uuid", uuid);
		data.put("expire_date", ts.expireDate());
		certificationDao.updateExpireTimeCertification(data);
		return true;
	}

	public boolean deleteCertification(String channel, String uuid) throws Exception {
		Map<String, String> data = new HashMap<>();
		data.put("channel", channel);
		data.put("uuid", uuid);
		certificationDao.deleteCertification(data);
		return true;
	}

	public boolean updateCertificationCode(String unique_cd, String auth_cd) throws Exception {
		Map<String, String> data = new HashMap<>();
		data.put("unique_cd", unique_cd);
		data.put("auth_cd", auth_cd);
		certificationDao.updateCertificationCode(data);
		return true;
	}
}
