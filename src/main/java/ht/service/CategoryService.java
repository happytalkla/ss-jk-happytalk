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
import org.springframework.util.MultiValueMap;

import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.persistence.CategoryDao;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService {

	@Resource
	private CategoryDao categoryDao;

	/**
	 * 분류조회 관리자용 1건용
	 */
	public Map<String, Object> selectCategoryManager(Map<String, Object> params) {
		return categoryDao.selectCategory(params);
	}

	/**
	 * 분류 목록 조회 관리자용 리스트용
	 */
	public List<Map<String, Object>> selectCategoryListManager(Map<String, Object> params) {
		return categoryDao.selectCategoryList(params);
	}

	/**
	 * 분류 조회
	 */
	public Map<String, Object> selectCategory(String ctgNum) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("ctgNum", ctgNum);
		return categoryDao.selectCategory(sqlParams);
	}

	/**
	 * 분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryList(String upperCtgNum) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("upperCtgNum", upperCtgNum);
		return categoryDao.selectCategoryList(sqlParams);
	}

	/**
	 * 분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryList(Map<String, Object> params) {

		return categoryDao.selectCategoryList(params);
	}

	/**
	 * 최상위 분류 목록 조회
	 */
	public List<Map<String, Object>> selectCategoryTopList() {
		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("ctgDpt", "1");
		sqlParams.put("useYn", "Y");
		// 부서코드, 등록및수정 권한 추가
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		sqlParams.put("departCd", memberVO.getDepartCd());
		sqlParams.put("memberDivCd", memberVO.getMemberDivCd());

		return categoryDao.selectCategoryList(sqlParams);
	}

	/**
	 * 기본 분류 여부
	 */
