package ht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableRetry
//@EnableRedisHttpSession
//@EnableSwagger2
//@EnableWebMvc // DO NOT USE
public class HappyTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(HappyTalkApplication.class, args);
	}
}
