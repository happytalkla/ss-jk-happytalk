package ht.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 코드에 대한 기본 상수값 설정
 * @author wizard
 * @formatter:off
 */
public class CommonConstants {
	public static final String MCA_CHANNEL_CODE				= "D02";						// MCA 채널코드

	// 처리 메세지 코드
	public static final String RESULT_CD_SUCCESS				= "SUCCESS";				// 성공
	public static final String RESULT_CD_FAILURE				= "FAILURE";				// 실패
	public static final String RESULT_NOT_FOUND					= "NOT_FOUND";				// 실패
	public static final String RESULT_CD_ERROR					= "ERROR";					// 오류
	public static final String RESULT_CD_LOGIN_DEFAULT_PASSWD	= "RESULT_CD_LOGIN_DEFAULT_PASSWD";	// 초기 비번 사용중
	public static final String RESULT_CD_LOGIN_USED_PASSWD		= "RESULT_CD_LOGIN_USED_PASSWD";	// 기존 비번으로 변경 시도
	public static final String RESULT_CD_LOGIN_WRONG_CURRENT_PASSWD		= "RESULT_CD_LOGIN_WRONG_CURRENT_PASSWD";	// 현재 비번 틀림

	// 공통 코드 그룹
	public static final String COMM_CD_MEMBER_DIV_CD			= "MEMBER_DIV_CD";			// 회원 구분 코드
	public static final String COMM_CD_WORK_STATUS_CD			= "WORK_STATUS_CD"; 		// 근무 상태 코드
	public static final String COMM_CD_MSG_DIV_CD				= "MSG_DIV_CD"; 			// 메세지 구분 코드
	public static final String COMM_CD_CNSR_DIV_CD				= "CNSR_DIV_CD"; 			// 상담직원 구분 코드
	public static final String COMM_CD_CSTM_LINK_DIV_CD			= "CSTM_LINK_DIV_CD"; 		// 고객 접속 구분 코드
	public static final String COMM_CD_CHAT_ROOM_STATUS_CD		= "CHAT_ROOM_STATUS_CD"; 	// 채팅 룸 상태 코드
	public static final String COMM_CD_END_DIV_CD				= "END_DIV_CD"; 			// 종료 구분 코드
	public static final String COMM_CD_SEND_DIV_CD				= "SEND_DIV_CD"; 			// 전송 구분 코드
	public static final String COMM_CD_CONT_DIV_CD				= "CONT_DIV_CD"; 			// 내용 구분 코드
	public static final String COMM_CD_MSG_STATUS_CD			= "MSG_STATUS_CD"; 			// 메세지 상태 코드
	public static final String COMM_CD_TPL_DIV_CD				= "TPL_DIV_CD"; 			// 템플릿 구분 코드
	public static final String COMM_CD_TPL_MSG_DIV_CD			= "TPL_MSG_DIV_CD"; 		// 템플릿 메세지 구분 코드
	public static final String COMM_CD_CHNG_DIV_CD				= "CHNG_DIV_CD"; 			// 변경 구분 코드
	public static final String COMM_CD_AVATAR_DIV_CD			= "AVATAR_DIV_CD"; 			// 아바타 구분 코드
	public static final String COMM_CD_LOG_DIV_CD				= "LOG_DIV_CD"; 			// 로그 구분 코드
	public static final String COMM_CD_NOTICE_DIV_CD			= "NOTICE_DIV_CD"; 			// 공지 구분 코드
	public static final String COMM_CD_MEMBER_ROLE_CD			= "MEMBER_ROLE_CD"; 		// 회원 권한 코드
	public static final String COMM_CD_SENDER_DIV_CD			= "SENDER_DIV_CD"; 			// 송신자 구분 코드
	public static final String COMM_CD_CSTM_DIV_CD				= "CSTM_DIV_CD"; 			// 고객 구분 코드
	public static final String COMM_CD_CSTM_OS_DIV_CD			= "CSTM_OS_DIV_CD"; 		// 고객 OS 구분 코드
	public static final String COMM_CD_API_DIV_CD				= "API_DIV_CD";				// 호출 API 구분 코드
	public static final String COMM_CD_DEPART_CD				= "DEPART_CD";				// 부서 코드


