package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * @author xjh
 * @since 2022/6/29
 * Time: 12:14
 * @description 防重复提交注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventDuplicatesSubmit {
    /**
     * 配置value时间内禁止重复提交,单位秒
     */
    long value() default 5;
}