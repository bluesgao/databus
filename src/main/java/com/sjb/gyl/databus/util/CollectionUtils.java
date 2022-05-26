package com.sjb.gyl.databus.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
    //list转map
    public static Map<String, String> toMap(List<Map<String, String>> data) {
        Map<String, String> resultMap = new HashMap<>(16);
        for (Map<String, String> item : data) {
            resultMap.putAll(item);
        }
        return resultMap;
    }
}
