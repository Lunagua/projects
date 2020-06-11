package com.kkb.sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class SortDemo {
    public static void main(String[] args) {
        int num = 50000000;
        int[] arr = new int[num];
        int[] temp = new int[num];
        long time1, time2;
        for (int k = 0; k < 4; k++) {
            for (int i = 0; i < num; i++) {
                arr[i] = (int) (Math.random() * 8000000);
            }
            switch (k) {
                case 0:
                    time1 = System.currentTimeMillis();
                    quickSort(arr, 0, arr.length - 1);
                    time2 = System.currentTimeMillis();
                    System.out.println("快速排序时间" + (time2 - time1) / 1000.0);
                    break;
                case 1:
                    time1 = System.currentTimeMillis();
                    mergeSort(arr, 0, arr.length - 1, temp);
                    time2 = System.currentTimeMillis();
                    System.out.println("归并排序时间:" + (time2 - time1) / 1000.0);
                    break;
                case 2:
//                    time1 = System.currentTimeMillis();
//                    insertSort(arr);
//                    time2 = System.currentTimeMillis();
//                    System.out.println("插入排序时间:" + (time2 - time1) / 1000.0);
                    break;
                default:
                    time1 = System.currentTimeMillis();
                    shellSort(arr);
                    time2 = System.currentTimeMillis();
                    System.out.println("希尔排序时间:" + (time2 - time1) / 1000.0);
                    break;
            }
        }
    }

    /**
     * 冒泡排序
     */
    public static void bubbleSort(int[] arr) {
        int length = arr.length;
        int temp = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
//        System.out.println(Arrays.toString(arr));
    }

    /**
     * 选择排序
     */
    public static void selectSort(int[] arr) {
        int length = arr.length;
        int min = 0;
        int minIndex = 0;
        boolean flag = false;
        for (int i = 0; i < length; i++) {
            min = arr[i];
            minIndex = i;
            for (int j = i; j < length; j++) {
                if (min > arr[j]) {
                    min = arr[j];
                    minIndex = j;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            } else {
                flag = false;
            }
            arr[minIndex] = arr[i];
            arr[i] = min;

        }
//        System.out.println(Arrays.toString(arr));
    }

    /**
     * 插入排序
     */
    public static void insertSort(int[] arr) {
        int length = arr.length;
        int value = 0;
        int index = 0;
        for (int i = 1; i < length; i++) {
            value = arr[i];
            index = i - 1;
            while (index >= 0 && value < arr[index]) {
                arr[index + 1] = arr[index];
                index--;
            }
            arr[index + 1] = value;
        }
//        System.out.println(Arrays.toString(arr));
    }

    /**
     * 希尔排序
     */
    public static void shellSort(int[] arr) {
        int length = arr.length;
        int value = 0;
        int index = 0;
        for (int gap = length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < length; i++) {
                value = arr[i];
                index = i - gap;

                while (index >= 0 && value < arr[index]) {
                    arr[index + gap] = arr[index];
                    index -= gap;
                }
                arr[index + gap] = value;

            }
        }
//        System.out.println(Arrays.toString(arr));
    }

    /**
     * 快速排序
     */
    public static void quickSort(int[] arr, int left, int right) {
        int l = left;                           //左游标
        int r = right;                          //右游标
        int temp = 0;                           //临时变量
        int pivot = arr[(left + right) / 2];    //中轴的值
        while (l < r) {             //如果l游标小于r游标的值,则一直循环进行交换流程
            while (arr[r] > pivot) {    //如果中轴右边的元素小于等于pivot,则准备进入交换
                r--;                    //否则r游标前移
            }
            while (arr[l] < pivot) {    //如果中轴左边的元素大于等于pivot,则准备进入交换
                l++;                    //否则l游标后移
            }
            if (l >= r) {               //如果l游标值大于等于r游标值
                break;                  //退出循环
            }
            temp = arr[l];              //交换流程
            arr[l] = arr[r];
            arr[r] = temp;

            //防止由于出现l<r,但是内while循环无法移动游标,而导致外while出现死循环
            if (arr[r] == pivot) {      //如果r游标所在的元素值和pivot相等
                l++;                    //则执行l游标后移
            }
            if (arr[l] == pivot) {      //如果l游标所在的元素值和pivot相等
                r--;                    //则执行r游标前移
            }
        }
        //如果出现左右游标相等的情况,则执行错开操作,
        if (l == r) {
            l++;
            r--;
        }
        //左递归
        if (left < r) {     //如果r游标大于left值,则进入递归流程继续排序
            quickSort(arr, left, r);
        }
        //右递归
        if (right > l) {    //如果l游标小于right值,则进入递归流程继续排序
            quickSort(arr, l, right);
        }

    }

    /**
     * 归并排序
     */
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
        while (r <= right) {
            temp[index] = arr[r];
            index++;
            r++;
        }
        while (l <= mid) {
            temp[index] = arr[l];
            l++;
            index++;
        }
        index = 0;
        int leftIndex = left;
        while (leftIndex <= right) {
            arr[leftIndex] = temp[index];
            leftIndex++;
            index++;
        }

    }

}

