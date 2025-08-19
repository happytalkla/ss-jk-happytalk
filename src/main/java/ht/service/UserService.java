package ht.service;

import static ht.constants.CommonConstants.MEMBER_DIV_CD_C;
import static ht.constants.CommonConstants.RESULT_CD_ERROR;
import static ht.constants.CommonConstants.RESULT_CD_FAILURE;
import static ht.constants.CommonConstants.RESULT_CD_LOGIN_DEFAULT_PASSWD;
import static ht.constants.CommonConstants.RESULT_CD_LOGIN_USED_PASSWD;
import static ht.constants.CommonConstants.RESULT_CD_LOGIN_WRONG_CURRENT_PASSWD;
import static ht.constants.CommonConstants.RESULT_CD_SUCCESS;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import ht.exception.BizException;
import ht.persistence.MemberDao;
import ht.persistence.SettingDao;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Resource
	private MemberDao memberDao;
	@Resource
	private SettingDao settingDao;
	@Resource
	private McaService mcaService;

	/**
	 * 회원 정보 조회
	 */
	public Map<String, Object> selectUserById(String userId) {
		return memberDao.selectMemberById(userId);
	}

	/**
	 * 회원 목록 조회
	 */
	public List<Map<String, Object>> selectMemberList(Map<String, Object> param) {
		List<Map<String, Object>> resultList = memberDao.selectMemberList(param);

		for(Map<String, Object> map : resultList) {
			map.put("name", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(map.get("name"), "")) );
			map.put("coc_id", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(map.get("coc_id"), "")) );
		}
		return resultList;
	}

	/**
	 * 부서 리스트 조회
	 */
	public List<Map<String, Object>> selectDepartList(Map<String, Object> param) {
		return memberDao.selectDepartList(param);
	}

	/**
	 * 회원 조회
	 */
	public Map<String, Object> selectMember(Map<String, Object> param) {
		Map<String, Object> map = memberDao.selectMember(param);

		map.put("name", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(map.get("name"), "")) );
		map.put("coc_id", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(map.get("coc_id"), "")) );

		return map;
	}

	/**
	 * 조건별 회원수 조회
	 */
	public Map<String, Object> selectMemberTypeCount(Map<String, Object> param) {
		return memberDao.selectMemberTypeCount(param);
	}
	
	
	
	/**
	 * 계정 활성화 체크
	 */
	@Transactional
	public Map<String, Object> accessIPCheck(Map<String, Object> param) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Map<String, Object> sqlParams = new HashMap<>();
		String username = (String) param.get("username");
		sqlParams.put("cocId", username);
		Map<String, Object> member = memberDao.selectCMember(sqlParams);

		if (member == null) {
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "로그인 정보가 잘못되었습니다.");
			return rtnMap;
		} else if ("Y".equals(member.get("leave_yn"))) {
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "해당 계정은 중지 상태입니다. 관리자에게 문의해 주시기 바랍니다.");
			return rtnMap;
		}
		String allowAccessIP = (String) member.get("allow_access_ip");
		log.info("allowAccessIP: {}", allowAccessIP);

		if (!Strings.isNullOrEmpty(allowAccessIP)) {
			String linkIp = (String) param.get("linkIp");

			if (!Strings.isNullOrEmpty(passwdEncoding) && idEncoding.equals(passwdEncoding)) {
				String passwdEncodingInput = StringUtil.encryptSHA256(String.valueOf(param.get("password")));

				if (passwdEncodingInput.equals(passwdEncoding)) {
					rtnMap.put("rtnCd", RESULT_CD_LOGIN_DEFAULT_PASSWD);
					rtnMap.put("rtnMsg", "초기 비밀번호를 사용하고 있습니다. 비밀번호를 변경해주세요.");
				} else {
					rtnMap.put("rtnCd", RESULT_CD_ERROR);
					rtnMap.put("rtnMsg", "로그인 정보가 잘못되었습니다.");
				}

				return rtnMap;
			}
		}
		// allowAccessIp 가 null이면 로그인 성공
		rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
		return rtnMap;
	}
	
	
	
	
	/**
	 * 회원 등록
	 */
	@Transactional
	public String insertMember(Map<String, Object> param) throws Exception {

		// 상담원이 아니면 상위 상담관리자 제거
		String memberDivCd = StringUtil.nvl(param.get("memberDivCd"));
		if (!MEMBER_DIV_CD_C.equals(memberDivCd)) {
			param.put("upperMemberUid", "");
		}

		param.put("name", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("name"), "")) );
		param.put("id", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("id"), "")) );

		String memberUid ="";

		//초기비밀번호 현재 등록년월일(yyyymmdd) 세팅
