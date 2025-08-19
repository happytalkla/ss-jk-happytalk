package ht.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.persistence.EndCtgDao;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EndCtgService {

	@Resource
	private EndCtgDao endCtgDao;

	/**
	 * 분류조회 관리자용 1건용
	 * */
	public Map<String, Object> selectCategoryManager(Map<String, Object> params) {
		return endCtgDao.selectCategory(params);
	}

	/**
	 * 분류 목록 조회 관리자용 리스트용
	 */
	public List<Map<String, Object>> selectCategoryListManager(Map<String, Object> params) {
		return endCtgDao.selectCategoryList(params);
	}


	/**
	 * 분류 조회
	 */
	public Map<String, Object> selectCategory(String ctgNum) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("ctgNum", ctgNum);
		return endCtgDao.selectCategory(sqlParams);
	}
	
	/**
	 * 분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryList(Integer ctgNum) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("ctgNum", ctgNum);
		return endCtgDao.selectCategoryList(sqlParams);
	}

	/**
	 * 분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryList(Map<String, Object> params) {

		return endCtgDao.selectCategoryList(params);
	}

	/**
	 * 최상위 분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryTopList() {
		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("ctgDpt", "1");
		sqlParams.put("useYn", "Y");
		//부서코드, 등록및수정 권한 추가
//		MemberVO memberVO = MemberAuthService.getCurrentUser();
//		sqlParams.put("departCd", memberVO.getDepartCd());

		return endCtgDao.selectCategoryList(sqlParams);
	}
	/**
	 * 중분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryLv2List() {
		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("ctgDpt", "2");
		sqlParams.put("useYn", "Y");
		//부서코드, 등록및수정 권한 추가
//		MemberVO memberVO = MemberAuthService.getCurrentUser();
//		sqlParams.put("departCd", memberVO.getDepartCd());

		return endCtgDao.selectCategoryList(sqlParams);
	}
	/**
	 * 소분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryLv3List() {
		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("ctgDpt", "3");
		sqlParams.put("useYn", "Y");
		//부서코드, 등록및수정 권한 추가
//		MemberVO memberVO = MemberAuthService.getCurrentUser();
//		sqlParams.put("departCd", memberVO.getDepartCd());

		return endCtgDao.selectCategoryList(sqlParams);
	}	
	/**
	 * 종료 분류 검색 목록
	 */
	public List<Map<String, Object>> selectEndCategoryListByCtgNm(String keyword) {

		return selectEndCategoryListByCtgNm(keyword, 1);
	}
	/**
	 * 종료 분류 검색 목록
	 */
	public List<Map<String, Object>> selectEndCategoryListByCtgNm(String keyword, int ctgDpt) {

		Map<String, Object> params = new HashMap<>();
		params.put("delYn", "N");
		params.put("keyword", "%" + keyword + "%");
		params.put("ctgDpt", ctgDpt);

		try {
			return endCtgDao.selectCategoryList(params);
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	/**
	 * 분류 등록
	 */
	@Transactional
	public void insertCategory(Map<String, Object> param) {
		endCtgDao.insertCategory(param);
	}

	/**
	 * 분류 수정
	 */
	@Transactional
	public void updateCategory(Map<String, Object> param) {
		endCtgDao.updateCategory(param);
	}

	/**
	 * 분류 삭제
	 */
	@Transactional
	public void deleteCategory(Map<String, Object> param) {
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("ctgNum", param.get("ctgNum"));
		Map<String, Object> category = endCtgDao.selectCategory(sqlParam);
		if (category != null) {
			if ("Y".equals(category.get("dft_ctg_yn"))) {
				throw new UnsupportedOperationException("기본 분류는 삭제할 수 없습니다.");
			} else {
				sqlParam = new HashMap<>();
				sqlParam.put("upperCtgNum", category.get("ctg_num"));
				sqlParam.put("dftCtgYn", "Y");
				List<Map<String, Object>> subCategory = endCtgDao.selectCategoryList(sqlParam);
				log.info("subCategory: {}", subCategory);
				if (subCategory.size() > 0) {
					throw new UnsupportedOperationException("기본 분류를 포함한 분류는 삭제할 수 없습니다.");
				}
			}
			endCtgDao.deleteCategory(param);
		}
	}

}
