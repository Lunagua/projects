package com.kkb.hdfs2hbase;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class HDFS2HBaseMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private NullWritable NullValue = NullWritable.get();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.write(value, NullValue);
    }
}
