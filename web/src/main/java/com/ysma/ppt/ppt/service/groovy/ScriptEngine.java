package com.ysma.ppt.ppt.service.groovy;

import java.util.Map;

/**
 * 脚本接口 定义接口标准
 */
public interface ScriptEngine {

    /**
     * 执行脚本文本
     * @param script 脚本
     * @param bindsMap 参数等载体
     */
    Object execute(String script, Map<String, Object> bindsMap);

}
