package ht.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "chatRoomUid" })
// @RedisHash
@Deprecated
public class ChatRoomCache {

	@Id
	private String chatRoomUid;
	private String siteId;
	private String ctgNum;
	private String ctgNm1;
	private String ctgNm2;
	private String ctgNm3;
	private String endCtgNm1;
	private String endCtgNm2;
	private String endCtgNm3;
	private String managerUid;
	private String memberUid;
	private String memberName;
	private String cstmUid;
	private String cstmDivCd;
	private String cstmCocId;
	private String cstmName;
	private String cstmGender;
	private String cstmBirthDay;
	private String roomTitle;
	private String cnsrDivCd;
	private String cnsrDivCdNm;
	private String frtCnsrDivCd;
	private String cstmLinkDivCd;
	private String chatRoomStatusCd;
	private String chatRoomStatusCdNm;
	private String roomMarkNum;
	private String roomCreateDt;
	private String roomCreateDay;
	private String roomCreateWeekday;
	private String cnsrLinkDt;
	private String lastChatNum;
	private String lastChatCont;
	private String lastChatDt;
	private String endYn;
	private String roomEndDt;
	private String endDivCd;
	private String endDivCdNm;
	private String cstmReplyDelayYn;
	private String senderDivCd;
	private String cstmDelYn;
	private String botProjectId;
	private String botSessionKey;
	private Integer evlScore;
	private String evlCont;
	private String endCtgCd;

	// Join
	private String passHours;
	private String passMinutes;
	private String markImgClassName;
	private String markDesc;
	private String gradeNm;
	private String gradeMemo;
	private String gradeImgClassName;

	public ChatRoomCache(ChatRoom chatRoom) {

		BeanUtils.copyProperties(chatRoom, this);

		DateTime dateTime = null;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd hh:mm");

		if (chatRoom.getRoomCreateDt() != null) {
			dateTime = new DateTime(chatRoom.getRoomCreateDt().getTime());
			this.setRoomCreateDt(fmt.print(dateTime));
		}
		if (chatRoom.getCnsrLinkDt() != null) {
			dateTime = new DateTime(chatRoom.getCnsrLinkDt().getTime());
			this.setCnsrLinkDt(fmt.print(dateTime));
		}
		if (chatRoom.getLastChatDt() != null) {
			dateTime = new DateTime(chatRoom.getLastChatDt().getTime());
			this.setLastChatDt(fmt.print(dateTime));
		}
		if (chatRoom.getRoomEndDt() != null) {
			dateTime = new DateTime(chatRoom.getRoomEndDt().getTime());
			this.setRoomEndDt(fmt.print(dateTime));
		}
	}
}
