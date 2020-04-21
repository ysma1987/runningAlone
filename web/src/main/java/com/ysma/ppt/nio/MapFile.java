package com.ysma.ppt.nio;

import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * desc: mapped file
 *
 * @author ysma
 * date : 2020/4/21 9:35
 */
public class MapFile {


    public static void main(String[] args) {
        MappedByteBuffer mbf = getFile();
        Map<String, Integer> calcMap = read(mbf);
        Word[] words = wrapWords(calcMap);

        //构造top10的小根堆
        Word[] top10 = buildHeap(Arrays.copyOfRange(words, 0, 10), 10);
        for(int i = 10; i < words.length; i++){
            if(words[i].count > top10[0].count){
                top10[0] = words[i];
                heapify(top10, 10, 0);
            }
        }

        String  a = Arrays.stream(top10).map(w-> String.format("%s:%d", w.key, w.count)).collect(Collectors.joining(","));
        System.out.println(a);
        int len = top10.length - 1;
        for(int i = len; i >= 0; i--){
            ArrayUtils.swap(top10, i, 0);
            heapify(top10, i, 0);
        }

        String  b = Arrays.stream(top10).map(w-> String.format("%s:%d", w.key, w.count)).collect(Collectors.joining(","));
        System.out.println(b);
    }

    private static void heapify(Word[] words, int n, int i){
        if(i >= n){
            return;
        }

        int min = i;
        int c1 = 2*i + 1;
        int c2 = 2*i + 2;

        if(c1 < n && words[c1].count < words[min].count){
            min = c1;
        }

        if(c2 < n && words[c2].count < words[min].count){
            min = c2;
        }

        if(min != i){
            ArrayUtils.swap(words, i, min);
            heapify(words, n, min);
        }
    }

    //小根堆
    private static Word[] buildHeap(Word[] words, int n){
        int lastNode = n - 1;
        int parentNode = (lastNode - 1)/2;

        for(int i = parentNode; i > 0; i--){
            heapify(words, n, i);
        }
        return words;
    }

    /**构造数组*/
    private static Word[] wrapWords(Map<String, Integer> calcMap){
        Word[] words = new Word[calcMap.size()];
        AtomicInteger i = new AtomicInteger(0);
        calcMap.forEach((key, count)-> words[i.getAndIncrement()] = new Word(key, count));
        return words;
    }

    private static void parse(Map<String, Integer> calcMap, String s) {
        Arrays.stream(s.replaceAll("\\n", "")
                .replaceAll("\\r", "")
                .split(" "))
                .filter(StringUtils::isNotBlank)
                .forEach(word-> calcMap.compute(word, (key, v1)-> v1 == null ? 1 : v1 + 1));
    }

    private static Map<String, Integer> read(MappedByteBuffer mbf){
        Map<String, Integer> calcMap = new HashMap<>();
        byte[] bytes = new byte[1024];
        int remain = mbf.remaining();
        while (remain > 0){
            if(remain > 1024){
                mbf.get(bytes);
            } else {
                bytes = new byte[remain];
                mbf.get(bytes);
            }
            parse(calcMap, new String(bytes, Charset.forName("utf-8")));
            remain = mbf.remaining();
        }
        return calcMap;
    }

    private static MappedByteBuffer getFile(){
        try (FileChannel fileChannel = FileChannel.open(Paths.get("D:\\ysma\\test","druid-slow-sql-2019-09-23.log"))){
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Data
    static class Word{
        private String key;

        private Integer count;

        public Word(String key, Integer count){
            this.key = key;
            this.count = count;
        }

    }
}