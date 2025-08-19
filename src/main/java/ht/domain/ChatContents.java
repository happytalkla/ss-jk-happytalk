package ht.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.base.Strings;

import ht.util.HTUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChatContents {

	public static final String DEFAULT_CONTENTS_VERSION = "0.1";

	/**
	 * 빈 섹션으로, 빈 말풍선
	 */
	public ChatContents() {

		this.v = DEFAULT_CONTENTS_VERSION;
		this.orient = "V";				//블럭의 버튼 방향은 기본 세로로 표시
	}
	public ChatContents(@NotNull @Valid String orient) {
		this.v = DEFAULT_CONTENTS_VERSION;
		this.orient = orient;
	}	

	/**
	 * 단일 말풍선, 단일 섹션 메세지
	 */
	public ChatContents(@NotNull @Valid Section section) {

		this.v = DEFAULT_CONTENTS_VERSION;
		this.orient = "V";

		// 섹션 목록
		List<Section> sectionList = new ArrayList<>();
		sectionList.add(section);

		// 말풍선 목록
		this.balloons = new ArrayList<>();
		this.balloons.add(Balloon.builder()
				.sections(sectionList)
				.build());
	}

	/**
	 * 단일 말풍선, 멀티 섹션 메세지
	 */
	public ChatContents(@NotEmpty List<Section> sectionList) {

		this.v = DEFAULT_CONTENTS_VERSION;
		this.orient = "V";
		
		// 말풍선 목록
		this.balloons = new ArrayList<>();
		this.balloons.add(Balloon.builder()
				.sections(sectionList)
				.build());
	}

	@NotEmpty
	private String v;
	
	@NotEmpty
	private String orient;		

	@NotEmpty
	private List<Balloon> balloons;

	@Size(max = 50)
	private String intent;

	@Data
	@AllArgsConstructor
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Balloon {

		@NotEmpty
		private List<Section> sections;

		public Balloon() {
			this.sections = new ArrayList<>();
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Section {

		@NotNull
		private SectionType type;
		public enum SectionType {
			title, text, file, action, command
		}
		private String data; // 일반 데이터 (텍스트, 이미지 주소 등)
		private String display; // 표현 방법, 파일 포맷 (image, image/jpg, application/vnd.ms-excel 등)
		private String extra; // 숨김 데이터 (버튼 이벤트 실행시, UI에 노출되지 않고 전달되는 데이터)
		private Map<String, Object> params; // 파라미터
		private Command command;
		private List<Action> actions;
	}

	public enum Command {
		center_off_duty,
		no_counselor,
		end_slient, // 무응답 상담종료
		scheduled,
		end_by_counselor,
		end_by_customer
	}

	public enum ActionType {
		message, link, hotkey
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Action {
		@NotNull
		@ApiModelProperty("타입")
		private ActionType type;
		@NotEmpty
		@ApiModelProperty("노출 데이터")
		private String name;
		@NotEmpty
		@ApiModelProperty("실행 데이터")
		private String data;
		@ApiModelProperty("아이콘")
		private String keyword;
		@ApiModelProperty("디바이스 타입")
		private DeviceType deviceType;
		@ApiModelProperty("대체 실행 데이터")
		private String extra;
		@ApiModelProperty("파라미터")
		private Map<String, Object> params;

		@ApiModelProperty("액션 식별자")
		@Positive
		private Long id;
	}

	public enum DeviceType {

		all, pc, mobile, andriod, ios
	}

	/**
	 * 말풍선 추가
	 */
	@JsonIgnore
	public void add(@NotNull Balloon balloon) {

		if (this.getBalloons() == null) {
			this.setBalloons(new ArrayList<>());
		}

		this.getBalloons().add(balloon);
	}

	/**
	 * 마지막 말풍선에 섹션 추가
	 */
	@JsonIgnore
	public void add(@NotNull Section section) {

		Balloon lastBalloon = getLastBalloon();

		if (lastBalloon.getSections() == null) {
			lastBalloon.setSections(new ArrayList<>());
		}

		lastBalloon.getSections().add(section);
	}

	/**
	 * 첫번째 말풍선
	 */
	@JsonIgnore
	public Balloon getFirstBalloon() {

		if (this.getBalloons() == null) {
			this.setBalloons(new ArrayList<>());
		}

		Balloon firstBalloon;
		if (this.getBalloons().isEmpty()) {
			firstBalloon = Balloon.builder()
					.sections(new ArrayList<>())
					.build();
			this.add(firstBalloon);
		} else {
			firstBalloon = this.getBalloons().get(0);
		}

		return firstBalloon;
	}

	/**
	 * 마지막 말풍선
	 */
	@JsonIgnore
	public Balloon getLastBalloon() {

		if (this.getBalloons() == null) {
			this.setBalloons(new ArrayList<>());
		}

		Balloon lastBalloon;
		if (this.getBalloons().isEmpty()) {
			lastBalloon = Balloon.builder()
					.sections(new ArrayList<>())
					.build();
			this.add(lastBalloon);
		} else {
			lastBalloon = this.getBalloons().get(this.getBalloons().size() - 1);
		}

		return lastBalloon;
	}

	/**
	 * 채팅 내용을 텍스트로 요약 (채팅방 목록에 노출, 챗봇 질의용 등)
	 */
	@JsonIgnore
	public String getSummary() {

		StringBuilder summary = new StringBuilder();

		for (Balloon balloon : this.getBalloons()) {
			for (Section section : balloon.getSections()) {
				if (Section.SectionType.text.equals(section.getType())) {
					summary.append(" ").append(section.getData());
				}
			}
		}

		return summary.toString().trim();
	}

	/**
	 * 해피톡 포맷 기준: 단일 섹션 && 이미지, 텍스트 메세지
	 * 네이버 라인은 이미지, 텍스트 메세지 타입이 없음, flex 타입으로 처리
	 */
	@JsonIgnore
	public boolean isTextAndFileMessage() {

		if (this.getBalloons().size() == 1) {
			boolean hasTextSection = false;
			boolean hasImageSection = false;
			for (Section section : this.getBalloons().get(0).getSections()) {
				if (Section.SectionType.text.equals(section.getType())) {
					hasTextSection = true;
				} else if (Section.SectionType.file.equals(section.getType())) {
					hasImageSection = true;
				} else {
					return false;
				}
			}

			return hasTextSection && hasImageSection;
		}

		return false;
	}

	/**
	 * 해피톡 포맷 기준: 단일 섹션 && (텍스트 메세지 || 파일 메세지)
	 * TODO: 단일 섹션은 아니지만 텍스트로만 표현 가능한 경우
	 */
	@JsonIgnore
	public boolean isTextOrFileMessage() {

		Section section = this.getBalloons().get(0).getSections().get(0);

		return this.getBalloons().size() == 1
				&& this.getBalloons().get(0).getSections().size() == 1
				&& (Section.SectionType.text.equals(section.getType())
						|| Section.SectionType.file.equals(section.getType())
						|| Section.SectionType.command.equals(section.getType()));
	}

	/**
	 * 해피톡 포맷 기준: 단일 섹션 && 텍스트 메세지
	 */
	@JsonIgnore
	public boolean isTextOnlyMessage() {

		try {
			Section section = this.getBalloons().get(0).getSections().get(0);

			return this.getBalloons().size() == 1
					&& this.getBalloons().get(0).getSections().size() == 1
					&& Section.SectionType.text.equals(section.getType());
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			return false;
		}
	}

	/**
	 * 해피톡 포맷 기준: 단일 섹션 && 텍스트 메세지
	 */
	@JsonIgnore
	public boolean isFileOnlyMessage() {

		try {
			Section section = this.getBalloons().get(0).getSections().get(0);

			return this.getBalloons().size() == 1
					&& this.getBalloons().get(0).getSections().size() == 1
					&& Section.SectionType.file.equals(section.getType());
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error(e.getLocalizedMessage(), e);
			return false;
		}
	}

	/**
	 * 캐러셀 메세지
	 */
	@JsonIgnore
	public boolean isCarouselMessage() {

		return this.getBalloons().size() > 1;
	}

	@JsonIgnore
	public String getExtra() {

		if (this.getBalloons() != null && this.getBalloons().size() == 1) {
			Balloon balloon = this.getBalloons().get(0);
			if (balloon.getSections() != null && balloon.getSections().size() == 1) {
				Section section = balloon.getSections().get(0);
				if (!Strings.isNullOrEmpty(section.getExtra())) {
					return section.getExtra();
				}
			}
		}

		return null;
	}

	@JsonIgnore
	public Map<String, Object> getParams() {

		if (this.getBalloons() != null && this.getBalloons().size() == 1) {
			Balloon balloon = this.getBalloons().get(0);
			if (balloon.getSections() != null && balloon.getSections().size() == 1) {
				Section section = balloon.getSections().get(0);
				if (section.getParams() != null && !section.getParams().isEmpty()) {
					return section.getParams();
				}
			}
		}

		return null;
	}

	@JsonIgnore
	public boolean isEmpty() {

		if (this.getBalloons() != null && !this.getBalloons().isEmpty()) {
			Balloon balloon = this.getBalloons().get(0);
			if (balloon.getSections() != null && !balloon.getSections().isEmpty()) {
				return false;
			}
		}

		return true;
	}
}
