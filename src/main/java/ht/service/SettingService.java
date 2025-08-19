package ht.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.domain.ChatRoom;
import ht.persistence.CategoryDao;
import ht.persistence.CommonDao;
import ht.persistence.ManageDao;
import ht.persistence.MemberDao;
import ht.persistence.SettingDao;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Service
@Slf4j
public class SettingService {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private SettingDao settingDao;
	@Resource
	private CategoryDao categoryDao;
	@Resource
	private ManageDao manageDao;
	@Resource
	private CommonDao commonDao;
	@Resource
	private MemberDao memberDao;

	/**
	 * 근무시간 설정 조회
	 */
	public Map<String, Object> selectWorkTime(Map<String, Object> param) {
		return settingDao.selectWorkTime(param);
	}
	public Map<String, Object> selectTMWorkTime() {
		return settingDao.selectTMWorkTime();
	}
	public Map<String, Object> selectCSWorkTime() {
		return settingDao.selectCSWorkTime();
	}
	public Map<String, Object> selectKKWorkTime() {
		return settingDao.selectKKWorkTime();
	}


	/**
	 * 기본 설정 정보 조회
	 */
	public Map<String, Object> selectDefaultSet() {
		return settingDao.selectDefaultSet();
	}
	
	/**
	 * 기본 설정 변경
	 */
	@Transactional
	public void updateSet(Map<String, Object> param) {
		String selBtn = StringUtil.nvl(param.get("selBtn"));
		
		if ("unsocialAcceptYn".equals(selBtn)) {
			// 근무시간 변경
			settingDao.updateSetWorkTime(param);
			settingDao.updateBSetWorkTime(param);
			settingDao.updateCSetWorkTime(param);
			settingDao.updateDSetWorkTime(param);
			/*
			 * IPCC_MCH ARS 채널 추가
			 */
			settingDao.updateESetWorkTime(param);
			
			// 오늘이후 스케줄 삭제
			manageDao.deleteAllSchedule();
			
			// 이번달스케줄 등록. TODO : 
			Map<String, Object> map = new HashMap<>();
			map.put("schDateType", "MONTH");
			map.put("schAdd", 0);
			Map<String, Object> thisMonthMap = commonDao.selectCustomDate(map);
			String thisMonth = String.valueOf(thisMonthMap.get("sel_day"));
			param.put("schMonth", thisMonth.replaceAll("-", ""));
			manageDao.insertSchedule(param);
			manageDao.insertBSchedule(param);
			manageDao.insertCSchedule(param);
			manageDao.insertDSchedule(param);
			/*
			 * IPCC_MCH ARS 채널 추가
			 */
			manageDao.insertESchedule(param);
			
			// 다음달 스케줄 등록.
			map.put("schAdd", 1);
			Map<String, Object> nextMonthMap = commonDao.selectCustomDate(map);
			String nextMonth = String.valueOf(nextMonthMap.get("sel_day"));
			param.put("schMonth", nextMonth.replaceAll("-", ""));
			manageDao.insertSchedule(param);
			manageDao.insertBSchedule(param);
			manageDao.insertCSchedule(param);
			manageDao.insertDSchedule(param);
			/*
			 * IPCC_MCH ARS 채널 추가
			 */
			manageDao.insertESchedule(param);
		} else if ("passTimeUseYn".equals(selBtn)) {
			// 상담 경과시간 표시
			settingDao.updateSetPassTime(param);
		} else if ("ctgMgtDpt".equals(selBtn)) {
			// 소분류 사용여부
			categoryDao.deleteAllMapping();
		}
		
		settingDao.updateSet(param);
	}

	/**
	 * 메세지 설정 정보 조회
	 */
	public Map<String, Object> selectMessage(Map<String, Object> param) {
		return settingDao.selectMessage(param);
	}

	/**
	 * 해피봇 설정 정보 조회
	 */
	public List<Map<String, Object>> selectHappyBot(Map<String, Object> param) {
		return settingDao.selectHappyBot(param);
	}

	/**
	 * 해피봇 블럭 리스트
	 */
	public List<Map<String, Object>> selectBotBlockList(Map<String, Object> param) {
		return settingDao.selectBotBlockList(param);
	}

