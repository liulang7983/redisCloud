package com.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @Author ming.li
 * @Date 2025/8/1 16:20
 * @Version 1.0
 */
@RestController
@RequestMapping("set")
public class SetController {
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("set")
    public String set(@RequestBody JSONObject jsonObject) {
        String key = jsonObject.getString("key");
        List list=jsonObject.getJSONArray("value");
        for (int i = 0; i <list.size() ; i++) {
            redisTemplate.opsForSet().add(key,list.get(i));
        }
        return "成功";
    }
    @RequestMapping("get")
    public Set get(String key) {
        Set members = redisTemplate.opsForSet().members(key);
        return members;
    }
    //差集
    @RequestMapping("getDiff")
    public Set getDiff(String key,String key1) {
        Set members = redisTemplate.opsForSet().difference(key,key1);
        return members;
    }
    //交集
    @RequestMapping("getInter")
    public Set getInter(String key,String key1) {
        Set members = redisTemplate.opsForSet().intersect(key,key1);
        return members;
    }
    //并集
    @RequestMapping("getUnion")
    public Set getUnion(String key,String key1) {
        Set members = redisTemplate.opsForSet().union(key,key1);
        return members;
    }
}
