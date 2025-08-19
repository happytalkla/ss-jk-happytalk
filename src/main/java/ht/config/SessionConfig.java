package ht.config;

// @Configuration
// @EnableRedisHttpSession
@Deprecated
public class SessionConfig { // implements ApplicationListener<ApplicationEvent> {

	// @Value("${spring.session.timeout}")
	// private Integer maxInactiveIntervalInSeconds;
	//
	// @Resource
	// private RedisOperationsSessionRepository redisOperation;
	//
	// @Override
	// public void onApplicationEvent(ApplicationEvent event) {
	// if (event instanceof ContextRefreshedEvent) {
	// redisOperation.setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
	// }
	// }
}
