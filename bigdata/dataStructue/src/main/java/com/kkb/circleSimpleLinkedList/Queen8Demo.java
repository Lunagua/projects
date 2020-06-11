package com.kkb.circleSimpleLinkedList;


/**
 * 思路:
 * 1.循环的尝试每一个位置是否合适,
 * 2.是否合适的规则;不处于同一行(通过递增排除),不处于同一列(arr[i] != arr[n]),不处于一条斜线(abs(n-i) != abs(arr[n]-arr[i])
 * 3.递归出口,n>max,出口处打印列表
 */
public class Queen8Demo {
    static int max = 8;
    static int[] arr = new int[max];
    int count = 0;

    public static void main(String[] args) {
        Queen8Demo queen8Demo = new Queen8Demo();
        queen8Demo.check(0);
        System.out.println(queen8Demo.count);
    }

    private void check(int n) {
        if (n == max) {   //递归出口
            print();
            count++;
            return;
        }
        for (int i = 0; i < max; i++) {
            arr[n] = i;
            if (judge(n)) {
                check(n + 1);
            }
        }
    }

    /**
     * 判断当前皇后的位置是否满足不互杀
     *
     * @param n 当前皇后的行数
     */
    private boolean judge(int n) {
        for (int i = 0; i < n; i++) {
            if (arr[i] == arr[n] || Math.abs(n - i) == Math.abs(arr[n] - arr[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 输出最终排列出来的列表
     */
    private void print() {
        for (int i = 0; i < max; i++) {
            System.out.print(arr[i] + "\t");
        }
        System.out.println();
    }


}
