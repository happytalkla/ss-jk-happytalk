package ht.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CipherUtils {

	private static final String privateKey = "b737f0d0-5420-475b-8dca-96580f8468a8";
	private IvParameterSpec ivParameterSpec;
	private SecretKeySpec secretKeySpec;

	public CipherUtils() {

		String initVector = CipherUtils.privateKey.substring(0, 16);
		this.ivParameterSpec = new IvParameterSpec(initVector.getBytes());
		byte[] keyBytes = new byte[16];
		byte[] b = CipherUtils.privateKey.getBytes(StandardCharsets.UTF_8);
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}

		System.arraycopy(b, 0, keyBytes, 0, len);
		this.secretKeySpec = new SecretKeySpec(keyBytes, "AES");
	}

	public String encrypt(String source) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec, ivParameterSpec);
		byte[] encrypted = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8));

		return new String(Base64.encodeBase64(encrypted));
	}

	public String decrypt(String encrypted) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, ivParameterSpec);
		byte[] decodedBase64 = Base64.decodeBase64(encrypted.getBytes());

		String decrypted = new String(cipher.doFinal(decodedBase64), StandardCharsets.UTF_8);
		log.debug("decrypted: {}", decrypted);
		try {
			if (isEncrypted(decrypted)) {
				cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, ivParameterSpec);
				decodedBase64 = Base64.decodeBase64(decrypted.getBytes());
				decrypted = new String(cipher.doFinal(decodedBase64), StandardCharsets.UTF_8);
				log.debug("decrypted: {}", decrypted);
			}
		} catch (Exception e) {
			log.debug(e.getLocalizedMessage());
			return decrypted;
		}

		return decrypted;
	}

	public boolean isEncrypted(String encrypted) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, ivParameterSpec);
		byte[] decodedBase64 = Base64.decodeBase64(encrypted.getBytes());

		String decrypted = new String(cipher.doFinal(decodedBase64), StandardCharsets.UTF_8);
		String encryptedAgain = encrypt(decrypted);
		if (encrypted.equals(encryptedAgain)) {
			return true;
		} else {
			return false;
		}
	}
}
