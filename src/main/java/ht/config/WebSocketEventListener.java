package ht.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		log.info("=========================================================");
		log.info("CONNECT EVENT: {}", event);
		log.info("=========================================================");

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getSessionAttributes() != null) {
			for (String key : headerAccessor.getSessionAttributes().keySet()) {
				log.info("=========================================================");
				log.info("WS SESSION, {}: {}", key, headerAccessor.getSessionAttributes().get(key));
				log.info("=========================================================");
			}
		}
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

		log.info("=========================================================");
		log.info("DISCONNECT EVENT: {}", event);
		log.info("=========================================================");

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getSessionAttributes() != null) {
			for (String key : headerAccessor.getSessionAttributes().keySet()) {
				log.info("=========================================================");
				log.info("WS SESSION, {}: {}", key, headerAccessor.getSessionAttributes().get(key));
				log.info("=========================================================");
			}
//			String username = (String) headerAccessor.getSessionAttributes().get("username");
//			if (username != null) {
//				ChatMessage chatMessage = new ChatMessage();
//				chatMessage.setType(ChatMessage.ChatMessageType.LEAVE);
//				messagingTemplate.convertAndSend(
//						customProperty.getWsTopicPath() + "/" + "CHAT_RROM_UID", chatMessage);
//			}
		}
	}
}
