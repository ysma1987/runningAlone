package com.ysma.ppt.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class VariableConfig {

    @Value("${http.wrap.flag}")
    private Boolean httpWrapFlag;
}
