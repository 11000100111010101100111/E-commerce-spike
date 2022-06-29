package com.example.demo.utils;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * @author xjh
 * @since 2022/6/29
 * Time: 12:14
 * Spring 工具类
 */

@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext = null;

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }
    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        applicationContext = null;
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     */
    @Override
    public void destroy() {
        SpringContextHolder.clearHolder();
    }

    /**
     * 获取HttpServletRequest
     *
     * @return javax.servlet.http.HttpServletRequest
     * @since 2021/1/23
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes)
                .getRequest();
        return null == request ? null : request;
    }

}