	/**
	 * 해피봇 블럭 리스트
	 */
	public List<Map<String, Object>> selectChannelCdList(Map<String, Object> param) {
		return settingDao.selectBotBlockList(param);
	}




	/**
	 * 메세지 설정 변경
	 */
	@Transactional
	public void updateMessage(Map<String, Object> param) {
		settingDao.updateMessage(param);
	}

	/**
	 * 해피봇 시나리오 설정
	 */
	@Transactional
	public void updateHappyBot(Map<String, Object> param, String[] nameArr, String[] modifiedArr, String[] useYnArr, String[] firstBlockIdArr) {
		settingDao.deleteHappyBot(param);
		for (int i=0;i<useYnArr.length;i++) {
			String useYn= (useYnArr[i].equals("Y")) ? "Y" : "N";
			String name=nameArr[i];
			String firstBlockId=firstBlockIdArr[i];
			String modified=modifiedArr[i];
			param.put("useYn", useYn);
			param.put("name", name);
			param.put("firstBlockId", firstBlockId);
			param.put("sort", i);
			param.put("modified", modified);
			settingDao.insertHappyBot(param);
		}

	}

	/**
	 * 채팅 경과시간 표시 목록
	 */
	public List<Map<String, Object>> selectChatPassTimeList() {
		return settingDao.selectChatPassTimeList();
	}

	/**
	 * 욕필터 목록 조회
	 */
	public List<Map<String, Object>> selectForbiddenList() {
		List<Map<String, Object>> resultList =  settingDao.selectForbiddenList();

		for(Map<String, Object> map : resultList) {
			map.put("forbidden", StringEscapeUtils.unescapeHtml4(StringUtil.nvl(map.get("forbidden"), "")) );
		}

		return resultList;
	}
	/**
	 * 욕필터 중복 조회
	 */
	public List<Map<String, Object>> selectForbiddenDupList(Map<String, Object> param) {
		return settingDao.selectForbiddenDupList(param);
	}
	/**
	 * 하이라이트 단어 목록 조회
	 */
	public List<Map<String, Object>> selectHighLightList(String memberUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("memberUid", memberUid);
		Map<String, Object> member = memberDao.selectCMember(params);
		String departCd = (String) member.get("depart_cd");

		params = new HashMap<>();
		params.put("memberUid", memberUid);
		params.put("departCd", departCd);

		return settingDao.selectHighLightList(params);
	}

	/**
	 * 욕필터 등록
	 */
	@Transactional
	public void insertForbidden(Map<String, Object> param) {
		param.put("forbidden", StringEscapeUtils.escapeHtml4(StringUtil.nvl(param.get("forbidden"), "")) );

		settingDao.insertForbidden(param);
	}

	/**
	 * 욕필터 삭제
	 */
	@Transactional
	public void deleteForbidden(Map<String, Object> param) {
		settingDao.deleteForbidden(param);
	}

	// ////////////////////////////////////////////////////////////////////////
	// 분류-상담원 매칭
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 분류별 상담원 목록
	 */
	List<Map<String, Object>> selectCounselorByCategory(@NotEmpty String ctgNum) {

		log.debug("GET COUNSELOR BY CATEGORY");

		Map<String, Object> params = new HashMap<>();
		//		params.put("ctgNum", ctgNum);
		//		List<Map<String, Object>> mapList = settingDao.selectCtgSub(params);
		//		List<String> ctgNumList = new ArrayList<>();
		//		for (Map<String, Object> map : mapList) {
		//			ctgNumList.add((String) map.get("ctg_num"));
		//		}
		//
		//		if (ctgNumList.isEmpty()) {
		//			return Collections.emptyList();
		//		}


		List<String> ctgNumList = new ArrayList<>();
		ctgNumList.add(ctgNum);
		//		params = new HashMap<>();
		params.put("ctgNumList", ctgNumList);
		return settingDao.selectCtgMemberMappingList(params);
	}
	/**
	 * 분류별 상담원 목록
	 */
	List<Map<String, Object>> selectCounselorByCategoryNwday(@NotEmpty String ctgNum) {

		log.debug("GET COUNSELOR BY CATEGORY no time");

		Map<String, Object> params = new HashMap<>();

		List<String> ctgNumList = new ArrayList<>();
		ctgNumList.add(ctgNum);
		params.put("ctgNumList", ctgNumList);
		params.put("WaitCstm", "true");
		return settingDao.selectCtgMemberMappingList(params);
	}
	// ////////////////////////////////////////////////////////////////////////
	// 분류
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 분류 조회
	 */
	public Map<String, Object> selectCategory(String ctgNum, boolean includeDeletedCategory) {

		Map<String, Object> params = new HashMap<>();
		params.put("ctgNum", ctgNum);
		if (includeDeletedCategory) {
			params.put("delYn", "Y");
		}
		return settingDao.selectCtg(params);
	}

