package com.sjb.gyl.databus.core.datasource.operate;

import com.alibaba.fastjson.JSON;
import com.google.common.base.CaseFormat;
import com.sjb.gyl.databus.common.EventType;
import com.sjb.gyl.databus.core.binlog.Binlog;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.DstCfg;
import com.sjb.gyl.databus.core.rule.entity.Field;
import com.sjb.gyl.databus.core.rule.entity.RedisDsCfg;
import com.sjb.gyl.databus.core.rule.entity.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RedisOperator extends AbstractOperator {

    @Override
    public void operate(MsgWrapper msgWrapper, DstCfg dstCfg) {
//        Jedis jedis = DataSourceClientHolder.getRedisClient(target.getDsId());
//        if (jedis != null) {
//            try {
//                doOperate(jedis, binlog, target.getRedisDsCfg());
//            } catch (Exception e) {
//                log.error("redis操作异常,binlog:{},target:{}", JSON.toJSONString(binlog), JSON.toJSONString(target));
//            } finally {
//                jedis.close();
//            }
//        } else {
//            //todo 异常处理
//        }
    }

    @Override
    public Map<String, Object> doMapping(Map<String, Field> fieldMap, Map<String, String> mysqlTypeMap, Map<String, String> dataMap) {
        return null;
    }

    @Override
    public boolean doUpsert(String eventType, Map<String, String> data, DstCfg dstCfg) {
        return false;
    }

    private void doOperate(Jedis jedis, Binlog binlog, RedisDsCfg redisDsCfg) {
        String type = binlog.getType();
        RedisKey redisKey = redisDsCfg.getKey();
        String key = genKey(redisKey);
        Map<String, String> valuesMap = populateValuesMap(redisDsCfg.getValues(), binlog.getData());
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(valuesMap)) {
            //todo key，values不存在，异常处理
            return;
        }
        if (type.equalsIgnoreCase(EventType.INSERT.getEvent())) {
            redisOps(jedis, redisKey, key, valuesMap);
        } else if (type.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            redisOps(jedis, redisKey, key, valuesMap);
        } else if (type.equalsIgnoreCase(EventType.DELETE.getEvent())) {
            jedis.del(key);
        }
    }

    private void redisOps(Jedis jedis, RedisKey redisKey, String key, Map<String, String> valuesMap) {
        if (redisKey.getType().equalsIgnoreCase("string")) {
            jedis.set(key, JSON.toJSONString(valuesMap));
        } else if (redisKey.getType().equalsIgnoreCase("map")) {
            jedis.hmset(key, valuesMap);
        } else {
            log.warn("redis sink 暂时只支持string和map数据结构,redisKey:", JSON.toJSONString(redisKey));
        }
    }

    /**
     * 1,string -> set user:1000 {"username":"gx","age":"12"}
     * 2,hash -> hmset user:1000 username gx age 12
     * 3,set -> sadd user:addr:1000 {"street":"光谷金融港A3"}
     * 4,list -> rpush user:
     */


    private static String genKey(RedisKey redisKey) {
        StringBuilder builder = new StringBuilder();

        //拼接前缀
        if (!StringUtils.isEmpty(redisKey.getPrefix())) {
            builder.append(redisKey.getPrefix());
            //拼接分隔符
            if (redisKey.getSeparator() != null && redisKey.getSeparator().length() > 0) {
                builder.append(redisKey.getSeparator());
            }
        }

        //拼接元素
        if (!CollectionUtils.isEmpty(redisKey.getItems())) {
            for (int i = 0; i < redisKey.getItems().size(); i++) {
                String item = redisKey.getItems().get(i);
                builder.append(item);
                //拼接分隔符(最后一个元素不需要拼接分隔符)
                if (i < (redisKey.getItems().size() - 1)) {
                    if (redisKey.getSeparator() != null && redisKey.getSeparator().length() > 0) {
                        builder.append(redisKey.getSeparator());
                    }
                }
            }
        }
        return builder.toString();
    }

    private Map<String, String> populateValuesMap(List<String> columns, List<Map<String, String>> data) {
        Map<String, String> cvMap = new HashMap<>(16);
        for (int i = 0; i < columns.size(); i++) {
            String col = columns.get(i);
            for (Map<String, String> temp : data) {
                for (String key : temp.keySet()) {
                    if (col.equals(key)) {
                        if (key.contains("_")) {
                            //将 数据库的列名转换为小驼峰，方便业务侧反序列化
                            cvMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key), temp.get(key));
                        } else {
                            cvMap.put(key, temp.get(key));
                        }
                    }
                }
            }

        }
        return cvMap;
    }


/*    public static void main(String[] args) {
        RedisKey redisKey = new RedisKey();
        redisKey.setPrefix("user");
        redisKey.setSeparator("_");
        List<String> items = new ArrayList<>();
        //items.add("123");
        items.add("456");

        redisKey.setItems(items);

        System.out.println(genKey(redisKey));

    }*/
}
