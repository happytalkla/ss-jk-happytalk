package ht.domain;

import static ht.constants.CommonConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import ht.domain.ChatContents.Balloon;
import ht.domain.ChatContents.Section;
import ht.domain.ChatContents.Section.SectionType;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

/**
 * 채팅 메세지 도메인
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "chatNum" })
@Slf4j
public class ChatMessage implements Comparable<ChatMessage> {

	@Transient
	@Builder.Default
	private String signature = "ChatMessage"; // 클라이언트에서 ChatMessage 타입인지 판별용

	@Value("ht.redirectBasePath")
	@JsonIgnore
	private String redirectBasePath;

	private Long chatNum;
	private String chatRoomUid;
	private String senderDivCd; // 고객, 상담직원, 챗봇
	private String senderUid;
	private String memberName;	//sender user name
	private String cnsrMemberUid; // 상담직원
	private String contDivCd; // 메세지 내용 타입 (일반: T, 메모: M, 파일: F)
	private String cont; // 메세지 내용
	private String contText; // 메세지 내용
	private String todayFrtCnsYn; // 사용 안 함, 뭔지 기억도 안남
	private String msgStatusCd; // 보냄, 받음, 읽음
	private Timestamp regDt;
	private Long relChatNum; // 챗봇 답변일 경우, 해당하는 고객 질문의 메세지 시퀀스
	private String botCategory1;
	private String botCategory2;
	private String botCategory3;
	private String botIntent;
	private String avatarCd; // 사용 안 함, 요건 변경됨
	private String evlTypeCd; // 챗봇 답변일 경우, 평가 버튼

	@Transient
	@Builder.Default
	private Target target = Target.all;
	public enum Target {
		all, socket, channel
	}
	@Transient
	private ChatMessageType type;
	@Transient
	private UserActiveStatus userActiveStatus;
	// @Transient
	// private ChatCommand command;
	@Transient
	private String commandContents; // 매니저 메세지 편집시 사용 (NOT JSON 형태로 사용)
	@Transient
	private String assign; // 고객 전용 (NoticeMessage.CHATROOM 대용)
	@Transient
	private List<ChatMessageFile> fileList;
	@Transient
	private String receiver;
	// @Transient
	// private String contentsHandler;
	// @Transient
	// private String returnCode;
	// COMMAND Message: Assign*
	// @Transient
	// private String prevCounselorId;
	// @Transient
	// private String nextCounselorId;

	/**
	 * 메세지 타입, 웹소켓에서만 사용
	 */
	@Getter
	@AllArgsConstructor
	public enum ChatMessageType {

		MESSAGE(1), STATUS(10), ACTIVE(15), JOIN(90), END(91), LEAVE(92)
		, ASSIGN(20), EDIT(2), REMOVE(3); // , COMMAND(20) ;

		private Integer value;
	}

	/**
	 * 사용자의 브라우저 활동 상태
	 */
	@Getter
	@AllArgsConstructor
	public enum UserActiveStatus {

		TYPING(1), NOT_TYPING(2), IDLE(3), ACTIVE(4);

		private Integer value;
	}

	/**
	 * 채팅시 채팅 메세지 외, 여러가지 이벤트 타입
	 */
	@Getter
	@AllArgsConstructor
	public enum ChatCommand {

		REQUEST_COUNSELOR(1),
		CHANGE_COUNSELOR(2),
		ASSIGN_COUNSELOR(3),
		ASSIGN_COUNSELOR_SELF(4),
		SUBMIT_CHATROOM(5),
		ASSIGN_CUSTOMER_GRADE(10),
		MARK_CHATROOM_FLAG(11),
		REQUEST_REVIEW(12),
		COMPLETE_REVIEW(13),
		HIDE_BY_CUSTOMER(14),
		NEW_CHATROOM(15),
		MANAGER_COUNSEL(16),
		LEAVE_MANAGER_COUNSEL(17);
		// , END_CHATROOM_BY_COUNSELOR(98), END_CHATROOM_BY_CUSTOMER(99);

		private Integer value;
	}

	/**
	 * 채팅시 채팅 메세지 외, 여러가지 이벤트에 대한 결과
	 */
	@Getter
	@AllArgsConstructor
	public enum ChatCommandResult {

		SUCCEED(0), FAILED(1);

		private Integer value;
	}

	public String getRegDtPretty() {

		if (regDt != null) {
			DateTime dateTime = new DateTime(regDt.getTime());
			DateTimeFormatter fmt = DateTimeFormat.forPattern("a hh:mm");
			return fmt.print(dateTime).replaceAll("PM", "오후").replaceAll("AM", "오전");
		} else {
			return "";
		}
	}

	public String getRegDay() {

		if (regDt != null) {
			DateTime dateTime = new DateTime(regDt.getTime());
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy년 MM월 dd일 EEE요일");
			return fmt.print(dateTime)
					.replaceAll("Mon","월").replaceAll("Tue","화").replaceAll("Wed","수")
					.replaceAll("Thu","목").replaceAll("Fri","금").replaceAll("Sat","토")
					.replaceAll("Sun","일");
		} else {
			return "";
		}
	}

	/**
	 * 일관성을 위해 MyBatis 에서는 입출력을 {@code Map}으로 사용함
	 */
	@JsonIgnore
	public Map<String, Object> toMap() {

		Map<String, Object> map = new HashMap<>();
		map.put("chatNum", chatNum);
		map.put("chatRoomUid", chatRoomUid);
		map.put("senderDivCd", senderDivCd);
		map.put("senderUid", senderUid);
		map.put("memberName", memberName);
		map.put("cnsrMemberUid", cnsrMemberUid);
		map.put("contDivCd", contDivCd);
		map.put("cont", cont);
		map.put("contText", parseChatMessageText(cont));
		map.put("todayFrtCnsYn", todayFrtCnsYn);
		map.put("msgStatusCd", msgStatusCd);
		map.put("regDt", regDt);
		map.put("relChatNum", relChatNum);
		map.put("botCategory1", botCategory1);
		map.put("botCategory2", botCategory2);
		map.put("botCategory3", botCategory3);
		map.put("botIntent", botIntent);
		map.put("avatarCd", avatarCd);

		return map;
	}

	/**
	 * 일관성을 위해 MyBatis 에서는 입출력을 {@code Map}으로 사용함
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@JsonIgnore
	public ChatMessage fromMap(Map<String, Object> map) {

		if (map != null) {
			chatNum = ((BigDecimal) map.get("chat_num")).longValue();
			chatRoomUid = (String) map.get("chat_room_uid");
			senderDivCd = (String) map.get("sender_div_cd");
			senderUid = (String) map.get("sender_uid");
			memberName = (String) map.get("member_name");
			contDivCd = (String) map.get("cont_div_cd");
//			Clob clobCont = (Clob) map.get("cont");
//			cont = StringUtil.clobToString(clobont);
			cont = (String) map.get("cont");
//			Clob clobContText = (Clob) map.get("cont_text");
//			contText = StringUtil.clobToString(clobContText);
			contText = (String) map.get("cont_text");
			todayFrtCnsYn = (String) map.get("today_frt_cns_yn");
			msgStatusCd = (String) map.get("msg_status_cd");
			regDt = (Timestamp) map.get("reg_dt");
			if (map.get("rel_chat_num") != null) {
				relChatNum = ((BigDecimal) map.get("rel_chat_num")).longValue();
			}
			botCategory1 = (String) map.get("bot_category_1");
			botCategory2 = (String) map.get("bot_category_2");
			botCategory3 = (String) map.get("bot_category_3");
			botIntent = (String) map.get("bot_intent");
			avatarCd = (String) map.get("avatar_cd");
			evlTypeCd = (String) map.get("evl_type_cd");
		}

		return this;
	}

	/**
	 * TODO: REFACTORY, MOVE TO ChatContents
	 * 메세지 내용 중 텍스트 섹션만 추출
	 */
	public static String parseChatMessageText(String cont) {

		log.debug("cont: {}", cont);;
		ChatContents chatContents = new Gson().fromJson(cont, ChatContents.class);
		String result = "";

		for (ChatContents.Balloon balloon : chatContents.getBalloons()) {
			for (ChatContents.Section section : balloon.getSections()) {
				if (ChatContents.Section.SectionType.text.equals(section.getType())) {
					result += " " + section.getData();
				}
			}
		}

		return result.trim();
	}

	private static final String NO_TEXT_MESSAGE = "파일 메세지";
	private static final String INVALID_TEXT_MESSAGE = "메세지 내용을 표시할 수 없습니다.";

	/**
	 * TODO: REFACTORY, ChatRoom.lastChatMessage: JSON -> 단순 텍스트로 변경
	 * 채팅방 마지막 메세지 내용으로 사용할 텍스트 추출, 첫 번째 text 섹션만 추출, 70자로 자름
	 */
	public static String bulidForLastChatMessage(String cont) {

		String text = parseChatMessageText(cont);
		if (!Strings.isNullOrEmpty(text)) {
			if (text.length() > 70) {
				return text.substring(0, 70);
			} else {
				return text;
			}
		}

		// 메세지에 텍스트 섹션이 없을 경우
		Gson gson = new Gson();
		ChatContents chatContents = gson.fromJson(cont, ChatContents.class);
		if (chatContents.isFileOnlyMessage()) {
			return NO_TEXT_MESSAGE;
		}

		return INVALID_TEXT_MESSAGE;
	}

	/**
	 * TODO: REFACTORY, MOVE TO ChatContents
	 * 텍스트 메세지 Json 포맷으로 변환
	 */
	public static String buildChatMessageText(String text) {

		ChatContents chatContents = new ChatContents(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.text)
				.data(text)
				.build());

		String jsonContents = new Gson().toJson(chatContents);
		log.debug("jsonContents: {}", jsonContents);

		return jsonContents;
	}

	/**
	 * TODO: REFACTORY, MOVE TO ChatContents
	 * 텍스트 메세지 Json 포맷으로 변환
	 */
	public static String buildChatMessageText(String text, String extra) {

		ChatContents chatContents = new ChatContents(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.text)
				.data(text)
				.extra(extra)
				.build());

		String jsonContents = new Gson().toJson(chatContents);
		log.debug("jsonContents: {}", jsonContents);

		return jsonContents;
	}

	/**
	 * TODO: REFACTORY, MOVE TO ChatContents
	 * 파일 메세지 Json 포맷으로 변환
	 */
	public static String buildChatMessageFile(String fileURL, String mimeType) {

		ChatContents chatContents = new ChatContents(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.file)
				.data(fileURL)
				.display(mimeType)
				.build());

		String jsonContents = new Gson().toJson(chatContents);
		log.debug("jsonContents: {}", jsonContents);

		return jsonContents;
	}

	/**
	 * TODO: REFACTORY, MOVE TO ChatContents
	 * 상담직원 연결 요청 메세지 Json 포맷으로 변환
	 */
	public static String buildChatMessageHotKeyRequestCounselor(String text) {

		ChatContents chatContents = new ChatContents(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.text)
				.data(text)
				.build());

		List<ChatContents.Action> actionList = new ArrayList<>();
		actionList.add(ChatContents.Action.builder()
				.type(ChatContents.ActionType.hotkey)
				.name("상담직원 연결")
				.data("HappyTalk/RequestCounselor")
				.extra("HappyTalk/RequestCounselor")
				.build());
		chatContents.add(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.action)
				.actions(actionList)
				.build());

		String jsonContents = new Gson().toJson(chatContents);
		log.debug("jsonContents: {}", jsonContents);

		return jsonContents;
	}

	/**
	 * TODO: REFACTORY, MOVE TO ChatContents
	 * '새 채팅방 생성' 메세지 Json 포맷으로 변환
	 */
	public static String buildChatMessageHotKeyNewChatRoom(String text) {

		ChatContents chatContents = new ChatContents(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.text)
				.data(text)
				.build());

		List<ChatContents.Action> actionList = new ArrayList<>();
		actionList.add(ChatContents.Action.builder()
				.type(ChatContents.ActionType.hotkey)
				.name("상담 다시 시작하기")
				//				.data("HappyTalk/NewChatRoom")
				.extra("HappyTalk/NewChatRoom")
				.build());
		chatContents.add(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.action)
				.actions(actionList)
				.build());

		String jsonContents = new Gson().toJson(chatContents);
		log.debug("jsonContents: {}", jsonContents);

		return jsonContents;
	}

	/**
	 * TODO: REFACTORY, MOVE TO ChatContents
	 * 텍스트 메세지 Json 포맷으로 변환
	 */
	public static String buildChatMessageSectionText(String text) {

		ChatContents chatContents = new ChatContents(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.text)
				.data(text)
				.build());

		String jsonContents = new Gson().toJson(chatContents);
		log.info("jsonContents: {}", jsonContents);

		return jsonContents;
	}

	/**
	 * TODO: REFACTORY, MOVE TO ChatContents
	 * 파일 메세지 Json 포맷으로 변환
	 */
	public static String buildChatMessageSectionFile(String imageURL, String mimeType) {

		ChatContents chatContents = new ChatContents(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.file)
				.data(imageURL)
				.display(mimeType)
				.build());

		String jsonContents = new Gson().toJson(chatContents);
		log.debug("jsonContents: {}", jsonContents);

		return jsonContents;
	}

	// public static Pattern textTypePattern = Pattern.compile("\"type\":\\s*\"[a-zA-Z]*text\"");
	//
	// @JsonIgnore
	// public boolean hasTextType() {
	//
	// Matcher m = textTypePattern.matcher(cont);
	// return m.find();
	// }

	// ////////////////////////////////////////////////////////////////////////
	// 사용자 페이지(상담직원, 매니저) 체팅 메세지 뷰어
	// 프론트: chatRoom.js: ChatRoomViewer.buildMessageElement 와 싱크 필요
	/**
	 * 채팅 메세지 클래스 이름
	 */
	@JsonIgnore
	@Transient
	public String getMessageClassName() {

		if (SENDER_DIV_CD_U.equals(this.senderDivCd)) { // 고객 메세지
			//return "left_area";
			return "left-chat-wrap";
		} else if (CONT_DIV_CD_M.equals(this.contDivCd)) { // 메모
			return "left-chat-wrap change_staff";
		} else if (CONT_DIV_CD_L.equals(this.contDivCd)) { // 로그
			return "right-chat-wrap log";
		}

		//return "right_area";
		return "right-chat-wrap";
	}

	/**
	 * 사용자 아바타 클래스 이름
	 */
	@JsonIgnore
	@Transient
	public String getAvatarClassName() {

		if (SENDER_DIV_CD_R.equals(this.senderDivCd)) { // 로봇 메세지
			return "icon_bot chatbot" + (Strings.isNullOrEmpty(avatarCd) ? "" : " " + avatarCd);
		} else if (SENDER_DIV_CD_U.equals(this.senderDivCd)) { // 고객 메세지
			return "icon_bot customer";
		} else if (CONT_DIV_CD_M.equals(this.contDivCd)) {
			return "";
		} else { // 상담직원 메세지
			return "icon_bot counselor";
		}
	}

	/**
	 * 메세지 박스 클래스 이름
	 */
	@JsonIgnore
	@Transient
	public String getMessageBoxClassName() {

		if (SENDER_DIV_CD_R.equals(this.senderDivCd)) { // 로봇 메세지
			return "mch type_text blue";
		} else if (SENDER_DIV_CD_C.equals(this.senderDivCd)) { // 상담직원 메세지
			return "mch type_text green";
		}
		//		else if (SENDER_DIV_CD_C.equals(this.senderDivCd) // 시스템 메세지
		//				&& CONT_DIV_CD_L.equals(this.contDivCd)) { // 로그 메세지
		//			return "text_box log";
		//		}

		return "mch type_text";
	}

	/**
	 * 메세지 박스 클래스 이름
	 */
	@JsonIgnore
	@Transient
	public String getbubbleClassName() {

		if (SENDER_DIV_CD_R.equals(this.senderDivCd)) { // 로봇 메세지
			return "bubble-templet";
		} else if (SENDER_DIV_CD_U.equals(this.senderDivCd)) { // 고객 메세지
			return "bubble-arrow-blue";
		} else if (CONT_DIV_CD_M.equals(this.contDivCd)) {
			return "bubble-arrow";
		} else { // 상담직원 메세지
			return "bubble-round";
		}
	}

	/**
	 * 메세지 내용
	 */
	@JsonIgnore
	@Transient
	public String getMessages() {

		if (Strings.isNullOrEmpty(this.cont)) {
			return "&nbsp;";
		}

		ChatContents chatContents = new Gson().fromJson(this.cont, ChatContents.class);
		log.debug("chatContents: {}", chatContents);

		return getMessages(chatContents);
	}

	/**
	 * 메세지 내용
	 */
	@JsonIgnore
	@Transient
	public String getMessages(ChatContents chatContents) {

		try {
			StringBuilder resultDOM = new StringBuilder();
			List<ChatContents.Balloon> balloonList = chatContents.getBalloons();
			log.debug("balloonSize: {}", balloonList.size());

			// 캐러셀 메세지인 경우
			if (chatContents.isCarouselMessage()) {
				// 컨테이너 열기
				resultDOM.append("<div class=\"swiper-container\">");
				resultDOM.append("<div class=\"swiper-wrapper\">");
				// 시간 표기 안함
				this.setRegDt(null);
			}

			for (int i = 0; balloonList.size() > i; i++) {
				ChatContents.Balloon balloon = balloonList.get(i);

				// 캐러셀 메세지인 경우
				if (chatContents.isCarouselMessage()) {
					resultDOM.append("<div class=\"swiper-slide\">");
				}

				// 섹션
				List<ChatContents.Section> sectionList = balloon.getSections();
				for (ChatContents.Section section : sectionList) {
					ChatContents.Section.SectionType type = section.getType();
					log.debug("type : {}", type);

					if (ChatContents.Section.SectionType.file.equals(type)) { // 파일
						if (section.getDisplay().startsWith("image")) {
							String linkTitle = "새창으로 이미지 보기";
							String imgAlt = "채팅 이미지";
							if (i == balloonList.size() - 1) { // 마지막 캐러셀 스타일 다름
								if (section.getData().startsWith("/")) {
									resultDOM.append("<div class=\"tit\"><a>")
									.append("<img src=\"")
									.append(Strings.nullToEmpty(redirectBasePath)).append(section.getData()).append("\" class=\"chatImageCs\"")
									.append("\" alt=\"").append(imgAlt).append("\" /></a></div>");

								} else {
									resultDOM.append("<div class=\"tit\"><a>")
									.append("<img src=\"")
									.append(section.getData()).append("\" class=\"chatImageCs\"")
									.append("\" alt=\"").append(imgAlt).append("\" /></a></div>");
								}
							} else {
								if (section.getData().startsWith("/")) {
									resultDOM.append("<div class=\"img_area\"><a >")
									.append("<img src=\"")
									.append(Strings.nullToEmpty(redirectBasePath)).append(section.getData()).append("\" class=\"chatImageCs\"")
									.append("\" alt=\"").append(imgAlt).append("\" /></a></div>");
								} else {
									resultDOM.append("<div class=\"img_area\"><a>")
									.append("<img src=\"")
									.append(section.getData()).append("\" class=\"chatImageCs\"")
									.append("\" alt=\"").append(imgAlt).append("\" /></a></div>");
								}
							}
						} else if(section.getDisplay().contains("pdf")) {
							log.error("PDF FILE TYPE: {}", section.getDisplay());
							resultDOM.append("<dd class=\"btn-wrap\">")
							.append("<a href=\"")
							.append(section.getData())
							.append("\" target=\"_blank\" class=\"btn-bn-list-icon\" title=\"새창열림\">")
							.append(section.getExtra())
							.append("</a></dd>");
						} else {
							log.error("NOT IMPLEMENT FILE TYPE: {}", section.getDisplay());
							continue;
						}
					} else if (ChatContents.Section.SectionType.text.equals(type)) { // 텍스트
						String contents = section.getData().replaceAll("(\n|\r\n)", "<br />");
//						if (contents.length() >= 200) {
//							resultDOM.append("<div class=\"mch type_text text200 text_top\">").append(contents).append("</div>");
//							resultDOM.append("<div class=\"inner_btn_area\"><button class=\"btn_view_detail\">자세히보기</button></div>");
//						} else {
							resultDOM.append("<div class=\"mch type_text\">").append(contents).append("</div>");
//						}
					} else if (ChatContents.Section.SectionType.action.equals(type)) { // select

						// 링크, 메세지 액션
						StringBuilder actionDOM = new StringBuilder();
						for (ChatContents.Action action : section.getActions()) {
							if (ChatContents.ActionType.message.equals(action.getType())) {
								actionDOM.append("<a href=\"javascript:void(0);\" class=\"btn-bn-list\"");
								if (!Strings.isNullOrEmpty(action.getExtra())) {
									actionDOM.append(" data-extra=\"").append(action.getExtra()).append("\"");
								}
								if (action.getParams() != null) {
									Map<String, Object> params = action.getParams();
									for (String key : params.keySet()) {
										actionDOM.append(" data-params" + key + "=\"").append(params.get(key)).append("\"");
									}
								}
								actionDOM.append(">")
								.append(action.getName())
								.append("</a>");
							} else if (ChatContents.ActionType.link.equals(action.getType())) {
								actionDOM.append("<a href=\"").append(action.getData())
								.append("\" target=\"_blank\" class=\"btn_hot_key\">")
								.append(action.getName()) //.append(" (새창)")
								.append("</a>");
							}
						}

						if (actionDOM.length() > 0) {
							resultDOM.append("<ul class=\"btn-wrap\">");
							resultDOM.append(actionDOM);
							resultDOM.append("</ul>");
						}

						// 핫키 액션
						for (ChatContents.Action action : section.getActions()) {
							if (ChatContents.ActionType.hotkey.equals(action.getType())) {
								if ("HappyTalk/RequestCounselor".equals(action.getExtra())) {
									resultDOM.append("<a href=\"javascript:void(0);\" class=\"btn_hot_key requestCounselor\">채팅 상담직원과 연결하기<i></i></a>");
								} else
								if ("HappyTalk/NewChatRoom".equals(action.getExtra())) {
									resultDOM.append("<a href=\"javascript:void(0);\" class=\"btn_hot_key newChatRoom\">삼성증권 채팅 다시 시작하기<i></i></a>");
								} else {
									resultDOM.append("<a href=\"javascript:void(0);\" class=\"btn_hot_key\" data-extra=\"" + action.getExtra() + "\"");
									if (action.getParams() != null) {
										Map<String, Object> params = action.getParams();
										for (String key : params.keySet()) {
											actionDOM.append(" data-params" + key + "=\"").append(params.get(key)).append("\"");
										}
									}
									resultDOM.append(">" + action.getName() + "<i></i></a>");
								}
							}
						}
					} else {
						log.error("INVALID MESSAGE SECTION TYPE: {}", type);
						continue;
					}
				}

				// 캐러셀 메세지인 경우
				if (chatContents.isCarouselMessage()) {
					resultDOM.append("</div>");
				}
			}

			// 캐러셀 메세지인 경우
			if (chatContents.isCarouselMessage()) {
				// 컨테이너 닫기
				resultDOM.append("</div>");
				resultDOM.append("</div>");
			}

			log.debug("result: {}", resultDOM);
			return resultDOM.toString();

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			return "&nbsp;";
		}
	}

	/**
	 * 편집 버튼
	 */
	@JsonIgnore
	@Transient
	public String getEditButton() throws Exception {

		if (CONT_DIV_CD_M.equals(this.contDivCd) || SENDER_DIV_CD_U.equals(this.senderDivCd)) {
			StringBuilder resultDOM = new StringBuilder("<div class=\"chat_btn_area\">");
			resultDOM.append("<button type=\"button\" class=\"btn_chat_modify\">수정</button>");
			if (CONT_DIV_CD_M.equals(this.contDivCd)) {
				resultDOM.append("<button type=\"button\" class=\"btn_chat_del\">삭제</button>");
			}
			resultDOM.append("</div>");

			return resultDOM.toString();
		} else {
			return "";
		}
	}

	/**
	 * 메모 메세지 내용 (헤더 부분 노출용)
	 */
	@JsonIgnore
	@Transient
	public String getMemoMessages() throws Exception {

		return ChatMessage.parseChatMessageText(this.cont);
	}

	@Override
	public int compareTo(ChatMessage o) {

		return this.chatNum.compareTo(o.getChatNum());
	}

	/**
	 * 챗봇 답변 메세지에 대한 평가 버튼, 노출 여부
	 */
	@JsonIgnore
	@Transient
	public boolean isShowEvlButton() {

		return false;
	}
	/*public boolean isShowEvlButton() {

		if ("인사말".equals(this.botCategory1) || "공지사항".equals(this.botCategory1)) {
			return false;
		}
		if ("Default_Fallback_Intent".equals(this.botIntent)) {
			return false;
		}

		return true;
	}*/

	public static ChatMessage normalizeView(ChatMessage chatMessage, List<String> forbiddenList, String alternativeForbidden) {

		Gson gson = new Gson();
		ChatContents chatContents = gson.fromJson(chatMessage.getCont(), ChatContents.class);
		ChatContents chatContentsForForbidden = new ChatContents();
		chatContentsForForbidden.add(Section.builder()
				.type(SectionType.text)
				.data(alternativeForbidden)
				.build());

		// 금지어 처리
		for (Balloon balloon : chatContents.getBalloons()) {
			for (Section section : balloon.getSections()) {
				if (SectionType.text.equals(section.getType())) {
					for (String forbidden : forbiddenList) {
						if (section.getData().contains(forbidden)) {
							chatMessage.setCont(gson.toJson(chatContentsForForbidden));
							return chatMessage;
						}
					}
				}
			}
		}

		return chatMessage;
	}

	public static String normalizePersist(String cont) {

		// 민감정보 처리
		cont = StringUtil.getMaskedString(cont);
		return cont;
	}
}
