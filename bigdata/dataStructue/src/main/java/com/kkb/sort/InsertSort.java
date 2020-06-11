package com.kkb.sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class InsertSort {
    public static void main(String[] args) {
//        int num = 200000;
//        int[] arr = new int[num];
//        for (int i = 0; i < num; i++) {
//            arr[i] = (int) (Math.random() * 8000000);
//        }
//
//        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
//        insertSort(arr);
//        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
////        System.out.println(Arrays.toString(arr));

        int[] arr = {6, 3, 9, 4, 1, 0, 7, 8, 5, 2};
        insertSort(arr);
        System.out.println(Arrays.toString(arr));
    }


    public static void insertSort(int[] arr) {
        /*
        思路,插入排序是将待排序的数列分为2段,第一段为有序,第二段为无序,
        1.第一次取第一个元素作为第一段有序数列
        2.依次取出第二段的元素,与第一段的比较过后,以有序的方式插入第一段,
        3.使用位移法,可以提升效率
         */
        int length = arr.length;
        int index, value;
        for (int i = 1; i < length; i++) {
            index = i - 1;
            value = arr[i];
            while (index >= 0 && arr[index] > value) {
                arr[index + 1] = arr[index];
                index--;
            }
            arr[index + 1] = value;
        }
    }

}
