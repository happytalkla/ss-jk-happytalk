package ht.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 채팅 메세지 외, 웹소켓 (또는 Ajax 결과) 메세지
 */
@Data
@NoArgsConstructor
public class NoticeMessage {

	@Transient
	private String signature = "NoticeMessage"; // 클라이언트에서 NoticeMessage 타입인지 판별용
	private NoticeMessageType type;
	private String title;
	private String contents;
	private ChatRoom chatRoom; // NoticeMessageType.CHATROOM 시 사용
	private ChatMessage chatMessage; // NoticeMessageType.CHAT_MESSAGE 시 사용
	private Map<String, Object> setting; // NoticeMessageType.SETTING 시 사용
	private List<Map<String, Object>> mapList;
	private Map<String, Integer> statusCounter; // NoticeMessageType.STATUS_COUNTER 시 사용

	private String returnCode;
	private String returnMessage;

	@Getter
	@AllArgsConstructor
	public enum NoticeMessageType {

		CHATROOM(1), SETTING(2), ALRAM(3), NOTICE(4), STATUS_COUNTER(5), CHAT_MESSAGE(6), HIGHLIGHT(8), COMMAND(99);

		private Integer value;
	}

	public NoticeMessage(NoticeMessageType type) {
		super();
		this.type = type;
	}

	public NoticeMessage(ChatRoom chatRoom) {
		super();
		this.type = NoticeMessageType.CHATROOM;
		this.chatRoom = chatRoom;
	}
}
