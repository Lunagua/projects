package com.gamedata.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shell {
	
	public static void excute(String[] cmd){
		InputStream in = null;
		try {
			Process pro = Runtime.getRuntime().exec(cmd);
			pro.waitFor();
			in = pro.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while((line=read.readLine())!=null){
				System.out.println("INFO:" + line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		String[] cmd = { "sh", "/home/bigdata/test/ss.sh", 
//				"select id,time,device_id,platform_id,platform_account,player_id,account,server_id,role_id,role_name,recharge_from,money,diamond,item_id,item_num,order_no,cp_order_no,vip_level_pre,role_level_pre,ip, 12, 11 from global_recharge limit 10", 
//				"/home/bigdata/test/result.txt" };
		
		String[] cmd = new String[4];
		cmd[0] = "sh";
		cmd[1] = "/home/bigdata/test/ss.sh";
		cmd[2] = "select id,time,device_id,platform_id,platform_account,player_id,account,server_id,role_id,role_name,recharge_from,money,diamond,item_id,item_num,order_no,cp_order_no,vip_level_pre,role_level_pre,ip, 12, 11 from global_recharge limit 10";
		cmd[3] = "/home/bigdata/test/result.txt";
		
		excute(cmd);
	}
}
