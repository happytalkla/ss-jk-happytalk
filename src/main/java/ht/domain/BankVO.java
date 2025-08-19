package ht.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class BankVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4559717068852984459L;

	 private String bankSeq = "";
	 private String bankCd  = "";
	 private String bankNm  = "";
	 private String bankType= "";
	 private String etc = ""; /* 기타 */ 
	 private String useYn = "Y"; /* 사용여부 */ 
	 private String createDt = ""; /* 등록 일시 */ 
	 private String creater = ""; /* 등록자 */ 

}
