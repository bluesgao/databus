package com.sjb.gyl.databus.core.datasource.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ClientBuilderHolder {
    /*客户端创建者*/
    private static Map<String, ClientBuilder> CLIENT_BUILDER_HOLDER = new HashMap<>(8);

    static {
        log.info("D客户端创建者 加载开始...");
        CLIENT_BUILDER_HOLDER.put("mysql", new JdbcClientBuilder());
        CLIENT_BUILDER_HOLDER.put("redis", new RedisClientBuilder());
        CLIENT_BUILDER_HOLDER.put("es", new EsClientBuilder());
        log.info("客户端创建者 加载成功...");
    }

    /**
     * 获取builder
     *
     * @param type
     * @return
     */
    public static ClientBuilder getBuilder(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type should not be empty.");
        }
        return CLIENT_BUILDER_HOLDER.get(type);
    }
}
