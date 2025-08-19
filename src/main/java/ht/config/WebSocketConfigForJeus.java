package ht.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;

import jeus.spring.websocket.JeusHandshakeHandler;

import javax.annotation.Resource;

@Profile({ "!local" })

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigForJeus implements WebSocketMessageBrokerConfigurer {

	@Resource
	private CustomProperty customProperty;

	@Bean
	public HandshakeHandler handshakeHandler() {

		return new JeusHandshakeHandler();
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

//		registry.addEndpoint(customProperty.getWsConnectionPath()).setAllowedOrigins("*")
//				.setHandshakeHandler(handshakeHandler()).addInterceptors(new HttpHandshakeInterceptor()).withSockJS();
		registry.addEndpoint(customProperty.getWsConnectionPath()).setAllowedOrigins("*")
				.setHandshakeHandler(handshakeHandler()).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.enableStompBrokerRelay(customProperty.getWsTopicPath(), customProperty.getWsQueuePath())
				.setRelayHost(customProperty.getMessageBroker().getHost())
				.setRelayPort(customProperty.getMessageBroker().getPort())
				.setClientLogin(customProperty.getMessageBroker().getUser())
				.setClientPasscode(customProperty.getMessageBroker().getPass()).setSystemHeartbeatSendInterval(10000)
				.setSystemHeartbeatReceiveInterval(10000);
		registry.setApplicationDestinationPrefixes(customProperty.getWsDefaultDestinationPath());
	}
}
