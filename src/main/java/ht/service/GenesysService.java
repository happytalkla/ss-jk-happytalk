package ht.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.constraints.NotNull;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ht.config.CustomProperty;
import ht.constants.CommonConstants;
import ht.persistence.GenesysDao;
import ht.util.CipherUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * IPCC_ADV 고객여정 I/F Service
 */
@Slf4j
@Service
public class GenesysService {

	@Resource
	private CustomProperty customProperty;
	@Resource(name = "restTemplate")
	private RestTemplate restTemplate;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private CipherUtils cipherUtils;
	@Resource
	private ChatService chatService;
	@Resource
	private ChatRoomService chatRoomService;
	@Resource
	private McaService mcaService;
	@Resource
	private GenesysDao genesysDao;

	private final String MEDIA_TYPE = "chatting";
	private final String SERVICE_TYPE = "CHATTING_SE_IB";
	private final String STATE_TYPE = "CHATTING_ST_IB";
	private final String TASK_TYPE = "CHATTING_TA_IB";
	private final String CUSTOMER_SEARCH = "CUSTOMER_SEARCH";
	private final String CUSTOMER_CREATE = "CUSTOMER_CREATE";
	private final String SERVICE_START = "SERVICE_START";
	private final String STATE_START = "STATE_START";
	private final String TASK_START = "TASK_START";
	private final String SERVICE_END = "SERVICE_END";
	private final String STATE_END = "STATE_END";
	private final String TASK_END = "TASK_END";

