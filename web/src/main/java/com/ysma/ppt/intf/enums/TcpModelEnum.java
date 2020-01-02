package com.ysma.ppt.intf.enums;

import lombok.AllArgsConstructor;

/**
 * @author ysma
 * TCP 模式
 */
@AllArgsConstructor
public enum TcpModelEnum {
    CLOSE_BEFORE("BEFORE", "客户端先关闭"),
    CLOSE_AFTER("AFTER", "客户端后关闭");

    //TCP通道模式
    private String model;

    //TCP通道描述
    private String desc;
}