package ht.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * API 요청 결과
 */
@Data
@AllArgsConstructor
@JsonPropertyOrder({ "returnCode", "returnMessage", "result" })
@ApiModel("API 요청 결과")
public class ApiItemWrapper<T> {

    /**
     * 결과 코드
     */
    @ApiModelProperty(value = "결과 코드")
	ApiReturnCode returnCode;

    /**
     * 결과 메세지
     */
    @ApiModelProperty(value = "결과 메세지")
    String returnMessage;

    /**
     * 요청 결과
     */
    @ApiModelProperty(value = "요청 결과")
    T result;

    public ApiItemWrapper() {

        this.returnCode = ApiReturnCode.SUCCEED;
    }

    public ApiItemWrapper(ApiReturnCode returnCode) {

        this.returnCode = returnCode;
    }

    public ApiItemWrapper(ApiReturnCode returnCode, String returnMessage) {

        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
    }

    public ApiItemWrapper(T result) {

        this.returnCode = ApiReturnCode.SUCCEED;
        this.result = result;
    }
}
