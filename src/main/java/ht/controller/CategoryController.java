package ht.controller;

import static ht.constants.CommonConstants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ht.domain.MemberVO;
import ht.service.MemberAuthService;
import ht.service.CategoryService;
import ht.service.SettingService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 셋팅 관련 controller
 * 상담 분류 관리
 * 상담 종료 분류 관리
 * 상담 배분 관리
 *
 * @author wizard
 *
 */
@Controller
@RequestMapping(path = "/category")
@Slf4j
public class CategoryController {

	@Resource
	private SettingService settingService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private HTUtils htUtils;

	/**
	 * 상담 분류 관리 페이지로 이동
	 */
	@RequestMapping(value = "/selectCategory", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCategory(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectCategory: {}", param);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("memberDivCd", memberVO.getMemberDivCd());

			// 1 depth 카테고리 설정
			param.put("ctgNum", null);
			param.put("ctgDpt", "1");

			// 기본 설정 정보 조회
			Map<String, Object> defaultSet = settingService.selectDefaultSet();
			model.put("defaultSet", defaultSet);

			//채널 코드 리스트 조회
			List<Map<String, Object>> selectChannelList = settingService.selectChannelList(param);
			model.put("selectChannelList", selectChannelList);
			
			List<Map<String, Object>> categoryList = categoryService.selectCategoryListManager(param);
			model.put("categoryList", categoryList);

			// }catch(BizException be){
			// //be.printStackTrace();
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "category/categoryManage";
	}

	/**
	 * 상담 분류 목록 조회
	 */
	@RequestMapping(value = "/selectCategoryAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<Map<String, Object>> selectCategoryAjax(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectCategoryAjax: {}", param);

		List<Map<String, Object>> categoryList = null;

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("memberDivCd", memberVO.getMemberDivCd());

			String ctgNum = param.get("ctgNum").toString();
			if(ctgNum.equals("0") || ctgNum.equals(null)) {	// 1 depth 카테고리
				param.put("ctgNum", null);
				param.put("ctgDpt", "1");
			}
			else { // 중분류 이하
				param.put("upperCtgNum", ctgNum);
				param.put("ctgNum", null);
			}
			/// 등록 수정권한 + 부서코드 분기 처리
			if(MEMBER_DIV_CD_S.equals(memberVO.getMemberDivCd())) {
				param.put("departCd", null);
			} else {
				param.put("useYn", "Y");
				param.put("departCd", memberVO.getDepartCd());
			}

			categoryList = categoryService.selectCategoryListManager(param);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return categoryList;
	}

	/**
	 * 상담분류 추가
	 */
	@RequestMapping(value = "/insertCategory", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> insertCategory(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("insertCategory: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			categoryService.insertCategory(param);

			rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 상담분류 수정
	 */
	@RequestMapping(value = "/updateCategory", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateCategory(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("updateCategory: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			categoryService.updateCategory(param);

			rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 상담분류 삭제
	 */
	@RequestMapping(value = "/deleteCategory", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteCategory(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("deleteCategory: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			categoryService.deleteCategory(param);
			rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 삭제 되었습니다.");

			//		} catch (UnsupportedOperationException e) {
			//			log.error("{}", e.getLocalizedMessage());
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", e.getLocalizedMessage());
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 상담 배분 관리 페이지로 이동
	 */
	@RequestMapping(value = "/selectCategoryMatch", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectCategoryMatch(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectCategoryMatch: {}", param);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			// 기본 설정 정보 조회
			Map<String, Object> defaultSet = settingService.selectDefaultSet();
			model.put("defaultSet", defaultSet);

			String ctgMgtDpt = StringUtil.nvl(defaultSet.get("ctg_mgt_dpt"), DEFAULT_CTG_MGT_DPT);

			// 카테고리 상담직원 매핑 정보
			List<Map<String, Object>> categoryList = categoryService.selectAllCategoryList(ctgMgtDpt);

			model.put("categoryList", categoryList);
			model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "category/categoryMatchManage";
	}

	/**
	 * 분류 매핑용 상담직원 목록 조회
	 */
	@RequestMapping(value = "/selectMatchCnsrList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectMatchCnsrList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectMatchCnsrList: {}", param);

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("departCd", memberVO.getDepartCd());

			// 분류 매핑용 상담직원 목록 조회
			List<Map<String, Object>> counselorList = categoryService.selectMatchCnsrList(param);

			model.put("counselorList", counselorList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return "category/matchCounselorList";
	}

	/**
	 * 상담분류에 상담직원 매칭
	 */
	@RequestMapping(value = "/insertMatchCnsr", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> insertMatchCnsr(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, @RequestParam Map<String, Object> param,
			@RequestParam MultiValueMap<String, String> lparam) {

		log.info("insertMatchCnsr: {}", param);

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			categoryService.insertMatchCnsr(param, lparam);

			rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}
}
