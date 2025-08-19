package ht.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.persistence.ScreenDao;
import ht.util.StringUtil;

@SuppressWarnings("deprecation")
@Service
public class AppService {

	@Resource
	private ScreenDao screenDao;

	/**
	 * 용어사전 목록 조회
	 */
	public List<Map<String, Object>> selectScreenList(Map<String, Object> param) {
		List<Map<String, Object>> resultList = screenDao.selectScreenList(param);
		
		for(Map<String, Object> screen : resultList) {
			screen.put("screen_num", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(screen.get("screen_num"), "")) );
			screen.put("screen_name", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(screen.get("screen_name"), "")) );
		}
		return resultList;
	}
	
	/**
	 * 용어사전 전체 목록 조회
	 */
	public List<Map<String, Object>> selectAllScreenList(Map<String, Object> param) {
		return screenDao.selectAllScreenList(param);
	}
	
	/**
	 * 용어사전 상세 조회
	 */
	public Map<String, Object> selectScreenDetail(Map<String, Object> param){
		Map<String, Object> screen = screenDao.selectScreenDetail(param);
		
		screen.put("screen_num", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(screen.get("screen_num"), "")) );
		screen.put("screen_name", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(screen.get("screen_name"), "")) );

		return screen;
	}

	/**
	 * 용어사전 저장
	 */
	@Transactional
	public void insertScreen(Map<String, Object> param) {
		screenDao.insertScreen(param);
	}

	/**
	 * 용어사전 수정
	 */
	@Transactional
	public void updateScreen(Map<String, Object> param) {
		screenDao.updateScreen(param);
	}

	/**
	 * 용어사전 삭제
	 */
	@Transactional
	public void deleteScreen(Map<String, Object> param) {
		screenDao.deleteScreen(param);
	}
	
	/**
	 * 용어사전 목록 조회
	 */
	public List<Map<String, Object>> selectAutoScreenListAjax(Map<String, Object> param) {
		return screenDao.selectAutoScreenListAjax(param);
	}
	
	
	/**
	 * 용어검색 저장
	 */
	@Transactional
	public void insertReportingScreen(Map<String, Object> param) {
		screenDao.insertReportingScreen(param);
	}
}
