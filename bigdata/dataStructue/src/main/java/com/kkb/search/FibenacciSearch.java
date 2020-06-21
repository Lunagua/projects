package com.kkb.search;

import java.util.Arrays;

public class FibenacciSearch {
    public static void main(String[] args) {
//        System.out.println(Arrays.toString(fib(20)));
        int[] arr = {1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765};
        System.out.println(fibebacciSearch(arr, 34));
    }

    public static int fibebacciSearch(int[] arr, int findValue) {
        int length = arr.length;        //数列长度
        int mid = 0;                    //分割点下标
        int[] f = fib(20);       //斐波那契数列
        int k = 0;                      //斐波那契数列数值下标
        int left = 0;                   //最左下标
        int right = length - 1;         //最右下标
        while (length > f[k]) {         //获取斐波那契数列的下标k的值,需要保证f[k]大于待查找数列的长度
            k++;
        }
        int[] temp = Arrays.copyOf(arr, f[k]);          //新建数组temp,存放新数列,比原数组 长的部分用最大值来补足
        for (int i = length; i < temp.length; i++) {
            temp[i] = arr[right];
        }
        while (left <= right) {
            mid = left + f[k - 1] - 1;
            if (findValue < temp[mid]) {
                right = mid - 1;
                k--;
            } else if (findValue > temp[mid]) {
                left = mid + 1;
                k -= 2;
            } else {
                if (mid < right) {
                    return mid;
                } else {
                    return right;
                }
            }
        }
        return -1;
    }

    public static int[] fib(int num) {
        int[] f = new int[num];
        f[0] = f[1] = 1;
        for (int i = 2; i < num; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }
        return f;
    }

}
