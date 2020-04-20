package com.ysma.ppt.sort;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;

/**
 * desc: 小根堆
 *
 * @author ysma
 * date : 2020/4/20 10:41
 */
@SuppressWarnings("Duplicates")
public class SmallHeapSort {
    public static void main(String[] args) {
        int[] arr = {1,23,45,65,75,43,24,51,21,14,18,45,41};

        int n = arr.length;
        buildSmallHeap(arr, n);

        System.out.println(JSON.toJSONString(arr));

        for(int i = n - 1; i > 0; i--){
            ArrayUtils.swap(arr, i, 0);
            heapify(arr, i, 0);
        }
        System.out.println(JSON.toJSONString(arr));
    }

    /**
     * 小根堆
     * @param arr 数组
     * @param length 大小
     */
    private static void buildSmallHeap(int[] arr, int length) {

        int lastNode = length - 1;
        int parentNode = (lastNode - 1)/2;
        for(int i = parentNode; i >= 0; i--){
            heapify(arr, length, i);
        }
    }

    /**
     * 堆化
     * @param arr 数组
     * @param n 大小
     * @param i 位置
     */
    private static void heapify(int[] arr, int n, int i) {
        if(i >= n){
            return;
        }
        int c1 = 2*i + 1;
        int c2 = 2*i + 2;
        int min = i;

        if(c1 < n && arr[c1] < arr[min]){
            min = c1;
        }

        if(c2 < n && arr[c2] < arr[min]){
            min = c2;
        }

        if(min != i){
            ArrayUtils.swap(arr, min, i);
            heapify(arr, n, min);
        }
    }
}
