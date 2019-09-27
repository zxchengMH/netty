package com.example.nettyserver.config.msg;

/**
 * @Description 消息发送者
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/24 11:37
 * @Version 1.0
 */
public class MessageSendHandler {

    /**
     * 发送消息
     * @param receiver 指定接收用户
     * @param msg 消息内容
     */
    public static void sendMessage(String receiver, String msg){
        /*1.获取接收者的连接通道所在的服务(优选本地，再在缓存中查找)*/

        /*2.若接收者未连接，则将消息放置到消息队列中，等到用户上线消费*/

        /*3.调用对应服务的消息发送接口*/
    }


}
