package com.example.nettyserver.config.http;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

import java.util.Map;

/**
 * @ClassName HttpUtil
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/26 10:49
 * @Version 1.0
 */
public class HttpUtil {

    public static String getRequestContent(FullHttpRequest request){
        String body = null;
        if(request.content().isReadable()){
            body = request.content().toString(CharsetUtil.UTF_8);
        }
        return body;
    }

    public static Map<String, Object> getParameterMap(FullHttpRequest request){

        return null;
    }

}
