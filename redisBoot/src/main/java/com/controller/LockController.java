package com.controller;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author ming.li
 * @Date 2025/8/4 10:02
 * @Version 1.0
 */
@RestController
@RequestMapping("lock")
public class LockController {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate redisTemplate;

    private static String name="redisson";
    @RequestMapping("set")
    public String set(@RequestBody Map map) {
        redisTemplate.opsForValue().set(map.get("key"),(Integer)map.get("value"));
        return "成功";
    }
    @RequestMapping("getReduce")
    public String getReduce(String key) {
        Integer o = (Integer)redisTemplate.opsForValue().get(key);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (o>0){
            redisTemplate.opsForValue().increment(key,-1);
        }
        o = (Integer)redisTemplate.opsForValue().get(key);
        return o.toString();
    }
    @RequestMapping("getLockReduce")
    public String getLockReduce(String key) {
        RLock lock = redissonClient.getLock(key + name);
        lock.lock();
        Integer o = (Integer)redisTemplate.opsForValue().get(key);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (o>0){
            redisTemplate.opsForValue().decrement(key);
        }
        o = (Integer)redisTemplate.opsForValue().get(key);
        lock.unlock();
        return o.toString();
    }

    @RequestMapping("getReadWriteLock")
    public String getReadWriteLock(String key) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
        //获取锁，不是加锁(这一步后再软件上还看不到锁，因为还没尝试向redis加锁)
        RLock rLock = readWriteLock.writeLock();
        rLock.lock();
        System.out.println(Thread.currentThread().getName()+":获取到锁");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+":释放锁");
        rLock.unlink();
        return "成功";
    }

}
