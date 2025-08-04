package com.controller;

import com.alibaba.fastjson.JSONArray;
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
 * @Date 2025/8/1 17:11
 * @Version 1.0
 */
@RestController
@RequestMapping("zSet")
public class ZSetController {
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("set")
    public String set(@RequestBody JSONObject jsonObject) {
        String key = jsonObject.getString("key");
        JSONArray value = jsonObject.getJSONArray("value");
        for (int i = 0; i <value.size() ; i++) {
            JSONObject jsonObject1 = value.getJSONObject(i);
            redisTemplate.opsForZSet().add(key,jsonObject1.getString("value"),jsonObject1.getDouble("score"));
        }
        return "成功";
    }
    @RequestMapping("getAll")
    public Set getAll(String key) {
        Set range = redisTemplate.opsForZSet().range(key, 0, -1);
        return range;
    }
    @RequestMapping("getRange")
    public Set getRange(String key) {
        Set range = redisTemplate.opsForZSet().rangeByScore(key,1,1.2);
        return range;
    }
    @RequestMapping("getReverseRange")
    public Set getReverseRange(String key) {
        Set range = redisTemplate.opsForZSet().reverseRangeByScore(key,1,1.2);
        return range;
    }
    @RequestMapping("getRangeWithScores")
    public Set getRangeWithScores(String key) {
        Set set = redisTemplate.opsForZSet().rangeByScoreWithScores(key, 1, 1.2);
        return set;
    }
    @RequestMapping("getReverseRangeWithScores")
    public Set getReverseRangeWithScores(String key) {
        Set set = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 1, 1.2);
        return set;
    }
}
