package ht.domain.chatbot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 해피톡 챗봇 메세지 포맷 > Scenario Data 필드
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("해피톡 챗봇 메세지 포맷 > Scenario Data 필드")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BizHappyBotDataScenario {

	private Long sid;

	@SerializedName("bot_id")
	@JsonProperty("bot_id")
	private String botId;

	@Pattern(regexp = "on|off")
	private String active;

	@SerializedName("user_open")
	@JsonProperty("user_open")
	@Pattern(regexp = "Y|N")
	private String userOpen;

	@SerializedName("sort_order")
	@JsonProperty("sort_order")
	private String sortOrder; // 버튼 라벨 프리픽스

	@SerializedName("surveyls_title")
	@JsonProperty("surveyls_title")
	private String surveylsTitle; // 버튼 라벨

//	public String getLabel() {
//		return (sortOrder + surveylsTitle).trim();
//	}

	@SerializedName("surveyls_description")
	@JsonProperty("surveyls_description")
	private String surveylsDescription;

	private String created;

	private QuestionAnswer question1st;

	@Data
	@NoArgsConstructor
	public static class QuestionAnswer {
		private String qid;
		private String type;
		private String title;
		private Question question;
		@Data
		@NoArgsConstructor
		public static class Question {
			private QuestionText text;
			@Data
			@NoArgsConstructor
			public static class QuestionText {
				private String id;
				private String optionType;
				private String group;
				private String text;
				private List<Args> args;
				@Data
				@NoArgsConstructor
				public static class Args {
					private String type; // user_message
					private String text;
					private Boolean user_name;
				}
			}
		}

		private Answer answers;
		@Data
		@NoArgsConstructor
		public static class Answer {
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
				private Integer idx;
			}
		}

//		private List<Next> next;
//		@Data
//		@NoArgsConstructor
//		public static class Next {
//			private String next;
//			private Condition condition;
//			@Data
//			@NoArgsConstructor
//			public static class Condition {
//				private String type;
//				private String filter;
//				private String value;
//			}
//		}

		private Boolean done;
	}

	@SerializedName("service_period")
	@JsonProperty("service_period")
	private ServicePeriod servicePeriod;
	@Data
	@NoArgsConstructor
	public static class ServicePeriod {
		private String startDt;
		private String end_dt;
		private CycleTime cycle_time;
		@Data
		@NoArgsConstructor
		public static class CycleTime {
			private List<String> dayOfWeek; // TODO: 타입 확인
			private String start_tm;
			private String end_tm;
		}
	}

//	@SerializedName("dialog_order")
//	@JsonProperty("dialog_order")
//	private List<DialogOrder> dialogOrderList;
//	@Data
//	@NoArgsConstructor
//	public static class DialogOrder {
//		private String prev; //
//		private String self;
//	}
	/*
		"dialog_order": [
			{
				"prev": "",
				"self": "Dialog_1"
			},
			{
				"prev": [
					"Dialog_1",
					"Dialog_1",
					"Dialog_1"
				],
				"self": "Dialog_2"
			}
		],
	 */

	private String utime;

	private Start start;
	@Data
	@NoArgsConstructor
	public static class Start {
		private String keyword;
		private String dialog;
	}
}
