package ht.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class UserAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException authDeniedEx
	) throws IOException, ServletException {

		req.setAttribute("errMsg", authDeniedEx.getMessage());
		req.getRequestDispatcher("/error").forward(req, res);
	}
}
