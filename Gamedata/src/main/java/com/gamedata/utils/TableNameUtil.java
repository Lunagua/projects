package com.gamedata.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据传入的 表名和规则确定需要查询的表
 * @author hutao
 *
 */
public class TableNameUtil {
public static Set<String> fixTables(String mysqlTable, String rule, String where) {
		
		Set<String> tables = new HashSet<String>();
		
		if(where.equals("1=1") || where.equals(" 1=1 ")){
			tables.add(mysqlTable);
			return tables;
		}
		
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
				String month = split[0] + "_" + Integer.parseInt(split[1]);
				String table = mysqlTable + "_" + month;
				tables.add(table);
			}
		}

		return tables;
	}
}
