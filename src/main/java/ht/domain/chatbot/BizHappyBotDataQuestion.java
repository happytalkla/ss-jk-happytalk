package ht.domain.chatbot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 해피톡 챗봇 메세지 포맷 > Question Data 필드
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("해피톡 챗봇 메세지 포맷 > Question Data 필드")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BizHappyBotDataQuestion {

	@NotNull
	@Positive
	private Long sid;

	@NotNull
	@Pattern(regexp = "[0-9]+")
	private String type;

	@NotEmpty
	private String title;

	@SerializedName("img_url")
	@JsonProperty("img_url")
	@URL
	private String imgUrl;

	@SerializedName("btn_url")
	@JsonProperty("btn_url")
	@URL
	private String btnUrl;

	@SerializedName("btn_name")
	@JsonProperty("btn_name")
	private String btnName;

	@SerializedName("hotkey_display_switch")
	@JsonProperty("hotkey_display_switch")
	@Pattern(regexp = "true|false")
	private String hotkeyDisplaySwitch;

	@SerializedName("hotkey_first")
	@JsonProperty("hotkey_first")
	@Pattern(regexp = "on|off")
	private String hotkeyFirst;

	@SerializedName("hotkey_prev")
	@JsonProperty("hotkey_prev")
	@Pattern(regexp = "on|off")
	private String hotkeyPrev;

	@SerializedName("hotkey_operator")
	@JsonProperty("hotkey_operator")
	@Pattern(regexp = "on|off")
	private String hotkeyOperator;

	@SerializedName("hotkey_endbot")
	@JsonProperty("hotkey_endbot")
	@Pattern(regexp = "on|off")
	private String hotkeyEndbot;

	private String fieldnames;

	private String lastanswer;

	private String category_id;

	private String division_id;

	private Operator operator;
	@Data
	@AllArgsConstructor
	public static class Operator {
		private String major_cate;
		private String middle_cate;
		private List<Cate> middle_cate_list;
		@Data
		@AllArgsConstructor
		public static class Cate {
			@Pattern(regexp = "[0-9]+")
			private String id;
			@Pattern(regexp = "[0-9]+")
			private String pid;
			@SerializedName("site_id")
			@JsonProperty("site_id")
			@Pattern(regexp = "[0-9]+")
			private String siteId;
			private String name;
			@Pattern(regexp = "[0-9]+")
			private String sort;
			@Pattern(regexp = "Y|N")
			private String use;
		}
	}

	private Answer answer;
	@Data
	@NoArgsConstructor
	public static class Answer {
		private String type;
		private List<Choice> choice;
		@Data
		@NoArgsConstructor
		public static class Choice {
			private String id;
			private String optionType;
			private String group;
			private String type;
			private String label;
			private Payload payload;
			@Data
			@NoArgsConstructor
			public static class Payload {
				private String text;
			}
			private String next;
			private String idx;
		}
	}
}
