package ht.controller;

import static ht.constants.CommonConstants.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.service.MemberAuthService;
import ht.service.ChatManageService;
import ht.service.CommonService;
import ht.service.CustomerService;
import ht.service.EndCtgService;
import ht.service.ChatService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 채팅 관리 controller
 * 상담 관리
 * 봇상담
 * 봇상담 종료
 * 상담직원 상담 대기
 * 상담 진행중
 * 읽지않은 대화방
 * 상담 종료
 * 상담직원 변경 요청
 * 상담내용 검토 요청
 *
 * @author wizard
 *
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(path = "/chatManage")
@Slf4j
public class ChatManageController {

	@Resource
	private CustomProperty customProperty;

	@Resource
	private CommonService commonService;
	@Resource
	private ChatManageService chatManageService;
	@Resource
	private HTUtils htUtils;
	@Resource
	private CustomerService customerService;
	@Resource
	private ChatService chatService;
	@Resource
	private EndCtgService endCtgService;

	/**
	 * 채팅방 상태별 목록 조회 페이지
	 */
	@RequestMapping(value = "/selectStatusRoomList", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectChatStatusList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		StopWatch stopWatch = new StopWatch("selectStatusRoomList");
		stopWatch.start("selectStatusRoomList");
		log.debug("selectStatusRoomList: {}", param);

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		String schText = StringUtil.nvl(param.get("schText"));
		schText = StringEscapeUtils.escapeHtml4(schText);
		model.put("schText", schText);
		param.put("schText", schText);


		model = this.selectChatRoomList(model, param);
		model.put("schRoomStatus", param.get("schRoomStatus"));
		model.put("schRoomStatus", param.get("schRoomStatus").toString());
		stopWatch.stop();
		System.out.println(stopWatch.shortSummary());

		log.info("REQUEST URI: {}, TIMESTAMP: {}", request.getRequestURI(), System.currentTimeMillis());

		return "chatManage/selectChatStatusList";
	}

	/**
	 * 채팅방 상태별 목록 조회 (Ajax 요청)
	 */
	@RequestMapping(value = "/selectRoomListAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectRoomListAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		String schText = StringUtil.nvl(param.get("schText"));
		schText = StringEscapeUtils.escapeHtml4(schText);
		model.put("schText", schText);
		param.put("schText", schText);

		model = this.selectChatRoomList(model, param);
		model.put("schRoomStatus", param.get("schRoomStatus"));

		return "chatManage/chatRoomListLayer";
	}

	/**
	 * 채팅방 상태별 목록 조회 (Ajax 요청)
	 */
	@RequestMapping(value = "/selectRoomListMoreAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public String selectRoomListMoreAjax(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		model = this.selectChatRoomList(model, param);
		model.put("schRoomStatus", param.get("schRoomStatus"));

		return "chatManage/chatRoomListMoreLayer";
	}

	/**
	 * 채팅방 상태별 목록 조회
	 */
	private ModelMap selectChatRoomList(ModelMap model, Map<String, Object> param) {

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			// 파라미터 셋팅
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("sessionMemberDivCd", memberVO.getMemberDivCd());
			String departCd = memberVO.getDepartCd();
			if("DC".equals(departCd.toString()) || "M1".equals(departCd.toString()) || "M2".equals(departCd.toString()) ) {
				param.put("dateSrch" , "N");
			}
			// 매니저인 경우, 소속된 상담직원만 (매니저 아이디로 조회)
			if (MEMBER_DIV_CD_M.equals(memberVO.getMemberDivCd())) {
				param.put("managerUid", memberVO.getMemberUid());
			}
			// 슈퍼유저, 관리자인 경우 부서 전체 (부서 코드로 조회)
			else {
				//param.put("departCd", memberVO.getDepartCd());
			}

			// 조회 페이지
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			int pageListCount = CommonConstants.PAGE_CHAT_LIST_COUNT;
			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);

			// 채팅방 상태 (좌측 메뉴: botIng, botEnd, cnsrWait, cnsrIng, cnstNotJoin, cnsrEnd, cnsrChange, contReview)
			String schRoomStatus = StringUtil.nvl(param.get("schRoomStatus"), "botIng");
			param.put("schRoomStatus", schRoomStatus);

