package com.kkb.sort;


import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
//        int[] arr = {3, 2, 1, 6, 5, 4, 9, 8, 7, 0};
        int num = 10;
        int arr[] = new int[num];
        int[] temp = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = (int) (Math.random() * 800000);
        }

        mergeSort(arr, 0, num - 1, temp);
        System.out.println(Arrays.toString(arr));
    }


    public static void mergeSort(int[] arr, int left, int right, int[] temp) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid, temp);
            mergeSort(arr, mid + 1, right, temp);
            merge(arr, left, mid, right, temp);
        }
    }

    public static void merge(int[] arr, int left, int mid, int right, int[] temp) {
        int l = left;
        int r = mid + 1;
        int index = 0;
        while (l <= mid && r <= right) {
            if (arr[l] <= arr[r]) {
                temp[index++] = arr[l++];
            } else {
                temp[index++] = arr[r++];
            }
        }

        while (l <= mid) {
            temp[index++] = arr[l++];
        }
        while (r <= right) {
            temp[index++] = arr[r++];
        }

        index = 0;
        int leftIndex = left;
        while (leftIndex <= right) {
            arr[leftIndex++] = temp[index++];
        }
    }


}