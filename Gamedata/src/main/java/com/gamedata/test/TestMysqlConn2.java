package com.gamedata.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMysqlConn2 {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		File file = new File(
				"D:\\work\\svn\\hadoop-project\\hqg\\work\\table_import\\mysql_import_dev\\conf\\hqg-server-list.csv");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				list.add(tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		ExecutorService pool = Executors.newFixedThreadPool(2100);

		final List<String> fileDb = new ArrayList<String>();
		final List<String> successedDb = new ArrayList<String>();

		for (final String dbInfo : list) {
			pool.execute(new Runnable() {
				public void run() {

					String[] split = dbInfo.split("\t");
					String ip = split[2].trim();
					String port = split[3].trim();
					String username = split[4].trim();
					String password = split[5].trim();
					String database = split[6].trim();

					Connection conn = null;
					Statement stmt = null;
					String myDriver = "com.mysql.jdbc.Driver";
					String url = "jdbc:mysql://" + ip + ":" + port + "/" + database
							+ "?connectTimeout=6000&socketTimeout=6000";

					try {
						Class.forName(myDriver);
						conn = DriverManager.getConnection(url, username, password);
						System.out.println("连接成功:   " + dbInfo);
						successedDb.add(dbInfo);
						stmt = conn.createStatement();
					} catch (Exception e) {
						// e.printStackTrace();
						System.err.println("连接失败:  " + dbInfo);
						fileDb.add(dbInfo);
					} finally {
						if (conn != null) {
							try {
								conn.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		}

		if (fileDb != null && fileDb.size() > 0) {
			System.out.println("失败连接：" + fileDb.size() + " 条数据库。。。。 ");
			for (String ins : fileDb) {
				System.out.println(ins + " ：连接失败");
			}
			System.exit(-1);
		}
	}
}