	/**
	 * IPCC_ADV CB01_고객조회
	 * return customerId
	 */
	@Transactional
	public String getCustomer(Map<String, Object> map, boolean isResend) throws Exception {
		String result = null;
		try {
			
			String requestUrl = isResend ? (String) map.get("url") : customProperty.getGenesysCsApiUrl() + "/profiles?CustomerNo=" + map.get("entityId");
			log.info(">>>>>>>> genesys url : {} " , requestUrl);
	
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> request = new HttpEntity<>(headers);
	
			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (isResend) {
				genesysDao.updateIfResultForResend(map);
			} else {
				paramMap.put("CustomerNo", map.get("entityId"));
				
				map.put("parameter", new Gson().toJson(paramMap));
				map.put("url", requestUrl);
				map.put("type", CUSTOMER_SEARCH);
				genesysDao.insertIfResultForStart(map);
			}

			ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
	        
			log.info("GENESYS IF MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
	
			List<Map<String, Object>> responseBody = responseEntity.getBody();
	
			log.info("getCustomer : STATUS CODE : {} ", responseEntity.getStatusCode());
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				result = (String) responseBody.get(0).get("customer_id");
				log.info("getCustomer : CUSTOMER ID : {} ", result);
				map.put("successYn", "Y");
				map.put("response", new Gson().toJson(responseBody));
			} else if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
				log.info("getCustomer : CUSTOMER ID : NOT EXISTS ");
				map.put("successYn", "Y");
				map.put("response", new Gson().toJson(responseBody));
			}
			map.put("isError", false);
		} catch (Exception e) {
			map.put("successYn", "N");
			map.put("isError", true);
			map.put("response", e.getMessage());
			log.error("GENESYS IF REQUEST ERROR: {}", e.getMessage());
		}
		genesysDao.updateIfResultForEnd(map);
		return result;
	}

	/**
	 * IPCC_ADV CB02_신규고객생성
	 * return customerId
	 */
	@Transactional
	public String createCustomer(Map<String, Object> map, boolean isResend) throws Exception {
		String result = null;
		try {
			String requestUrl = isResend ? (String) map.get("url") : customProperty.getGenesysCsApiUrl() + "/profiles";
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			log.info(">>>>>>>> genesys call : {} " , headers);
	
			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (isResend) {
				paramMap = new Gson().fromJson((String) map.get("parameter"), new TypeToken<Map<String, Object>>(){}.getType());
				genesysDao.updateIfResultForResend(map);
			} else {
				paramMap.put("CustomerNo", map.get("entityId"));

				map.put("parameter", new Gson().toJson(paramMap));
				map.put("url", requestUrl);
				map.put("type", CUSTOMER_CREATE);
				genesysDao.insertIfResultForStart(map);
			}
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(paramMap, headers);
	
			ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
			log.info("GENESYS IF MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
	
			Map<String, Object> responseBody = responseEntity.getBody();
	
			if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
				result = (String) responseBody.get("customer_id");
				log.info("createCustomer : CUSTOMER ID : {} ", result);
				map.put("successYn", "Y");
				map.put("response", new Gson().toJson(responseBody));
			} else {
				map.put("successYn", "N");
				map.put("response", responseEntity.toString());
			}
		} catch (Exception e) {
			map.put("successYn", "N");
			map.put("response", e.getMessage());
			log.error("GENESYS IF REQUEST ERROR: {}", e.getMessage());
		}
		genesysDao.updateIfResultForEnd(map);
		return result;
	}

	/**
	 * IPCC_ADV 서비스 시작 전송
	 * return serviceId
	 */
	@Transactional
	public String serviceStart(Map<String, Object> map, boolean isResend) throws Exception {
		String result = null;
		try {
			String requestUrl = isResend ? (String) map.get("url") : customProperty.getGenesysCsApiUrl() + "/services/start";
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			log.info(">>>>>>>> genesys call : {} " , requestUrl);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			if (isResend) {
				paramMap = new Gson().fromJson((String) map.get("parameter"), new TypeToken<Map<String, Object>>(){}.getType());
				genesysDao.updateIfResultForResend(map);
			} else {
				paramMap.put("customer_id", map.get("customerId"));
				paramMap.put("service_type", SERVICE_TYPE);

				paramMap.put("media_type", MEDIA_TYPE);
				map.put("parameter", new Gson().toJson(paramMap));
				map.put("url", requestUrl);
				map.put("type", SERVICE_START);
				genesysDao.insertIfResultForStart(map);
			}

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(paramMap, headers);
			log.info("GENESYS IF REQUEST : {}", request);
	
			ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
			log.info("GENESYS IF MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
	
			if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
				Map<String, Object> responseBody = responseEntity.getBody();
				result = (String) responseBody.get("service_id");
				map.put("successYn", "Y");
				map.put("response", new Gson().toJson(responseBody));
			} else {
				map.put("successYn", "N");
				map.put("response", responseEntity.toString());
			}
		} catch (Exception e) {
			map.put("successYn", "N");
			map.put("response", e.getMessage());
			log.error("GENESYS IF REQUEST ERROR: {}", e.getMessage());
		}
		genesysDao.updateIfResultForEnd(map);
		return result;
	}

	/**
	 * IPCC_ADV 상태 시작 전송
	 * return stateId
	 */
	@Transactional
	public String stateStart(Map<String, Object> map, boolean isResend) throws Exception {
		String result = null;
		try {
			String requestUrl = isResend ? (String) map.get("url") : customProperty.getGenesysCsApiUrl() + "/services/" + map.get("serviceId") + "/states/start";
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			log.info(">>>>>>>> genesys call : {} " , requestUrl);
	
			Map<String, Object> paramMap = new HashMap<String, Object>();
			if (isResend) {
				paramMap = new Gson().fromJson((String) map.get("parameter"), new TypeToken<Map<String, Object>>(){}.getType());
				genesysDao.updateIfResultForResend(map);
			} else {
				paramMap.put("media_type", MEDIA_TYPE);
				paramMap.put("state_type", STATE_TYPE);

				map.put("parameter", new Gson().toJson(paramMap));
				map.put("url", requestUrl);
				map.put("type", STATE_START);
				genesysDao.insertIfResultForStart(map);
			}
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(paramMap, headers);
	
			log.info("GENESYS IF REQUEST : {}", request);
			ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
			log.info("GENESYS IF MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
	
			Map<String, Object> responseBody = responseEntity.getBody();
	
			if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
				result = (String) responseBody.get("state_id");
				map.put("successYn", "Y");
				map.put("response", new Gson().toJson(responseBody));
			} else {
				map.put("successYn", "N");
				map.put("response", responseEntity.toString());
			}
		} catch (Exception e) {
			map.put("successYn", "N");
			map.put("response", e.getMessage());
			log.error("GENESYS IF REQUEST ERROR: {}", e.getMessage());
		}
		genesysDao.updateIfResultForEnd(map);
		return result;
	}

	/**
	 * IPCC_ADV 태스크 시작 전송
	 * 
	 * return stateId
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String taskStart(Map<String, Object> map, boolean isResend) throws Exception {
		String result = null;
		try {
			String serviceId = (String) map.get("serviceId");
			String stateId = (String) map.get("stateId");
			String cstmLinkDivCdNm = (String) map.get("cstmLinkDivCdNm");
	
			String requestUrl = isResend ? (String) map.get("url") : customProperty.getGenesysCsApiUrl() + "/services/" + serviceId + "/tasks/start";
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			log.info(">>>>>>>> genesys call : {} ", requestUrl);
	
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, String> custMap = new HashMap<String, String>();
			if (isResend) {
				paramMap = new Gson().fromJson((String) map.get("parameter"), new TypeToken<Map<String, Object>>(){}.getType());
				custMap = (Map<String, String>) paramMap.get("customstart");
				custMap.put("상담내용", cipherUtils.decrypt((String) custMap.get("상담내용")));
				genesysDao.updateIfResultForResend(map);
			} else {
				paramMap.put("state_id", stateId);
				paramMap.put("media_type", MEDIA_TYPE);
				paramMap.put("task_type", TASK_TYPE);
				custMap.put("인입경로", cstmLinkDivCdNm);
				custMap.put("고객유무", (String) map.get("cstmType"));
				custMap.put("상담유형_상품", (String) map.get("dep1CtgNm"));
				custMap.put("상담유형_중분류", (String) map.get("dep2CtgNm"));
				custMap.put("상담유형_소분류", (String) map.get("dep3CtgNm"));
				custMap.put("채팅내용", (String) map.get("chatContText"));
				custMap.put("상담내용", (String) map.get("memo"));
				custMap.put("상담직원", (String) map.get("cnsrNm"));
				custMap.put("prce_sect_code", (String) map.get("sectCode"));
				custMap.put("cnsl_prhs_id", (String) map.get("mcaNum"));
				paramMap.put("customstart", custMap);
				
				map.put("url", requestUrl);
				map.put("type", TASK_START);
				genesysDao.insertIfResultForStart(map);
			}
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(paramMap, headers);

			log.info("GENESYS IF REQUEST : {}", request);
			ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
			log.info("GENESYS IF MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());

			custMap.put("상담내용", cipherUtils.encrypt((String) custMap.get("상담내용")));
			map.put("parameter", new Gson().toJson(paramMap));

			Map<String, Object> responseBody = responseEntity.getBody();
			if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
				result = (String) responseBody.get("task_id");
				map.put("successYn", "Y");
				map.put("response", new Gson().toJson(responseBody));
			} else {
				map.put("successYn", "N");
				map.put("response", responseEntity.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("successYn", "N");
			map.put("response", e.getMessage());
			log.error("GENESYS IF REQUEST ERROR: {}", e.getMessage());
		}
		genesysDao.updateIfResultForEnd(map);
		return result;
	}

	/**
	 * IPCC_ADV 태스크 종료 전송
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean taskEnd(Map<String, Object> map, boolean isResend) throws Exception {
		boolean result = false;
		try {
			String serviceId = (String) map.get("serviceId");
			String stateId = (String) map.get("stateId");
			String taskId = (String) map.get("taskId");
			String cstmLinkDivCdNm = (String) map.get("cstmLinkDivCdNm");
	
			String requestUrl = isResend ? (String) map.get("url") : customProperty.getGenesysCsApiUrl() + "/services/" + serviceId + "/tasks/" + taskId + "/end";
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			log.info(">>>>>>>> genesys call : {} ", requestUrl);
	
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, String> custMap = new HashMap<String, String>();
			if (isResend) {
				paramMap = new Gson().fromJson((String) map.get("parameter"), new TypeToken<Map<String, Object>>(){}.getType());
				custMap = (Map<String, String>) paramMap.get("customstart");
				custMap.put("상담내용", cipherUtils.decrypt((String) custMap.get("상담내용")));
				genesysDao.updateIfResultForResend(map);
			} else {
				paramMap.put("state_id", stateId);
				paramMap.put("media_type", MEDIA_TYPE);
				paramMap.put("task_type", TASK_TYPE);

				custMap.put("인입경로", cstmLinkDivCdNm);
				custMap.put("고객유무", (String) map.get("cstmType"));
				custMap.put("상담유형_상품", (String) map.get("dep1CtgNm"));
				custMap.put("상담유형_중분류", (String) map.get("dep2CtgNm"));
				custMap.put("상담유형_소분류", (String) map.get("dep3CtgNm"));
				custMap.put("채팅내용", (String) map.get("chatContText"));
				custMap.put("상담내용", (String) map.get("memo"));
				custMap.put("상담직원", (String) map.get("cnsrNm"));
				custMap.put("prce_sect_code", (String) map.get("sectCode"));
				custMap.put("cnsl_prhs_id", (String) map.get("mcaNum"));
				paramMap.put("customstart", custMap);

				map.put("url", requestUrl);
				map.put("type", TASK_END);
				genesysDao.insertIfResultForStart(map);
			}
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(paramMap, headers);
			log.info("GENESYS IF REQUEST : {}", request);
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<String>() {});
			log.info("GENESYS IF MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());

			custMap.put("상담내용", cipherUtils.encrypt((String) custMap.get("상담내용")));
			map.put("parameter", new Gson().toJson(paramMap));

			if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
				result = true;
				map.put("successYn", "Y");
				map.put("response", "");
			} else {
				map.put("successYn", "N");
				map.put("response", responseEntity.getStatusCode());
			}
		} catch (Exception e) {
			map.put("successYn", "N");
			map.put("response", e.getMessage());
			log.error("GENESYS IF REQUEST ERROR: {}", e.getMessage());
		}
		genesysDao.updateIfResultForEnd(map);
		return result;
	}

	/**
	 * IPCC_ADV 상태 종료 전송
	 */
	@Transactional
	public boolean stateEnd(Map<String, Object> map, boolean isResend) throws Exception {
		boolean result = false;
		try {
			String requestUrl = isResend ? (String) map.get("url") : customProperty.getGenesysCsApiUrl() + "/services/" + map.get("serviceId") + "/states/" + map.get("stateId") + "/end";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			log.info(">>>>>>>> genesys call : {} ", requestUrl);
	
			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (isResend) {
				paramMap = new Gson().fromJson((String) map.get("parameter"), new TypeToken<Map<String, Object>>(){}.getType());
				genesysDao.updateIfResultForResend(map);
			} else {
				paramMap.put("media_type", MEDIA_TYPE);
				paramMap.put("state_type", STATE_TYPE);
				
				map.put("parameter", new Gson().toJson(paramMap));
				map.put("url", requestUrl);
				map.put("type", STATE_END);
				genesysDao.insertIfResultForStart(map);
			}
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(paramMap, headers);

			log.info("GENESYS IF REQUEST : {}", request);
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<String>() {});
			log.info("GENESYS IF MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
	
			if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
				result = true;
				map.put("successYn", "Y");
				map.put("response", "");
			} else {
				map.put("successYn", "N");
				map.put("response", responseEntity.toString());
			}
		} catch (Exception e) {
			map.put("successYn", "N");
			map.put("response", e.getMessage());
			log.error("GENESYS IF REQUEST ERROR: {}", e.getMessage());
		}
		genesysDao.updateIfResultForEnd(map);
		return result;
	}

	/**
	 * IPCC_ADV 서비스 종료 전송
	 */
	@Transactional
	public boolean serviceEnd(Map<String, Object> map, boolean isResend) throws Exception {
		boolean result = false;
		try {
			String requestUrl = isResend ? (String) map.get("url") : customProperty.getGenesysCsApiUrl() + "/services/" + map.get("serviceId") + "/end";
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			log.info(">>>>>>>> GENESYS CALL : {} ", headers);
	
			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (isResend) {
				paramMap = new Gson().fromJson((String) map.get("parameter"), new TypeToken<Map<String, Object>>(){}.getType());
				genesysDao.updateIfResultForResend(map);
			} else {
				paramMap.put("media_type", MEDIA_TYPE);
				paramMap.put("customer_id", map.get("customerId"));
				paramMap.put("service_type", SERVICE_TYPE);
				
				map.put("parameter", new Gson().toJson(paramMap));
				map.put("url", requestUrl);
				map.put("type", SERVICE_END);
				genesysDao.insertIfResultForStart(map);
			}

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(paramMap, headers);
			log.info("GENESYS IF MESSAGE, REQUEST: {}", request);
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<String>() {});
			log.info("GENESYS IF MESSAGE, RETURN CODE: {}, RESPONSE BODY: {}", responseEntity.getStatusCode(), responseEntity.getBody());
			
			if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
				result = true;
				map.put("successYn", "Y");
				map.put("response", "");
			} else {
				map.put("successYn", "N");
				map.put("response", responseEntity.toString());
			}
		} catch (Exception e) {
			map.put("successYn", "N");
			map.put("response", e.getMessage());
			log.error("GENESYS IF REQUEST ERROR: {}", e.getMessage());
		}
		genesysDao.updateIfResultForEnd(map);
		return result;
	}

	/**
	 * 고객여정 전송
	 */
	public boolean sendGenesys(Map<String, Object> paramMap) {
		log.info("======================================================================================");
		log.info("============================== GENESYS INTERFACE START ===============================");
		log.info("======================================================================================");
		try {
			int seq = 1;
			paramMap.put("seq", seq);
			paramMap.put("id", genesysDao.selectIfResultNextId());
			String cstmType = "기존";
			log.info("============================== GET CUSTOMER ==============================");
			String customerId = this.getCustomer(paramMap, false);

			if ((boolean) paramMap.get("isError")) {
				return false;
			}

			if (StringUtil.isEmpty(customerId)) {
				log.info("============================== CREATE CUSTOMER ==============================");
				paramMap.put("seq", ++seq);
				customerId = this.createCustomer(paramMap, false);
				cstmType = "신규";
			}
			log.info("CUSTOMER ID : {}", customerId);
			if (StringUtil.isEmpty(customerId)) {
				log.error("GENESYS I/F ERROR : CREATE CUSTOMER");
				return false;
			}
			paramMap.put("customerId", customerId);
			paramMap.put("cstmType", cstmType);

			paramMap.put("seq", ++seq);
			log.info("============================== SERVICE START ==============================");
			String serviceId = this.serviceStart(paramMap, false);
			log.info("SERVICE ID : {}", serviceId);
			if (StringUtil.isEmpty(serviceId)) {
				log.error("GENESYS I/F ERROR : SERVICE START");
				return false;
			}
			paramMap.put("serviceId", serviceId);

			paramMap.put("seq", ++seq);
			log.info("============================== STATE START ==============================");
			String stateId = this.stateStart(paramMap, false);
			log.info("STATE ID : {}", stateId);
			if (StringUtil.isEmpty(stateId)) {
				log.error("GENESYS I/F ERROR : STATE START");
				return false;
			}
			paramMap.put("stateId", stateId);

			paramMap.put("seq", ++seq);
			log.info("============================== TASK START ==============================");
			String taskId = this.taskStart(paramMap, false);
			log.info("TASK ID : {}", taskId);
			if (StringUtil.isEmpty(taskId)) {
				log.error("GENESYS I/F ERROR : TASK START");
				return false;
			}
			paramMap.put("taskId", taskId);

			paramMap.put("seq", ++seq);
			log.info("============================== TASK END ==============================");
			if (this.taskEnd(paramMap, false)) {
				paramMap.put("seq", ++seq);
				log.info("============================== STATE END ==============================");
				if (this.stateEnd(paramMap, false)) {
					paramMap.put("seq", ++seq);
					log.info("============================== SERVICE END ==============================");
					if (!this.serviceEnd(paramMap, false)) {
						log.error("GENESYS I/F ERROR : SERVICE END");
						return false;
					}
				} else {
					log.error("GENESYS I/F ERROR : STATE END");
					return false;
				}
			} else {
				log.error("GENESYS I/F ERROR : TASK END");
				return false;
			}
		} catch (Exception e) {
			log.error("GENESYS I/F ERROR : {}", e.getMessage());
			return false;
		}
		log.info("======================================================================================");
		log.info("================================ GENESYS INTERFACE END ===============================");
		log.info("======================================================================================");

		return true;
	}

	/**
	 * 고객여정 실패건 재전송
	 */
	public boolean resendFailedList() {
		List<Map<String, Object>> failedList = genesysDao.selectFailedList();
		
		for (Map<String, Object> failed : failedList) {
			if (!StringUtil.isEmpty(failed.get("memo"))) {
				try {
					failed.put("memo", cipherUtils.decrypt((String) failed.get("memo")));
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			if (StringUtil.isEmpty(failed.get("cnslPrhsId"))) {
				try {
					Map<String, Object> resMap = sendReviewToRegacy((String) failed.get("cstmLinkDivCd"), (String) failed.get("entityId"), failed);
					failed.put("cnslPrhsId", resMap.get("mcaNum"));
					failed.put("mcaNum", resMap.get("mcaNum"));
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			String cstmType = "기존"; 
			int seq = ((BigDecimal) failed.get("seq")).intValue();
			switch((String) failed.get("type")) {
				case CUSTOMER_SEARCH :
					try {
						String customerId = this.getCustomer(failed, true);

						if ((boolean) failed.get("isError")) {
							return false;
						}

						if (StringUtil.isEmpty(customerId)) {
							failed.put("seq", ++seq);
							log.info("============================== CREATE CUSTOMER ==============================");
							customerId = this.createCustomer(failed, false);
							cstmType = "신규";
						}
						log.info("CUSTOMER ID : {}", customerId);

						if (StringUtil.isEmpty(customerId)) {
							log.error("GENESYS I/F ERROR : CREATE CUSTOMER");
							return false;
						}
						failed.put("customerId", customerId);
						failed.put("cstmType", cstmType);
	
						failed.put("seq", ++seq);
						log.info("============================== SERVICE START ==============================");
						String serviceId = this.serviceStart(failed, false);
						log.info("SERVICE ID : {}", serviceId);
						if (StringUtil.isEmpty(serviceId)) {
							log.error("GENESYS I/F ERROR : SERVICE START");
							return false;
						}
						failed.put("serviceId", serviceId);
	
						failed.put("seq", ++seq);
						log.info("============================== STATE START ==============================");
						String stateId = this.stateStart(failed, false);
						log.info("STATE ID : {}", stateId);
						if (StringUtil.isEmpty(stateId)) {
							log.error("GENESYS I/F ERROR : STATE START");
							return false;
						}
						failed.put("stateId", stateId);
	
						failed.put("seq", ++seq);
						log.info("============================== TASK START ==============================");
						String taskId = this.taskStart(failed, false);
						log.info("TASK ID : {}", taskId);
						if (StringUtil.isEmpty(taskId)) {
							log.error("GENESYS I/F ERROR : TASK START");
							return false;
						}
						failed.put("taskId", taskId);
	
						failed.put("seq", ++seq);
						log.info("============================== TASK END ==============================");
						if (this.taskEnd(failed, false)) {
							failed.put("seq", ++seq);
							log.info("============================== STATE END ==============================");
							if (this.stateEnd(failed, false)) {
								failed.put("seq", ++seq);
								log.info("============================== SERVICE END ==============================");
								if (!this.serviceEnd(failed, false)) {
									log.error("GENESYS I/F ERROR : SERVICE END");
									return false;
								}
							} else {
								log.error("GENESYS I/F ERROR : STATE END");
								return false;
							}
						} else {
							log.error("GENESYS I/F ERROR : TASK END");
							return false;
						}
					} catch (Exception e) {
						log.error("GENESYS I/F ERROR : {}", e.getMessage());
						return false;
					}
					break;
				case CUSTOMER_CREATE :
					try {
						String customerId = this.createCustomer(failed, true);
						cstmType = "신규";
	
						if (StringUtil.isEmpty(customerId)) {
							log.error("GENESYS I/F ERROR : CREATE CUSTOMER");
							return false;
						}
						failed.put("customerId", customerId);
						failed.put("cstmType", cstmType);
						failed.put("seq", ++seq);
						log.info("============================== SERVICE START ==============================");
						String serviceId = this.serviceStart(failed, false);
						log.info("SERVICE ID : {}", serviceId);
						if (StringUtil.isEmpty(serviceId)) {
							log.error("GENESYS I/F ERROR : SERVICE START");
							return false;
						}
						failed.put("serviceId", serviceId);
	
						failed.put("seq", ++seq);
						log.info("============================== STATE START ==============================");
						String stateId = this.stateStart(failed, false);
						log.info("STATE ID : {}", stateId);
						if (StringUtil.isEmpty(stateId)) {
							log.error("GENESYS I/F ERROR : STATE START");
							return false;
						}
						failed.put("stateId", stateId);
	
						failed.put("seq", ++seq);
						log.info("============================== TASK START ==============================");
						String taskId = this.taskStart(failed, false);
						log.info("TASK ID : {}", taskId);
						if (StringUtil.isEmpty(taskId)) {
							log.error("GENESYS I/F ERROR : TASK START");
							return false;
						}
						failed.put("taskId", taskId);
	
						failed.put("seq", ++seq);
						log.info("============================== TASK END ==============================");
						if (this.taskEnd(failed, false)) {
							failed.put("seq", ++seq);
							log.info("============================== STATE END ==============================");
							if (this.stateEnd(failed, false)) {
								failed.put("seq", ++seq);
								log.info("============================== SERVICE END ==============================");
								if (!this.serviceEnd(failed, false)) {
									log.error("GENESYS I/F ERROR : SERVICE END");
									return false;
								}
							} else {
								log.error("GENESYS I/F ERROR : STATE END");
								return false;
							}
						} else {
							log.error("GENESYS I/F ERROR : TASK END");
							return false;
						}
					} catch (Exception e) {
						log.error("GENESYS I/F ERROR : {}", e.getMessage());
						return false;
					}
					break;
				case SERVICE_START :
					try {
						log.info("============================== SERVICE START ==============================");
						String serviceId = this.serviceStart(failed, true);
						log.info("SERVICE ID : {}", serviceId);
						if (StringUtil.isEmpty(serviceId)) {
							log.error("GENESYS I/F ERROR : SERVICE START");
							return false;
						}
						failed.put("serviceId", serviceId);
						
						failed.put("seq", ++seq);
						log.info("============================== STATE START ==============================");
						String stateId = this.stateStart(failed, false);
						log.info("STATE ID : {}", stateId);
						if (StringUtil.isEmpty(stateId)) {
							log.error("GENESYS I/F ERROR : STATE START");
							return false;
						}
						failed.put("stateId", stateId);
						
						failed.put("seq", ++seq);
						log.info("============================== TASK START ==============================");
						String taskId = this.taskStart(failed, false);
						log.info("TASK ID : {}", taskId);
						if (StringUtil.isEmpty(taskId)) {
							log.error("GENESYS I/F ERROR : TASK START");
							return false;
						}
						failed.put("taskId", taskId);
						
						failed.put("seq", ++seq);
						log.info("============================== TASK END ==============================");
						if (this.taskEnd(failed, false)) {
							failed.put("seq", ++seq);
							log.info("============================== STATE END ==============================");
							if (this.stateEnd(failed, false)) {
								failed.put("seq", ++seq);
								log.info("============================== SERVICE END ==============================");
								if (!this.serviceEnd(failed, false)) {
									log.error("GENESYS I/F ERROR : SERVICE END");
									return false;
								}
							} else {
								log.error("GENESYS I/F ERROR : STATE END");
								return false;
							}
						} else {
							log.error("GENESYS I/F ERROR : TASK END");
							return false;
						}
					} catch (Exception e) {
						log.error("GENESYS I/F ERROR : {}", e.getMessage());
						return false;
					}
					break;
				case STATE_START :
					try {
						log.info("============================== STATE START ==============================");
						String stateId = this.stateStart(failed, true);
						log.info("STATE ID : {}", stateId);
						if (StringUtil.isEmpty(stateId)) {
							log.error("GENESYS I/F ERROR : STATE START");
							return false;
						}
						failed.put("stateId", stateId);
						
						failed.put("seq", ++seq);
						log.info("============================== TASK START ==============================");
						String taskId = this.taskStart(failed, false);
						log.info("TASK ID : {}", taskId);
						if (StringUtil.isEmpty(taskId)) {
							log.error("GENESYS I/F ERROR : TASK START");
							return false;
						}
						failed.put("taskId", taskId);
						
						failed.put("seq", ++seq);
						log.info("============================== TASK END ==============================");
						if (this.taskEnd(failed, false)) {
							failed.put("seq", ++seq);
							log.info("============================== STATE END ==============================");
							if (this.stateEnd(failed, false)) {
								failed.put("seq", ++seq);
								log.info("============================== SERVICE END ==============================");
								if (!this.serviceEnd(failed, false)) {
									log.error("GENESYS I/F ERROR : SERVICE END");
									return false;
								}
							} else {
								log.error("GENESYS I/F ERROR : STATE END");
								return false;
							}
						} else {
							log.error("GENESYS I/F ERROR : TASK END");
							return false;
						}
					} catch (Exception e) {
						log.error("GENESYS I/F ERROR : {}", e.getMessage());
						return false;
					}
					break;
				case TASK_START :
					try {
						log.info("============================== TASK START ==============================");
						String taskId = this.taskStart(failed, true);
						log.info("TASK ID : {}", taskId);
						if (StringUtil.isEmpty(taskId)) {
							log.error("GENESYS I/F ERROR : TASK START");
							return false;
						}
						failed.put("taskId", taskId);
						
						failed.put("seq", ++seq);
						log.info("============================== TASK END ==============================");
						if (this.taskEnd(failed, false)) {
							failed.put("seq", ++seq);
							log.info("============================== STATE END ==============================");
							if (this.stateEnd(failed, false)) {
								failed.put("seq", ++seq);
								log.info("============================== SERVICE END ==============================");
								if (!this.serviceEnd(failed, false)) {
									log.error("GENESYS I/F ERROR : SERVICE END");
									return false;
								}
							} else {
								log.error("GENESYS I/F ERROR : STATE END");
								return false;
							}
						} else {
							log.error("GENESYS I/F ERROR : TASK END");
							return false;
						}
					} catch (Exception e) {
						log.error("GENESYS I/F ERROR : {}", e.getMessage());
						return false;
					}
					break;
				case TASK_END :
					try {
						log.info("============================== TASK END ==============================");
						if (this.taskEnd(failed, true)) {
							failed.put("seq", ++seq);
							log.info("============================== STATE END ==============================");
							if (this.stateEnd(failed, false)) {
								failed.put("seq", ++seq);
								log.info("============================== SERVICE END ==============================");
								if (!this.serviceEnd(failed, false)) {
									log.error("GENESYS I/F ERROR : SERVICE END");
									return false;
								}
							} else {
								log.error("GENESYS I/F ERROR : STATE END");
								return false;
							}
						} else {
							log.error("GENESYS I/F ERROR : TASK END");
							return false;
						}
					} catch (Exception e) {
						log.error("GENESYS I/F ERROR : {}", e.getMessage());
						return false;
					}
					break;
				case STATE_END :
					try {
						log.info("============================== STATE END ==============================");
						if (this.stateEnd(failed, true)) {
							failed.put("seq", ++seq);
							log.info("============================== SERVICE END ==============================");
							if (!this.serviceEnd(failed, false)) {
								log.error("GENESYS I/F ERROR : SERVICE END");
								return false;
							}
						} else {
							log.error("GENESYS I/F ERROR : STATE END");
							return false;
						}
					} catch (Exception e) {
						log.error("GENESYS I/F ERROR : {}", e.getMessage());
						return false;
					}
					break;
				case SERVICE_END :
					try {
						log.info("============================== SERVICE END ==============================");
						if (!this.serviceEnd(failed, true)) {
							log.error("GENESYS I/F ERROR : SERVICE END");
							return false;
						}
					} catch (Exception e) {
						log.error("GENESYS I/F ERROR : {}", e.getMessage());
						return false;
					}
					break;
			}
		}
		return true;
	}

	/**
	 * 기간계 후처리 전송
	 */
	private Map<String, Object> sendReviewToRegacy(String channel, String entityId, Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<>();

		if (CommonConstants.CSTM_LINK_DIV_CD_B.equals(channel)) { // 카카오 채널인 경우
			// 내용에 대분류 / 중분류 / 소분류 추가하여 보냄
			result = mcaService.sgd0038p(entityId, params);
		} else {
			// 카테고리 번호가 3자리이상인 경우 잘라서 사용함 , 기간계는 2자리만 받음
			String dep1 = StringUtil.nvl(params.get("dep1CtgNm"));
			String dep2 = StringUtil.nvl(params.get("dep2CtgNm"));
			String dep3 = StringUtil.nvl(params.get("dep3CtgNm"));

			// 카테고리 이름을 .을 기준으로 잘라서 사용함, 기간계는 2자리만 받음
			if (!Strings.isNullOrEmpty(dep1)) {
				dep1 = dep1.split("\\.")[0];
			}
			if (!Strings.isNullOrEmpty(dep2)) {
				dep2 = dep2.split("\\.")[0];
			}
			if (!Strings.isNullOrEmpty(dep3)) {
				dep3 = dep3.split("\\.")[0];
			}

			params.put("dep1CtgCd", dep1);
			params.put("dep2CtgCd", dep2);
			params.put("dep3CtgCd", dep3);
			params.put("channel", channel);

			log.info("new params ====>>>>>>>>>>> {}" + params);
			result = mcaService.sge1011p(entityId, params);
		}

		return result;
	}
	// TODO: 테스트 후 반영전 아래 메소드 전부 삭제
	/*
	 * 테스트용 SSL 인증서 무시
	 */
	private TrustManager[] getTrustManager() {
		TrustManager[] trustAllCerts = new TrustManager[] {
			new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}
			}
		};
		return trustAllCerts;
	}
	
	private HostnameVerifier getHostnameVerifier() {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String host, SSLSession session) {
				return true;
			}
		};
		return hostnameVerifier;
	}

	/**
	 * IPCC_ADV CB01_고객조회(테스트용 SSL 인증서 무시)
	 * return customerId
	 */
	@Transactional
	public String getCustomer2(@NotNull String entityId) throws Exception {
		String requestUrl = customProperty.getGenesysCsApiUrl() + "/profiles";
		log.info(">>>>>>>> GENESYS URL : {} " , requestUrl);

		TrustManager[] trustAllCerts = this.getTrustManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(requestUrl + "?CustomerNo=" + entityId);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setHostnameVerifier(getHostnameVerifier());

		Gson gson = new Gson();
		//String jsonParam = gson.toJson(new HashMap<String, String>());
		con.setDoInput(true);
		//con.setDoOutput(true);
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		//OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
		//osw.write(jsonParam.toString());
		//osw.close();

		int resCode = con.getResponseCode();
		String resMsg = con.getResponseMessage();
		log.info("GENESYS CUSTOMER SEARCH Response Code : {} Message : {}", resCode, resMsg);
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String outLine;
		while ((outLine = br.readLine()) != null) {
			sb.append(outLine);
		}
		br.close();
		log.info("GENESYS CUSTOMER SEARCH Response ID : {}", sb.toString());
		con.disconnect();
		Type t = new TypeToken<List<Map<String, String>>>() {}.getType();
		List<Map<String, String>> resultMapList = gson.fromJson(sb.toString(), t);
		
		return resultMapList.get(0).get("customer_id");
	}

	/**
	 * IPCC_ADV CB02_신규고객생성
	 * return customerId
	 */
	@Transactional
	public String createCustomer2(@NotNull String entityId) throws Exception {
		String requestUrl = customProperty.getGenesysCsApiUrl() + "/profiles";
		log.info(">>>>>>>> GENESYS URL : {} ", requestUrl);

		TrustManager[] trustAllCerts = this.getTrustManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(requestUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setHostnameVerifier(getHostnameVerifier());

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("CustomerNo", entityId);
		Gson gson = new Gson();
		String jsonParam = gson.toJson(paramMap);
		log.info("GENESYS CUSTOMER CREATE PARAMETER : {}", paramMap);

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
		osw.write(jsonParam.toString());
		osw.close();

		int resCode = con.getResponseCode();
		String resMsg = con.getResponseMessage();
		log.info("GENESYS CUSTOMER CREATE Response Code : {} Message : {}", resCode, resMsg);
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String outLine;
		while ((outLine = br.readLine()) != null) {
			sb.append(outLine);
		}
		br.close();
		log.info("GENESYS CUSTOMER CREATE Response ID : {}", sb.toString());
		con.disconnect();

		Type t = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, String> resultMap = gson.fromJson(sb.toString(), t);
		
		return resultMap.get("customer_id");
	}
	

	/**
	 * IPCC_ADV 서비스 시작 전송
	 * return serviceId
	 */
	@Transactional
	public String serviceStart2(@NotNull String customerId) throws Exception {
		String requestUrl = customProperty.getGenesysCsApiUrl() + "/services/start";
		log.info(">>>>>>>> GENESYS URL : {} ", requestUrl);

		TrustManager[] trustAllCerts = this.getTrustManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(requestUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setHostnameVerifier(getHostnameVerifier());

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("media_type", MEDIA_TYPE);
		paramMap.put("customer_id", customerId);
		paramMap.put("service_type", SERVICE_TYPE);

		log.info("GENESYS SERVICE START2 PARAMETER : {}", paramMap);

		Gson gson = new Gson();
		String jsonParam = gson.toJson(paramMap);
		log.info("GENESYS SERVICE START2 PARAMETER PARSING : {}", jsonParam.toString());

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
		osw.write(jsonParam);
		osw.flush();
		osw.close();

		int resCode = con.getResponseCode();
		String resMsg = con.getResponseMessage();
		log.info("GENESYS SERVICE START2 Response Code : {} Message : {}", resCode, resMsg);
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String outLine;
		while ((outLine = br.readLine()) != null) {
			sb.append(outLine);
		}
		br.close();
		log.info("GENESYS SERVICE START2 Response ID : {}", sb.toString());
		con.disconnect();
		return sb.toString();
	}

	/**
	 * IPCC_ADV 상태 시작 전송
	 * return stateId
	 */
	@Transactional
	public String stateStart2(@NotNull String serviceId) throws Exception {
		String requestUrl = customProperty.getGenesysCsApiUrl() + "/services/" + serviceId + "/states/start";
		log.info(">>>>>>>> GENESYS URL : {} ", requestUrl);

		TrustManager[] trustAllCerts = this.getTrustManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(requestUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setHostnameVerifier(getHostnameVerifier());

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("media_type", MEDIA_TYPE);
		paramMap.put("state_type", STATE_TYPE);

		Gson gson = new Gson();
		String jsonParam = gson.toJson(paramMap);
		log.info("GENESYS STATE START2 PARAMETER : {}", paramMap);

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
		osw.write(jsonParam.toString());
		osw.close();

		int resCode = con.getResponseCode();
		String resMsg = con.getResponseMessage();
		log.info("GENESYS STATE START2 Response Code : {} Message : {}", resCode, resMsg);
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String outLine;
		while ((outLine = br.readLine()) != null) {
			sb.append(outLine);
		}
		br.close();
		log.info("GENESYS STATE START2 Response ID : {}", sb.toString());
		con.disconnect();
		return sb.toString();
	}

	/**
	 * IPCC_ADV 태스크 시작 전송
	 * 
	 * return stateId
	 */
	@Transactional
	public String taskStart2(Map<String, Object> map) throws Exception {
		String serviceId = (String) map.get("serviceId");
		String stateId = (String) map.get("stateId");
		String channelNm = (String) map.get("channel");

		String requestUrl = customProperty.getGenesysCsApiUrl() + "/services/" + serviceId + "/tasks/start";

		log.info(">>>>>>>> GENESYS URL : {} ", requestUrl);

		TrustManager[] trustAllCerts = this.getTrustManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(requestUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setHostnameVerifier(getHostnameVerifier());

		Map<String, String> paramMap = new HashMap<String, String>();
		map.put("state_id", stateId);
		map.put("media_type", MEDIA_TYPE);
		map.put("task_type", TASK_TYPE);

		Map<String, String> custMap = new HashMap<String, String>();
		custMap.put("인입경로", channelNm);
		custMap.put("고객유무", (String) map.get("cstmType"));
		custMap.put("상담유형_상품", (String) map.get("dep1CtgNm"));
		custMap.put("상담유형_중분류", (String) map.get("dep2CtgNm"));
		custMap.put("상담유형_소분류", (String) map.get("dep3CtgNm"));
		custMap.put("상담내용", (String) map.get("content"));
		custMap.put("상담직원", (String) map.get("cnsrNm"));
		map.put("customstart", custMap);

		Gson gson = new Gson();
		String jsonParam = gson.toJson(paramMap);
		log.info("GENESYS TASK START2 PARAMETER : {}", paramMap);

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
		osw.write(jsonParam.toString());
		osw.close();

		int resCode = con.getResponseCode();
		String resMsg = con.getResponseMessage();
		log.info("GENESYS TASK START2 Response Code : {} Message : {}", resCode, resMsg);
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String outLine;
		while ((outLine = br.readLine()) != null) {
			sb.append(outLine);
		}
		br.close();
		log.info("GENESYS TASK START2 Response ID : {}", sb.toString());
		con.disconnect();
		return sb.toString();
	}

	/**
	 * IPCC_ADV 태스크 종료 전송
	 */
	@Transactional
	public String taskEnd2(Map<String, Object> map) throws Exception {
		String serviceId = (String) map.get("serviceId");
		String stateId = (String) map.get("stateId");
		String taskId = (String) map.get("stateId");
		String channelNm = (String) map.get("channelNm");
		String requestUrl = customProperty.getGenesysCsApiUrl() + "/services/" + serviceId + "/tasks/" + taskId + "/end";

		log.info(">>>>>>>> GENESYS URL : {} ", requestUrl);

		TrustManager[] trustAllCerts = this.getTrustManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(requestUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setHostnameVerifier(getHostnameVerifier());

		Map<String, String> paramMap = new HashMap<String, String>();
		map.put("state_id", stateId);
		map.put("media_type", MEDIA_TYPE);
		map.put("task_type", TASK_TYPE);

		Map<String, String> custMap = new HashMap<String, String>();
		custMap.put("인입경로", channelNm);
		custMap.put("고객유무", (String) map.get("cstmType"));
		custMap.put("상담유형_상품", (String) map.get("dep1CtgNm"));
		custMap.put("상담유형_중분류", (String) map.get("dep2CtgNm"));
		custMap.put("상담유형_소분류", (String) map.get("dep3CtgNm"));
		custMap.put("상담내용", (String) map.get("content"));
		custMap.put("상담직원", (String) map.get("cnsrNm"));
		map.put("customstart", custMap);

		Gson gson = new Gson();
		String jsonParam = gson.toJson(paramMap);
		log.info("GENESYS TASK END2 PARAMETER : {}", paramMap);

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
		osw.write(jsonParam.toString());
		osw.close();

		int resCode = con.getResponseCode();
		String resMsg = con.getResponseMessage();
		log.info("GENESYS TASK END2 Response Code : {} Message : {}", resCode, resMsg);
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String outLine;
		while ((outLine = br.readLine()) != null) {
			sb.append(outLine);
		}
		br.close();
		log.info("GENESYS TASK END2 Response ID : {}", sb.toString());
		con.disconnect();
		return sb.toString();
	}
	
	/**
	 * IPCC_ADV 상태 종료 전송
	 */
	@Transactional
	public String stateEnd2(@NotNull String serviceId, @NotNull String stateId) throws Exception {
		String requestUrl = customProperty.getGenesysCsApiUrl() + "/services/" + serviceId + "/states/" + stateId + "/end";

		TrustManager[] trustAllCerts = this.getTrustManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(requestUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setHostnameVerifier(getHostnameVerifier());

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("media_type", MEDIA_TYPE);
		paramMap.put("state_type", STATE_TYPE);

		Gson gson = new Gson();
		String jsonParam = gson.toJson(paramMap);
		log.info("GENESYS STATE START2 PARAMETER : {}", paramMap);

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
		osw.write(jsonParam.toString());
		osw.close();

		int resCode = con.getResponseCode();
		String resMsg = con.getResponseMessage();
		log.info("GENESYS STATE START2 Response Code : {} Message : {}", resCode, resMsg);
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String outLine;
		while ((outLine = br.readLine()) != null) {
			sb.append(outLine);
		}
		br.close();
		log.info("GENESYS STATE START2 Response ID : {}", sb.toString());
		con.disconnect();
		return sb.toString();
	}
	
	/**
	 * IPCC_ADV 서비스 종료 전송
	 */
	@Transactional
	public String serviceEnd2(@NotNull String serviceId, @NotNull String customerId) throws Exception {
		String requestUrl = customProperty.getGenesysCsApiUrl() + "/services/" + serviceId + "/end";

		TrustManager[] trustAllCerts = this.getTrustManager();
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(requestUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setHostnameVerifier(getHostnameVerifier());

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("media_type", MEDIA_TYPE);
		paramMap.put("customer_id", customerId);
		paramMap.put("service_type", SERVICE_TYPE);

		Gson gson = new Gson();
		String jsonParam = gson.toJson(paramMap);
		log.info("GENESYS SERVICE END2 PARAMETER : {}", paramMap);

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		//OutputStream os = con.getOutputStream();
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.write(jsonParam.getBytes("UTF-8"));
		dos.close();

		int resCode = con.getResponseCode();
		String resMsg = con.getResponseMessage();
		StringBuilder sb = new StringBuilder();
		log.info("GENESYS SERVICE END2 Response Code : {} Message : {}", resCode, resMsg);
		if (resCode == org.apache.commons.httpclient.HttpStatus.SC_NO_CONTENT) {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String outLine;
			while ((outLine = br.readLine()) != null) {
				sb.append(outLine);
			}
			br.close();
		}
		log.info("GENESYS SERVICE END2 Response ID : {}", sb.toString());
		con.disconnect();
		return sb.toString();
	}

	/**
	 * IPCC_ADV 고객여정 고객 채팅 내역 조회
	 */
	public List<Map<String, Object>> selectChatList(Map<String, Object> params) throws Exception {
		return genesysDao.selectChatList(params);
	}
	
	/**
	 * IPCC_IST 통합통계 목록 조회
	 */
	public List<String> selectIstList(@NotNull String strDate) throws Exception {
		return genesysDao.selectIstList(strDate);
	}
}