package ht.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.persistence.CodeDao;
import ht.util.StringUtil;

@SuppressWarnings("deprecation")
@Service
@Slf4j
public class CodeService {

	@Resource
	private CodeDao codeDao;

	/**
	 * 코드 목록 조회
	 */
	public List<Map<String, Object>> selectCodeList(Map<String, Object> param) {
		List<Map<String, Object>> resultList = codeDao.selectCodeList(param);
		log.info("============= param : " + param);
		log.info("============================== resultList : " + resultList);
		for(Map<String, Object> code : resultList) {
			code.put("cd", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(code.get("cd"), "")) );
			code.put("cd_nm", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(code.get("cd_nm"), "")) );
			code.put("cd_desc", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(code.get("cd_desc"), "")) );
		}
		return resultList;
	}
	
	/**
	 * 코드 전체 목록 조회
	 */
	public List<Map<String, Object>> selectAllCodeList(Map<String, Object> param) {
		return codeDao.selectAllCodeList(param);
	}
	
	/**
	 * 코드 상세 조회
	 */
	public Map<String, Object> selectCodeDetail(Map<String, Object> param){
		Map<String, Object> code = new HashMap<String, Object>();
		if(param.get("cd") != null) {
			code = codeDao.selectCodeDetail(param);
			code.put("cd", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(code.get("cd"), "")) );
			code.put("cd_nm", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(code.get("cd_nm"), "")) );
			code.put("cd_desc", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(code.get("cd_desc"), "")) );
			
		}
		return code;
	}

	/**
	 * 코드 저장
	 */
	@Transactional
	public void insertCode(Map<String, Object> param) {
		codeDao.insertCode(param);
	}

	/**
	 * 코드 수정
	 */
	@Transactional
	public void updateCode(Map<String, Object> param) {
		codeDao.updateCode(param);
	}

	/**
	 * 코드 삭제
	 */
	@Transactional
	public void deleteCode(Map<String, Object> param) {
		codeDao.deleteCode(param);
	}
	
}
