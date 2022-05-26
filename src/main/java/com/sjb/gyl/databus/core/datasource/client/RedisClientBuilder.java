package com.sjb.gyl.databus.core.datasource.client;

import com.sjb.gyl.databus.core.datasource.cfg.DataSourceCfg;
import com.sjb.gyl.databus.core.datasource.cfg.Redis;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Slf4j
public class RedisClientBuilder implements ClientBuilder {
    @Override
    public Object build(DataSourceCfg<?> ds) {
        Redis redis = (Redis) ds.getCfg();
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxTotal(10);
        return new JedisPool(poolConfig, redis.getHost(),
                Integer.parseInt(redis.getHost()));
    }
}
