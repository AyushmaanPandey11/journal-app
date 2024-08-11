package com.Backend.Journal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    public RedisTemplate redisTemplate;

    public <T> T get( String Key, Class<T> entityClass){
        try{
            Object obj = redisTemplate.opsForValue().get(Key);
            ObjectMapper mapper = new ObjectMapper();
            assert obj != null;
            return mapper.readValue(obj.toString(),entityClass);
        }catch (Exception e){
            log.error("Exception : ",e);
            return null;
        }
    }

    public void set(String key, Object obj, Long ttl){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(obj);
            redisTemplate.opsForValue().set(key,jsonValue,ttl, TimeUnit.HOURS);
        }catch (Exception e){
            log.error("Exception : ",e);
        }
    }

    public void delete(String key){
        try{
            redisTemplate.delete(key);
        }catch (Exception e){
            log.error("Exception : ",e);
        }
    }
}
