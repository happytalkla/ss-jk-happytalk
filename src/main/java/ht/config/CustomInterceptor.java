package ht.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.ModelAndView;

import ht.domain.MemberVO;
import ht.service.MemberAuthService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
@Slf4j
public class CustomInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler
			) throws Exception {

		String signature = request.getRequestURI();
		StopWatch stopWatch = null;
		if (signature != null && signature.endsWith("/selectStatusRoomList")) {
			log.info("REQUEST URI: {}, TIMESTAMP: {}", signature, System.currentTimeMillis());
			stopWatch = new StopWatch(signature);
			stopWatch.start(signature);
		}

		try {
			//if (request.getRequestURI().indexOf("/ws") == -1) {
				// 세션 사용자 정보 조회
				MemberVO memberVO = MemberAuthService.getCurrentUser();

				HttpSession session = request.getSession();
				String topMenu = StringUtil.nvl(request.getParameter("topMenu"));
				if ("".equals(topMenu)) {
					topMenu = StringUtil.nvl(request.getAttribute("topMenu"));
				}
				if ("".equals(topMenu)) {
					topMenu = StringUtil.nvl(session.getAttribute("topMenu"));
				}

				session.setAttribute("topMenu", topMenu);
				request.setAttribute("topMenu", topMenu);
				request.setAttribute("sessionVo", memberVO);
				request.setAttribute("contextPath", request.getContextPath());
				request.setAttribute("requestUri", request.getRequestURI());
			//}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		if (signature != null && signature.endsWith("/selectStatusRoomList")) {
			stopWatch.stop();
			System.out.println(stopWatch.shortSummary());
			log.info("REQUEST URI: {}, TIMESTAMP: {}", signature, System.currentTimeMillis());
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		String signature = request.getRequestURI();
		if (signature != null && signature.endsWith("/selectStatusRoomList")) {
			log.info("POST HANDLE, REQUEST URI: {}, TIMESTAMP: {}", signature, System.currentTimeMillis());
		}
		// HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		String signature = request.getRequestURI();
		if (signature != null && signature.endsWith("/selectStatusRoomList")) {
			log.info("AFTER COMLETION, REQUEST URI: {}, TIMESTAMP: {}", signature, System.currentTimeMillis());
		}
		// HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
