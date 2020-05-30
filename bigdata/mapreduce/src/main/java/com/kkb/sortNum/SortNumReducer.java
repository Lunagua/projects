package com.kkb.sortNum;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SortNumReducer extends Reducer<IntWritable, NullWritable, Text, NullWritable> {
    private Integer lineNum;
    private Text text;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        lineNum = new Integer(0);
        text = new Text();
    }

    @Override
    protected void reduce(IntWritable key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        lineNum++;
        String res = lineNum + "\t" + key.get();
        text.set(res);
        context.write(text, NullWritable.get());
    }
}
