package com.kkb.search;

import java.util.ArrayList;

public class BinarySearch {
    public static void main(String[] args) {

        int[] arr = {1, 23, 55, 66, 777, 8888, 9999, 9999, 9999, 9999, 10000};
        System.out.println(binarySearch(arr, 0, arr.length, 1));
        System.out.println(binarySearch2(arr, 0, arr.length - 1, 10000));

    }

    public static int binarySearch(int[] arr, int left, int right, int findVal) {
        if (left > right || findVal < arr[0] || findVal > arr[arr.length - 1]) {
            return -1;
        }
        int midIndex = (left + right) / 2;
        int midVal = arr[midIndex];

        if (findVal > midVal) {
            return binarySearch(arr, midIndex + 1, right, findVal);
        } else if (findVal < midVal) {
            return binarySearch(arr, left, midIndex - 1, findVal);
        } else {
            return midIndex;
        }
    }

    public static ArrayList<Integer> binarySearch2(int[] arr, int left, int right, int findValue) {
        if (left > right || findValue < arr[0] || findValue > arr[arr.length - 1]) {
            return new ArrayList<>();
        }
        int midIndex = (left + right) / 2;
        int midValue = arr[midIndex];

        if (findValue > midValue) {
            return binarySearch2(arr, midIndex + 1, right, findValue);
        } else if (findValue < midValue) {
            return binarySearch2(arr, left, midIndex - 1, findValue);
        } else {
            ArrayList<Integer> list = new ArrayList<>();
            int temp = midIndex - 1;
            while (true) {
                if (temp < 0 || arr[temp] != findValue) {
                    break;
                }
                list.add(temp--);
            }
            list.add(midIndex);
            temp = midIndex + 1;
            while (true) {
                if (temp >= arr.length || arr[temp] != findValue) {
                    break;
                }
                list.add(temp++);
            }
            return list;
        }
    }
}
