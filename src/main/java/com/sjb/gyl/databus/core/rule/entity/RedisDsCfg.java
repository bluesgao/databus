package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class RedisDsCfg {
    private RedisKey key;
    private List<String> values;
}
