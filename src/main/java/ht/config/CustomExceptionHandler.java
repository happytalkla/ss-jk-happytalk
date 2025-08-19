package ht.config;

import com.google.common.base.Strings;
import ht.domain.ApiItemWrapper;
import ht.domain.ApiReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * {@link Controller}로 전달 받은 요청에 대한 예외 처리, 에러 페이지 노출
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

	/**
	 * 잘못된 요청시 예외 처리
	 */
	@ExceptionHandler({
		MissingRequestHeaderException.class,
		MethodArgumentTypeMismatchException.class,
		IllegalArgumentException.class,
		IllegalStateException.class
	})
	@Nullable
	protected String handleBadRequest(Exception ex, WebRequest request) {

		log.error("BadRequest Exception Handler: {}", ex.getLocalizedMessage());
		return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * 미구현 요청시 예외 처리
	 */
	@ExceptionHandler({
		UnsupportedOperationException.class
	})
	@Nullable
	protected String handleNotImplement(RuntimeException ex, WebRequest request) {

		log.error("NotImplement Exception Handler: {}", ex.getLocalizedMessage());
		return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED, request);
	}

	/**
	 * DataAccess 예외 처리
	 */
	@ExceptionHandler({
		DataAccessException.class,
		DataRetrievalFailureException.class
	})
	@Nullable
	protected String handleDataAccess(DataAccessException ex, WebRequest request) {

		log.error("DataAccess Exception Handler: {}", ex.getLocalizedMessage());
		return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	/**
	 * 그 외 모든 예외 처리
	 */
	@ExceptionHandler({
		Exception.class
	})
	@Nullable
	protected String handleDefault(Exception ex, WebRequest request) {

		log.error("Default Exception Handler: {}", ex.getLocalizedMessage());
		return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	/**
	 * 결과 포맷 정규화
	 */
	private String handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		ServletWebRequest httpServletRequest = (ServletWebRequest) request;
		String requestPath = httpServletRequest.getRequest().getRequestURI();
		String requestQuery = httpServletRequest.getRequest().getQueryString();
		if (!Strings.isNullOrEmpty(requestQuery)) {
			requestPath += "?" + requestQuery;
		}
		log.error("REQUEST PATH: {}", requestPath);
		Iterator<String> headerIterator = httpServletRequest.getHeaderNames();
		while (headerIterator.hasNext()) {
			String headerName = headerIterator.next();
			log.error("REQUEST HEADER: {}: {}", headerName, httpServletRequest.getHeader(headerName));
		}

		try {
			//            log.error("requestBody: {}", IOUtils.toString(httpServletRequest.getRequest().getReader()));
			String requestBody = IOUtils.toString(httpServletRequest.getRequest().getInputStream(), StandardCharsets.UTF_8);
			if (!Strings.isNullOrEmpty(requestBody)) {
				log.error("REQUEST BODY: {}", requestBody);
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}

		ApiItemWrapper<String> result = new ApiItemWrapper<>(ApiReturnCode.FAILED);
		if (body != null) {
			result.setReturnMessage(body.toString());
		} else {
			result.setReturnMessage(ex.getLocalizedMessage());
		}

		log.error("CustomExceptionHandler\n", ex);
		//ex.printStackTrace();

		return "error/error";
	}
}