/*
	public boolean isDefaultCtgNum(@NotEmpty String ctgNum) {

		Map<String, Object> defaultCategory = selectDefaultCategory();
		Assert.notNull(defaultCategory, "NO DEFAULT CATEGORY");
		String defaultCtgNum = (String) defaultCategory.get("ctg_num");
		Assert.notNull(defaultCtgNum, "NO DEFAULT CATEGORY");

		return ctgNum.equals(defaultCtgNum);
	}
*/
	/**
	 * 기본 분류 식별자 조회
	 */
	
	/*
	public String selectDefaultCtgNum() {

		Map<String, Object> defaultCategory = selectDefaultCategory();
		Assert.notNull(defaultCategory, "NO DEFAULT CATEGORY");
		String defaultCtgNum = (String) defaultCategory.get("ctg_num");
		Assert.notNull(defaultCtgNum, "NO DEFAULT CATEGORY");

		return defaultCtgNum;
	}
	*/
	
	/**
	 * 부서별 분류 식별자 조회
	 */
	public String selectDefaultCtgNum(String departCd) {

		Map<String, Object> defaultCategory = selectDefaultCategory(departCd);
		Assert.notNull(defaultCategory, "NO DEFAULT CATEGORY");
		String defaultCtgNum = (String) defaultCategory.get("ctg_num");
		Assert.notNull(defaultCtgNum, "NO DEFAULT CATEGORY");

		return defaultCtgNum;
	}

	/**
	 * 부서별 기본 분류 조회
	 */
	public Map<String, Object> selectDefaultCategory(String departCd) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("departCd", departCd);
		sqlParams.put("ctgDpt", 2);
		sqlParams.put("dftCtgYn", "Y");
		return categoryDao.selectCategory(sqlParams);
	}
	
	/**
	 * 채널별 기본 분류 조회
	 */
	public Map<String, Object> selectDefaultCategoryByChannel(String channel, String departCd) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("cstmLinkDivCd", channel);
		sqlParams.put("ctgDpt", 2);
		sqlParams.put("dftCtgYn", "Y");
		sqlParams.put("departCd", departCd);
		return categoryDao.selectCategory(sqlParams);
	}
	
	/**
	 * 채널별 기본 분류 조회
	 */
	public Map<String, Object> selectDefaultCategoryByChannel(String channel) {

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("cstmLinkDivCd", channel);
		sqlParams.put("ctgDpt", 2);
		sqlParams.put("dftCtgYn", "Y");
		if(CommonConstants.CSTM_LINK_DIV_CD_B.equals(channel)){
			sqlParams.put("departCd", "DC");
		}
		return categoryDao.selectCategory(sqlParams);
	}

	/**
	 * 분류 등록
	 */
	@Transactional
	public void insertCategory(Map<String, Object> param) {
		categoryDao.insertCategory(param);
	}

	/**
	 * 분류 수정
	 */
	@Transactional
	public void updateCategory(Map<String, Object> param) {
		categoryDao.updateCategory(param);
	}

	/**
	 * 분류 삭제
	 */
	@Transactional
	public void deleteCategory(Map<String, Object> param) {
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("ctgNum", param.get("ctgNum"));
		Map<String, Object> category = categoryDao.selectCategory(sqlParam);
		if (category != null) {
			if ("Y".equals(category.get("dft_ctg_yn"))) {
				throw new UnsupportedOperationException("기본 분류는 삭제할 수 없습니다.");
			} else {
				sqlParam = new HashMap<>();
				sqlParam.put("upperCtgNum", category.get("ctg_num"));
				sqlParam.put("dftCtgYn", "Y");
				List<Map<String, Object>> subCategory = categoryDao.selectCategoryList(sqlParam);
				log.info("subCategory: {}", subCategory);
				if (subCategory.size() > 0) {
					throw new UnsupportedOperationException("기본 분류를 포함한 분류는 삭제할 수 없습니다.");
				}
			}
			categoryDao.deleteCategory(param);
		}
	}

	/**
	 * 상담원 매핑용 분류 목록 조회
	 */
	public List<Map<String, Object>> selectAllCategoryList(String ctgMgtDpt) {
		if (ctgMgtDpt.equals(CommonConstants.DEFAULT_CTG_MGT_DPT)) {
			return categoryDao.selectAllCategoryDpt2List();
		} else {
			return categoryDao.selectAllCategoryDpt3List();
		}
	}

	/**
	 * 카테고리 분류 정보
	 */
	public List<Map<String, Object>> selectAllCategoryDpt21List(Map<String, Object> param) {
		return categoryDao.selectAllCategoryDpt21List(param);
	}

	public List<Map<String, Object>> selectCtgMemberMapping(Map<String, Object> param) {
		return categoryDao.selectCtgMemberMapping(param);
	}

	/**
	 * 분류 매핑용 상담원 목록 조회
	 */
	public List<Map<String, Object>> selectMatchCnsrList(Map<String, Object> param) {
		return categoryDao.selectMatchCnsrList(param);
	}

	/**
	 * 상담분류에 상담원 매칭
	 */
	@Transactional
	public void insertMatchCnsr(Map<String, Object> param, MultiValueMap<String, String> lparam) {
		// 기존 분류에 매칭된 상담원 삭제
		categoryDao.deleteMatchCnsr(param);

		// 신규 매칭된 상담원 등록
		List<String> memberUidArr = StringUtil.nvlList(lparam.get("memberUidArr"));
		if (memberUidArr.size() > 0) {
			param.put("memberUidArr", memberUidArr);
			categoryDao.insertMatchCnsr(param);
		}
	}

	/*	*//**
			 * 종료 분류 목록 Top (1-Depth)
			 */
	/*
	 * public List<Map<String, Object>> selectEndCategoryListTop() {
	 * 
	 * Map<String, Object> params = new HashMap<>(); params.put("delYn", "N");
	 * params.put("ctgDpt", 1);
	 * 
	 * try { return categoryDao.selectEndCategoryList(params); } catch (Exception e)
	 * { return Collections.emptyList(); } }
	 * 
	 * 
	 * 
	 *//**
		 * 종료 분류 하위 목록
		 */
	/*
	 * public List<Map<String, Object>> selectEndCategoryListByCtgCd(String
	 * upperCtgCd, String depthNum) {
	 * 
	 * Map<String, Object> params = new HashMap<>(); params.put("delYn", "N");
	 * params.put("depthCd", "depth" + depthNum + "_cd"); params.put("depthNm",
	 * "depth" + depthNum + "_cd_nm"); if(Integer.parseInt(StringUtil.nvl(depthNum))
	 * > 1) { params.put("srcDepthCd", "depth" + (Integer.parseInt(depthNum)-1) +
	 * "_cd"); } params.put("srcKeyword", upperCtgCd);
	 * 
	 * try { return categoryDao.selectEndCategoryList(params); } catch (Exception e)
	 * { return Collections.emptyList(); } }
	 * 
	 *//**
		 * 종료 분류 검색 목록
		 */
	/*
	 * public List<Map<String, Object>> selectEndCategoryListByCtgNm(String
	 * srcKeyword, String depthNum) {
	 * 
	 * Map<String, Object> params = new HashMap<>(); params.put("delYn", "N");
	 * params.put("depthCd", "depth1_cd, depth2_cd, depth3_cd, depth4_cd");
	 * params.put("depthNm",
	 * "depth1_cd_nm, depth2_cd_nm, depth3_cd_nm, depth4_cd_nm");
	 * if(Integer.parseInt(StringUtil.nvl(depthNum)) > 1) { params.put("srcDepthCd",
	 * "depth4_cd_nm"); } params.put("srcKeyword", "%" + srcKeyword + "%");
	 * 
	 * try { return categoryDao.selectEndCategorySrcList(params); } catch (Exception
	 * e) { return Collections.emptyList(); } }
	 * 
	 *//**
		 * 종료 분류 조회
		 */
	/*
	 * public Map<String, Object> selectEndCategory(String joinedCategogyCode) {
	 * 
	 * Map<String, Object> params = new HashMap<>(); params.put("depthCd",
	 * "depth1_cd, depth2_cd, depth3_cd, depth4_cd, depth4_cd");
	 * params.put("depthNm",
	 * "depth1_cd_nm, depth2_cd_nm, depth3_cd_nm, depth4_cd_nm, depth4_cd_nm");
	 * 
	 * String[] categoryCodeList = joinedCategogyCode.split("\\|"); if
	 * (categoryCodeList.length == 4) { params.put("depth1Cd", categoryCodeList[0]);
	 * params.put("depth2Cd", categoryCodeList[1]); params.put("depth3Cd",
	 * categoryCodeList[2]); params.put("depth4Cd", categoryCodeList[3]); return
	 * categoryDao.selectEndCategory(params); }
	 * 
	 * return null; }
	 * 
	 *//**
		 * 종료 분류 검색 목록
		 */
	/*
	 * public List<Map<String, Object>> selectEndCategoryListByCtgNm(String
	 * srcKeyword) {
	 * 
	 * return selectEndCategoryListByCtgNm(srcKeyword, "0"); }
	 * 
	 *//**
		 * 종료 분류 목록 기본 출력용
		 *//*
			 * public List<Map<String, Object>> selectEndCategoryList(String depthNum) {
			 * 
			 * Map<String, Object> params = new HashMap<>(); params.put("delYn", "N");
			 * params.put("depthCd", "depth" + depthNum + "_cd"); params.put("depthNm",
			 * "depth" + depthNum + "_cd_nm"); try { return
			 * categoryDao.selectEndCategoryList(params); } catch (Exception e) { return
			 * Collections.emptyList(); } }
			 */
}
