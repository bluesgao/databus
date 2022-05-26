package com.sjb.gyl.databus.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DataSourceValidator.class)
public @interface DataSource {
    String message() default "events包含update事件时,fields必须有值";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
