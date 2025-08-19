package ht.domain;

import static ht.constants.CommonConstants.CONT_DIV_CD_M;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;

import ht.constants.CommonConstants;
import ht.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 채팅방(TB_CHAT_ROOM) 도메인
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

	@Id
	private String chatRoomUid;
	private String siteId;
	private String entranceCode;
	private String screenName;
	private String ctgNum;
	private String ctgNm1;
	private String ctgNm2;
	private String ctgNm3;
	private String departCd;
	private String departNm;
	private String endCtgCd1;
	private String endCtgCd2;
	private String endCtgCd3;
	private String endCtgCd4;
	private String endCtgNm1;
	private String endCtgNm2;
	private String endCtgNm3;
	private String endCtgNm4;
	private String managerUid; // 현재 배정 상담직원의 매니저
	private String memberUid; // 현재 배정 상담직원 (챗봇 포함)
	private String memberName;
	private String memberCocId;
	private String senderName;

	// 고객 정보
	private String roomCocId;
	private String roomCocNm;
	private String cstmUid;
	private String cstmDivCd;
	private String cstmCocId;
	private String cstmCocNm;
	private String cstmName;
	private String cstmGender;
	private String cstmBirthDay;
	private String cstmOsDivCd;
	private String cstmOsDivCdNm;
	private String cstmLinkDivCd;
	private String cstmLinkDivCdNm;
	private String cstmLinkCustomerUid;
	private String cstmCocTypeNm;

	private String roomTitle;
	private String cnsrDivCd; // 현재 상담직원 종류 (상담직원/챗봇)
	private String cnsrDivCdNm;
	private String frtCnsrDivCd; // 채팅방 생성시 상담직원 종류 (상담직원/챗봇)
	private String chatRoomStatusCd;
	private String chatRoomStatusCdNm;
	private String roomMarkNum; // 깃발
	private Timestamp roomCreateDt;
	private String roomCreateDay;
	private String roomCreateWeekday;
	private Timestamp cnsrLinkDt; // 상담직원 첫 메세지 대응 시간
	private String lastChatNum; // 마지막 메세지 시퀀스
	private String lastChatCont;
	private String lastContText;
	private Timestamp lastChatDt;
	private String endYn;
	private Timestamp roomEndDt;
	private String endDivCd;
	private String endDivCdNm;
	private String cstmReplyDelayYn; // 고객 답변 지연 여부
	private String senderDivCd; // 마지막 메세지를 보낸 사용 타입 (고객/상담직원/챗봇)
	private String cstmDelYn; // 고객 채팅창, 채팅 목록에서 감추기 기능
	private String botProjectId;
	private String botSessionKey;
	private String firstBotIntent; // 챗봇 첫 메세지
	private Integer evlScore;
	private String evlCont;
	private String endCtgCd; // 종료 분류 (종료 분류가 따로 있을 경우 사용됨)
	private String endMemo; // 종료 메모
	private String passHours; // 채팅방 생성후 시간
	private String passMinutes;
	private String markImgClassName; // 깃발 이미지
	private String markDesc;
	private String gradeNm; // 코끼리
	private String gradeMemo;
	private String gradeImgClassName; // 코끼리 이미지
	private String managerCounselImgClassName; // 매니저 상담 요청 이미지
	private String authType; // 고객 인증 타입
	private String authTypeName; // 고객 인증 타입
	private String authPositionName; // 고객 인증 위치
	private Boolean isCreatedChatRoom; // 새로 생성됨 여부
	private String goodCode; // 상품정보
	
	private String chk30days;	//상담종료 30일 체크

	public ChatRoom(@NotEmpty String chatRoomUid, @NotEmpty Map<String, Object> customer, @NotEmpty String ctgNum
			, @NotEmpty String title, @NotEmpty String memberUid) {

		this.chatRoomUid = chatRoomUid;
		this.cstmUid = (String) customer.get("cstm_uid");
		this.cstmDivCd = (String) customer.get("cstm_div_cd");
		this.cstmOsDivCd = (String) customer.get("cstm_os_div_cd");
		this.managerUid = memberUid;
		this.memberUid = memberUid;
		this.roomTitle = title;
		this.ctgNum = ctgNum;
		this.cstmLinkDivCd = (String) customer.get("cstm_link_div_cd"); // 채널 선택
		this.endYn = "N";
		this.cstmReplyDelayYn = "N"; // 고객 답변 지연 여부 초기값
		this.cstmDelYn = "N"; // 고객 채팅창 목록에서 숨김 여부 초기값
		this.isCreatedChatRoom = false;
	}

	/**
	 * Setter 예외처리: 채팅방 상태 변경은 종료되지 않은 채팅방에만 허용
	 */
	public void setChatRoomStatusCd(String chatRoomStatusCd) {
		if (!CommonConstants.CHAT_ROOM_STATUS_CD_END.equals(this.getChatRoomStatusCd())) {
			this.chatRoomStatusCd = chatRoomStatusCd;
		}
	}

	@Transient
	private List<ChatMessage> chatMessageList; // 채팅 메세지 목록

	public ChatRoom(String chatRoomUid) {
		super();
		this.setChatRoomUid(chatRoomUid);
	}

	public String getRoomCreateDtPretty() {

		if (this.getRoomCreateDt() != null) {
			DateTime dateTime = new DateTime(this.getRoomCreateDt().getTime());
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm"); // a hh:mm
			//return fmt.print(dateTime).replaceAll("PM", "오후").replaceAll("AM", "오전");
			return fmt.print(dateTime);
		} else {
			return "";
		}
	}

	public String getRoomCreateDateTime() {

		if (this.getRoomCreateDt() != null) {
			DateTime dateTime = new DateTime(this.getRoomCreateDt().getTime());
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
			return fmt.print(dateTime);
		} else {
			return "";
		}
	}

	public String getRoomEndDateTime() {

		if (this.getRoomEndDt() != null) {
			DateTime dateTime = new DateTime(this.getRoomEndDt().getTime());
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
			return fmt.print(dateTime);
		} else {
			return "";
		}
	}

	public String getLastChatDtPretty() {

		if (this.getLastChatDt() != null) {
			DateTime dateTime = new DateTime(this.getLastChatDt().getTime());
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm"); // a hh:mm
			//return fmt.print(dateTime).replaceAll("PM", "오후").replaceAll("AM", "오전");
			return fmt.print(dateTime);
		} else {
			return "";
		}
	}

	/**
	 * 메모 메세지가 있는 채팅방인지 판별 (UI용)
	 */
	public boolean isHasMemo() {

		if (this.getChatMessageList() == null) {
			return false;
		}

		for (ChatMessage chatMessage : this.getChatMessageList()) {
			if (CONT_DIV_CD_M.equals(chatMessage.getContDivCd())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 일관성을 위해 MyBatis 에서는 입출력을 {@code Map}으로 사용함
	 */
	@JsonIgnore
	public Map<String, Object> toMap() {

		Map<String, Object> map = new HashMap<>();
		map.put("chatRoomUid", this.getChatRoomUid());
		map.put("siteId", this.getSiteId());
		map.put("entranceCode", this.getEntranceCode());
		map.put("ctgNum", this.getCtgNum());
		map.put("departCd", this.getDepartCd());
		map.put("departNm", this.getDepartNm());
		map.put("managerUid", this.getManagerUid());
		map.put("memberUid", this.getMemberUid());
		map.put("cstmUid", this.getCstmUid());
		map.put("cocId", this.getRoomCocId());
		map.put("cocNm", this.getRoomCocNm());
		map.put("authType", this.getAuthType());
		map.put("roomTitle", this.getRoomTitle());
		map.put("cnsrDivCd", this.getCnsrDivCd()); // 상담직원 구분 코드
		map.put("frtCnsrDivCd", this.getFrtCnsrDivCd()); // 최초 상담직원 구분 코드
		map.put("cstmOsDivCd", this.getCstmOsDivCd());
		map.put("cstmLinkDivCd", this.getCstmLinkDivCd());
		map.put("chatRoomStatusCd", this.getChatRoomStatusCd());
		map.put("roomMarkNum", this.getRoomMarkNum()); // 채팅방 표시 번호
		map.put("roomCreateDt", this.getRoomCreateDt());
		map.put("cnsrLinkDt", this.getCnsrLinkDt()); // 상담직원 접속 일시
		map.put("lastChatNum", this.getLastChatNum());
		map.put("lastChatCont", this.getLastChatCont());
		map.put("lastChatDt", this.getLastChatDt());
		map.put("endYn", this.getEndYn());
		map.put("roomEndDt", this.getRoomEndDt());
		map.put("endDivCd", this.getEndDivCd());
		map.put("cstmReplyDelayYn", this.getCstmReplyDelayYn()); // 고객 답변 지연 여부
		map.put("senderDivCd", this.getSenderDivCd());
		map.put("cstmDelYn", this.getCstmDelYn());
		map.put("botProjectId", this.getBotProjectId());
		map.put("botSessionKey", this.getBotSessionKey());
		map.put("firstBotIntent", this.getFirstBotIntent());
		map.put("evlScore", this.getEvlScore());
		map.put("evlCont", this.getEvlCont());
		map.put("chk30days", this.getChk30days());
		map.put("goodCode", this.getGoodCode());
		map.put("screenName", this.getScreenName());
		return map;
	}

	/**
	 * 일관성을 위해 MyBatis 에서는 입출력을 {@code Map}으로 사용함
	 */
	@JsonIgnore
	public ChatRoom fromMap(Map<String, Object> map) {

		if (map != null) {

			this.setChatRoomUid((String) map.get("chat_room_uid"));
			this.setSiteId((String) map.get("site_id"));
			this.setEntranceCode((String) map.get("entrance_code"));
			this.setCtgNum((String) map.get("ctg_num"));
			this.setCtgNm1((String) map.get("ctg_nm_1"));
			this.setCtgNm2((String) map.get("ctg_nm_2"));
			this.setCtgNm3((String) map.get("ctg_nm_3"));
			this.setDepartCd((String) map.get("depart_cd"));
			this.setDepartNm((String) map.get("depart_nm"));
			this.setEndCtgCd1((String) map.get("end_ctg_cd_1"));
			this.setEndCtgCd2((String) map.get("end_ctg_cd_2"));
			this.setEndCtgCd3((String) map.get("end_ctg_cd_3"));
			this.setEndCtgCd4((String) map.get("end_ctg_cd_4"));
			this.setEndCtgNm1((String) map.get("end_ctg_nm_1"));
			this.setEndCtgNm2((String) map.get("end_ctg_nm_2"));
			this.setEndCtgNm3((String) map.get("end_ctg_nm_3"));
			this.setEndCtgNm4((String) map.get("end_ctg_nm_4"));
			this.setManagerUid((String) map.get("manager_uid"));
			this.setMemberUid((String) map.get("member_uid"));
			this.setMemberName((String) map.get("member_name"));
			this.setMemberCocId((String) map.get("member_coc_id"));
			this.setSenderName((String) map.get("sender_name"));
			this.setCstmUid((String) map.get("cstm_uid"));
			this.setCstmDivCd((String) map.get("cstm_div_cd"));
			this.setRoomCocId((String) map.get("room_coc_id"));
			if (Strings.isNullOrEmpty((String) map.get("room_coc_nm"))) {
				this.setRoomCocNm("익명");
			} else {
				this.setRoomCocNm((String) map.get("room_coc_nm"));
			}
			this.setCstmCocId((String) map.get("cstm_coc_id"));
			if (Strings.isNullOrEmpty((String) map.get("cstm_coc_nm"))) {
				this.setCstmCocNm("익명");
			} else {
				this.setCstmCocNm((String) map.get("cstm_coc_nm"));
			}
			if (Strings.isNullOrEmpty((String) map.get("cstm_name"))) {
				this.setCstmName("익명");
			} else {
				this.setCstmName((String) map.get("cstm_name"));
			}
			String cstmSex = (String) map.get("cstm_sex");
			if (cstmSex != null) {
				this.setCstmGender("M".equals(cstmSex) ? "남자" : "여자");
			}
			this.setCstmBirthDay((String) map.get("cstm_birth_date"));
			this.setCstmOsDivCd((String) map.get("cstm_os_div_cd"));
			this.setCstmOsDivCdNm((String) map.get("cstm_os_div_cd_nm"));
			this.setCstmLinkDivCd((String) map.get("cstm_link_div_cd"));
			this.setCstmLinkDivCdNm((String) map.get("cstm_link_div_cd_nm"));
			this.setCstmLinkCustomerUid((String) map.get("cstm_link_customer_uid"));
			this.setCstmCocTypeNm((String) map.get("cstm_coc_type_nm"));
			this.setRoomTitle((String) map.get("room_title"));
			this.setCnsrDivCd((String) map.get("cnsr_div_cd"));
			this.setCnsrDivCdNm((String) map.get("cnsr_div_cd_nm"));
			this.setCnsrLinkDt((Timestamp) map.get("cnsr_link_dt"));
			this.setFrtCnsrDivCd((String) map.get("frt_cnsr_div_cd"));
			this.setChatRoomStatusCd((String) map.get("chat_room_status_cd"));
			this.setChatRoomStatusCdNm((String) map.get("chat_room_status_cd_nm"));
			this.setRoomMarkNum((String) map.get("room_mark_num"));
			this.setRoomCreateDt((Timestamp) map.get("room_create_dt"));
			this.setRoomCreateDay(DateUtil.getDay(this.getRoomCreateDt()));
			this.setRoomCreateWeekday(DateUtil.getWeekdayName(this.getRoomCreateDt()));
			this.setLastChatNum((String) map.get("last_chat_num"));
			//Clob clobLastChatCont = (Clob) map.get("last_chat_cont");
			//this.setLastChatCont(StringUtil.clobToString(clobLastChatCont));
			this.setLastChatCont((String) map.get("last_chat_cont"));
			this.setLastChatDt((Timestamp) map.get("last_chat_dt"));
			this.setEndYn((String) map.get("end_yn"));
			this.setRoomEndDt((Timestamp) map.get("room_end_dt"));
			this.setEndDivCd((String) map.get("end_div_cd"));
			this.setEndDivCdNm((String) map.get("end_div_cd_nm"));
			this.setCstmReplyDelayYn((String) map.get("cstm_reply_delay_yn"));
			this.setSenderDivCd((String) map.get("sender_div_cd"));
			this.setBotProjectId((String) map.get("bot_project_id"));
			this.setBotSessionKey((String) map.get("bot_session_key"));
			this.setFirstBotIntent((String) map.get("first_bot_intent"));
			// Join
			this.setPassHours((String) map.get("pass_hours"));
			// log.debug("passHours: \"{}\"", passHours);
			this.setPassMinutes((String) map.get("pass_minutes"));
			this.setMarkImgClassName((String) map.get("mark_img_class_name"));
			this.setMarkDesc((String) map.get("mark_desc"));
			this.setGradeNm((String) map.get("grade_nm"));
			this.setGradeMemo((String) map.get("grade_memo"));
			this.setGradeImgClassName((String) map.get("grade_img_class_name"));
			this.setManagerCounselImgClassName((String) map.get("manager_counsel_img_class_name"));
			BigDecimal evlScoreRaw = (BigDecimal) map.get("evl_score");
			if (evlScoreRaw != null) {
				this.setEvlScore(evlScoreRaw.intValue());
			}
			this.setEvlCont((String) map.get("evl_cont"));
			this.setEndCtgCd((String) map.get("end_ctg_cd"));
			this.setEndMemo((String) map.get("end_memo"));
//			Clob clobLastContText = (Clob) map.get("last_cont_text");
//			this.setLastContText(StringUtil.clobToString(clobLastContText));
			this.setLastContText((String) map.get("last_cont_text"));

			this.setAuthType((String) map.get("auth_type"));
			this.setAuthTypeName((String) map.get("auth_type_name"));
			this.setAuthPositionName((String) map.get("auth_position_name"));
			
			this.setChk30days((String) map.get("chk30days"));
			
			this.setGoodCode((String) map.get("goodCode"));
			this.setScreenName((String) map.get("screen_name"));
			this.isCreatedChatRoom = false;
		}

		return this;
	}

	/**
	 * 상담직원 배정 결과 코드
	 */
	public enum AssignResult {

		SUCCEED(0), FAILED(1), SCHEDULED(2), COUNSELOR_OFF_DUTY(3), COUNSELOR_BREAK_TIME(4),
		CENTER_OFF_DUTY(5), CENTER_BREAK_TIME(6), NO_PERMIT_SELF_ASSIGN(7), COUNSELOR_POOL_OVER(8),
		ALREADY_ASSIGNED(99), ASSIGN_CHANGED(8), NO_COUNSELOR(9);

		private Integer value;

		AssignResult(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
}
