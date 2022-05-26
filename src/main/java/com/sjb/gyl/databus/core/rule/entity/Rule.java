package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Rule {
    private String id;
    //模式 binlog，sql
    private String mode;
    private SrcCfg srcCfg;
    private List<DstCfg> dstCfg;
}
