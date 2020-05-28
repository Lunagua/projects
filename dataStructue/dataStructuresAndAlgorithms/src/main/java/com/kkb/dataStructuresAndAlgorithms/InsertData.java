package com.kkb.dataStructuresAndAlgorithms;

import java.util.ArrayList;


public class InsertData {

    public void insertData(ArrayList<Object> list, int index, Object e) {
        int lenList = list.size();
        list.add(null);
        if ((index) > lenList) {
            throw new IndexOutOfBoundsException("插入位置超过列表长度");
        } else {
            for (int i = lenList; i > index; i--) {
                list.set(i, list.get(i - 1));
            }
            list.set(index,e);
        }
    }

    public static void main(String[] args) {
        ArrayList<Object> a = new ArrayList<Object>();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        for(int i=0;i<5;i++) {
            new InsertData().insertData(a, i, 6);
            System.out.println(a);
            a.remove(i);
        }
    }
}
