package com.ysma.ppt.ppt.util;

import java.util.ArrayList;
import java.util.List;

public class ThreadUtil {

    //自带初始化 跨线程时无能为力
    private static final ThreadLocal<List<String>> stringLocal =
            ThreadLocal.withInitial(ArrayList::new);

    //未自带初始化 父子线程通信
    private static final InheritableThreadLocal<String> inheritStringLocal =
            new InheritableThreadLocal<>();


    public static void setString(String param){
        stringLocal.get().add(param);
    }
    public static List<String> getString(){
        return stringLocal.get();
    }

    public static void setInheritString(String param){
        inheritStringLocal.set(param);
    }

    public static String getInheritString(){
        return inheritStringLocal.get();
    }

    /**
     * 是ThreadLocal为弱引用，当主线程消亡后，其指向内容也会被gc掉
     * 但是tomcat的Thread是复用的
     *
     * 可通过减少tomcat的线程数复现此 不clear导致的内存泄漏的bug
     */
    public static void clear(){
        stringLocal.remove();
        inheritStringLocal.remove();
    }
}