	// 공통 코드 상세
	public static final String MEMBER_DIV_CD_S					= "S";						// 슈퍼 관리자
	public static final String MEMBER_DIV_CD_A					= "A";						// 사이트 관리자
	public static final String MEMBER_DIV_CD_M					= "M";						// 매니저
	public static final String MEMBER_DIV_CD_C					= "C";						// 상담직원
	public static final String MEMBER_DIV_CD_R					= "R";						// 챗봇
	public static final String MEMBER_DIV_CD_U					= "U";						// 고객

	public static final String WORK_STATUS_CD_W					= "W";						// 근무중
	public static final String WORK_STATUS_CD_R					= "R";						// 휴식중

	public static final String MSG_DIV_CD_T						= "T";						// 텍스트
	public static final String MSG_DIV_CD_I						= "I";						// 이미지

	public static final String CNSR_DIV_CD_C					= "C";						// 상담직원
	public static final String CNSR_DIV_CD_R					= "R";						// 로봇

	public static final String CSTM_LINK_DIV_CD_A				= "A";						// 홈페이지
	public static final String CSTM_LINK_DIV_CD_B				= "B";						// 카카오톡
	public static final String CSTM_LINK_DIV_CD_C				= "C";						// O2 talk
	public static final String CSTM_LINK_DIV_CD_D				= "D";						// mPOP톡
	public static final String CSTM_LINK_DIV_CD_E				= "E";						// ARS

	public static final String CHANNEL_CODE_A					= "H01";					//Homepage
	public static final String CHANNEL_CODE_B					= "KKO";					//KAKAO
	public static final String CHANNEL_CODE_C					= "I50";					//O2 talk
	public static final String CHANNEL_CODE_D					= "I48";					//mPop
	/*
	 * IPCC_MCH ARS 채널 추가
	 * TODO: 채널 코드 확인 필요
	 */
	public static final String CHANNEL_CODE_E					= "I99";					// ARS
	public static final Map<Long, String> CSTM_LINK_MAP = new HashMap<>();
	static {
		CSTM_LINK_MAP.put(0L, CSTM_LINK_DIV_CD_A);
		CSTM_LINK_MAP.put(2L, CSTM_LINK_DIV_CD_B);
		CSTM_LINK_MAP.put(3L, CSTM_LINK_DIV_CD_C);
		CSTM_LINK_MAP.put(4L, CSTM_LINK_DIV_CD_D);
		/*
		 * IPCC_MCH ARS 채널 추가
		 */
		CSTM_LINK_MAP.put(5L, CSTM_LINK_DIV_CD_E);
	}

	public static final Map<String, String> CSTM_LINK_STRING_MAP = new HashMap<>();
	static {
		CSTM_LINK_STRING_MAP.put("A", CSTM_LINK_DIV_CD_A);
		CSTM_LINK_STRING_MAP.put("B", CSTM_LINK_DIV_CD_B);
		CSTM_LINK_STRING_MAP.put("C", CSTM_LINK_DIV_CD_C);
		CSTM_LINK_STRING_MAP.put("D", CSTM_LINK_DIV_CD_D);
		/*
		 * IPCC_MCH ARS 채널 추가
		 */
		CSTM_LINK_STRING_MAP.put("E", CSTM_LINK_DIV_CD_E);
	}
	public static final Map<String, String> CHANNEL_ID_MAP = new HashMap<>();
	static {
		CHANNEL_ID_MAP.put(CSTM_LINK_DIV_CD_A, "0");
		CHANNEL_ID_MAP.put(CSTM_LINK_DIV_CD_B, "2");
		CHANNEL_ID_MAP.put(CSTM_LINK_DIV_CD_C, "3");
		CHANNEL_ID_MAP.put(CSTM_LINK_DIV_CD_D, "4");
		/*
		 * IPCC_MCH ARS 채널 추가
		 * TODO: 채널 ID 확인 필요
		 */
		CHANNEL_ID_MAP.put(CSTM_LINK_DIV_CD_E, "5");
	}

