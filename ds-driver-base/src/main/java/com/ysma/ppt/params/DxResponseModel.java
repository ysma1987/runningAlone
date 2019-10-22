package com.ysma.ppt.params;

import java.io.Serializable;

/**
 * @author ysma 2019-10-16 顶象二方库 出参标准模型
 */
public class DxResponseModel implements Serializable {

    private static final long serialVersionUID = -6586562349150257813L;

    /**
     * 返回值代码
     */
    private String resCode;

    /**
     * 返回值信息
     */
    private String resMsg;

    /**
     * 原始返回json
     */
    private String response;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
