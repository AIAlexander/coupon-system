package com.alex.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsh
 * @date 2020-08-02
 * <h1>统一报错信息</h1>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo<T> {
    /** 错误码 **/
    public static final Integer ERROR = -1;

    /** 特定错误码 **/
    private Integer code;

    /** 错误信息 **/
    private String message;

    /** 请求url **/
    private String url;

    private T data;
}
