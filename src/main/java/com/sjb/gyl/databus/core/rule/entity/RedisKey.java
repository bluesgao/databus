package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class RedisKey {
    private String prefix;
    private String separator;
    //string,map
    private String type;
    private List<String> items;
}
