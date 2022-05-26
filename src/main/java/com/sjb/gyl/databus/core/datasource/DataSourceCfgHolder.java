package com.sjb.gyl.databus.core.datasource;

import com.sjb.gyl.databus.core.datasource.cfg.DataSourceCfg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bei.wu
 */
@Slf4j
@Component
public class DataSourceCfgHolder {

    private static final Map<String, DataSourceCfg<?>> DATASOURCE_CFG_HOLDER = new ConcurrentHashMap<>(16);

    public static DataSourceCfg<?> get(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id should not be empty.");
        }
        return DATASOURCE_CFG_HOLDER.get(id);
    }

    public static void put(DataSourceCfg<?> dataSourceCfg) {
        DATASOURCE_CFG_HOLDER.putIfAbsent(dataSourceCfg.getId(), dataSourceCfg);
    }

    public static Map<String, DataSourceCfg<?>> getAll() {
        return DATASOURCE_CFG_HOLDER;
    }


}
