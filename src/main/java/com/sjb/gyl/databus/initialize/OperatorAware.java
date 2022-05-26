package com.sjb.gyl.databus.initialize;

import com.sjb.gyl.databus.core.datasource.operate.Operator;
import com.sjb.gyl.databus.core.datasource.operate.OperatorHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class OperatorAware implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        OperatorHolder.setOperators(context.getBeansOfType(Operator.class));
    }
}
