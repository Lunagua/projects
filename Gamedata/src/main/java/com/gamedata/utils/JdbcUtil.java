package com.gamedata.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.gamedata.test.TestUtil;

public class JdbcUtil {

	private static Logger logger = Logger.getLogger(JdbcUtil.class); 
	
	public static String select(String poll, Set<String> fixTables, String fields, String where, String game, String tmpDataPath) {
		
		String[] split = poll.split("\t");
		String plat = split[0].trim();
		String area_id = split[1].trim();
		String ip = split[2].trim();
		String port = split[3].trim();
		String username = split[4].trim();
		String password = split[5].trim();
		String database = split[6].trim();

		String url = "jdbc:mysql://" + ip + ":" + port + "/" + database
				+ "?connectTimeout=20000&socketTimeout=20000&autoReconnect=true&failOverReadOnly=false&maxReconnects=6";
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			
			conn = DriverManager.getConnection(url, username, password); 
			stmt = conn.createStatement();
			
			String fileName = plat + "_" + area_id + "_" + ip + "_" + port + "_" + database;
			
			File file = new File(tmpDataPath + "data/" + fileName);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (String table : fixTables) {
				String sql = "select " + fields + " , " + area_id + " , " + plat + " from " + table + " where " + where;
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String data = rs.getString(1);  
					bw.write(data);
					bw.newLine();
				}
				bw.flush();
			}
			bw.close();
			fw.close();
			
			return poll;
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				} 
				if(conn != null) {
					conn.close();  
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
