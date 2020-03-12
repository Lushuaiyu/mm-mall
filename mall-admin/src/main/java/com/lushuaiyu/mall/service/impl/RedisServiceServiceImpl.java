package com.lushuaiyu.mall.service.impl;

import com.lushuaiyu.mall.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by jones on 7/3/2020
 *
 * @author lushuaiyu
 */
@Service
public class RedisServiceServiceImpl implements RedisService {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, String token, long time) {
        redisTemplate.opsForValue().set(key, token, time, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
