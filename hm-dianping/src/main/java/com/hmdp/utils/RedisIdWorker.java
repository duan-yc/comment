package com.hmdp.utils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;



@Component
public class RedisIdWorker {
    //定义初始时间戳
    private static final long BEGIN_TIMESTAMP = 1640995200L;
    private static final short COUNT_BITS = 32;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Long nextId(String keyPrefix){
        //生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        //生成序列号
        // 2.1.获取当前日期，精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 2.2.自增长
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        //拼接并返回
        // 因为如果直接拼接就是字符串，为了拼接成数字，所以首先需要时间戳左移32位，在以或运算拼接序列号
        return timestamp << COUNT_BITS | count;
    }
}
