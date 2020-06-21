package com.gamedata.data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ConnTest {
	
	public static void main(String[] args) {
		
		int threadCount = Integer.parseInt(args[0]); // 线程个数
		String filePath = args[1];  // 要测试的文件
//		String path = "D:\\work\\svn\\hadoop-project\\ser\\work\\table_import\\mysql_import_dev\\conf\\ser-global-server-list.csv";
		
		long timeStart = System.currentTimeMillis();
		
		System.out.println("开始执行数据库地址测试.................");
		
		CountDownLatch latch = new CountDownLatch(threadCount);
		ConcurrentLinkedQueue<String> taskCounts= TaskGetUrl.getTaskCount(filePath);
		ExecutorService es = Executors.newFixedThreadPool(threadCount);
		ConnTestThread  taskThread = new ConnTestThread();
		taskThread.setTaskCountQueue(taskCounts, latch);
		for(int i = 0; i < threadCount; i++){
			es.submit(taskThread);	
		}
		try {
			latch.await(); //使得主线程(main)阻塞直到latch.countDown()为零才继续执行
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("测试时间共消耗 ： " + (System.currentTimeMillis() - timeStart)/1000 + " 秒");
		es.shutdown();
		
		System.out.println("连接成功的地址有 ： " + taskThread.getSuccessUrl().size());
		System.out.println("连接失败的地址有 ： " + taskThread.getFailUrl().size());
		
		/**
		 * 如果所有地址连接都是OK的话就继续拉取数，不OK的话就退出程序
		 */
		if(taskThread.getFailUrl().size()!=0){
			System.out.println("连接失败的地址有 ： ");
			for (String string : taskThread.getFailUrl()) {
				System.out.println(string);
				System.out.println();
			}
			System.exit(-1);
		}
	}
}
