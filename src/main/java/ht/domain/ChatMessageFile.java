package ht.domain;

import lombok.Data;

/**
 * 파일 메세지 전송시 파일 객체
 */
@Data
public class ChatMessageFile {

	private String fileNum;
	private String fileNm;
	private String fileType;
}
