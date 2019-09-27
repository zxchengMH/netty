package com.example.nettyserver.config.handler;

import com.example.nettyserver.config.http.NettyHttpResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

/**
 * @ClassName InterceptorHandler
 * @Description 拦截器，处理一些请求的校验
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/24 17:36
 * @Version 1.0
 */
public class InterceptorHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest request = (FullHttpRequest) msg;
        //1.判断是否为websocket连接请求，以及验证请求合法性
        HttpHeaders headers = request.headers();
        String upgrade = headers.get("Upgrade");
        if(upgrade != null && upgrade.length() > 0 && upgrade.equals("websocket")){
            //使用加密算法，校验用户密文正确性
            if(!checkWebSocket(request)){
                sendHttpResponse(ctx, (FullHttpRequest)msg, NettyHttpResponse.unauthorized());
            }
            ctx.fireChannelRead(msg);
            return;
        }

        //2.校验HTTP请求的合法性
        if(!checkHttp(request)){
            sendHttpResponse(ctx, (FullHttpRequest)msg, NettyHttpResponse.notFound());
        }
        ctx.fireChannelRead(msg);
    }

    private boolean checkWebSocket(FullHttpRequest request){
        String uri = request.uri();
        int post = uri.indexOf("ws/");
        String userSign = uri.substring(post + 3);
        //使用加密算法，校验用户密文正确性
        if(checkUser(userSign) == null){
            return false;
        }
        request.headers().set("uid", userSign);
        return true;
    }

    private boolean checkHttp(FullHttpRequest request){
        return true;
    }

    /**
     * 校验用户的合法性
     * @param userSign
     * @return <>null</> 不合法， 合法则返回该用户唯一标识
     */
    private String checkUser(String userSign){
        return userSign;
    }

    /**
     * Http返回
     * @param ctx
     * @param request
     * @param response
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {

        // 返回应答给客户端
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        // 如果是非Keep-Alive，关闭连接
        if (!HttpHeaders.isKeepAlive(request)) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
