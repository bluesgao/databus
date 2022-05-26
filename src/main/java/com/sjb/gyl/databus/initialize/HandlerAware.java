package com.sjb.gyl.databus.initialize;

import com.sjb.gyl.databus.core.handler.HandlerChain;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HandlerAware implements ApplicationContextAware {

    //@Resource
    //private HandlerChain handlerChain;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        //handlerChain.setHandlers(context.getBeansOfType(Handler.class));
        //使用order进行降序排序（使用reversed()方法）
//        List<Handler> handlers = context.getBeansOfType(Handler.class).values().stream().sorted(Comparator.comparing(Handler::getOrder).reversed()).collect(Collectors.toList());
//        handlerChain.setHandlers(handlers);
    }
}
