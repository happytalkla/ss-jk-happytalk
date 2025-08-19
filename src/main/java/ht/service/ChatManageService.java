package ht.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.persistence.ChatManageDao;
import ht.util.StringUtil;

@Service
public class ChatManageService {

	@Resource
	private ChatManageDao chatManageDao;

	/**
	 * 상담관리 - 상담 status별 목록 조회
	 */
	public List<Map<String, Object>> selectStatusRoomList(Map<String, Object> param) {
		return chatManageDao.selectStatusRoomList(param);
	}

	/**
	 * 상담 이력 조회
	 */
	public List<Map<String, Object>> selectCnsHisList(Map<String, Object> param) {
		return chatManageDao.selectCnsHisList(param);
	}

	/**
	 * 상담관리 - 지식화 관리 목록 - 프로젝트 목록
	 */
	public List<Map<String, Object>> selectProjectList(Map<String, Object> param) {
		return chatManageDao.selectProjectList(param);
	}

	/**
	 * 상담관리 - 지식화 관리 목록
	 */
	public List<Map<String, Object>> selectKnowList(Map<String, Object> param) {
		return chatManageDao.selectKnowList(param);
	}

	/**
	 * 상담관리 - 지식화 관리 조회
	 */
	public Map<String, Object> selectKnowledge(Map<String, Object> param) {
		return chatManageDao.selectKnowledge(param);
	}

	/**
	 * 상담관리 - 지식화 관리 등록
	 */
	@Transactional
	public void saveKnowMgt(Map<String, Object> param) {
		String knowNum = StringUtil.nvl(param.get("knowNum"));
		if("".equals(knowNum)) {
			chatManageDao.insertKnowMgt(param);
		}else {
			chatManageDao.updateKnowMgt(param);
		}
	}

	/**
	 * 상담관리 - 지식화 관리 삭제
	 */
	@Transactional
	public void deleteKnowMgt(Map<String, Object> param) {
		chatManageDao.deleteKnowMgt(param);
	}

}
