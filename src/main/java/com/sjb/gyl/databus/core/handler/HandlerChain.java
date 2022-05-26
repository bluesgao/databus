package com.sjb.gyl.databus.core.handler;

import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.core.handler.impl.*;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author bei.wu
 */
@Slf4j
@Component
public class HandlerChain {
    @Resource
    private EventFilterHandler eventFilterHandler;
    @Resource
    private FieldFilterHandler fieldFilterHandler;
    @Resource
    private FetchDataHandler fetchDataHandler;
    @Resource
    private MysqlWriteHandler mysqlWriteHandler;
    @Resource
    private RedisWriteHandler redisWriteHandler;


    public void process(MsgWrapper msgWrapper, Table table, Rule rule) {
        log.info("处理器链开始处理：msgWrapper:{},table:{},rule:{}", JSON.toJSONString(msgWrapper), JSON.toJSONString(table), JSON.toJSONString(rule));
        eventFilterHandler.setNextHandler(fieldFilterHandler);
        fieldFilterHandler.setNextHandler(fetchDataHandler);
        fetchDataHandler.setNextHandler(mysqlWriteHandler);
        mysqlWriteHandler.setNextHandler(redisWriteHandler);

        eventFilterHandler.handle(msgWrapper, table, rule);

    }
}
