package com.controller;

import com.bean.Employees;
import com.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author ming.li
 * @date 2023/4/20 16:09
 */
@RestController
@RequestMapping("/string")
public class StringController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("set")
    public String set(@RequestBody Map map) {
        redisTemplate.opsForValue().set(map.get("key"),map.get("value"));
        return "成功";
    }

    @RequestMapping("get")
    public String  get(String name) {
        Object o = redisTemplate.opsForValue().get(name);
        return o.toString();
    }
}
