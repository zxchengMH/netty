package com.example.nettyserver.config.handler.mapper;

import com.example.nettyserver.config.http.NettyHttpRequest;

/**
 * 自定义 servlet 处理接口
 */
public interface IServletHandler {

    /**
     * 请求映射初始化方法
     * 在spring容器启动时执行
     */
    default void init() {

    }

    /**
     * q请求业务处理方法
     * @param request
     * @return
     */
    String service(NettyHttpRequest request);
}
