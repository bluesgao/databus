package com.sjb.gyl.databus.core.datasource.cfg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author bei.wu
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Redis extends DsInfo {

    /**
     * redis连接地址
     */
    private String host;
    /**
     * redis连接端口号
     */
    private String port;

}
