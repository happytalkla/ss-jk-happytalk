package ht.domain;

import com.google.gson.JsonObject;

import lombok.Data;

/**
 * 채팅 메세지의 내용 포맷 (TB_CHAT.CONT)
 *
 * <p>
 * JSON 배열 (배열 하나는 말풍선 한개에 대응됨)
 *
 * <pre>
 * [
 *   {type: 'text', contents: ''}
 *   , {type: 'file', contents: ''}
 *   ...
 * ]
 * </pre>
 *
 * <p>
 * 두 개의 배열이면 캐러셀 형태 (공지사항에만 사용하기로 정의)
 *
 * <pre>
 * [
 *   [{type: '', contents: ''} ...]
 *   , [{type: '', contents: ''} ...]
 *   ...
 * ]
 * </pre>
 */
@Data
@Deprecated
public class ChatMessageContents {

	private ChatMessageSectionType type;
	private JsonObject contents;

	public enum ChatMessageSectionType {

		text, file, select, requestion, link, ref, table, hotkey, more // , narrowText, longText
	}
}
