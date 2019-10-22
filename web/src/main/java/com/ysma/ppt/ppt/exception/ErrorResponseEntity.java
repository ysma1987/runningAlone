package com.ysma.ppt.ppt.exception;

import com.ysma.ppt.ppt.intf.entity.RespEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseEntity extends RespEntity {

    //原因码 结果为false时
    private String errorCode;

    //原因说明 结果为false时
    private String errorMsg;
}
