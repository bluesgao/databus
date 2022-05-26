package com.sjb.gyl.databus.core.datasource.operate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OperatorHolder {
    private static Map<String, Operator> DATASOURCE_OPERATOR_HOLDER = new HashMap<>(8);

//    static {
//        DATASOURCE_OPERATOR_HOLDER.put("mysql", new MysqlOperator());
//    }

    public static Operator getOperator(String type) {
        return DATASOURCE_OPERATOR_HOLDER.get(type);
    }

    public static void setOperators(Map<String, Operator> operators) {
        DATASOURCE_OPERATOR_HOLDER = operators;
    }
}
