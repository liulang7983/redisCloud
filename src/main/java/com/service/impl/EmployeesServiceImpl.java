package com.service.impl;

import com.bean.Employees;
import com.dao.EmployeesDao;
import com.service.EmployeesService;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author ming.li
 * @date 2023/3/10 14:11
 */
@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private EmployeesDao employeesDao;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Redisson redisson;
    @Override
    public int create(Employees employees) {

        int i = employeesDao.create(employees);
        if (1>0){
            //插入成功则写入到redis中，且设置超时时间
            redisTemplate.opsForValue().set("employees:"+employees.getName(),employees,new Random().nextInt(200),TimeUnit.SECONDS);
            redissonClient.getBloomFilter("lm").add("employees:"+employees.getName());

        }
        return i;


    }

    @Override
    public Employees getByName(String name) {
        Employees employees=null;
        if (redissonClient.getBloomFilter("lm").contains("employees:"+name)){
             employees = (Employees)redisTemplate.opsForValue().get("employees:"+name);
        }
        if (employees==null){
            employees=employeesDao.getByName(name);
            redisTemplate.opsForValue().set("employees:"+employees.getName(),employees,100,TimeUnit.SECONDS);
        }

        return employees;
    }

    @Override
    public Employees getByNameRedisson(String name) {
        String employeesLock="empLock"+name;
        RLock lock = redissonClient.getLock(employeesLock);
        System.out.println(employeesLock+",尝试加锁");
        long start = System.currentTimeMillis();
        lock.lock();
        System.out.println(employeesLock+",加锁成功");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Employees employees=null;
        if (redissonClient.getBloomFilter("lm").contains("employees:"+name)){
            employees = (Employees)redisTemplate.opsForValue().get("employees:"+name);
        }
        if (employees==null){
            employees=employeesDao.getByName(name);
            redisTemplate.opsForValue().set("employees:"+employees.getName(),employees,100,TimeUnit.SECONDS);
        }
        lock.unlock();
        long end = System.currentTimeMillis();
        System.out.println(employeesLock+"耗时："+(end-start));
        return employees;
    }

    @Override
    public Employees read(String name) {
        String employeesLock="redaAndwrite"+name;
        RReadWriteLock lock = redisson.getReadWriteLock(employeesLock);
        System.out.println("读锁开始加锁："+employeesLock);
        long start = System.currentTimeMillis();
        //此时才是真的获取锁而不是加锁
        RLock readLock = lock.readLock();
        //此时才真正的加锁
        readLock.lock();
        //集群无法获取这个锁信息就很奇怪
        //System.out.println(redisTemplate.opsForHash().entries(employeesLock));
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Employees employees=null;
        if (redissonClient.getBloomFilter("lm").contains("employees:"+name)){
            employees = (Employees)redisTemplate.opsForValue().get("employees:"+name);
        }
        if (employees==null){
            employees=employeesDao.getByName(name);
            redisTemplate.opsForValue().set("employees:"+employees.getName(),employees,100,TimeUnit.SECONDS);
        }
        readLock.unlock();
        long end = System.currentTimeMillis();
        System.out.println(employeesLock+"耗时："+(end-start));
        return employees;
    }

    @Override
    public Employees write(String name) {
        String employeesLock="redaAndwrite"+name;
        RReadWriteLock lock = redisson.getReadWriteLock(employeesLock);
        System.out.println("写锁开始加锁："+employeesLock);
        long start = System.currentTimeMillis();
        //此时才是真的获取锁而不是加锁
        RLock writeLock = lock.writeLock();
        //此时才真正的加锁
        writeLock.lock();
        //System.out.println(redisTemplate.opsForHash().entries(employeesLock));
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Employees employees=null;
        if (redissonClient.getBloomFilter("lm").contains("employees:"+name)){
            employees = (Employees)redisTemplate.opsForValue().get("employees:"+name);
        }
        if (employees==null){
            employees=employeesDao.getByName(name);
            redisTemplate.opsForValue().set("employees:"+employees.getName(),employees,100,TimeUnit.SECONDS);
        }
        writeLock.unlock();
        long end = System.currentTimeMillis();
        System.out.println(employeesLock+"耗时："+(end-start));
        return employees;
    }

}
