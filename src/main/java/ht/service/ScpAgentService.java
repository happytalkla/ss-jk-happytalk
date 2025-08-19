package ht.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.ValidationException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.penta.scpdb.ScpDbAgent;

import ht.config.CustomProperty;
import ht.util.HTUtils;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScpAgentService {
	@Resource
	private CustomProperty customProperty;
	@Resource
	private HTUtils htUtils;
	@Resource
	private RestTemplate externalRestTemplate;

	private static final String privateKey = "b737f0d0-5420-475b-8dca-96580f8468a8";
	private IvParameterSpec ivParameterSpec;
	private SecretKeySpec secretKeySpec;

	public static final int ENC_KEY = 0 ;				//공통키 
	public static ScpDbAgent agt;
	public static byte[] ctx0;
	
	public static String SiteKey = "w!hasdfghjklqkrr@nqkrd#ladla$dydwl%sqjax^oxoru&dckdc*jfqja(rbdnt)jdrlr!b";
	private String iniPath;
	private String agentId;
	private String dbName;
	private String owner;
	private String tblName;

	public byte[] init() {
		if(ctx0 == null) {
			log.info("=====================scp init==============================");
			log.info("iniPath : {}", customProperty.getDamoIniPath());
			log.info("agentId : {}", customProperty.getDamoAgentId());
			log.info("dbName : {}", customProperty.getDamoDbName());
			log.info("owner : {}", customProperty.getDamoOwner());
			log.info("tableName : {}", customProperty.getDamoTblName());
			File file = null;
	
			try {
				file = ResourceUtils.getFile(customProperty.getDamoIniPath());
				iniPath = file.getAbsolutePath();
				log.info("iniPath filepath ==>>>>" + iniPath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("===>>>error : {}" + e.toString());
			}
			
			//iniPath = customProperty.getDamoIniPath();
			agentId = customProperty.getDamoAgentId().trim();
			dbName = customProperty.getDamoDbName().trim();
			owner = customProperty.getDamoOwner().trim();
			tblName =  customProperty.getDamoTblName().trim();
			agt = new ScpDbAgent();
			
			int ret = agt.AgentInit(iniPath);
			
			log.info("===========scp ret : {}", ret);
			
			//D'Amo서버에서 암호Key 받아오기
			try {
				log.info("agentId : {}", agentId);
				log.info("dbName : {}", dbName);
				log.info("owner : {}", owner);
				log.info("tblName : {}", tblName);
				
				log.info("agt : {}", agt);
				
				ctx0 = agt.AgentCipherCreateContext( agentId, dbName, owner, tblName, "ENC_KEY" );
	
			}catch (Exception e) {
				// TODO: handle exception
				log.info("========>>>>>>>>>createContextException : {}", e.toString());
			}
			log.info("========>>>>>>>>>ctx0 : {}", ctx0);
			log.info("=====================scp end==============================");			
		}
		return ctx0;
	}

	public String encrypt(String source, byte[] ctx) throws Exception {

		byte[] enc;
		
		String encrypted="";
		try {
			if(StringUtil.isEmpty(source)) {
				throw new Exception("String is null");
			}else if(StringUtil.isEmpty(rtrim(source))) {
				return source;
			}
			
			source = rtrim(source);
			
			enc = agt.AgentCipherEncryptB64(ctx, source);
			log.info("AgentCipherEncryptString : {}" , enc);

			encrypted = new String(enc);
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("===========encrpyt error : {}", e.toString());
		}
		
		return encrypted;
	}
	public String decrypt(String encrypted, byte[] ctx) throws Exception {
		String decrypted = new String(agt.AgentCipherDecryptB64(ctx, encrypted));
		return decrypted;
	}

	public static String rtrim(String s) {
		char[] val = s.toCharArray();
		int st = 0;
		int len = s.length();
		while (st < len && val[len-1] <= ' ') {
			len--;
		}
		return s.substring(0, len);
	}	
	
	public String getEncString(String source) throws Exception {
		if(htUtils.isActiveProfile("local")){
			String res="";
			String url = "https://tchat.ss.local/happytalk/api/encrpt?source="+ source;
			try{
				res = externalRestTemplate.getForObject(url, String.class);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return res;
		}else {
			//init();
			return encrypt(source, init()).replaceAll("+", "-").replaceAll("/", "_");
		}

	}
	
	
	public String getDecString(String source) throws Exception {
		if(htUtils.isActiveProfile("local")){
			String res="";
			String url = "https://tchat.ss.local/happytalk/api/decrpt?source="+ source;
			try{
				res = externalRestTemplate.getForObject(url, String.class);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return res;
		}else {
			//init();
			source = source.replaceAll("-", "+").replaceAll("_", "/");
			return decrypt(source, init());
		}		

	}
}
