package com.alex.passbook.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>日志生成器</h1>
 */
@Slf4j
public class LogGenerator {
    /**
     * <h2>生成log</h2>
     * @param request {@link HttpServletRequest}
     * @param userId 用户id
     * @param action 用户操作
     * @param info 日志信息
     */
    public static void generateLog(HttpServletRequest request, Long userId, String action, Object info){
        log.info(
                JSON.toJSONString(new LogObject(action, userId, System.currentTimeMillis(), request.getRemoteAddr(), info))
        );
    }
}
