package ht.domain;
import java.io.Serializable;

import lombok.Data;

@Data
public class CBCLS002VO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4559717068852984459L;

	 private String chat_type          =""; /* 업무구분 */
	 private String hk_title           =""; /* 서비스 이용내역 */
	 private String hk_cert_channel    =""; /* 인증채널 */
	 private String hk_cert            =""; /* 인증방식 */
	 private String hk_datetime        =""; /* 기간계 호출 일시 */
	 private String chat_room_uid      =""; /* 채팅방ID */
	 private String channel	           =""; /* 채널 */
	 private String start_datetime     =""; /* 상담시작시간 */
	 private String end_datetime       =""; /* 상담종료시간 */
	 private String counselorNo        =""; /* 상담직원번호 */
	 private String counselorNm        =""; /* 상담직원명 */
	 private String cstm_uid           =""; /* 해피톡생성 고객 key */
	 private String channel_customer_id=""; /* 채널별 고객 key */	
	 private String user_nm            =""; /* 고객명 */
	 private String device             =""; /* 고객기기 */


}
