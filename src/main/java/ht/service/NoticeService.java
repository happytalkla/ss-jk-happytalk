package ht.service;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.domain.NoticeVO;
import ht.persistence.NoticeDao;

@Service
public class NoticeService {

	@Resource
	private NoticeDao noticeDao;

	/**
	 * 공지사항 목록 조회
	 */
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> param) {
		return noticeDao.selectNoticeList(param);
	}

	/**
	 * 공지사항 구분 목록 조회
	 */
	public List<Map<String, Object>> selectNoticeDivList(Map<String, Object> param) {
		return noticeDao.selectNoticeDivList(param);
	}

	/**
	 * 공지사항 상세 조회
	 */
	public NoticeVO selectNoticeDetail(Map<String, Object> param){
		NoticeVO noticeVO = noticeDao.selectNoticeDetail(param);
		String cont = noticeVO.getCont();

		if (!Strings.isNullOrEmpty(cont)) {
			noticeVO.setCont(cont.replaceAll("\\&lt;", "<").replaceAll("\\&gt;", ">"));
		}

		return noticeVO;
	}

	/**
	 * 공지사항 저장
	 */
	@Transactional
	public void insertNotice(Map<String, Object> param) {
		noticeDao.insertNotice(param);
	}

	/**
	 * 공지사항 수정
	 */
	@Transactional
	public void updateNotice(Map<String, Object> param) {
		noticeDao.updateNotice(param);
	}

	/**
	 * 공지사항 삭제
	 */
	@Transactional
	public void deleteNotice(Map<String, Object> param) {
		noticeDao.deleteNotice(param);
	}
}
