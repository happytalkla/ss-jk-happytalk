package ht.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.persistence.TermDao;
import ht.util.StringUtil;

@SuppressWarnings("deprecation")
@Service
public class TermService {

	@Resource
	private TermDao termDao;

	/**
	 * 용어사전 목록 조회
	 */
	public List<Map<String, Object>> selectTermList(Map<String, Object> param) {
		List<Map<String, Object>> resultList = termDao.selectTermList(param);
		
		for(Map<String, Object> term : resultList) {
			term.put("term_div_nm", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(term.get("term_div_nm"), "")) );
			term.put("title", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(term.get("title"), "")) );
			term.put("cont", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(term.get("cont"), "")) );
			term.put("term_tag", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(term.get("term_tag"), "")) );
		}
		return resultList;
	}
	
	/**
	 * 용어사전 전체 목록 조회
	 */
	public List<Map<String, Object>> selectAllTermList(Map<String, Object> param) {
		return termDao.selectAllTermList(param);
	}
	
	/**
	 * 용어사전 상세 조회
	 */
	public Map<String, Object> selectTermDetail(Map<String, Object> param){
		Map<String, Object> term = termDao.selectTermDetail(param);
		
		term.put("term_div_nm", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(term.get("term_div_nm"), "")) );
		term.put("title", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(term.get("title"), "")) );
		term.put("cont", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(term.get("cont"), "")) );
		term.put("term_tag", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(term.get("term_tag"), "")) );

		return term;
	}

	/**
	 * 용어사전 저장
	 */
	@Transactional
	public void insertTerm(Map<String, Object> param) {
		termDao.insertTerm(param);
	}

	/**
	 * 용어사전 수정
	 */
	@Transactional
	public void updateTerm(Map<String, Object> param) {
		termDao.updateTerm(param);
	}

	/**
	 * 용어사전 삭제
	 */
	@Transactional
	public void deleteTerm(Map<String, Object> param) {
		termDao.deleteTerm(param);
	}
	
	/**
	 * 용어사전 목록 조회
	 */
	public List<Map<String, Object>> selectAutoTermListAjax(Map<String, Object> param) {
		return termDao.selectAutoTermListAjax(param);
	}
	
	
	/**
	 * 용어검색 저장
	 */
	@Transactional
	public void insertReportingTerm(Map<String, Object> param) {
		termDao.insertReportingTerm(param);
	}
}
