package com.example.nettyserver.config.http;

/**
 * @ClassName RequestMethod
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/25 16:36
 * @Version 1.0
 */
public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private RequestMethod() {
    }
}
