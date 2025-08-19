package ht.util;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Clob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import ht.domain.ChatContents;
import ht.domain.ChatContents.Balloon;
import ht.domain.ChatContents.DeviceType;
import ht.domain.ChatContents.Section;
import ht.domain.ChatContents.Section.SectionType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

	/**
	 * 공백 또는 null 여부 체크
	 *
	 * @param str
	 * @return 공백 또는 null 이면 true, 아니면 false
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 공백 또는 null 여부 체크
	 *
	 * @param obj
	 * @return 공백 또는 null 이면 true, 아니면 false
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null || "".equals(obj.toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 왼쪽에 문자열 채우기
	 *
	 * @param str
	 *            원본 문자열
	 * @param pad
	 *            붙일 문자열
	 * @param length
	 *            채워서 만들 문자열 길이
	 * @return
	 */
	public static String lpad(String str, String pad, int length) {
		while (str.length() < length) {
			str = pad + str;
		}
		return str;
	}

	public static String lpad(int num, String pad, int length) {
		return lpad(String.valueOf(num), pad, length);
	}

	/**
	 * 오른쪽에 문자열 채우기
	 *
	 * @param str
	 *            원본 문자열
	 * @param pad
	 *            붙일 문자열
	 * @param length
	 *            채워서 만들 문자열 길이
	 * @return
	 */
	public static String rpad(String str, String pad, int length) {
		while (str.length() < length) {
			str = str + pad;
		}
		return str;
	}

	public static String rpad(int num, String pad, int length) {
		return rpad(String.valueOf(num), pad, length);
	}

	/**
	 * MethodName: nvl 널이거나 blank이면 "", 그렇지 않으면 입력값을 리턴
	 *
	 * @param value
	 * @return String
	 */
	public static String nvl(Object value) {
		if (value == null) {
			return "";
		} else {
			return value.toString();
		}
	}

	/**
	 * MethodName: nvl 널이거나 blank이면 def값, 그렇지 않으면 입력값을 리턴
	 *
	 * @param value
	 * @return String
	 */
	public static String nvl(Object value, String def) {
		if (value == null || "".equals(value)) {
			return def;
		} else {
			return value.toString();
		}
	}

	/**
	 * MethodName: nvlList 널이면 빈 List 리턴
	 *
	 * @param obj
	 * @return List<String>
	 */
	@SuppressWarnings("unchecked")
	public static List<String> nvlList(Object obj) {
		if (obj == null) {
			return new ArrayList<>();
		} else {
			return (List<String>) obj;
		}
	}

	// 파일을 변환한다
	public static String convertKorToUTF(String str) {
		try {
			return new String(str.getBytes("8859_1"), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException ex) {
			log.error(ex.getMessage());
			// logger.error(ex.getMessage(), ex);
		}
		return "";
	}

	// 파일을 변환한다
	public static String convertUTFToKor(String str) {
		try {
			return new String(str.getBytes("UTF-8"), "8859_1");
		} catch (UnsupportedEncodingException e) {

			log.error("{}", e.getLocalizedMessage(), e);
		}
		return "";
	}

	/**
	 * 템플릿, HTML 폼 입력을 Json 포맷({@link ht.domain.ChatContents}) 으로 변경
	 */
	public static String buildTemplateContents(
			List<String> inputMsgArr
			, List<Map<String, String>> inputFileArr
			, List<Map<String, String>> linkBtnArr) {

		ChatContents chatContents = new ChatContents();
		ChatContents.Balloon balloon = new ChatContents.Balloon();
		chatContents.add(balloon);

		boolean hasTextSection = false;

		// 이미지
		if (inputFileArr != null && inputFileArr.size() > 0) {
			for (Map<String, String> fileMap : inputFileArr) {
				if(fileMap.get("fileType").contains("image")) {
					ChatContents.Section section = ChatContents.Section.builder()
							.type(ChatContents.Section.SectionType.file)
							.display(fileMap.get("fileType"))
							.data(fileMap.get("fileNm"))
							.extra(fileMap.get("title"))
							.build();
					chatContents.add(section);
					log.debug("chatContents : {}", chatContents);
				}
			}
		}

		// 텍스트
		if (inputMsgArr != null && inputMsgArr.size() > 0) {
			for (String inputMsg : inputMsgArr) {
				ChatContents.Section section = ChatContents.Section.builder()
						.type(ChatContents.Section.SectionType.text)
						.data(inputMsg)
						.build();
				chatContents.add(section);
				hasTextSection = true;
			}
		}

		// 링크
		if (linkBtnArr != null && linkBtnArr.size() > 0) {

			List<ChatContents.Action> actionList = new ArrayList<>();
			ChatContents.Section section = ChatContents.Section.builder()
					.type(ChatContents.Section.SectionType.action)
					.actions(actionList)
					.build();

			for (Map<String, String> linkMap : linkBtnArr) {
				ChatContents.Action action = ChatContents.Action.builder()
						.type(ChatContents.ActionType.link)
						.name(linkMap.get("linkNm"))
						.data(linkMap.get("linkUrl"))
						.deviceType(DeviceType.all)
						.build();
				actionList.add(action);
			}

			chatContents.add(section);
		}

		// PDF 파일
		if (inputFileArr != null && inputFileArr.size() > 0) {
			for (Map<String, String> fileMap : inputFileArr) {
				if(fileMap.get("fileType").contains("pdf")) {
					ChatContents.Section section = ChatContents.Section.builder()
							.type(ChatContents.Section.SectionType.file)
							.display(fileMap.get("fileType"))
							.data(fileMap.get("fileNm"))
							.extra(fileMap.get("title"))
							.build();
					chatContents.add(section);
					log.debug("chatContents : {}", chatContents);
				}
			}
		}


		log.info("last chatContents : {}", chatContents);

		if (hasTextSection) {
			return new Gson().toJson(chatContents);
		} else {
			return new Gson().toJson(chatContents);
		}
	}

	/**
	 * 입력받은 문자열의 주민등록 번호, 핸드폰 번호를 *로 변환하여 돌려준다.
	 * MethodName: getMaskedString
	 *
	 * @param value
	 * @return String
	 */
	public static String getMaskedString(String cont) {

		Gson gson = new Gson();
		ChatContents chatContents = gson.fromJson(cont, ChatContents.class);

		for (Balloon balloon : chatContents.getBalloons()) {
			for (Section section : balloon.getSections()) {
				if (SectionType.text.equals(section.getType())) {
					String value = section.getData();

					String rtnStr = "";
					String regex1 = "(\\d{6})([-./*+~\\s]*)([1234])\\d{6}"; // 주민번호
					rtnStr = value.replaceAll(regex1, "******$2$3******");
					//					rtnStr = value.replaceAll(regex1, "******$2*******");
					// rtnStr = value.replaceAll(regex1, "$1$2*******");

					String regex2 = "(01[0|1|7|8|9])([-./*+~\\s]*)(\\d{3,4})([-./*+~\\s]*)\\d{4}"; // 핸드폰
					rtnStr = rtnStr.replaceAll(regex2, "$1$2****$4****");
					//					rtnStr = rtnStr.replaceAll(regex2, "***$2****$4****");
					// rtnStr = rtnStr.replaceAll(regex2, "$1$2****$4****");

					section.setData(rtnStr);
				}
			}
		}

		return gson.toJson(chatContents);
	}

	/**
	 * 이름의 중간에 * 로 변환후 리턴
	 *
	 * @param value
	 * @return String
	 */
	public static String getMaskedName(String value) {
		String rtnStr = "";
		String regex1 = "^(.).(.+)";

		if (value.length() == 2) {
			regex1 = "^(.).";
			rtnStr = value.replaceAll(regex1, "$1*");
		} else {
			rtnStr = value.replaceAll(regex1, "$1*$2");
		}

		return rtnStr;
	}


	public static String dateFormat(String date, String before, String after) {

		try {
			SimpleDateFormat f1 = new SimpleDateFormat(after);
			SimpleDateFormat f2 = new SimpleDateFormat(before);

			return f1.format(f2.parse(date));
		} catch (ParseException e) {
			HTUtils.batmanNeverDie(e);
		}

		return "";
	}

	/**
	 * {@code &lt;, &gt;}을 {@code <, >}로 변경
	 */
	public static String replaceHtmlTagToAsciiCode(@NotEmpty String str) {

		return str.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	}

	/**
	 * {@code <, >}을 {@code &lt;, &gt;}로 변경
	 */
	public static String replaceHtmlTag(String str) {
		if (str != null) {
			return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		return "";
	}

	/**
	 * {@code <, >}을 {@code &lt;, &gt;}로 변경
	 */
	public static String[] replaceHtmlTag(String[] strArr) {
		String[] rtnStr = new String[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			rtnStr[i] = replaceHtmlTag(strArr[i]);
		}

		return rtnStr;
	}

	/**
	 * CLOB to String
	 */
	public static String clobToString(Clob clob) {

		if (clob == null) {
			return "";
		}

		String readline;
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(clob.getCharacterStream());
			StringBuilder sb = new StringBuilder();
			while ((readline = reader.readLine()) != null) {
				sb.append(readline);
			}

			reader.close();
			return sb.toString();

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return "";
		} finally {
			if (reader != null) {
				reader = null;
			}
		}
	}

	/**
	 * 문자열 암호화
	 */
	public static String encryptSHA256(String str) {

		if (str == null) {
			return null;
		}

		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			return null;
		}
	}

	public static String getFileExtension(String name) {
		String extension = "";
		if(!StringUtil.isEmpty(name)) {
			extension = name.substring(name.lastIndexOf("."));
		}
		return extension;
	}

	public static String changeStrToDouble(String str) {
		log.info("================ str : " + str);
		if(isEmpty(str)) {
			return "";
		}

		try {
			return "" + Double.parseDouble(str);

		}catch (Exception e) {
			// TODO: handle exception
			return str;
		}

	}

/*
 * 문자를 숫자로 변환, 18자리까지 숫자로 변환가능
 */
	public static String changeStrToLong(String str) {

		if(isEmpty(str)) {
			return "";
		}

		try {
			return "" + Long.parseLong(str);

		}catch (Exception e) {
			// TODO: handle exception
			return str;
		}

	}

	/**
	 * 공백이나 null인 값을 -로 표시
	 * @param str
	 * @return
	 */
	public static String changBlankToDash(String str) {

		if(Strings.isNullOrEmpty(str)) {
			return "-";
		}else {
			return str;
		}

	}
}
