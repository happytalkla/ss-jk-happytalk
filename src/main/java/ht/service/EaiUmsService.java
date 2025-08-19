package ht.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

import com.samsungfn.eai.core.msg.util.EaiMessageV2;
import com.samsungfn.eai.core.common.net.HttpEaiCaller;
import com.samsungfn.eai.core.common.util.StringUtil;
@Service
@Slf4j
public class EaiUmsService {
	@Resource
	private HTUtils htUtils;
	
	@Transactional
	public void throwEai(Map<String, Object> param) {
		// 1. 송신 전문 생성
		EaiMessageV2 eaiMessage = new EaiMessageV2();

		// EAI ID
		eaiMessage.setSysEsbId("O_HFDA_HTP_0001");
		// EAI ID
		eaiMessage.setSysSvcId("O_HFDA_HTP_0001");
		
		//아래는 고정값
		eaiMessage.setSysCrwrTyp("0");
		eaiMessage.setSysTestSect("R");
		// 필요에따라 헤더 세팅 추가

		// 업무 메세지
		String umsCode = "hfda01";
		String channel = null;
		if(param.get("cstmLinkDivCd").equals("C")) {
			channel = "I50";
		}
		if(param.get("cstmLinkDivCd").equals("D")) {
			channel = "I48";
		}
		// TODO: IPCC_MCH ARS 채널 푸시 사용여부 확인 후 코드 추가

		String msgForEai = param.get("pushMsg").toString();
		for(int i = (msgForEai.getBytes()).length; i < 80; i++) {
			msgForEai += " ";
		}
		
		// LKJ 20210422 자릿수 초과로 인해 형변환 타입 변경 (int->long)
//		int intEntityId = (int) Double.parseDouble(param.get("entityId").toString());
		long intEntityId = (long) Double.parseDouble(param.get("entityId").toString());
		
		String pushMsg = String.format("%019d", (intEntityId)) + umsCode + channel + msgForEai;
		log.info("************************************ pushMsg : " + pushMsg);
		eaiMessage.setBizMessage(pushMsg);

		// 2. HTTP 클라이언트 생성
		HttpEaiCaller client = new HttpEaiCaller();
		if (htUtils.isActiveProfile("live1") || htUtils.isActiveProfile("live2")) {
			HttpEaiCaller.setRealEaiUrl(); //전송할 EAI 서버로 운영 서버를 선택
		}
		else {
			//HttpEaiCaller.setDevEaiUrl(); //전송할 EAI 서버로 개발 서버를 선택
			HttpEaiCaller.setTestEaiUrl(); //전송할 EAI 서버로 테스트 서버를 선택
			//HttpEaiCaller.setDrEaiUrl(); //전송할 EAI 서버로 DR 서버를 선택
		}

		
		try {
			// 3. 전문 송수신
			String returnMessage = new String( client.executeGet(  eaiMessage.getEaiMessage(), HttpEaiCaller.DEFAULT_INVOKE_TIMES, HttpEaiCaller.DEFAULT_CONNECTION_TIMEOUT));
			// 4. 수신전문 파싱
			eaiMessage.setReturnMessage(returnMessage);
			  
			// 헤더 처리결과
			String prcRst = eaiMessage.getSysPrceRslt();
			log.info(">>>>> EAI >>>>>>     push pushMsg : " + pushMsg + "|");
			log.info(">>>>> EAI >>>>>>     prcRst : {}", prcRst);
			// 업무 메시지
			String rtnBizMessage = eaiMessage.getBizMessage();
			log.info(">>>>> EAI >>>>>>     rtnBizMessage : "+rtnBizMessage);
			
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);

		}
		
	}
}
