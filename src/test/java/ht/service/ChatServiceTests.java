package ht.service;

import ht.constants.CommonConstants;
import ht.domain.ChatMessage;
import ht.domain.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("local")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChatServiceTests {

	@Resource
	private SettingService settingService;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;

	private Map<String, Object> siteSetting;

	@Before
	public void before() {

		siteSetting = settingService.selectSiteSetting();
	}

	@Test
	@Transactional
	public void addGreetMessage() throws Exception {

		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid("6dfb2687-d3c1-4bcd-bbd9-50152482952a");
		List<ChatMessage> chatMessageList = chatService.addGreetMessage(chatRoom, siteSetting, CommonConstants.CNSR_DIV_CD_R, false);
		for (ChatMessage chatMessage : chatMessageList) {
			log.info("GREETING MESSAGE: {}", chatMessage);
		}
	}
}
