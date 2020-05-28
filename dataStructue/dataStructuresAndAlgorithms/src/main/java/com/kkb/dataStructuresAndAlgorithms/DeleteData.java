package com.kkb.dataStructuresAndAlgorithms;

import java.util.ArrayList;

public class DeleteData {
    public void deleteData(ArrayList<Object> list, int index) {
        int len = list.size();
        if (index >= len) {
            throw new IndexOutOfBoundsException("下标不存在");
        }
        for (int i = index; i < len - 1; i++) {
            list.set(i, list.get(i + 1));
        }
        list.remove(len - 1);
    }

    public static void main(String[] args) {
        ArrayList<Object> a = new ArrayList<Object>();
        for (int i = 0; i < 5; i++) {
            a.add(1);
            a.add(2);
            a.add(3);
            a.add(4);
            System.out.println(a);
            new DeleteData().deleteData(a, i);
            System.out.println(a);
            a.clear();
            System.out.println(a);
        }
    }
}
