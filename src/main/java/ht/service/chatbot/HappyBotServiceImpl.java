package ht.service.chatbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.domain.ChatContents;
import ht.domain.ChatContents.ActionType;
import ht.domain.ChatContents.Section;
import ht.domain.ChatContents.Section.SectionType;
import ht.domain.ChatMessage;
import ht.domain.ChatRoom;
import ht.domain.chatbot.ChatBotRequestMessageType;
import ht.service.ChatRoomService;
import ht.service.CustomerService;
import ht.service.SettingService;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.Valid;

import static ht.constants.CommonConstants.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("HappyBotServiceImpl")
@Slf4j
public class HappyBotServiceImpl implements ChatBotService {

	public static final String BOT_NAME = "HappyBot";
	public static final String API_PATH_EXTRA = "/block/extra";
	public static final String API_PATH_GREETING = "/block/greeting";
	public static final String API_PATH_TRIGGER_CODE = "/block/by-trigger-code";
	public static final String API_PATH_KEYWORD = "/block/by-keyword";
	public static final String API_PATH_FILE = "/block/by-file";
	public static final String API_PATH_SIMULATION_BASE = "/simulation";

	@Resource(name = "restTemplate")
	private RestTemplate restTemplate;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private CustomerService customerService;
	@Resource
	private SettingService settingService;
	@Resource
	private HappyBotService happyBotService;
	@Resource
	private ChatRoomService chatRoomService;

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
		log.info("HAPPY BOT, TYPE: {}, CHAT MESSAGE: {}", type, requestChatMessage);

		//String channel = "?channel="+URLEncoder.encode(chatRoom.getCstmLinkDivCd(), StandardCharsets.UTF_8.name());

		Map<String, Object>  goodInfo = chatRoomService.selectChatRoomGoodCode(chatRoom.getChatRoomUid());
		log.info("************************************ : " + goodInfo);


		List<ChatMessage> chatMessageList = new ArrayList<>();
		String addparam = "?";
		if(!ObjectUtils.isEmpty(goodInfo)) {
			if (!ObjectUtils.isEmpty(goodInfo.get("good_code"))) { //
				String goodCode = StringUtil.nvl(goodInfo.get("good_code").toString());
				addparam +=  "&goodCode=" + URLEncoder.encode(goodCode, StandardCharsets.UTF_8.name());
				log.info("HAPPY BOT, goodCode: {}, ", goodCode);
			}
			if (!ObjectUtils.isEmpty(goodInfo.get("app_code"))) { //
				String appCode = StringUtil.nvl(goodInfo.get("app_code").toString());
				addparam +=  "&appCode=" + URLEncoder.encode(appCode, StandardCharsets.UTF_8.name());
				log.info("HAPPY BOT, appCode: {}, ", appCode);
			}
		}

		String sCode = null;
		if (CSTM_LINK_DIV_CD_A.equals(chatRoom.getCstmLinkDivCd()))
			sCode = "website";
		else if (CSTM_LINK_DIV_CD_B.equals(chatRoom.getCstmLinkDivCd()))
			sCode = "kakao";
		else if (CSTM_LINK_DIV_CD_C.equals(chatRoom.getCstmLinkDivCd()))
			sCode = "default";
		else if (CSTM_LINK_DIV_CD_D.equals(chatRoom.getCstmLinkDivCd()))
			sCode = "mpopapp";
		/*
		 * IPCC_MCH ARS 채널 추가
		 */
		else if (CSTM_LINK_DIV_CD_E.equals(chatRoom.getCstmLinkDivCd()))
			sCode = "ars";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("API-Key", customProperty.getHappyBotApiKey());
		headers.add("Session-Id", chatRoom.getChatRoomUid());
		headers.add("Scenario-Code", StringUtil.nvl(sCode));
//		headers.add("Scenario-Code", "default");
		headers.add("channel", chatRoom.getCstmLinkDivCd());
		HttpEntity<String> request = new HttpEntity<>(headers);

		String cont = requestChatMessage.getCont();
		ChatContents chatContents = new ChatContents();
		if (!Strings.isNullOrEmpty(cont)) {
			chatContents = objectMapper.readValue(cont, ChatContents.class);
		}

