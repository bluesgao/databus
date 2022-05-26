package com.sjb.gyl.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author bei.wu
 */
@Data
@ToString
public class Table {
    @NotBlank(message = "database不能为空")
    @Length(max = 32)
    private String database;
    @NotBlank(message = "name不能为空")
    private String name;
    @NotEmpty
    private List<@NotBlank(message = "类型标识不能为空") @Pattern(regexp = "^[insert|update|delete]+$") String> events;
    private List<String> fields;
    private List<String> uniqueFields;
    @NotBlank
    private String ruleId;
}
