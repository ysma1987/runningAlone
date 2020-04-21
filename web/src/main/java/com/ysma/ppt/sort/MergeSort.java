package com.ysma.ppt.sort;

/**
 * desc: 归并排序：指的是将两个顺序序列合并成一个顺序序列的方法
 *
 * @author ysma
 * date : 2020/4/21 11:36
 */
public class MergeSort {

    public static void main(String[] args) {
        int[] arr = {23,34,12,26,73,82,19,54,81};
        int[] arr2 = merge(arr, 0, arr.length - 1);
        for (int i =0;i < arr2.length; i++)
            System.out.print(arr2[i] + ",");
    }

    /**
     * 归并
     * @param arr 数组
     * @param low 起始位置
     * @param high 截止位置
     * @return new int[]
     */
    private static int[] merge(int[] arr, int low, int high){
        if(low == high){
            return new int[]{arr[low]};
        }

        int mid = (high + low)/2;

        int[] lrr = merge(arr, low, mid);//左
        int[] rrr = merge(arr, mid + 1, high);//右
        int[] mrr = new int[lrr.length + rrr.length];//新

        int i = 0, j = 0, m = 0;
        while (i < lrr.length && j < rrr.length){
            mrr[m++] = lrr[i] < rrr[j] ? lrr[i++] : rrr[j++];
        }

        while (i < lrr.length){
            mrr[m++] = lrr[i++];
        }

        while (j < rrr.length){
            mrr[m++] = rrr[j++];
        }

        return mrr;
    }
}