	/**
	 * 분류 조회 (채널별)
	 */
	public Map<String, Object> selectCategory(String ctgNum, String channel, boolean includeDeletedCategory) {

		Map<String, Object> params = new HashMap<>();
		params.put("ctgNum", ctgNum);
		params.put("cstmLinkDivCd", channel);
		if (includeDeletedCategory) {
			params.put("delYn", "Y");
		}
		return settingDao.selectCtg(params);
	}

	/**
	 * 대외 고객 분류 조회
	 */
	public List<Map<String, Object>> selectCategoryForCustomer() {

		return settingDao.selectCtgCstm();
	}

	/**
	 * 분류 전체 목록
	 */
	public List<Map<String, Object>> selectCategoryListAll(boolean includeDeletedCategory) {

		Map<String, Object> params = new HashMap<>();
		if (includeDeletedCategory) {
			params.put("delYn", "Y");
		}
		return settingDao.selectCtgList(params);
	}

	/**
	 * 단계별 분류 목록
	 */
	public List<Map<String, Object>> selectCategoryByDepth(Integer ctgDpt) {

		Map<String, Object> params = new HashMap<>();
		params.put("ctgDpt", ctgDpt);
		return settingDao.selectCtgList(params);
	}

	/**
	 * 하위 분류 목록
	 */
	public List<Map<String, Object>> selectCategoryByParent(String upperCtgNum) {

		Map<String, Object> params = new HashMap<>();
		params.put("upperCtgNum", upperCtgNum);
		return settingDao.selectCtgList(params);
	}

	/**
	 * 하위 트리 목록
	 */
	public List<Map<String, Object>> selectCategorySubList(String ctgNum) {

		Map<String, Object> params = new HashMap<>();
		params.put("ctgNum", ctgNum);
		return settingDao.selectCtgSub(params);
	}

	/**
	 * 상위 트리 목록
	 */
	public List<Map<String, Object>> selectCategorySuperList(String ctgNum) {

		Map<String, Object> params = new HashMap<>();
		params.put("ctgNum", ctgNum);
		return settingDao.selectCtgSuper(params);
	}

	/**
	 * 미분류
	 */
	public Map<String, Object> selectDefaultCategory() {

		Map<String, Object> params = new HashMap<>();
		params.put("dftCtg", "dftCtg");
		params.put("siteId", customProperty.getSiteId());
		return settingDao.selectCtg(params);
	}

	// ////////////////////////////////////////////////////////////////////////
	// 사이트 세팅
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 사이트 세팅 목록
	 */
	public Map<String, Object> selectSiteSetting() {
		
		return settingDao.selectSet();
	}

	/**
	 * 사이트 세팅 값
	 */
	public Object getSetting(String name) {

		Map<String, Object> siteSetting = selectSiteSetting();
		return siteSetting.get(name);
	}

	/**
	 * 사이트 세팅 값
	 */
	public Object getSetting(String name, Map<String, Object> siteSetting) {

		return siteSetting.get(name);
	}

	/**
	 * 상담센터 ON/OFF
	 */
	boolean isSiteOn(Map<String, Object> siteSetting) {

		return CommonConstants.WORK_STATUS_CD_W.equals(siteSetting.get("work_status_cd"));
	}

	/**
	 * 자동 배정 여부
	 */
	public boolean isAutoAssign(Map<String, Object> siteSetting) {

		return "Y".equals(siteSetting.get("auto_mat_use_yn"));
	}

	/**
	 * 다이렉트 배정 여부
	 */
	boolean isDirectAssign(Map<String, Object> siteSetting) {

		return "Y".equals(siteSetting.get("ctg_mapping_use_yn"));
	}

