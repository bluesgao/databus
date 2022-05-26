package com.sjb.gyl.databus.core.datasource.cfg;


import lombok.Data;

/**
 * 数据源配置父类
 * @author bei.wu
 */
@Data
public class DsInfo {

    /**
     * 数据源连接用户名
     */
    protected String username;
    /**
     * 数据源连接密码
     */
    protected String password;
    /**
     * 数据源实例
     */
    protected String database;
}
