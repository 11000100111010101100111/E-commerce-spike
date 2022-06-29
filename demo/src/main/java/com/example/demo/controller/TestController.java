package com.example.demo.controller;

import com.example.demo.annotation.PreventDuplicatesSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 *
 * @author xjh
 * @since 2022/6/29
 * Time: 12:07
 **/
@RestController("/activate")
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${redisConfig.hotKey.keyName}")
    private String keyName;

    @PostMapping("/text")
    //利用防重复提交保证用户的正常访问，
    //后端通过切面拦截请求进行处理，利用redis缓存用户ID和ip以及当前请求作为Key存储，设置过期时间为3秒
    //同时前端也可以对按钮或连接进行致灰或加载等待，
    @PreventDuplicatesSubmit(value = 3)
    public String limitFlow2(){
        //利用Redis的令牌桶算法完成正常访问用户进行限流的操作。
        //令牌桶算法提及到输入速率和输出速率[输出速率>输入速率=超出流量限制]
        //每访问一次请求的时候，可以从Redis中获取一个令牌，如果拿到令牌了，那就说明没超出限制，而如果拿不到，则结果相反。
        //在redis中利用List的leftPop来获取令牌
        Object result = redisTemplate.opsForList().leftPop(this.keyName);
        if(result == null){
            return "请求繁忙";
        }

        //......秒杀或者优惠活动业务逻辑代码

        return "成功参与秒杀或者优惠活动";
    }
}
