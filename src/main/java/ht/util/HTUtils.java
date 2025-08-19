package ht.util;

import static ht.constants.CommonConstants.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import ht.config.CustomProperty;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HTUtils {

	@Resource
	private Environment environment;
	@Resource
	private CustomProperty customProperty;

	/**
	 * 스프링 프로파일 확인
	 */
	public boolean isActiveProfile(String profile) {

		return Arrays.stream(environment.getActiveProfiles()).anyMatch(
				env -> (env.equalsIgnoreCase(profile)));
	}

	/**
	 * 모바일 디바이스 판별
	 */
	public boolean isMobile(String userAgent) {

		if (userAgent == null) {
			return false;
		}

		String lowerUserAgent = userAgent.toLowerCase();
		return lowerUserAgent.contains("iphone")
				|| lowerUserAgent.contains("android");
	}

	/**
	 * 디바이스 구분
	 */
	public String getDeviceType(@NotEmpty String userAgent, boolean isDiffMobileOS) {

		String lowerUserAgent = userAgent.toLowerCase();
		if (lowerUserAgent.contains("iphone")) {
			if (isDiffMobileOS) {
				return CSTM_OS_DIV_CD_IOS;
			} else {
				return CSTM_OS_DIV_CD_MOBILE;
			}
		} else if (lowerUserAgent.contains("android")) {
			if (isDiffMobileOS) {
				return CSTM_OS_DIV_CD_ANDROID;
			} else {
				return CSTM_OS_DIV_CD_MOBILE;
			}
		}

		return CSTM_OS_DIV_CD_WEB;
	}

	/**
	 * 클라이언트 IP 주소
	 */
	public String getRemoteIpAddress(HttpHeaders headers, HttpServletRequest request) {

		for (String key : headers.keySet()) {
			log.debug("HEADER: {}:{}", key, headers.get(key));
		}

		String ipAddr = "";

		if (headers.get("X-Forwarded-For") != null) {
			ipAddr = Joiner.on(",").join(headers.get("X-Forwarded-For"));
			log.debug("X-Forwarded-For: {}", headers.get("X-Forwarded-For"));
		}

		if (headers.get("X-Original-Host") != null) {
			ipAddr = Joiner.on(",").join(headers.get("X-Original-Host"));
			log.debug("X-Original-Host: {}", headers.get("X-Original-Host"));
		}

		if (Strings.isNullOrEmpty(ipAddr) && headers.get("Proxy-Client-IP") != null) {
			ipAddr = Joiner.on(",").join(headers.get("Proxy-Client-IP"));
			log.debug("Proxy-Client-IP: {}", headers.get("Proxy-Client-IP"));
		}

		if (Strings.isNullOrEmpty(ipAddr) && headers.get("HTTP_CLIENT_IP") != null) {
			ipAddr = Joiner.on(",").join(headers.get("HTTP_CLIENT_IP"));
			log.debug("HTTP_CLIENT_IP: {}", headers.get("HTTP_CLIENT_IP"));
		}

		if (Strings.isNullOrEmpty(ipAddr) && headers.get("HTTP_X_FORWARDED_FOR") != null) {
			ipAddr = Joiner.on(",").join(headers.get("HTTP_X_FORWARDED_FOR"));
			log.debug("HTTP_X_FORWARDED_FOR: {}", headers.get("HTTP_X_FORWARDED_FOR"));
		}

		if (Strings.isNullOrEmpty(ipAddr) && headers.get("true-client-ip") != null) {
			ipAddr = Joiner.on(",").join(headers.get("true-client-ip"));
			log.debug("true-client-ip: {}", headers.get("true-client-ip"));
		}

		// log.debug("UPGRADE: {}", headers.get(HttpHeaders.UPGRADE));

		if (Strings.isNullOrEmpty(ipAddr) && request.getRemoteAddr() != null) {
			ipAddr = request.getRemoteAddr();
			log.debug("getRemoteAddr: {}", request.getRemoteAddr());
		}

		return ipAddr;
	}

	private final static int port = 22;
	private final static String sshId = "chatbot";
	private final static String sshPw = "chatbot12#$";

	/**
	 * {@param file}을 {@param host}로 복사 (SCP)
	 */
	public boolean scp(File file, String host) throws Exception {

		if ("localhost".equals(host)) {
			log.info("NOT PRODUCTION SERVER");
			return true;
		}

		Session session = null;
		Channel ch = null;
		ChannelSftp chSftp = null;

		try {
			JSch jsch = new JSch();
			// 접속 정보
			session = jsch.getSession(sshId, host, port);
			session.setPassword(sshPw);

			// 세션 config 설정
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			// 세션 연결
			session.connect();

			// 체널 오픈
			ch = session.openChannel("sftp");
			// 체널 연결
			ch.connect();

			// 체널을 ftp체널 객체로 케스팅
			chSftp = (ChannelSftp) ch;

			// 카피파일 (jsch)
			FileInputStream fi = new FileInputStream(file);

			// 디렉토리 만들어보고 있으면 스킵
			try {
				chSftp.mkdir(file.getParent());
			} catch (SftpException e) {
				log.info(e.getLocalizedMessage());
			}
			chSftp.cd(file.getParent());
			chSftp.put(fi, file.getName());

			// sftp 연결 종료
			chSftp.quit();
			fi.close();
			session.disconnect();
			return true;

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return false;
		}
	}

	public boolean ftpUpload(@NotEmpty String hostName, @Positive Integer port, @NotEmpty String user, @NotEmpty String passwd, @NotEmpty String remotePath,@NotEmpty String fileName, @NotEmpty String contents) {

		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8");

		try {
			ftpClient.connect(hostName, port);

			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			}

			ftpClient.setSoTimeout(1000 * 10);
			ftpClient.login(user, passwd);

			//			FTPListParseEngine engine = ftpClient.initiateListParsing("/public");
			//			while (engine.hasNext()) {
			//				FTPFile[] fileList = engine.getNext(10);
			//				if (fileList != null) {
			//					for (int i = 0; i < fileList.length; i++) {
			//						FTPFile file = fileList[i];
			//						log.info("{}", file);
			//					}
			//				}
			//			}

			ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);

			try {
				ftpClient.makeDirectory(remotePath);
			} catch (Exception ee) {
				log.error("{}", ee.getLocalizedMessage(), ee);
			}

			Path path = Files.createTempFile("__FTP__", "__TMP__");
			Files.write(path, contents.getBytes());

			FileInputStream fileInputStream = new FileInputStream(path.toFile());
			boolean result = ftpClient.storeFile(remotePath + fileName, fileInputStream);

			fileInputStream.close();
			ftpClient.disconnect();

			return result;

		} catch (Exception e) {
			log.info(e.getLocalizedMessage(), e);
			return false;
		}
	}

	/**
	 * IPCC_IST SFTP 파일 전송
	 */
	public boolean sftpFlieUpload(@NotEmpty String hostName, @Positive Integer port, @NotEmpty String user, @NotEmpty String passwd, @NotEmpty String remotePath, @NotEmpty String fileName, @NotEmpty String contents) throws Exception {

		Session session = null;
		Channel ch = null;
		ChannelSftp chSftp = null;

		try {
			JSch jsch = new JSch();
			// 접속 정보
			session = jsch.getSession(user, hostName, port);
			session.setPassword(passwd);
			
			// 세션 config 설정
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			// 세션 연결
			session.connect();
			// 체널 오픈
			ch = session.openChannel("sftp");
			// 체널 연결
			ch.connect();
			// 체널을 ftp체널 객체로 케스팅
			chSftp = (ChannelSftp) ch;

			Path path = Files.createTempFile("__FTP__", "__TMP__");
			Files.write(path, contents.getBytes());
			File f = new File(path.toUri());
			// 카피파일 (jsch)
			FileInputStream fi = new FileInputStream(f);
			// 디렉토리 만들어보고 있으면 스킵
			try {
				chSftp.mkdir(remotePath);
			} catch (SftpException e) {
				log.info(e.getLocalizedMessage());
			}
			chSftp.cd(remotePath);
			chSftp.put(fi, fileName);

			// sftp 연결 종료
			chSftp.quit();
			fi.close();
			session.disconnect();
			return true;

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return false;
		}
	}

	static public void batmanNeverDie(Exception e) {

		log.debug(e.getLocalizedMessage(), e);
	}

	static public void batmanNeverDie(Exception e, Object o) {

		log.debug(e.getLocalizedMessage(), e);
	}
}
