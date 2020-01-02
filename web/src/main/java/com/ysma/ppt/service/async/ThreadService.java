package com.ysma.ppt.service.async;

import com.ysma.ppt.PptFuture;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;


/**
 * 描述异步服务
 * @see com.ysma.ppt Test
 */
@Service
public class ThreadService {

    private static volatile Map<String, Integer> indexMap = new HashMap<>();

    private AtomicReferenceArray<String> spinLock = new AtomicReferenceArray<>(10);

    private AtomicInteger intLock = new AtomicInteger(0);

    /**
     * 复杂请求 会产生多个请求分别请求不同的源
     * @param pptFuture source
     * @return result
     */
    public String calcFuture(PptFuture pptFuture){

        String name = pptFuture.getName();
        int vl = intLock.getAndAdd(1);
        Integer v = indexMap.putIfAbsent(name, vl);
        System.out.println("name:"+name+",v:" + v);
        //System.out.println("indexMap:"+indexMap.toString());
        //System.out.println("intLock:"+intLock.get());
        System.out.println("====================vl:"+vl);
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadService ts = new ThreadService();
        //CountDownLatch cd = new CountDownLatch(2);
        List<Thread> tlist = new ArrayList<>();

        for (int i =0; i< 10; i++){
            Thread a = new Thread(()->{
                PptFuture pptFuture = new PptFuture();
                pptFuture.setName("hah");
                ts.calcFuture(pptFuture);
            });
            tlist.add(a);
        }
        for (int i =0; i< 10; i++){
            Thread a = new Thread(()->{
                PptFuture pptFuture = new PptFuture();
                pptFuture.setName("heh");
                ts.calcFuture(pptFuture);
            });
            tlist.add(a);
        }
        //cd.countDown();
        for (Thread t : tlist){
            t.start();
        }
        TimeUnit.SECONDS.sleep(5);

    }
}
