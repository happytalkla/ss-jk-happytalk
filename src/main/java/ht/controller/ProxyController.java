package ht.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProxyController {

	@Resource
	private RestTemplate restTemplate;

	/**
	 * 카카오 상담톡, 파일 프록시
	 *
	 * https://api.line.me/v2/bot/message/:messageId/content
	 * http://local.test:8854/static/channel/naver/line/file/39/U25a93fc83cb2b93b540588482f0a0faf/9783986735539/contents
	 *
	 * @param channelServiceId 채널 서비스 식별자
	 * @param channelMessageId 메세지 식별자
	 * @return
	 */
	@GetMapping("/proxy/kakao")
	@ResponseBody
	public StreamingResponseBody getNaverLineFile(@RequestParam("url") String url) {

		log.info("REQUEST KAKAO COUNSEL FILE: URL: {}", url);

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		ResponseEntity<org.springframework.core.io.Resource> responseEntity = restTemplate.exchange(
				url, HttpMethod.GET, request, org.springframework.core.io.Resource.class);

		try {
			if (responseEntity.getBody() != null) {
				InputStream inputStream = responseEntity.getBody().getInputStream();
				return (outputStream) -> {
					redirectStream(inputStream, outputStream);
				};
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}

		return null;
	}

	/**
	 * InputStream to OutputStream
	 *
	 * @param inputStream InputStream
	 * @param outputStream OutputStream
	 * @throws IOException
	 */
	private void redirectStream(InputStream inputStream, OutputStream outputStream) throws IOException {

		byte[] buffer = new byte[8192];
		int readBytes;

		while ((readBytes = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, readBytes);
		}

		outputStream.flush();
	}
}
