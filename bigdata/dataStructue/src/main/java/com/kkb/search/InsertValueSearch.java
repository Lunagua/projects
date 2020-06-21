package com.kkb.search;

public class InsertValueSearch {
    public static void main(String[] args) {
        int[] arr = {1, 23, 55, 66, 777, 8888, 9999, 9999, 9999, 9999, 10000};
        System.out.println(insertValueSearch(arr, 0, arr.length - 1, 1000));
    }

    public static int insertValueSearch(int[] arr, int left, int right, int findValue) {
        System.out.println("ci");
        if (left > right || findValue < arr[left] || findValue > arr[right]) {
            return -1;
        }

        int midIndex = left + (right - left) * (findValue - arr[left]) / (arr[right] - arr[left]);
        int midValue = arr[midIndex];

        if (findValue < midValue) {
            return insertValueSearch(arr, left, midIndex - 1, findValue);
        } else if (findValue > midValue) {
            return insertValueSearch(arr, midIndex + 1, right, findValue);
        } else {
            return midIndex;
        }
    }
}
