package com.sjb.gyl.databus.common;

/**
 * 数据源配置枚举
 * @author bei.wu
 */
public enum DataSourceEnum {

    /**
     * mysql
     */
    MYSQL,
    /**
     * redis
     */
    REDIS,
    /**
     * es
     */
    ES,
    /**
     * 未知类型
     */
    UNKNOWN;

    /**
     * DataSource配置文件中type类型
     * @param type 数据源类型
     * @return DataSourceEnum
     */
    public static DataSourceEnum getDataSourceEnum(String type){
        for (DataSourceEnum ds : DataSourceEnum.values()) {
            if (type.toUpperCase().equals(ds.name())) {
                return ds;
            }
        }
        return DataSourceEnum.UNKNOWN;
    }
}
