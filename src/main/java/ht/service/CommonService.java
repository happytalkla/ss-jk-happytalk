package ht.service;

import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.domain.ChatMessage;
import ht.domain.ChatMessageFile;
import ht.exception.BizException;
import ht.persistence.CommonDao;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonService {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private CommonDao commonDao;
	@Resource
	private HTUtils htUtils;

	/**
	 * 공통 코드 목록 조회
	 */
	public List<Map<String, Object>> selectCodeList(String cdGroup) {
		return commonDao.selectCommonCodeList(cdGroup);
	}

	/**
	 * 현재 년, 월, 일 조회
	 */
	public Timestamp selectSysdate() {
		return commonDao.selectSysdate();
	}

	/**
	 * 현재 날짜 조회
	 */
	public Map<String, Object> selectNowDate() {
		return commonDao.selectNowDate();
	}

	/**
	 * 특정 년-월 조회
	 * 특정 년-월-일 조회
	 */
	public Map<String, Object> selectCustomDate(Map<String, Object> param) {
		return commonDao.selectCustomDate(param);
	}

	/**
	 * 특정 일자 조회
	 *
	 * @param add 0:현재, -1:어제, 1:내일
	 * @return 날짜 {@code yyyymmdd}
	 */
	public String selectDateToString(int add) {
		String rtnDate = "";
		Map<String, Object> param = new HashMap<>();
		param.put("schDateType", "DAY");
		param.put("schDayType", "add");
		param.put("schAdd", add);

		Map<String, Object> dateMap = commonDao.selectCustomDate(param);
		if (dateMap != null) {
			rtnDate = StringUtil.nvl(dateMap.get("sel_day"));
			rtnDate = rtnDate.replaceAll("-", "");
		}

		return rtnDate;
	}

	/**
	 * 특정 일자 조회
	 *
	 * @param add 0:현재, -1:어제, 1:내일
	 * @return 날짜 {@code yyyy-mm-dd}
	 */
	public String selectDateToStringFormat(int add) {
		String rtnDate = "";
		Map<String, Object> param = new HashMap<>();
		param.put("schDateType", "DAY");
		param.put("schDayType", "add");
		param.put("schAdd", add);

		Map<String, Object> dateMap = commonDao.selectCustomDate(param);
		if (dateMap != null) {
			rtnDate = StringUtil.nvl(dateMap.get("sel_day"));
		}

		return rtnDate;
	}

	/**
	 * 과거인지 현재인지 미래인지 체크
	 */
	public int selectNowDateCheck(String schDate) {
		return commonDao.selectNowDateCheck(schDate);
	}

	/**
	 * 공통 로그 등록
	 */
	public void insertLog(String logDivCd, String succText, String logCont, String sessionMemberUid) {

		try {
			Map<String, Object> logMap = new HashMap<>();
			logMap.put("logDivCd", logDivCd);
			logMap.put("succText", succText);
			if (logCont != null && logCont.length() > 4000) {
				logCont = logCont.substring(0, 4000);
			}
			logMap.put("logCont", logCont);
			logMap.put("sessionMemberUid", sessionMemberUid);

			commonDao.insertLog(logMap);

		} catch (Exception e) {
			log.error("logDivCde : {}, succText : {}, sessionMemberUid : {}", logDivCd, succText, sessionMemberUid);
			e.printStackTrace();
			log.error("{}", e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 공통 로그 목록 조회
	 */
	public List<Map<String, Object>> selectLogList(Map<String, Object> param) {
		return commonDao.selectLogList(param);
	}

	/**
	 * 업로드 파일
	 */
	@Transactional
	public Map<String, Object> upload(MultipartFile originalFile, String parentPathName) {

		return upload(originalFile, null, null, parentPathName);
	}

	/**
	 * 업로드 파일
	 */
	@Transactional
	public Map<String, Object> upload(MultipartFile originalFile, String chatRoomUid, String userId, String parentPathName) {

		Map<String, Object> uploadFileInfo = new HashMap<>();

		File uploadPath = new File(customProperty.getStoragePath() + File.separator + "upload");
		if (!Strings.isNullOrEmpty(parentPathName)) {
			uploadPath = new File(customProperty.getStoragePath() + File.separator + "upload" + File.separator + parentPathName);
		}
		if (!uploadPath.exists()) {
			if (!uploadPath.mkdirs()) {
				log.error("CAN NOT CREATE DIRECTORY: {}", uploadPath.getAbsolutePath());
				uploadFileInfo.put("resultMessage", "디렉토리를 생성할 수 없습니다.");
				return uploadFileInfo;
			}
		}
		if (!uploadPath.isDirectory()) {
			log.error("NOT DIRECTORY: {}", uploadPath.getAbsolutePath());
			uploadFileInfo.put("resultMessage", "디렉토리가 아닙니다.");
			return uploadFileInfo;
		}
		if (!uploadPath.canWrite()) {
			log.error("NOT WRITABLE DIRECTORY: {}", uploadPath.getAbsolutePath());
			uploadFileInfo.put("resultMessage", "파일을 쓸 권한이 없습니다.");
			return uploadFileInfo;
		}
		
		String mineType = originalFile.getContentType();
		log.info("originalfilename===>>>>>>>>>>" + originalFile.getOriginalFilename());
		log.info("name===>>>>>>>>>>" + originalFile.getName());
		String fileName = UUID.randomUUID() + "__" + System.currentTimeMillis() + ".png";
		
		if(!mineType.contains("image")) {
			fileName = UUID.randomUUID() + "__" + System.currentTimeMillis() + StringUtil.getFileExtension(originalFile.getOriginalFilename());
		}
		
		return writeFile(originalFile, uploadPath, fileName, parentPathName);
	}

	private Map<String, Object> writeFile(MultipartFile originalFile, File uploadPath, String fileName, String parentPathName) {

		Map<String, Object> uploadFileInfo = new HashMap<>();
		File uploadFile = null;

		String mineType = originalFile.getContentType();
		try {
			if (originalFile.isEmpty()) {
				log.error("Empty file: {}", originalFile.getName());
				uploadFileInfo.put("resultMessage", "EMPTY FILE");
				return uploadFileInfo;
			}
			log.info("content type: {}", originalFile.getContentType());
			log.info("upload path: {}", uploadPath);
			uploadFile = new File(uploadPath, fileName);
			FileCopyUtils.copy(originalFile.getBytes(), uploadFile);
			uploadFile.setReadable(true, false);
			log.info("UPLOADED FILE: {}", uploadFile.getAbsoluteFile());

			if (!Strings.isNullOrEmpty(parentPathName)) {
				uploadFileInfo.put("url", customProperty.getUploadUrlBase()
						+ File.separator + parentPathName
						+ File.separator + uploadFile.getName());
			} else {
				uploadFileInfo.put("url", customProperty.getUploadUrlBase()
						+ File.separator + uploadFile.getName());
			}
			if(mineType.contains("image")) {
				uploadFileInfo.put("mimeType", "image/png");
			}else {
				uploadFileInfo.put("mimeType", mineType);
			}
			//uploadFileInfo.put("mimeType", new Tika().detect(uploadFile));
			// uploadFileInfo.put("title", originalFile.getOriginalFilename());
			uploadFileInfo.put("title", originalFile.getName());
			log.debug("{}", Files.probeContentType(uploadFile.toPath()));

			if (isValidFile(uploadFileInfo)) {
				// if (!htUtils.scp(uploadFile, customProperty.getUploadCopyIp())) {
				// uploadFile.delete();
				// uploadFileInfo = new HashMap<>();
				// uploadFileInfo.put("resultMessage", "FAIL TO SCP");
				// return uploadFileInfo;
				// }
				return uploadFileInfo;
			} else {
				uploadFile.delete();
				uploadFileInfo = new HashMap<>();
				uploadFileInfo.put("resultMessage", "NOT SUPPORTED FILE TYPE");
				return uploadFileInfo;
			}
		} catch (Exception e) {
			if (uploadFile != null) {
				uploadFile.delete();
			}

			log.error("{}", e.getLocalizedMessage(), e);
			uploadFileInfo = new HashMap<>();
			uploadFileInfo.put("resultMessage", e.getLocalizedMessage());
			return uploadFileInfo;
		}
	}

	private boolean isValidFile(Map<String, Object> uploadFile) {

		if (uploadFile.get("mimeType") == null) {
			log.error("NOT FOUND FILE TYPE");
			uploadFile.put("resultMessage", "NOT FOUND FILE TYPE");
		} else if (((String) uploadFile.get("mimeType")).indexOf("image") == 0) {
			return true;
		} else if (((String) uploadFile.get("mimeType")).indexOf("pdf") > -1) {
			return true;			
		} else {
			log.error("NOT SUPPORTED FILE TYPE: {}", uploadFile.get("mimeType"));
			uploadFile.put("resultMessage", "NOT SUPPORTED FILE TYPE");
		}

		return false;
	}

	/**
	 * 파일 메세지 저장
	 */
	@Transactional
	public int insertChatAttFile(ChatMessage chatMessage) {

		int d = 0;

		if (chatMessage.getFileList() == null) {
			return 0;
		}

		for (ChatMessageFile file : chatMessage.getFileList()) {

			log.debug("\n{}\n{}", chatMessage.getCont(), file.getFileNm());

			Map<String, Object> params = new HashMap<>();
			params.put("chatNum", chatMessage.getChatNum());
			params.put("fileNm", file.getFileNm());
			params.put("fileType", file.getFileType());

			d += commonDao.insertChatAttFile(params);
			file.setFileNum((String) params.get("fileNum"));
			log.debug("file: {}", file);
		}

		assert (d == chatMessage.getFileList().size());
		return d;
	}

	/**
	 * 스킨 정보 조회
	 */
	public Map<String, Object> selectCustomerSkin() {
		return commonDao.selectCustomerSkin();
	}

	/**
	 * 배치 시작 여부 판단
	 */
	@Transactional
	public boolean checkBatchStart(String jobId, String flag) {
		Map<String, Object> params = new HashMap<>();
		params.put("jobId", jobId);
		params.put("flag", flag);
		params.put("startYn", "Y");

		boolean rtnFlag = false;
		if (!StringUtil.isEmpty(commonDao.checkBatchStart(params))) {
			commonDao.updateBatchStart(params);
			rtnFlag = true;
		}
		return rtnFlag;
	}

	/**
	 * 배치 종료 처리
	 */
	@Transactional
	public void checkBatchEnd(String jobId, String flag) {
		Map<String, Object> params = new HashMap<>();
		params.put("jobId", jobId);
		params.put("flag", flag);
		params.put("startYn", "N");
		commonDao.updateBatchStart(params);
	}

	@Transactional
	public void insertTest() throws Exception {
		try {
			commonDao.insertTest();
		} catch (Exception e) {
			throw new BizException("err1234", "오류");
		}

	}

	/**
	 * 두 부서 중 상대 부서 코드
	 */
	public Map<String, Object> getCounterPartDepartCode(@NotEmpty String departCd, @NotEmpty List<Map<String, Object>> departList)
			throws BizException {

		for (Map<String, Object> depart : departList) {
			if (!departCd.equals(depart.get("cd"))) {
				return depart;
			}
		}

		throw new BizException("부서 정보를 찾을 수 없습니다.");
	}
}
