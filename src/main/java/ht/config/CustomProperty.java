package ht.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "ht")
@Validated
@Data
public class CustomProperty {

	private String happyTalkHostName;
	private String happyTalkUrl;
	private String happyTalkPublicUrl;
	private String happyTalkPrivatecUrl;
	private String staticUpdateParam;
	private String loginPage;
	private String enableSchedule;
	private String redirectBasePath;
	private Integer chatRoomPageSize;

	private MessageBroker messageBroker;
	private String wsConnectionPath;
	private String wsTopicPath;
	private String wsQueuePath;
	private String wsNoticePath;
	private String wsDefaultDestinationPath;

	private String storagePath;
	private String uploadUrlBase;
	private String happyBotApiUrl;
	private String happyBotApiKey;
	private String happyBotBuilderUrl;
	private String webhookApiUrl;
	private String openBuilderSkillApiUrl;
	private String legacyApiUrl;

	private String defaultAssignMemeberUid;
	private String systemMemeberUid;

	private String memberFile;
	private String memberFileCnsr;
	private String memberFileFp;
	private String loginUrl;

	private String happyBotMemeberUid;
	private String iBotMemeberUid;
	private String categoryBotMemeberUid;
	private String mockChatBotMemeberUid;

	private String simulatorUid;

	private String siteId;

	private String kakaoCounselId;
	private String naverTalkTalkId;
	private String kakaoJavascriptApiKey;
	
	private String damoIniPath;
	private String damoAgentId;
	private String damoDbName;
	private String damoOwner;
	private String damoTblName;
	
	@Data
	public static class MessageBroker {
		private String host;
		private int port;
		private String user;
		private String pass;
	}

	/**
	 * IPCC_ADV 고객여정 API URL 추가
	 */
	private String genesysCsApiUrl;
	/**
	 * IPCC_IST 통합통계 FTP 서버 정보 추가
	 */
	private IstFtp istFtp;

	@Data
	public static class IstFtp {
		private String host;
		private Integer port;
		private String user;
		private String pswd;
		private String path;
	}
}
