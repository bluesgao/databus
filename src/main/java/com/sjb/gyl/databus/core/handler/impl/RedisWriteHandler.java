package com.sjb.gyl.databus.core.handler.impl;

import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.common.DsType;
import com.sjb.gyl.databus.core.datasource.operate.OperatorHolder;
import com.sjb.gyl.databus.core.exception.ExceptionPolicy;
import com.sjb.gyl.databus.core.handler.AbstractHandler;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.DstCfg;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisWriteHandler extends AbstractHandler {
    @Override
    public boolean handleBusiness(MsgWrapper msgWrapper, Table table, Rule rule) {
        log.info("redis处理器：msgWrapper:{},table:{},rule:{}", JSON.toJSONString(msgWrapper), JSON.toJSONString(table), JSON.toJSONString(rule));
        boolean flag = true;
        for (DstCfg dstCfg : rule.getDstCfg()) {
            if (dstCfg.getDsType().equalsIgnoreCase(DsType.REDIS.getType())) {
                try {
                    OperatorHolder.getOperator(dstCfg.getDsType()).operate(msgWrapper, dstCfg);
                } catch (Exception e) {
                    log.error("redis处理器执行异常：msgWrapper:{},table:{},rule:{}，e:{}", JSON.toJSONString(msgWrapper), JSON.toJSONString(table), JSON.toJSONString(rule), e);
                    flag = false;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean canSkip() {
        return true;
    }

    @Override
    public ExceptionPolicy exceptionPolicy() {
        return ExceptionPolicy.RETRY;
    }
}
