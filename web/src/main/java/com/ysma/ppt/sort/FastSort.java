package com.ysma.ppt.sort;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;

/**
 * desc: 快速排序
 * 1、分界值进行分界：大于分界值在右边,小于分界值在左边
 * 2、递归左右区域执行再次分界-直到排序完成
 *
 * @author ysma
 * date : 2020/4/20 9:22
 */
public class FastSort {

    public static void main(String[] args) {
        int[] arr = {1,98,23,26,65,47,54,32,38,61};
        fastSort(arr, 0, arr.length - 1);
        System.out.println(JSON.toJSONString(arr));
    }

    private static void fastSort(int[] arr, int start, int end) {
        int low = start;
        int high = end;
        int index = start;

        while (low < high){
            //右边集合
            while (arr[high] >= arr[index] && high > low){
                high--;
            }
            if(arr[high] < arr[index]){//小于分界值 交换
                ArrayUtils.swap(arr, high, index);
                index = high;
            }
            //左边集合
            while (arr[low] <= arr[index] && low < high){
                low++;
            }
            if(arr[low] > arr[index]){
                ArrayUtils.swap(arr, low, index);
                index = low;
            }
        }

        if(low > start){
            fastSort(arr, start, low - 1);
        }
        if(high < end){
            fastSort(arr, high + 1, end);
        }
    }
}
