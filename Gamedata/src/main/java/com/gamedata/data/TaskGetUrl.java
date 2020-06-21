package com.gamedata.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 获取到文件的 数据库连接地址， 并缓存到队列里面
 * @author hutao
 *
 */
public class TaskGetUrl {

	public static ConcurrentLinkedQueue<String> getTaskCount(String path) {
		ConcurrentLinkedQueue<String> taskCountQueue = new ConcurrentLinkedQueue<String>();
		File file = new File(path);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				taskCountQueue.offer(tempString);
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

		return taskCountQueue;
	}
}
