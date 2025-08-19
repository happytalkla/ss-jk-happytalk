package ht.config.security;

import static ht.constants.CommonConstants.LOG_DIV_CD_F;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.google.common.base.Strings;

import ht.service.CommonService;
import ht.service.MemberService;

public class UserLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Resource
	private MemberService memberService;
	@Resource
	private CommonService commonService;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication
			) throws IOException, ServletException {

		if (authentication != null && !Strings.isNullOrEmpty((String) authentication.getPrincipal())) {

			Map<String, Object> member = memberService.selectMemberByMemberUid((String) authentication.getPrincipal());
			
			String logCont = authentication.getPrincipal() + "(" + authentication.getPrincipal() + ") 로그아웃";
			
			if(member != null) {
				logCont = member.get("name") + "(" + member.get("coc_id") + ") 로그아웃";
			}
			commonService.insertLog(LOG_DIV_CD_F, "로그아웃", logCont, (String) authentication.getPrincipal());
		}

		super.onLogoutSuccess(request, response, authentication);
	}
}
