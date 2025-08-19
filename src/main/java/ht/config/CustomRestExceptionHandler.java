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
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * {@link RestController}로 전달 받은 요청에 대한 예외 처리,
 * {@link ResponseEntityExceptionHandler}에서 잡는 예외는 처리하지 않음
 */
//@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

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
    protected ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {

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
    protected ResponseEntity<Object> handleNotImplement(RuntimeException ex, WebRequest request) {

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
    protected ResponseEntity<Object> handleDataAccess(DataAccessException ex, WebRequest request) {

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
    protected ResponseEntity<Object> handleDefault(Exception ex, WebRequest request) {

        log.error("Default Exception Handler: {}", ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 결과 포맷 정규화
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
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

        return super.handleExceptionInternal(ex, result, headers, status, request);
    }
}
