package com.alex.passbook.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>日志对象</h1>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogObject {

    /** 日志动作类型 **/
    private String action;

    /** 当前用户id **/
    private Long userId;

    /** 时间戳 **/
    private Long timestamp;

    /** 客户端ip地址 **/
    private String remoteIp;

    /** 日志内容 **/
    private Object info = null;
}