	public static final String CHAT_ROOM_STATUS_CD_BOT				= "11";						// 챗봇 상담 중
	public static final String CHAT_ROOM_STATUS_CD_WAIT_CNSR		= "31";						// 상담사 접수 대기(미배정)
	public static final String CHAT_ROOM_STATUS_CD_ASSIGN			= "41";						// 미접수(상담직원 최초 응대전)
	public static final String CHAT_ROOM_STATUS_CD_NEED_ANSWER		= "51";						// 응대 필요(고객 답변 후 상담직원 답변 필요)
	public static final String CHAT_ROOM_STATUS_CD_WAIT_REPLY		= "61";						// 고객 답변 대기중(상담직원 답변 후 고객 답변 대기중)
	public static final String CHAT_ROOM_STATUS_CD_END				= "91";						// 종료(상담직원 종료, 챗봇 종료, 자동 종료)
	public static final String CHAT_ROOM_STATUS_CD_MANAGER_COUNSEL	= "81";						// 매니저 상담요청

	//	public static final String CHAT_ROOM_STATUS_CD_11			= "11";						// 챗봇 상담
	//	public static final String CHAT_ROOM_STATUS_CD_31			= "31";						// 상담직원 접수 대기
	//	public static final String CHAT_ROOM_STATUS_CD_51			= "51";						// 상담직원 입장 대기
	//	public static final String CHAT_ROOM_STATUS_CD_71			= "71";						// 상담중
	//	public static final String CHAT_ROOM_STATUS_CD_91			= "91";						// 종료

	public static final String END_DIV_CD_CENTER_OFF_DUTY			= "CENTER_OFF_DUTY";		// 근무시간외 종료
	public static final String END_DIV_CD_COUNSELOR_POOL_OVER		= "COUNSELOR_POOL_OVER";	// 미배정 개수 초과 메세지
	public static final String END_DIV_CD_CNSR_CSTM					= "C_CSTM";					// 상담직원과 채팅 - 고객 종료
	public static final String END_DIV_CD_CNSR_CSTM_WAIT_CNSR		= "C_CSTM_WAIT_CNSR";		// 상담직원과 채팅 - 고객 종료 - 미배정 상태
	public static final String END_DIV_CD_CNSR_CSTM_ASSIGN_CNSR		= "C_CSTM_ASSIGN_CNSR";		// 상담직원과 채팅 - 고객 종료 - 배정 상태
	public static final String END_DIV_CD_CNSR_CNSR					= "C_CNSR";					// 상담직원과 채팅 - 상담직원 종료
	public static final String END_DIV_CD_CNSR_TIME					= "C_TIME";					// 상담직원과 채팅 - 시간 자동 종료
	public static final String END_DIV_CD_CNSR_BAT					= "C_BAT";					// 상담직원과 채팅 - 날짜 경과 자동 종료
	public static final String END_DIV_CD_BOT_CSTM					= "B_CSTM";					// 봇상담 - 고객 종료
	public static final String END_DIV_CD_BOT_TIME					= "B_TIME";					// 봇상담 - 시간 자동 종료
	public static final String END_DIV_CD_BOT_BAT					= "B_BAT";					// 상담직원과 채팅 - 날짜 경과 자동 종료
	public static final String END_DIV_CD_NORMAL					= "NORMAL";					// 종료

	//	public static final String END_DIV_CD_A						= "A";						// 고객 종료
	//	public static final String END_DIV_CD_B						= "B";						// 상담직원 종료
	//	public static final String END_DIV_CD_C						= "C";						// 자동 종료
	//	public static final String END_DIV_CD_D						= "D";						// 봇상담 종료
	//	public static final String END_DIV_CD_E						= "E";						// 종료

	public static final String SEND_DIV_CD_R					= "R";						// 로봇
	public static final String SEND_DIV_CD_C					= "C";						// 상담직원
	public static final String SEND_DIV_CD_U					= "U";						// 고객

