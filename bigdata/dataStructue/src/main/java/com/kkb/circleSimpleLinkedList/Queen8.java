//package com.kkb.circleSimpleLinkedList;
//
//public class Queen8 {
//    static int maxSize = 8;
//    static int[] arr = new int[maxSize];
//
//    public static void main(String[] args) {
//        Queen8 queen8 = new Queen8();
//        queen8.check(0);
//
//    }
//
//    /**
//     * 思路:
//     * 1.定义个长度为8的一维数组,下标代表第几个皇后(第几行),元素值代表皇后的位置(第几列)
//     * 2.判断当前皇后与之前皇后的位置是否为同一列arr[i]==arr[n],是否为斜线Math.abs(n-i) == Math.abs(arr[n]-arr[i])
//     * 3.递归出口,n>8.因为n是递增的,达到出口时,输出arr
//     */
//
//    private void check(int n) {
//        if (n == maxSize) {
//            print();
//            return;
//        }
//        for (int i = 0; i < maxSize; i++) {
//            arr[n] = i;
//            if (judeg(n)) {
//                check(n + 1);
//            }
//        }
//    }
//
//    private boolean judeg(int n) {
//        for (int i = 0; i < n; i++) {
//            if (arr[i] == arr[n] || Math.abs(n - i) == Math.abs(arr[n] - arr[i])) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void print() {
//        for (int i = 0; i < maxSize; i++) {
//            System.out.print(arr[i] + "\t");
//        }
//        System.out.println();
//    }
//}
