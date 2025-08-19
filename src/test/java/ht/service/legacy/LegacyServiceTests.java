package ht.service.legacy;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ht.domain.ChatRoom;
import ht.persistence.ChatDao;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles({"local"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LegacyServiceTests {

	@Resource
	private ChatDao chatDao;
//	@Resource
//	private LegacyService legacyService;

	@Test
	public void testPostEndChatRoom() {

			ChatRoom chatRoom = new ChatRoom();
			chatRoom.setChatRoomUid("bb1759d9-118b-483d-8284-f877dbffffc6");

//			chatRoom = legacyService.postEndChatRoom(chatRoom);
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> LEGACY >>>>>>>>>>>>>>>> chatRoom: {}", chatRoom);
			assertThat(chatRoom).isNotNull();
			assertThat(chatRoom.getEndCtgCd1()).isNotEmpty();
			assertThat(chatRoom.getEndCtgCd2()).isNotEmpty();
			assertThat(chatRoom.getEndCtgCd3()).isNotEmpty();
			assertThat(chatRoom.getEndCtgCd4()).isNotEmpty();
			assertThat(chatRoom.getEndCtgNm1()).isNotEmpty();
			assertThat(chatRoom.getEndCtgNm2()).isNotEmpty();
			assertThat(chatRoom.getEndCtgNm3()).isNotEmpty();
			assertThat(chatRoom.getEndCtgNm4()).isNotEmpty();
	}
//
//	@Test
//	public void testGetToken() {
//
//		LegacyAuthTokenResponse response = legacyService.getToken("mobile", "RANDOM_CI");
//		assertThat(response).isNotNull();
//		assertThat(response.getTokenBody().getToken()).isNotEmpty();
//	}
//
//	@Test
//	public void testGetCert() {
//
//		LegacyAuthTokenResponse response = legacyService.getCert("mobile", "RANDOM_TOKEN", "RAMDOM_CI");
//		assertThat(response).isNotNull();
//		assertThat(response.getCertBody()).isNotNull();
//	}
}
