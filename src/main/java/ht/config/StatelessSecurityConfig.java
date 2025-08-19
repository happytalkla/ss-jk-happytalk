package ht.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * WebLogic
 */
//@Configuration
//@Order(1)
public class StatelessSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {

		web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/upload/**", "/v2/**", "/html/**",
				"/fonts/**", "/actuator/**", "/skill/**", "/mock/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.antMatcher("/ws/**")
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http
		.antMatcher("/ws")
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http
		.antMatcher("/topic")
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http
		.antMatcher("/topic/**")
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
