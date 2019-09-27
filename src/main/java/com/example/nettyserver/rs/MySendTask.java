package com.example.nettyserver.rs;

import com.example.nettyserver.config.channel.ChannelHandlerPool;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName ScheduleTask
 * @Description TODO
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/19 18:22
 * @Version 1.0
 */
@Component
public class MySendTask {

    //3.添加定时任务
    @Scheduled(cron = "0/1 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        String time = LocalDateTime.now().toString();
        //System.err.println("执行静态定时任务时间: " + time);
        ChannelHandlerPool.sendMessage("zxc", time);
    }
}
