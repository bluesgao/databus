package com.sjb.gyl.databus.core.handler;

import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;

public interface Handler {
    void handle(MsgWrapper msgWrapper, Table table, Rule rule);

    void exception(MsgWrapper msgWrapper, Table table, Rule rule, Exception e);
}
