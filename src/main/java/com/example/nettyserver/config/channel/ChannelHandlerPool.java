package com.example.nettyserver.config.channel;

import com.example.nettyserver.config.handler.WebSocketConnectHandler;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 通道组池，管理当前服务的所有websocket连接
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/20 9:38
 * @Version 1.0
 */
public class ChannelHandlerPool {

    private static ConcurrentHashMap<String, WebSocketConnectHandler> ChannelHandler = new ConcurrentHashMap<>();

    /**
     * 新增一个用户的连接通道
     * @param userSign
     * @param connect
     */
    public static void add(String userSign, WebSocketConnectHandler connect){

        ChannelHandler.put(userSign, connect);
        //save to redis
    }

    /**
     * 移除一个用户的连接通道
     * @param userSing
     */
    public static void remove(String userSing){

        ChannelHandler.remove(userSing);
        //remove form redis
    }

    /**
     * 获取所有的连接用户
     * @return
     */
    public static List<String> getAll(){

        List<String> rsLst = new ArrayList<>(ChannelHandler.size());
        Enumeration<String> keys = ChannelHandler.keys();
        while (keys.hasMoreElements()){
            rsLst.add(keys.nextElement());
        }
        return rsLst;
    }

    /**
     * 判断某个用户是否连接在线
     * @param userSign
     * @return
     */
    public static boolean get(String userSign){

        return ChannelHandler.containsKey(userSign);
    }

    /**
     * 发送消息
     * @param receiver 指定接收用户
     * @param msg 消息内容
     */
    public static void sendMessage(String receiver, String msg){
        WebSocketConnectHandler context = ChannelHandler.get(receiver);
        if(context == null){
            //System.out.println("用户["+receiver+"]暂时不在线");
            return;
        }
        context.sendWebSocketMessage(msg);
    }

}
