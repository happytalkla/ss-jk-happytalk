package ht.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;

@Configuration
public class SpringBeanConfig {

	// ////////////////////////////////////////////////////////////////////////
	// Rest Template Bean
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 기본 클라이언트
	 */
	@Bean("restTemplate")
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	/**
	 * 외부 접속용 클라이언트
	 */
	@Bean("externalRestTemplate")
	public RestTemplate getExternalRestTemplate(RestTemplateBuilder builder) {

		return builder
				.setConnectTimeout(Duration.ofMillis(1000 * 5))
				.setReadTimeout(Duration.ofMillis(1000 * 10))
				.errorHandler(new ExternalRestTemplateExceptionHandler())
				.build();
	}

	/**
	 * Object Mapper
	 */
	@Bean
	public ObjectMapper objectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper;
	}
}
