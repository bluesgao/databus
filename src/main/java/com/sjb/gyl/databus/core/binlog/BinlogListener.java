package com.sjb.gyl.databus.core.binlog;

import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.common.Constants;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BinlogListener {

    @Resource
    private BinlogDispatcher dispatcher;

    @KafkaListener(topics = "#{'${topicName}'.split(',')}", id = Constants.BINLOG_CONSUMER_GROUP_ID)
//    @KafkaListener(topics = Constants.BINLOG_TOPIC_PATTERN, id = Constants.BINLOG_CONSUMER_GROUP_ID)
    public void onMessage(List<ConsumerRecord<?, ?>> records) {
        log.info("binlog批量拉取的数据数量:{}:", records.size());

        List<MsgWrapper> msgWrappers = new ArrayList<MsgWrapper>(records.size());
        for (ConsumerRecord<?, ?> record : records) {
            if (Optional.ofNullable(record.value()).isPresent()) {
                //parse binlog
                Binlog binlog = JSON.parseObject(record.value().toString(), Binlog.class);
                log.info("binlog:{}", JSON.toJSONString(binlog));
                //包装binlog
                msgWrappers.add(MsgWrapper.warp(binlog));
            }
        }

        log.info("msgWrappers:{}", JSON.toJSONString(msgWrappers));
        if (!CollectionUtils.isEmpty(msgWrappers)) {
            //处理binlog
            dispatcher.dispatch(msgWrappers);
        }

    }
}
