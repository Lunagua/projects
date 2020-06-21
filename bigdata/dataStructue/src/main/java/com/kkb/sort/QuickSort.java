package com.kkb.sort;

import java.util.Arrays;

public class QuickSort {
    public static void main(String[] args) {
//                int[] arr = {3, 2, 1, 6, 5, 4, 9, 8, 7, 0};
        int num = 10;
        int arr[] = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = (int) (Math.random() * 800000);
        }
        quickSort(arr, 0, num - 1);
        System.out.println(Arrays.toString(arr));
    }


    public static void quickSort(int[] arr, int left, int right) {
        int l = left;
        int r = right;
        int pivot = arr[(left + right) / 2];
        int temp = 0;

        while (l < r) {

            while (arr[l] < pivot) {
                l++;
            }
            while (arr[r] > pivot) {
                r--;
            }

            if (l >= r) {
                break;
            }

            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;

            if (arr[l] == pivot) {
                r--;
            }
            if (arr[r] == pivot) {
                l++;
            }
        }
        if(l==r){
            l++;
            r--;
        }
        if (left < r) {
            quickSort(arr, left, r);
        }
        if (l < right) {
            quickSort(arr, l, right);
        }
    }
}