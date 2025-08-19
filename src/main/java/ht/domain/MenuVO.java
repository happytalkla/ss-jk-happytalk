package ht.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class MenuVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4559717068852984459L;

	private String menuId = ""; /* 메뉴 아이디 */ 
	private String menuNm = ""; /* 메뉴명 */ 
	private String menuDiv = ""; /* 메뉴구분 */ 
	private String locale = ""; /* 언어설정 */ 
	private String menuDc = ""; /* 메뉴설명 */ 
	private String menuPath = ""; /* 메뉴경로 */ 
	private String menuLvl = ""; /* 메뉴레벨 */ 
	private String menuNum = ""; /* 메뉴순번 */ 
	private String uprMenuId = ""; /* 상위메뉴아이디 */ 
	private String leftMenuYn = ""; /* 좌측메뉴여부 */ 
	private String userAuth = ""; /* 사용자 권한 */ 
	private String useYn = "Y"; /* 사용여부 */ 
	private String chat = ""; /* 상담인지 아닌지 상담의 하위는 jsp의 class명 상담메뉴는 Y, N으로 들어간다. */ 	
	private String createDt = ""; /* 등록 일시 */ 
	private String creater = ""; /* 등록자 */ 
	private String updateDt = ""; /* 수정 일시 */ 
	private String updater = ""; /* 수정자 */ 

}
