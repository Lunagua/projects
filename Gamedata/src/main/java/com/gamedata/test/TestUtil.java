package com.gamedata.test;

import java.util.Set;

import org.apache.log4j.Logger;

import com.gamedata.utils.TableNameUtil;

public class TestUtil {

	private static Logger logger = Logger.getLogger(TestUtil.class);  
	
	public static void main(String[] args) {
		Set<String> fixTables = TableNameUtil.fixTables("log", "n_yyyym", " date(time)>='2017-01-01' and date<='2017-01-31' ");
//		System.out.println(fixTables);
		
		logger.info(fixTables); 
	}

}
