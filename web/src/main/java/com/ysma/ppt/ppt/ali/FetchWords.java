package com.ysma.ppt.ppt.ali;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ysma 2019-11-24
 */
public class FetchWords {

    private static final Logger log = LoggerFactory.getLogger(FetchWords.class);

    private static final String dir = "D:\\ysma\\test";

    private static final int processors = Runtime.getRuntime().availableProcessors();

    //智能线程 cpu数目
    private static ExecutorService executorService =  Executors.newWorkStealingPool(processors);

    //线程安全
    private static ConcurrentHashMap<String, Integer> calcMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        //1.获取文件目录
        File directory = new File(dir);

        //2.校验文件目录
        if(directory.exists()){
            //2-1.获取文件列表
            File[] files = directory.listFiles();
            if(files == null || files.length == 0){
                log.error("文件夹为空，path:{}", dir);
            } else {
                List<File> fileList = Arrays.asList(files);

                //多线程读取文件
                Future[] futures = new Future[processors];
                for(int i = 0; i < processors; i++){
                    futures[i] = executorService.submit(new SubFetchWords(i, fileList));
                }
                //关闭任务
                executorService.shutdown();

                for(Future future : futures){
                    while (!future.isDone() || future.isCancelled()){
                        //等待线程执行完成
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                Integer max = calcMap.values().stream().reduce(Integer::compareTo).orElse(0);
                for(Map.Entry<String, Integer> entry : calcMap.entrySet()){
                    if(entry.getValue().equals(max)){
                        log.info("FetchWords 多文件出现频率最高的词汇为:{},次数:{}", entry.getKey(), max);
                    }
                    System.out.println(entry.getKey()+"："+entry.getValue());
                }
            }
        } else {
            log.error("文件夹不存在,path:{}", dir);

        }
    }

    static class SubFetchWords implements Runnable{

        private int index;

        private List<File> fileList;

        SubFetchWords(int index, List<File> fileList){
            this.index = index;
            this.fileList = fileList;
        }

        @Override
        public void run() {
            //1.求余processors 决定文件归属线程
            BufferedReader br = null;
            try {
                for (int i = 0; i < fileList.size(); i++) {
                    if (i % processors == index) {
                        File file = fileList.get(i);
                        //2.读取文件 readline 小段读取  TODO 如果单行太长则采取另外方案
                        br = new BufferedReader(new FileReader(file));
                        String temp;
                        while ((temp = br.readLine()) != null){
                            String[] words = temp.split(" "); //空格分隔符
                            for(String word : words){
                                calcMap.compute(word, (k, v) -> v == null? 1 : v +1);
                            }
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                log.error("SubFetchWords FileNotFoundException 文件不存在", ex);
            } catch (IOException ex) {
                log.error("SubFetchWords IOException 文件解析异常", ex);
            } finally {
                if(br != null){
                    try {
                        br.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }
}