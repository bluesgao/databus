package com.sjb.gyl.databus.core.datasource.client;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源client
 */
@Slf4j
@Component
public class ClientHolder {
    /*jdbc客户端*/
    private static Map<String, JdbcTemplate> JDBC_CLIENT_HOLDER = new ConcurrentHashMap<>(8);

    /*reids客户端*/
    private static Map<String, JedisPool> REDIS_CLIENT_HOLDER = new ConcurrentHashMap<>(8);

    /*es客户端*/
    private static Map<String, RestHighLevelClient> ES_CLIENT_HOLDER = new ConcurrentHashMap<>(8);

    /**
     * 获取client
     */
    public static JdbcTemplate getJdbcClient(String dsId) {
        return JDBC_CLIENT_HOLDER.get(dsId);
    }

    public static Jedis getRedisClient(String dsId) {
        return REDIS_CLIENT_HOLDER.get(dsId).getResource();
    }

    public static void putJdbcClient(String dsId, JdbcTemplate jdbcTemplate) {
        if (!JDBC_CLIENT_HOLDER.containsKey(dsId)) {
            if (jdbcTemplate != null) {
                ClientHolder.JDBC_CLIENT_HOLDER.putIfAbsent(dsId, jdbcTemplate);
            }
        }
    }

    public static void putRedisClient(String dsId, JedisPool jedisPool) {
        if (REDIS_CLIENT_HOLDER.containsKey(dsId)) {
            if (jedisPool != null) {
                ClientHolder.REDIS_CLIENT_HOLDER.putIfAbsent(dsId, jedisPool);
            }
        }
    }
}
