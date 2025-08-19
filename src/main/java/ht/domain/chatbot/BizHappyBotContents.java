package ht.domain.chatbot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

/**
 * 해피톡 V2 챗봇 메세지 포맷
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("해피톡 챗봇 메세지 포맷")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BizHappyBotContents {

	@NotNull
	public enum BizHappyBotHotKeyType {
		bot, end, prev_question, close_room, reload_scenario, request_chat
	}
	public static final Map<BizHappyBotHotKeyType, String> hotKeyList = new HashMap<>();
	static {
		hotKeyList.put(BizHappyBotHotKeyType.bot, "&bot");
		hotKeyList.put(BizHappyBotHotKeyType.end, "&end");
		hotKeyList.put(BizHappyBotHotKeyType.prev_question, "&55");
		hotKeyList.put(BizHappyBotHotKeyType.close_room, "&66");
		hotKeyList.put(BizHappyBotHotKeyType.reload_scenario, "&77");
		hotKeyList.put(BizHappyBotHotKeyType.request_chat, "&88");
	}
	public static final Map<BizHappyBotHotKeyType, String> hotKeyNameList = new HashMap<>();
	static {
		hotKeyNameList.put(BizHappyBotHotKeyType.bot, "시작");
		hotKeyNameList.put(BizHappyBotHotKeyType.end, "챗봇 종료");
		hotKeyNameList.put(BizHappyBotHotKeyType.prev_question, "이전으로");
		hotKeyNameList.put(BizHappyBotHotKeyType.close_room, "상담 종료");
		hotKeyNameList.put(BizHappyBotHotKeyType.reload_scenario, "처음으로");
		hotKeyNameList.put(BizHappyBotHotKeyType.request_chat, "상담직원 연결");
	}

	@NotNull
	private BizHappyBotContentsType type;
	public enum BizHappyBotContentsType {
		init, bot, lists, answer, completed, request_chat, api_result, finished, error, delete,
		question,
		scenario_lists
	}

	/**
	 * 상담직원 연결 버튼 노출 여부
	 */
	@SerializedName("hotkey_operator")
	@JsonProperty(value = "hotkey_operator", defaultValue = "on")
	@Pattern(regexp = "on|off")
	private String hotkeyOperator;

	@SerializedName("hotkey_display_switch")
	@JsonProperty("hotkey_display_switch")
	@Pattern(regexp = "true|false")
	private String hotkeyDisplaySwitch;

	private boolean encrypted;

	@NotNull
	private Object data;

	/**
	 * 첫 메세지 여부
	 * 첫 메세지는 몇 가지 핫키를 (뒤로가기 등) 제공할 필요없음
	 */
	@SerializedName("first_talk")
	@JsonProperty(value = "first_talk", defaultValue = "false")
	private Boolean firstTalk;
}
