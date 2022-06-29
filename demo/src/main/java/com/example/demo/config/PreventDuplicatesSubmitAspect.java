package com.example.demo.config;

import com.example.demo.annotation.PreventDuplicatesSubmit;
import com.example.demo.utils.RedisService;
import com.example.demo.utils.SpringContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * @author xjh
 * @since 2022/6/29
 * Time: 12:14
 * @description 防重复提交切面
 */

@Aspect
@Component
public class PreventDuplicatesSubmitAspect {
    @Autowired
    RedisService redisUtil;

    @Pointcut("@annotation(com.petecc.ptms.core.annotation.PreventDuplicatesSubmit)")
    public void annotationPointCut() {}

    @Around("annotationPointCut()")
    public Object aroundRequest(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = SpringContextHolder.getRequest();

        //获取注解中配置的时间参数,设置多长
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        PreventDuplicatesSubmit action = method.getAnnotation(PreventDuplicatesSubmit.class);
        long time = action.value();

        //请求用户,伪代码
        Long userId = getUserId();


        String userNumber = loginUser != null ? loginUser.getId().toString() : "noUser";
        //uri
        String uri = getPath(request.getRequestURI());
        //客户端ip
        String remortIP = this.getRemortIP(request);

        //http method
        String httpMethod = request.getMethod();
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append("preventDuplicatesSubmit")
                .append(userId)
                .append(httpMethod)
                .append(uri)
                .append(remortIP);
        //得到key
        String key=keyBuilder.toString();
        //如果redis中有该key则拦截
        if (redisUtil.hasKey(key)){
            throw new RuntimeException("不要频繁点击哦，已经忙不过来啦╥﹏╥...!");
        }
        //没有以这些字段为key，存入redis，设定过期时间为time,单位秒
        redisUtil.set(key, true, time);
        Object result = point.proceed();
        return result;
    }


    /**
     * 获取URI
     * @param uriStr
     * @return
     */
    public static String getPath(String uriStr) {
        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
        }
        return uri.getPath();
    }
    /**
     * 获取客户端ip
     * @param request
     * @return
     */
    public static String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}
