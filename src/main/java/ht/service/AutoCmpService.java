package ht.service;

import static ht.constants.CommonConstants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import ht.domain.ApiItemWrapper;
import ht.domain.ApiReturnCode;
import ht.domain.MemberVO;
import ht.persistence.AutoCmpDao;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Service
@Slf4j
public class AutoCmpService {

	@Resource
	private AutoCmpDao autoCmpDao;

	/**
	 * 자동완성 목록 조회
	 */
	public List<Map<String, Object>> selectAutoCmpListForCounselor(Map<String, Object> params) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("forCounselor", "forCounselor");
		sqlParams.put("counselorUid", params.get("memberUid"));
		sqlParams.put("counselorDepartCd", params.get("departCd"));
		sqlParams.put("autoCmpDiv", params.get("autoCmpDiv"));
		sqlParams.put("schText", params.get("schText"));
		sqlParams.put("autoCmpCus", params.get("autoCmpCus"));
		
		List<Map<String, Object>> resultList = autoCmpDao.selectAutoCmpList(sqlParams);
		
		return resultList;
	}

	/**
	 * 자동완성 목록 조회
	 */
	public List<Map<String, Object>> selectAutoCmpList(Map<String, Object> params) {
		return autoCmpDao.selectAutoCmpList(params);
	}

	/**
	 * 자동완성 저장
	 */
	public ApiItemWrapper<String> saveAutoCmp(Map<String, Object> params) {
		
		params.put("content",StringUtil.nvl(params.get("content"), "").replaceAll("(?i)script", " ") );

		if (Strings.isNullOrEmpty((String) params.get("autoCmpId"))) {
			return insertAutoCmp(params);
		} else {
			return updateAutoCmp(params);
		}
	}

	/**
	 * 자동완성 등록
	 */
	public ApiItemWrapper<String> insertAutoCmp(Map<String, Object> params) {

		String autoCmpDiv = (String) params.get("autoCmpDiv");
		String autoCmpCUs = (String) params.get("autoCmpCus");
		String memberUid = (String) params.get("memberUid");

		// 후처리 자동완성
		if ("A".equals(autoCmpDiv)) {
			if ("C".equals(autoCmpCUs)) { // 공용은 부서별로만 카운트
				params.remove("memberUid");
			}
			int count = autoCmpDao.selectAutoCmpCnt(params);
			log.debug("TOTAL AUTO COMPLETE COUNT: {}", count);
			if (count >= 100) {
				return new ApiItemWrapper<>(ApiReturnCode.FAILED, "후처리용 자동완성은 100건까지 등록가능합니다.");
			}
		}

		// 고객 답변용 상담 자동완성
		if ("C".equals(autoCmpDiv)) {
			if ("C".equals(autoCmpCUs)) { // 공용은 부서별로만 카운트
				params.remove("memberUid");
			}
			int count = autoCmpDao.selectAutoCmpCnt(params);
			log.debug("TOTAL AUTO COMPLETE COUNT: {}", count);
			if (count >= 100) {
				return new ApiItemWrapper<>(ApiReturnCode.FAILED, "고객 답변용 자동완성은 100건까지 등록가능합니다.");
			}
		}

		params.put("memberUid", memberUid);
		autoCmpDao.insertAutoCmp(params);

		return new ApiItemWrapper<>(ApiReturnCode.SUCCEED, "정상적으로 처리되었습니다.");
	}

	/**
	 * 자동완성 수정
	 */
	public ApiItemWrapper<String> updateAutoCmp(Map<String, Object> params) {

		autoCmpDao.updateAutoCmp(params);
		return new ApiItemWrapper<>(ApiReturnCode.SUCCEED, "정상적으로 처리되었습니다.");
	}

	/**
	 * 자동완성 삭제
	 */
	public int deleteAutoCmp(Long autoCmpId, MemberVO member) {

		Map<String, Object> sqlParams = new HashMap<>();
		if (MEMBER_DIV_CD_M.equals(member.getMemberDivCd())
				|| MEMBER_DIV_CD_A.equals(member.getMemberDivCd())
				|| MEMBER_DIV_CD_S.equals(member.getMemberDivCd())) {
			sqlParams.put("autoCmpId", autoCmpId);
		} else {
			sqlParams.put("autoCmpId", autoCmpId);
			sqlParams.put("memberUid", member.getMemberUid());
		}

		return autoCmpDao.deleteAutoCmp(sqlParams);
	}
}
