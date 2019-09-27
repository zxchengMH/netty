package com.example.nettyserver.rs;


import com.example.nettyserver.config.annotation.NettyRequestMapping;
import com.example.nettyserver.config.handler.mapper.IServletHandler;
import com.example.nettyserver.config.http.NettyHttpRequest;
import org.springframework.stereotype.Controller;

@Controller
@NettyRequestMapping(path = "/hello/world")
public class HelloWorldHandler implements IServletHandler {

    @Override
    public String service(NettyHttpRequest request) {
        return request.getParameter("name");
    }

}
