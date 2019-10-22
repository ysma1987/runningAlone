package com.ysma.ppt.anno;

import java.lang.annotation.*;

@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DriverDescAnno {

    //驱动入口 实现jar包自行覆盖
    String driverName() default "com.dingxianginc.obj.IDxDriver";
}
