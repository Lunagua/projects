package com.gamedata.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamedata.utils.DateUtil;

public class TestDate {

	public static void main(String[] args) {

		String mysqlTable = "global_recharge";
		String rule = "n"; // name_2015_1 n_yyyym, name_201501 n_yyyymm, name n
		String where = " date(time)>='2017-05-01' and date(time)<='2017-05-31' ";

		// 根据正则获取到日期，然后计算日期之间的月份，并获取加一个月和减一个月的月份
		Pattern pattern = Pattern.compile(" .*(.*)>='(.*)' and (.*)<='(.*)'.* ");
		Matcher matcher = pattern.matcher(where);
		String startDate = null;
		String endDate = null;
		while (matcher.find()) {
			startDate = matcher.group(2);
			endDate = matcher.group(4);
		}
		
		List<String> monthBetween = DateUtil.getMonthBetween(DateUtil.monthLess1(startDate),DateUtil.monthAdd1(endDate));
		for (String string : monthBetween) {
			System.out.println(string);
		}

		Set<String> tables = new HashSet<String>();

		if (rule.equals("n")) {
			tables.add(mysqlTable);
		} else if (rule.equals("n_yyyymm")) {
			for (String string : monthBetween) {
				String table = mysqlTable + "_" + string.replace("-", "");
				tables.add(table);
			}
		} else if (rule.equals("n_yyyym")) {
			for (String string : monthBetween) {
				String[] split = string.split("-");
				String month = split[0]+"_"+Integer.parseInt(split[1]);
				String table = mysqlTable + "_" + month;
				tables.add(table);
			}
		}

		for (String table : tables) {
			System.out.println(table);
		}
	}

}
