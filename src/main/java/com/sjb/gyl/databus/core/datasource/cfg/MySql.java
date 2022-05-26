package com.sjb.gyl.databus.core.datasource.cfg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author bei.wu
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class MySql extends DsInfo {

    /**
     * 数据库地址
     */
    private String url;
    /**
     * mysql驱动
     */
    private String driverClassName;

}
