package ht.service.chatbot;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import ht.config.CustomProperty;
import ht.domain.ChatContents;
import ht.service.AssignService;
import ht.service.CategoryService;
import ht.service.SettingService;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class CategoryBotParser {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private CategoryService categoryService;
	@Resource
	private SettingService settingService;
	@Resource
	private AssignService assignService;
	@Resource
	private HTUtils htUtils;

	/**
	 * 챗봇 응답 포맷 -> 해피톡 포맷으로 파싱
	 *
	 * @param cont {@link ChatContents}
	 * @return 해피톡 포맷
	 */
	public String parse(String cont) {

		log.debug("CATEGORY BOT");
		String parent = "";

		Gson gson = new Gson();
		ChatContents requestChatContents = gson.fromJson(cont, ChatContents.class);
		if (requestChatContents != null) {
			for (ChatContents.Balloon balloon : requestChatContents.getBalloons()) {
				for (ChatContents.Section section : balloon.getSections()) {
					String extra = section.getExtra();
					log.debug("EXTRA: {}", extra);
					if (!Strings.isNullOrEmpty(extra) && extra.startsWith(CategoryBotServiceImpl.BOT_NAME)) {
						if (!CategoryBotServiceImpl.BOT_NAME.equals(extra)) {
							parent = extra.replaceFirst(CategoryBotServiceImpl.BOT_NAME + "/", "");
						}
					}
				}
			}
		}

		Map<String, Object> siteSetting = settingService.selectSiteSetting();
		long maxDepth = settingService.getMaxCategoryDepth(siteSetting);
		List<Map<String, Object>> categoryList;

		if (Strings.isNullOrEmpty(parent)) {
			log.debug("CATEGORY BOT, TOP CATEGORY");
			Map<String, Object> sqlParams = new HashMap<>();
			sqlParams.put("ctgDpt", 1);
			sqlParams.put("useYn", "Y");
			sqlParams.put("dftCtgYn", "N");
			categoryList = categoryService.selectCategoryList(sqlParams);
		} else {
			log.debug("CATEGORY BOT, CATEGORY OF: {}", parent);
			Map<String, Object> sqlParams = new HashMap<>();
			sqlParams.put("upperCtgNum", parent);
			sqlParams.put("useYn", "Y");
			sqlParams.put("dftCtgYn", "N");
			categoryList = categoryService.selectCategoryList(sqlParams);
		}
		log.debug("CATEGORY BOT, CATEGORY LIST: {}", categoryList);

		List<ChatContents.Action> actionList = new ArrayList<>();
		for (Map<String, Object> category : categoryList) {
			if (maxDepth == ((BigDecimal) category.get("ctg_dpt")).longValue()) {
				ChatContents.Action action = ChatContents.Action.builder()
						.type(ChatContents.ActionType.message)
						.name((String) category.get("ctg_nm"))
						.data((String) category.get("ctg_num"))
						.extra("HappyTalk/RequestCounselor/" + category.get("ctg_num"))
						.build();
				actionList.add(action);
			} else {
				ChatContents.Action action = ChatContents.Action.builder()
						.type(ChatContents.ActionType.message)
						.name((String) category.get("ctg_nm"))
						.data((String) category.get("ctg_num"))
						.extra(CategoryBotServiceImpl.BOT_NAME + "/" + category.get("ctg_num"))
						.build();
				actionList.add(action);
			}
		}

		if (!Strings.isNullOrEmpty(parent)) {
			ChatContents.Action action = ChatContents.Action.builder()
					.type(ChatContents.ActionType.message)
					.name("이전으로")
					.data("이전으로")
					.extra(CategoryBotServiceImpl.BOT_NAME)
					.build();
			actionList.add(action);
		}

		List<ChatContents.Section> sectionList = new ArrayList<>();
		//		if (htUtils.isActiveProfile("live")) { // 외부 접속 필요
		sectionList.add(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.file)
				.data(customProperty.getHappyTalkUrl() + "/images/catebot_greeting.jpg" )
				.display("image/jpg")
				.extra("채팅상담 업무선택")
				.build());
		//		}
		sectionList.add(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.text)
				.data("1:1상담사 채팅은 상담품질 관리를 위해 삼성증권에 저장됩니다.\n" +
						"고객님의 문의사항에 알맞게 상담유형을 선택해 주시기 바랍니다.")
				.build());
		sectionList.add(ChatContents.Section.builder()
				.type(ChatContents.Section.SectionType.action)
				.actions(actionList)
				.build());
		ChatContents chatContents = new ChatContents(sectionList);

		String json = gson.toJson(chatContents, ChatContents.class);
		log.debug("chatContents: {}", json);
		return json;
	}
}
