package com.gamedata.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * 线程类，用来处理缓存到队列里面的数据库地址的类
 * @author hutao
 *
 */
public class ConnTestThread implements Runnable {

	/**
	 * 接收的 mysql 地址队列
	 */
	private ConcurrentLinkedQueue<String> taskCountQueue = null;
	/**
	 * CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
	 */
	private CountDownLatch latch = null;
	
	private static List<String> successUrl = new ArrayList<>(); 
	private static List<String> failUrl = new ArrayList<>(); 

	public ConcurrentLinkedQueue<String> getTaskCountQueue() {
		return taskCountQueue;
	}

	public void setTaskCountQueue(ConcurrentLinkedQueue<String> taskCountQueue, CountDownLatch latch) {
		this.taskCountQueue = taskCountQueue;
		this.latch = latch;
	}

	public void run() {
		while (true) {
			String poll = taskCountQueue.poll();
			if (poll == null) {
				break;
			}

			//打印当前线程名称以及消费的任务名称
//			System.out.println(Thread.currentThread().getName() + ":" + poll);
			
			String[] split = poll.split("\t");
			String ip = split[2].trim();
			String port = split[3].trim();
			String username = split[4].trim();
			String password = split[5].trim();
			String database = split[6].trim();

			Connection conn = null;
			Statement stmt = null;
			String myDriver = "com.mysql.jdbc.Driver";
//			String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?connectTimeout=6000&socketTimeout=6000";
			String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?connectTimeout=6000&socketTimeout=6000&autoReconnect=true&failOverReadOnly=false&maxReconnects=3";

			try {
				Class.forName(myDriver);
				conn = DriverManager.getConnection(url, username, password);
				System.out.println(Thread.currentThread().getName() + " :    " + "连接成功:   " + poll);
				successUrl.add(poll);
				stmt = conn.createStatement();
			} catch (Exception e) {
				// e.printStackTrace();
				System.err.println("连接失败:  " + poll);
				failUrl.add(poll);
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
		latch.countDown();
	}
	
	public static List<String> getSuccessUrl(){
		return successUrl;
	}
	
	public static List<String> getFailUrl(){
		return failUrl;
	}
}
