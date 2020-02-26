package com.ysma.ppt;

/**
 * desc: TODO
 *
 * @author ysma
 * date : 2020/2/26 18:37
 */
public abstract class BaseTest {
    static {
        /**
         * Springboot整合Elasticsearch&redis 在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
