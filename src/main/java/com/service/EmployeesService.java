package com.service;

import com.bean.Employees;

/**
 * @author ming.li
 * @date 2023/3/10 14:11
 */
public interface EmployeesService {
    int create(Employees payment);
    Employees getByName(String name);

    Employees getByNameRedisson(String name);

    Employees read(String name);

    Employees write(String name);

}
