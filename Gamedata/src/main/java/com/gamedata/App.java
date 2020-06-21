package com.gamedata;

import java.util.HashSet;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
    	
    	Set<String> set = new HashSet<>();
    	set.add("log_diamond_gain_201705"); 
    	set.add("log_diamond_gain_201704");
    	set.add("log_diamond_gain_201706");
    	
    	System.out.println(set.contains("log_diamond_gain"));
    }
}
