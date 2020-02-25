package com.ysma.ppt.es;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EConsumer {
    XFXJ("NJ", "南京使用"),
    H5("HZ", "杭州使用");
    private String code;
    private String desc;
}
