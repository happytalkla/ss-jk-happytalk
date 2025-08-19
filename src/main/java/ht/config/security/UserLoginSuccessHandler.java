package ht.config.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth
			) throws IOException, ServletException {

		log.info("LOGIN SUCCEESS, ROLES: {}", Joiner.on(", ").join(auth.getAuthorities()));
		//		for (GrantedAuthority authority : auth.getAuthorities()) {
		//		}
		res.sendRedirect(req.getContextPath() + "/main/admin");
	}
}
