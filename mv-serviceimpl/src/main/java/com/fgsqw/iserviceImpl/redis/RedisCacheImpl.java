package com.fgsqw.iserviceImpl.redis;

import com.fgsqw.iservice.redis.IRedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheImpl implements IRedisCacheService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate2;//序列化会导致key，value存在redis里为byte[]

    @Override
    public void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setString(String key, String value, int seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void setString(String key, String value, Long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.HOURS);
    }

    @Override
    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setObject(String key, Object value) {
        redisTemplate2.opsForValue().set(key, value);
    }

    @Override
    public Object getObject(String key) {
        return redisTemplate2.opsForValue().get(key);
    }

}
