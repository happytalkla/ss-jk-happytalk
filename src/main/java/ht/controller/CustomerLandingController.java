package ht.controller;

import static ht.constants.CommonConstants.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ht.config.CustomProperty;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 고객 랜팅 페이지
 */
@Controller
@Slf4j
public class CustomerLandingController {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private HTUtils htUtils;

	@GetMapping(value = "/customer/landing")
	public String landing(
			ModelMap model,
			@RequestHeader(value = "User-Agent") String userAgent,
			@RequestParam("code") @NotEmpty String code,
			@RequestParam Map<String, String> params,
			RedirectAttributes attributes) throws Exception {

		log.info("CUSTOMER LANDING, CODE: {}, PARAMS: {}", code, params);
		// 디바이스 구분
		String deviceType = htUtils.getDeviceType(userAgent, true);

		String pcUrl = "redirect:";
		String mobileUrl = "redirect:";

		switch (code) {
		// 앱 다운로드
		case "appDownLoad":
			if (CSTM_OS_DIV_CD_IOS.equals(deviceType)) {
				mobileUrl += "https://itunes.apple.com/kr/app/ok-" + URLEncoder.encode("저축은행", StandardCharsets.UTF_8.name()) + "/id973344869?mt=8";
			} else if (CSTM_OS_DIV_CD_ANDROID.equals(deviceType)) {
				mobileUrl += "https://play.google.com/store/apps/details?id=com.cabsoft.oksavingbank";
			} else {
				pcUrl = "error/error";
				mobileUrl = "error/error";
				model.put("alert", "모바일에서 이용해 주시기 바랍니다.");
				model.put("message", "모바일에서 이용해 주시기 바랍니다.");
			}
			break;
		default:
			pcUrl = "error/error";
			mobileUrl = "error/error";
			break;
		}

		String result = pcUrl;
		if (!CSTM_OS_DIV_CD_WEB.equals(deviceType)) {
			result = mobileUrl;
		}

		for (String key : params.keySet()) {
			if (!"code".equals(key)) {
				attributes.addAttribute(key, params.get(key));
			}
		}

		log.info("CUSTOMER LANDING, RESULT URL: {}", result);

		return result;
	}
}
