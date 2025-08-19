package ht.service.chatbot;

import com.google.common.base.Strings;
import ht.config.CustomProperty;
import ht.domain.ChatMessage;
import ht.domain.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static ht.constants.CommonConstants.*;

@Service("CategoryBotServiceImpl")
@Slf4j
public class CategoryBotServiceImpl implements ChatBotService {

	public static final String BOT_NAME = "CategoryBot";

	@Resource
	private CustomProperty customProperty;
	@Resource
	private CategoryBotParser parser;

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
		log.debug("MOCK CATEGORY BOT");
		ChatMessage responseChatMessage = new ChatMessage();
		responseChatMessage.setChatRoomUid(requestChatMessage.getChatRoomUid());
		responseChatMessage.setSenderUid(customProperty.getCategoryBotMemeberUid());
		responseChatMessage.setSenderDivCd(MEMBER_DIV_CD_R);
		responseChatMessage.setType(ChatMessage.ChatMessageType.MESSAGE);
		responseChatMessage.setMsgStatusCd(MSG_STATUS_CD_SEND);
		responseChatMessage.setContDivCd(CONT_DIV_CD_T);
		// 채팅 메세지에 상담원 세팅
		responseChatMessage.setCnsrMemberUid(chatRoom.getMemberUid());
		// 인텐트
		responseChatMessage.setBotIntent("CATEGORY");
		// 컨텐츠
		responseChatMessage.setCont(parser.parse(requestChatMessage.getCont()));
		// 캐릭터
		responseChatMessage.setAvatarCd(null);
		// 프로젝트 ID
		chatRoom.setBotProjectId("CATEGORY_BOT");
		// 세션 ID
		if (!Strings.isNullOrEmpty(chatRoom.getBotSessionKey())) {
			chatRoom.setBotSessionKey(chatRoom.getBotSessionKey());
		} else {
			chatRoom.setBotSessionKey(UUID.randomUUID().toString());
		}

		log.debug("RESPONSE MESSAGE FROM ROBOT: {}", responseChatMessage);
		return Collections.singletonList(responseChatMessage);
	}

	@Override
	public void endSession(ChatRoom chatRoom)
			throws Exception
	{
		log.info("END SESSION: MOCK CATEGORY BOT");
	}
}
