package com.gamedata.down;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class DownloadWriteUrlFile {
	
	private static Logger logger = Logger.getLogger(DownloadWriteUrlFile.class);

	/**
	 * 批量写入成功的下载数据的url
	 */
	public static void writeOutUrl(List<String> writeUrl, String tmpDataPath, String game, String mysqlTable, String where){
		
		String fileName = null;
		
		if(where.equals("1=1") || where.equals(" 1=1 ")){
			fileName = mysqlTable + ".txt";
		}else{
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
		
		String path = tmpDataPath + "/" + game + "/" + mysqlTable + "/done_url/" + fileName;
		logger.info("拉取数据成功的url在 ： " + path);
		
		try {
			File file = new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			for(String url : writeUrl){
				bw.write(url);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 单条写入成功的下载数据的url
	 */
	public static void writeOutUrl(String writeUrl, String tmpDataPath, String game, String mysqlTable, String where){
		
		String fileName = null;
		
		if(where.equals("1=1") || where.equals(" 1=1 ")){
			fileName = mysqlTable + ".txt";
		}else{
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
		
		String path = tmpDataPath + "/" + game + "/" + mysqlTable + "/done_url/" + fileName;
//		logger.info("拉取数据成功的url在 ： " + path);
		
		try {
			File file = new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(writeUrl);
			bw.newLine();
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
