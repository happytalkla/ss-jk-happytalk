package ht.config;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

//@Component
//@Order(10)
@Slf4j
public class CustomFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain
			) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		String url = httpServletRequest.getRequestURI();

		if (!Strings.isNullOrEmpty(url)) {
			if (url.startsWith("/actuator/health")) {
				log.debug("FILTER: ACTUATOR: {}", url);
			}
		}

		if (log.isDebugEnabled()) {
			String queryString = httpServletRequest.getQueryString();
			log.debug("{}?{}", url, queryString);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
