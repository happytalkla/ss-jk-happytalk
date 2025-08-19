package ht;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.ss.mca.MCALib;

import ht.constants.CommonConstants;
import ht.domain.ApiItemWrapper;
import ht.mca.vo.Mag0081pVO;
import ht.mca.vo.Sgd1616pVO;
import ht.service.McaService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class) 
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class HappyTalkApplicationTests {
	@Resource
	private McaService mcaService;
	@Resource
	private RestTemplate restTemplate;
	
	@Test
	public void contextLoads() {
		String localhost = "";
		try {
			localhost = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String authKey = botSkill.getAuthKey();
		if (!Strings.isNullOrEmpty(authKey)) {
			String[] authKeys = authKey.split(": ");
			if (authKeys.length == 2) {
				headers.add(authKeys[0], authKeys[1]);
			}
		}

		Map<String, Object> requestBody = new HashMap<>();
		//requestBody.put("screenNum", "1021");
		//requestBody.put("chat_room_uid", botSession.getId());
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
		String targetUrl = "http://localhost:8080/happytalk/skill/igv2001p?userId=1111&goodCode=null&screenNum=2001";
		String botSession = "1b4d5299-f0ce-4226-9e5a-26e87e2e87d2";
		ResponseEntity<ApiItemWrapper<Map<String, Object>>> rest = restTemplate.exchange(targetUrl, HttpMethod.GET, request, new ParameterizedTypeReference<ApiItemWrapper<Map<String, Object>>>() {
		});
		System.out.println("rest=>>>>>>>>>>" + rest);

		ResponseEntity<ApiItemWrapper<Map<String, Object>>> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, request
				,new ParameterizedTypeReference<ApiItemWrapper<Map<String, Object>>>() {
				},botSession);
		log.info("CALL REST RESOURCE, SESSION ID: {}, RETURN CODE: {}, RESPONSE BODY: {}", botSession,
				responseEntity.getStatusCode(), responseEntity.getBody());
		*/
		
		
/*
		String userId = "1001725005";
		String entityId = "1001725005";
		String channelCode= CommonConstants.MCA_CHANNEL_CODE;
		String custNo = "6910161102535"; //고객번호
		String pwd = "noPassword";
		Sgd0027pVO vo1 = new Sgd0027pVO(localhost, userId, channelCode);
		vo1.getInRec1().setH_ia_rfrn_date1("20200304");
		vo1.getInRec1().setH_ia_rfrn_date2("20200604");
		vo1.getInRec1().setH_iclnt_enty_id(entityId);
		vo1.getInRec1().setH_icrm_clnt_sect_code("");
		vo1.getInRec1().setH_iacti_ctnt("");
		vo1.getInRec1().setH_icnsd_clnt_icls_yn("");
		vo1.getInRec1().setH_iempy_no("");
		vo1.getInRec1().setH_icrm_tch_path_sect_code("00");
		vo1.getInRec1().setH_itch_chnl_sect_code("");
		vo1.getInRec1().setH_ia_nxt_date("99991231");
		vo1.getInRec1().setH_ia_len_6_cnti_rfrn_kyvl("999999");
		vo1.getInRec1().setH_ia_nxt_clnt_enty_id("0");
		vo1.getInRec1().setH_ia_len_30_cnti_rfrn_kyvl("0");
		
		vo1 = (Sgd0027pVO) MCALib.call(vo1, false);
		
		System.out.println("accInfo ===========>>>>>>>>>>>>>"+vo1);
		ArrayList<Map<String, Object>> accntList =  (ArrayList<Map<String, Object>>)vo1.toMap().get("outRec2");
		System.out.println("accntList ===========>>>>>>>>>>>>>"+ accntList.size());
		if(accntList.size()>0)
			System.out.println("accntList ===========>>>>>>>>>>>>>"+ accntList.get(0));
		//System.out.println(">>>>>>>>>>>>>result v : " + vo.getOutRec2());	
/*
		//hfca_oa_105aq01
		//trHeader.trErrCode=99020
		//com.ss.common.exception.TrResultException: 현재 제공되지 않은 서비스입니다. (TPENOENT):(GROUP_BNEXIA)
		
		String custNo = "6910161102535";
		String custId = "EHANS532";
		String userId = "320306";

		Hfca_oa_105aq01VO v = new Hfca_oa_105aq01VO(localhost, userId, CommonConstants.MCA_CHANNEL_CODE);
		v.getInRec1().setA_RefrSect("2");
		v.getInRec1().setA_AcctNo20("");
		v.getInRec1().setCustNo(custNo);
		v.getInRec1().setUserID("");	
		
		v = (Hfca_oa_105aq01VO) MCALib.call(v, false);
		System.out.println(">>>>>>>>>>>>>result v : " + v.getOutRec1().toString());


		String channelCode="I50";
		
		String h_IUSER_ID = "320306";
		String H_IUSER_PSWD = "Samsung*99";
		String H_ipswd_cryp = "j71tW2QbZVuFKCuEGfwW//LB/p5ZDYBPOhgF4UPOdfk=";
		
		Pfdz102pVO vv2 = new Pfdz102pVO(localhost, "total", CommonConstants.MCA_CHANNEL_CODE);//"D02"
		vv2.getInRec1().setH_iuser_id(h_IUSER_ID);
		vv2.getInRec1().setH_iuser_pswd(H_IUSER_PSWD);
		vv2 = (Pfdz102pVO) MCALib.call(vv2, false);
		System.out.println("vv2@@@@@@@@@@@@@@"+vv2.getOutRec1().toMap());
		
		String H_osucs_yn = "Y";
		String H_odtbr_code = "21090";
		
		Pfdz103pVO vv3 = new Pfdz103pVO(localhost, "total", channelCode);//"D02"
		vv3.getInRec1().setH_iuser_id(h_IUSER_ID);
		vv3.getInRec1().setH_ipswd_cryp(H_ipswd_cryp);
		vv3 = (Pfdz103pVO) MCALib.call(vv3, false);
		System.out.println("vv3@@@@@@@@@@@@@@"+vv3.getOutRec1().toMap());

		//접촉내역
		String userId = "290219";
		String entityId = "1003771302";
		String channelCode= CommonConstants.MCA_CHANNEL_CODE;
		String custNo = "6910161102535"; //고객번호
		String pwd = "noPassword";
		Sgd0027pVO vo1 = new Sgd0027pVO(localhost, userId, channelCode);
		vo1.getInRec1().setH_ia_rfrn_date1("20190504");
		vo1.getInRec1().setH_ia_rfrn_date2("20200504");
		vo1.getInRec1().setH_iclnt_enty_id(entityId);
		vo1.getInRec1().setH_icrm_clnt_sect_code("");
		vo1.getInRec1().setH_iacti_ctnt("");
		vo1.getInRec1().setH_icnsd_clnt_icls_yn("");
		vo1.getInRec1().setH_iempy_no("");
		vo1.getInRec1().setH_icrm_tch_path_sect_code("00");
		vo1.getInRec1().setH_itch_chnl_sect_code("");
		vo1.getInRec1().setH_ia_nxt_date("99991231");
		vo1.getInRec1().setH_ia_len_6_cnti_rfrn_kyvl("999999");
		vo1.getInRec1().setH_ia_nxt_clnt_enty_id("0");
		vo1.getInRec1().setH_ia_len_30_cnti_rfrn_kyvl("0");
		
		vo1 = (Sgd0027pVO) MCALib.call(vo1, false);
		System.out.println(">>>>>>>>>>>>>result v : " + vo1.getOutRec1().toMap());
		System.out.println(">>>>>>>>>>>>>result v : " + vo1.getOutRec2().toMap());
		*/
		
		String testId = "320306";
		//String userId = "total";
		String CLNT_RNNO = "";			
		String A_PSWD_CRYP = "j71tW2QbZVuFKCuEGfwW//LB/p5ZDYBPOhgF4UPOdfk=";
		//String A_PSWD_CRYP="MtUzq6txeNvDiMbYiVcQgC5TmntfWGM79BugP3LgUg8=";
		String ACNT_STAT = "1";
		//String acnt_no ="1015981063";
		String acnt_no = "706889737201";
		String deptCD = "21090";
		Mag0081pVO v = new Mag0081pVO(localhost, testId, CommonConstants.MCA_CHANNEL_CODE, deptCD, "", "");
		v.getInRec1().setACNT_NO(acnt_no);
		v.getInRec1().setCLNT_RNNO(CLNT_RNNO);
		v.getInRec1().setA_PSWD_CRYP(A_PSWD_CRYP);
		v.getInRec1().setA_ACNT_STAS_DTLS_CODE(ACNT_STAT);
		v.getInRec1().setA_NXT_ACNT_ID("");
		
		v = (Mag0081pVO) MCALib.call(v, false);
		System.out.println(">>>>>>>>>>>>>result v : " + v);		

		
		String entityId = "2000560062";
		String channelCode="I50";
		String custNo = "6910161102535"; //고객번호
		String pwd = "noPassword";
		String alis_name = "3603151126497";

		Sgd1616pVO vo = new Sgd1616pVO(localhost, "", channelCode);
		vo.getInRec1().setCRM_CLNT_ID(entityId);
		vo.getInRec1().setACNT_STAS_TYPE_CODE("AC");
		vo.getInRec1().setA_LEN_10_CNTI_RFRN_KYVL("0");
		vo = (Sgd1616pVO) MCALib.call(vo, false);
		
		System.out.println(">>>>>>>>>>>>>result vo : " + vo);		

	/*		
		String userId="total";
		String testId = "1001725005";
		Mah0363pVO v = new Mah0363pVO(localhost, userId, CommonConstants.MCA_CHANNEL_CODE);
		v.getInRec1().setACNT_NO("");
		v.getInRec1().setCLNT_RRNO("");
		v.getInRec1().setCLNT_ENTY_ID(testId);
		v = (Mah0363pVO) MCALib.call(v, false);
		System.out.println(">>>>>>>>>>>>>result v : " + v);

		
		String testId = "320306";
		String userId = "290219";
		String pw = "Samsung*99";			
		//String A_PSWD_CRYP = "MtUzq6txeNvDiMbYiVcQgC5TmntfWGM79BugP3LgUg8=";
		String A_PSWD_CRYP = "j71tW2QbZVuFKCuEGfwW//LB/p5ZDYBPOhgF4UPOdfk=";
		//계좌 상세정보

		Maa0421pVO v = new Maa0421pVO(localhost, testId, CommonConstants.MCA_CHANNEL_CODE);
		v.getInRec1().setACNT_NO("1012720901");
		v.getInRec1().setA_PSWD_CRYP(A_PSWD_CRYP);
		v = (Maa0421pVO) MCALib.call(v, false);
		System.out.println(">>>>>>>>>>>>>result v : " + v.getOutRec1().toMap());
	

	
		
		/*
		String entityId = "1000721320";
		String channelCode=CommonConstants.MCA_CHANNEL_CODE;
		String prdtId = "100364730";
		Aimp024pVO vo = new Aimp024pVO(localhost, entityId, channelCode);
		vo.getInRec1().setA_CHK_CODE("2");
		vo.getInRec1().setISCD("KR65333AV766");
		
		//vo.getInRec1().setSTNR_DATE("20200409");
		//vo.getInRec1().setZRN_FUND_CD("K55101AZ7044");
		//vo.getInRec1().setZRN_ZRN_CD("22010");
		
		vo = (Aimp024pVO)MCALib.call(vo, false);
		
		System.out.println("OutRec1 : " + vo.toMap());
		*/
		//System.out.println("OutRec2 : " + vo.getOutRec2List());
		//System.out.println("OutRec1List : " + vo.getOutRec1List());
	}
}
