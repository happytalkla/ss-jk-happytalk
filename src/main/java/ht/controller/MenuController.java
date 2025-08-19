package ht.controller;

import static ht.constants.CommonConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ht.domain.MemberVO;
import ht.domain.MenuVO;
import ht.service.MemberAuthService;
import ht.service.MenuService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 메뉴 관련 controller
 * 메뉴 관리
 *
 * @author wizard
 *
 */
@Controller
@RequestMapping(path = "/menu")
@Slf4j
public class MenuController {

	@Resource
	private MenuService menuService;

	/**
	 * 메뉴 목록 조회
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@GetMapping(value = "/selectMenuListAjax")
	public @ResponseBody List<MenuVO> selectMenuListAjax(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,Locale locale, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("MenuController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());
		MenuVO vo = new MenuVO();

		MemberVO memberVO = MemberAuthService.getCurrentUser();
		List<MenuVO> selectMenuList = new ArrayList<>();
		try {
			String topMenu = StringUtil.nvl(request.getParameter("topMenu"));
			if ("".equals(topMenu)) {
				topMenu = StringUtil.nvl(request.getAttribute("topMenu"));
			}
			if ("".equals(topMenu)) {
				topMenu = StringUtil.nvl(session.getAttribute("topMenu"));
			}
			vo.setLocale(locale.getLanguage());
			vo.setMenuDiv(topMenu);
			if (MEMBER_DIV_CD_S.equals(memberVO.getMemberDivCd())) {
				vo.setUserAuth(memberVO.getMemberDivCd());
			}
			selectMenuList = menuService.selectMenuList(vo);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		return selectMenuList;
	}
}
