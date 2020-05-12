package com.ysma.ppt.ali;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * desc: TransferQueue 队列实现数字字母交替输出
 *      newCachedThreadPool就可以用这个
 *
 * @author ysma
 * date : 2020/5/12 18:28
 */
public class OutputAlternately2 {
    public static void main(String[] args) {
        char[] c1 = "1234567".toCharArray();
        char[] c2 = "ABCDEFG".toCharArray();

        //阻塞的容量为0的队列
        TransferQueue<Character> queue = new LinkedTransferQueue<>();

        new Thread(()->{
            for(char c : c1){
                try {
                    System.out.println(queue.take());
                    queue.transfer(c);//没有线程来取走c  则阻塞
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        new Thread(()->{
            for(char c : c2){
                try {
                    queue.transfer(c);//没有线程来取走c  则阻塞
                    System.out.println(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();
    }
}
