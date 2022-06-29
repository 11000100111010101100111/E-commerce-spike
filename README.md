# E-commerce-spike
运营推广部门某次策划上线秒杀或者优惠活动，经测试人员估算压力测试，大约在一个小时内进来100万+用户访问，系统吞吐量固定的情况下，为保障Java服务端正常运行不崩溃，需要对正常访问用户进行限流处理，大约每秒响应1000个请求

## 防重复提交
    利用防重复提交保证用户的正常访问，
    后端通过切面拦截请求进行处理，利用redis缓存用户ID和ip以及当前请求作为Key存储，设置过期时间为3秒
    同时前端也可以对按钮或连接进行致灰或加载等待，
    
## Redis令牌桶
    利用Redis的令牌桶算法完成正常访问用户进行限流的操作。
    令牌桶算法提及到输入速率和输出速率[输出速率>输入速率=超出流量限制]
    每访问一次请求的时候，可以从Redis中获取一个令牌，如果拿到令牌了，那就说明没超出限制，而如果拿不到，则结果相反。
    在redis中利用List的leftPop来获取令牌
    
## 定时任务
    依靠定时任务，定时在redis中往List中rightPush唯一性令牌
    1S的速率往令牌桶中添加UUID，只为保证唯一性
