package com.ysma.ppt.intf;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Json {

    String path() default "";

    Class[] types() default {Object.class};
}
