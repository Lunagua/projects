package com.kkb.mapreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    //第三步：分区   相同key的数据发送到同一个reduce里面去，相同key合并，value形成一个集合

    /**
     * 继承Reducer类之后，覆写reduce方法
     *
     * @param key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int num = 0;        //初始化数量统计变量
        for (IntWritable value : values) {  //遍历传入reduce的value中的数组
            num += value.get();     //累加数组中的值
        }
        context.write(key, new IntWritable(num));   //将累加结果按照kv输出
    }
}
