package ht.domain.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * 채팅방 생성 응답 포맷
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BizResponseOpen {

	@NotEmpty
	@JsonProperty("site_id")
	@SerializedName("site_id")
	private String siteId;

	@JsonProperty("channel_api_key")
	@SerializedName("channel_api_key")
	private String channelApiKey;

	@NotEmpty
	@JsonProperty("chat_room_id")
	@SerializedName("chat_room_id")
	private String chatRoomId;

	@NotEmpty
	@JsonProperty("category_id")
	@SerializedName("category_id")
	private String categoryId;

	@NotEmpty
	@JsonProperty("customer_id")
	@SerializedName("customer_id")
	private String customerId;
}
