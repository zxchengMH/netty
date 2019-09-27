package com.example.nettyserver.config.handler;

import com.example.nettyserver.config.channel.ChannelHandlerPool;
import com.example.nettyserver.config.exception.RequestNotFoundException;
import com.example.nettyserver.config.handler.mapper.IServletHandler;
import com.example.nettyserver.config.handler.mapper.RequestMappingHandler;
import com.example.nettyserver.config.http.NettyHttpRequest;
import com.example.nettyserver.config.http.NettyHttpResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

/**
 * @ClassName WebSocketConnectHandler
 * @Description Netty 会为每一个连接创建一个连接channel，由该对象维护该通道
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/20 9:49
 * @Version 1.0
 */
public class WebSocketConnectHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    /**
     * 连接通道
     */
    private ChannelHandlerContext ctx;

    /**
     * 通道所属用户标识
     */
    private String userSign;

    /**
     * channel 通道 action 活跃的 当客户端主动链接服务端的链接后，这个通道就是活跃的了。
     * 也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        System.out.println("与客户端建立连接，通道开启！");
    }

    /**
     * channel 通道 Inactive 不活跃的 当客户端主动断开服务端的链接后，这个通道就是不活跃的。
     * 也就是说客户端与服务端关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        System.out.println("与客户端断开连接，通道关闭！");
        //移除
        ChannelHandlerPool.remove(this.userSign);
    }

    /**
     * 接收客户端发送的消息 channel 通道 Read 读 简而言之就是从通道中读取数据，也
     * 就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

        if (o instanceof FullHttpRequest) { //传统的HTTP接入(首次连接是FullHttpRequest)

            handleHttpRequest(channelHandlerContext, (FullHttpRequest)o);
        } else if (o instanceof WebSocketFrame) { //WebSocket接入

            handleWebSocketFrame(channelHandlerContext, (TextWebSocketFrame)o);
        }
    }

    /**
     * 处理Http请求，完成WebSocket握手<br/>
     * 注意：WebSocket连接第一次请求使用的是Http
     * @param ctx
     * @param request
     * @throws Exception
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception{

        System.out.println(request.uri());
        // 如果HTTP解码失败，返回HTTP异常
        if (!request.getDecoderResult().isSuccess()) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        String upgrade = request.headers().get("Upgrade");
        if(upgrade != null && upgrade.equals("websocket")){
            handlerWebSocket(ctx, request);
        }else {
            handlerHttp(ctx, request);
        }
    }

    /**
     * 处理普通的HTTP请求
     * @param ctx
     * @param request
     * @throws Exception
     */
    private void handlerHttp(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception{
        try {
            IServletHandler handler = RequestMappingHandler.matchFunctionHandler(request);
            String result = handler.service(new NettyHttpRequest(request));
            sendHttpResponse(ctx, request, NettyHttpResponse.ok(result));
        }catch (RequestNotFoundException e){
            e.printStackTrace();
            sendHttpResponse(ctx, request, NettyHttpResponse.notFound());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理websocket连接的http请求
     * @param ctx
     * @param request
     */
    private void handlerWebSocket(ChannelHandlerContext ctx, FullHttpRequest request){
        // 正常WebSocket的Http连接请求，构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory("ws://" + request.headers().get(HttpHeaders.Names.HOST),
                        null, false);
        handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null) { // 无法处理的websocket版本
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else { // 向客户端发送websocket握手,完成握手

            String userSign = request.headers().get("uid");
            this.ctx = ctx;            //记录管道处理上下文，便于服务器推送数据到客户端
            this.userSign = userSign;  //记录当前通道的关联用户，方便注销使用使用
            ChannelHandlerPool.add(userSign, this);
            handshaker.handshake(ctx.channel(), request);
        }
    }

    /**
     * 处理Socket请求
     * @param ctx
     * @param frame
     * @throws Exception
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception{
        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 当前只支持文本消息，不支持二进制消息
        if ((frame instanceof TextWebSocketFrame)) {
            //获取发来的消息
            String text =((TextWebSocketFrame)frame).text();
            System.out.println("msg : " + text);
            //消息转成MessageBean
            //MessageBean message = JSONObject.parseObject(text, MessageBean.class);

        }
    }

    /**
     * Http返回
     * @param ctx
     * @param request
     * @param response
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        // 返回应答给客户端
        if (response.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(response, response.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        if (!HttpHeaders.isKeepAlive(request) || response.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 向该通道的连接着发送消息
     * @param msg
     */
    public void sendWebSocketMessage(String msg){

        this.ctx.writeAndFlush(new TextWebSocketFrame(msg));
    }
}
