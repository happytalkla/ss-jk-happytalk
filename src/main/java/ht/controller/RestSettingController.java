package ht.controller;

import static ht.constants.CommonConstants.WORK_STATUS_CD_W;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.domain.NoticeMessage;
import ht.domain.NoticeMessage.NoticeMessageType;
import ht.service.BatchService;
import ht.service.ChatRoomService;
import ht.service.CommonService;
import ht.service.MemberAuthService;
import ht.service.MemberService;
import ht.service.SettingService;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RestSettingController {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private SimpMessageSendingOperations messagingTemplate;
	@Resource
	private SettingService settingService;
	@Resource
	private CommonService commonService;
	@Resource
	private MemberService memberService;
	@Resource
	private BatchService batchService;
	@Resource
	private ChatRoomService chatRoomService;

	/**
	 * 사이트 세팅
	 */
	@GetMapping(path = "/api/setting")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getSetting() {

		log.debug("GET SETTING");

		Map<String, Object> setting = settingService.selectSiteSetting();

		if (setting != null) {
			return new ResponseEntity<>(setting, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * 사이트 및 사용자 세팅
	 */
	@GetMapping(path = "/api/setting/{memberUid}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getSetting(@PathVariable(value = "memberUid") String memberUid) {

		Map<String, Object> setting = getSiteAndMemeberSetting(memberUid);
		if (setting == null) {
			return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(setting, HttpStatus.OK);
		}
	}
	
	@PostMapping(path = "/api/logoutCheck/{memberUid}")
	@ResponseBody
	public ResponseEntity<String> logoutCheck(@PathVariable(value = "memberUid") String memberUid,
			@RequestParam Map<String, Object> requestParams) {
		//상담후처리 미입력 여부 확인
		int cnt = memberService.selectChatRoomEndMemoEmptyCount(memberUid);
		if(cnt > 0) {
			return new ResponseEntity<>("완료되지 않은 상담이 있습니다. 상담내용을 확인 해 주세요.", HttpStatus.OK);
		}
		return new ResponseEntity<>("OK", HttpStatus.OK);
		
	}
			

	/**
	 * 사용자 세팅 업데이트
	 */
	@PostMapping(path = "/api/setting/{memberUid}")
	@ResponseBody
	public ResponseEntity<String> setSetting(@PathVariable(value = "memberUid") String memberUid,
			@RequestParam Map<String, Object> requestParams) {

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		if (memberVO == null) {
			return new ResponseEntity<>("세션이 만료되었습니다. 다시 로그인 해주세요.", HttpStatus.NOT_ACCEPTABLE);
		}
		

		requestParams.put("memberUid", memberUid);
		int d = 0;

		// 상담직원 휴식 설정 로그 저장
		try {
			d = memberService.saveMemberSetting(memberUid, requestParams);

			String name = memberVO.getName();
			String id = memberVO.getId();
			if (requestParams.get("workStatusCd") != null) {
				String logCont = "상담직원 휴식 설정 : ";
				if (WORK_STATUS_CD_W.equals(requestParams.get("workStatusCd"))) {
					logCont += "상담가능";
				} else {
					logCont += "상담불가";
				}
				logCont += " - " + name + "(" + id + ")";
				commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberUid);
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			String name = memberVO.getName();
			String id = memberVO.getId();
			if (requestParams.get("workStatusCd") != null) {
				String logCont = "상담직원 휴식 설정 : ";
				if (WORK_STATUS_CD_W.equals(requestParams.get("workStatusCd"))) {
					logCont += "상담가능";
				} else {
					logCont += "상담불가";
				}
				logCont += " - " + name + "(" + id + ")";
				commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberUid);
			}
		}

		if (d == 1) {
			NoticeMessage message = new NoticeMessage(NoticeMessageType.SETTING);
			message.setSetting(getSiteAndMemeberSetting(memberUid));
			messagingTemplate.convertAndSend(customProperty.getWsQueuePath() + "/" + memberUid, message);
			return new ResponseEntity<>("OK", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("NOT_MODIFIED", HttpStatus.NOT_MODIFIED);
		}
	}

	private Map<String, Object> getSiteAndMemeberSetting(String memberUid) {

		Map<String, Object> siteSetting = settingService.selectSiteSetting();
		if (siteSetting == null) {
			return null;
		}

		Map<String, Object> memberSetting = memberService.selectMemberByMemberUid(memberUid);
		siteSetting.put("member_work_status_cd", memberSetting.get("work_status_cd"));
		siteSetting.put("member_work_status_cd_by_m", memberSetting.get("work_status_cd_by_m"));
		siteSetting.put("member_enter_use_yn", memberSetting.get("enter_use_yn"));
		siteSetting.put("member_avatar_num", memberSetting.get("avatar_num"));

		return siteSetting;
	}

	/**
	 * 금지어 목록
	 */
	@GetMapping(path = "/api/forbidden")
	@ResponseBody
	public ResponseEntity<List<String>> getForbidden() {

		List<Map<String, Object>> mapList = settingService.selectForbiddenList();
		if (mapList == null) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
		}

		List<String> forbiddenList = new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			forbiddenList.add((String) map.get("forbidden"));
		}

		return new ResponseEntity<>(forbiddenList, HttpStatus.OK);
	}

	/**
	 * 금지어 대체 메세지
	 */
	@GetMapping(path = "/api/forbidden/alternative")
	@ResponseBody
	public ResponseEntity<String> getForbiddenAlternative() {

		Map<String, Object> siteSetting = settingService.selectSiteSetting();
		if (Strings.isNullOrEmpty((String) siteSetting.get("cstm_proh_msg"))) {
			return new ResponseEntity<>("&nbsp;", HttpStatus.OK);
		}

		return new ResponseEntity<>((String) siteSetting.get("cstm_proh_msg"), HttpStatus.OK);
	}

	/**
	 * 하이라이트 단어 목록
	 */
	@GetMapping(path = "/api/highlight/{memberUid}")
	@ResponseBody
	public ResponseEntity<List<String>> getHighlight(@PathVariable(value = "memberUid") String memberUid) {

		List<Map<String, Object>> mapList = settingService.selectHighLightList(memberUid);
		if (mapList == null) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
		}

		List<String> highlightList = new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			highlightList.add(""+ map.get("kwd_nm"));
		}

		return new ResponseEntity<>(highlightList, HttpStatus.OK);
	}
}
