package com.kkb.mapreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 自定义mapper类需要继承Mapper，有四个泛型，
 * keyin: k1   行偏移量 Long
 * valuein: v1   一行文本内容   String
 * keyout: k2   每一个单词   String
 * valueout : v2   1         int
 */
public class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    /**
     * 继承mapper之后，覆写map方法，每次读取一行数据，都会来调用一下map方法
     *
     * @param key：对应k1
     * @param value:对应v1
     * @param context    上下文对象。承上启下，承接上面步骤发过来的数据，通过context将数据发送到下面的步骤里面去
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();     //按行读取数据,将其转化为字符串
        String[] words = line.split(",");   //按照逗号","切割成字符串数组
        IntWritable valueOut = new IntWritable(1);  //初始化输出value为Intwritable类型的1
        Text keyOut = new Text();   //定义输出key类型为Text
        for (String word : words) {     //遍历数组,
            keyOut.set(word);   //输出key的内容设置为数组中的字符串元素
            context.write(keyOut,valueOut); //通过上下文将kv写到下游
        }
    }
}
