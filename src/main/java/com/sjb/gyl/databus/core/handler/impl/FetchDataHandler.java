package com.sjb.gyl.databus.core.handler.impl;

import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.common.ModeType;
import com.sjb.gyl.databus.core.binlog.Binlog;
import com.sjb.gyl.databus.core.datasource.client.ClientHolder;
import com.sjb.gyl.databus.core.exception.ExceptionPolicy;
import com.sjb.gyl.databus.core.handler.AbstractHandler;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FetchDataHandler extends AbstractHandler {
    @Override
    public boolean handleBusiness(MsgWrapper msgWrapper, Table table, Rule rule) {
        log.info("获取数据处理器：msgWrapper:{},table:{},rule:{}", JSON.toJSONString(msgWrapper), JSON.toJSONString(table), JSON.toJSONString(rule));
        Binlog binlog = msgWrapper.getBinlog();
        boolean flag = true;
        //根据sql获取数据
        Map<String, String> dataMap = com.sjb.gyl.databus.util.CollectionUtils.toMap(binlog.getData());

        if (rule.getMode().equalsIgnoreCase(ModeType.SQL.getType())) {

            String newScript = populateSql(rule.getSrcCfg().getScript(), dataMap, rule.getSrcCfg().getParamFields());
            if (newScript != null) {
                Map<String, Object> resultMap = null;
                try {
                    resultMap = ClientHolder.getJdbcClient(rule.getSrcCfg().getDsId()).queryForMap(newScript);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    flag = false;
                }
                log.info("MysqlDataSourceHandler handleBusiness resultMap:{}", JSON.toJSONString(resultMap));
                if (!CollectionUtils.isEmpty(resultMap)) {
                    for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                        //temp.put(entry.getKey(), entry.getValue().toString());
                        msgWrapper.getOriginalData().put(entry.getKey(), entry.getValue() == null ? null : entry.getValue().toString());
                    }
                } else {
                    flag = false;
                }

            }
        } else {
            msgWrapper.setOriginalData(dataMap);
            log.warn("不支持的mode类型：{}", JSON.toJSONString(rule));
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean canSkip() {
        return false;
    }

    @Override
    public ExceptionPolicy exceptionPolicy() {
        return ExceptionPolicy.RETRY;
    }

    private String populateSql(String script, Map<String, String> data, List<String> paramsFields) {

        if (stringCount(script, "?") != paramsFields.size()) {
            return null;
        }

        for (String field : paramsFields) {
            String value = data.get(field);
            if (value == null) {
                return null;
            }
            script = script.replaceFirst("\\?", "'" + value + "'");
        }
        return script;
    }

    //1.统计一个字符串在另一个字符串中出现的次数
    public static int stringCount(String str, String key) {
        int index = 0;
        int count = 0;
        while ((index = str.indexOf(key)) != -1) {
            str = str.substring(index + key.length());
            count++;
        }

        return count;
    }
}
