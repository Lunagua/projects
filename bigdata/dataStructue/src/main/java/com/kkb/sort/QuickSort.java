package com.kkb.sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class QuickSort {
    public static void main(String[] args) {
        int num = 10;
        int[] arr = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = (int) (Math.random() * 8000000);
        }

        long t1 = System.currentTimeMillis();
        quickSort(arr, 0, arr.length - 1);
        long t2 = System.currentTimeMillis();
        System.out.println((t2 - t1) / 1000.0);
        System.out.println(Arrays.toString(arr));
    }

    public static void quickSort(int[] arr, int left, int right) {
        int temp = 0;
        int l = left;
        int r = right;
        int mid = arr[(left + right) / 2];

        while (l < r) {
            while (arr[l] < mid) {
                l++;
            }
            while (arr[r] > mid) {
                r--;
            }
            if (l >= r) {
                break;
            }
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;

            if (arr[l] == mid) {
                r++;
            }
            if (arr[r] == mid) {
                l++;
            }
        }
        if (arr[l] == arr[r]) {
            r--;
            l++;
        }
        if (left < r) {
            quickSort(arr, left, r);
        }
        if (right > l) {
            quickSort(arr, l, right);
        }

    }
}