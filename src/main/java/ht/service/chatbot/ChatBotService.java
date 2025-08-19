package ht.service.chatbot;

import java.util.List;

import org.springframework.stereotype.Service;

import ht.domain.ChatMessage;
import ht.domain.ChatRoom;

@Service
public interface ChatBotService {

	/**
	 * 챗봇 질의 API 호출
	 *
	 * @param chatRoom 채팅방
	 * @param requestChatMessage 요청 메세지
	 * @param type 요청 타입
	 * @return 응답 메세지
	 */
	List<ChatMessage> chatWithRobot(ChatRoom chatRoom, ChatMessage requestChatMessage, String type) throws Exception;

	/**
	 * 챗봇 질의 API 호출 (페이징)
	 *
	 * @param chatRoom 채팅방
	 * @param requestChatMessage 요청 메세지
	 * @param type 요청 타입
	 * @param page 페이지
	 * @return 응답 메세지
	 */
	List<ChatMessage> chatWithRobot(ChatRoom chatRoom, ChatMessage requestChatMessage, String type, Integer page) throws Exception;

	/**
	 * 세션 종료 API 호출
	 *
	 * @param chatRoom 채팅방
	 */
	void endSession(ChatRoom chatRoom) throws Exception; 
}
