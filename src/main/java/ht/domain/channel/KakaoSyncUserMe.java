package ht.domain.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoSyncUserMe {

	@NotNull
	@Positive
	private Long id;
	@NotNull
	private KakaoAccount kakaoAccount;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class KakaoAccount {
		private KakaoUserProfile profile;
		private String email;
		private String phoneNumber;
		private Boolean hasCi;
		private String ci;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class KakaoUserProfile {

		@NotEmpty
		private String nickname;
	}
}
