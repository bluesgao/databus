package com.sjb.gyl.databus.util.validation;

import com.sjb.gyl.databus.core.datasource.cfg.DataSourceCfg;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author bei.wu
 */
public class DataSourceValidator implements ConstraintValidator<DataSource, DataSourceCfg<?>> {

    @Override
    public boolean isValid(DataSourceCfg dataSource, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
