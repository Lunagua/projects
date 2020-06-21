package com.gamedata.down;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.gamedata.test.TestUtil;
import com.gamedata.utils.ExecShell;
import com.gamedata.utils.JdbcUtil;

/**
 * 线程类，用来处理缓存到队列里面的数据库地址的类
 * 
 * @author hutao
 *
 */
public class DownloadThread implements Runnable {

	private static Logger logger = Logger.getLogger(DownloadThread.class);

	/**
	 * 接收的 mysql 地址队列
	 */
	private ConcurrentLinkedQueue<String> taskCountQueue = null;
	/**
	 * CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
	 */
	private CountDownLatch latch = null;

	private static ConcurrentLinkedQueue<String> successUrl = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<String> writeUrl = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<String> writeFailUrl = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<String> failUrl = new ConcurrentLinkedQueue<>();

	private int timeOut = 6000;
	private String game = null;
	private String mysqlTable = null;
	private String fields = null;
	private String where = null;
	private String tmpDataPath = null;
	private Set<String> fixTables = null;

	public ConcurrentLinkedQueue<String> getTaskCountQueue() {
		return taskCountQueue;
	}

	public void setTaskCountQueue(int timeOut, String game, ConcurrentLinkedQueue<String> taskCountQueue,
			CountDownLatch latch, String mysqlTable, String fields, String where, String tmpDataPath,
			Set<String> fixTables) {
		this.timeOut = timeOut;
		this.game = game;
		this.taskCountQueue = taskCountQueue;
		this.latch = latch;
		this.mysqlTable = mysqlTable;
		this.fields = fields;
		this.where = where;
		this.tmpDataPath = tmpDataPath;
		this.fixTables = fixTables;
	}

	public void run() {
		while (true) {
			String poll = taskCountQueue.poll();
			if (poll == null) {
				break;
			}

			String[] split = poll.split("\t");
			String plat = split[0].trim();
			String area_id = split[1].trim();
			String ip = split[2].trim();
			String port = split[3].trim();
			String username = split[4].trim();
			String password = split[5].trim();
			String database = split[6].trim();

//			String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?connectTimeout=" + timeOut
//					+ "&socketTimeout=" + timeOut
//					+ "&autoReconnect=true&failOverReadOnly=false&maxReconnects=3&zeroDateTimeBehavior=convertToNull";
			String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?connectTimeout=" + timeOut
					+ "&autoReconnect=true&failOverReadOnly=false&maxReconnects=3&zeroDateTimeBehavior=convertToNull";

			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			// logger.info(Thread.currentThread().getName() + "开始连接:" + poll);
			// try {
			// Class.forName("com.mysql.jdbc.Driver");
			// conn = DriverManager.getConnection(url, username, password);
			// stmt = conn.createStatement();
			// logger.info("连接成功:" + poll);
			// } catch (Exception e) {
			// logger.error("连接失败:"+e.getMessage());
			// failUrl.offer(poll);
			// try {
			// if(stmt != null) {
			// stmt.close();
			// }
			// if(conn != null) {
			// conn.close();
			// }
			// } catch (Exception e2) {
			// e2.printStackTrace();
			// }
			// }
			// successUrl.offer(poll);

			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url, username, password);
				stmt = conn.createStatement();

				successUrl.offer(poll);

				String fileName = plat + "_" + area_id + "_" + ip + "_" + port + "_" + database + ".data";

				File file = new File(tmpDataPath + "/" + game + "/" + mysqlTable + "/data/" + fileName);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				FileWriter fw = new FileWriter(file, true);
				BufferedWriter bw = new BufferedWriter(fw);

				rs = stmt.executeQuery("show tables like '%" + mysqlTable + "%';");
				List<String> queryTables = new ArrayList<>();
				while (rs.next()) {
					String table = rs.getString(1);
					if (fixTables.contains(table)) {
						queryTables.add(table);
					}
				}

				for (String table : queryTables) {

					String sql = "select " + fields + " , " + area_id + " , " + plat + " from " + table + " where "
							+ where;
					
					rs = stmt.executeQuery(sql);
					
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					if(!fields.equals("*")){
						while (rs.next()) {
							StringBuffer sb = new StringBuffer();
							String[] cloumns = fields.split(",");
							for (String cloumn : cloumns) {
								String field = rs.getString(cloumn.trim());
								sb.append(field + "\t");
							}
							sb.append(area_id + "\t" + plat);
							String line = sb.toString();
							
							bw.write(line);
							bw.newLine();
						}
					}else{
						while(rs.next()){
							StringBuffer sb = new StringBuffer();
							sb.append(area_id + "\t" + plat + "\t");
							for(int i=1;i<=columnCount;i++){
								if(rs.getObject(i)!=null){
									String line = rs.getObject(i).toString();
									sb.append(line + "\t");
								}else{
									sb.append("NULL" + "\t");
								}
							}
							String line = sb.toString();
							bw.write(line);
							bw.newLine();
						}
					}
					bw.flush();
				}

				bw.close();
				fw.close();
				writeUrl.offer(poll);

				DownloadWriteUrlFile.writeOutUrl(poll, tmpDataPath, game, mysqlTable, where);

			} catch (SQLException e) {
				// logger.info(e.getMessage());
				logger.error("写数据失败:" + e.getMessage());
				logger.error("写数据失败:" + poll);
				writeFailUrl.offer(poll);
			} catch (IOException e) {
				logger.error("写数据失败:" + e.getMessage());
				logger.error("写数据失败:" + poll);
				writeFailUrl.offer(poll);
			} catch (ClassNotFoundException e) {
				logger.error("写数据失败:" + e.getMessage());
				logger.error("写数据失败:" + poll);
				writeFailUrl.offer(poll);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			// 当Java进程被 kill -15 的时候，回调此方法关闭连接
			Runtime.getRuntime().addShutdownHook(new ShutdownKill(conn, stmt, rs));
		}
		latch.countDown();
	}

	public static ConcurrentLinkedQueue<String> getSuccessUrl() {
		return successUrl;
	}

	public static ConcurrentLinkedQueue<String> getWriteUrl() {
		return writeUrl;
	}

	public static ConcurrentLinkedQueue<String> getWriteFailUrl() {
		return writeFailUrl;
	}

	public static ConcurrentLinkedQueue<String> getFailUrl() {
		return failUrl;
	}
}
