package com.ysma.ppt.intf;


import com.ysma.ppt.es.EConsumer;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnLog {

    //是否启用es
    boolean esOn() default false;

    EConsumer source() default EConsumer.XFXJ;

}
