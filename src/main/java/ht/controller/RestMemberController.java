package ht.controller;

import static ht.constants.CommonConstants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.exception.BizException;
import ht.service.CommonService;
import ht.service.CustomerService;
import ht.service.MemberAuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import ht.service.MemberService;
import ht.service.SettingService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RestMemberController {

	@Resource
	private MemberService memberService;
	@Resource
	private CustomerService customerService;
	@Resource
	private SettingService settingService;
	@Resource
	private CommonService commonService;

	@GetMapping(path = "/api/member/{memberUid}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getMember(@PathVariable("memberUid") String memberUid) {

		log.debug("GET MEMBER INFO: {}", memberUid);

		Map<String, Object> member = memberService.selectMemberByMemberUid(memberUid);
		if (member != null) {
			return new ResponseEntity<>(member, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = "/api/member/available")
	public String getGroup(@RequestParam(value = "memberUid") String memberUid,
			@RequestParam(value = "rollType") String rollType,
			@RequestParam(name = "managerFlag", required = false) String managerFlag,
			HttpSession session,
			Model model) throws BizException {

		log.debug("GET AVAILABLE MEMBER LIST: ROLL TYPE: {}", rollType);

		// 매니저 페이지이면 메모 영역 불필요
		boolean isManagerPage = MEMBER_DIV_CD_M.equals(rollType);
		log.debug("isManagerPage: {}, rollType: {}", isManagerPage, rollType);

		// 자동 변경 승인 여부 설정
		Map<String, Object> siteSetting = settingService.selectSiteSetting();
		boolean isAutoPermitForRequestChangeCounselor = settingService
				.isAutoPermitForRequestChangeCounselor(siteSetting);
		model.addAttribute("isAutoPermitForRequestChangeCounselor", isAutoPermitForRequestChangeCounselor);

		List<Map<String, Object>> mapList =  new ArrayList<>();
		
		List<Map<String, Object>> availableMemberList = new ArrayList<>();
		
		// 자동 변경 승인 허용일 경우 상담직원 목록 추가
		// 매니저 페이지인 경우 상담직원 목록 추가
		if (isAutoPermitForRequestChangeCounselor || isManagerPage) {

			// 상대 부서 정보
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			if(memberVO == null) {
				model.addAttribute("isMemberVo", true);
			}else {
				model.addAttribute("isMemberVo", false);
				Map<String, Object> counterpartDepart = commonService.getCounterPartDepartCode(memberVO.getDepartCd(), commonService.selectCodeList(CommonConstants.COMM_CD_DEPART_CD));
				model.addAttribute("counterPartDepartCd", counterpartDepart.get("cd"));
				model.addAttribute("counterPartDepartNm", counterpartDepart.get("cd_nm"));
				Assert.notNull(counterpartDepart, "NO COUNTERPART DEPART");

				model.addAttribute("availableMemberList", availableMemberList);
	
				// 상담 가능한 상담직원
				mapList = memberService.selectAvailableCounselorList(memberVO.getDepartCd());
			}
			
			if (mapList.isEmpty()) {
				if (isManagerPage) {
					return "chatManage/restAvailable";
				} else {
					return "counselor/restAvailable";
				}
			}

			// 기타 정보 (상담중인 채팅방 개수, 대기중인 채팅방 개수)
			List<Map<String, Object>> mapListExtra = memberService.selectMemberUidAndAssignCount(mapList);
			for (Map<String, Object> map : mapListExtra) {
				if (isManagerPage // 매니저 페이지
						|| !memberUid.equals(map.get("member_uid"))) { // 상담직원 페에지에서 자신은 제외
					availableMemberList.add(map);
				}
			}

			assert (mapList.size() == mapListExtra.size());
		}

		if (isManagerPage) {
			return "chatManage/restAvailable";
		} else {
			if (managerFlag == null) {
				return "counselor/restAvailable";
			}else {
				return "counselor/restAvailableManager";
			}

		}
	}

	@GetMapping(path = "/api/customer/{cstmUid}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getCustomer(@PathVariable("cstmUid") String cstmUid) {

		log.debug("GET CUSTOMER INFO: {}", cstmUid);

		Map<String, Object> customer = customerService.selectCustomerByCstmUid(cstmUid);
		if (customer != null) {
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.NOT_FOUND);
		}
	}
}
