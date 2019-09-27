package com.example.nettyserver.config.annotation;

import java.lang.annotation.*;

/**
 * @ClassName Content
 * @Description TODO
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/26 10:43
 * @Version 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Content {
    boolean required() default true;
}
