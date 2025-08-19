package ht.controller;

import static ht.constants.CommonConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import ht.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import ht.config.CustomProperty;
import ht.domain.ChatRoom;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestChatControllerTests {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private TestRestTemplate restTemplate;
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

	@Before
	public void before() throws Exception {
		// 사이트 세팅
		siteSetting = settingService.selectSiteSetting();
		// 대외 고객
		cstmUser.put("cstmUid", customerService.createCstmUid());
		cstmUser.put("cstmDivCd", CSTM_DIV_CD_CSTM);
		cstmUser.put("name", "단위테스트");
		customerService.saveCustomer(cstmUser);
		cstmUser = customerService.selectCustomerByCstmUid((String) cstmUser.get("cstmUid"));
		// FP 고객
		fpUser.put("cstmUid", customerService.createCstmUid());
		fpUser.put("cstmDivCd", CSTM_DIV_CD_FP);
		fpUser.put("name", "단위테스트");
		fpUser.put("cocId", "cstm_fp");
		customerService.saveCustomer(fpUser);
		fpUser = customerService.selectCustomerByCstmUid((String) fpUser.get("cstmUid"));

		// 카테고리 깊이에 따라 기본 카테고리 값이 변경
		String ctgNum = (String) categoryService.selectDefaultCategory(DEPART_CD_CS).get("ctg_num");
//		String ctgMgtDpt = StringUtil.nvl(siteSetting.get("ctg_mgt_dpt"));
		//		if (ctgMgtDpt.equals("3")) {
		//			ctgNum = customProperty.getDefaultCategory3Seq();
		//		} else {
		//			ctgNum = customProperty.getDefaultCategory2Seq();
		//		}

		// 대외 고객 채팅방
		cstmChatRoom = chatRoomService.createChatRoom(cstmUser, DEPART_CD_NONE, ctgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		cstmChatRoom = assignService.assignChatRoom(cstmChatRoom, siteSetting, true, false);
		// FP 고객 채팅방
		fpChatRoom = chatRoomService.createChatRoom(fpUser, DEPART_CD_NONE, ctgNum, siteSetting, null, CSTM_OS_DIV_CD_MOBILE);
		fpChatRoom = assignService.assignChatRoom(fpChatRoom, siteSetting, true, false);
	}

	@After
	public void after() {
		customerService.deleteCustomer((String) cstmUser.get("cstm_uid"));
		customerService.deleteCustomer((String) fpUser.get("cstm_uid"));
		chatRoomService.deleteChatRoom(cstmChatRoom.getChatRoomUid());
		chatRoomService.deleteChatRoom(fpChatRoom.getChatRoomUid());
	}

	@Test
	public void getChatRoom() {

		// 대외 고객 채팅방
		ResponseEntity<ChatRoom> responseCstmChatRoom = restTemplate.getForEntity(
				"/api/chat/room/" + cstmChatRoom.getChatRoomUid(),
				ChatRoom.class);
		assertThat(responseCstmChatRoom.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseCstmChatRoom.getBody()).isNotNull();
		assertThat(responseCstmChatRoom.getBody().getChatRoomUid()).isEqualTo(cstmChatRoom.getChatRoomUid());
		assertThat(responseCstmChatRoom.getBody().getCstmUid()).isEqualTo(cstmUser.get("cstm_uid"));
		// 대외 고객은 상담직원에 배정됨
		assertThat(responseCstmChatRoom.getBody().getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_C);

		// FP 고객 채팅방
		ResponseEntity<ChatRoom> responseFpChatRoom = restTemplate.getForEntity(
				"/api/chat/room/" + fpChatRoom.getChatRoomUid(),
				ChatRoom.class);
		assertThat(responseFpChatRoom.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseFpChatRoom.getBody()).isNotNull();
		assertThat(responseFpChatRoom.getBody().getChatRoomUid()).isEqualTo(fpChatRoom.getChatRoomUid());
		assertThat(responseFpChatRoom.getBody().getCstmUid()).isEqualTo(fpUser.get("cstm_uid"));
		// 챗봇 사용 여부
		if (settingService.isUseChatbot(siteSetting, fpChatRoom.getCstmLinkDivCd().toString())) {
			// 'Y'일때, FP 고객은 로봇에 배정됨
			assertThat(responseFpChatRoom.getBody().getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_R);
		} else {
			// 'N'일때, FP 고객은 상담직원에 배정됨
			assertThat(responseFpChatRoom.getBody().getCnsrDivCd()).isEqualTo(CNSR_DIV_CD_C);
		}
	}
}