	/**
	 * 챗봇 배정 여부
	 */
	public boolean isUseChatbot(Map<String, Object> siteSetting, String channel) {

		return "Y".equals(siteSetting.get(channel + "_" + "chatbot_use_yn"));
	}

	/**
	 * 상담원이 직접 배정 여부
	 */
	boolean isSelfAssign(Map<String, Object> siteSetting) {

		return "Y".equals(siteSetting.get("self_choi_use_yn"));
	}

	/**
	 * 바쁜시간 메세지 사용 여부
	 */
	public boolean isBusyMessage(Map<String, Object> siteSetting, String channel) {

		return "Y".equals(siteSetting.get(channel + "_" + "busy_msg_use_yn"));
	}

	/**
	 * 상담원 변경 요청시 자동 승인 여부
	 */
	public boolean isAutoPermitForRequestChangeCounselor(Map<String, Object> siteSetting) {

		return "Y".equals(siteSetting.get("auto_chng_appr_yn"));
	}

	/**
	 * 상담센터 상담 가능 여부
	 */
	boolean isWorkDay(Map<String, Object> workMap) {

		Integer now = Ints.tryParse(DateTimeFormat.forPattern("HHmm").print(new DateTime()));
		Integer startTime = Ints.tryParse(Strings.nullToEmpty((String) workMap.get("start_time")));
		Integer endTime = Ints.tryParse(Strings.nullToEmpty((String) workMap.get("end_time")));

		log.debug("workMap: {}", workMap);
		log.debug("now: {}, startTime: {}, endTime: {}", now, startTime, endTime);
		if (now != null && startTime != null && endTime != null) {
			return now >= startTime && now < endTime;
		}

		return false;
	}

	/**
	 * 근무시간외 접수 여부
	 */
	boolean isAcceptOffDuty(Map<String, Object> siteSetting, String channel) {

		return "Y".equals(siteSetting.get(channel + "_" + "unsocial_accept_yn"));
	}

	/**
	 * 상담원이 처리중인 최대 채팅방 수 (미접수 + 진행중)
	 */
	int getCounselorMaxCount(Map<String, Object> siteSetting) {
		try {
			return ((BigDecimal) siteSetting.get("cnsr_once_max_cnt")).intValue();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return 0;
		}
	}

	/**
	 * 사이트, 분류 Depth
	 */
	public int getMaxCategoryDepth(Map<String, Object> siteSetting) {
		try {
			return Ints.tryParse((String) siteSetting.get("ctg_mgt_dpt"));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return 0;
		}
	}

	/**
	 * 최대 미배정 개수 제한 여부, 제한(Y) / 무제한(N)
	 */
	public String isLimitMaxWaitCount(Map<String, Object> siteSetting, String channel) {
		try {
			return (String) siteSetting.get(channel + "_" + "cns_wait_pers_use_yn");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return "N";
		}
	}

