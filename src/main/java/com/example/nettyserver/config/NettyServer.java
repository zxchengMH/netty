package com.example.nettyserver.config;

import com.example.nettyserver.config.handler.InterceptorHandler;
import com.example.nettyserver.config.handler.WebSocketConnectHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description NettyServer Netty服务器配置
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/20 9:29
 * @Version 1.0
 */
@Component
public class NettyServer implements CommandLineRunner {

    @Value("${netty.port}")
    private Integer port;


    @Override
    public void run(String... args) throws Exception {
        start();
    }

    public void start() throws Exception {

        //bossGroup 线程组实际就是 Acceptor 线程池，负责处理客户端的 TCP 连接请求。
        // 如果系统只有一个服务端端口需要监听，则建议 bossGroup 线程组线程数设置为 1
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //workerGroup 是真正负责 I/O 读写操作的线程组，通过 ServerBootstrap 的 group 方法进行设置，用于后续的 Channel 绑定
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.group(bossGroup, workerGroup)                  // 绑定线程池
            .channel(NioServerSocketChannel.class)            // 指定使用的channel
            .localAddress(this.port)                          // 绑定监听端口
            .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作
                  @Override
                  protected void initChannel(SocketChannel ch) throws Exception {
                       System.out.println("收到新连接");
                       ChannelPipeline pipeline = ch.pipeline();

                       pipeline.addLast(new HttpServerCodec());         //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                       pipeline.addLast(new ChunkedWriteHandler());     //以块的方式来写的处理器,WebSocket通信支持
                       pipeline.addLast(new HttpObjectAggregator(65536)); //数据包最大长度
                       pipeline.addLast(new InterceptorHandler());
                       pipeline.addLast(new WebSocketConnectHandler()); //为每个连个ws连接创建一个连接通达对象
                       pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true));
                  }
            });
            ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
            System.out.println(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());
            cf.channel().closeFuture().sync();  // 关闭服务器通道
        } finally {
            // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }
}
