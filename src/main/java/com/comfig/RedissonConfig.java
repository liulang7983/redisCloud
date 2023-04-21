package com.comfig;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ming.li
 * @date 2023/4/20 17:20
 */
@Configuration
public class RedissonConfig {
    /*@Value("${redisson.address}")
    private String address;

    @Value("${redisson.password}")
    private String password;*/
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient testRedisson(){
        List<String> nodes = redisProperties.getCluster().getNodes();
        Config config = new Config();
        List<String> clusterNodes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            clusterNodes.add("redis://" + nodes.get(i));
        }
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]));

        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            clusterServersConfig.setPassword(redisProperties.getPassword());
        }
        //看门狗的锁续期时间，默认30000ms，这里配置成15000ms
        config.setLockWatchdogTimeout(15000);
        RedissonClient redisson = Redisson.create(config);
        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("lm");
        //初始化布隆过滤器：预计元素为100000000L,误差率为3%,根据这两个参数会计算出底层的bit数组大小
        bloomFilter.tryInit(100000000L,0.03);
        /*System.out.println(address);

        //集群配置需要这样
        config.useSingleServer().setAddress(address).setAddress("redis://127.0.0.1:8001").setAddress("redis://127.0.0.1:8002").setPassword(password);
        //构造Redisson
        RedissonClient redisson = Redisson.create(config);*/
        /*RBloomFilter<String> bloomFilter = redisson.getBloomFilter("lms");
        //初始化布隆过滤器：预计元素为100000000L,误差率为3%,根据这两个参数会计算出底层的bit数组大小
        bloomFilter.tryInit(100000000L,0.03);*/
        return redisson;
    }
}
