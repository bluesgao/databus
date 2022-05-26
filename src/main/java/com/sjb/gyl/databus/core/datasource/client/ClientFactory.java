package com.sjb.gyl.databus.core.datasource.client;

import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.core.datasource.cfg.DataSourceCfg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 数据源client工厂
 */
@Slf4j
@Component
public class ClientFactory {
    /**
     * 创建ds
     *
     * @param ds
     */
    public static synchronized void create(DataSourceCfg<?> ds) {
        ClientBuilder clientBuilder = ClientBuilderHolder.getBuilder(ds.getType());
        if (clientBuilder == null) {
            return;
        }
        if (clientBuilder instanceof JdbcClientBuilder) {
            if (ClientHolder.getJdbcClient(ds.getId()) == null) {
                JdbcTemplate jdbcTemplate = (JdbcTemplate) clientBuilder.build(ds);
                if (jdbcTemplate != null) {
                    ClientHolder.putJdbcClient(ds.getId(), jdbcTemplate);
                }
            }
        } else if (clientBuilder instanceof RedisClientBuilder) {
            //todo
        } else if (clientBuilder instanceof EsClientBuilder) {
            //todo
        } else {
            log.error("未知类型的数据源配置:{}", JSON.toJSONString(ds));
        }
    }
}
