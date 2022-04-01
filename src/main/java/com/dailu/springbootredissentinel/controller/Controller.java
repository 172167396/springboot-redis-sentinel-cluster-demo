package com.dailu.springbootredissentinel.controller;

import com.dailu.springbootredissentinel.entity.UserEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
public class Controller {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

//    @Resource
//    ValueOperations<String, String> valueOperations;

    @GetMapping("/setUser")
    public String setUser() {
        String id = UUID.randomUUID().toString().replace("-", "");
        UserEntity user = new UserEntity(id, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "池州青阳县");
        redisTemplate.opsForValue().set(id, user);
        return id;
    }

    @GetMapping("/getUser/{id}")
    public UserEntity getUser(@PathVariable String id) {
        Object o = redisTemplate.opsForValue().get(id);
        return (UserEntity) o;
    }

    @GetMapping("/delete/{id}")
    public Boolean delete(@PathVariable String id) {
        return redisTemplate.delete(id);
    }


    @GetMapping("/set")
    public String set() {
        String id = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(id, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return id;
    }

    @GetMapping("/set/{key}/{value}")
    public String setKeyValue(@PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value);
        return key;
    }

    @GetMapping("/get/{id}")
    public Object get(@PathVariable String id) {
        return redisTemplate.opsForValue().get(id);
    }

}
