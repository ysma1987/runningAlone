package com.ysma.ppt.params;

import java.io.Serializable;
import java.util.Map;

/**
 * @author ysma 2019-10-16 顶象二方库 入参标准模型
 */
public class DxRequestModel implements Serializable {

    private static final long serialVersionUID = 9101682693323154549L;

    /**
     * 请求权限校验需要的字段Map
     */
    private Map<String, String> keyMap;

    /**
     * 请求参数map,放入所有的可选必选参数，校验工作由CTU-Engine完成
     */
    private Map<String, String> argsMap;

    /**
     * 发送请求的Url
     */
    private String apiUrl;

    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, String> keyMap) {
        this.keyMap = keyMap;
    }

    public Map<String, String> getArgsMap() {
        return argsMap;
    }

    public void setArgsMap(Map<String, String> argsMap) {
        this.argsMap = argsMap;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

}
