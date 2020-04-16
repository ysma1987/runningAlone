package com.ysma.ppt.ali;// ## 请在下方描述你的面试题内容( 支持Markdown )

import java.io.*;
import java.util.*;

public class ShowMeBug {
  /*
   * 输入两个升序(从小到大)的整数数列，合并成一个升序(从小到大)的数列返回。
   * 要求不能用系统提供的sort，并尽量考虑优化算法效率。
   * 例如：输入 [1,3,5] , [2,4,6] 输入 [1,2,3,4,5,6]
   */
  public static ArrayList<Integer> mergeArray(ArrayList<Integer> arr1, ArrayList<Integer> arr2) {
    // 在这里写代码
    arr1.addAll(arr2);
    quickSort(arr1, 0, arr1.size()-1);
    return arr1;
  }

  public static void quickSort(ArrayList<Integer> arr, int start, int end){
    //1.快分
    int key = arr.get(end);
    int low = start;
    int high = end;
    while (low < high){
      //前-> 后   小于key的 左边
      while (low < high && arr.get(low) <= key)
        low++;
      if(arr.get(low) >= key){//swap
        swap(arr, low, high);
      }

      //后-> 前 大于key的 右边
      while (low < high && arr.get(high) >= key)
        high--;
      if(arr.get(high) <= key){
        swap(arr, low, high);
      }

      if(low > start) quickSort(arr, start, low-1);
      if(high < end) quickSort(arr, high + 1, end);
    }
  }

  public static void swap(ArrayList<Integer> arr, int left, int right){
    Integer temp = arr.get(left);
    arr.set(left, arr.get(right));
    arr.set(right, temp);
  }

  public static void fastSort(ArrayList<Integer> arr, int low, int high){
    int start = low;
    int end = high;
    int key = arr.get(low);
    while (start < end){
      //后->前
      while (end > start && arr.get(end) >= key){
        end--;
      }
      if(arr.get(end) <= key){//swap
        Integer temp = arr.get(end);
        arr.set(end, arr.get(start));
        arr.set(start, temp);
      }

      //前->后
      while (start < end && arr.get(start) <= key){
        start++;
      }
      if(arr.get(start) >= key){//swap
        Integer temp = arr.get(start);
        arr.set(start, arr.get(end));
        arr.set(end, temp);
      }

      //递归
      if(start>low) fastSort(arr, low, start-1);
      if(end<high) fastSort(arr, end+1, high);
    }
  }

  //插入排序
  public static ArrayList<Integer> mysort(ArrayList<Integer> arr){
    for(int i =1; i < arr.size(); i++){
      for(int j = 0; j < i; j++){
        if (arr.get(j) > arr.get(i)){
          Integer temp = arr.get(j);
          arr.set(j, arr.get(i));
          arr.set(i, temp);
        }
      }
    }
    return arr;
  }
  
  /*
   * 请添加一个测试实例
   */
  public static void test3() {
    // 在这里写测试

  }
  
  public static void main(String[] args) {
    test1();
    test2();
    //test3();
  }

  /*
   * 测试合并[1,3,5],[2,4,6]，期望输出[1,2,3,4,5,6]
   */  
  public static void test1() {
    ArrayList<Integer> arr1 = new ArrayList<Integer>();
    arr1.add(1);
    arr1.add(3);
    arr1.add(5);

    ArrayList<Integer> arr2 = new ArrayList<Integer>();
    arr2.add(2);
    arr2.add(4);
    arr2.add(6);
    
    ArrayList<Integer> result = mergeArray(arr1, arr2);
    for (Integer num : result) {
      System.out.print(num +",");
    } // 期望输出 1 2 3 4 5 6
    System.out.println();
  }

  /*
   * 测试合并[-10,10,100],[1,2,3,4,200,300,400]，期望输出[-10,1,2,3,4,10,100,200,300,400]
   */  
  public static void test2() {
    ArrayList<Integer> arr1 = new ArrayList<Integer>();
    arr1.add(-10);
    arr1.add(10);
    arr1.add(100);

    ArrayList<Integer> arr2 = new ArrayList<Integer>();
    arr2.add(1);
    arr2.add(2);
    arr2.add(3);
    arr2.add(4);
    arr2.add(200);
    arr2.add(300);
    arr2.add(400);
    
    ArrayList<Integer> result = mergeArray(arr1, arr2);
    for (Integer num : result) {
      System.out.print(num+",");
    } // 期望输出 [-10,1,2,3,4,10,100,200,300,400]
  }
}
