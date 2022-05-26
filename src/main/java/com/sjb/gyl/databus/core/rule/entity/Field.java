package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Field {
    private FieldMapping mapping;
    private String defaultValue;
}
