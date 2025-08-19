package ht.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;

import ht.constants.CommonConstants;
import ht.exception.BizException;
import ht.persistence.TemplateDao;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TemplateService {

	@Resource
	private TemplateDao templateDao;
	@Resource
	private CommonService commonService;

	/**
	 * 템플릿 카테고리 목록 조회
	 */
	public List<Map<String, Object>> selectTplCategoryList(Map<String, Object> param) {
		List<Map<String, Object>> resultList =  templateDao.selectTplCategoryList(param);
		
		for(Map<String, Object> map : resultList) {
			map.put("tplCtgNm", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(map.get("tplCtgNm"), "")) );
		}
		
		return resultList;
	}

	/**
	 * 카테고리 추가/수정
	 */
	@Transactional
	public void saveTplCtg(Map<String, Object> param) throws Exception {
		String tplCtgNum = StringUtil.nvl(param.get("tplCtgNum"));
		
		param.put("tplCtgNm", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("tplCtgNm"), "")) );
		
		if("".equals(tplCtgNum)) {
			List<Map<String, Object>> list = templateDao.selectTplCategoryList(param);
			if(list != null && list.size() >= 7) {
				throw new BizException("카테고리는 최대 7개까지 추가 가능합니다");
			}
			templateDao.insertTplCtg(param);
		}else {
			templateDao.updateTplCtg(param);
		}
	}

	/**
	 * 카테고리의 템플릿 개수 조회
	 */
	public int selectTplCtgCnt(Map<String, Object> param) {
		return templateDao.selectTplCtgCnt(param);
	}

	/**
	 * 카테고리 삭제
	 */
	@Transactional
	public void deleteTplCtg(Map<String, Object> param) {
		templateDao.deleteCtgTemplate(param);
		templateDao.deleteTplCtg(param);
	}

	/**
	 * 템플릿 목록 조회
	 */
	public List<Map<String, Object>> selectTemplateList(Map<String, Object> param) {
		return templateDao.selectTemplateList(param);
	}

	/**
	 * 등록 대기중인 템플릿 목록 조회
	 */
	public List<Map<String, Object>> selectConfirmTplList(Map<String, Object> param) {
		return templateDao.selectConfirmTplList(param);
	}

	/**
	 * 템플릿 저장
	 */
	@Transactional
	public void saveTemplate(Map<String, Object> param, MultiValueMap<String, String> lparam, MultipartFile file, MultipartFile file2) throws Exception {

		// 템플릿 타입
		String tplMsgDivCd = StringUtil.nvl(param.get("tplMsgDivCd"));
		// 텍스트
		List<String> inputMsgArr = new ArrayList<>();
		inputMsgArr.add(StringUtil.nvl(param.get("replyContText")));
		// 이미지
		List<Map<String, String>> inputFileArr = new ArrayList<>();;
		// 링크
		List<Map<String, String>> linkBtnArr = null;

		// 'NORMAL'타입일 경우 이미지, 링크 존재
		if (CommonConstants.TPL_MSG_DIV_CD_NORMAL.equals(tplMsgDivCd)) {
			// 이미지
			if (!file.isEmpty()) {
				Map<String, Object> uploadFile = commonService.upload(file, "template");

				if (uploadFile != null && !Strings.isNullOrEmpty((String) uploadFile.get("url"))) {
					Map<String, String> fileMap = new HashMap<>();
					fileMap.put("fileNm", (String) uploadFile.get("url"));
					fileMap.put("fileType", (String) uploadFile.get("mimeType"));
					fileMap.put("title", StringUtil.nvl(param.get("cstmQue"), "템플릿 이미지"));
					inputFileArr.add(fileMap);
					
					param.put("orgImgNm", file.getOriginalFilename());
					param.put("orgImgPath", (String) uploadFile.get("url"));					
				}
			}else {
				if("N".equals(param.get("oldOrgImgDelYn")) && !StringUtil.isEmpty(param.get("oldOrgImgPath"))){
					Map<String, String> fileMap = new HashMap<>();
					fileMap.put("fileNm", (String)param.get("oldOrgImgPath"));
					fileMap.put("fileType", "image/png");
					fileMap.put("title", StringUtil.nvl(param.get("cstmQue"), "템플릿 이미지"));
					inputFileArr.add(fileMap);
					
					param.put("orgImgNm", StringUtil.nvl(param.get("oldOrgImgNm")));
					param.put("orgImgPath", StringUtil.nvl(param.get("oldOrgImgPath")));					
				}
			}
			
			// pdf
			if (!file2.isEmpty()) {
				Map<String, Object> uploadFile = commonService.upload(file2, "template");

				if (uploadFile != null && !Strings.isNullOrEmpty((String) uploadFile.get("url"))) {
					//inputFileArr = new ArrayList<>();
					Map<String, String> fileMap = new HashMap<>();
					fileMap.put("fileNm", (String) uploadFile.get("url"));
					fileMap.put("fileType", (String) uploadFile.get("mimeType"));
					fileMap.put("title", StringUtil.nvl(param.get("tplPdfBtn"), "템플릿 파일첨부"));
					inputFileArr.add(fileMap);
					
					param.put("orgPdfNm", file2.getOriginalFilename());
					param.put("orgPdfPath", (String) uploadFile.get("url"));
					param.put("orgMimeType", (String) uploadFile.get("mimeType"));
				}
			}else {
				if("N".equals(param.get("oldOrgPdfDelYn")) && !StringUtil.isEmpty(param.get("oldOrgPdfPath"))){
					Map<String, String> fileMap = new HashMap<>();
					fileMap.put("fileNm", StringUtil.nvl(param.get("oldOrgPdfPath"), ""));
					fileMap.put("fileType", StringUtil.nvl(param.get("oldMimeType"), "application/pdf"));
					fileMap.put("title", StringUtil.nvl(param.get("tplPdfBtn"), "템플릿 파일첨부"));
					inputFileArr.add(fileMap);
					
					param.put("orgPdfNm", StringUtil.nvl(param.get("oldOrgPdfNm")));
					param.put("orgPdfPath", StringUtil.nvl(param.get("oldOrgPdfPath")));
					param.put("orgMimeType", StringUtil.nvl(param.get("oldMimeType"), "application/pdf"));
				}
			}

			// 링크
			linkBtnArr = new ArrayList<>();
			List<String> linkNmList = StringUtil.nvlList(lparam.get("linkBtnNmArr"));
			List<String> linkUrlList = StringUtil.nvlList(lparam.get("linkUrlArr"));

			if (linkNmList != null && linkNmList.size() > 0 && linkUrlList != null && linkUrlList.size() > 0) {
				for (int i=0; i<linkNmList.size(); i++) {
					String linkNm = linkNmList.get(i);
					String linkUrl = linkUrlList.get(i);
					if (!StringUtil.isEmpty(linkNm) && !StringUtil.isEmpty(linkUrl)) {
						Map<String, String> linkMap = new HashMap<>();
						linkMap.put("linkNm", linkNm);
						linkMap.put("linkUrl", linkUrl);
						linkBtnArr.add(linkMap);
					}
				}
			}
		}

		String replyCont = StringUtil.buildTemplateContents(inputMsgArr, inputFileArr, linkBtnArr);
		param.put("replyCont", replyCont);
		
		// 기본값 셋팅
		String webYn = StringUtil.nvl(param.get("webYn"), "N");
		param.put("webYn", webYn);
		String androidYn = StringUtil.nvl(param.get("androidYn"), "N");
		param.put("androidYn", androidYn);
		String iphoneYn = StringUtil.nvl(param.get("iphoneYn"), "N");
		param.put("iphoneYn", iphoneYn);
		String topMarkYn = StringUtil.nvl(param.get("topMarkYn"), "N");
		param.put("topMarkYn", topMarkYn);
		String permitYn = StringUtil.nvl(param.get("permitYn"), "N");
		param.put("permitYn", permitYn);

		saveTemplate(param);
	}

	/**
	 * 템플릿 저장
	 */
	@Transactional
	public void saveTemplate(Map<String, Object> param) {
		log.info("save tpl, param : {}",param);
		String tplNum = StringUtil.nvl(param.get("tplNum"));
		
		param.put("cstmQue",StringUtil.nvl(param.get("cstmQue"), "").replaceAll("(?i)script", " ") );
		param.put("replyCont",StringUtil.nvl(param.get("replyCont"), "").replaceAll("(?i)script", " ") );
		
		if (!"".equals(tplNum)) {
			templateDao.updateTemplate(param);
		} else {
			templateDao.insertTemplate(param);
		}

		templateDao.deleteTplKwd(param);

		// 기존 키워드 삭제
		String kwdNmArr = StringUtil.nvl(param.get("kwdNmArr"));
		if (!"".equals(kwdNmArr)) {
			String[] kwdNmList = kwdNmArr.split(",");
			for (String kwdNm : kwdNmList) {
				param.put("kwdNm",StringUtil.nvl(kwdNm, "").replaceAll("(?i)script", " ") );
				templateDao.insertTplKwd(param);
			}
		}
	}

	/**
	 * 템플릿 삭제
	 */
	@Transactional
	public void deleteTemplate(Map<String, Object> param) throws BizException {

		templateDao.deleteTemplate(param);
	}

	/**
	 * 템플릿 조회
	 */
	public Map<String, Object> selectTemplate(Map<String, Object> param) {
		return templateDao.selectTemplate(param);
	}

	/**
	 * 템플릿 일괄 등록
	 */
	@Transactional
	public String saveTemplateAll(List<Map<String, Object>> dataList) {

		String rtnMsg = "";

		int idx = 0;
		for (Map<String, Object> param : dataList) {
			idx++;
			try {
				saveTemplate(param);
			} catch (Exception e) {
				if (!"".equals(rtnMsg)) {
					rtnMsg += ", ";
				}
				rtnMsg += idx;
			}
		}

		return rtnMsg;
	}

}
