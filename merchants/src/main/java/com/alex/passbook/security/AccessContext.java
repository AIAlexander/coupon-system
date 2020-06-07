package com.alex.passbook.security;

/**
 * @author wsh
 * @date 2020-05-27
 * <h1>使用 ThreadLocal 去单独存储每一个线程携带的 Token 信息</h1>
 */
public class AccessContext {

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static String getToken(){
        return token.get();
    }

    public static void setToken(String tokenString){
        token.set(tokenString);
    }

    public static void clearAccessKey(){
        token.remove();
    }
}
