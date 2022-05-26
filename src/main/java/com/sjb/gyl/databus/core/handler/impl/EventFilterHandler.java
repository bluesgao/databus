package com.sjb.gyl.databus.core.handler.impl;

import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.core.binlog.Binlog;
import com.sjb.gyl.databus.core.exception.ExceptionPolicy;
import com.sjb.gyl.databus.core.handler.AbstractHandler;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EventFilterHandler extends AbstractHandler {
    @Override
    public boolean handleBusiness(MsgWrapper msgWrapper, Table table, Rule rule) {
        log.info("事件过滤处理器：msgWrapper:{},table:{},rule:{}", JSON.toJSONString(msgWrapper), JSON.toJSONString(table), JSON.toJSONString(rule));
        Binlog binlog = msgWrapper.getBinlog();
        boolean flag = false;
        if (table.getEvents().contains(binlog.getType().toLowerCase())) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean canSkip() {
        return false;
    }

    @Override
    public ExceptionPolicy exceptionPolicy() {
        return ExceptionPolicy.DISCARD;
    }

    private List<String> getAllFieldName(List<Map<String, String>> data) {
        List<String> fields = new ArrayList<>();
        for (Map<String, String> field : data) {
            for (String key : field.keySet()) {
                fields.add(key);
            }
        }
        return fields;
    }
}
