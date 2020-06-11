package com.kkb.sort;

import java.util.Arrays;
import java.util.Date;

public class MergeSort {
    public static void main(String[] args) {
        int num = 10;
        int[] arr = new int[num];
//        int[] arr = {6, 5, 9, 3, 2, 0, 1, 8, 7, 4};
        int[] temp = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = (int) (Math.random() * 8000000);
        }
        long t1 = new Date().getTime();
        mergeSort(arr, 0, arr.length - 1, temp);
        long t2 = new Date().getTime();
        System.out.println((t2 - t1) / 1000.0);
        System.out.println(Arrays.toString(arr));
    }

    public static void mergeSort(int[] arr, int left, int right, int[] temp) {
        int mid;
        if (left < right) {
            mid = (left + right) / 2;
            mergeSort(arr, left, mid, temp);
            mergeSort(arr, mid + 1, right, temp);
            merge(arr, left, mid, right, temp);
        }
    }

    public static void merge(int[] arr, int left, int mid, int ringht, int[] temp) {
        int l = left;
        int r = mid + 1;
        int index = 0;
        while (l <= mid && r <= ringht) {
            if (arr[l] < arr[r]) {
                temp[index] = arr[l];
                l++;
                index++;
            } else {
                temp[index] = arr[r];
                r++;
                index++;
            }
        }

        while (r <= ringht) {
            temp[index] = arr[r];
            r++;
            index++;
        }
        while (l <= mid) {
            temp[index] = arr[l];
            l++;
            index++;
        }
        index = 0;
        int leftIndex = left;
        while (leftIndex <= ringht) {
            arr[leftIndex] = temp[index];
            leftIndex++;
            index++;
        }
    }
}
