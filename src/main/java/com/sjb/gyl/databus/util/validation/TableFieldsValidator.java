package com.sjb.gyl.databus.util.validation;


import com.sjb.gyl.databus.core.rule.entity.Table;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author bei.wu
 */
public class TableFieldsValidator implements ConstraintValidator<TableFields, Table> {

    @Override
    public boolean isValid(Table table, ConstraintValidatorContext constraintValidatorContext) {
        // 验证database
        if (table.getEvents().contains("update") && table.getFields().size()==0) {
            return false;
        }
        return true;
    }
}
