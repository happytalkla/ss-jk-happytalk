package ht.service.legacy;

import ht.persistence.legacy.MappingCustomerDao;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

/**
 * 기간계 고객 정보 (DB 사용)
 */
@Service
public class MappingCustomerService {

	@Resource
	private MappingCustomerDao mappingCustomerDao;

	/**
	 * 조회
	 */
	public Map<String, Object> selectMappingCustomer(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);

		return mappingCustomerDao.selectMappingCustomer(sqlParams);
	}

	/**
	 * 조회
	 */
	public Map<String, Object> selectMappingCustomer(@NotEmpty String chatRoomUid, @NotEmpty String lonRcvNo) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		sqlParams.put("lonRcvNo", lonRcvNo);

		return mappingCustomerDao.selectMappingCustomer(sqlParams);
	}

	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectMappingCustomerList(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		sqlParams.put("useYn", "Y");
		sqlParams.put("notLonBlamt", "0");

		return mappingCustomerDao.selectMappingCustomerList(sqlParams);
	}
	
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectMappingCustomerNotlonList(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		sqlParams.put("useYn", "Y");

		return mappingCustomerDao.selectMappingCustomerList(sqlParams);
	}

	/**
	 * 송금 완료 목록 조회
	 */
	public List<Map<String, Object>> selectPayCompleteList(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		sqlParams.put("useYn", "Y");
		sqlParams.put("rcvStsCdNot", "00000");
		sqlParams.put("rcvDtLessDay", 14); // 최근 14일 데이터만

		return mappingCustomerDao.selectMappingCustomerList(sqlParams);
	}

	/**
	 * 콜 목록 조회
	 */
	public List<Map<String, Object>> selectMappingCustomerCallHistoryList(@NotEmpty String chatRoomUid) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("chatRoomUid", chatRoomUid);
		sqlParams.put("useYn", "Y");

		return mappingCustomerDao.selectMappingCustomerCallHistoryList(sqlParams);
	}
}
