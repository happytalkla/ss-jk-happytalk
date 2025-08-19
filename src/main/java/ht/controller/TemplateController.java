package ht.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.exception.BizException;
import ht.service.MemberAuthService;
import ht.service.CommonService;
import ht.service.TemplateService;
import ht.util.DateUtil;
import ht.util.ExcelFileType;
import ht.util.ExcelView;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 셋팅 관련 controller
 * 		템플릿 관리
 * @author wizard
 *
 */
@Controller
@RequestMapping(path = "/template")
@Slf4j
public class TemplateController {

	@Resource
	private TemplateService templateService;
	@Resource
	private CommonService commonService;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private HTUtils htUtils;

	/**
	 * 템플릿 페이지 조회
	 */
	@RequestMapping(value="/selectTemplate" , method = {RequestMethod.GET, RequestMethod.POST})
	public String selectTemplate(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param) {

		String memberDivCd;
		String tplDivCd;

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		try{
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			memberDivCd = memberVO.getMemberDivCd().toString();
			
			//부서코드
			param.put("departCd", memberVO.getDepartCd());
			model.put("memberDivCd", memberDivCd);

			if(memberDivCd.equals(CommonConstants.MEMBER_DIV_CD_A) 
					|| memberDivCd.equals(CommonConstants.MEMBER_DIV_CD_S) 
					|| memberDivCd.equals(CommonConstants.MEMBER_DIV_CD_M)) {
				tplDivCd = "G";
			} else {
				tplDivCd = "P";
			}

			param.put("tplDivCd", tplDivCd);

			// 템플릿 카테고리 목록 조회
			List<Map<String, Object>> categoryList = templateService.selectTplCategoryList(param);

//			model.put("tplDivCd", tplDivCd);
			model.put("categoryList", categoryList);

			// 템플릿 메세지 구분 코드 목록 조회
			List<Map<String, Object>> tplMsgDivCdList = commonService.selectCodeList(CommonConstants.COMM_CD_TPL_MSG_DIV_CD);
			model.put("tplMsgDivCdList", tplMsgDivCdList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		model.addAttribute("staticUpdateParam", customProperty.getStaticUpdateParam());
		model.addAttribute("happyTalkUrl", customProperty.getHappyTalkUrl());
		model.addAttribute("isProduction", htUtils.isActiveProfile("live"));

		return "template/templateManage";
	}

	/**
	 * 카테고리 추가/수정
	 */
	@RequestMapping(value="/saveTplCtg" , method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String, Object> saveTplCtg(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			//부서코드
			param.put("departCd", memberVO.getDepartCd());

			templateService.saveTplCtg(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 저장 되었습니다.");
		} catch (BizException e) {
			rtnMap.put("rtnCd", e.getErrCode());
			rtnMap.put("rtnMsg", e.getMessage());
		} catch(Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 카테고리의 템플릿 개수 조회
	 */
	@RequestMapping(value="/selectTplCtgCnt" , method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String, Object> selectTplCtgCnt(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		try {
			int tplCount = templateService.selectTplCtgCnt(param);
			rtnMap.put("tplCount", tplCount);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 카테고리 삭제
	 */
	@RequestMapping(value="/deleteTplCtg" , method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String, Object> deleteTplCtg(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			templateService.deleteTplCtg(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 삭제 되었습니다.");
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 템플릿 목록 조회
	 */
	@RequestMapping(value="/selectTemplateList" , method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String, Object> selectTemplateList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			
			//부서코드
			param.put("departCd", memberVO.getDepartCd());

			List<Map<String, Object>> templateList = templateService.selectTemplateList(param);
			rtnMap.put("templateList", templateList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 등록 대기중인 템플릿 목록 조회
	 */
	@RequestMapping(value="/selectConfirmTplList" , method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String, Object> selectConfirmTplList(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();

			//부서코드
			param.put("departCd", memberVO.getDepartCd());

			// 등록 대기중인 템플릿 목록 조회
			List<Map<String, Object>> confirmTplList = templateService.selectConfirmTplList(param);
			rtnMap.put("confirmTplList", confirmTplList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 템플릿 저장
	 */
	@RequestMapping(value="/saveTemplate" , method = {RequestMethod.POST})
	public @ResponseBody Map<String, Object> saveTemplate(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param
			, @RequestParam MultiValueMap<String, String> lparam
			, @RequestParam("tplImgUrl") MultipartFile file
			, @RequestParam("tplPdfUrl") MultipartFile file2) {

		log.debug("ManageController: Params: {}", param);
		log.debug("ManageController: MultiPartParams: {}", lparam);

		String memberDivCd;
		String permitYn = "N";
		String tplDivCd = StringUtil.nvl(param.get("tplDivCd"), "P");
		Map<String, Object> rtnMap = new HashMap<>();

		try {
			log.debug("********************************************************"+tplDivCd);
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());
			param.put("departCd", memberVO.getDepartCd());
			memberDivCd = memberVO.getMemberDivCd().toString();
			
			//공유파라미터 디폴트 공유x
			if (param.get("tplDivCd") != null) {
				tplDivCd =  param.get("tplDivCd").toString();
			} else {
				tplDivCd = "P";
			}
			
			//삼성증권 슈퍼어드민은 부서코드가 없음으로 템플릿을 무조건 전체공유로 세팅
			if (CommonConstants.MEMBER_DIV_CD_S.equals(memberDivCd)) {
				tplDivCd = "G";
			}
			
			//슈퍼어드민, 기본유저, 매니저 템플릿 자동 승인처리
			if (CommonConstants.MEMBER_DIV_CD_S.equals(memberDivCd) || CommonConstants.MEMBER_DIV_CD_A.equals(memberDivCd) || CommonConstants.MEMBER_DIV_CD_M.equals(memberDivCd)) {
				permitYn = "Y";
			}
			
			param.put("tplDivCd", tplDivCd);
			param.put("permitYn", permitYn);

			String kwdNm = param.get("kwdNmArr").toString();

			if (kwdNm.length() >= 40) {
				param.put("kwdNmArr", kwdNm.substring(0, 40));
			}

			templateService.saveTemplate(param, lparam, file, file2);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 저장 되었습니다.");
		} catch (BizException e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_FAILURE);
			rtnMap.put("rtnMsg", e.getLocalizedMessage());
		} catch (Exception e) {
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 템플릿 삭제
	 */
	@RequestMapping(value="/deleteTemplate" , method = {RequestMethod.POST})
	public @ResponseBody Map<String, Object> deleteTemplate(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			templateService.deleteTemplate(param);

			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
			rtnMap.put("rtnMsg", "정상적으로 저장 되었습니다.");
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 템플릿 조회
	 */
	@RequestMapping(value="/selectTemplateOne" , method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Map<String, Object> selectTemplateOne(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		try {
			Map<String, Object> template = templateService.selectTemplate(param);
			String org_img_path = (String) template.get("org_img_path");
			String cns_frt_msg_img ="";
			if (org_img_path != null) {
				cns_frt_msg_img = org_img_path.replaceAll(customProperty.getStoragePath(), customProperty.getUploadUrlBase());
				template.put("cns_frt_msg_img", cns_frt_msg_img);
			}

			rtnMap.put("template", template);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}

	/**
	 * 템플릿 양식 다운로드
	 * @param jSessionId
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @param ModelMap
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/downloadTplExcel" , method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView downloadExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, Map<String, Object> ModelMap
			, @RequestParam Map<String, Object> param) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		MemberVO memberVO = null;

		try {
			// 세션 사용자 정보 조회
			memberVO = MemberAuthService.getCurrentUser();
			param.put("departCd", memberVO.getDepartCd());
			// 엑셀파일명
			String target = "sample_" + DateUtil.getCurrentDate("yyyyMMdd");
			String docName = URLEncoder.encode(target, "UTF-8"); // UTF-8로 인코딩

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ".xls");
			// 엑셀파일명 한글깨짐 조치
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			List<Map<String, Object>> titleList = new ArrayList<>();
			Map<String, Object> map = new HashMap<>();
			map.put("title", "템플릿 카테고리명(필수)");
			map.put("data", "tpl_ctg_nm");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "템플릿 카테고리 ID(필수)");
			map.put("data", "tpl_ctg_num");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "안드로이드 여부(Y,N)");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "아이폰 여부(Y,N)");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "웹 여부(Y,N)");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "구분(TEXT,NORMAL)");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "고객질문");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "상담직원답변");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "링크버튼명1");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "링크URL1");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "링크버튼명2");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "링크URL2");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "링크버튼명3");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "링크URL3");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "링크버튼명4");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "링크URL4");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "키워드(,로구분)");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			map = new HashMap<>();
			map.put("title", "최상단등록(Y,N)");
			map.put("data", "");
			map.put("width", 0);
			titleList.add(map);

			model.put("titleList", titleList);

			// 엑셀파일에 저장할 리스트를 가져온다.
			List<Map<String, Object>> categoryList = null;
			try {
				categoryList = templateService.selectTplCategoryList(param);
			} catch (Exception e1) {
				HTUtils.batmanNeverDie(e1);
				categoryList = null;
			}

			model.put("dataList", categoryList);

		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return new ModelAndView(new ExcelView(), model);
	}

	/**
	 * 템플릿 일괄 등록
	 */
	@RequestMapping(value="/updateTplExcel" , method = {RequestMethod.POST})
	public @ResponseBody Map<String, Object> updateTplExcel(@CookieValue(value = "JSESSIONID", required = false) String jSessionId
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, ModelMap model
			, @RequestParam Map<String, Object> param
			, @RequestParam("tplUploadFile") MultipartFile file) {

		log.debug("ManageController > : {}", request.getRequestURI());
		log.debug("session : {}", session.getId());

		Map<String, Object> rtnMap = new HashMap<>();

		try {
			// 세션 사용자 정보 조회
			MemberVO memberVO = MemberAuthService.getCurrentUser();
			param.put("sessionMemberUid", memberVO.getMemberUid());

			Workbook wb = ExcelFileType.getWorkBook(file);

			Sheet sheet = wb.getSheetAt(0);
			int rowCnt = sheet.getPhysicalNumberOfRows();

			Row row;

			List<Map<String, Object>> dataList = new ArrayList<>();
			Map<String, Object> dataMap;

			for(int rowIndex = 1; rowIndex < rowCnt; rowIndex++) {
				row = sheet.getRow(rowIndex);
				if(row != null) {
					dataMap = new HashMap<>();

					dataMap.put("tplDivCd", "G");
					dataMap.put("sessionMemberUid", memberVO.getMemberUid());

					//					String tplMsgDivCd = StringUtil.nvl(row.getCell(5)).replaceAll(" ", "").toUpperCase();
					//					String replyContText = StringUtil.nvl(row.getCell(7));
					String linkNm1 = StringUtil.nvl(row.getCell(8)).replaceAll(" ", "");
					String linkUrl1 = StringUtil.nvl(row.getCell(9)).replaceAll(" ", "");
					String linkNm2 = StringUtil.nvl(row.getCell(10)).replaceAll(" ", "");
					String linkUrl2 = StringUtil.nvl(row.getCell(11)).replaceAll(" ", "");
					String linkNm3 = StringUtil.nvl(row.getCell(12)).replaceAll(" ", "");
					String linkUrl3 = StringUtil.nvl(row.getCell(13)).replaceAll(" ", "");
					String linkNm4 = StringUtil.nvl(row.getCell(14)).replaceAll(" ", "");
					String linkUrl4 = StringUtil.nvl(row.getCell(15)).replaceAll(" ", "");


					dataMap.put("tplCtgNm", String.valueOf(row.getCell(0)).replaceAll(" ", ""));
					dataMap.put("tplCtgNum", String.valueOf(row.getCell(1)).replaceAll(" ", ""));
					dataMap.put("androidYn", String.valueOf(row.getCell(2)).replaceAll(" ", "").toUpperCase());
					dataMap.put("iphoneYn", String.valueOf(row.getCell(3)).replaceAll(" ", "").toUpperCase());
					dataMap.put("webYn", String.valueOf(row.getCell(4)).replaceAll(" ", "").toUpperCase());
					dataMap.put("tplMsgDivCd", String.valueOf(row.getCell(5)).replaceAll(" ", "").toUpperCase());
					dataMap.put("cstmQue", String.valueOf(row.getCell(6)));
					dataMap.put("replyContText", String.valueOf(row.getCell(7)));
					dataMap.put("linkNm1", linkNm1);
					dataMap.put("linkUrl1", linkUrl1);
					dataMap.put("linkNm2", linkNm2);
					dataMap.put("linkUrl2", linkUrl2);
					dataMap.put("linkNm3", linkNm3);
					dataMap.put("linkUrl3", linkUrl3);
					dataMap.put("linkNm4", linkNm4);
					dataMap.put("linkUrl4", linkUrl4);
					dataMap.put("kwdNmArr", String.valueOf(row.getCell(16)).replaceAll(" ", ""));
					dataMap.put("topMarkYn", String.valueOf(row.getCell(17)).replaceAll(" ", "").toUpperCase());

					// text
					List<String> inputMsgArr = new ArrayList<>();
					inputMsgArr.add(String.valueOf(row.getCell(7)));

					// image
					List<Map<String, String>> inputFileArr = null;

					// link
					List<Map<String, String>> linkBtnArr = null;

					if((!"".equals(linkNm1) && !"".equals(linkUrl1))
							|| (!"".equals(linkNm2) && !"".equals(linkUrl2))
							|| (!"".equals(linkNm3) && !"".equals(linkUrl3))
							||(!"".equals(linkNm4) && !"".equals(linkUrl4))) {
						linkBtnArr = new ArrayList<>();
						if(!"".equals(linkNm1) && !"".equals(linkUrl1)) {
							Map<String, String> linkMap = new HashMap<String, String>();
							linkMap.put("linkNm", linkNm1);
							linkMap.put("linkUrl", linkUrl1);
							linkBtnArr.add(linkMap);
						}
						if(!"".equals(linkNm2) && !"".equals(linkUrl2)) {
							Map<String, String> linkMap = new HashMap<String, String>();
							linkMap.put("linkNm", linkNm2);
							linkMap.put("linkUrl", linkUrl2);
							linkBtnArr.add(linkMap);
						}
						if(!"".equals(linkNm3) && !"".equals(linkUrl3)) {
							Map<String, String> linkMap = new HashMap<String, String>();
							linkMap.put("linkNm", linkNm3);
							linkMap.put("linkUrl", linkUrl3);
							linkBtnArr.add(linkMap);
						}
						if(!"".equals(linkNm4) && !"".equals(linkUrl4)) {
							Map<String, String> linkMap = new HashMap<String, String>();
							linkMap.put("linkNm", linkNm4);
							linkMap.put("linkUrl", linkUrl4);
							linkBtnArr.add(linkMap);
						}
					}

					String replyCont = StringUtil.buildTemplateContents(inputMsgArr, inputFileArr, linkBtnArr);
					dataMap.put("replyCont", replyCont);

					dataList.add(dataMap);
				}
			}

			if (dataList.size() > 0) {
				String result = templateService.saveTemplateAll(dataList);

				if ("".equals(result)) {
					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
					rtnMap.put("rtnMsg", "정상적으로 저장 되었습니다.");
				} else {
					rtnMap.put("rtnCd", CommonConstants.RESULT_CD_SUCCESS);
					rtnMap.put("rtnMsg", result + "행이 등록되지 않았습니다.");
				}
			} else {
				rtnMap.put("rtnCd", CommonConstants.RESULT_CD_FAILURE);
				rtnMap.put("rtnMsg", "등록된 내용이 없습니다.");
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			rtnMap.put("rtnCd", CommonConstants.RESULT_CD_ERROR);
			rtnMap.put("rtnMsg", "처리중 오류가 발생하였습니다.");
			log.error("{}", e.getLocalizedMessage(), e);
		}

		return rtnMap;
	}
}
