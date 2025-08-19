package ht.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"local", "dev"})
public class SwaggerConfig {

//	@Value("${happytalk.core.swaggerHost}")
//	private String swaggerHost;

	@Bean
	public Docket api() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("ht.controller"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false)
//                .globalResponseMessage(RequestMethod.GET, globalResponseMessage)
				//.directModelSubstitute(Timestamp.class, LocalDate.class)
				.tags(
						new Tag("biz", "비즈니스단 호출")
//						, new Tag("ht1", "해피톡v1")
						, new Tag("ht2", "해피톡v2")
						, new Tag("chatRoom", "채팅방")
						, new Tag("util", "유틸리티")
						, new Tag("proxy", "프록시")

						, new Tag("kakao", "카카오")
						, new Tag("naver", "네이버")
						, new Tag("facebook", "패이스북")
						, new Tag("wechat", "위쳇")
						, new Tag("counsel", "상담톡")
						, new Tag("line", "라인")
						, new Tag("talktalk", "톡톡")
						, new Tag("send", "송신")
						, new Tag("receive", "수신")

						, new Tag("mock", "테스트용")
				)
				;

//		if (!Strings.isNullOrEmpty(swaggerHost)) {
//			docket.host(swaggerHost);
//		}

		return docket;
	}
}
