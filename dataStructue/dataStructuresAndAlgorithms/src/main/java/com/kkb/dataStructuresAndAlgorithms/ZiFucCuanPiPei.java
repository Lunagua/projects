package com.kkb.dataStructuresAndAlgorithms;

import java.util.ArrayList;

public class ZiFucCuanPiPei {
    public static void main(String[] args) {
        String a = "尚硅谷Java数据结构与java算法，韩顺平数据结构与算法";
        String b = "平数据结构";
        int len = b.length();
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i <= a.length() - len; i++) {
            list.add(a.substring(i, i + len));
        }
        System.out.println(list);
        System.out.println(list.indexOf(b));

    }

}

class HanNuoTa {
    public static void main(String[] args) {
        new HanNuoTa().hannuo(5, "A", "B", "C");
    }

    public void hannuo(int n, String a, String b, String c) {
        if (n == 1) {
            System.out.println(a + " -- > " + c);
        } else {
            hannuo(n - 1, a, c, b);
            System.out.println(a + " -- > " + c);
            hannuo(n - 1, b, a, c);
        }
    }
}