package ht.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.domain.NoticeVO;
import ht.service.MemberAuthService;
import ht.service.NoticeService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 공지사항 관련 controller
 * 공지사항 조회
 *
 * @author wizard
 *
 */
@Controller
@RequestMapping(path = "/notice")
@Slf4j
public class NoticeController {

	@Resource
	private NoticeService noticeService;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private HTUtils htUtils;

	/**
	 * 공지사항 조회
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/noticeList", method = { RequestMethod.GET, RequestMethod.POST })
	public String noticeList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			Locale locale, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		/** 테스트
		String xxx = messageSource.getMessage("greeting", null, "default text", locale);
		String yyy = messageSource.getMessage("greeting2", new String[] {"first"}, "default text", locale);
		테스트 e**/
		log.debug("NoticeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		log.debug("noticeDivCd : {}", param.get("noticeDivCd"));
		log.debug("noticeDivCdPageVal : {}", param.get("noticeDivCdPageVal"));
		log.debug("searchVal : {}", param.get("searchVal"));

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			model.put("member", memberVO);

			log.debug("memberDivCd : {}", memberVO.getMemberDivCd());

			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("memberDivCd", memberVO.getMemberDivCd());
			param.put("upperMemberUid", memberVO.getUpperMemberUid());
			param.put("noticeDivCd", param.get("noticeDivCdPageVal"));

			// 페이징 정보
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			int pageListCount = CommonConstants.PAGE_LIST_COUNT;
			int totPage = 1;
			int totCount = 0;

			if (param.get("noticeDivCd") == "") {
				param.put("noticeDivCd", null);

			}

			if (param.get("searchVal") == "") {
				param.put("searchVal", null);

			}

			param.put("nowPage", nowPage);
			param.put("pageListCount", pageListCount);
			param.put("CD_GROUP", CommonConstants.COMM_CD_NOTICE_DIV_CD);

			// 공지사항 조회
			List<Map<String, Object>> noticeList = noticeService.selectNoticeList(param);

			// 공지사항 분류 조회
			List<Map<String, Object>> noticeDivList = noticeService.selectNoticeDivList(param);

			JSONObject jsonObj = new JSONObject();
			// jsonObj.putAll((Map)noticeDivList);

			log.debug("jsonObj : {}", jsonObj.toString());
			// JSONParser parser = new J

			if (noticeList != null && noticeList.size() > 0) {
				Map<String, Object> map = noticeList.get(0);
				totCount = ((BigDecimal) map.get("tot_count")).intValue();
				totPage = ((totCount - 1) / pageListCount) + 1;
			}
			if (noticeList != null && noticeList.size() > 0) {
				model.put("noticeList", noticeList);
			}
			if (noticeDivList != null && noticeDivList.size() > 0) {
				model.put("noticeDivList", noticeDivList);
			}

			model.put("noticeDivCd", param.get("noticeDivCdPageVal"));
			model.put("nowPage", nowPage);
			model.put("totPage", totPage);
			model.put("totCount", totCount);
			model.put("searchVal", param.get("searchVal"));

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "notice/noticeList";
	}

