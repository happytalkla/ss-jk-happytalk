package ht.service.chatbot;

import com.google.common.base.Strings;
import ht.config.CustomProperty;
import ht.domain.ChatMessage;
import ht.domain.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static ht.constants.CommonConstants.*;

@Service("MockChatBotServiceImpl")
@Slf4j
public class MockChatBotServiceImpl implements ChatBotService {

	public static final String BOT_NAME = "HappyBot";

	@Resource
	private CustomProperty customProperty;
	@Resource
	private MockChatBotParser parser;

	@Override
	public List<ChatMessage> chatWithRobot(ChatRoom chatRoom, ChatMessage requestChatMessage, String type)
			throws Exception
	{
		return chatWithRobot(chatRoom, requestChatMessage, type, null);
	}

	@Override
	public List<ChatMessage> chatWithRobot(ChatRoom chatRoom, ChatMessage requestChatMessage, String type, Integer page)
			throws Exception
	{
		log.debug("MOCK CHAT BOT");
		ChatMessage responseChatMessage = new ChatMessage();
		responseChatMessage.setType(ChatMessage.ChatMessageType.MESSAGE);
		responseChatMessage.setChatRoomUid(requestChatMessage.getChatRoomUid());
		responseChatMessage.setSenderUid(customProperty.getMockChatBotMemeberUid());
		responseChatMessage.setSenderDivCd(MEMBER_DIV_CD_R);
		responseChatMessage.setMsgStatusCd(MSG_STATUS_CD_SEND);
		responseChatMessage.setContDivCd(CONT_DIV_CD_T);
		// 채팅 메세지에 상담원 세팅
		responseChatMessage.setCnsrMemberUid(chatRoom.getMemberUid());
		// 인텐트
		responseChatMessage.setBotIntent("MOCK_INTENT");
		// 컨텐츠
		responseChatMessage.setCont(parser.parse("MOCK CONTENTS"));
		// 캐릭터
		responseChatMessage.setAvatarCd(null);
		// 프로젝트 ID
		chatRoom.setBotProjectId("MOCK_CHAT_BOT");
		// 세션 ID
		if (!Strings.isNullOrEmpty(chatRoom.getBotSessionKey())) {
			chatRoom.setBotSessionKey(chatRoom.getBotSessionKey());
		} else {
			chatRoom.setBotSessionKey(UUID.randomUUID().toString());
		}

		log.info("RESPONSE MESSAGE FROM ROBOT: {}", responseChatMessage);
		return Collections.singletonList(responseChatMessage);
	}

	@Override
	public void endSession(ChatRoom chatRoom)
			throws Exception
	{
		log.info("END SESSION: MOCK CHATBOT");
	}
}
