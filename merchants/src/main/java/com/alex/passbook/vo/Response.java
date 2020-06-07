package com.alex.passbook.vo;

import lombok.Data;

/**
 * @author wsh
 * @date 2020-06-02
 * <h1>通用的相应对象</h1>
 */
@Data
public class Response {

    //错误码  正确返回 0
    private Integer errorCode = 0;

    //错误信息 正确返回空字符串
    private String errorMsg = "";

    //返回值对象
    private Object data;

    public Response(){

    }

    /**
     * <h2>正确的相应返回对象<h2/>
     * @param data 返回值对象
     */
    public Response(Object data) {
        this.data = data;
    }


}

