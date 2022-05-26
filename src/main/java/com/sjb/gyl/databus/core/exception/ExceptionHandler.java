package com.sjb.gyl.databus.core.exception;

import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.retry.RetryMsgProducer;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ExceptionHandler {
    @Resource
    private RetryMsgProducer retryMsgProducer;

    public void handleException(ExceptionPolicy exceptionPolicy, MsgWrapper msgWrapper, Table table, Rule rule, Exception e) {
        log.info("处理异常:msg[{}],table[{}],rules[{}],e[{}]", msgWrapper, table, rule, e);
        if (exceptionPolicy.equals(ExceptionPolicy.LOG)) {
            log.error("处理异常策略[LOG]:msg[{}],table[{}],rules[{}],e[{}]", msgWrapper, table, rule, e);
        } else if (exceptionPolicy.equals(ExceptionPolicy.DISCARD)) {
            log.warn("处理异常策略[DISCARD]:msg[{}],table[{}],rules[{}],e[{}]", msgWrapper, table, rule, e);
        } else if (exceptionPolicy.equals(ExceptionPolicy.RETRY)) {
            log.info("处理异常策略[RETRY]:msg[{}],table[{}],rules[{}],e[{}]", msgWrapper, table, rule, e);
            //异常处理
            MsgWrapper wrapper = MsgWrapper.warp(msgWrapper.getBinlog(),
                    msgWrapper.getRetry().getRetryCount() + 1,
                    "handler处理异常",
                    System.currentTimeMillis());
            try {
                retryMsgProducer.send(wrapper);
            } catch (Exception ex) {
                //记录日志，防止发送失败
                log.error("retryProducer.send 失败,消息:{}", JSON.toJSONString(msgWrapper), ex);
            }
        } else {
            log.error("处理异常策略配置错误:msg[{}],table[{}],rules[{}],e[{}]", msgWrapper, table, rule, e);
        }

    }
}
