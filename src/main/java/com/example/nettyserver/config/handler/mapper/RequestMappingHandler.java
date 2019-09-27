package com.example.nettyserver.config.handler.mapper;

import com.example.nettyserver.config.annotation.NettyRequestMapping;
import com.example.nettyserver.config.exception.RequestNotFoundException;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RequestMappingHandler
 * @Description TODO
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/25 16:47
 * @Version 1.0
 */
@Configuration
public class RequestMappingHandler implements ApplicationContextAware {

    private static Map<MappingModel, IServletHandler> handlerMap = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
        init();
    }

    private void init(){
        Map<String, Object> map =
                this.applicationContext.getBeansWithAnnotation(NettyRequestMapping.class);

        for (Map.Entry<String, Object> entry : map.entrySet()){
            Object handler = entry.getValue();
            if(handler instanceof IServletHandler){
                NettyRequestMapping mapping = handler.getClass().getAnnotation(NettyRequestMapping.class);
                MappingModel mappingModel = new MappingModel(mapping.path(), mapping.method().toString());
                if(handlerMap.containsKey(mappingModel)){
                    throw new BeanInstantiationException(handler.getClass(), "exit");
                }
                IServletHandler servletHandler = (IServletHandler) handler;
                servletHandler.init();
                handlerMap.put(mappingModel, (IServletHandler)handler);
            }
        }
    }

    public static IServletHandler matchFunctionHandler(FullHttpRequest request) throws Exception{
        String uri = request.uri();
        int f = uri.indexOf("?");
        if(f > 0){
            uri = uri.substring(0, f);
        }
        String method = request.method().toString();
        MappingModel mappingModel = new MappingModel(uri, method);
        IServletHandler handler = handlerMap.get(mappingModel);
        if(handler == null){
            throw new RequestNotFoundException("request: " + method + "["+uri+"] not fount");
        }
        return handler;
    }
}
