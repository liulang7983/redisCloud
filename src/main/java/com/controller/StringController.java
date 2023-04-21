package com.controller;

import com.bean.Employees;
import com.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author ming.li
 * @date 2023/4/20 16:09
 */
@RestController
@RequestMapping("/string")
public class StringController {
    @Autowired
    private EmployeesService employeesService;

    @RequestMapping("set")
    public String set(@RequestBody Employees employees) {
        employees.setHireTime(new Date());
        employeesService.create(employees);
        //redisTemplate.opsForValue().set(map.get("key"),map.get("value"));
        return "成功";
    }

    @RequestMapping("get")
    public Employees get(String name) {
        Employees byName = employeesService.getByName(name);
        //redisTemplate.opsForValue().set(map.get("key"),map.get("value"));
        return byName;
    }

    //测试分布式红锁
    @RequestMapping("redisson")
    public Employees redisson(String name) {
        Employees employees = employeesService.getByNameRedisson(name);
        //redisTemplate.opsForValue().set(map.get("key"),map.get("value"));
        return employees;
    }

    //测试读写锁
    @RequestMapping("read")
    public Employees read(String name) {
        Employees employees = employeesService.read(name);
        return employees;
    }

    @RequestMapping("write")
    public Employees write(String name) {
        Employees employees = employeesService.write(name);
        return employees;
    }
}
