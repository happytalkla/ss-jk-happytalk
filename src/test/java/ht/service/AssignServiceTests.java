package ht.service;

import static ht.constants.CommonConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ht.config.CustomProperty;
import ht.domain.ChatMessage;
import ht.domain.ChatRoom;
import ht.domain.ChatRoom.AssignResult;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles({"local"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssignServiceTests {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private AssignService assignService;
	@Resource
	private SettingService settingService;
	@Resource
	private CustomerService customerService;
	@Resource
	private CategoryService categoryService;

	// 사이트 세팅
	private Map<String, Object> siteSetting;
	// 대외 고객
	private Map<String, Object> cstmUser = new HashMap<>();
	// FP 고객
	private Map<String, Object> fpUser = new HashMap<>();
	// 대외 고객 채팅방
	private ChatRoom cstmChatRoom;
	// FP 고객 채팅방
	private ChatRoom fpChatRoom;
	// 카테고리
	private String defaultCtgNum;

	@Before
	public void before() {

		// 사이트 세팅
		siteSetting = settingService.selectSiteSetting();

		// 대외 고객
//		cstmUser = customerService.selectCustomerByCocId("ut_cstm");
//		log.info("<<<");
//		if (cstmUser == null) {
//			cstmUser = customerService.createCustomer(CSTM_DIV_CD_CSTM, null, null, null, "cstm", "UT_CSTM", null, null, null);
//			log.info("<<<");
//			cstmUser = customerService.selectCustomerByCstmUid((String) cstmUser.get("cstm_uid"));
//		}
//		cstmUser = customerService.selectCustomerByCstmUid((String) cstmUser.get("cstm_uid"));
//
//		// FP 고객
//		fpUser = customerService.selectCustomerByCocId("ut_fp");
//		log.info(">>>");
//		if (fpUser == null) {
//			fpUser = customerService.createCustomer(CSTM_DIV_CD_FP, null, null, null, "fp", "UT_FP", null, null, null);
//			log.info(">>>");
//			fpUser = customerService.selectCustomerByCstmUid((String) fpUser.get("cstm_uid"));
//		}

		// 상담직원1
		// memberService.selectCounselorList

		// 상담직원2

		// 카테고리 깊이에 따라 기본 카테고리 값이 변경
		//		long maxDepth = settingService.getMaxCategoryDepth(siteSetting);
		//		if (maxDepth == 3L) {
		//			defaultCtgNum = customProperty.getDefaultCategory3Seq();
		//		} else {
		//			defaultCtgNum = customProperty.getDefaultCategory2Seq();
		//		}
		categoryService.selectDefaultCategory(DEPART_CD_CS);
	}

	@After
	public void after() {
		customerService.deleteCustomer((String) cstmUser.get("cstm_uid"));
		customerService.deleteCustomer((String) fpUser.get("cstm_uid"));
	}

	@Test
	public void 테스트() {

//		Map<String, Object> customer = customerService.createCustomer(CSTM_DIV_CD_FP, null, null, null, null, "UTEST_FP", null, null, null);
//		log.warn("customer: {}", customer);
	}

	@Test
	public void 챗봇_배정() throws Exception {

		// ////////////////////////////////////////////////////////////////////
		// '챗봇 사용 여부' 사용 일 때
		siteSetting.put("chatbot_use_yn", "Y");

		// FP 고객이 채팅방을 생성하면
		fpChatRoom = chatRoomService.createChatRoom(fpUser, DEPART_CD_NONE, defaultCtgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		fpChatRoom = assignService.assignChatRoom(fpChatRoom, siteSetting);

		// 로봇에 배정됨
		assertThat(fpChatRoom.getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_R);

		chatRoomService.deleteChatRoom(fpChatRoom.getChatRoomUid());
		chatService.deleteChatMessage(fpChatRoom.getChatRoomUid());

		// ////////////////////////////////////////////////////////////////////
		// '챗봇 사용 여부' 사용안함 일 때
		siteSetting.put("chatbot_use_yn", "N");

		// FP 고객이 채팅방을 생성하면
		fpChatRoom = chatRoomService.createChatRoom(fpUser, DEPART_CD_NONE, defaultCtgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		fpChatRoom = assignService.assignChatRoom(fpChatRoom, siteSetting);

		// 상담직원 배정됨
		assertThat(fpChatRoom.getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_C);

		chatRoomService.deleteChatRoom(fpChatRoom.getChatRoomUid());
		chatService.deleteChatMessage(fpChatRoom.getChatRoomUid());
	}

	@Test
	public void 자동_상담직원_배정() throws Exception {

		// ////////////////////////////////////////////////////////////////////
		// '자동 배정 여부' 사용 일 때
		siteSetting.put("auto_mat_use_yn", "Y");
		// '챗봇 사용 여부' 사용 일 때
		siteSetting.put("chatbot_use_yn", "Y");

		// 대외고객이 채팅방을 생성하면
		cstmChatRoom = chatRoomService.createChatRoom(cstmUser, DEPART_CD_NONE, defaultCtgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		cstmChatRoom = assignService.assignChatRoom(cstmChatRoom, siteSetting);

		// 상담직원에 배정됨
		assertThat(cstmChatRoom.getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_C);
		// 기본 상담직원이 아님
		assertThat(cstmChatRoom.getMemberUid()).isNotEqualTo(customProperty.getDefaultAssignMemeberUid());

		chatRoomService.deleteChatRoom(cstmChatRoom.getChatRoomUid());
		chatService.deleteChatMessage(cstmChatRoom.getChatRoomUid());
	}

	@Test
	public void 수동_상담직원_배정() throws Exception {

		// ////////////////////////////////////////////////////////////////////
		// '자동 배정 여부' 사용 일 때
		siteSetting.put("auto_mat_use_yn", "N");
		// '챗봇 사용 여부' 사용 일 때
		siteSetting.put("chatbot_use_yn", "Y");

		// 대외고객이 채팅방을 생성하면
		cstmChatRoom = chatRoomService.createChatRoom(cstmUser, DEPART_CD_NONE, defaultCtgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		cstmChatRoom = assignService.assignChatRoom(cstmChatRoom, siteSetting);

		// 상담직원에 배정됨
		assertThat(cstmChatRoom.getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_C);
		// 기본 상담직원으로 배정됨
		assertThat(cstmChatRoom.getMemberUid()).isEqualTo(customProperty.getDefaultAssignMemeberUid());

		chatRoomService.deleteChatRoom(cstmChatRoom.getChatRoomUid());
		chatService.deleteChatMessage(cstmChatRoom.getChatRoomUid());
	}

	@Test
	public void 근무시간외_배정() throws Exception {

		// ////////////////////////////////////////////////////////////////////
		// '자동 배정 여부' 사용 일 때
		siteSetting.put("auto_mat_use_yn", "N");
		// '챗봇 사용 여부' 사용 일 때
		siteSetting.put("chatbot_use_yn", "Y");

		// 대외고객이 채팅방을 생성하면
		cstmChatRoom = chatRoomService.createChatRoom(cstmUser, DEPART_CD_NONE, defaultCtgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		cstmChatRoom = assignService.assignChatRoom(cstmChatRoom, siteSetting);

		// 상담직원에 배정됨
		assertThat(cstmChatRoom.getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_C);
		// 기본 상담직원으로 배정됨
		assertThat(cstmChatRoom.getMemberUid()).isEqualTo(customProperty.getDefaultAssignMemeberUid());

		chatRoomService.deleteChatRoom(cstmChatRoom.getChatRoomUid());
		chatService.deleteChatMessage(cstmChatRoom.getChatRoomUid());
	}

	@Test
	public void addChatMessage() throws Exception {

		cstmChatRoom = chatRoomService.createChatRoom(cstmUser, DEPART_CD_NONE, defaultCtgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		cstmChatRoom = assignService.assignChatRoom(cstmChatRoom, siteSetting);
		ChatMessage chatMessage = chatService.saveChatMessage(cstmChatRoom, ChatMessage.buildChatMessageText("HELLO"));
		assertThat(chatMessage.getSenderUid()).isEqualTo(cstmChatRoom.getMemberUid());

		chatRoomService.deleteChatRoom(cstmChatRoom.getChatRoomUid());
		chatService.deleteChatMessage(chatMessage.getChatNum());
	}

	@Test
	public void 상담직원_배정() throws Exception {

		// ////////////////////////////////////////////////////////////////////
		// 사이트 세팅
		siteSetting.put("cnsr_max_cnt", BigDecimal.valueOf(5L));
		siteSetting.put("auto_mat_use_yn", "Y");
		siteSetting.put("ctg_mapping_use_yn", "Y");
		siteSetting.put("chatbot_use_yn", "N");
		siteSetting.put("cnsr_once_max_cnt", "10");
		siteSetting.put("unsocial_accept_yn", "Y");
		siteSetting.put("ctg_mgt_dpt", BigDecimal.valueOf(3L));

		log.info("getCounselorMaxCount: {}", settingService.getCounselorMaxCount(siteSetting));

		final String ctgNum = "365";

		// 대외고객이 채팅방을 생성하면
		cstmChatRoom = chatRoomService.createChatRoom(cstmUser, DEPART_CD_CS, ctgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		AssignResult assignResult = assignService.assignCounselorByCustomer(cstmChatRoom, siteSetting, "C");

		// 상담직원에 배정됨
		assertThat(assignResult).isEqualTo(AssignResult.SUCCEED);
		assertThat(cstmChatRoom.getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_C);
		// 기본 상담직원으로 배정됨
		assertThat(cstmChatRoom.getMemberUid()).isNotNull();

		chatRoomService.deleteChatRoom(cstmChatRoom.getChatRoomUid());
		chatService.deleteChatMessage(cstmChatRoom.getChatRoomUid());
	}

	// 조건
	// - 근무시간: 09:00~18:00
	// - 휴무일/근무 관리 (총2명)
	// - 슈퍼유저: 상담가능, 상담가능, 09:00~18:00
	// - 김동출: 상담불가, 상담가능, 09:00~18:00
}
