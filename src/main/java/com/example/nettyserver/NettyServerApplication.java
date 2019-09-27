package com.example.nettyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   //开启定时任务
public class NettyServerApplication {

    public static void main(String[] args) {

        //== SpringApplicationBuilder
        SpringApplication application = new SpringApplication(NettyServerApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);

    }

}
