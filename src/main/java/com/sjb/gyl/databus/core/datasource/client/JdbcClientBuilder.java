package com.sjb.gyl.databus.core.datasource.client;

import com.sjb.gyl.databus.core.datasource.cfg.DataSourceCfg;
import com.sjb.gyl.databus.core.datasource.cfg.MySql;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Slf4j
public class JdbcClientBuilder implements ClientBuilder {

    @Override
    public Object build(DataSourceCfg<?> ds) {
        MySql mySql = (MySql) ds.getCfg();
        DataSource dataSource = null;
        try {
            dataSource = DataSourceBuilder.create(JdbcClientBuilder.class.getClassLoader())
                    .type(HikariDataSource.class)
                    .driverClassName(mySql.getDriverClassName())
                    .url(mySql.getUrl())
                    .username(mySql.getUsername())
                    .password(mySql.getPassword())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Map<String, Object> cfgMap = new HashMap<>(16);
//        //基本属性 url、user、password
//        cfgMap.put("url", url);
//        cfgMap.put("driverClassName", driverClassName);
//        cfgMap.put("username", username);
//        cfgMap.put("password", password);
//        //配置初始化大小、最小、最大
//        cfgMap.put("initialSize", 5);
//        cfgMap.put("minIdle", 5);
//        cfgMap.put("maxActive", 10);
//        //配置获取连接等待超时的时间
//        cfgMap.put("maxWait", 3000);
//        //配置一个连接在池中最小生存的时间，单位是毫秒
//        cfgMap.put("minEvictableIdleTimeMillis", 300000);
//
//
//        try {
//            dataSource = DruidDataSourceFactory.createDataSource(cfgMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (dataSource != null) {
            return new JdbcTemplate(dataSource);
        }
        return null;
    }
}
