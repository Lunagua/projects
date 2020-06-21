package com.kkb.sort;

import java.util.Arrays;

public class RadixSort {
    public static void main(String[] args) {
//        int[] arr = {3, 2, 1, 6, 5, 4, 9, 8, 7, 0};
//        int[] arr = {53,3,542,748,14,214};
        int num = 10000000;
        int arr[] = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = (int) (Math.random() * 800000);
        }
        long t1 = System.currentTimeMillis();
        radixSort(arr);
        long t2 = System.currentTimeMillis();
        System.out.println((t2 - t1) / 1000.0);
    }

    public static void radixSort(int[] arr) {
        //取得数组中的最大数值
        int max = arr[0];
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        //计算最大数的位数
        int num = (max + "").length();
        //定义桶的二维数组
        int[][] bucket = new int[10][length];
        //定义一维数组,用于存放每个桶的有效数值个数
        int[] elementsCount = new int[10];
        int index = 0;
        //循环位数,进行处理,按照个,十,百,千,万... ...依次处理后,将对应的数值放入不同的桶中
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < length; j++) {
                int elementIndex = arr[j] / (int) (Math.pow(10, i)) % 10;
                bucket[elementIndex][elementsCount[elementIndex]++] = arr[j];
            }
            //依次从桶中取出数值,放回原数组
            index = 0;
            for (int k = 0; k < 10; k++) {
                if (elementsCount[k] != 0) {
                    for (int j = 0; j < elementsCount[k]; j++) {
                        arr[index++] = bucket[k][j];
                    }
                }
                elementsCount[k] = 0;
            }

        }


    }

}
