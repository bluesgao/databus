package com.sjb.gyl.databus.core.datasource.cfg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author bei.wu
 */
@Data
@ToString
@NoArgsConstructor
public class DataSourceCfg<T extends DsInfo> {
    /**
     * 数据源ID（在规则配置文件中会引用到该ID）
     */
    private String id;
    /**
     * 数据源类型（mysql/es/redis）
     */
    private String type;
    /**
     * 数据源连接信息
     */
    private T cfg;

    public DataSourceCfg(String id, String type) {
        this.id = id;
        this.type = type;
    }
}
