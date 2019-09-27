package com.example.nettyserver.rs;

import com.example.nettyserver.config.annotation.NettyRequestMapping;
import com.example.nettyserver.config.handler.mapper.IServletHandler;
import com.example.nettyserver.config.http.NettyHttpRequest;
import com.example.nettyserver.config.http.RequestMethod;
import org.springframework.stereotype.Controller;

@Controller
@NettyRequestMapping(path = "/request/body",method = RequestMethod.POST)
public class RequestBodyHandler implements IServletHandler {

    @Override
    public String service(NettyHttpRequest request) {

        return request.getContent();
    }
}
