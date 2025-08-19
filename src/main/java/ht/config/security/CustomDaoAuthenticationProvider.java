package ht.config.security;

import java.util.ArrayList;
import java.util.List;

import ht.domain.MemberDetails;
import ht.service.MemberAuthService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ht.constants.CommonConstants;
import ht.domain.MemberVO;

import javax.annotation.Resource;

@Component
@Slf4j
public class CustomDaoAuthenticationProvider implements AuthenticationProvider {

	@Resource
	private MemberAuthService memberAuthService;

	// 로그인 버튼을 누를 경우
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String id = authentication.getName();
		String password = authentication.getCredentials().toString();
		return authenticate(id, password);
	}

	private Authentication authenticate(String id, String password) throws AuthenticationException {

		MemberVO memberVO = memberAuthService.selectUserInfoForLogin(id, password);

		if (memberVO == null) {

			throw new UsernameNotFoundException("xxxxx");
		}

		log.info("LOGIN MEMBER: {}", memberVO);

		String userRole = CommonConstants.getUserRole(memberVO.getMemberDivCd());
		List<GrantedAuthority> gas = new ArrayList<>();
		gas.add(new SimpleGrantedAuthority(userRole));

		return new MemberDetails(memberVO.getMemberUid(), memberVO.getPwd(), gas, memberVO);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
