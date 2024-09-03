package com.dao;

import com.bean.Employees;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ming.li
 * @date 2023/3/10 14:05
 */
@Mapper
public interface EmployeesDao {
    int create(Employees payment);

    Employees getByName(String name);
}
