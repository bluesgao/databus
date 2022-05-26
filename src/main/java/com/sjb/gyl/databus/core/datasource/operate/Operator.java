package com.sjb.gyl.databus.core.datasource.operate;

import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.DstCfg;


public interface Operator {
    void operate(MsgWrapper msgWrapper, DstCfg dstCfg);
}
