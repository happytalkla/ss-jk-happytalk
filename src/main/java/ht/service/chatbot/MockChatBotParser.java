package ht.service.chatbot;

import com.github.javafaker.*;
import com.google.gson.Gson;
import ht.domain.ChatContents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MockChatBotParser {

	/**
	 * 챗봇 응답 포맷 -> 해피톡 포맷으로 파싱
	 *
	 * @param responseBody 챗봇 응답 포맷
	 * @return 해피톡 포맷
	 */
	public String parse(@NotEmpty String responseBody) {

		ChatContents chatContents = new ChatContents();

		Faker faker = new Faker(new Locale("ko"));
		Lorem lorem = faker.lorem();
		Animal animal = faker.animal();
		Internet internet = faker.internet();

		//		int balloonSize = new Random().nextInt(3) + 1;
		//		int sectionRand = new Random().nextInt(3) + 1;
		int balloonSize = 2;
		int sectionRand = 2;

		for (int n = 0; n < balloonSize; n++) {

			ChatContents.Balloon balloon = new ChatContents.Balloon();
			List<ChatContents.Section> sectionList = new ArrayList<>();
			balloon.setSections(sectionList);

			// 이미지
			if (sectionRand % 3 == 1) {
				ChatContents.Section section = ChatContents.Section.builder()
						.type(ChatContents.Section.SectionType.file)
						.display("image")
						.data("/happytalk/images/sample/happytalk_960x460.jpg")
						.build();
				sectionList.add(section);
				log.debug("IMAGE: {}", chatContents);
			}

			// 답변 내용
			//			if (sectionRand % 3 == 2) {
			{
				ChatContents.Section section = ChatContents.Section.builder()
						.type(ChatContents.Section.SectionType.text)
						.data(lorem.paragraph())
						.build();
				sectionList.add(section);
				log.debug("MESSAGE: {}", chatContents);
			}
			//			}

			// 메세지 버튼
			if (sectionRand % 2 == 0) {
				List<ChatContents.Action> actionList = new ArrayList<>();
				ChatContents.Action action = ChatContents.Action.builder()
						.type(ChatContents.ActionType.message)
						.name(animal.name())
						.data(animal.name())
						.extra("HappyBot/10/128")
						.build();
				actionList.add(action);
				action = ChatContents.Action.builder()
						.type(ChatContents.ActionType.message)
						.name(animal.name())
						.data(animal.name())
						.extra("HappyBot/10/131")
						.build();
				actionList.add(action);

				ChatContents.Section section = ChatContents.Section.builder()
						.type(ChatContents.Section.SectionType.action)
						.actions(actionList)
						.build();

				sectionList.add(section);
				log.debug("LINK: {}", balloon);
			}

			// 링크 버튼
			if (sectionRand % 3 == 0) {
				List<ChatContents.Action> actionList = new ArrayList<>();
				ChatContents.Action action = ChatContents.Action.builder()
						.type(ChatContents.ActionType.link)
						.name(animal.name())
						.data("http://" + internet.url())
						.extra("HappyBot/10/128")
						.build();
				actionList.add(action);
				action = ChatContents.Action.builder()
						.type(ChatContents.ActionType.link)
						.name(animal.name())
						.data("http://" + internet.url())
						.extra("HappyBot/10/131")
						.build();
				actionList.add(action);

				ChatContents.Section section = ChatContents.Section.builder()
						.type(ChatContents.Section.SectionType.action)
						.actions(actionList)
						.build();

				sectionList.add(section);
				log.debug("LINK: {}", balloon);
			}

			// 별첨
			//			if (sectionRand % 3 == 0) {
			//				List<ChatContents.Action> actionList = new ArrayList<>();
			//				ChatContents.Action action = ChatContents.Action.builder()
			//						.name(book.title())
			//						.data(lorem.paragraph())
			//						.build();
			//				actionList.add(action);
			//
			//				ChatContents.Section section = ChatContents.Section.builder()
			//						.type(ChatContents.Section.SectionType.action)
			//						.actionType(ChatContents.ActionType.reference)
			//						.actions(actionList)
			//						.build();
			//
			//				sectionList.add(section);
			//				log.debug("REF: {}", balloon);
			//			}

			// 핫키
			//			if (sectionRand % 2 == 0) {
			{
				List<ChatContents.Action> actionList = new ArrayList<>();
				ChatContents.Action action = ChatContents.Action.builder()
						.type(ChatContents.ActionType.hotkey)
						.name("상담직원 연결")
						.data("상담직원 연결")
						.extra("HappyTalk/RequestCounselor")
						.build();
				actionList.add(action);

				ChatContents.Section section = ChatContents.Section.builder()
						.type(ChatContents.Section.SectionType.action)
						.actions(actionList)
						.build();

				sectionList.add(section);
				log.debug("HOTKEY: {}", balloon);
			}
			//			}

			chatContents.add(balloon);
		}

		// ////////////////////////////////////////////////////////////////////
		// SORRY 메세지시 버튼 추가
		//		if (WisenutResponseMessage.WisenutAnswerType.sorry == result.getAnswerType()
		//				|| "연락처 문의".equals(result.getIntentName())) {
		//
		//			log.debug("SORRY MESSAGE");
		//			JsonObject jsonHotKey = new JsonObject();
		//			jsonHotKey.addProperty("type", "hotkey");
		//			jsonHotKey.addProperty("contents", "HappyTalk/RequestCounselor");
		//			cont.add(jsonHotKey);
		//		}

		Gson gson = new Gson();
		String json = gson.toJson(chatContents, ChatContents.class);
		log.info("chatContents: {}", json);
		return json;
	}
}
