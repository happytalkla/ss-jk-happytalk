package ht.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.service.MemberAuthService;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 셋팅 관련 controller
 * 공지사항 관리
 *
 * @author wizard
 *
 */
@Controller
@Slf4j
public class MainController {

	/**
	 * 관리자 메인 페이지 이동
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param auth
	 * @param model
	 * @param param
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/admin", method = { RequestMethod.GET, RequestMethod.POST })
	public String admin(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, Authentication auth,
			ModelMap model, @RequestParam Map<String, Object> param) throws IOException {

		log.debug("MainController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		log.debug("getMemberUid : {}", memberVO.getMemberUid());
		log.debug("getMemberDivCd : {}", memberVO.getMemberDivCd());
		log.debug("getName : {}", memberVO.getName());

		String memberDivCd = memberVO.getMemberDivCd();
		if (CommonConstants.MEMBER_DIV_CD_S.equals(memberDivCd)
				|| CommonConstants.MEMBER_DIV_CD_A.equals(memberDivCd)) {
			session.setAttribute("topMenu", "serviceSet");
			response.sendRedirect(request.getContextPath() + "/set/selectSet");
		} else if (CommonConstants.MEMBER_DIV_CD_M.equals(memberDivCd)) {
			session.setAttribute("topMenu", "counselManage");
			response.sendRedirect(request.getContextPath() + "/report/managerMain");
		} else if (CommonConstants.MEMBER_DIV_CD_C.equals(memberDivCd)) {
			session.setAttribute("topMenu", "counselling");
			response.sendRedirect(request.getContextPath() + "/counselor");
		} else {
			response.sendRedirect(request.getContextPath() + "/logout");
		}

		return null;
	}

}
