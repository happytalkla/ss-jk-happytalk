package ht.controller.mock;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ht.constants.CommonConstants;
import ht.service.BatchService;
import ht.util.HTUtils;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TestController {

	@Resource
	private BatchService batchService;

	/**
	 * 배치잡 수동 실행
	 */
	@GetMapping(value = "/api/test/runBatch")
	public ResponseEntity<String> testBatch(
			@RequestParam("jobId") @NotEmpty String jobId,
			@RequestParam(value = "dateTime", required = false) String dateTime,
			@RequestParam(value = "startDate", required = false) String startDate, // yyyymmdd_hh24miss
			@RequestParam(value = "endDate", required = false) String endDate, // yyyymmdd_hh24miss
			@RequestParam(value = "limit", required = false, defaultValue = "0") Integer limit)
	{
		try {
			switch (jobId) {
			case CommonConstants.BATCH_JOB_ASSIGN:
				batchService.runAssignScheduler();
				break;

			case CommonConstants.BATCH_JOB_AUTOSTOP:
				batchService.runAutoEndChatRoom();
				break;

			case CommonConstants.BATCH_JOB_CLEANROOM:
				batchService.runCleanChatRoom();
				break;

			case CommonConstants.BATCH_JOB_LOGIN30CHECK:
				batchService.runLoginMember();
				break;

			case CommonConstants.BATCH_JOB_BREAKTIME:
				batchService.runCounselorBreakTime();
				break;

			case CommonConstants.BATCH_JOB_STATISTICS:
				batchService.runSaveStatistics();
				break;

			case CommonConstants.BATCH_JOB_STATISCHATBOT:
				batchService.runSaveStatChat();
				break;

			case CommonConstants.BATCH_JOB_DELETE_LOG:
				batchService.runDeleteLogs();
				break;

			case CommonConstants.BATCH_JOB_END_INFO_TO_BSP:
				// http://localhost:8080/happytalk/api/test/runBatch?jobId=END_INFO_TO_BSP
//				batchService.runEndInfoToBSP(startDate, endDate, limit);
				break;

				//			case CommonConstants.BATCH_JOB_ENCRYPT_SINFO:
				//				batchService.runEncryptSInfo(dateTime);
				//				break;
				//
				//			case CommonConstants.BATCH_JOB_ENCRYPT_END_MEMO:
				//				batchService.runEncryptEndMemo(dateTime);
				//				break;
			/*
			 * IPCC_ADV 고객여정 I/F 실패 건 재송신
			 */
			case CommonConstants.BATCH_JOB_RESEND_GENESYS:
				batchService.runResendGenesys();
				break;
			/*
			 * IPCC_IST 통합통계 FTP 파일 전송
			 */
			case CommonConstants.BATCH_JOB_FILE_SEND_IST:
				batchService.runIstFileSend();
				break;
			default:
				log.info("INVALID JOB ID: {}", jobId);
				break;
			}
		} catch (Exception e) {
			HTUtils.batmanNeverDie(e);
			log.error("{}", e.getLocalizedMessage(), e);
			return new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * 배치잡 조회
	 */
	@GetMapping(value = "/api/test/batch")
	public ResponseEntity<List<Map<String, Object>>> selectBatch()
	{
		return new ResponseEntity<>(batchService.selectBatchJob(), HttpStatus.OK);
	}

	/**
	 * 배치잡 수동 실행
	 */
	@GetMapping(value = "/api/test/istbatchtest")
	public ResponseEntity<String> istbatchtest()
	{
		batchService.testrunIstFileSend();

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	/**
	 * 배치잡 수동 실행
	 */
	@GetMapping(value = "/api/test/batch/{switch}")
	public ResponseEntity<String> switchBatch(
			@PathVariable("switch") @NotEmpty @Pattern(regexp = "on|off") String switchs)
	{
		log.info("SECRET BATCH SWITCH: {}", switchs);
		if ("on".equals(switchs)) {
			batchService.updateBatchJob("N");
		} else if ("off".equals(switchs)) {
			batchService.updateBatchJob("Y");
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