	/**
	 * 공지사항 등록화면
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/addNotice", method = { RequestMethod.GET, RequestMethod.POST })
	public String addNotice(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("NoticeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			// 공지사항 분류 조회
			param.put("CD_GROUP", CommonConstants.COMM_CD_NOTICE_DIV_CD);
			param.put("memberUid", memberVO.getMemberUid());

			List<Map<String, Object>> noticeDivList = noticeService.selectNoticeDivList(param);

			model.put("noticeDivList", noticeDivList);
			model.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("memberDivCd", memberVO.getMemberDivCd());
			model.put("upperMemberUid", memberVO.getUpperMemberUid());

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "notice/addNotice";
	}

	/**
	 * 공지사항 상세 조회
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/viewNotice", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewNotice(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("NoticeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		log.debug("notice_num : {}", param.get("notice_num"));
		log.debug("nowPage : {}", param.get("nowPage"));

		log.debug("delete_searchVal : {}", param.get("searchVal"));

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			// 공지사항 상세조회
			if (param.get("notice_num") != null) {
				NoticeVO noticeDetail = noticeService.selectNoticeDetail(param);
				model.put("noticeDetail", noticeDetail);
			}

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			log.debug("getMemberDivCd : {}", memberVO.getMemberDivCd());
			model.put("nowPage", nowPage);
			model.put("searchVal", param.get("searchVal"));
			model.put("noticeDivCd", param.get("noticeDivCd"));
			model.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("sessionMemberDivCd", memberVO.getMemberDivCd());

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "notice/viewNotice";
	}

	/**
	 * 공지사항 삭제
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/deleteNotice", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deleteNotice(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("NoticeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		log.debug("notice_num : {}", param.get("notice_num"));

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			log.debug("notice_num : {}", param.get("notice_num"));
			log.debug("sessionMemberUid : {}", param.get("sessionMemberUid"));
			// 공지사항 삭제
			if (param.get("notice_num") != null) {
				noticeService.deleteNotice(param);
			}

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");

			model.put("nowPage", nowPage);
			model.put("searchVal", param.get("searchVal"));
			model.put("noticeDivCd", param.get("noticeDivCd"));
			model.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("sessionMemberDivCd", memberVO.getMemberDivCd());

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		RedirectView rv = new RedirectView("noticeList");
		rv.setExposeModelAttributes(true);
		ModelAndView mv = new ModelAndView(rv);
		mv.addAllObjects(model);
		return mv;
	}

	/**
	 * 공지사항 저장
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/saveNotice", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView saveNotice(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("NoticeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			log.debug("notice_num : {}", param.get("notice_num"));
			log.debug("title : {}", param.get("title"));
			log.debug("cont : {}", param.get("cont"));
			log.debug("noticeDivVal : {}", param.get("noticeDivVal"));

			// 공지사항 등록

			noticeService.insertNotice(param);

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");

			model.put("nowPage", nowPage);
			model.put("searchVal", param.get("searchVal"));
			model.put("noticeDivCd", param.get("noticeDivCd"));
			model.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("sessionMemberDivCd", memberVO.getMemberDivCd());
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}

		RedirectView rv = new RedirectView("noticeList");
		rv.setExposeModelAttributes(false);
		ModelAndView mv = new ModelAndView(rv);
		// mv.addAllObjects(model);
		return mv;
	}

	/**
	 * 공지사항 수정화면
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/editNotice", method = { RequestMethod.GET, RequestMethod.POST })
	public String editNotice(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("NoticeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		log.debug("notice_num : {}", param.get("notice_num"));
		log.debug("nowPage : {}", param.get("nowPage"));

		log.debug("delete_searchVal : {}", param.get("searchVal"));

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			// 공지사항 상세조회
			if (param.get("notice_num") != null) {
				NoticeVO noticeDetail = noticeService.selectNoticeDetail(param);
				model.put("noticeDetail", noticeDetail);
			}
			// 공지사항 분류 조회
			param.put("CD_GROUP", CommonConstants.COMM_CD_NOTICE_DIV_CD);
			List<Map<String, Object>> noticeDivList = noticeService.selectNoticeDivList(param);
			model.put("noticeDivList", noticeDivList);
			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");
			log.debug("getMemberDivCd : {}", memberVO.getMemberDivCd());
			model.put("notice_num", param.get("notice_num"));
			model.put("nowPage", nowPage);
			model.put("searchVal", param.get("searchVal"));
			model.put("noticeDivCd", param.get("noticeDivCd"));
			model.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("sessionMemberDivCd", memberVO.getMemberDivCd());

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "notice/editNotice";
	}

	/**
	 * 공지사항 수정저장
	 *
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/modifyNotice", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView modifyNotice(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {

		log.debug("NoticeController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			param.put("sessionMemberUid", memberVO.getMemberUid());
			log.debug("notice_num : {}", param.get("notice_num"));
			log.debug("title : {}", param.get("title"));
			log.debug("cont : {}", param.get("cont"));
			log.debug("noticeDivVal : {}", param.get("noticeDivVal"));

			// 공지사항 등록

			noticeService.updateNotice(param);

			String nowPage = StringUtil.nvl(param.get("nowPage"), "1");

			model.put("nowPage", nowPage);
			model.put("searchVal", param.get("searchVal"));
			model.put("noticeDivCd", param.get("noticeDivCd"));
			model.put("sessionMemberUid", memberVO.getMemberUid());
			model.put("sessionMemberDivCd", memberVO.getMemberDivCd());
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		RedirectView rv = new RedirectView("noticeList");
		rv.setExposeModelAttributes(false);
		ModelAndView mv = new ModelAndView(rv);
		// mv.addAllObjects(model);
		return mv;
	}

}
