package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author bei.wu
 */
@Data
@ToString
public class RuleCfg {
    @NotBlank(message = "RuleId不能为空")
    @Length(max = 32)
    private String id;
    @NotBlank(message = "desc备注信息不能为空")
    @Length(max = 255)
    private String desc;
    //@NotBlank
    private String exception;
    @Valid
    @NotEmpty(message = "table规则配置不能为空")
    private List<Table> table;
    @NotEmpty(message = "rule规则配置不能为空")
    private List<Rule> rule;
}
