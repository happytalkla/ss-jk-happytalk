package ht.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import ht.util.CipherUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatDao {

	@Resource
	private SqlSession sqlSession;
	@Resource
	private CipherUtils cipherUtils;

	/**
	 * 채팅 메세지 조회
	 */
	public Map<String, Object> selectChatMessage(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.selectOne("chat.selectChatMessage", params);
	}

	/**
	 * 채팅 메세지 목록 조회
	 */
	public List<Map<String, Object>> selectChatMessageList(Map<String, Object> params) {

		return sqlSession.selectList("chat.selectChatMessage", params);
	}

	/**
	 * 채팅 메세지 목록 조회
	 */
	public List<Map<String, Object>> selectChatPrevMessageList(Map<String, Object> params) {

		return sqlSession.selectList("chat.selectChatPrevMessage", params);
	}

	/**
	 * 채팅 메세지 등록
	 */
	public int insertChatMessage(Map<String, Object> params) {

		return sqlSession.update("chat.insertChatMessage", params);
	}

	/**
	 * 채팅 메세지 변경
	 */
	public int updateChatMessage(Map<String, Object> params) {

		return sqlSession.update("chat.updateChatMessage", params);
	}

	/**
	 * 채팅 메세지 삭제
	 */
	public int deleteChatMessage(Map<String, Object> params) {

		return sqlSession.delete("chat.deleteChatMessage", params);
	}

	/**
	 * 채팅방 조회
	 */
	public Map<String, Object> selectChatRoom(Map<String, Object> params) {

		Map<String, Object> item = sqlSession.selectOne("chat.selectChatRoom", params);
		if (item != null && item.get("end_memo") != null) {
			try {
				item.put("end_memo", cipherUtils.decrypt((String) item.get("end_memo")));
			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage());
			}
		}
		return item;
	}

	/**
	 * 채팅방 목록 조회
	 */
	public List<Map<String, Object>> selectChatRoomList(Map<String, Object> params) {

		return sqlSession.selectList("chat.selectChatRoom", params);
	}

	/**
	 * 채팅방 목록 카운트
	 */
	public Map<String, Object> selectChatRoomGoodCode(Map<String, Object> params) {

		return sqlSession.selectOne("chat.selectChatRoomGoodCode", params);
	}
	/**
	 * 채팅방 목록 카운트
	 */
	public int selectChatRoomListCount(Map<String, Object> params) {

		return sqlSession.selectOne("chat.selectChatRoomCount", params);
	}
	/**
	 * 채팅방 UID 목록 검색
	 */
	public List<String> selectChatRoomUidListBySearch(Map<String, Object> params) {

		return sqlSession.selectList("chat.selectChatRoomUidListBySearch", params);
	}

	/**
	 * 채팅방 상태별 카운트 (봇 관련, 공용)
	 */
	public Map<String, Object> selectChatRoomStatusCounter(Map<String, Object> params) {

		return sqlSession.selectOne("chat.selectChatRoomStatusCounter", params);
	}

	/**
	 * 채팅방 상태별 카운트 (상담직원 관련, 사용자별)
	 */
	public Map<String, Object> selectChatRoomStatusCounterByMemberUid(Map<String, Object> params) {

		return sqlSession.selectOne("chat.selectChatRoomStatusCounterByMemberUid", params);
	}

	/**
	 * 채팅방 상태별 카운트 (요청 관련, 사용자별)
	 */
	public Map<String, Object> selectChatRoomExtraStatusCounterByMemberUid(Map<String, Object> params) {

		return sqlSession.selectOne("chat.selectChatRoomExtraStatusCounterByMemberUid", params);
	}

	/**
	 * 채팅방 저장
	 */
	public int saveChatRoom(Map<String, Object> params) {

		return sqlSession.update("chat.saveChatRoom", params);
	}

	/**
	 * 깃발 저장
	 */
	public int updateRoomMark(Map<String, Object> params) {

		return sqlSession.update("chat.updateRoomMark", params);
	}

	/**
	 * 채팅방 정리 배치, 종료 대상 조회
	 */
	public List<Map<String, Object>> selectChatRoomByScheduler(Map<String, Object> params) {

		return sqlSession.selectList("chat.selectChatRoomByScheduler", params);
	}

	/**
	 * 채팅방 정리 배치, 종료
	 */
	public int endChatRoomByScheduler(Map<String, Object> params) {

		return sqlSession.update("chat.endChatRoomByScheduler", params);
	}

	/**
	 * 채팅방 삭제 (테스트용)
	 */
	public int deleteChatRoom(Map<String, Object> params) {

		return sqlSession.delete("chat.deleteChatRoom", params);
	}

	/**
	 * 상담직원 변경 요청 조회
	 */
	public Map<String, Object> selectChatCnsrChngReq(Map<String, Object> params) {

		return sqlSession.selectOne("chat.selectChatCnsrChngReq", params);
	}

	/**
	 * 상담직원 변경 요청 목록 조회
	 */
	public List<Map<String, Object>> selectChatCnsrChngReqList(Map<String, Object> params) {

		return sqlSession.selectList("chat.selectChatCnsrChngReq", params);
	}

	/**
	 * 상담직원 변경 요청 등록
	 */
	public int insertChatCnsrChngReq(Map<String, Object> params) {

		return sqlSession.insert("chat.insertChatCnsrChngReq", params);
	}

	/**
	 * 상담직원 변경 요청 변경
	 */
	public int updateChatCnsrChngReq(Map<String, Object> params) {

		return sqlSession.update("chat.updateChatCnsrChngReq", params);
	}

	/**
	 * 상담직원 변경 요청 이력 등록
	 */
	public int insertChatCnsrChngHis(Map<String, Object> params) {

		return sqlSession.insert("chat.insertChatCnsrChngHis", params);
	}

	/**
	 * 상담 평가 저장
	 */
	public int saveCnsEvl(Map<String, Object> params) {

		return sqlSession.insert("chat.saveCnsEvl", params);
	}

	/**
	 * '상담 종료 후 처리' 조회
	 */
	public Map<String, Object> selectChatEndInfo(Map<String, Object> params) {

		Map<String, Object> item = sqlSession.selectOne("chat.selectChatEndInfo", params);
		if (item != null && item.get("memo") != null) {
			try {
				item.put("memo", cipherUtils.decrypt((String) item.get("memo")));
			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage());
			}
		}
		return item;
	}

	/**
	 * '상담 종료 후 처리 팝업 조회 전용'
	 */
	public Map<String, Object> selectChatEndInfoPop(Map<String, Object> params) {

		Map<String, Object> item = sqlSession.selectOne("chat.selectChatEndInfoPop", params);
		if (item != null && item.get("memo") != null) {
			try {
				item.put("memo", cipherUtils.decrypt((String) item.get("memo")));
			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage());
			}
		}
		return item;
	}

	/**
	 * '상담 종료 후 처리' 저장
	 */
	public int saveChatEndInfo(Map<String, Object> params) {

		if (!Strings.isNullOrEmpty((String) params.get("memo"))) {
			try {
				params.put("memo", cipherUtils.encrypt((String) params.get("memo")));
			} catch (Exception e) {
				log.error("{}", e.getLocalizedMessage(), e);
			}
		}
		return sqlSession.update("chat.saveChatEndInfo", params);
	}

	/**
	 * 후처리시 분류 정보 업데이트
	 */
	public int saveChatRoomCtgNum(Map<String, Object> params) {

		return sqlSession.update("chat.saveChatRoomCtgNum", params);
	}

	/**
	 * 검토 요청 조회
	 */
	public List<Map<String, Object>> selectChatContReview(Map<String, Object> params) {

		return sqlSession.selectList("chat.selectChatContReview", params);
	}

	/**
	 * 검토 요청 등록
	 */
	public int insertChatContReview(Map<String, Object> params) {

		return sqlSession.insert("chat.insertChatContReview", params);
	}

	/**
	 * 검토 요청 변경
	 */
	public int updateChatContReview(Map<String, Object> params) {

		return sqlSession.update("chat.updateChatContReview", params);
	}

	/**
	 * '챗봇과 대화' 채팅방
	 */
	public List<Map<String, Object>> selectChatRoomWithBot(String startDate, String endDate, String id, String div,
			String name, String intent) {

		if (Strings.isNullOrEmpty(endDate)) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			endDate = formatter.parseDateTime(startDate).plusDays(1).toString("yyyy-MM-dd");
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("id", id);
		params.put("div", div);
		params.put("name", name);
		params.put("intent", intent);

		return sqlSession.selectList("chat.selectChatRoomWithBot", params);
	}

	/**
	 * 챗봇과 대화 이력
	 */
	public List<Map<String, Object>> selectChatWithBot(Set<String> chatRoomUidList, String intent) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("chatRoomUidList", chatRoomUidList);
		params.put("intent", intent);

		return sqlSession.selectList("chat.selectChatWithBot", params);
	}

	/**
	 * 챗봇과 대화 이력 (엑셀용)
	 */
	public List<Map<String, Object>> selectChatWithBotForExcel(String startDate, String endDate) {

		if (Strings.isNullOrEmpty(endDate)) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			endDate = formatter.parseDateTime(startDate).plusDays(1).toString("yyyy-MM-dd");
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);

		return sqlSession.selectList("chat.selectChatWithBotForExcel", params);
	}

	/**
	 * 챗봇과 대화 이력
	 */
	public List<Map<String, Object>> selectChatWithBot2(Map<String, Object> param) {

		return sqlSession.selectList("chat.selectChatWithBot2", param);
	}

	/**
	 * TODO: DELETEME, 임시오픈시만 사용
	 * 챗봇 답변 평가 버튼
	 */
	public int evlChatMessage(Map<String, Object> params) {

		return sqlSession.update("chat.evlChatMessage", params);
	}
}
