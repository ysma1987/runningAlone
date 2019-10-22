package com.ysma.ppt.ppt.util.resource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class VariableConfig {

    @Value("${http.wrap.flag}")
    private Boolean httpWrapFlag;

    /**restTemplate 请求超时时间*/
    @Value("${rest.template.time.out}")
    private Integer restTemplateTimeOut;

    @Value("${file.path}")
    private String filePath;
}
