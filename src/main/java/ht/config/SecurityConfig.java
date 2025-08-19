package ht.config;

import javax.annotation.Resource;
import javax.servlet.SessionCookieConfig;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ht.config.security.CustomDaoAuthenticationProvider;
import ht.config.security.UserAccessDeniedHandler;
import ht.config.security.UserLoginFailureHandler;
import ht.config.security.UserLoginSuccessHandler;
import ht.config.security.UserLogoutSuccessHandler;

@Configuration
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;

	@Bean
	public LogoutSuccessHandler getUserLogoutSuccessHandler() {
		return new UserLogoutSuccessHandler();
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new UserLoginSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler failureHandler() {
		return new UserLoginFailureHandler();
	}

	@Bean
	public AccessDeniedHandler deniedHandler() {
		return new UserAccessDeniedHandler();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/storage/upload/**", "/upload/**", "/v2/**", "/html/**",
				"/fonts/**", "/actuator/**", "/ws", "/ws/**", "/publish/**", "/topic/**", "/queue/**", "/skill/**", "/mock/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Auth Provider
		http.authenticationProvider(customDaoAuthenticationProvider);

		// 세션
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

		http.headers().frameOptions().sameOrigin();

		// 시큐리티 설정
		http.authorizeRequests()
				// .antMatchers("/loginPage").access("hasIpAddress('localhost')"
				// + " or hasIpAddress('127.0.0.1')"
				// + " or hasIpAddress('10.178.101.0/16')"
				// + " or hasIpAddress('10.178.101.53')")
				.antMatchers("/happybot/**").hasAnyRole("SUPER").antMatchers("/board/**")
				.hasAnyRole("SUPER", "ADMIN", "MANAGER").antMatchers("/category/selectCategory")
				.hasAnyRole("SUPER", "ADMIN").antMatchers("/category/**").hasAnyRole("SUPER", "ADMIN", "MANAGER")
				.antMatchers("/manage/selectMemberList").hasAnyRole("SUPER", "ADMIN")
				.antMatchers("/manage/selectLogList").hasAnyRole("SUPER", "ADMIN").antMatchers("/manage/**")
				.hasAnyRole("SUPER", "ADMIN", "MANAGER")
				// .antMatchers("/report/adminMain").hasAnyRole("SUPER", "ADMIN", "MANAGER")
				.antMatchers("/report/managerMain").hasAnyRole("SUPER", "MANAGER").antMatchers("/set/*Forbidden")
				.hasAnyRole("SUPER", "ADMIN", "MANAGER").antMatchers("/set/**").hasAnyRole("SUPER", "ADMIN")
				.antMatchers("/template/**").hasAnyRole("SUPER", "ADMIN", "MANAGER").antMatchers("/user/**")
				.hasAnyRole("SUPER", "ADMIN", "MANAGER").antMatchers("/counselor")
				.hasAnyRole("SUPER", "MANAGER", "COUNSELOR").antMatchers("/chatCsnr")
				.hasAnyRole("SUPER", "MANAGER", "COUNSELOR").antMatchers("/reporting/**").hasAnyRole("SUPER", "MANAGER")
				// .antMatchers("/ws", "/ws/**", "/publish/**", "/topic/**",
				// "/queue/**").permitAll()
				.antMatchers("/api/html/chat/**").authenticated()
				.antMatchers("/api/**").permitAll().antMatchers("/customer", "/customer/**").permitAll()
				.antMatchers("/channel/**").permitAll().antMatchers("/").permitAll().antMatchers("/error", "/error/**")
				.permitAll().antMatchers("/login").permitAll().antMatchers("/changePasswdView").permitAll()
				.antMatchers("/chatRoomView/**").permitAll().antMatchers("/loginPage").permitAll()
				.antMatchers("/login/error").permitAll().antMatchers("/login/success").permitAll()
				.antMatchers("/login-processing").permitAll().antMatchers("/open_builder/**").permitAll()
				.antMatchers("/sample/**").permitAll().antMatchers("/hk/**").permitAll().antMatchers("/hk_auth/**")
				.permitAll().antMatchers("/hk_survey/**").permitAll()
				// .antMatchers(actuatorEndpoints()).permitAll()
				// .antMatchers("/**").authenticated();
				.anyRequest().authenticated()
//		.and().cors()
				.and().csrf().disable();

		http.formLogin().loginPage("/loginPage").loginProcessingUrl("/login-processing").failureUrl("/login/error")
				.defaultSuccessUrl("/login/success", true).usernameParameter("username").passwordParameter("password");

		http.logout().logoutSuccessHandler(getUserLogoutSuccessHandler())
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/loginPage")
				.invalidateHttpSession(true);
	}
}
