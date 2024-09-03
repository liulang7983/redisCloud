package com.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ming.li
 * @date 2023/4/20 16:28
 */
public class Employees implements Serializable {
    private Long id;
    private String name;
    private Integer age;
    private String position;
    private Date hireTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getHireTime() {
        return hireTime;
    }

    public void setHireTime(Date hireTime) {
        this.hireTime = hireTime;
    }
}
