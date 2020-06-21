package com.gamedata.test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Test2 {

	public static void main(String[] args) {
//		1_4_120.132.89.234_3307_game_log_9_10_new.data

		Connection conn = null;
		Statement stmt = null;
		String myDriver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://120.132.89.234:3307/game_log_9_10_new";
		ResultSet rs = null;
		
		try {
			Class.forName(myDriver);
			conn = DriverManager.getConnection(url, "webadmin", "3c3XeO8Mubq0hmGR");
			System.out.println("连接成功");
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("连接失败");
			return;
		}
		
		System.out.println("sssssssssssssss");
		
		String filed = "*";
		
		try {	
			ResultSet resultSet = rs = stmt.executeQuery("select "+filed+" , 35 , 1 from log_recharge where  date(time)>='2015-08-13' and date(time)<='2015-08-13'");
			
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			System.out.println(columnCount + "个字段");
			
			if(!filed.equals("*")){
				while(resultSet.next()){
					String string = resultSet.getString(1);
					System.out.println(string);
				}
			}else{
				StringBuffer sb = new StringBuffer();
				while(resultSet.next()){
					for(int i=1;i<=columnCount;i++){
						if(rs.getObject(i)!=null){
							String line = rs.getObject(i).toString();
							sb.append(line + "\t");
						}else{
							sb.append("NULL" + "\t");
						}
					}
					String line = sb.toString();
					System.out.println(line);
				}
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("连接失败");
		}
		
	}

}
