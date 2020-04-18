package com.ysma.ppt.sort;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author by ysma
 * desc: 堆排序-大根堆
 * Date 2020/4/18 19:13
 */
public class HeapSort {

    public static void main(String[] args) {
        int[] arr = {10,2,3,65,43,23,12,25,89,35,58};
        buildHeap(arr, arr.length);
        System.out.println(JSON.toJSON(arr));

        sortArr(arr);
    }

    /**
     * 进行排序
     * @param arr
     */
    private static void sortArr(int[] arr){
        int n = arr.length;
        for(int i = n - 1; i > 0; i--){
            ArrayUtils.swap(arr, i, 0);
            heapify(arr, i, 0);
        }
        System.out.println(JSON.toJSON(arr));
    }

    /**
     * 建堆
     * @param arr 数组
     */
    private static void buildHeap(int[] arr, int length) {
        int lastNode = length - 1;
        int parentNode = (lastNode - 1) / 2;
        for(int i = parentNode; i >= 0; i--){
            heapify(arr, length, i);
        }
    }

    /**
     * 堆排序
     * @param arr 数据集合
     * @param n 大小
     * @param i index
     */
    private static void heapify(int[] arr, int n, int i) {
        if(i >= n){//递归出口
            return;
        }
        int leftC = 2*i + 1;
        int rightC = 2*i + 2;
        int max = i;

        if(leftC < n //子节点不能出界
                && arr[leftC] > arr[max]){
            max = leftC;
        }

        if(rightC < n
                && arr[rightC] > arr[max]){
            max = rightC;
        }

        if(max != i){
            ArrayUtils.swap(arr, i, max);
            heapify(arr, n, max);//子堆继续排列
        }
    }
}
