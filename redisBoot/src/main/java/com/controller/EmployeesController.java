package com.controller;

import com.bean.Employees;
import com.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author ming.li
 * @Date 2024/9/3 15:46
 * @Version 1.0
 */
@RestController
@RequestMapping("employees")
public class EmployeesController {
    @Autowired
    private EmployeesService employeesService;

    @RequestMapping("set")
    public String set(@RequestBody Employees employees) {
        employees.setHireTime(new Date());
        employeesService.create(employees);
        return "成功";
    }

    @RequestMapping("get")
    public Employees get(String name) {
        Employees byName = employeesService.getByName(name);
        return byName;
    }
    @RequestMapping("test")
    public void get() {
        employeesService.test();
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