			// 봇상담, 봇종료 페이지인 경우
			// 봇 아이디로 조회
			if ("botIng".equals(schRoomStatus) || "botEnd".equals(schRoomStatus)) {
				param.put("botMemberUidList", Arrays.asList(customProperty.getHappyBotMemeberUid(), customProperty.getCategoryBotMemeberUid()));
			}

			// 조회 일자
			String schDate = StringUtil.nvl(param.get("schDate"));
			param.put("schDate", schDate);

			// 조회 기간: toDay, yestDay, oneWeek, thisMonth, oneMonthAgo, twoMonthAgo
			String schTerm = StringUtil.nvl(param.get("schTerm"), "toDay");
			param.put("schTerm", schTerm);

			// 검색 기준: cont, title, roomId, counselor, cstmId, cstmName, endDate
			String schType = StringUtil.nvl(param.get("schType"), "cont");
			param.put("schType", schType);

			// 정렬 기준: lastChat, name, title, chatTime, createTime
			String schSortColumn = StringUtil.nvl(param.get("schSortColumn"), "lastChat");
			param.put("schSortColumn", schSortColumn);

			// 정렬 방향
			String schSortType = StringUtil.nvl(param.get("schSortType"), "asc");
			param.put("schSortType", schSortType);


			log.info("****************************** : {}" , param);
			// 채팅방 상태별 목록 조회
			List<Map<String, Object>> chatRoomList = chatManageService.selectStatusRoomList(param);
			model.put("chatRoomList", chatRoomList);

			// 조회값 화면으로 셋팅
			model.put("nowPage", nowPage);
			model.put("schRoomStatus", schRoomStatus);
			model.put("schDate", schDate);
			model.put("schTerm", schTerm);
			model.put("schType", schType);
			model.put("schSortColumn", schSortColumn);
			model.put("schSortType", schSortType);

			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("schDateType", "MONTH");
			param1.put("schAdd", 0);
			model.put("nowMonth", commonService.selectCustomDate(param1));

			param1.put("schAdd", -1);
			model.put("onwMonthAgo", commonService.selectCustomDate(param1));

			param1.put("schAdd", -2);
			model.put("twoMonthAgo", commonService.selectCustomDate(param1));

			model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return model;
	}

	/**
	 * 상담내역 후처리 윈도우 팝업 페이지
	 */
	@GetMapping("/endReviewPopup")
	public String getEndReviewPopup(String jSessionId, HttpServletResponse response, HttpSession session,
			Model model, @RequestParam Map<String, Object> param) {

		log.info("getEndReview: {}", param);

		// 고객 정보
		Map<String, Object> customer = customerService.selectCustomerByCstmUid(param.get("cstmUid").toString());
		model.addAttribute("customer", customer);

		String chatRoomUid = param.get("chatRoomUid").toString();
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		param.put("departCd", memberVO.getDepartCd());

		// 기존 정보 있으면 조회
		// IPCC_MCH 기존에 가져오는 부분 잘못되었음.
		Map<String, Object> chatEndInfo = chatService.selectChatEndInfoPop(chatRoomUid);
		model.addAttribute("chatRoomUid", param.get("chatRoomUid"));
		model.addAttribute("cstmUid", param.get("cstmUid"));
		model.addAttribute("chatEndInfo", chatEndInfo);
		model.addAttribute("endCtg1", endCtgService.selectCategoryTopList());
		if(!StringUtil.isEmpty(chatEndInfo.get("DEP_1_CTG_NUM"))) { // 후처리시 무족건 1,2 분류 체크
			model.addAttribute("endReviewMode", "W");
			model.addAttribute("endCtg2", endCtgService.selectCategoryList(Integer.parseInt(chatEndInfo.get("DEP_2_CTG_NUM").toString())));
			if(!StringUtil.isEmpty(chatEndInfo.get("DEP_3_CTG_NUM"))) { // 예전정보 조회시
			model.addAttribute("endCtg3", endCtgService.selectCategoryList(Integer.parseInt(chatEndInfo.get("DEP_3_CTG_NUM").toString())));
			}
		}
		log.info("topCtgList: {}",  endCtgService.selectCategoryTopList());
		return "counselor/restEndReviewPopup";
	}
}
