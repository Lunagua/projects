package com.kkb.sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class SelectSort {
    public static void selectSort(int[] arr) {
        int min = 0;
        int minIndex = 0;
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            min = arr[i];
            minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                count++;
                if (min > arr[j]) {
                    min = arr[j];
                    minIndex = j;

                }
            }
            arr[minIndex] = arr[i];
            arr[i] = min;
        }
        System.out.println(Arrays.toString(arr));
        System.out.println("循环次数"+count);
    }

    public static void main(String[] args) {

        int[] arr = {6, 3, 9, 4, 1, 0, 7, 8, 5, 2};
        selectSort(arr);
//        int num = 200000;
//        int[] arr = new int[num];
//        for (int i = 0; i < num; i++) {
//            arr[i] = (int) (Math.random() * 8000000);
//        }
//        long time1 = new Date().getTime();
//        selectSort(arr);
//        long time2 = new Date().getTime();
//
//        System.out.println(new SimpleDateFormat("HH:mm:ss").format(time1));
//        System.out.println(new SimpleDateFormat("HH:mm:ss").format(time2));
    }


}
