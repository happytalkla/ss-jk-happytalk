package ht.controller;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ht.config.CustomProperty;
import ht.domain.ApiItemWrapper;
import ht.domain.ApiReturnCode;
import ht.domain.MemberVO;
import ht.service.AutoCmpService;
import ht.service.MemberAuthService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 자동완성 관련 controller
 * 자동완성 관리
 *
 * @author wizard
 *
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(path = "/autoCmp")
@Slf4j
public class AutoCmpController {

	@Resource
	private AutoCmpService autoCmpService;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private HTUtils htUtils;

	/**
	 * 자동완성 목록 조회
	 */
	@RequestMapping(value = "/selectAutoCmpList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectAutoCmpList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			Locale locale, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			ModelMap model, @RequestParam Map<String, Object> param) {

		log.info("selectAutoCmpList, {}", param);

		try {
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			String departCd = StringUtil.nvl(param.get("departCd"), memberVO.getDepartCd());
			param.put("departCd", departCd);
			model.put("departCd", departCd);

			String autoCmpDiv = StringUtil.nvl(param.get("autoCmpDiv"), "C");
			param.put("autoCmpDiv", autoCmpDiv);
			model.put("autoCmpDiv", autoCmpDiv);

			String autoCmpCus=StringUtil.nvl(param.get("autoCmpCus"));
			model.put("autoCmpCus", autoCmpCus);

			String schText = StringUtil.nvl(param.get("schText"));
			schText = StringEscapeUtils.escapeHtml4(schText);
			log.info("@@selectAutoCmpList schText :, {}", schText);
			model.put("schText", schText);
			param.put("schText", schText);

			model.put("resultList", autoCmpService.selectAutoCmpList(param));
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "autoCmp/autoCmpManage";
	}

	/**
	 * 자동완성 목록 조회
	 */
	@RequestMapping(value = "/selectAutoCmpListAjax", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Map<String, Object>> selectAutoCmpListAjax(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,Locale locale, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("selectAutoCmpListAjax, {}", param);

		try {
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);
			param.put("memberUid", memberVO.getMemberUid());

			String departCd = StringUtil.nvl(param.get("departCd"), memberVO.getDepartCd());
			param.put("departCd", departCd);
			model.put("departCd", departCd);

			String autoCmpDiv = StringUtil.nvl(param.get("autoCmpDiv"), "C");
			param.put("autoCmpDiv", autoCmpDiv);
			model.put("autoCmpDiv", autoCmpDiv);

			String autoCmpCus = StringUtil.nvl(param.get("schAutoCmpCus"));
			model.put("autoCmpCus", autoCmpCus);
			param.put("autoCmpCus", autoCmpCus);

			String schText = StringUtil.nvl(param.get("schText"));
			schText = StringEscapeUtils.escapeHtml4(schText);
			model.put("schText", schText);
			param.put("schText", schText);

			return autoCmpService.selectAutoCmpListForCounselor(param);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			return Collections.emptyList();
		}
	}

	/**
	 * 자동완성 저장
	 */
	@RequestMapping(value = "/saveAutoCmp", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ApiItemWrapper<String> saveAutoCmp(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,Locale locale, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.info("saveAutoCmp, {}", param);

		try {
			//			MemberVO memberVO = MemberAuthService.getCurrentUser();
			//			param.put("memberUid", memberVO.getMemberUid());
			return autoCmpService.saveAutoCmp(param);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ApiItemWrapper<>(ApiReturnCode.FAILED);
	}

	/**
	 * 자동완성 삭제
	 */
	@RequestMapping(value = "/deleteAutoCmp", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ApiItemWrapper<String> deleteAutoCmp(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,Locale locale, ModelMap model,
			@RequestParam("autoCmpId") @NotNull @Positive Long autoCmpId) {

		log.info("deleteAutoCmp, autoCmpId: {}", autoCmpId);

		try {
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			autoCmpService.deleteAutoCmp(autoCmpId, memberVO);
			return new ApiItemWrapper<>();
		} catch(Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			return new ApiItemWrapper<>(ApiReturnCode.FAILED, "처리중 오류가 발생하였습니다.");
		}
	}
}
