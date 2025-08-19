package ht.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class NoticeVO implements Serializable {

	private static final long serialVersionUID = -4559717068852984459L;

	private String noticeNum;
	private String noticeDivCd;
	private String noticeDivNm;
	private String title;
	private String cont;
	private String memberUid;
	private String pwd;
	private String regDt;
	private String delYn;

}
