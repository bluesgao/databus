package com.sjb.gyl.databus.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sjb.gyl.databus.common.Constants;
import com.sjb.gyl.databus.common.DataSourceEnum;
import com.sjb.gyl.databus.core.datasource.DataSourceCfgHolder;
import com.sjb.gyl.databus.core.datasource.cfg.*;
import com.sjb.gyl.databus.core.datasource.client.ClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * 加载数据源配置文件
 * 在加载rule规则配置文件之前执行
 * @author bei.wu
 */
@Slf4j
@Configuration
@AutoConfigureBefore(RuleSettingConfig.class)
public class DataSourceSettingConfig {

    @Value("${gyl.databus.setting.datasource}")
    protected String datasourcePath;

    @PostConstruct
    public void init() {
        log.info("*** 开始-加载数据库配置文件 ***");
        List<String> datasourceList = FileUtil.listFileNames(datasourcePath);
        if (CollectionUtils.isEmpty(datasourceList)) {
            log.warn("{}, 未发现配置文件", datasourcePath);
        }

        datasourceList.forEach(dataSource -> {
            if (dataSource.endsWith(Constants.SETTING_SUFFIX)) {
                File file = FileUtil.file(this.datasourcePath, dataSource);
                log.info("配置文件路径：{}", file.toString());
                String content = FileUtil.readUtf8String(file);
                if (StrUtil.isNotBlank(content)) {
                    holderAndCreateClient(content);
                }
            }
        });
    }

    private void holderAndCreateClient(String content) {
        JSONArray array = JSON.parseArray(content);
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String id = object.getString("id");
            String type = object.getString("type");
            JSONObject cfgObj = object.getJSONObject("cfg");
            DataSourceCfg<DsInfo> cfg = new DataSourceCfg<>(id, type);
            DataSourceEnum dsEnum = DataSourceEnum.getDataSourceEnum(type);
            switch (Objects.requireNonNull(dsEnum)) {
                case MYSQL: cfg.setCfg(cfgObj.toJavaObject(MySql.class)); break;
                case REDIS: cfg.setCfg(cfgObj.toJavaObject(Redis.class)); break;
                case ES: cfg.setCfg(cfgObj.toJavaObject(Es.class)); break;
                default: break;
            }

            log.info("{}", cfg.toString());
            DataSourceCfgHolder.put(cfg);
            //创建数据源
            try {
                ClientFactory.create(cfg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
