package com.example.nettyserver.config.annotation;


import com.example.nettyserver.config.http.RequestMethod;
import java.lang.annotation.*;

/**
 * @ClassName NettyRequestMapping
 * @Description TODO
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/25 16:21
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NettyRequestMapping {

    String path() default "";

    RequestMethod method() default RequestMethod.GET;
}
