package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FieldMapping {
    private String name;
    private String type;
    private String format;
}
