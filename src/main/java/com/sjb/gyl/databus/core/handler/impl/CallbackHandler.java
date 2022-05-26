package com.sjb.gyl.databus.core.handler.impl;

import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.common.DsType;
import com.sjb.gyl.databus.common.ProtocolType;
import com.sjb.gyl.databus.core.exception.ExceptionPolicy;
import com.sjb.gyl.databus.core.handler.AbstractHandler;
import com.sjb.gyl.databus.core.msg.MsgWrapper;
import com.sjb.gyl.databus.core.rule.entity.DstCfg;
import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.Table;
import com.sjb.gyl.databus.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CallbackHandler extends AbstractHandler {
    private String getParams(Map<String, String> data, List<String> params) {
        Map<String, String> paramsMap = new HashMap<>();
        for (String p : params) {
            paramsMap.put(p, data.get(p));
        }
        return JSON.toJSONString(paramsMap);
    }

    @Override
    public boolean handleBusiness(MsgWrapper msgWrapper, Table table, Rule rule) {
        log.info("回调处理器：msgWrapper:{},table:{},rule:{}", JSON.toJSONString(msgWrapper), JSON.toJSONString(table), JSON.toJSONString(rule));
        boolean flag = true;
        for (DstCfg dstCfg : rule.getDstCfg()) {
            if (dstCfg.getDsType().equalsIgnoreCase(DsType.CALLBACK.getType())) {
                if (dstCfg.getProtocol().equalsIgnoreCase(ProtocolType.HTTP.getType())) {
                    String url = dstCfg.getUrl();
                    String params = getParams(msgWrapper.getOriginalData(), dstCfg.getParams());
                    log.info("httpPostJson url:{},headers:{},params:{}", url, dstCfg.getHeaders(), params);
                    String result = null;
                    try {
                        result = OkHttpUtils.httpPostJson(url, dstCfg.getHeaders(), params);
                    } catch (Exception e) {
                        log.error("回调处理器异常：msgWrapper:{},table:{},rule:{},e:{}", JSON.toJSONString(msgWrapper), JSON.toJSONString(table), JSON.toJSONString(rule), e);
                        flag = false;
                    }
                    log.info("httpPostJson url:{},headers:{},params:{},result:{}", url, dstCfg.getHeaders(), params, result);
                } else {
                    log.warn("不支持的callback协议：{}", JSON.toJSONString(dstCfg));
                    flag = false;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean canSkip() {
        return true;
    }

    @Override
    public ExceptionPolicy exceptionPolicy() {
        return ExceptionPolicy.RETRY;
    }
}