		String requestUrl = customProperty.getHappyBotApiUrl();
		log.info("TITLE: {}", chatRoom.getRoomTitle());
		if (!StringUtil.isEmpty(chatRoom.getRoomTitle()) && chatRoom.getRoomTitle().startsWith("SIMULATION")) {
			requestUrl += API_PATH_SIMULATION_BASE;
		}

		if (ChatBotRequestMessageType.GREETING.name().equals(type)) {
			if (!Strings.isNullOrEmpty(chatRoom.getEntranceCode())) { // 진입코드가 있을 경우
				requestUrl += API_PATH_TRIGGER_CODE + "/" + chatRoom.getEntranceCode();
			} else {
				requestUrl += API_PATH_GREETING;
			}
			requestUrl += addparam;
		} else if (!Strings.isNullOrEmpty(chatContents.getExtra())) {
			requestUrl += API_PATH_EXTRA + "/" + URLEncoder.encode(chatContents.getExtra() + addparam, StandardCharsets.UTF_8.name());
		} else {
			if (chatContents.isFileOnlyMessage()) {
				requestUrl += API_PATH_FILE;
			} else {
				// TODO: 메세지 내용으로 챗봇 호출 (형태소 분석기 필요)
				//				throw new NotImplementedException("NOT IMPLEMENTED");
				String keyword = chatContents.getSummary();
				// 텍스트 메세지가 없을 경우 (ex, 카카오 상담톡을 통해 고객이 이미지를 보냈을 경우)
				if (Strings.isNullOrEmpty(keyword)) {
					keyword = "텍스트 없음";
				}
				requestUrl += API_PATH_KEYWORD + "/" + URLEncoder.encode(keyword, StandardCharsets.UTF_8.name());
			}

		}

		try {
			log.info("HAPPY BOT MESSAGE, HEADER: {}", headers.toString());
			log.info("HAPPY BOT MESSAGE, requestUrl: {}", requestUrl);

			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, request, String.class);
			log.debug("HAPPY BOT MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());

			List<ChatContents> responseChatContentsList = Arrays.asList(objectMapper.readValue(responseEntity.getBody(), ChatContents[].class));
			for (ChatContents responseChatContents : responseChatContentsList) {
				chatMessageList.add(getChatMessage(chatRoom, responseChatContents));
				log.debug("RESPONSE MESSAGE FROM ROBOT: {}", chatMessageList);
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.info("{}, REQUUEST MESSAGE: {}", e.getLocalizedMessage(), requestChatMessage);
		}

		// 인사말 제외한 첫 인텐트 저장
		if (Strings.isNullOrEmpty(chatRoom.getFirstBotIntent())) {
			for (ChatMessage chatMessage : chatMessageList) {
				if (!Strings.isNullOrEmpty(chatMessage.getBotIntent())
						&& !"GREETING".equals(chatMessage.getBotIntent())) {
					chatRoom.setFirstBotIntent(chatMessage.getBotIntent());
					break;
				}
			}
		}

		// 폴백 메세지
		if (chatMessageList.isEmpty()) {
			Map<String, Object> siteSetting = settingService.selectSiteSetting();
			chatContents = new ChatContents();
			chatContents.add(Section.builder()
					.type(SectionType.text)
					.data((String) siteSetting.get("bot_cstm_err_msg"))
					.build());
			chatContents = buildScenarioList(chatContents);
			chatContents.setIntent("SYSTEM_FALLBACK");
			chatMessageList.add(getChatMessage(chatRoom, chatContents));
		}

		return chatMessageList;
	}

