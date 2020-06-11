package com.kkb.sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ShellSort {
    public static void main(String[] args) {
        int num = 10;
        int[] arr = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = (int) (Math.random() * 8000000);
        }

        long t1 = System.currentTimeMillis();
        shellSort(arr);
        long t2 = System.currentTimeMillis();
        System.out.println((t2 - t1) / 1000.0);
        System.out.println(Arrays.toString(arr));
    }

    public static void shellSort(int[] arr) {
        int index, value;
        int length = arr.length;
        for (int gap = length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < length; i += gap) {
                index = i - gap;
                value = arr[i];
                while (index >= 0 && arr[index] > value) {
                    arr[index + gap] = arr[index];
                    index -= gap;
                }
                arr[index + gap] = value;
            }
        }
    }





    /*

     */


        /*
        //第一轮
        for (int i = 5; i < length; i++) {
            for (int j = i - 5; j >= 0; j -= 5) {
                if (arr[j] > arr[j + 5]) {
                    temp = arr[j];
                    arr[j] = arr[j + 5];
                    arr[j + 5] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr));
        //第二轮
        for (int i = 2; i < length; i++) {
            for (int j = i - 2; j >= 0; j -= 2) {
                if (arr[j] > arr[j + 2]) {
                    temp = arr[j];
                    arr[j] = arr[j + 2];
                    arr[j + 2] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr));
        //第三轮
        for(int i=1;i<length;i++){
            for (int j = i - 1; j >= 0; j -= 1) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr));

         */

}
