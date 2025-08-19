package ht.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;

import ht.domain.ApiItemWrapper;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;
import ht.service.EaiUmsService;
/**
 * EAI 관련 controller
 * EAI 전송 
 *
 * @author wizard
 *
 */

@Controller
@Slf4j
public class EaiUmsController {

	@Resource
	private EaiUmsService eaiUmsService;
	@Resource
	private HTUtils htUtils;

	@CrossOrigin
	@PostMapping(path = "/eaiums/sendUms")
	@ResponseBody
	public ResponseEntity<ApiItemWrapper<String>> sendUms(@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
			@RequestParam Map<String, Object> param) {
		try {
			
			param.put("pushType", "고객상태 idle");
	
			log.info("param : {}", param);
			
			eaiUmsService.throwEai(param);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

}
