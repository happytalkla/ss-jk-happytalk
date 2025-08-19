package ht.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class UserLoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException authEx
			) throws IOException, ServletException {

		req.setAttribute("errMsg", "로그인 정보가 잘못되었습니다.");
		req.getRequestDispatcher("/error").forward(req, res);
	}
}
