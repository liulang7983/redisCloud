package com.comfig;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Set;

/**
 * @Author ming.li
 * @Date 2024/9/3 14:42
 * @Version 1.0
 */
@Component
public class BloomFilterUtil implements InitializingBean {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 10000000, 0.01);

    public void add(String key) {
        bloomFilter.put(key);
    }

    public boolean mightContain(String key) {
        return bloomFilter.mightContain(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<String> keys = redisTemplate.keys("*");
        for(String key:keys){
            bloomFilter.put(key);
        }
    }
}
