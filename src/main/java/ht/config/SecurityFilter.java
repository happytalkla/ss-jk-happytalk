package ht.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ht.util.RequestWrapper;

//@Component
//@Order(20)
public class SecurityFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain
			) throws IOException, ServletException {

		// 웹취약성, 잘못된 URL 접근시 에러페이지로 리다이렉트
		String uri = "";
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		uri = httpRequest.getRequestURI();
		if (uri.startsWith("/error/") || uri.startsWith("//")) {
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect(httpRequest.getContextPath() + "/error");
			return;
		}

		/*
		 * Cookie[] cookies = httpRequest.getCookies();
		 *
		 * log.debug("=============================== SessionFilter start ====================================");
		 * if(cookies != null) {
		 * for(Cookie cookie : cookies) {
		 * log.debug(cookie.getName() + " : " + cookie.getValue());
		 * }
		 * }
		 * log.debug("uri : {}", uri);
		 * log.debug("=============================== SessionFilter end ====================================");
		 */
		// chain.doFilter(request, response);

		/*
		 * MemberVO memberVO = AuthorizationService.getCurrentUser();
		 * if(memberVO == null || memberVO.getMemberUid() == null || "".equals(memberVO.getMemberUid())) {
		 * HttpServletRequest req = (HttpServletRequest)request;
		 * HttpServletResponse res = (HttpServletResponse)response;
		 *
		 * if(isAjaxrequest(req)) {
		 * String uri = req.getRequestURI();
		 * // 채팅의 경우는 로그인 없이 호출
		 * if(uri.contains("/api/")) {
		 * chain.doFilter(request, response);
		 * // }else {
		 * // res.sendError(res.SC_UNAUTHORIZED);
		 * }
		 * }else {
		 * chain.doFilter(request, response);
		 * }
		 * }else {
		 * chain.doFilter(request, response);
		 * }
		 */

		// requestWrapper 에서 파라미터값 변조 처리
		// HttpServletRequest httpRequest = (HttpServletRequest)request;
		RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
		chain.doFilter(requestWrapper, response);
	}

	@Override
	public void destroy() {
	}

//	/**
//	 * ajax 여부 판단
//	 *
//	 * @param request
//	 * @return
//	 */
//	private boolean isAjaxrequest(HttpServletRequest request) {
//		String header = request.getHeader("x-requested-with");
//		return "XMLHttpRequest".equals(header);
//	}
}
