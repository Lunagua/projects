package com.kkb.dataStructuresAndAlgorithms;


import java.util.ArrayList;
import java.util.List;

public class DataStructuresTest1 {
    /*
    Listlength
    GetElem
    LocateElem
    ListInsert
     */

    public  List<Object> union(List<Object> aList, List<Object> bList) {
        int aLen = aList.size();
        int bLen = bList.size();
        List<Object> cList = new ArrayList<Object>();
        for(int i = 0; i < bLen; i++){
            Object o = bList.get(i);
            if(!aList.contains(o)){
                cList.add(o);
            }
            cList.add(aList.get(i));
        }
        return cList;
    }

    public static void main(String[] args) {
        List<Object> a = new ArrayList<Object>(10);
        List<Object> b = new ArrayList<Object>();
        a.add("10");
        b.add("9");
        a.add("11");
        b.add("16");
        a.add("12");
        b.add("12");
        a.add("13");
        b.add("13");
        System.out.println(a);
        System.out.println(b);
        System.out.println(new DataStructuresTest1().union(a,b));
    }
}
