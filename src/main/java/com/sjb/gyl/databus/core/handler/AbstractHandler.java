package com.sjb.gyl.databus.core.handler;

import com.sjb.gyl.databus.core.exception.ExceptionHandler;
import com.sjb.gyl.databus.core.exception.ExceptionPolicy;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public abstract class AbstractHandler implements Handler {
    private Handler nextHandler;

    @Resource
    private ExceptionHandler exceptionHandler;

    @Override
    public void handle(MsgWrapper msgWrapper, Table table, Rule rule) {
        boolean ret = false;
        try {
            ret = handleBusiness(msgWrapper, table, rule);
        } catch (Exception e) {
            exception(msgWrapper, table, rule, e);
        }
        //如果不能跳过，则需要要判断返回值，如果返回值为true，执行下一步，否则中断
        if (ret) {
            nextHandler.handle(msgWrapper, table, rule);
        } else {
            exception(msgWrapper, table, rule, null);
            if (canSkip()) {
                nextHandler.handle(msgWrapper, table, rule);
            }
        }
    }

    @Override
    public void exception(MsgWrapper msgWrapper, Table table, Rule rule, Exception e) {
        exceptionHandler.handleException(exceptionPolicy(), msgWrapper, table, rule, e);
    }

    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }

    /**
     * 业务处理
     *
     * @param binlog
     * @param table
     * @param rule
     * @return
     */
    public abstract boolean handleBusiness(MsgWrapper binlog, Table table, Rule rule);

    /**
     * 是否能跳过该阶段
     *
     * @return
     */
    public abstract boolean canSkip();

    /**
     * 异常处理策略
     *
     * @return
     */
    public abstract ExceptionPolicy exceptionPolicy();
}
