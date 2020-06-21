package com.gamedata.test;

import java.util.ArrayList;
import java.util.List;

public class Demo {

	public static void main(String[] args) {
		List<String> list1 = new ArrayList<String>();  
        List<String> list2 = new ArrayList<String>();  
        int num = 5;  
        // int num=5000;  
        for (int i = 0; i < num; i++) {  
            list1.add("test_" + i);  
            list2.add("test_" + i * 2);  
        } 
        
        System.out.println(list1);
        System.out.println(list2);
	}

}
