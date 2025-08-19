package ht.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MemberDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 회원 조회 by userId
	 */
	public Map<String, Object> selectMemberById(String userId) {
		return sqlSession.selectOne("member.selectUserInfoForLogin", userId);
	}

	/**
	 * 회원 목록 조회
	 */
	public List<Map<String, Object>> selectMemberList(Map<String, Object> param) {
		return sqlSession.selectList("member.selectMemberList", param);
	}

	/**
	 * 부서리스트 조회
	 */
	public List<Map<String, Object>> selectDepartList(Map<String, Object> param) {
		return sqlSession.selectList("member.selectDepartList", param);
	}

	/**
	 * 상담가능 회원수 조회
	 */
	public int selectValidMemberCount() {
		return sqlSession.selectOne("member.selectValidMemberCount");
	}

	/**
	 * 회원 조회 by memberUid
	 */
	public Map<String, Object> selectMember(Map<String, Object> param) {
		return sqlSession.selectOne("member.selectMember", param);
	}

	/**
	 * 조건별 회원수 조회
	 */
	public Map<String, Object> selectMemberTypeCount(Map<String, Object> param) {
		return sqlSession.selectOne("member.selectMemberTypeCount", param);
	}

	/**
	 * 회원 조회
	 */
	public List<Map<String, Object>> selectMemberForInsert(Map<String, Object> params) {
		return sqlSession.selectList("member.selectMemberForInsert", params);
	}

	/**
	 * 회원 등록
	 */
	public void insertMember(Map<String, Object> param) {
		sqlSession.insert("member.insertMember", param);
	}

	public String selectMemberKey(Map<String, Object> param) {
		return sqlSession.selectOne("member.selectMemberKey", param);
	}

	/**
	 * 회원 설정 정보 등록
	 */
	public void mergeMemberSet(Map<String, Object> param) {
		sqlSession.insert("member.mergeMemberSet", param);
	}

	/**
	 * 카테고리 회원 매핑 삭제
	 */
	public void deleteCtgMemberMapping(Map<String, Object> param) {
		sqlSession.delete("member.deleteCtgMemberMapping", param);
	}

	/**
	 * 카테고리 회원 매핑 저장
	 */
	public void insertCtgMemberMapping(Map<String, Object> param) {
		sqlSession.update("member.insertCtgMemberMapping", param);
	}

	/**
	 * 카테고리 회원 매핑 수정
	 */
	public void updateCtgMemberMapping(Map<String, Object> param) {
		sqlSession.update("member.updateCtgMemberMapping", param);
	}
	/**
	 * 회원 수정
	 */
	public void updateMember(Map<String, Object> param) {
		sqlSession.update("member.updateMember", param);
	}



	/**
	 * 비밀번호 변경
	 */
	public void changePasswd(Map<String, Object> param) {
		sqlSession.update("member.changePasswd", param);
	}

	/**
	 * 아너스넷 비밀번호 변경
	 */
	public void changeHonorPasswd(Map<String, Object> param) {
		sqlSession.update("member.changeHonorPasswd", param);
	}

	/**
	 * 아너스넷 비밀번호 조회
	 */
	public Map<String, Object> selectHonorsNetPw(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.selectOne("member.selectHonorsNetPw", params);
	}

	/**
	 * 아너스넷 스킵유져 체크
	 */
	public int selectSkipUser(Map<String, Object> params) {
		return sqlSession.selectOne("member.selectSkipUserCheck", params);
	}

	/**
	 * 계정활성화 체크
	 */
	public int selectValidCheck(Map<String, Object> param) {
		return sqlSession.selectOne("member.selectValidCheck", param);
	}

	/**
	 * 로그인 실패 6회이상 체크
	 */
	public int selectFailCheck(Map<String, Object> param) {
		return sqlSession.selectOne("member.selectFailCheck", param);
	}

	/**
	 * 아이디 비밀번호 체크
	 */
	public int selectIdPwdCheck(Map<String, Object> param) {
		return sqlSession.selectOne("member.selectIdPwdCheck", param);
	}

	/**
	 * 로그인 실패횟수 초기화
	 */
	public void updatePwdFail(Map<String, Object> param) {
		sqlSession.update("member.updatePwdFail", param);
	}




	/**
	 * 비밀번호 3회 중복체크
	 */
	public int passwd3DuplCheck(Map<String, Object> param) {
		return sqlSession.selectOne("member.passwd3DuplCheck", param);
	}

	/**
	 * 비밀번호 90일경과 체크
	 */
	public int passwd90Check(Map<String, Object> param) {
		return sqlSession.selectOne("member.passwd90Check", param);
	}

	/**
	 * memberUid 가져오기
	 */
	public String selectMemberUid(Map<String, Object> param) {
		return sqlSession.selectOne("member.selectMemberUid", param);
	}


	/**
	 * 로그인 날짜 업데이트
	 */
	public int updateLoginDate(Map<String, Object> param) {
		return sqlSession.update("member.updateLoginDate", param);
	}

	/**
	 * 상담원이면 나를 상위 상담관리자로 지정한 경우 모두 제거
	 */
	public void deleteUpperMemberUid(Map<String, Object> param) {
		sqlSession.update("member.deleteUpperMemberUid", param);
	}

	/**
	 * 계정 활성화
	 */
	public void updateMemberValid(Map<String, Object> param) {
		sqlSession.update("member.updateMemberValid", param);
	}

	/**
	 * 회원 삭제
	 */
	public void deleteMember(Map<String, Object> param) {
		sqlSession.update("member.deleteMember", param);
	}

	/**
	 * 사용자 조회
	 */
	public Map<String, Object> selectCMember(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.selectOne("member.selectCMember", params);
	}

	/**
	 * 상담 가능한 상담원 목록 조회
	 */
	public List<Map<String, Object>> selectCMemberList(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.selectList("member.selectCMember", params);
	}

	/**
	 * 상담 가능한 상담원 목록 조회
	 */
	public List<Map<String, Object>> selectAvailableCounselorList(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.selectList("member.selectAvailableMemberList", params);
	}

	/**
	 * 상담원 배정: 상담원별 상담중인 채팅방 개수
	 */
	public List<Map<String, Object>> selectMemberUidAndAssignCount(Map<String, Object> params) {

		return sqlSession.selectList("member.selectMemberUidAndAssignCount", params);
	}

	/**
	 * 사용자 설정 저장
	 */
	public int saveMemberSetting(Map<String, Object> params) {

		return sqlSession.update("member.saveMemberSetting", params);
	}

	/**
	 * 고객 정보 조회
	 */
	public Map<String, Object> selectCustomer(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.selectOne("member.selectCustomer", params);
	}

	/**
	 * 고객 정보 조회
	 */
	public Map<String, Object> selectCustomerByChatRoom(Map<String, Object> params) {

		log.debug("params: {}", params);
		return sqlSession.selectOne("member.selectCustomerByChatRoom", params);
	}

	/**
	 * 고객 정보 저장
	 */
	public int saveCustomer(Map<String, Object> params) {

		return sqlSession.update("member.saveCustomer", params);
	}

	/**
	 * 고객 정보 삭제 (테스트용)
	 */
	public int deleteCustomer(Map<String, Object> params) {

		return sqlSession.delete("member.deleteCustomer", params);
	}

	/**
	 * 카카오 고객 인증 정보 삭제 (30일)
	 */
	public int deleteCustomerKakao() {

		return sqlSession.delete("member.deleteCustomerKakao");
	}

	/**
	 * 카카오 고객 인증 정보 삭제 (고객)
	 */
	public int deleteCustomerKakaoByCstm(Map<String, Object> params) {

		return sqlSession.delete("member.deleteCustomer", params);
	}

	
	/**
	 * 고객 Grade(코끼리, VIP ...) 등록
	 */
	public int mergeCstmGradeHis(Map<String, Object> params) {

		return sqlSession.insert("member.mergeCstmGradeHis", params);
	}

	/**
	 * 임시 회원 목록 조회
	 */
	public List<Map<String, Object>> selectTmpMemberList(Map<String, Object> param) {
		return sqlSession.selectList("member.selectTmpMemberList", param);
	}

	/**
	 * 임시 회원 정보 등록
	 */
	public int insertTempMember(Map<String, Object> params) {
		return sqlSession.insert("member.insertTempMember", params);
	}

	/**
	 * FP 목록 삭제
	 */
	public int deleteFpTempMemberInfo() {
		return sqlSession.insert("member.deleteFpTempMemberInfo");
	}

	/**
	 * FP 목록 등록
	 *
	 * @param params
	 * @return
	 */
	public int insertFpTempMember(Map<String, Object> params) {
		return sqlSession.insert("member.insertFpTempMember", params);
	}

	/**
	 * FP 목록 제외 FP 삭제
	 */
	public int deleteFpTempMember() {
		return sqlSession.insert("member.deleteFpTempMember");
	}

	/**
	 * 기존 임시 회원 삭제
	 */
	public int deleteTempMember() {
		return sqlSession.insert("member.deleteTempMember");
	}

	/**
	 * 신규 임시 회원 사용으로 변경
	 */
	public int updateTempMember() {
		return sqlSession.insert("member.updateTempMember");
	}

	/**
	 * 회원 탈퇴 처리 - 신규 DB에 존재하지 않는 회원은 탈퇴 처리
	 */
	public int updateMemberLeaveAuto(String systemUser) {
		return sqlSession.insert("member.updateMemberLeaveAuto", systemUser);
	}

	/**
	 * 로그인 여부 30일 이상 체크
	 */
	public List<Map<String, Object>> selectLoginMemberCheck() {
		return sqlSession.selectList("member.selectLoginMemberCheck");
	}

	/**
	 * 로그인 여부 30일 이상인 계정 비활성화
	 */
	public int updateLoginMemberCheck(String member_uid) {
		return sqlSession.update("member.updateLoginMemberCheck", member_uid);
	}


	/**
	 * 회원 미인증 처리 - 부서 및 이름 변경 시 미인증 처리
	 */
	public int updateMemberValidAuto(String systemUser) {
		return sqlSession.insert("member.updateMemberValidAuto", systemUser);
	}

	/**
	 * 회원 수정 - 부서명만 변경시 update
	 */
	public int updateMemberDepartNm(String systemUser) {
		return sqlSession.insert("member.updateMemberDepartNm", systemUser);
	}

	/**
	 * 회원 유효성 체크
	 */
	public Map<String, Object> selectTmpMember(Map<String, Object> param) {

		return sqlSession.selectOne("member.selectTmpMember", param);
	}

	/**
	 * 고객 민감정보 조회
	 */
	public Map<String, Object> selectCstmSinfo(Map<String, Object> param) {

		return sqlSession.selectOne("member.selectCstmSinfo", param);
	}


	/**
	 * 상담원 배정수 재검증용
	 */
	public Map<String, Object> selectCountRoomByCounselor(Map<String, Object> param) {

		return sqlSession.selectOne("member.selectCountRoomByCounselor", param);
	}

	/**
	 * 고객 민감정보 저장
	 */
	public int saveCstmSinfo(Map<String, Object> param) {

		return sqlSession.update("member.saveCstmSinfo", param);
	}

	/**
	 * 고객 민감정보 삭제
	 */
	public int deleteCstmSinfo(Map<String, Object> param) {

		return sqlSession.delete("member.deleteCstmSinfo", param);
	}

	/**
	 * 기간계 계정 동기화 테이블 정보 조회
	 */
	public Map<String, String> selectTempMember(Map<String, String> param){

		List<Map<String, String>> tempMemberList = sqlSession.selectList("member.selectTempMember", param);
		if (tempMemberList == null || tempMemberList.isEmpty()) {
			return null;
		} else {
			return tempMemberList.get(0);
		}
	}

	/**
	 * 채팅방 후처리 미완료 count
	 */
	public int selectChatRoomEndMemoEmptyCount(Map<String, Object> params) {
		return sqlSession.selectOne("member.selectChatRoomEndMemoEmptyCount", params);
	}
}
