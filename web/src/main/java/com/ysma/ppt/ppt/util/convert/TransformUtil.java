package com.ysma.ppt.ppt.util.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author ysma 转换工具
 */
@Slf4j
public class TransformUtil {

    @SafeVarargs
    public static  <S, T>  T transform(S s, Class<T> clazz, BiFunction<S, T, T>... function) {
        if(s != null){
            try {
                T t = clazz.newInstance();
                BeanUtils.copyProperties(s, t);
                return function.length == 0 ? t : function[0].apply(s, t);
            } catch (InstantiationException | IllegalAccessException ex) {
                log.error("TransformService.transform 对象赋值异常", ex);
            }
        }
        return null;
    }

    @SafeVarargs
    public static  <S, T> List<T> transformList(List<S> sList, Class<T> clazz, BiFunction<S, T, T>... function){
        try {
            if(!CollectionUtils.isEmpty(sList)){
                List<T> tList = new ArrayList<>();
                sList.forEach(s ->{
                    if(s != null){
                        tList.add(transform(s, clazz, function));
                    }
                });
                return tList;
            }
        } catch (Exception ex) {
            log.error("TransformService.transform 对象赋值异常", ex);
            throw new RuntimeException("复制转换失败");
        }
        return Collections.emptyList();
    }
}
