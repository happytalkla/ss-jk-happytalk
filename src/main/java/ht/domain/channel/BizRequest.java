package ht.domain.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ht.domain.ChatContents;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;

/**
 * 해피톡 V2, 요청 포맷
 */
@ApiModel("해피톡으로 이벤트 전달")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BizRequest {

	/**
	 * 채널 식별자
	 */
	@NotNull
	@PositiveOrZero
	private Long channelId;

	/**
	 * 채널 서비스 식별자
	 */
	@NotEmpty
	private String channelServiceId;

	/**
	 * 채널 고객 식별자
	 */
	@NotEmpty
	private String channelCustomerId;

	/**
	 * 채팅 메세지
	 */
	@Valid
	private ChatContents chatContents;

	/**
	 * 봇 채팅 메세지 (오픈 빌더 응답)
	 */
	@Valid
	private List<ChatContents> botChatContentsList;

	/**
	 * 채팅방 생성 데이터 (메세지 이벤트시)
	 */
	@Valid
	private BizResponseOpen bizResponseOpen;

	/**
	 * 채널 인입 파라미터 (오픈 이벤트시)
	 */
	private Map<String, Object> channelParams;
}