//		String pwd = (String) param.get("pwd");
		Date date = new Date();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd"); // Java 시간 더하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String pwd = sdformat.format(cal.getTime());
		String encryptPwd = StringUtil.encryptSHA256(pwd);
		param.put("pwd", encryptPwd);

		List<Map<String, Object>> list = memberDao.selectMemberForInsert(param);
		if (list != null && list.size() > 0) {
			Map<String, Object> map = list.get(0);
			String leaveYn = StringUtil.nvl(map.get("leave_yn"));
			if ("Y".equals(leaveYn)) {
				// 기존에 탈퇴한 회원이 존재하면 해당 회원 update
				param.put("memberUid", map.get("member_uid"));
				param.put("validYn", "N");
				memberDao.updateMember(param);
				memberUid = (String) map.get("member_uid");

				// 상담원이면 나를 상위 상담관리자로 지정한 경우 모두 제거
				if(MEMBER_DIV_CD_C.equals(memberDivCd)) {
					memberDao.deleteUpperMemberUid(param);
				}
			} else {
				throw new BizException("등록된 회원이 존재합니다.");
			}
		} else {
			memberUid = memberDao.selectMemberKey(param);
			param.put("memberUid", memberUid);
			memberDao.insertMember(param);
		}

		// 회원 설정 정보 등록
		memberDao.mergeMemberSet(param);
		return memberUid;
	}

	/**
	 * 회원 수정
	 */
	@Transactional
	public void updateMember(Map<String, Object> param) {
		// 상담원이 아니면 상위 상담관리자가 제거
		String memberDivCd = StringUtil.nvl(param.get("memberDivCd"));
		if(!MEMBER_DIV_CD_C.equals(memberDivCd)) {
			param.put("upperMemberUid", "");
		}

		param.put("name", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("name"), "")) );

		memberDao.updateMember(param);

		// 상담원이면 나를 상위 상담관리자로 지정한 경우 모두 제거
		if(MEMBER_DIV_CD_C.equals(memberDivCd)) {
			memberDao.deleteUpperMemberUid(param);
		}
	}

	/**
	 * 비밀번호 변경
	 *
	 * @param isReset 비밀번호 변경이 아닌 리셋
	 */
	@Transactional
	public String changePasswd(Map<String, Object> param, boolean isReset, boolean withValidate) {

		Map<String, Object> sqlParams = new HashMap<>();
		if (!Strings.isNullOrEmpty((String) param.get("memberUid"))) {
			sqlParams.put("memberUid", param.get("memberUid"));
		} else if (!Strings.isNullOrEmpty((String) param.get("cocId"))) {
			sqlParams.put("cocId", param.get("cocId"));
		}
		if (sqlParams.isEmpty()) {
			log.error("EMPTY IDENTITY");
			return RESULT_CD_ERROR;
		}

		Map<String, Object> member = memberDao.selectCMember(sqlParams);

		// 비밀번호 초기화 기본값 yyyyMMdd
		if(isReset) {
			SimpleDateFormat passForm = new SimpleDateFormat("yyyyMMdd");
			Date passChgTime = new Date();
			String newResetPass = passForm.format(passChgTime);
			param.put("newPassword", newResetPass.toString());
		}
		//System.out.println("=====================================" + param.get("newPassword").toString());
		// 현재 비밀번호 체크
		String currentPasswd = (String) member.get("pwd");
		String oldPasswd = (String) param.get("oldPassword");
		if (!Strings.isNullOrEmpty(oldPasswd)) {
			String oldEncryptPasswd = StringUtil.encryptSHA256(oldPasswd);
			if (!oldEncryptPasswd.equals(currentPasswd)) {
				return RESULT_CD_LOGIN_WRONG_CURRENT_PASSWD;
			}
		}

		String newPasswd = (String) param.get("newPassword");
		String newEncryptPasswd = StringUtil.encryptSHA256(newPasswd);

		if (!isReset) {
			if (newEncryptPasswd.equals(member.get("pwd"))
					|| newEncryptPasswd.equals(member.get("pwd2"))
					|| newEncryptPasswd.equals(member.get("pwd3"))) {
				return RESULT_CD_LOGIN_USED_PASSWD;
			}
		}

		sqlParams = new HashMap<String, Object>();
		sqlParams.put("memberUid", param.get("memberUid"));
		sqlParams.put("cocId", param.get("cocId"));
		sqlParams.put("username", param.get("username"));
		if (withValidate) {
			sqlParams.put("validYn", "Y");
		}
		if (!sqlParams.isEmpty()) {
			sqlParams.put("pwd", newEncryptPasswd);
			memberDao.changePasswd(sqlParams);
		}

		return RESULT_CD_SUCCESS;
	}

	/**
	 * 계정 활성화 체크
	 */
	@Transactional
	public Map<String, Object> selectValidCheck(Map<String, Object> param) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		String username = (String) param.get("username");

		//String idEncoding = StringUtil.encryptSHA256(username);
		SimpleDateFormat passForm = new SimpleDateFormat("yyyyMMdd");
		Date passChgTime = new Date();
		String idEncoding = StringUtil.encryptSHA256(passForm.format(passChgTime));

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("cocId", username);
		Map<String, Object> member = memberDao.selectCMember(sqlParams);

		if (member == null) {
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "로그인 정보가 잘못되었습니다.");
			return rtnMap;
		} else if ("N".equals(member.get("valid_yn"))) {
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "해당 계정은 중지 상태입니다. 관리자에게 문의해 주시기 바랍니다.");
			return rtnMap;
		} else if ("Y".equals(member.get("leave_yn"))) {
			rtnMap.put("rtnCd", RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "해당 계정은 중지 상태입니다. 관리자에게 문의해 주시기 바랍니다.");
			return rtnMap;
		}

		log.info("idEncoding: {}", idEncoding);

		if (!Strings.isNullOrEmpty(idEncoding)) {
			String passwdEncoding = (String) member.get("pwd");

			if (!Strings.isNullOrEmpty(passwdEncoding) && idEncoding.equals(passwdEncoding)) {
				String passwdEncodingInput = StringUtil.encryptSHA256(String.valueOf(param.get("password")));

				if (passwdEncodingInput.equals(passwdEncoding)) {
					rtnMap.put("rtnCd", RESULT_CD_LOGIN_DEFAULT_PASSWD);
					rtnMap.put("rtnMsg", "초기 비밀번호를 사용하고 있습니다. 비밀번호를 변경해주세요.");
				} else {
					rtnMap.put("rtnCd", RESULT_CD_ERROR);
					rtnMap.put("rtnMsg", "로그인 정보가 잘못되었습니다.");
				}

				return rtnMap;
			}
		}

		rtnMap.put("rtnCd", RESULT_CD_SUCCESS);
		return rtnMap;
	}

	/**
	 * 로그인 실패 6회이상 체크
	 */
	@Transactional
	public int selectFailCheck(Map<String, Object> param) {

		return memberDao.selectFailCheck(param);

	}

	/**
	 * 아이디 비밀번호 체크 및 아너스넷 비밀번호 체크
	 * @throws Exception
	 */
	@Transactional
	public int selectIdPwdCheck(Map<String, Object> param) throws Exception {

		// 유저 존재 여부 (아이디로 조회)
		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("id", param.get("username"));
		Map<String, Object> member = memberDao.selectCMember(sqlParams);

		boolean isUserExist = member != null;
		if (!isUserExist) {
			return 0;
		}

		// 암호 체크
		int loginFailCnt = ((BigDecimal) member.get("login_fail_cnt")).intValue();

		sqlParams = new HashMap<>();
		sqlParams.put("id", param.get("username"));
		sqlParams.put("pwd", StringUtil.encryptSHA256((String)param.get("password")));
		member = memberDao.selectCMember(sqlParams);

		if (member != null) {
			String id = (String)member.get("id");

			/***************************************
			 * Honors-net Skip User -> skipUser
			 ***************************************/
			int skipUser = isSkipUserYn(member);
			if(skipUser < 1) {
				int honorKey = isHonorsnetYn(member,id);

				switch (honorKey) {
				case 1:
					break;
				case 2:
					return 2;
				case 3:
					return 3;
				default:
					break;
				}
			}

			param.put("loginFailCnt", 0);
			memberDao.updatePwdFail(param);
			return 1;
		} else {
			param.put("loginFailCnt", loginFailCnt + 1);
			if (loginFailCnt + 1 == 6) {
				param.put("validYn", "N");
			}
			memberDao.updatePwdFail(param);
			return 0;
		}
	}

	/**
	 * 비밀번호 3회 중복체크
	 */
	@Transactional
	public int passwd3DuplCheck(Map<String, Object> param) {

		String pwd=(String) param.get("pwd");
		String encryptPwd = StringUtil.encryptSHA256(pwd);
		param.put("pwd", encryptPwd);
		return memberDao.passwd3DuplCheck(param);
	}

	/**
	 * 비밀번호 90일 이상 체크
	 */
	@Transactional
	public Map<String, Object> passwd90Check(Map<String, Object> param) {
		int cnt = memberDao.passwd90Check(param);
		String memberUid = memberDao.selectMemberUid(param);
		Map<String, Object> result = new HashMap<>();
		result.put("cnt", cnt);
		result.put("memberUid", memberUid);
		return result;
	}

	/**
	 * 로그인 날짜 update
	 */
	@Transactional
	public int updateLoginDate(Map<String, Object> param) {

		return memberDao.updateLoginDate(param);
	}

	/**
	 * 기존 비밀번호 체크
	 */
	@Transactional
	public int selectOldPwdCheck(Map<String, Object> param) {

		String pwd=(String) param.get("oldPassword");
		String encryptPwd = StringUtil.encryptSHA256(pwd);
		param.put("password", encryptPwd);
		return memberDao.selectIdPwdCheck(param);
	}

	/**
	 * 카테고리 회원 매핑 삭제 및 저장
	 */
	@Transactional
	public void updateCtgMemberMapping(Map<String, Object> param, String[] ctgNumArr) {

		memberDao.deleteCtgMemberMapping(param);
		if (ctgNumArr != null) {
			param.put("ctgNumArr", ctgNumArr);
			memberDao.insertCtgMemberMapping(param);
			memberDao.updateCtgMemberMapping(param);
		}
	}

	/**
	 * 계정 활성화
	 */
	@Transactional
	public void updateMemberValid(Map<String, Object> param) throws BizException {
		Map<String, Object> settingInfo = settingDao.selectDefaultSet();
		String licenseCnt = StringUtil.nvl(settingInfo.get("license"), "0");
		int maxCnt = Integer.parseInt(licenseCnt);
		int validMemberCount = memberDao.selectValidMemberCount();

		// 계정 활성시 라이센스 개수 체크
		if ("Y".equals(param.get("validYn"))) {
			if (maxCnt <= validMemberCount) {
				throw new BizException(RESULT_CD_FAILURE,
						"활성화 할 수 있는 상담직원의 수(" + maxCnt + ")를 초과하였습니다.\n기존 활성화된 계정의 인증을 해제하시거나 삭제 후 다시 시도하여 주세요.");
			}
		}

		memberDao.updateMemberValid(param);
	}

	/**
	 * 회원 삭제
	 */
	@Transactional
	public void deleteMember(Map<String, Object> param) {

		memberDao.deleteMember(param);
		memberDao.deleteCtgMemberMapping(param);
	}

	/**
	 * 임시 회원 목록 조회
	 */
	public List<Map<String, Object>> selectTmpMemberList(Map<String, Object> param) {
		return memberDao.selectTmpMemberList(param);
	}


	/**
	 * 아너스넷 비밀번호 변경
	 *
	 * @param isReset 비밀번호 변경이 아닌 리셋
	 */
	@Transactional
	public String changeHonorPasswd(Map<String, Object> param, boolean isReset, boolean withValidate) {

		Map<String, Object> sqlParams = new HashMap<>();
		if (!Strings.isNullOrEmpty((String) param.get("memberUid"))) {
			sqlParams.put("memberUid", param.get("memberUid"));
		}
		if (!Strings.isNullOrEmpty((String) param.get("username"))) {
			sqlParams.put("username", param.get("username"));
		}
		if (!Strings.isNullOrEmpty((String) param.get("odtbrCode"))) {
			sqlParams.put("odtbrCode", param.get("odtbrCode"));
		}
		if (!Strings.isNullOrEmpty((String) param.get("honorsPwd"))) {
			sqlParams.put("honorsPwd", param.get("honorsPwd"));
		}
		if (!Strings.isNullOrEmpty((String) param.get("id"))) {
			sqlParams.put("id",param.get("id"));
		}
		if (sqlParams.isEmpty()) {
			log.error("EMPTY IDENTITY");
			return RESULT_CD_ERROR;
		}

		if (!sqlParams.isEmpty()) {
			memberDao.changeHonorPasswd(sqlParams);
		}

		return RESULT_CD_SUCCESS;
	}

	/**
	 * 아너스넷 비밀번호 조회 체크 후 부지점코드 업데이트
	 * input  : Map, String
	 * output : 1:정상, 2:아너스넷비밀번호 틀림 or member Table에 아너스넷 비밀번호 없음
	 */
	public int isHonorsnetYn(Map<String, Object> member, String id) throws Exception {
		/* 아너스넷 비밀번호,부지점코드 조회  */
		Map<String,Object> honors = memberDao.selectHonorsNetPw(member);

		/* 아너스넷 체크 */
		if(honors != null) {
			String honorsPwd = (String)honors.get("honors_pwd");
			if(honorsPwd != null) {
				Map<String,Object> honorsnet = mcaService.pfdz103p( id , id , honorsPwd);

				String honorYn = String.valueOf(honorsnet.get("H_osucs_yn"));
				if(honorYn.equals("Y")) {
					member.put("odtbrCode", honorsnet.get("H_odtbr_code"));
					memberDao.changeHonorPasswd(member);
				}else {	//아너스넷 비밀번호 다를경우, 없는경우
					return 2;
				}
			}else {
				return 2;
			}
		}else {
			return 2;
		}
		return 1;
	}


	/**
	 * 아너스넷 스킵 유저 체크
	 * @param member
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int isSkipUserYn(Map<String, Object> member) throws Exception{
		return memberDao.selectSkipUser(member);
	}
}
