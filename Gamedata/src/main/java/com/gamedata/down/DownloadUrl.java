package com.gamedata.down;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadUrl {

	public static ConcurrentLinkedQueue<String> getUrls(String path, String tmpDataPath, String game, String mysqlTable, String where) {
		ConcurrentLinkedQueue<String> taskCountQueue = new ConcurrentLinkedQueue<String>();

		// 第一次读取，传入的路径是原始url文件的路径
		List<String> allUrls = readUrl(path);

		// 这个路径是写成功了的路径
		String fileName = null;
		if (where.equals("1=1") || where.equals(" 1=1 ")) {
			fileName = mysqlTable + ".txt";
		} else {
			Pattern pattern = Pattern.compile(" .*(.*)>='(.*)' and (.*)<='(.*)'.* ");
			Matcher matcher = pattern.matcher(where);
			String startDate = null;
			String endDate = null;
			while (matcher.find()) {
				startDate = matcher.group(2);
				endDate = matcher.group(4);
			}
			fileName = mysqlTable + "_" + startDate + "_" + endDate + ".txt";
		}
		String writeUrlPath = tmpDataPath + "/" + game + "/" + mysqlTable + "/done_url/" + fileName;
		List<String> writeUrls = readUrl(writeUrlPath);
		
		if(writeUrls!=null && writeUrls.size()>0){
			for(String url : allUrls){
				if(!writeUrls.contains(url)){
					taskCountQueue.offer(url);
				}
			}
		}else{
			for(String url : allUrls){
				taskCountQueue.offer(url);
			}
		}
		
		
		return taskCountQueue;
	}

	/*
	 * 读取指定文件的url
	 */
	public static List<String> readUrl(String path) {
		List<String> taskCountQueue = new ArrayList<String>();
		File file = new File(path);
		BufferedReader reader = null;
		if(!file.exists()){
			return null;
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				taskCountQueue.add(tempString);
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
