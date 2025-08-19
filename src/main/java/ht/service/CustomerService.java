package ht.service;

import static ht.constants.CommonConstants.*;

import com.google.common.base.Strings;
import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.persistence.CustomerDao;
import ht.persistence.MemberDao;
import ht.util.CipherUtils;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;

import java.util.*;

@Service
@Slf4j
public class CustomerService {

	@Resource
	private CustomerDao customerDao;
	@Resource
	private MemberDao memberDao;
	@Resource
	private SettingService settingService;
	@Resource
	private HTUtils htUtils;
	@Resource
	private CipherUtils cipherUtils;
	@Resource
	private CustomProperty customProperty;

	/**
	 * 고객 UID 생성
	 */
	@Transactional
	public String createCstmUid() {

		String uuid = UUID.randomUUID().toString();
		for (int i = 0; i < 10; i++) {
			if (selectCustomerByCstmUid(uuid) == null) {
				return uuid;
			}
		}

		throw new UnsupportedOperationException();
	}

	/**
	 * 고객 생성 (채널)
	 */
	@Transactional
	public Map<String, Object> createCustomer(String channelId, String channelServiceId, String channelCustomerId) {

		return createCustomer(channelId, channelServiceId, channelCustomerId
				, null, null, CSTM_OS_DIV_CD_MOBILE, null);
	}

	/**
	 * 고객 생성 (웹채팅)
	 */
	@Transactional
	public Map<String, Object> createCustomer(String userId, String userName, String channel
			, String deviceType, HttpHeaders headers, HttpServletRequest request) {

		return createCustomer(channel, customProperty.getSiteId(), null
				, userId, userName, deviceType, htUtils.getRemoteIpAddress(headers, request));
	}

	/**
	 * 고객 생성
	 */
	@Transactional
	public Map<String, Object> createCustomer(String channelCd, String channelServiceId, String channelCustomerId
			, String userId, String userName, String deviceType, String ipAddr) {
		log.debug("CREATE CUSTOMER");

		Map<String, Object> user = new HashMap<>();
		user.put("cstmUid", createCstmUid());
		user.put("cocId", userId);
		user.put("cocNm", userName);
		user.put("name", userName);
		user.put("linkIp", ipAddr);
		user.put("cstmLinkDivCd", channelCd);
		user.put("cstmLinkServiceUid", channelServiceId);
		user.put("cstmLinkCustomerUid", channelCustomerId == null ? user.get("cstmUid") : channelCustomerId);
		user.put("loginYn", userId == null ? "N" : "Y");
		user.put("creater", customProperty.getSystemMemeberUid());
		saveCustomer(user);

		return selectCustomerByCstmUid((String) user.get("cstmUid"));
	}

	/**
	 * 고객 수정
	 */
	@Transactional
	public Map<String, Object> updateCustomer(Map<String, Object> user, String userId, String userName) {

		return updateCustomer(user, userId, userName, null, null, "");
	}
	
	/**
	 * 고객 수정 Kakao 인증
	 */
	@Transactional
	public Map<String, Object> updateCustomer(Map<String, Object> user, String userId, String userName, String kakaoCrtfc) {
		
		return updateCustomer(user, userId, userName, null, null, kakaoCrtfc);
	}

	/**
	 * 고객 수정
	 */
	@Transactional
	public Map<String, Object> updateCustomer(Map<String, Object> user, String userId, String userName
			, String deviceType, HttpHeaders headers, HttpServletRequest request) {

		return updateCustomer(user, userId, userName, deviceType, htUtils.getRemoteIpAddress(headers, request), null);
	}

	/**
	 * 고객 수정
	 */
	@Transactional
	public Map<String, Object> updateCustomer(Map<String, Object> user, String userId, String userName
			, String deviceType, String ipAddr, String kakaoCrtfc) {

		log.debug("UPDATE CUSTOMER");

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", user.get("cstm_uid"));
		params.put("cocId", userId);
		if (!Strings.isNullOrEmpty(userName)) {
			params.put("name", userName);
			params.put("cocNm", userName);
		}
		if (!Strings.isNullOrEmpty(kakaoCrtfc)) {	//카카오인증채널 
			params.put("kakaoCrtfc", kakaoCrtfc);
		}
		params.put("cstmLinkDivCd", user.get("cstm_link_div_cd"));
		params.put("linkIp", ipAddr);
		params.put("loginYn", userId == null ? "N" : "Y");
		params.put("updater", customProperty.getSystemMemeberUid());
		saveCustomer(params);

		return selectCustomerByCstmUid((String) params.get("cstmUid"));
	}

