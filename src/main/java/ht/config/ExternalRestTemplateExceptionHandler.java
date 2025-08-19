package ht.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

/**
 * '외부 접속용 클라이언트' 에러 핸들러
 */
@Component
public class ExternalRestTemplateExceptionHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
						|| httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse)
			throws IOException {

//		if (httpResponse.getStatusCode()
//				.series() == HttpStatus.Series.SERVER_ERROR) {
//			;
//		} else if (httpResponse.getStatusCode()
//				.series() == HttpStatus.Series.CLIENT_ERROR) {
//			;
////			if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
////				;
////			}
//		}

		// log.error("EXTERNAL REST TEMPLATE: {}", IOUtils.toString(new InputStreamReader(httpResponse.getBody(), StandardCharsets.UTF_8)));
		throw new RestClientException(httpResponse.getStatusCode() + ": " + httpResponse.getStatusText());
	}
}
