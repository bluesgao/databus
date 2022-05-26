package com.sjb.gyl.databus.util;

import com.google.common.collect.Lists;
import com.sjb.gyl.databus.core.rule.entity.RuleCfg;
import com.sjb.gyl.databus.util.validation.ValidError;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bei.wu
 */
@Slf4j
public class ValidationUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void isValid(RuleCfg ruleCfg){
        Set<ConstraintViolation<RuleCfg>> constraintValidators = validator.validate(ruleCfg);
        if (!constraintValidators.isEmpty()) {
            Map<String, List<ValidError>> errors = new HashMap<>(8);
            for (ConstraintViolation<RuleCfg> constraintViolation : constraintValidators) {
                String ruleId = constraintViolation.getRootBean().getId();
                String field = constraintViolation.getPropertyPath().toString();
                String message = constraintViolation.getMessage();
                if (errors.containsKey(ruleId)){
                    List<ValidError> temp = errors.get(ruleId);
                    temp.add(new ValidError(field, message));
                } else {
                    List<ValidError> temp = Lists.newArrayList(new ValidError(field, message));
                    errors.put(ruleId, temp);
                }
            }
            printError(errors);
        }
    }

    private static void printError(Map<String, List<ValidError>> errors) {
        errors.forEach((ruleId, error)->{
            log.error("{} 配置文件加载失败", ruleId);
            log.error("--------------------------------------------------");
            error.forEach(validError -> {
                log.error("{} : {}", validError.getField(), validError.getMessage());
            });
            log.error("--------------------------------------------------\n");
        });
    }
}
