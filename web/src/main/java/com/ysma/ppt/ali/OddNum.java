package com.ysma.ppt.ali;

/**
 *
 * 结果有问题~
 */
public class OddNum {

    //一个数组 有1种数出现了奇数次，其余的出现了偶数次，怎么找到并打印这种数
    private static void oddNum(){
        int[] arr = {123,456,657,893,657,456,893};//a^a = 0
        int eor = 0;//eor = 0^a^b^a^b^c...  eor为出现奇数次的数:c
        for(int i = 0; i < arr.length; i++){
            eor ^= arr[i];
        }

        System.out.println("有1种数出现了奇数次,出现奇数次的数为：" + eor);
    }

    //一个数组 有2种数出现了奇数次，其余的出现了偶数次，怎么找到并打印这种数
    private static void evenNum(){
        int[] arr = {123,235,578, 578, 235, 254};//a^a = 0
        int eor = 0;//eor = 0^a^b^a^b^c^d...  eor为出现奇数次的数:c^d
        for(int i = 0; i < arr.length; i++){
            eor ^= arr[i];
        }
        //eor二进制右侧第一个为1的数
        int rightOne = eor & (~eor + 1);

        int target = 0;
        for(int i = 0; i < arr.length; i++){
            if((arr[i] & rightOne) != 0){//分成两类  偶数和非偶数
                target ^= arr[i];
                System.out.println("test:" + arr[i] );
            }
        }
        System.out.println("onlyOne:" + (target ^ eor));
    }

    public static void main(String[] args) {
        oddNum();
        evenNum();
    }
}
