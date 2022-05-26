package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class CallbackCfg {
    private String protocol;
    private String url;
    private List<String> params;
    private Map<String,String> headers;
}
