package com.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author ming.li
 * @Date 2025/8/1 15:36
 * @Version 1.0
 */
@RestController
@RequestMapping("list")
public class ListController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("set")
    public String set(@RequestBody JSONObject jsonObject) {
        String key = jsonObject.getString("key");
        List list=jsonObject.getJSONArray("value");
        redisTemplate.opsForList().leftPushAll(key,list);
        return "成功";
    }
    @RequestMapping("get")
    public String get(String key) {
        Object o = redisTemplate.opsForList().leftPop(key);
        return o.toString();
    }
}
