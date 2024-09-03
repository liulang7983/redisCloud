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

}