	public static final String CONT_DIV_CD_T					= "T";						// 텍스트
	public static final String CONT_DIV_CD_F					= "F";						// 파일
	public static final String CONT_DIV_CD_M					= "M";						// 메모
	public static final String CONT_DIV_CD_L					= "L";						// 로그
	public static final String CONT_DIV_CD_E					= "E";						// 이벤트

	public static final String MSG_STATUS_CD_SEND				= "SEND";					// 송신
	public static final String MSG_STATUS_CD_RECEIVE			= "RECEIVE";				// 수신
	public static final String MSG_STATUS_CD_READ				= "READ";					// 읽음

	public static final String TPL_DIV_CD_P						= "P";						// 개인
	public static final String TPL_DIV_CD_G						= "G";						// 그룹

	public static final String TPL_MSG_DIV_CD_TEXT				= "TEXT";					// 텍스트
	public static final String TPL_MSG_DIV_CD_NORMAL			= "NORMAL";					// 텍스트 + 이미지 + 링크

	public static final String CHNG_DIV_CD_AUTO					= "AUTO";					// 자동 매칭
	public static final String CHNG_DIV_CD_MANAGER				= "MANAGER";				// 매니저가 지정
	public static final String CHNG_DIV_CD_COUNSEL				= "COUNSEL";				// 상담직원가 지정
	public static final String CHNG_DIV_CD_SELF					= "SELF";					// 본인이 선택

	public static final String AVATAR_DIV_CD_C					= "C";						// 상담직원
	public static final String AVATAR_DIV_CD_U					= "U";						// 고객

	public static final String LOG_DIV_CD_A						= "A";						// 로그인
	public static final String LOG_DIV_CD_B						= "B";						// 서비스 기본 설정
	public static final String LOG_DIV_CD_C						= "C";						// 서비스 관리
	public static final String LOG_DIV_CD_D						= "D";						// 상담 관리
	public static final String LOG_DIV_CD_E						= "E";						// 시스템
	public static final String LOG_DIV_CD_F						= "F";						// 로그아웃

	public static final String NOTICE_DIV_CD_A					= "A";						// 공지
	public static final String NOTICE_DIV_CD_B					= "B";						// 매니저 공지
	public static final String NOTICE_DIV_CD_C					= "C";						// 매니저의 상담직원 공지

	public static final String MEMBER_ROLE_CD_S					= "ROLE_SUPER";				// 슈퍼 관리자
	public static final String MEMBER_ROLE_CD_A					= "ROLE_ADMIN";				// 사이트 관리자
	public static final String MEMBER_ROLE_CD_M					= "ROLE_MANAGER";			// 매니저
	public static final String MEMBER_ROLE_CD_C					= "ROLE_COUNSELOR";			// 상담직원

	public static final String SENDER_DIV_CD_C					= "C";						// 상담직원
	public static final String SENDER_DIV_CD_R					= "R";						// 로봇
	public static final String SENDER_DIV_CD_U					= "U";						// 고객
	public static final String SENDER_DIV_CD_S					= "S";						// 시스템

	public static final String CSTM_DIV_CD_CSTM					= "CSTM";					// 일반회원
	public static final String CSTM_DIV_CD_MEMBER				= "MEMBER";					// 내근사원
	public static final String CSTM_DIV_CD_FP					= "FP";						// FP
	public static final String CSTM_DIV_CD_CNSR					= "CNSR";					// 상담직원
	public static final List<String> CSTM_DIV_CD_LIST			= new ArrayList<String>();
	static {
		CSTM_DIV_CD_LIST.add(CSTM_DIV_CD_CSTM);
		CSTM_DIV_CD_LIST.add(CSTM_DIV_CD_MEMBER);
		CSTM_DIV_CD_LIST.add(CSTM_DIV_CD_FP);
		CSTM_DIV_CD_LIST.add(CSTM_DIV_CD_CNSR);
	}

	public static final String CSTM_OS_DIV_CD_WEB				= "pc";						// pc
	public static final String CSTM_OS_DIV_CD_MOBILE			= "mobile";					// mobile
	public static final String CSTM_OS_DIV_CD_ANDROID			= "android";				// android
	public static final String CSTM_OS_DIV_CD_IOS				= "ios";					// ios


