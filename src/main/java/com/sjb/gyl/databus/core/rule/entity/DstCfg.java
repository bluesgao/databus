package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class DstCfg {
    private String dsId;
    private String dsType;
    private String table;
    private Map<String, Field> fields;
    private List<String> uniqueFields;

    private String protocol;
    private String url;
    private List<String> params;
    private Map<String,String> headers;
}
