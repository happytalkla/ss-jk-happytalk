package ht.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SettingDao {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private SqlSession sqlSession;

	/**
	 * 해당년도의 휴일 목록 조회
	 */
	public Map<String, Object> selectWorkTime(Map<String, Object> param) {
		return sqlSession.selectOne("setting.selectWorkTime", param);
	}
	public Map<String, Object> selectTMWorkTime() {
		return sqlSession.selectOne("setting.selectTMWorkTime");
	}	
	public Map<String, Object> selectCSWorkTime() {
		return sqlSession.selectOne("setting.selectCSWorkTime");
	}	
	public Map<String, Object> selectKKWorkTime() {
		return sqlSession.selectOne("setting.selectKKWorkTime");
	}

	/**
	 * 기본 설정 정보 조회
	 */
	public Map<String, Object> selectDefaultSet() {
		return sqlSession.selectOne("setting.selectDefaultSet");
	}

	/**
	 * 기본 설정 변경
	 */
	public void updateSet(Map<String, Object> param) {
		sqlSession.update("setting.updateSet", param);
	}

	/**
	 * 근무 시간 설정 변경 - 홈페이지
	 */
	public void updateSetWorkTime(Map<String, Object> param) {
		// TODO: 서비스설정-정보일괄설정, 상담관리-근무관리 로직 확인...
		param.put("channel", StringUtil.nvl(param.get("channel"), CommonConstants.CSTM_LINK_DIV_CD_A));
		sqlSession.update("setting.updateSetWorkTime", param);
	}
	
	public void updateBSetWorkTime(Map<String, Object> param) {
		param.put("channel", "B");
		param.put("weekStartTime", 	param.get("KKweekStartTime").toString());
		param.put("weekEndTime", 	param.get("KKweekEndTime").toString());
		param.put("satWorkYn", 		param.get("KKsatWorkYn").toString());
		param.put("satStartTime", 	param.get("KKsatStartTime").toString());
		param.put("satEndTime", 	param.get("KKsatEndTime").toString());
		param.put("sunWorkYn", 		param.get("KKsunWorkYn").toString());
		param.put("sunStartTime", 	param.get("KKsunStartTime").toString());
		param.put("sunEndTime", 	param.get("KKsunEndTime").toString());
		sqlSession.update("setting.updateSetWorkTime", param);
	}
	public void updateCSetWorkTime(Map<String, Object> param) {
		param.put("channel", "C");
		param.put("weekStartTime", 	param.get("O2weekStartTime").toString());
		param.put("weekEndTime", 	param.get("O2weekEndTime").toString());
		param.put("satWorkYn", 		param.get("O2satWorkYn").toString());
		param.put("satStartTime", 	param.get("O2satStartTime").toString());
		param.put("satEndTime", 	param.get("O2satEndTime").toString());
		param.put("sunWorkYn", 		param.get("O2sunWorkYn").toString());
		param.put("sunStartTime", 	param.get("O2sunStartTime").toString());
		param.put("sunEndTime", 	param.get("O2sunEndTime").toString());
		sqlSession.update("setting.updateSetWorkTime", param);
	}
	public void updateDSetWorkTime(Map<String, Object> param) {
		param.put("channel", "D");
		param.put("weekStartTime", 	param.get("MPweekStartTime").toString());
		param.put("weekEndTime", 	param.get("MPweekEndTime").toString());
		param.put("satWorkYn", 		param.get("MPsatWorkYn").toString());
		param.put("satStartTime", 	param.get("MPsatStartTime").toString());
		param.put("satEndTime", 	param.get("MPsatEndTime").toString());
		param.put("sunWorkYn", 		param.get("MPsunWorkYn").toString());
		param.put("sunStartTime", 	param.get("MPsunStartTime").toString());
		param.put("sunEndTime", 	param.get("MPsunEndTime").toString());
		sqlSession.update("setting.updateSetWorkTime", param);
	}

	/*
	 * IPCC_MCH ARS 채널 근무시간 수정
	 */
	public void updateESetWorkTime(Map<String, Object> param) {
		param.put("channel", "E");
		param.put("weekStartTime", 	param.get("ARSweekStartTime").toString());
		param.put("weekEndTime", 	param.get("ARSweekEndTime").toString());
		param.put("satWorkYn", 		param.get("ARSsatWorkYn").toString());
		param.put("satStartTime", 	param.get("ARSsatStartTime").toString());
		param.put("satEndTime", 	param.get("ARSsatEndTime").toString());
		param.put("sunWorkYn", 		param.get("ARSsunWorkYn").toString());
		param.put("sunStartTime", 	param.get("ARSsunStartTime").toString());
		param.put("sunEndTime", 	param.get("ARSsunEndTime").toString());
		sqlSession.update("setting.updateSetWorkTime", param);
	}
	

	/**
	 * 상담 경과 시간 표시 변경
	 */
	public void updateSetPassTime(Map<String, Object> param) {
		sqlSession.update("setting.updateChatPassTimeInit", param);
		sqlSession.update("setting.updateChatPassTime", param);
	}

	/**
	 * 메세지 설정 정보 조회
	 */
	public Map<String, Object> selectMessage(Map<String, Object> param) {
		return sqlSession.selectOne("setting.selectMessage", param);
	}
	
	/**
	 * 해피봇 설정 정보 조회
	 */
	public List<Map<String, Object>> selectHappyBot(Map<String, Object> param) {
		return sqlSession.selectList("setting.selectHappyBot", param);
	}	
	
	/**
	 * 해피봇 블럭 리스트
	 */
	public List<Map<String, Object>> selectBotBlockList(Map<String, Object> param) {
		return sqlSession.selectList("setting.selectBotBlockList", param);
	}		
	

	/**
	 * 메세지 설정 변경
	 */
	public void updateMessage(Map<String, Object> param) {
		sqlSession.update("setting.updateMessage", param);
	}

	/**
	 * 해피봇 시나리오 설정 변경
	 */
	public void insertHappyBot(Map<String, Object> param) {
		sqlSession.update("setting.insertHappyBot", param);
	}
	/**
	 * 해피봇 시나리오 설정 삭제
	 */
	public void deleteHappyBot(Map<String, Object> param) {
		sqlSession.delete("setting.deleteHappyBot", param);
	}	
	

	/**
	 * 채팅 경과시간 표시 목록
	 */
	public List<Map<String, Object>> selectChatPassTimeList() {
		return sqlSession.selectList("setting.selectChatPassTimeList");
	}

	/**
	 * 욕필터 목록 조회
	 */
	public List<Map<String, Object>> selectForbiddenList() {
		return sqlSession.selectList("setting.selectForbiddenList");
	}
	/**
	 * 욕필터 목록 조회
	 */
	public List<Map<String, Object>> selectForbiddenDupList(Map<String, Object> param) {
		return sqlSession.selectList("setting.selectForbiddenList", param);
	}
	/**
	 * 욕필터 등록
	 */
	public void insertForbidden(Map<String, Object> param) {
		sqlSession.insert("setting.insertForbidden", param);
	}

	/**
	 * 욕필터 삭제
	 */
	public void deleteForbidden(Map<String, Object> param) {
		sqlSession.update("setting.deleteForbidden", param);
	}

	public List<Map<String, Object>> selectCtgMemberMappingList(Map<String, Object> params) {

		return sqlSession.selectList("setting.selectCtgMemberMapping", params); // TODO: join member_info
	}

	public Map<String, Object> selectCtg(Map<String, Object> params) {

		return sqlSession.selectOne("setting.selectCtg", params);
	}

	public List<Map<String, Object>> selectCtgList(Map<String, Object> params) {

		return sqlSession.selectList("setting.selectCtg", params);
	}

	public List<Map<String, Object>> selectCtgSub(Map<String, Object> params) {

		return sqlSession.selectList("setting.selectCtgSub", params);
	}

	public List<Map<String, Object>> selectCtgSuper(Map<String, Object> params) {

		return sqlSession.selectList("setting.selectCtgSuper", params);
	}

	public List<Map<String, Object>> selectCtgCstm() {

		return sqlSession.selectList("setting.selectCtgCstm");
	}

	public Map<String, Object> selectSet() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siteId", customProperty.getSiteId());

		Map<String, Object> map = sqlSession.selectOne("setting.selectSet", params);
		if (map == null) {
			throw new UnsupportedOperationException("NO SITE INIT");
		}

		log.debug("{}", map);

		return map;
	}

	public Map<String, Object> selectAssignScheduler(Map<String, Object> params) {

		return sqlSession.selectOne("setting.selectAssignScheduler", params);
	}

	public List<Map<String, Object>> selectAssignSchedulerList(Map<String, Object> params) {

		return sqlSession.selectList("setting.selectAssignScheduler", params);
	}

	public int saveAssignScheduler(Map<String, Object> params) {

		return sqlSession.update("setting.saveAssignScheduler", params);
	}

	public int deleteAssignScheduler(Map<String, Object> params) {

		return sqlSession.delete("setting.deleteAssignScheduler", params);
	}

	public List<Map<String, Object>> selectHighLightList(Map<String, Object> params) {
		return sqlSession.selectList("setting.selectHighLight", params);
	}

	public List<Map<String, Object>> selectRoomMarkList(Map<String, Object> params) {

		return sqlSession.selectList("setting.selectRoomMark", params);
	}

	public List<Map<String, Object>> selectCstmGradeList(Map<String, Object> params) {

		return sqlSession.selectList("setting.selectCstmGrade", params);
	}

	public int insertCnsrBreakHis(Map<String, Object> params) {

		return sqlSession.insert("setting.insertCnsrBreakHis", params);
	}

	/**
	 * 근무시작 배치 작동여부 체크
	 */
	public Map<String, Object> selectWorkStartTime() {

		return sqlSession.selectOne("setting.selectWorkStartTime");
	}

	/**
	 * 근무시작 배치
	 */
	public void updateWorkStartTime(String sessionMemberUid) {

		sqlSession.update("setting.updateWorkStartTime", sessionMemberUid);
	}
	
	/**
	 * 채널 코드 리스트 조회
	 */
	public List<Map<String, Object>> selectChannelList(Map<String, Object> params) {

		return sqlSession.selectList("setting.selectChannelList", params);
	}

}
