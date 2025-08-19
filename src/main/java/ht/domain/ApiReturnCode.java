package ht.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * API 응답 코드
 */
@ApiModel("API 응답 코드")
public enum ApiReturnCode {

    @ApiModelProperty(value = "정상")
    SUCCEED,

    @ApiModelProperty(value = "실패")
    FAILED
}
