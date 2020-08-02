package com.alex.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsh
 * @date 2020-08-02
 * <h1>Controller通用返回对象</h1>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /** 错误码：正确返回0 */
    private Integer errorCode = 0;

    /** 成功返回的是空 */
    private String errorMsg = "";

    /** 返回对象 */
    private Object data;

    /**
     * 正确信息的返回对象的构造函数
     * @param data
     */
    public Response(Object data){
        this.data = data;
    }

    /**
     * 正确信息的空响应
     * @return
     */
    public static Response success() {
        return new Response();
    }

    /**
     * 错误响应
     * @param errorMsg
     * @return
     */
    public static Response error(String errorMsg){
        return new Response(-1, errorMsg, null);
    }
}