	/**
	 * 고객 조회 (세션에서 조회, {@code cocId}로 조회, {@code cstmUid}로 조회)
	 */
	public Map<String, Object> selectCustomer(HttpSession session, String cocId, String cstmUid) {

		@SuppressWarnings("unchecked")
		Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");
		if (user != null) {
			return user;
		} else if (!Strings.isNullOrEmpty(cocId)) {
			user = selectCustomerByCocId(cocId);
			session.setAttribute("user", user);
			return user;
		} else if (!Strings.isNullOrEmpty(cstmUid)) {
			user = selectCustomerByCstmUid(cstmUid);
			session.setAttribute("user", user);
			return user;
		}

		return null;
	}

	/**
	 * 고객 조회 ({@code cocId}로 조회, {@code cstmUid}로 조회)
	 */
	public Map<String, Object> selectCustomer(String cocId, String cstmUid) {

		if (!Strings.isNullOrEmpty(cocId)) {
			return selectCustomerByCocId(cocId);
		} else if (!Strings.isNullOrEmpty(cstmUid)) {
			return selectCustomerByCstmUid(cstmUid);
		}

		return null;
	}

	/**
	 * 고객 조회 ({@code cstmUid}로 조회)
	 */
	public Map<String, Object> selectCustomerByCstmUid(String cstmUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", cstmUid);
		return memberDao.selectCustomer(params);
	}

	/**
	 * 고객 조회 ({@code cocId}로 조회)
	 */
	public Map<String, Object> selectCustomerByCocId(String cocId) {

		Map<String, Object> params = new HashMap<>();
		params.put("cocId", cocId);
		return memberDao.selectCustomer(params);
	}
	
	/**
	 * 고객 조회 채널 포함 ({@code cocId}로 조회)
	 */
	public Map<String, Object> selectCustomerByCocId(Map<String, Object> param) {
		return memberDao.selectCustomer(param);
	}


	/**
	 * 고객 조회 (채널 정보로 조회)
	 */
	public Map<String, Object> selectCustomerByChannel(String channelId, String channelServiceId, String channelCustomerId) {

		Map<String, Object> params = new HashMap<>();
		params.put("cstmLinkDivCd", channelId);
		params.put("cstmLinkServiceUid", channelServiceId);
		params.put("cstmLinkCustomerUid", channelCustomerId);
		return memberDao.selectCustomer(params);
	}

	/**
	 * 고객 조회 (채팅방 정보로 조회)
	 */
	public Map<String, Object> selectCustomerByChatRoom(@NotEmpty String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		return memberDao.selectCustomerByChatRoom(params);
	}

	/**
	 * 고객 저장
	 */
	@Transactional
	public int saveCustomer(Map<String, Object> param) {

		String name = (String) param.get("name");
		if (!Strings.isNullOrEmpty(name)
				&& "undefined".equals(name)) {
			log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			log.error("UNDEFINED");
			log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}

		return memberDao.saveCustomer(param);
	}

	/**
	 * 고객 삭제 (테스트용)
	 */
	@Transactional
	public int deleteCustomer(String cstmUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", cstmUid);

		return memberDao.deleteCustomer(params);
	}
	/**
	 * 고객 삭제 (카카오) - 30일
	 */
	@Transactional
	public int deleteCustomerKakao(String cstmUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", cstmUid);

		return memberDao.deleteCustomerKakao();
	}
	
	/**
	 * 고객 삭제 (카카오) - 고객실행
	 */
	@Transactional
	public int deleteCustomerKakaoByCstm(String cstmUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("cstmUid", cstmUid);

		return memberDao.deleteCustomerKakaoByCstm(params);
	}