	/**
	 * 최대 미배정 개수
	 */
	public int getMaxWaitCount(Map<String, Object> siteSetting, String channel) {

		try {
			//log.info("***************************** getMaxWaitCount ++++++++++++++++++++++++ " + siteSetting.get(channel + "_" + "cns_wait_cnt"));
			return ((BigDecimal) siteSetting.get(channel + "_" + "cns_wait_cnt")).intValue();
			
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return 0;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// 배정 스케줄러
	// ////////////////////////////////////////////////////////////////////////
	//	/**
	//	 * 채팅방 아이디로 배정 스케줄 열람
	//	 *
	//	 * @param chatRoomUid
	//	 * @return
	//	 */
	// public Map<String, Object> selectAssignSchedulerByChatRoomUid(String chatRoomUid) {
	//
	// Map<String, Object> params = new HashMap<>();
	// params.put("chatRoomUid", chatRoomUid);
	// return settingDao.selectAssignScheduler(params);
	// }

	/**
	 * 배정 스케줄 카운트
	 */
	public Integer selectAssignSchedulerCount(String ctg_num) {
		
		return selectAssignSchedulerList(ctg_num).size();
	}
	/**
	 * 배정 스케줄 열람 by ctg_num
	 */
	public Integer selectAssignSchedulerCountForCtg( String ctg_num ) {

		Map<String, Object> params = new HashMap<>();
		params.put("ctg_num", ctg_num);
		List<Map<String, Object>> listScheduler = settingDao.selectAssignSchedulerList(params);
		return listScheduler.size();
	}
	/**
	 * 배정 스케줄 열람 by chatRoomuid
	 */
	public boolean selectAssignSchedulerRoomUid( String chatRoomUid ) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		List<Map<String, Object>> listScheduler = settingDao.selectAssignSchedulerList(params);
		if(listScheduler.size() > 0)	return true;
		else return false;
	}

	/**
	 * 배정 스케줄 열람
	 */
	List<Map<String, Object>> selectAssignSchedulerList(String ctg_num ) {

		Map<String, Object> params = new HashMap<>();
		params.put("ctg_num", ctg_num);
		return settingDao.selectAssignSchedulerList(params);
	}

	/**
	 * 배정 스케줄 생성
	 */
	@Transactional
	public int saveAssignScheduler(ChatRoom chatRoom) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoom.getChatRoomUid());
		params.put("cstmUid", chatRoom.getCstmUid());
		params.put("ctgNum", chatRoom.getCtgNum());
		return settingDao.saveAssignScheduler(params);
	}

	/**
	 * 배정 스케줄 삭제
	 */
	@Transactional
	public int deleteAssignScheduler(String chatRoomUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("chatRoomUid", chatRoomUid);
		return settingDao.deleteAssignScheduler(params);
	}

	/**
	 * 배정 스케줄 전체 삭제
	 */
	@Transactional
	public int deleteAssignScheduler() {

		Map<String, Object> params = new HashMap<>();
		return settingDao.deleteAssignScheduler(params);
	}

	// ////////////////////////////////////////////////////////////////////////
	// 기타
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 깃발 정보 맵
	 *
	 * key: TB_ROOM_MARK.IMG_CLASS_NAME, value: TB_ROOM_MARK.ROOM_MARK_NUM
	 */
	public Map<String, String> selectRoomMarkMap() {

		Map<String, Object> params = new HashMap<>();
		params.put("delYn", "N");
		List<Map<String, Object>> mapList = settingDao.selectRoomMarkList(params);
		Map<String, String> result = new HashMap<>();
		for (Map<String, Object> map : mapList) {
			result.put((String) map.get("img_class_name"), (String) map.get("room_mark_num"));
		}

		return result;
	}

	/**
	 * 회원 등급 정보 맵
	 *
	 * key: TB_CSTM_GRADE.GRADE_NM, value: TB_CSTM_GRADE
	 */
	public Map<String, Map<String, Object>> selectCustomerGradeMap() {

		Map<String, Object> params = new HashMap<>();
		params.put("delYn", "N");
		List<Map<String, Object>> mapList = settingDao.selectCstmGradeList(params);
		Map<String, Map<String, Object>> result = new HashMap<>();
		for (Map<String, Object> map : mapList) {
			result.put((String) map.get("grade_nm"), map);
		}

		return result;
	}

	/**
	 * 상담원 휴식 시간 이력 저장
	 */
	@Transactional
	public int insertCnsrBreakHis(String memberUid, String creater, String workStatusCd) {

		Map<String, Object> params = new HashMap<>();
		params.put("memberUid", memberUid);
		params.put("creater", creater);
		params.put("workStatusCd", workStatusCd);

		return settingDao.insertCnsrBreakHis(params);
	}

	/**
	 * 근무시작전 상담원 근무가능으로 변경
	 */
	@Transactional
	public boolean runCounselorBreakTime() {
		boolean rtnFlag = false;
		Map<String, Object> map = settingDao.selectWorkStartTime();
		if (map != null) {
			String workYn = StringUtil.nvl(map.get("work_yn"));
			String type1 = StringUtil.nvl(map.get("type1"), "-1");
			String type2 = StringUtil.nvl(map.get("type2"), "-1");

			if ("Y".equals(workYn) && Integer.parseInt(type1) != -1 && Integer.parseInt(type2) != -1) {
				settingDao.updateWorkStartTime(customProperty.getSystemMemeberUid());
				rtnFlag = true;
			}
		}
		return rtnFlag;
	}

	/**
	 * 채널 코드리스트 조회
	 */
	public List<Map<String, Object>> selectChannelList(Map<String, Object> params) {
		return settingDao.selectChannelList(params);
	}
}
