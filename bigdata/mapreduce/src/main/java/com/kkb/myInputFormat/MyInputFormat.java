package com.kkb.myInputFormat;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;
import java.nio.file.Path;

public class MyInputFormat extends FileInputFormat<NullWritable, BytesWritable> {
    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        MyRecordReader myRecordReader = new MyRecordReader();
        myRecordReader.initialize(split, context);
        return myRecordReader;
    }
    /**
     * 注意这个方法，决定我们的文件是否可以切分，如果不可切分，直接返回false
     * 到时候读取数据的时候，一次性将文件内容全部都读取出来
     */
    public boolean isSplitable(JobContext context, Path filename){
        return false;
    }
}
