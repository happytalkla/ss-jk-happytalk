package ht.domain.chatbot;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 해피봇 컨텐츠 Deserializer
 *
 * <code>
 * GsonBuilder gsonBuilder = new GsonBuilder();
 * Gson gson = gsonBuilder.registerTypeAdapter(BizHappyBotContents.class, new BizHappyBotContentsDeserializer()).create();
 * </code>
 */
public class BizHappyBotContentsDeserializer implements JsonDeserializer<BizHappyBotContents> {

	@Override
	public BizHappyBotContents deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		Gson gson = new Gson();
		BizHappyBotContents bizHappyBotContents = gson.fromJson(json, BizHappyBotContents.class);

		// data 필드 파싱
		JsonElement data = json.getAsJsonObject().get("data");
		if (data == null) {
			throw new UnsupportedOperationException("Invalid HappyBot Format: Data filed is null");
		}

		switch (bizHappyBotContents.getType()) {
			case answer:
			case request_chat:
			case error:
			case init:
			case bot:
			case lists:
			case completed:
			case api_result:
			case finished:
			case delete:
				// 위에서 Gson 으로 파싱
//				log.info("data: {}", bizHappyBotContents.getData());
//				bizHappyBotContents.setData(data.getAsString());
				break;
			case question:
				BizHappyBotDataQuestion bizHappyBotDataQuestion = parseQuestion(data.getAsJsonObject());
				bizHappyBotContents.setData(bizHappyBotDataQuestion);
				break;
			case scenario_lists:
				List<BizHappyBotDataScenario> bizHappyBotDataScenarioList = parseScenario(data.getAsJsonArray());
				bizHappyBotContents.setData(bizHappyBotDataScenarioList);
				break;
			default:
				throw new UnsupportedOperationException("Invalid HappyBot Format: Unknown type");
		}

		return bizHappyBotContents;
	}

	private BizHappyBotDataQuestion parseQuestion(JsonObject data) {

		Gson gson = new Gson();
		return gson.fromJson(data, BizHappyBotDataQuestion.class);
	}

	private List<BizHappyBotDataScenario> parseScenario(JsonArray data) {

		Gson gson = new Gson();
		return gson.fromJson(data, new TypeToken<List<BizHappyBotDataScenario>>(){}.getType());
	}
}
