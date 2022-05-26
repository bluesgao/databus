package com.sjb.gyl.databus.core.datasource.operate;

import cn.hutool.core.collection.CollectionUtil;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.DstCfg;
import com.sjb.gyl.databus.core.rule.entity.Field;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public abstract class AbstractOperator implements Operator {
    @Override
    public void operate(MsgWrapper msgWrapper, DstCfg dstCfg) {
        if (!CollectionUtil.isEmpty(dstCfg.getFields())) {
            Map<String, Object> mapped = doMapping(dstCfg.getFields(), msgWrapper.getBinlog().getMysqlType(), msgWrapper.getOriginalData());
            if (CollectionUtil.isNotEmpty(mapped)) {
                //将mapping后的字段放到data中
                for (Map.Entry<String, Object> item : mapped.entrySet()) {
                    msgWrapper.getOriginalData().put(item.getKey(), item.getValue() == null ? null : item.getValue().toString());
                }

                //同时将老字段删除
                for (String oldFieldName : dstCfg.getFields().keySet()) {
                    msgWrapper.getOriginalData().remove(oldFieldName);
                }
            }
        }
        doUpsert(msgWrapper.getBinlog().getType(), msgWrapper.getOriginalData(), dstCfg);
    }

    public abstract Map<String, Object> doMapping(Map<String, Field> fieldMap, Map<String, String> mysqlTypeMap, Map<String, String> dataMap);

    public abstract boolean doUpsert(String eventType, Map<String, String> data, DstCfg dstCfg);

}
