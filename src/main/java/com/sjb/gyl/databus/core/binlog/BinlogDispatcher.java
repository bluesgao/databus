package com.sjb.gyl.databus.core.binlog;

import com.sjb.gyl.databus.core.handler.HandlerChain;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.RuleCfgHolder;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

@Slf4j
@Data
@Component
public class BinlogDispatcher {
    @Resource
    private Executor handlerExecutor;
    @Resource
    private HandlerChain handlerChain;

    public void dispatch(List<MsgWrapper> msgWrappers) {
        for (MsgWrapper msgWrapper : msgWrappers) {
            Binlog binlog = msgWrapper.getBinlog();
            Table table = RuleCfgHolder.getTable(binlog.getDatabase(), binlog.getTable());
            if (!Objects.isNull(table)) {
                handlerExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Rule rule = RuleCfgHolder.getTableRule(table);
                        if (rule != null) {
                            handlerChain.process(msgWrapper, table, rule);
                        }
                    }
                });
            }
        }
    }

}
