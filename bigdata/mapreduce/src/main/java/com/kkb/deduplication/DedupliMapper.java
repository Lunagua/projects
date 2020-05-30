package com.kkb.deduplication;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class DedupliMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private Text textValue;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        textValue = new Text();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
       String res =  value.toString().trim();
       textValue.set(res);
        context.write(textValue, NullWritable.get());
    }
}
