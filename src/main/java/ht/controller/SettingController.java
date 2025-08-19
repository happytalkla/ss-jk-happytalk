package ht.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.domain.NoticeMessage;
import ht.domain.NoticeMessage.NoticeMessageType;
import ht.service.CommonService;
import ht.service.MemberAuthService;
import ht.service.SettingService;
import ht.util.HTUtils;
import ht.util.LogUtil;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 셋팅 관련 controller
 * 기본 조건 설정
 * 자동 메세지 관리
 * 욕 필터 관리
 *
 * @author wizard
 *
 */
@Controller
@RequestMapping(path = "/set")
@Slf4j
public class SettingController {

	@Resource
	private SettingService settingService;
	@Resource
	private CommonService commonService;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private HTUtils htUtils;
	@Resource
	private SimpMessageSendingOperations messagingTemplate;

	/**
	 * 기본 설정 정보 조회
	 */
	@RequestMapping(value = "/selectSet", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectSet(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			/*
			 * MemberVO memberVO = MemberAuthService.getCurrentUser(); param.put("departCd",
			 * memberVO.getDepartCd());
			 */

			// 기본 설정 정보 조회
			Map<String, Object> defaultSet = settingService.selectDefaultSet();
			model.put("defaultSet", defaultSet);

			// 근무시간 설정 정보 조회
			param.put("siteId", defaultSet.get("site_id").toString()) ;
			param.put("channel", CommonConstants.CSTM_LINK_DIV_CD_A) ;
			Map<String, Object> workTime = settingService.selectWorkTime(param);
			model.put("workTime", workTime);

			// 근무시간 설정 정보 조회 카카오
			param.put("channel", CommonConstants.CSTM_LINK_DIV_CD_B) ;
			Map<String, Object> workTimeKK = settingService.selectWorkTime(param);
			model.put("workTimeKK", workTimeKK);

			// 근무시간 설정 정보 조회 o2
			param.put("channel", CommonConstants.CSTM_LINK_DIV_CD_C) ;
			Map<String, Object> workTimeO2 = settingService.selectWorkTime(param);
			model.put("workTimeO2", workTimeO2);
			log.info("workTimeO2 ========================= " + workTimeO2);
			// 근무시간 설정 정보 조회 mpop
			param.put("channel", CommonConstants.CSTM_LINK_DIV_CD_D) ;
			Map<String, Object> workTimeMP = settingService.selectWorkTime(param);
			model.put("workTimeMP", workTimeMP);

			/*
			 * IPCC_MCH ARS 채널 추가
			 */
			// 근무시간 설정 정보 조회 ARS
			param.put("channel", CommonConstants.CSTM_LINK_DIV_CD_E) ;
			Map<String, Object> workTimeArs = settingService.selectWorkTime(param);
			model.put("workTimeArs", workTimeArs);

			// 채팅 경과시간 목록
			List<Map<String, Object>> chatPassTimeList = settingService.selectChatPassTimeList();
			model.put("chatPassTimeList", chatPassTimeList);

			// }catch(BizException be){
			// //be.printStackTrace();
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);

		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "set/defaultSet";
	}

	/**
	 * 기본 설정 변경
	 */
	@RequestMapping(value = "/updateSet", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateSet(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model, @RequestParam Map<String, Object> param,
			@RequestParam MultiValueMap<String, String> lparam) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());
		log.info("================= : " + param);
		Map<String, Object> rtnMap = new HashMap<>();
		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			
			List<String> markNumList = StringUtil.nvlList(lparam.get("markNum"));
			param.put("markNumList", markNumList);

