package ht.domain;
import java.io.Serializable;

import lombok.Data;

@Data
public class MemberVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4559717068852984459L;

	private String memberUid;
	private String memberDivCd;
	private String memberDivNm;
	private String id;
	private String name;
	private String pwd;
	private String cnsPossibleYn;
	private String validYn;
	private String cocId;
	private String upperMemberUid;
	private String upperMemberNm;
	private String departCd;
	private String departNm;
	private String prevLoginDate;
	private String prevLinkIp;

	private String honorsPwd;
	private String odtbrCode;
	private String nHnet;
}
