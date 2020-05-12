package com.ysma.ppt.ali;

import java.util.concurrent.locks.LockSupport;

/**
 * unpark和park不同的地方是 todo 可以先unpack 一个线程A
 * 则A要park阻塞的时候 如果发现有人叫醒了就不会在park阻塞了
 */
public class OutputAlternately {

    static Thread t1= null, t2 = null;
    public static void main(String[] args) throws InterruptedException {
        char[] c1 = "1234567".toCharArray();
        char[] c2 = "ABCDEFG".toCharArray();
        t1 = new Thread(()->{
            for(char c : c1){
                System.out.println(c);
                LockSupport.unpark(t2);
                LockSupport.park();
            }

        }, "t1");

        t2 = new Thread(()->{
            for(char c : c2){
                LockSupport.park();
                System.out.println(c);
                LockSupport.unpark(t1);
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