			settingService.updateSet(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

			// 전역 세팅
			Map<String, Object> setting = settingService.selectSiteSetting();
			NoticeMessage noticeMessage = new NoticeMessage();
			noticeMessage.setType(NoticeMessageType.SETTING);
			noticeMessage.setSetting(setting);

			// 메세지 발행
			messagingTemplate.convertAndSend(customProperty.getWsNoticePath(), noticeMessage);

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "updateSet");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "성공", logCont, memberVO.getMemberUid());
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getMessage());
			//			//be.printStackTrace();
			//
			//			// 로그 정보 등록
			//			String logCont = LogUtil.getCont(param, "updateSet");
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "updateSet");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 메세지 설정 정보 조회
	 */
	@RequestMapping(value = "/selectMessage", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectMessage(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param, String channel) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());
		//String cnsFrtMsgImgUrl = "";
		//String path ="";
		//String orgFileNm ="";
		try {
			// 기본 설정 정보 조회
			Map<String, Object> defaultSet = settingService.selectDefaultSet();
			model.put("defaultSet", defaultSet);
			param.put("siteId", defaultSet.get("site_id").toString());
			param.put("channel", channel.toString());

			///채널 명칭 가져오기 
			List<Map<String, Object>> channelList = settingService.selectChannelList(param);
			model.put("channelList", channelList);
			
			// 메세지 설정 정보 조회
			
			Map<String, Object> messageSet = settingService.selectMessage(param);
			model.put("messageSet", messageSet);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "set/selectMessage";
	}

	/**
	 * 메세지 설정 변경
	 */
	@RequestMapping(value = "/updateMessage", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateMessage(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			//param.put("departCd", memberVO.getDepartCd());
			param.put("firstMsgTextUseYn",param.get("cnsFrtMsgTextUseYn"));
			settingService.updateMessage(param);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "updateMessage");

			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "성공", logCont, memberVO.getMemberUid());

		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "updateMessage");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "실패", logCont, memberVO.getMemberUid());
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return rtnMap;
	}

	/**
	 * 해피봇 설정 정보 조회
	 */
	@RequestMapping(value = "/selectHappyBot", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectHappyBot(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());
		try {
			//			param.put("departCd", memberVO.getDepartCd());
			// 메세지 설정 정보 조회
			Map<String, Object> messageSet = settingService.selectMessage(param);
			List<Map<String, Object>> happyBotSetList = settingService.selectHappyBot(param);
			List<Map<String, Object>> botBlockList = settingService.selectBotBlockList(param);

			model.put("messageSet", messageSet);
			model.put("happyBotSetList", happyBotSetList);
			model.put("botBlockList", botBlockList);

			// }catch(BizException be){
			// //be.printStackTrace();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "set/selectHappyBot";
	}

	/**
	 * 해피봇 - block 리스트 가져오기
	 */
	@RequestMapping(value = "/selectBotBlockList", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> selectBotBlockList(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			List<Map<String, Object>> botBlockList = settingService.selectBotBlockList(param);
			rtnMap.put("botBlockList", botBlockList);
		} catch (Exception e) {

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "updateHappyBot");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}



	/**
	 * 해피봇 설정 변경
	 */
	@RequestMapping(value = "/updateHappyBot", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> updateHappyBot(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param,
			@RequestParam(value="nameArr", required =false) String[] nameArr,
			@RequestParam(value="firstBlockIdArr", required =false) String[] firstBlockIdArr,
			@RequestParam(value="modifiedArr", required =false) String[] modifiedArr,
			@RequestParam(value="useYnArr", required =false) String[] useYnArr) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("siteId", customProperty.getSiteId());
			//			param.put("departCd", memberVO.getDepartCd());
			if (param.get("selBtn").equals("scenario")) {
				settingService.updateHappyBot(param, nameArr, modifiedArr, useYnArr, firstBlockIdArr);

			}else {
				settingService.updateMessage(param);
			}

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 수정 되었습니다.");

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "updateHappyBot");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "성공", logCont, memberVO.getMemberUid());
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getHappyBot());
			//			//be.printStackTrace();
			//
			//			// 로그 정보 등록
			//			String logCont = LogUtil.getCont(param, "updateHappyBot");
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = LogUtil.getCont(param, "updateHappyBot");
			commonService.insertLog(CommonConstants.LOG_DIV_CD_B, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 욕필터 관리 페이지 조회
	 */
	@RequestMapping(value = "/selectForbidden", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectForbidden(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 기본 설정 정보 조회
			List<Map<String, Object>> forbiddenList = settingService.selectForbiddenList();
			model.put("forbiddenList", forbiddenList);

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "set/forbiddenManage";
	}

	/**
	 * 욕필터 등록
	 */
	@RequestMapping(value = "/insertForbidden", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> insertForbidden(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			String forbidden = String.valueOf(param.get("forbidden"));
			//공백입력 확인
			if (!forbidden.trim().equals("")) {
				param.put("forbidden", forbidden);
				// 중복 금지어 확인
				List<Map<String, Object>> forbiddenList = settingService.selectForbiddenDupList(param);
				if(forbiddenList.size() == 0) {
					settingService.insertForbidden(param);

					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
					rtnMap.put("rtnMsg", "정상적으로 등록 되었습니다.");
				}
				else {
					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
					rtnMap.put("rtnMsg", "이미 등록된 금지어 입니다.");
				}

				// 로그 정보 등록
				String logCont = "욕필터 등록 : " + StringUtil.nvl(param.get("forbidden"));
				commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
				//		} catch (BizException be) {
				//			rtnMap.put("rtnCd", be.getErrCode());
				//			rtnMap.put("rtnMsg", be.getMessage());
				//			//be.printStackTrace();
				//
				//			// 로그 정보 등록
				//			String logCont = "욕필터 등록 : " + StringUtil.nvl(param.get("forbidden"));
				//			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
			} else {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
				rtnMap.put("rtnMsg", "금지어로 지정할 키워드를 입력해 주세요.");
			}
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "욕필터 등록 : " + StringUtil.nvl(param.get("forbidden"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

	/**
	 * 욕필터 삭제
	 */
	@RequestMapping(value = "/deleteForbidden", method = { RequestMethod.POST })
	public @ResponseBody Map<String, Object> deleteForbidden(
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		// 세션 사용자 정보 조회
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		try {
			param.put("sessionMemberUid", memberVO.getMemberUid());
			settingService.deleteForbidden(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 삭제 되었습니다.");

			// 로그 정보 등록
			String logCont = "욕필터 삭제 : " + StringUtil.nvl(param.get("forbidden"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "성공", logCont, memberVO.getMemberUid());
			//		} catch (BizException be) {
			//			rtnMap.put("rtnCd", be.getErrCode());
			//			rtnMap.put("rtnMsg", be.getMessage());
			//			//be.printStackTrace();
			//
			//			// 로그 정보 등록
			//			String logCont = "욕필터 삭제 : " + StringUtil.nvl(param.get("forbidden"));
			//			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);

			// 로그 정보 등록
			String logCont = "욕필터 삭제 : " + StringUtil.nvl(param.get("forbidden"));
			commonService.insertLog(CommonConstants.LOG_DIV_CD_C, "실패", logCont, memberVO.getMemberUid());
		}

		return rtnMap;
	}

}
