package com.controller;

import com.alibaba.fastjson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author ming.li
 * @Date 2025/8/1 14:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/hash")
public class HashController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("set")
    public String set(@RequestBody JSONObject jsonObject) {
        String key = jsonObject.getString("key");
        Map map=jsonObject.getJSONObject("value");
        redisTemplate.opsForHash().putAll(key,map);
        return "成功";
    }

    @RequestMapping("get")
    public Map  get(String name) {
        Map entries = redisTemplate.opsForHash().entries(name);
        return entries;
    }
    @RequestMapping("getKey")
    public String  getKey(String hash,String key) {
        Object o = redisTemplate.opsForHash().get(hash, key);
        return o.toString();
    }
}
