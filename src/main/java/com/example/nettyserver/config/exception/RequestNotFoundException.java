package com.example.nettyserver.config.exception;

/**
 * 自定义异常，请求映射不存在
 */
public class RequestNotFoundException extends Exception {

    public RequestNotFoundException() {

        super("REQUEST MAPPER NOT FOUND");
    }

    public RequestNotFoundException(String message) {

        super(message);
    }
}
