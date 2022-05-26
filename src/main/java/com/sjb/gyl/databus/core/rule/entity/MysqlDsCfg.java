package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class MysqlDsCfg {
    private String table;
    private Map<String, Field> fields;
    private List<String> uniqueFields;
}
