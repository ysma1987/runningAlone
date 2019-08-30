package com.ysma.ppt;

import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Test {

    private static HashMap map = new HashMap();
    private static final ExecutorService executorService =
            Executors.newCachedThreadPool();

    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> ch = new ConcurrentHashMap();
        Integer a= ch.computeIfAbsent("ysma", new Function<String, Integer>(){
            @Override
            public Integer apply(String s) {
                System.out.println("======"+s);
                return 10;
            }

            @Override
            public <V> Function<String, V> andThen(Function<? super Integer, ? extends V> after) {
                System.out.println("==========<");
                return null;
            }
        });

        System.out.println(a);
        System.out.println(ch.get("ysma"));
    }

    public static void main2(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        for(int i =0; i <10000; i++){
            list.add(i);
        }

        StopWatch sw = StopWatch.createStarted();
        for(Integer i : list){
            //TimeUnit.MILLISECONDS.sleep(5);
        }
        System.out.println(sw.getTime(TimeUnit.MILLISECONDS));

        sw.reset(); sw.start();
        list.forEach(i->{
            try {
                //睡10毫秒是因为 lambda表达式存在线程开销
                //TimeUnit.MILLISECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(sw.getTime(TimeUnit.MILLISECONDS));
    }
}
