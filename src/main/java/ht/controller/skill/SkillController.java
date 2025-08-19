package ht.controller.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.print.DocFlavor.STRING;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Strings;

import ht.constants.CommonConstants;
import ht.domain.ApiItemWrapper;
import ht.domain.ChatRoom;
import ht.mca.vo.Igv2001pVO;
import ht.service.AuthService;
import ht.service.ChatRoomService;
import ht.service.McaService;
import ht.service.SkillService;
import ht.service.legacy.MappingCustomerService;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SkillController {

	@Resource
	private AuthService authService;
//	@Resource
//	private MappingCustomerService mappingCustomerService;
	@Resource
	private McaService mcaService;
	@Resource
	private SkillService skillService;
	@Resource
	private ChatRoomService chatRoomService;
	/**
	 * 인증 정보
	 */
	@GetMapping(value = "/skill/auth-type")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> getAuth(
			@RequestParam("chatRoomUid") @NotEmpty String chatRoomUid) throws Exception {

		log.info("SKILL, AUTH, CHAT ROOM UID: {}", chatRoomUid);

		Map<String, Object> auth = authService.selectAuth(chatRoomUid);
		if (auth == null) {
			//{coc_id=1234, create_dt=1584269486000, cstm_uid=fc9a5273-6715-49b6-b8a4-9894a9788546, chat_room_uid=ff4f1d04-549e-498f-a431-25f01f337742, expire_dt=1584269486000, auth_type=none, auth_id=1})
			auth = new HashMap<>();
			auth.put("chat_room_uid", chatRoomUid);
			auth.put("auth_type", "none");
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(auth);
		return new ResponseEntity<>(apiResult, HttpStatus.OK);
	}

	/**
	 * O2 APP 의 화면이름 가져오기
	 */
	@GetMapping(value = "/skill/o2app")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> o2app(
			@RequestParam("appCode") String screenNum) throws Exception {

		log.info("SKILL, LEGACY USER INFO, USER ID: {}", screenNum);
		HashMap<String, Object> params = new HashMap<>();
		params.put("screenNum", screenNum);

		Map<String, Object> lon = skillService.selectO2appName(params);

		if(lon == null) {
			lon = new HashMap<>();
			lon.put("screen_name", "요청하신 화면 이름이 없습니다");
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}


	/**
	 * 고객기본 정보
	 */
	@GetMapping(value = "/skill/sgd1611p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> sgd1611p(
			@RequestParam("userId") String userId) throws Exception {

		log.info("SKILL, LEGACY USER INFO, USER ID: {}", userId);

		Map<String, Object> lon = mcaService.sgd1611p(userId, CommonConstants.MCA_CHANNEL_CODE);
		if(lon== null) {
			lon = new HashMap<>();
			lon.put("CLNT_NAME", "고객정보가 없습니다.");
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}

	private Map<String, Object> getDefaultResMsg(List<String> cols){
		String defMsg = "상품정보가 없습니다.";
		Map<String, Object> res = new HashMap<>();
		for (String column : cols) {
			res.put(column, defMsg);
		}

		return res;
	}

	/**
	 * 상품전환프로그램
	 */
	@GetMapping(value = "/skill/pfdz101p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> getChangePrdCd(
			@RequestParam(value="goodCode", required=false) String goodCode) throws Exception {

		log.info("SKILL, LEGACY CHANG PRD PROGRAM, Product Id: {}", goodCode);
		Map<String, Object> map = new HashMap<>();

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode)) {
			map.putAll(getDefaultResMsg(Arrays.asList("PRDT_NAME")));
		}else {
			map = getProductInfo(goodCode);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(map);
		if (map == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}

	private Map<String, Object> getProductInfo(String goodCode){
		log.info("SKILL, getProductInfo, Product Id: {}", goodCode);

		String entityId = "";
		Map<String, Object> map = null;
		try {
			map = mcaService.pfdz101p(entityId, goodCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(map ==  null) {
				map = new HashMap<>();
				map.put("RPRS_ISCD", "");
			}
		}

		if(map ==  null) {
			map = new HashMap<>();
			map.put("RPRS_ISCD", "");
		}

		return map;
	}
	/**
	 * 펀드 기본정보
	 */
	@GetMapping(value = "/skill/igv2001p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> igv2001p(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ZRN_FUND_CD = ""+ map.get("RPRS_ISCD");
		String ZRN_DT = ""+ map.get("STNR_DATE");
		String FUND_CODE = ""+ map.get("FUND_CODE");

		log.info("SKILL, igv2001p, ZRN_FUND_CD: {}, ZRN_DT: {}, FUND_CODE: {}", ZRN_FUND_CD, ZRN_DT, FUND_CODE);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ZRN_FUND_CD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("ZRN_OPER_CO_NM", "MGCM_FUND_NAME")));
		}else {
			lon = mcaService.igv2001p(entityId, ZRN_FUND_CD, ZRN_DT, FUND_CODE);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}


	/**
	 * 펀드 유형
	 */
	@GetMapping(value = "/skill/igv9251p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> igv9251p(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ZRN_FUND_CD = ""+ map.get("RPRS_ISCD");
		String ZRN_DT = ""+ map.get("STNR_DATE");
		String FUND_CODE = ""+ map.get("FUND_CODE");

		log.info("SKILL, igv9251p, ZRN_FUND_CD: {}", ZRN_FUND_CD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ZRN_FUND_CD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("ZRN_CLASS_NM")));
		}else {
			lon = mcaService.igv9251p(entityId, ZRN_FUND_CD);
		}
		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}


	/**
	 * 펀드 수익률
	 */
	@GetMapping(value = "/skill/igv9252p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> igv9252p(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ZRN_FUND_CD = ""+ map.get("RPRS_ISCD");

		log.info("SKILL, igv9252p, ZRN_FUND_CD: {}", ZRN_FUND_CD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ZRN_FUND_CD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("FUND_NAME", "ZRN_FUND_NM")));
		}else {
			lon = mcaService.igv9252p(entityId, ZRN_FUND_CD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}

	/**
	 * 펀드 동일유형 수익률
	 */
	@GetMapping(value = "/skill/igv9253p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> igv9253p(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ZRN_FUND_CD = ""+ map.get("RPRS_ISCD");

		log.info("SKILL, igv9253p, ZRN_FUND_CD: {}", ZRN_FUND_CD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ZRN_FUND_CD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("ZRN_M1_RTN_0")));
		}else {
			lon = mcaService.igv9253p(entityId, ZRN_FUND_CD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}

	/**
	 * 펀드3
	 */
	@GetMapping(value = "/skill/igv4205p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> igv4205p(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ZRN_FUND_CD = ""+ map.get("RPRS_ISCD");
		String STNR_DATE = ""+ map.get("STNR_DATE");
		String ZRN_ZRN_CD = ""+ map.get("ZRN_CLASS_CD");
		log.info("SKILL, igv4205p, STNR_DATE: {}, ZRN_FUND_CD: {} , ZRN_ZRN_CD: {}", STNR_DATE, ZRN_FUND_CD, ZRN_ZRN_CD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ZRN_FUND_CD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("ZRN_MKT_BM_NM_0")));
		}else {
			lon = mcaService.igv4205p(entityId, STNR_DATE, ZRN_FUND_CD, ZRN_ZRN_CD);
		}
		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}

	/**
	 * 펀드4
	 */
	@GetMapping(value = "/skill/igv4206p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> igv4206p(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ZRN_FUND_CD = ""+ map.get("RPRS_ISCD");
		String STNR_DATE = ""+ map.get("STNR_DATE");
		String ZRN_ZRN_CD = ""+ map.get("ZRN_CLASS_CD");

		log.info("SKILL, igv4206p, STNR_DATE: {}, ZRN_FUND_CD: {}, ZRN_ZRN_CD: {}", STNR_DATE, ZRN_FUND_CD, ZRN_ZRN_CD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ZRN_FUND_CD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("ZRN_SEC_NM_0")));
		}else {
			lon = mcaService.igv4206p(entityId, STNR_DATE, ZRN_FUND_CD, ZRN_ZRN_CD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}

	/**
	 * 펀드가 투자하는 상위 업종, 상위종목
	 * @param goodCode
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/skill/fundInvestInfo")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> fundInvestInfo(
			@RequestParam("goodCode") String goodCode) throws Exception {
		Map<String, Object> lon = new HashMap<>();
		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ZRN_FUND_CD = ""+ map.get("RPRS_ISCD");
		String STNR_DATE = ""+ map.get("STNR_DATE");
		String ZRN_ZRN_CD = ""+ map.get("ZRN_CLASS_CD");
		log.info("SKILL, igv4205p, STNR_DATE: {}, ZRN_FUND_CD: {} , ZRN_ZRN_CD: {}", STNR_DATE, ZRN_FUND_CD, ZRN_ZRN_CD);

		String entityId = "";

		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ZRN_FUND_CD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("ZRN_MKT_BM_NM_0")));
		}else {
			Map<String, Object> map1 =  new HashMap<>();
			map1 = mcaService.igv4205p(entityId, STNR_DATE, ZRN_FUND_CD, ZRN_ZRN_CD);
			lon.putAll(map1);

			Map<String, Object> map2 =  new HashMap<>();
			map2 = mcaService.igv4206p(entityId, STNR_DATE, ZRN_FUND_CD, ZRN_ZRN_CD);
			lon.putAll(map2);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}


	/**
	 * ELS1
	 */
	@GetMapping(value = "/skill/aimp02gp")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> aimp02gp(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ISCD = ""+ map.get("RPRS_ISCD");

		log.info("SKILL, aimp02gp, ISCD: {}", ISCD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ISCD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("PRDT_NAME")));
		}else {
			lon = mcaService.aimp02gp(entityId, "02", ISCD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}


	/**
	 * ELS2
	 */
	@GetMapping(value = "/skill/aimp024p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> aimp024p(
			@RequestParam("goodCode") String goodCode) throws Exception {
		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ISCD = ""+ map.get("RPRS_ISCD");
		String A_CHK_CODE = "1";

		log.info("SKILL, aimp024p, A_CHK_CODE: {}, ISCD : {}", A_CHK_CODE, ISCD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ISCD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("UNAS_DSCN_CTNT")));
		}else {
			lon = mcaService.aimp024p(entityId, A_CHK_CODE, "02", ISCD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}
	/**
	 * ELS2
	 */
	@GetMapping(value = "/skill/aimp024p2")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> aimp024p2(
			@RequestParam("goodCode") String goodCode) throws Exception {
		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ISCD = ""+ map.get("RPRS_ISCD");
		String A_CHK_CODE = "2";

		log.info("SKILL, aimp024p, A_CHK_CODE: {}, ISCD : {}", A_CHK_CODE, ISCD);

		String entityId = "";

		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ISCD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("UNAS_DSCN_CTNT")));
		}else {
			lon = mcaService.aimp024p(entityId, A_CHK_CODE, "02", ISCD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}
	/**
	 * ELS2
	 */
	@GetMapping(value = "/skill/aimp024p3")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> aimp024p3(
			@RequestParam("goodCode") String goodCode) throws Exception {
		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ISCD = ""+ map.get("RPRS_ISCD");
		String A_CHK_CODE = "3";

		log.info("SKILL, aimp024p, A_CHK_CODE: {}, ISCD : {}", A_CHK_CODE, ISCD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ISCD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("UNAS_DSCN_CTNT")));
		}else {
			lon = mcaService.aimp024p(entityId, A_CHK_CODE, "02", ISCD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}
	/**
	 * ELS2
	 */
	@GetMapping(value = "/skill/aimp024p4")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> aimp024p4(
			@RequestParam("goodCode") String goodCode) throws Exception {
		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ISCD = ""+ map.get("RPRS_ISCD");
		String A_CHK_CODE = "4";

		log.info("SKILL, aimp024p, A_CHK_CODE: {}, ISCD : {}", A_CHK_CODE, ISCD);

		String entityId = "";

		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ISCD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("UNAS_DSCN_CTNT")));
		}else {
			lon = mcaService.aimp024p(entityId, A_CHK_CODE, "02", ISCD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}


	/**
	 * ELS1
	 */
	@GetMapping(value = "/skill/elsInfo")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> elsInfo(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ISCD = ""+ map.get("RPRS_ISCD");

		log.info("SKILL, aimp02gp, ISCD: {}", ISCD);

		String entityId = "";
		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ISCD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("PRDT_NAME")));
		}else {
			Map<String, Object> map1 = new HashMap<>();
			map1 = mcaService.aimp02gp(entityId, "02", ISCD);
			lon.putAll(map1);

			String A_CHK_CODE = "1";
			Map<String, Object> map2 = new HashMap<>();
			map2 = mcaService.aimp024p(entityId, A_CHK_CODE, "02", ISCD);
			lon.putAll(map2);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);

		return new ResponseEntity<>(apiResult, HttpStatus.OK);
	}

	/**
	 * 채권
	 */
	@GetMapping(value = "/skill/aigp015p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> aigp015p(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ISCD = ""+ map.get("RPRS_ISCD");

		log.info("SKILL, aigp015p, ISCD : {}", ISCD);

		String entityId = "";

		Map<String, Object> lon = new HashMap<>();
		lon.putAll(map);

		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ISCD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("PRDT_NAME", "ISUS_ABBR_NAME", "ISUS_ENNM", "ISUS_ABBR_ENNM")));
		}else {
			lon = mcaService.aigp015p(entityId, ISCD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}
	/**
	 * 채권수익률
	 */
	@GetMapping(value = "/skill/pga0011p")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> pga0011p(
			@RequestParam("goodCode") String goodCode) throws Exception {

		Map<String, Object> map = getProductInfo(goodCode);
		log.info("map : {}", map);

		String ISCD = ""+ map.get("RPRS_ISCD");
		String anctDate = ""+ map.get("STNR_DATE");

		log.info("SKILL, pga0011p, ISCD : {}, anctDate : {}", ISCD, anctDate);

		String entityId = "";

		Map<String, Object> lon = new HashMap<>();
		if(Strings.isNullOrEmpty(goodCode) || "null".equals(goodCode) || Strings.isNullOrEmpty(ISCD)) {
			lon.putAll(getDefaultResMsg(Arrays.asList("PRDT_ID", "ISUS_ABBR_NAME")));
		}else {
			lon = mcaService.pga0011p(entityId, anctDate, ISCD);
		}

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}

	/**
	 * 고객기본정보와 상품명
	 */
	@GetMapping(value = "/skill/cstmInfoPrd")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> cstmInfoPrd(
			@RequestParam("userId") String userId, @RequestParam("goodCode") String goodCode) throws Exception {

		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(userId);
		String CocId = chatRoom.getRoomCocId();

		Map<String, Object> lon = new HashMap<>();

		Map<String, Object> user = mcaService.sgd1611p(CocId, CommonConstants.MCA_CHANNEL_CODE);

		Map<String, Object> product = getProductInfo(goodCode);
		log.info("user : {}", user);
		log.info("product : {}", product);

		lon.putAll(user);
		lon.putAll(product);

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}

	/**
	 * 고객기본정보와 채권정보
	 */
	@GetMapping(value = "/skill/cstmInfoBond")
	public ResponseEntity<ApiItemWrapper<Map<String, Object>>> cstmInfoBond(
			@RequestParam("userId") String userId, @RequestParam("goodCode") String goodCode) throws Exception {

		ChatRoom chatRoom = chatRoomService.selectChatRoomByChatRoomUid(userId);
		String CocId = chatRoom.getRoomCocId();
		Map<String, Object> lon = new HashMap<>();

		Map<String, Object> user = mcaService.sgd1611p(userId, CommonConstants.MCA_CHANNEL_CODE);

		Map<String, Object> product = getProductInfo(goodCode);
		log.info("user : {}", user);
		log.info("product : {}", product);

		String ISCD = ""+ product.get("RPRS_ISCD");

		log.info("SKILL, aigp015p, ISCD : {}", ISCD);

		String entityId = userId;

		lon = mcaService.aigp015p(entityId, ISCD);
		lon.putAll(user);
		lon.putAll(product);

		ApiItemWrapper<Map<String, Object>> apiResult = new ApiItemWrapper<>(lon);
		if (lon == null) {
			return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(apiResult, HttpStatus.OK);
		}
	}
}
