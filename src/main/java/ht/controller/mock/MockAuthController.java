package ht.controller.mock;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import ht.constants.CommonConstants;
import ht.controller.CustomerController;
import ht.controller.CustomerController.CustomerEventType;
import ht.domain.ChannelVO;
import ht.domain.ChatRoom;
import ht.service.AuthService;
import ht.service.ChatRoomService;
import ht.service.McaService;
import ht.service.ScpAgentService;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증 페이지 Mock
 */
@Controller
@Slf4j
public class MockAuthController {

	@Resource
	private AuthService authService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private CustomerController CustomerController;
	@Resource
	private ScpAgentService scpagentService;

	@RequestMapping(value = "/mock/auth/{auth-type}", method = { RequestMethod.GET, RequestMethod.POST })
	public String auth(
			@PathVariable("auth-type") @NotEmpty String authType
			, @RequestParam(value = "chat_room_uid") String chatRoomUid
			, @RequestParam(value = "auth_position", required = false, defaultValue = "bot") String authPosition) throws Exception {

		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
		Map<String, Object> requestParams = new HashMap<>();
		requestParams.put("event_type", CustomerEventType.auth_complete);
		requestParams.put("auth_type", authType);
		CustomerController.postEvent(chatRoomUid, CustomerEventType.auth_complete, null
				, "1234", "김철수", "phone", "00","Y|Y"
				, requestParams, null);

		return "mock/auth";
	}
	
	@RequestMapping(value = "/mock/auth", method = { RequestMethod.GET, RequestMethod.POST })
	public String ssAuth( @RequestParam(value = "crypto", required = false) String crypto ) throws Exception {
		
		log.info("################after auth################");
		log.info("crypto ==>"+crypto);
		log.info("################after auth################");
		ChannelVO channelVo = null;
		if( crypto != null) {
			String decString = scpagentService.getDecString(crypto);
			
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@start@@@@@@@@@@@@@@@@@@@@@@@@");
			log.info("");
			log.info("decodeCrypto : "+decString);
			log.info("");
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@end@@@@@@@@@@@@@@@@@@@@@@@@");
			
			Gson gson = new Gson();
			channelVo = gson.fromJson(decString, ChannelVO.class);
			
			//cripto
			if(channelVo == null) {
				log.info("Cripto not decoding");
				return "error/error";
			}
			log.info("channelVo ==>"+channelVo.toString());
			
		}
		
		ChatRoom chatRoomTmp = chatRoomService.selectChatRoomByChatRoomUid(channelVo.getRoomUid().toString());
		String channel = channelVo.getCha_Code();
		String entityId = channelVo.getEntity_ID();
		String cstmUid = channelVo.getCstmUid();
		String chatRoomUid = channelVo.getRoomUid();
		String kakaoCrtfc = channelVo.getMngnClntYn();

		Map<String, Object> requestParams = new HashMap<>();
		
		/*인증 성공*/
		requestParams.put("event_type", CustomerEventType.auth_complete);
		/*AUTH TYPE --> phone*/
		requestParams.put("auth_type", "phone");
		requestParams.put("channel", channel);
		
		/*chat room 정보 가져오기*/
		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(chatRoomUid);
		String cstmosDivCd = String.valueOf(chatRoom.getCstmOsDivCd());
		
		/*USER NAME 가져오기*/
		String clntName = null;
		if(!"0".equals(entityId)) {
			McaService mcaService = new McaService();
			Map<String, Object> sgd1611p = mcaService.sgd1611p(entityId, CommonConstants.MCA_CHANNEL_CODE);
			clntName = String.valueOf(sgd1611p.get("CLNT_NAME"));
		}
		
		log.info("<######################## KAKAO ########################>");
		log.info("chatRoomUid ==>"+chatRoomUid.toString());
		log.info("auth_complete ==>"+CustomerEventType.auth_complete);
		log.info("entityId ==>"+entityId.toString());
		log.info("clntName ==>"+clntName.toString());
		log.info("cstmosDivCd ==>"+cstmosDivCd.toString());
		log.info("kakaoCrtfc ==>"+kakaoCrtfc.toString());
		log.info("requestParams ==>"+requestParams.toString());
		log.info("<######################## KAKAO ########################>");
		
		CustomerController.postEvent(chatRoomUid, CustomerEventType.auth_complete, null, entityId, clntName, cstmosDivCd, "00", kakaoCrtfc, requestParams, null);
		
		return "mock/auth";
	}
	
	
}
