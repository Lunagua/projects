package com.kkb.sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class BubbleSort {

    public static void bubbleSort(int[] arr) {
        int temp = 0;
        boolean flag = false;
        int length = arr.length;

        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            } else {
                flag = false;
            }
        }
    }

    public static void bubbleSort(int[] arr, boolean reverse) {
        if (reverse) {
            bubbleSort(arr);
        } else {
            int temp = 0;
            boolean flag = false;
            int length = arr.length;
            for (int i = 0; i < length - 1; i++) {
                for (int j = 0; j < length - 1 - i; j++) {
                    if (arr[j] < arr[j + 1]) {
                        temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        flag = true;
                    }
                }
                if (!flag) {
                    break;
                } else {
                    flag = false;
                }
            }
        }

    }

    public static void main(String[] args) {
        int[] arr = new int[80000];
        for (int i = 0; i < 80000; i++) {
            arr[i] = (int) (Math.random() * 8000000);
        }
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        bubbleSort(arr, false);
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }


}
