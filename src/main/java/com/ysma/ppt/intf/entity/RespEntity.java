package com.ysma.ppt.intf.entity;

import lombok.Data;

@Data
public class RespEntity {

    //结果
    private boolean success;

    //响应
    private Object data;
}