	public static final String DEFAULT_CTG_NUM					= "9999999999";				// 미분류 카테고리 번호

	public static final String DEFAULT_CTG_MGT_DPT				= "2";						// 기본 카테고리 깊이

	public static final int PAGE_LIST_COUNT						= 10;						// 페이지의 목록 개수
	public static final int PAGE_LOG_LIST_COUNT					= 20;						// 로그 페이지의 목록 개수
	public static final int PAGE_CHAT_LIST_COUNT				= 15;						// 상담관리, 채팅 페이지의 목록 개수
	public static final int PAGE_KNOW_LIST_COUNT				= 10;						// 지식화 관리 페이지의 목록 개수

	public static final String DEFAULT_SKIN_CSS_URL				= "/css/include.css";		// 고객 채팅창 기본 스킨
	public static final String MPOP_SKIN_CSS_URL				= "/css/include_mpop.css";	// mPOP 채팅창 스킨

	public static final String API_DIV_CD_OUTSIDE				= "OUTSIDE";				// 외부_API
	public static final String API_DIV_CD_INSIDE				= "INSIDE";					// 내부_API

	public static final String BATCH_JOB_ASSIGN					= "ASSIGN";					// 배치 - 배정 스케줄
	public static final String BATCH_JOB_AUTOSTOP				= "AUTOSTOP";				// 배치 - 자동 종료
	public static final String BATCH_JOB_CLEANROOM				= "CLEANROOM";				// 배치 - 채팅방 정리
	public static final String BATCH_JOB_BREAKTIME				= "BREAKTIME";				// 배치 - 휴식 종료
	public static final String BATCH_JOB_STATISTICS				= "STATISTICS";				// 배치 - 통계
	public static final String BATCH_JOB_STATISCHATBOT			= "STATISCHATBOT";			// 배치 - 통계 챗봇
	public static final String BATCH_JOB_STATISTERMDAY			= "STATISTERMDAY";			// 배치 - 통계 용어검색 일별
	public static final String BATCH_JOB_STATISTERMMON			= "STATISTERMMON";			// 배치 - 통계 용어검색 월별
	public static final String BATCH_JOB_MEMBER					= "MEMBER";					// 배치 - 회원
	public static final String BATCH_JOB_LOGIN30CHECK			= "LOGIN30CHECK";			// 배치 - 로그인 여부 30일 체크
	public static final String BATCH_JOB_DELETE_LOG				= "DELETE_LOG";				// 배치 - 로그 파일 삭제

	public static final String BATCH_JOB_END_INFO_TO_BSP		= "END_INFO_TO_BSP";		// 배치 - 종료 정보 BSP 로 전달
	public static final String BATCH_JOB_ENCRYPT_SINFO			= "ENCRYPT_SINFO";			// 배치 - 민감정보 암호화 마이그레이션
	public static final String BATCH_JOB_ENCRYPT_END_MEMO		= "ENCRYPT_END_MEMO";		// 배치 - 후처리 메모 암호화 마이그레이션
	public static final String BATCH_JOB_DIRECTASSIGN			= "DIRECTASSIGN";			// 배치는 아니나 테이블 이용해서 동시 배정 차단을 위해 설정함
	/**
	 * IPCC_ADV 고객여정 I/F 실패 건 재송신
	 */
	public static final String BATCH_JOB_RESEND_GENESYS			= "RESEND_GENESYS";			// 배치 - 고객여정 I/F 실패 건 재송신
	/**
	 * IPCC_IST 통합통계 FTP 파일 전송
	 */
	public static final String BATCH_JOB_FILE_SEND_IST			= "FILE_SEND_IST";		    // 배치 - 통합통계 FTP 파일 전송

	public static String getUserRole(String memberDivCd){
		if (MEMBER_DIV_CD_S.equals(memberDivCd)){
			return MEMBER_ROLE_CD_S;
		} else if (MEMBER_DIV_CD_A.equals(memberDivCd)){
			return MEMBER_ROLE_CD_A;
		} else if (MEMBER_DIV_CD_M.equals(memberDivCd)){
			return MEMBER_ROLE_CD_M;
		} else if (MEMBER_DIV_CD_C.equals(memberDivCd)){
			return MEMBER_ROLE_CD_C;
		} else {
			return "";
		}
	}

