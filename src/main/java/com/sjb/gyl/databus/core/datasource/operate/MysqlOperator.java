package com.sjb.gyl.databus.core.datasource.operate;

import cn.hutool.core.date.DateUtil;
import com.sjb.gyl.databus.common.EventType;
import com.sjb.gyl.databus.core.datasource.client.ClientHolder;
import com.sjb.gyl.databus.core.rule.entity.DstCfg;
import com.sjb.gyl.databus.core.rule.entity.Field;
import com.sjb.gyl.databus.core.rule.entity.FieldMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("mysql")
public class MysqlOperator extends AbstractOperator {
    /**
     * 根据mappings配置进行字段转换及数据转换
     */
    @Override
    public Map<String, Object> doMapping(Map<String, Field> fieldMap, Map<String, String> mysqlTypeMap, Map<String, String> dataMap) {
        Map<String, Object> cvMap = new HashMap<>(32);
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();
            String oldFieldName = entry.getKey();
            String oldFieldValue = dataMap.get(oldFieldName);
            String oldFieldType = mysqlTypeMap.get(oldFieldName);
            Object newFieldValue = null;
            String newFieldName = field.getMapping().getName();
            if (newFieldName == null || newFieldName.length() == 0) {
                newFieldName = oldFieldName;
            }
            FieldMapping fieldMapping = field.getMapping();
            if (oldFieldValue != null) {
                //数据转换
                if (fieldMapping.getType() != null) {
                    if (!fieldMapping.getType().equalsIgnoreCase(oldFieldType)) {
                        if (fieldMapping.getType().equalsIgnoreCase("string")) {
                            newFieldValue = oldFieldValue;
                        } else if (fieldMapping.getType().equalsIgnoreCase("int")) {
                            newFieldValue = Integer.valueOf(oldFieldValue);
                        } else if (fieldMapping.getType().equalsIgnoreCase("long")) {
                            //newFieldValue = Long.valueOf(oldFieldValue);
                            newFieldValue = new BigInteger(oldFieldValue);
                        } else if (fieldMapping.getType().equalsIgnoreCase("date") && !StringUtils.isEmpty(fieldMapping.getFormat())) {
                            newFieldValue = DateUtil.parse(oldFieldValue, fieldMapping.getFormat());
                        }
                    }
                }
            } else {
                //给默认值
                newFieldValue = field.getDefaultValue();
            }
            cvMap.put(newFieldName, newFieldValue);
        }

        return null;
    }

    private Map<String, String> getWhereMap(Map<String, String> data, DstCfg dstCfg) {
        Map<String, String> whereMap = new HashMap<>(8);
        for (String fk : dstCfg.getUniqueFields()) {
            whereMap.put(fk, data.get(fk));
        }
        return whereMap;
    }

    @Override
    public boolean doUpsert(String eventType, Map<String, String> data, DstCfg dstCfg) {
        StringBuilder sql = new StringBuilder();
        //组装sql insert into t_user(id,name) values(12,test);
        if (eventType.equalsIgnoreCase(EventType.INSERT.getEvent()) || eventType.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            sql.append("insert into ").append(dstCfg.getTable()).append(" (");

            StringBuilder values = new StringBuilder(" values(");
            int i = 0;
            for (String key : data.keySet()) {
                String value = data.get(key);
                if (!StringUtils.isEmpty(data.get(key)) && !value.equalsIgnoreCase("null")) {
                    sql.append(key);
                    /*if (value.equalsIgnoreCase("false")) {
                        values.append("'" + 0 + "'");
                    } else if (value.equalsIgnoreCase("true")) {
                        values.append("'" + 1 + "'");
                    } else {
                        values.append("'" + value + "'");
                    }*/
                    values.append("'" + value + "'");
                    if (i < data.keySet().size() - 1) {
                        sql.append(",");
                        values.append(",");
                    }
                }
                i++;
            }
            sql.append(") ");
            values.append(")");
            sql.append(values);

            //update
            int j = 0;
            sql.append(" ON DUPLICATE KEY UPDATE ");
            for (String key : data.keySet()) {
                String value = data.get(key);
                if (!StringUtils.isEmpty(data.get(key)) && !value.equalsIgnoreCase("null")) {
                    sql.append(key);
                    sql.append(" = ");
                    sql.append("'" + value + "'");
                    /*if (value.equalsIgnoreCase("false")) {
                        sql.append("'" + 0 + "'");
                    } else if (value.equalsIgnoreCase("true")) {
                        sql.append("'" + 1 + "'");
                    } else {
                        sql.append("'" + value + "'");
                    }*/
                    if (j < data.keySet().size() - 1) {
                        sql.append(" , ");
                    }
                }
                j++;
            }

        } /*else if (eventType.equalsIgnoreCase("update")) {

            Map<String, String> whereMap = new HashMap<>(8);
            for (String fk : dstCfg.getUniqueFields()) {
                whereMap.put(fk, data.get(fk));
            }
            if (CollectionUtils.isEmpty(whereMap)) {
                return false;
            }
            //组装sql
            *//**
         * 实例：
         *
         * 更新了员工编号1056的姓氏和电子邮件列：
         * UPDATE employees
         * SET
         *     lastname = 'Hill',
         *     email = 'mary.hill@yiibai.com'
         * WHERE
         *     employeeNumber = 1056;
         *//*

            sql.append("update ").append(dstCfg.getTable()).append(" set ");
            int i = 0;
            for (String key : data.keySet()) {
                sql.append(key);
                sql.append(" = ");
                sql.append("'" + data.get(key) + "'");
                if (i < data.keySet().size() - 1) {
                    sql.append(",");
                }
                i++;
            }

            //where
            for (String key : whereMap.keySet()) {
                sql.append(" where ");
                sql.append(key);
                sql.append(" = ");
                sql.append("'" + whereMap.get(key) + "'");
                if (i <= whereMap.keySet().size() - 1) {
                    sql.append(" and ");
                }
                i++;
            }

            sql.append(" ; ");
        }*/
        log.info("组装sql:{}", sql.toString());

        if (!StringUtils.isEmpty(sql.toString())) {
            try {
                ClientHolder.getJdbcClient(dstCfg.getDsId()).execute(sql.toString());
                return true;
            } catch (DataAccessException e) {
                log.error("sql执行失败，sql:{},e:{}", sql.toString(), e);
            }
        }


        return false;
    }
}
