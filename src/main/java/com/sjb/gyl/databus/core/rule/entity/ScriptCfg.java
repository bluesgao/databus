package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ScriptCfg {
    private String dsId;
    private String dsType;
    private String script;
    private List<String> paramFields;
}
