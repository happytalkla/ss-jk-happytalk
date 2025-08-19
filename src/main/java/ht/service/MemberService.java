package ht.service;

import static ht.constants.CommonConstants.MEMBER_DIV_CD_C;
import static ht.constants.CommonConstants.WORK_STATUS_CD_R;
import static ht.constants.CommonConstants.WORK_STATUS_CD_W;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.exception.BizException;
import ht.persistence.MemberDao;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService {

	@Resource
	private MemberDao memberDao;
	@Resource
	private SettingService settingService;
	@Resource
	private HTUtils htUtils;
	@Resource
	private CustomProperty customProperty;

	/**
	 * 사용자 UID 생성
	 */
	public String createMemberUid() {

		return UUID.randomUUID().toString();
	}

	/**
	 * 사용자 조회 (by memberUid)
	 */
	public Map<String, Object> selectMemberByMemberUid(String memberUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("memberUid", memberUid);
		params.put("leaveYn", "N");
		params.put("validYn", "Y");
		return memberDao.selectCMember(params);
	}

	/**
	 * 사용자 조회 (by cocId)
	 */
	public Map<String, Object> selectMemberByCocId(String cocId) {

		Map<String, Object> params = new HashMap<>();
		params.put("cocId", cocId);
		params.put("leaveYn", "N");
		params.put("validYn", "Y");
		return memberDao.selectCMember(params);
	}

	// public List<Map<String, Object>> selectMemberList(List<Map<String, Object>> memberList) {
	//
	// Map<String, Object> params = new HashMap<>();
	// params.put("memberList", memberList);
	// return memberDao.selectMemberList(params);
	// }

	// public int saveMember(Map<String, Object> map) {
	//
	// return memberDao.saveMember(map);
	// }

	/**
	 * 상담원 목록 조회
	 *
	 * @param onlyCounselor 타입이 상담원(TB_MEMBER_INFO.MEMBER_DIV_CD)인 멤버만
	 */
	public List<Map<String, Object>> selectCounselorList(boolean onlyCounselor) {

		Map<String, Object> params = new HashMap<>();
		if (onlyCounselor) {
			params.put("memberDivCd", MEMBER_DIV_CD_C);
		}
		params.put("cnsPossibleYn", 'Y');
		params.put("leaveYn", "N");
		params.put("validYn", "Y");
		return memberDao.selectCMemberList(params);
	}

	/**
	 * 상담 가능한 상담원 목록 조회
	 */
	public List<Map<String, Object>> selectAvailableCounselorList(@NotEmpty String departCd) {

		return selectAvailableCounselorList(null, false, false, departCd, false);
	}

	/**
	 * 상담 가능한 상담원 목록 조회
	 */
	public List<Map<String, Object>> selectAvailableCounselorList(List<Map<String, Object>> memberList) {

		return selectAvailableCounselorList(memberList, false, false, false);
	}

	/**
	 * 상담 가능한 상담원 목록 조회
	 */
	public List<Map<String, Object>> selectAvailableCounselorList(List<Map<String, Object>> memberList,
			boolean ignoreBreakTime, boolean onlyCounselor, boolean isSchedeuled) {

		return selectAvailableCounselorList(memberList, ignoreBreakTime, onlyCounselor, null, isSchedeuled);
	}

	/**
	 * 상담 가능한 상담원 목록 조회
	 */
	public List<Map<String, Object>> selectAvailableCounselorList(List<Map<String, Object>> memberList,
			boolean ignoreBreakTime, boolean onlyCounselor, String departCd, boolean isSchedeuled) {

		Map<String, Object> params = new HashMap<>();
		params.put("memberList", memberList);
//		params.put("departCd", departCd);
		if (onlyCounselor) {
			params.put("memberDivCd", MEMBER_DIV_CD_C);
		}
		if (!ignoreBreakTime) {
			params.put("workStatusCd", WORK_STATUS_CD_W);
		}
		if (!isSchedeuled) {
			params.put("isSchedeuled", isSchedeuled);
		}
		return memberDao.selectAvailableCounselorList(params);
	}

	/**
	 * 상담원에게 배정된 채팅방 개수
	 */
	public Integer selectAssignCount(String memberUid) {

		List<Map<String, Object>> memberList = new ArrayList<>();
		Map<String, Object> member = new HashMap<>();
		member.put("member_uid", memberUid);
		memberList.add(member);

		List<Map<String, Object>> result = selectMemberUidAndAssignCount(memberList);
		if (result.isEmpty()) {
			return 0;
		}

		BigDecimal count = (BigDecimal) result.get(0).get("assign_count");
		return count.intValue();
	}

	/**
	 * 상담원별 상담중인 채팅방 개수
	 */
	public Integer selectCounselingCount(String memberUid) {

		List<Map<String, Object>> memberList = new ArrayList<>();
		Map<String, Object> member = new HashMap<>();
		member.put("member_uid", memberUid);
		memberList.add(member);

		List<Map<String, Object>> result = selectMemberUidAndAssignCount(memberList);
		if (result.isEmpty()) {
			return 0;
		}

		BigDecimal count = (BigDecimal) result.get(0).get("counseling_count");
		return count.intValue();
	}

	/**
	 * 상담원별 상담중인 채팅방 개수
	 */
	public List<Map<String, Object>> selectMemberUidAndAssignCount(List<Map<String, Object>> memberList) {

		// 상담자별 할당 채팅방 개수
		Map<String, Object> params = new HashMap<>();
		params.put("memberList", memberList);
		List<Map<String, Object>> result = memberDao.selectMemberUidAndAssignCount(params);
		log.info("selectMemberUidAndAssignCount: {}", result);

		return result;
	}

	/**
	 * 상담원 중 우선순위 상담원 조회
	 */
	List<Map<String, Object>> selectCounselorListByPriority(List<Map<String, Object>> memberList, List<Map<String, Object>> mappingList) {

		log.debug(">>> ASSIGN, AVAILABLE COUNSELORS: {}", memberList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));
		log.debug(">>> ASSIGN, MAPPING LIST: {}", mappingList.stream().map(map -> map.get("name")).collect(Collectors.toSet()));

		List<Map<String, Object>> availableCounselorList = new ArrayList<>();
		for (Map<String, Object> member : memberList) {
			for (Map<String, Object> mappingInfo : mappingList) {
				if (member.get("member_uid").equals(mappingInfo.get("member_uid"))) {
					log.info(">>> ASSIGN, COUNSELOR: {}, PRIORITY: {}", member.get("name"), mappingInfo.get("first_yn"));
					if ("Y".equals(mappingInfo.get("first_yn"))) {
						availableCounselorList.add(member);
						break;
					}
				}
			}
		}

		return availableCounselorList;
	}

	/**
	 * 상담중인 채팅방이 적은 상담원 조회
	 */
	List<Map<String, Object>> selectCounselorListByMinimumAssigned(List<Map<String, Object>> memberList, String chatRoomUid) {

		// 상담원별, 배정된 채팅방 개수
		Map<String, Object> params = new HashMap<>();
		params.put("memberList", memberList);
		List<Map<String, Object>> list = memberDao.selectMemberUidAndAssignCount(params);

		// 상담원이 배정된 채팅방이 없는 경우, 전체 상담원(memberList) 리턴
		if (list.isEmpty()) {
			return memberList;
		}

		// 모든 상담원들 중 가장 적게 배정받은 채팅방 개수
		int minimumCount = Integer.MAX_VALUE;
		for (Map<String, Object> map : list) {
			log.debug(">>> ASSIGN, {}, COUNSELOR: {}, # OF ASSIGNED CHAT ROOM: {}"
					, chatRoomUid, map.get("name"), map.get("assign_count"));
			BigDecimal assignCount = (BigDecimal) map.get("assign_count");
			if (assignCount.intValue() < minimumCount) {
				minimumCount = assignCount.intValue();
			}
		}

		// 가장 적은 채팅방을 가지고 있는 상담원들
		log.info(">>> ASSIGN, {}, TRY FIND COUNSELOR BY MINIMUM COUNT: {}"
				, chatRoomUid, minimumCount);
		List<Map<String, Object>> availableCounselorList = new ArrayList<>();

		for (Map<String, Object> map : list) {

			BigDecimal assignCount = (BigDecimal) map.get("assign_count");
			BigDecimal counselorMaxCounter = (BigDecimal) map.get("cnsr_max_cnt");
			log.info(">>> ASSIGN, {}, COUNSELOR: {}, ASSIGNED: {}"
					, chatRoomUid, map.get("name"), assignCount.intValue());

			// 가장 적은 채팅방을 가지고 있는 상담원에 포함될 경우
			if (assignCount.intValue() == minimumCount) {
				// 할당받은 채팅방이 '배정 가능 최대값 (counselorMaxCounter)' 보다 작은 경우만
				if (assignCount.longValue() < counselorMaxCounter.intValue()) {
					log.info(">>> ASSIGN, {}, ACCEPTED, COUNSELOR: {}, ASSIGNED: {}, MINIMUM: {}, POSSIBLE: {}"
							, chatRoomUid, map.get("name"), assignCount.intValue(), minimumCount, counselorMaxCounter.intValue());

					availableCounselorList.add(map);

				} else {
					log.info(">>> ASSIGN, {}, NOT ACCEPTED, COUNSELOR: {}, ASSIGNED: {}, MINIMUM: {}, POSSIBLE: {}"
							, chatRoomUid, map.get("name"), assignCount.intValue(), minimumCount, counselorMaxCounter.intValue());
				}
			} else {
				log.info(">>> ASSIGN, {}, NOT ACCEPTED, COUNSELOR: {}, ASSIGNED: {}, MINIMUM: {}"
						, chatRoomUid, map.get("name"), assignCount.intValue(), minimumCount);
			}
		}

		// 가능한 상담원이 없을 경우
		if (availableCounselorList.isEmpty()) {
			// 배정 최대 개수내에서 가능한 상담원들
			log.info(">>> ASSIGN, {}, TRY FIND COUNSELOR BY MAXIMUM COUNSELING", chatRoomUid);

			for (Map<String, Object> map : list) {

				BigDecimal assignCount = (BigDecimal) map.get("assign_count");
				BigDecimal counselorMaxCounter = (BigDecimal) map.get("cnsr_max_cnt");
				log.info(">>> ASSIGN, {}, COUNSELOR: {}, ASSIGNED: {}, POSSIBLE: {}"
						, chatRoomUid, map.get("name"), assignCount.intValue(), counselorMaxCounter.intValue());

				if (assignCount.intValue() < counselorMaxCounter.intValue()) {
					log.info(">>> ASSIGN, {}, ACCEPTED, COUNSELOR: {}, ASSIGNED: {}, POSSIBLE: {}"
							, chatRoomUid, map.get("name"), assignCount.intValue(), counselorMaxCounter.intValue());

					availableCounselorList.add(map);

				} else {
					log.info(">>> ASSIGN, {}, NOT ACCEPTED, COUNSELOR: {}, ASSIGNED: {}, POSSIBLE: {}"
							, chatRoomUid, map.get("name"), assignCount.intValue(), counselorMaxCounter.intValue());
				}
			}
		}

		return availableCounselorList;
	}

	/**
	 * 회원 환경설정 저장
	 */
	@Transactional
	public int saveMemberSetting(String memberUid, Map<String, Object> requestParams) {

		// 상담원 휴식시간 이력 저장
		if (requestParams.get("workStatusCd") != null) {
			if (WORK_STATUS_CD_W.equals(requestParams.get("workStatusCd"))) {
				settingService.insertCnsrBreakHis(memberUid, memberUid, WORK_STATUS_CD_W);
			} else {
				settingService.insertCnsrBreakHis(memberUid, memberUid, WORK_STATUS_CD_R);
			}
		}
		return memberDao.saveMemberSetting(requestParams);
	}

	/**
	 * 임시 회원 정보 등록
	 */
	@Transactional
	public Map<String, Object> insertTempMember(File memberFile, File memberFileFp) throws Exception {
		Map<String, Object> rtnMap = new HashMap<>();

		int insertCnt = 0;
		int errorCnt = 0;

		BufferedReader bufReader = null;
		BufferedReader bufReaderFp = null;

		try {
			bufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(memberFile), StandardCharsets.UTF_8));
			String line;
			String[] lineArr;
			String id;
			String name;
			String departCd;
			String departNm;

			// 회원 정보 등록
			Map<String, Object> map;
			while ((line = bufReader.readLine()) != null) {
				try {
					lineArr = line.split(Pattern.quote("|"));
					if (lineArr.length != 1) {
						if (lineArr.length == 4) {
							id = lineArr[0].trim();
							name = lineArr[1].trim();
							departCd = lineArr[2].trim();
							departNm = lineArr[3].trim();

							map = new HashMap<>();
							map.put("id", id);
							map.put("name", name);
							map.put("departCd", departCd);
							map.put("departNm", departNm);

							if (id.startsWith("099")) {
								map.put("cstmDivCd", CommonConstants.CSTM_DIV_CD_MEMBER);
							} else {
								map.put("cstmDivCd", CommonConstants.CSTM_DIV_CD_FP);
							}

							memberDao.insertTempMember(map);
							insertCnt++;
						} else {
							errorCnt++;
						}
					}
				} catch (Exception e1) {
					log.error(e1.getMessage());
					errorCnt++;
				}
			}

			rtnMap.put("insertCnt", insertCnt);
			rtnMap.put("errorCnt", errorCnt);

			// 기존 임시 회원 삭제
			memberDao.deleteTempMember();

			// 신규 임시 회원 사용으로 변경
			memberDao.updateTempMember();

			// FP 목록 등록
			bufReaderFp = new BufferedReader(
					new InputStreamReader(new FileInputStream(memberFileFp), StandardCharsets.UTF_8));
			String lineFp;

			memberDao.deleteFpTempMemberInfo();
			Map<String, Object> mapFp;
			while ((lineFp = bufReaderFp.readLine()) != null) {
				try {
					lineFp = lineFp.replaceAll(" ", "");
					if (!"".equals(lineFp)) {
						mapFp = new HashMap<>();
						mapFp.put("id", lineFp);

						memberDao.insertFpTempMember(mapFp);
					}
				} catch (Exception e1) {
					// e1.printStackTrace();
					log.error(e1.getMessage());
				}
			}

			// FP목록 외 삭제
			int delFp = memberDao.deleteFpTempMember();
			rtnMap.put("delFp", delFp);

		} catch (FileNotFoundException fe) {
			//fe.printStackTrace();
			throw new BizException("회원 정보 일배치 : 파일 미존재");
		} catch (IOException ie) {
			//ie.printStackTrace();
			throw new BizException("회원 정보 일배치 : 파일 읽기 오류");
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
			throw new BizException("회원 정보 일배치 : 처리중 오류");
		} finally {
			if (bufReader != null) {
				bufReader.close();
			}
			if (bufReaderFp != null) {
				bufReaderFp.close();
			}
		}

		return rtnMap;
	}

	/**
	 * 임시 회원 정보 등록(상담원)
	 */
	@Transactional
	public Map<String, Object> insertTempMemberCnsr(File memberFile) throws Exception {
		Map<String, Object> rtnMap = new HashMap<>();

		int index = 0;
		int insertCnt = 0;
		int errorCnt = 0;

		BufferedReader bufReader = null;

		try {
			bufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(memberFile), "EUC-KR"));
			String line;
			String[] lineArr;
			String id;
			String name;
			String departCd;
			String departNm;

			Map<String, Object> map;
			while ((line = bufReader.readLine()) != null) {
				try {
					if (index > 0) {
						lineArr = line.split(Pattern.quote("$"));
						if (lineArr.length != 1) {
							if (lineArr.length > 5) {
								id = lineArr[0].trim();
								name = lineArr[1].trim();
								departCd = lineArr[3].trim();
								departNm = lineArr[4].trim();

								map = new HashMap<>();
								map.put("id", id);
								map.put("name", name);
								map.put("departCd", departCd);
								map.put("departNm", departNm);
								map.put("cstmDivCd", CommonConstants.CSTM_DIV_CD_CNSR);

								memberDao.insertTempMember(map);
								insertCnt++;
							} else {
								errorCnt++;
							}
						}
					}
				} catch (Exception e1) {
					log.error(e1.getMessage());
					errorCnt++;
				}
				index++;
			}
			bufReader.close();

			rtnMap.put("insertCnt", insertCnt);
			rtnMap.put("errorCnt", errorCnt);

			// 기존 임시 회원 삭제
			// memberDao.deleteTempMember();

			// 신규 임시 회원 사용으로 변경
			// memberDao.updateTempMember();

		} catch (FileNotFoundException fe) {
			//fe.printStackTrace();
			throw new BizException("회원 정보 일배치 : 파일 미존재");
		} catch (IOException ie) {
			//ie.printStackTrace();
			throw new BizException("회원 정보 일배치 : 파일 읽기 오류");
		} catch (Exception e) {
			log.error("{}", e.getLocalizedMessage(), e);
			throw new BizException("회원 정보 일배치 : 처리중 오류");
		} finally {
			if (bufReader != null) {
				bufReader.close();
			}
		}

		return rtnMap;
	}

	/**
	 * 회원 정보 변경
	 */
	@Transactional
	public void updateMember(String systemUser) {
		// 회원 탈퇴 처리 - 신규 DB에 존재하지 않는 회원은 탈퇴 처리
		memberDao.updateMemberLeaveAuto(systemUser);

		// 회원 미인증 처리 - 부서 및 이름 변경 시 미인증 처리
		memberDao.updateMemberValidAuto(systemUser);

		// 회원 수정 - 부서명만 변경시 update
		memberDao.updateMemberDepartNm(systemUser);
	}

	/**
	 * 로그인 여부 30일 이상 체크
	 */
	@Transactional
	public void loginMemberCheck() {
		String member_uid="";
		List<Map<String, Object>> retMap = memberDao.selectLoginMemberCheck();
		for (Map<String, Object> data : retMap) {
			member_uid = (String) data.get("member_uid");
			memberDao.updateLoginMemberCheck(member_uid);
		}
	}

	/**
	 * 기간계 회원 (TB_TEMP_MEMBER_INFO) 조회
	 */
	public Map<String, Object> selectKyoboMember(String id) {

		// TODO: DELETEME, 테스트 계정 예외처리
		if (id != null && id.startsWith("0000000")) {
			Map<String, Object> user = new HashMap<>();
			user.put("id", id);
			user.put("name", "테스트 사용자");
			user.put("cstm_div_cd", "MEMBER");
			return user;
		}
		// TODO: DELETEME, 테스트 계정 예외처리

		Map<String, Object> params = new HashMap<>();
		params.put("id", id);

		return memberDao.selectTmpMember(params);
	}

	/**
	 * 상담원 배정 재검증용
	 */
	public Map<String, Object> selectCountRoomByCounselor(Map<String, Object> param) {

		return memberDao.selectCountRoomByCounselor(param);
	}

	/**
	 * 고객별 채팅방 목록 카운트
	 */
	public int selectChatRoomEndMemoEmptyCount(String memberUid) {

		Map<String, Object> params = new HashMap<>();
		params.put("member_uid", memberUid);
		int count = 0;
		try {
			count = memberDao.selectChatRoomEndMemoEmptyCount(params);
		}catch(Exception e) {
			log.error("selectChatRoomEndMemoEmptyCount:{}", e.getLocalizedMessage(), e);
		}

		return count;
	}
	/**
	 * 카카오 고객 인증 정보 삭제 (테스트용)
	 */
	public int deleteCustomerKakao() {

		return memberDao.deleteCustomerKakao();
	}
}
