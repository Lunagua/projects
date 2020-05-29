package com.kkb.myInputFormat;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class MyMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {
    private Text text;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        this.text = new Text();

    }

    @Override
    protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        String name = fileSplit.getPath().getName();
        text.set(name);
        context.write(text,value);
    }
}
