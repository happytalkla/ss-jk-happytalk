package ht.service;
import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ht.domain.MemberDetails;
import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Service
@Slf4j
public class MemberAuthService {

	@Resource
	private SqlSession sqlSession;

	public MemberVO selectUserInfoForLogin(String id, String password) throws AuthenticationException {
		Map<String, String> param = new HashMap<>();
		param.put("userId", id);
		String passwdEncoding = StringUtil.encryptSHA256(password);
		//		String idEncoding = StringUtil.encryptSHA256(id);

		//		if (idEncoding.equals(passwdEncoding)) {
		//			throw new ProviderNotFoundException("초기 비밀번호를 사용하고 있습니다. 비밀번호를 변경해주세요.");
		//		}

		//log.debug("PASSWD HASH: {}", passwdEncoding);
		param.put("pwd", passwdEncoding);
		Map<String, String> userInfoMap = sqlSession.selectOne("member.selectUserInfoForLogin", param);

		if (userInfoMap == null) {
			log.info("LOGIN: NO USER: {}", id);
			//			throw new ProviderNotFoundException("사용자가 존재하지 않습니다.");
			throw new ProviderNotFoundException("로그인 정보가 잘못되었습니다.");
		}

		String memberUid = StringUtil.nvl(userInfoMap.get("member_uid"));
		String userId = StringUtil.nvl(userInfoMap.get("id"));
		String userNm = StringUtil.nvl(userInfoMap.get("name"));
		String userPwd = StringUtil.nvl(userInfoMap.get("pwd"));
		String cnspossibleYn = StringUtil.nvl(userInfoMap.get("cns_possible_yn"));
		String validYn = StringUtil.nvl(userInfoMap.get("valid_yn"));
		String upperMemberUid = StringUtil.nvl(userInfoMap.get("upper_member_uid"));
		String upperMemberNm = StringUtil.nvl(userInfoMap.get("upper_member_nm"));
		String departCd = StringUtil.nvl(userInfoMap.get("depart_cd"));
		String departNm = StringUtil.nvl(userInfoMap.get("depart_nm"));
		String prevLoginDate = StringUtil.nvl(userInfoMap.get("prev_login_date"));
		String prevLinkIp = StringUtil.nvl(userInfoMap.get("prev_link_ip"));
		String honorsPwd = StringUtil.nvl(userInfoMap.get("HONORS_PWD"));
		String odtbrCode = StringUtil.nvl(userInfoMap.get("ODTBR_CODE"));
		String nHnet = StringUtil.nvl(userInfoMap.get("MEMBER_GROUP_DIV_CD"));

		if(!"Y".equals(validYn)) {
			//			throw new ProviderNotFoundException("유효하지 않은 사용자 입니다.");
			throw new ProviderNotFoundException("로그인 정보가 잘못되었습니다.");
		}

		String memberDivCd = userInfoMap.get("member_div_cd");
		String memberDivNm = userInfoMap.get("member_div_nm");

		String userRole = CommonConstants.getUserRole(memberDivCd);
		List<GrantedAuthority> gas = new ArrayList<>();
		gas.add(new SimpleGrantedAuthority(userRole));

		MemberVO memberVO = new MemberVO();
		memberVO.setMemberUid(memberUid);
		memberVO.setMemberDivCd(memberDivCd);
		memberVO.setMemberDivNm(memberDivNm);
		memberVO.setId(userId);
		memberVO.setName(userNm);
		memberVO.setPwd(userPwd);
		memberVO.setCnsPossibleYn(cnspossibleYn);
		memberVO.setValidYn(validYn);
		memberVO.setUpperMemberUid(upperMemberUid);
		memberVO.setUpperMemberNm(upperMemberNm);
		memberVO.setDepartCd(departCd);
		memberVO.setDepartNm(departNm);
		memberVO.setPrevLoginDate(prevLoginDate);
		memberVO.setPrevLinkIp(prevLinkIp);
		memberVO.setHonorsPwd(honorsPwd);
		memberVO.setOdtbrCode(odtbrCode);
		memberVO.setNHnet(nHnet);
		return memberVO;
	}

	public static MemberVO getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof MemberDetails) {
			log.debug("FOUND MemberDetails: {}", authentication);
			return ((MemberDetails) authentication).getMemberVO();
		} else {
			log.debug("NOT FOUND MemberDetails: {}", authentication);
			return null;
		}
	}

	public static void setCurrentUser(MemberVO memberVO) {
		((MemberDetails) SecurityContextHolder.getContext().getAuthentication()).setMemberVO(memberVO);
	}
}
