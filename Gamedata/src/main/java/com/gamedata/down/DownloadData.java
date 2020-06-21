package com.gamedata.down;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.gamedata.utils.DateUtil;
import com.gamedata.utils.GetUrlUtil;
import com.gamedata.utils.TableNameUtil;

public class DownloadData {

	private static Logger logger = Logger.getLogger(DownloadData.class); 
	
	public static void main(String[] args) {
		
		// 线程个数
		int threadCount = Integer.parseInt(args[0]); 
//		int threadCount = 3; 
		
		// MySQL 连接超时时间设置
//		int timeOut = 600000;
		int timeOut = Integer.parseInt(args[1]);
		
		// 使用的游戏
//		String game = "hqg";
		String game = args[2];
		
		// mysql的地址文件路径
//		String filePath = "D:\\work\\svn\\hadoop-project\\data_import\\conf\\hqg-server-list.csv";
		String filePath = args[3];
		
		// 要下载的mysql表名
//		String mysqlTable = "log_diamond_consume"; 
		String mysqlTable = args[4];
		
		// 表名后缀规则，name_2015_1 n_yyyym, name_201501 n_yyyymm, name n
//		String rule = "n_yyyymm"; 
		String rule = args[5];
		
		// 要下载的字段
//		String fields = "platform_id,server_id,account,user_id,role_id,role_name,role_level,vip_level,time,item_rarity,item_id,item_num,reason,remark";
		String fields1 = args[6];
		String fields = fields1.replaceAll("\\\\", "");
		
		// where 条件 注意前后要加空格哦
//		String where =  " date(time)>='2017-06-01' and date(time)<='2017-06-01' "; 
		String where = args[7];
		
		// 下载的数据的存放目录，这里建议写明游戏名称
//		String tmpDataPath = "D:/test"; 
		String tmpDataPath = args[8];
		
		// 获取最终需要查询数据的表
		logger.info("filePath: " + filePath);
		logger.info("mysqlTable: " + mysqlTable);
		logger.info("rule: " + rule);
		logger.info("fields: " + fields);
		logger.info("where: " + where);
		logger.info("game: " + game);
		logger.info("tmpDataPath: " + tmpDataPath);
		
		Set<String> fixTables = TableNameUtil.fixTables(mysqlTable, rule, where);
		logger.info("要拉取的表为：" + fixTables);
		
		ConcurrentLinkedQueue<String> urls = DownloadUrl.getUrls(filePath, tmpDataPath, game, mysqlTable, where);
		logger.info("获取到文件的下载地址有: " + urls.size() + " 个");
		
		logger.info("开始拉数据...............请骚等.............");
		long downloadStart = System.currentTimeMillis();
		CountDownLatch latch = new CountDownLatch(threadCount);
		
		ExecutorService pool = Executors.newFixedThreadPool(threadCount);
		DownloadThread thread = new DownloadThread();
		thread.setTaskCountQueue(timeOut, game, urls, latch, mysqlTable, fields, where, tmpDataPath, fixTables);
		
		for (int i = 0; i < threadCount; i++) {
			pool.submit(thread);
		}
		try {
			latch.await(); // 使得主线程(main)阻塞直到latch.countDown()为零才继续执行
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 获取到写成功的url
		ConcurrentLinkedQueue<String> writeUrl = thread.getWriteUrl();
//		DownloadWriteUrlFile.writeOutUrl(writeUrl, tmpDataPath, game, mysqlTable, where);
		
		logger.info("拉取数据时间共消耗 ： " + (System.currentTimeMillis() - downloadStart) / 1000 + " 秒");
		pool.shutdown();
		
		int connSuccessedUrl = thread.getSuccessUrl().size();
		int writeSuccessedUrl = writeUrl.size();
		int writeFiledUrl = thread.getWriteFailUrl().size();
		int connFieledUrl = thread.getFailUrl().size();
		
		logger.info("连接数据成功的地址有 ： " + connSuccessedUrl);
		logger.info("写数据成功的地址有 ： " + writeSuccessedUrl);
		logger.info("写数据失败的地址有 ： " + writeFiledUrl);
		logger.info("连接数据失败的地址有 ： " + connFieledUrl);
		
		if(connFieledUrl!=0 || writeFiledUrl!=0){
			logger.info(" 连接失败的地址有 ： ");
			for (String string : thread.getFailUrl()) {
				logger.info(string);
			}
			logger.info(" -------拉取失败------- ");
			System.exit(-1);
		}
		
//		logger.info(" -------拉取成功------- ");
	}

}