	private ChatMessage getChatMessage(ChatRoom chatRoom, ChatContents chatContents) throws Exception {

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setType(ChatMessage.ChatMessageType.MESSAGE);
		chatMessage.setChatRoomUid(chatRoom.getChatRoomUid());
		chatMessage.setSenderUid(customProperty.getHappyBotMemeberUid());
		chatMessage.setSenderDivCd(MEMBER_DIV_CD_R);
		chatMessage.setMsgStatusCd(MSG_STATUS_CD_SEND);
		chatMessage.setContDivCd(CONT_DIV_CD_T);
		chatMessage.setCnsrMemberUid(chatRoom.getMemberUid()); // 상담원
		chatMessage.setAvatarCd(null); // 캐릭터
		chatMessage.setCont(objectMapper.writeValueAsString(chatContents)); // 컨텐츠
		chatMessage.setBotIntent(chatContents.getIntent()); // 인텐트
		chatRoom.setBotProjectId("HappyBot"); // 프로젝트 ID

		return chatMessage;
	}

	/*
	public List<ChatContents> skill(@NotNull ChatRoom chatRoom, @NotEmpty String skillId, Map<String, Object> params) throws Exception {

		Map<String, Object> customer = customerService.selectCustomerByCstmUid(chatRoom.getCstmUid());
		String channelCustomerId = (String) customer.get("cstm_link_customer_uid");
		Assert.notNull(channelCustomerId, "EMPTY CHANNEL CUSTOMER ID");

		OpenBuilderSkillRequest requestBody = OpenBuilderSkillRequest.builder()
				.userRequest(UserRequest.builder()
						.block(Block.builder()
								.id(openBuilderSkillResponseParser.getOpenBuilderBlockId(skillId)) // 호출 블록
								.build())
						.user(User.builder()
								.properties(UserProperties.builder()
										.plusfriendUserKey(channelCustomerId)
										.build())
								.build())
						.build())
				.action(Action.builder()
						.clientExtra(params)
						.params(ActionParams.builder()
								.action(skillId) // 호출 블록
								// .ignore_certification("T")
								.build())
						.channel(chatRoom.getCstmLinkDivCd())
						.build())
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Session-Id", chatRoom.getChatRoomUid());
		HttpEntity<OpenBuilderSkillRequest> request = new HttpEntity<>(requestBody, headers);
		String openBuilderUrl = customProperty.getOpenBuilderSkillApiUrl();
		log.debug("SKILL REQUEST BODY: {}", objectMapper.writeValueAsString(requestBody));
		ResponseEntity<String> responseEntity = restTemplate.exchange(openBuilderUrl, HttpMethod.POST, request, String.class);
		log.debug("SKILL RESPONSE BODY: {}", responseEntity.getBody());

		OpenBuildSkillResponse response = objectMapper.readValue(responseEntity.getBody(), OpenBuildSkillResponse.class);
		List<ChatContents> chatContentsList = openBuilderSkillResponseParser.parse(response, skillId);

		return chatContentsList;
	}
	*/

	private ChatContents buildScenarioList(@Valid ChatContents chatContents) {

		ChatContents.Balloon balloon = chatContents.getFirstBalloon();
		List<ChatContents.Action> actionList = new ArrayList<>();

		//		List<Map<String, Object>> scenarioList = settingService.selectHappyBot(new HashMap<>());
		//		for (Map<String, Object> scenario : scenarioList) {
		//			log.info("scenario: {}", scenario);
		//			ChatContents.Action action = ChatContents.Action.builder()
		//					.id(((BigDecimal) scenario.get("id")).longValue())
		//					.type(ActionType.message)
		//					.name((String) scenario.get("name"))
		//					.extra("HappyBot/Block/" + ((BigDecimal) scenario.get("first_block_id")).longValue())
		//					.build();
		//			actionList.add(action);
		//		}
		actionList.add(ChatContents.Action.builder()
				.type(ActionType.hotkey)
				.name("처음으로")
				.extra("HappyBot/First")
				.build());
		actionList.add(ChatContents.Action.builder()
				.type(ActionType.hotkey)
				.name("상담직원 연결")
				.extra("HappyTalk/RequestCounselor")
				.build());

		if (!actionList.isEmpty()) {
			balloon.getSections().add(ChatContents.Section.builder()
					.type(ChatContents.Section.SectionType.action)
					.actions(actionList)
					.build());
		}

		return chatContents;
	}

	@Override
	public void endSession(ChatRoom chatRoom)
			throws Exception
	{
		log.info("END SESSION: HAPPY BOT");
	}
}
