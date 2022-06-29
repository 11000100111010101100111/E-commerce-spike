package com.example.demo.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 *
 * @author xjh
 * @since 2022/6/29
 * Time: 12:14
 **/
@Component
public class RedisTask {
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${redisConfig.hotKey.keyName}")
    private String keyName;

    //依靠定时任务，定时在redis中往List中rightPush唯一性令牌
    // 1S的速率往令牌桶中添加UUID，只为保证唯一性
    @Scheduled(fixedDelay = 1000,initialDelay = 0)
    public void setIntervalTimeTask(){
        redisTemplate.opsForList().rightPush(this.keyName, UUID.randomUUID().toString());
    }
}