	public static final String LOG_CODE_END_CHATBOT					= "END_CHATBOT";				// 챗봇 종료
	public static final String LOG_CODE_REQUEST_COUNSELOR			= "REQUEST_COUNSELOR";			// 상담직원 연결 요청
	public static final String LOG_CODE_REQUEST_CHANGE_COUNSELOR	= "REQUEST_CHANGE_COUNSELOR";	// 상담직원 변경 요청
	public static final String LOG_CODE_ASSIGN_COUNSELOR			= "ASSIGN_COUNSELOR";			// 상담직원 배정
	public static final String LOG_CODE_REQUEST_CHANGE_DEPART		= "REQUEST_CHANGE_DEPART";		// 부서 변경 요청
	public static final String LOG_CODE_ASSIGN_DEPART				= "ASSIGN_DEPART";				// 부서 배정
	public static final String LOG_CODE_SUBMIT_COUNSELOR			= "SUBMIT_COUNSELOR";			// 상담직원 접수
	public static final String LOG_CODE_MANAGER_COUNSEL				= "MANAGER_COUNSEL";			// 매니저 상담 요청
	public static final String LOG_CODE_MANAGER_COUNSEL_JOIN		= "MANAGER_COUNSEL_JOIN";		// 매니저 상담 입장
	public static final String LOG_CODE_ASSIGN_SCHEDULED			= "LOG_CODE_ASSIGN_SCHEDULED";			// 스케줄

	public static String getLogChatMessage(String logCode) {

		if (LOG_CODE_END_CHATBOT.equals(logCode)) {
			return "챗봇 종료";
		} else if (LOG_CODE_REQUEST_COUNSELOR.equals(logCode)) {
			return "상담직원 연결";
		} else if (LOG_CODE_REQUEST_CHANGE_COUNSELOR.equals(logCode)) {
			return "상담직원 변경 요청";
		} else if (LOG_CODE_ASSIGN_COUNSELOR.equals(logCode)) {
			return "상담직원 배정";
		} else if (LOG_CODE_REQUEST_CHANGE_DEPART.equals(logCode)) {
			return "부서 변경 요청";
		} else if (LOG_CODE_ASSIGN_DEPART.equals(logCode)) {
			return "부서 배정";
		} else if (LOG_CODE_SUBMIT_COUNSELOR.equals(logCode)) {
			return "상담직원 접수";
		} else if (LOG_CODE_MANAGER_COUNSEL.equals(logCode)) {
			return "매니저 상담 요청";
		} else if (LOG_CODE_MANAGER_COUNSEL_JOIN.equals(logCode)) {
			return "매니저 상담 입장";
		} else if (LOG_CODE_ASSIGN_SCHEDULED.equals(logCode)) {
			return "배정 스케줄 됨";
		} else {
			return "";
		}
	}

	public static final String DEPART_CD_NONE					= "NN";				// 기본값
	public static final String DEPART_CD_CS						= "CS";				// CS
	public static final String DEPART_CD_TM						= "TM";				// TM
	public static final String DEPART_CD_FC1					= "F1";				// Family Center 1
	public static final String DEPART_CD_FC2					= "F2";				// Family Center 2
	public static final String DEPART_CD_DC						= "DC";				// Digital Counseling talk
	public static final String DEPART_CD_FM						= "FM";				// FM 영업부서
	/*
	 * IPCC_MCH ARS 부서 코드 추가
	 * TODO: 기존 사용중인 부서 코드가 기간계와 맞춘건지 확인 필요
	 */
	public static final String DEPART_CD_ARS					= "AR";	       		// ARS

	public static final String COC_TYPE_NAME_VIP				= "VIP";			// 고객사 고객 구분, VIP
	public static final String COC_TYPE_NAME_BAN				= "BAN";			// 고객사 고객 구분, 블랙리스트
}
