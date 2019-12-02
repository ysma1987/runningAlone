package com.ysma.ppt.ppt.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.HashMultimap;

import java.util.HashMap;
import java.util.Map;

public class CompareUtil {

    /**map中获取频率最高的key*/
    public static Map<String, Integer> getMax(Map<String, Integer> targetMap){
        Integer max = targetMap.values().stream().max(Integer::compare).orElse(0);

        HashMultimap<Integer, String> multimap = HashMultimap.create();
        targetMap.forEach((key, val) -> multimap.put(val, key));
        System.out.println("频率最高词汇为:" + multimap.get(max) + ",次数为" + max);

        Map<String, Integer> wantMap = new HashMap<>();
        multimap.get(max).forEach(key ->wantMap.put(key, max));
        return wantMap;
    }

    public static void main(String[] args) {
        Map<String, Integer> targetMap = new HashMap<>();
        targetMap.put("ysma", 1);
        targetMap.put("text", 10);
        targetMap.put("hello", 10);
        targetMap.put("shuying", 5);
        Map<String, Integer> wantMap = getMax(targetMap);
        System.out.println("===>"+ JSON.toJSONString(wantMap));
    }
}
