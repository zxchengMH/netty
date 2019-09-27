package com.example.nettyserver.config.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

/**
 * @ClassName NettyHttpResponse
 * @Description TODO
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/24 17:30
 * @Version 1.0
 */
public class NettyHttpResponse extends DefaultFullHttpResponse {

    private String content;

    public NettyHttpResponse(HttpResponseStatus status) {
        this(status, Unpooled.buffer(0));
    }

    public NettyHttpResponse(HttpResponseStatus status, ByteBuf content) {
        super(HttpVersion.HTTP_1_1, status, content);
        headers().set(CONTENT_TYPE, "application/json");
        headers().setInt(CONTENT_LENGTH, content().readableBytes());

        /**支持CORS 跨域访问*/
        headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept, RCS-ACCESS-TOKEN");
        headers().set(ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE");
    }

    public static NettyHttpResponse build(HttpResponseStatus status, String content){

        if(content == null){
            return new NettyHttpResponse(status);
        }
        ByteBuf buf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
        NettyHttpResponse response = new NettyHttpResponse(status, buf);
        response.content = content;
        return response;
    }

    public static FullHttpResponse ok(String context){

        return NettyHttpResponse.build(HttpResponseStatus.OK, context);
    }

    public static FullHttpResponse error(HttpResponseStatus status, String context){

        return NettyHttpResponse.build(status, context);
    }

    public static FullHttpResponse notFound(){

        return NettyHttpResponse.build(HttpResponseStatus.NOT_FOUND, null);
    }

    public static FullHttpResponse unauthorized(){

        return NettyHttpResponse.build(HttpResponseStatus.UNAUTHORIZED, null);
    }

    @Override
    public String toString() {
        super.toString();
        StringBuilder builder = new StringBuilder(125);
        builder.append("\n");
        builder.append(StringUtil.NEWLINE);
        builder.append(protocolVersion().toString()).append(" ").append(status().toString()).append("\n");
        builder.append(CONTENT_TYPE).append(": ").append(headers().get(CONTENT_TYPE)).append("\n");
        builder.append(DATE).append(": ").append(content).append("\n");
        return builder.toString();
    }

}