	/**
	 * 고객별 고객구분(코끼리, VIP 등) 정보 저장
	 */
	@Transactional
	public int saveCustomerGradeHistory(Map<String, Object> param) {

		return memberDao.mergeCstmGradeHis(param);
	}

	/**
	 * 고객 민감정보 조회
	 */
	public Map<String, Object> selectCstmSinfo(String chatRoomUid) throws Exception {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		Map<String, Object> sinfo = memberDao.selectCstmSinfo(sqlParams);

		if (sinfo != null) {
			log.info("sinfo: {}", sinfo);
			try {
				if (!Strings.isNullOrEmpty((String) sinfo.get("etc"))) {
					sinfo.put("etc", cipherUtils.decrypt((String) sinfo.get("etc")));
				}
				if (!Strings.isNullOrEmpty((String) sinfo.get("card_no1"))) {
					sinfo.put("card_no1", cipherUtils.decrypt((String) sinfo.get("card_no1")));
					sinfo.put("card_no2", cipherUtils.decrypt((String) sinfo.get("card_no2")));
					sinfo.put("card_no3", cipherUtils.decrypt((String) sinfo.get("card_no3")));
					sinfo.put("card_no4", cipherUtils.decrypt((String) sinfo.get("card_no4")));
				}
				if (!Strings.isNullOrEmpty((String) sinfo.get("tel_no1"))) {
					sinfo.put("tel_no1", cipherUtils.decrypt((String) sinfo.get("tel_no1")));
					sinfo.put("tel_no2", cipherUtils.decrypt((String) sinfo.get("tel_no2")));
					sinfo.put("tel_no3", cipherUtils.decrypt((String) sinfo.get("tel_no3")));
				}
				if (!Strings.isNullOrEmpty((String) sinfo.get("account_no"))) {
					sinfo.put("account_no", cipherUtils.decrypt((String) sinfo.get("account_no")));
				}
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
				return sinfo;
			}
		}

		return sinfo;
	}

	/**
	 * 고객 민감정보 저장
	 */
	@Transactional
	public int saveCstmSinfo(Map<String, Object> params) throws Exception {

		if (!Strings.isNullOrEmpty((String) params.get("etc"))) {
			params.put("etc", cipherUtils.encrypt((String) params.get("etc")));
		} else if (!Strings.isNullOrEmpty((String) params.get("cardNo1"))) {
			params.put("cardNo1", cipherUtils.encrypt((String) params.get("cardNo1")));
			params.put("cardNo2", cipherUtils.encrypt((String) params.get("cardNo2")));
			params.put("cardNo3", cipherUtils.encrypt((String) params.get("cardNo3")));
			params.put("cardNo4", cipherUtils.encrypt((String) params.get("cardNo4")));
		} else if (!Strings.isNullOrEmpty((String) params.get("telNo1"))) {
			params.put("telNo1", cipherUtils.encrypt((String) params.get("telNo1")));
			params.put("telNo2", cipherUtils.encrypt((String) params.get("telNo2")));
			params.put("telNo3", cipherUtils.encrypt((String) params.get("telNo3")));
		} else if (!Strings.isNullOrEmpty((String) params.get("accountNo"))) {
			params.put("accountNo", cipherUtils.encrypt((String) params.get("accountNo")));
		}

		return memberDao.saveCstmSinfo(params);
	}

	/**
	 * 고객 민감정보 삭제
	 */
	@Transactional
	public int deleteCstmSinfo(String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		return memberDao.deleteCstmSinfo(sqlParams);
	}

	/**
	 * 기간계 고객 조회
	 */
	public Map<String, Object> selectCustomerSinfo(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		return customerDao.selectCustomerSinfo(sqlParams);
	}

	/**
	 * 콜상담이력 조회
	 */
	public List<Map<String, Object>> selectCallHistList(@NotEmpty String chatRoomUid){

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		return customerDao.selectCallHistList(sqlParams);
	}

	/**
	 * 계약정보 목록 조회
	 */
	public List<Map<String, Object>> selectCustInfoList(@NotEmpty String chatRoomUid){

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		return customerDao.selectCustInfoList(sqlParams);
	}


}
