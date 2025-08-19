package ht.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import ht.config.CustomProperty;
import ht.service.CommonService;
import ht.service.ScpAgentService;
import ht.service.SettingService;

@Controller
public class UtilController {

	@Resource
	private SettingService settingService;
	@Resource
	private CustomProperty customProperty;
	@Resource
	private CommonService commonService;
	@Resource
	private SimpMessageSendingOperations messagingTemplate;
	@Resource
	private ScpAgentService scpAgtService;

	@PostMapping("/api/upload")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("parentPathName") String parentPathName) {

		Map<String, Object> uploadFile = commonService.upload(file, null, null, parentPathName);

		if (uploadFile == null) {
			uploadFile = new HashMap<>();
			uploadFile.put("resultMessage", "UNKNOWN ISSUE");
			return new ResponseEntity<>(uploadFile, HttpStatus.NOT_ACCEPTABLE);
		}

		// return new ResponseEntity<Map<String, Object>>(uploadFile,
		// HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return new ResponseEntity<>(uploadFile, HttpStatus.CREATED);
	}
	
	@GetMapping("/api/encrpt")
	@ResponseBody
	public ResponseEntity<String> encriptString(@RequestParam(value = "source", required = true) String source) {

		String encStr = "";
		try {
			encStr = scpAgtService.getEncString(source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(e.toString(), HttpStatus.NOT_ACCEPTABLE);
		}

		// return new ResponseEntity<Map<String, Object>>(uploadFile,
		// HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return new ResponseEntity<>(encStr, HttpStatus.CREATED);
	}	
	
	@GetMapping("/api/decrpt")
	@ResponseBody
	public ResponseEntity<String> decriptString(@RequestParam(value = "source", required = true) String source) {

		String decStr = "";
		try {
			decStr = scpAgtService.getDecString(source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(e.toString(), HttpStatus.NOT_ACCEPTABLE);
		}

		// return new ResponseEntity<Map<String, Object>>(uploadFile,
		// HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return new ResponseEntity<>(decStr, HttpStatus.CREATED);
	}		
}
