package com.gamedata.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Java 调用 shell 命令的 工具类
 * 
 * @author hutao
 *
 */
public class ExecShell {
	
	/**
	 * 调shell命令
	 */
	public static List<String> excuteShell(String shell) {

		Process process = null;
		List<String> processList = new ArrayList<String>();
		try {
			process = Runtime.getRuntime().exec(shell);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return processList;
	}

	/**
	 * 调shell 脚本，并要传参数
	 * @param shellString
	 */
	public static void callShell(String[] shellString) {
		InputStream in = null;
		try {
			Process pro = Runtime.getRuntime().exec(shellString);
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

		// String shell = "mysql -A -h120.132.75.110 -P3307 -uwebadmin
		// -p3c3XeO8Mubq0hmGR game_log_9_351_new -N -e \""
		// + "select
		// id,time,device_id,platform_id,platform_account,player_id,account,server_id,role_id,role_name,recharge_from,money,"
		// +
		// "diamond,item_id,item_num,order_no,cp_order_no,vip_level_pre,role_level_pre,ip
		// from global_recharge limit 10\" > tt";
		//
		// System.out.println("命令为： " + shell);
		//
		// List<String> excuteShell = excuteShell(shell);
		// System.out.println("得到的结果为： ");
		// for (String line : excuteShell) {
		// System.out.println(line);
		// }

//		String[] cmd = new String[4];
//		cmd[0] = "sh";
//		cmd[1] = "/home/bigdata/test/ss.sh";
//		cmd[2] = "select id,time,device_id,platform_id,platform_account,player_id,account,server_id,role_id,role_name,recharge_from,money,diamond,item_id,item_num,order_no,cp_order_no,vip_level_pre,role_level_pre,ip, 12, 11 from global_recharge limit 10";
//		cmd[3] = "/home/bigdata/test/result.txt";
		
		
		String[] cmd = new String[12];
		cmd[0] = "mysql";
		cmd[1] = "-A";
		cmd[2] = "-h120.132.75.110";
		cmd[3] = "-P3307";
		cmd[4] = "-uwebadmin";
		cmd[5] = "-p3c3XeO8Mubq0hmGR";
		cmd[6] = "game_log_9_351_new";
		cmd[7] = "-N";
		cmd[8] = "-e";
		cmd[9] = "\"select id,time,device_id,platform_id,platform_account,player_id,account,server_id,role_id,role_name,recharge_from,money,diamond,item_id,item_num,order_no,cp_order_no,vip_level_pre,role_level_pre,ip from global_recharge limit 10\"";
		cmd[10] = ">";
		cmd[11] = "tt";
		
		callShell(cmd);
	}
}
