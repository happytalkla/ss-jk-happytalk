package ht.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChannelVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4559717068852984459L;

	private String Entity_ID; //userId
	private String HTS_ID;		
	private String Login_Lv;
	private String Cha_Code;	//channel
	private String Auth_Type;
	private String PreView_ID;
	private String Scenario_Text;
	private String TimeStamp;
	private String mngnClntYn;	//kakao flag
	private String roomUid;		//kakao chat room Uid
	private String cstmUid;		//kakao cstm Uid
	private String chkSurvey;
	private String goodCode;			//상품코드
	private String appCode;				//화면번호
	/**
	 * IPCC_MCH ARS 채널 관련 부서코드 추가
	 */
	private String departCd; // 부서코드
}
