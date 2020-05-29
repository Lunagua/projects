package com.kkb.myInputFormat;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;


import java.io.IOException;

public class MyRecordReader extends RecordReader<NullWritable, BytesWritable> {
    private Configuration configuration;
    private boolean flag;
    private FileSplit fileSplit;
    private BytesWritable bytesWritable;

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        fileSplit = (FileSplit) inputSplit;
        configuration = new Configuration();
        bytesWritable = new BytesWritable();
        flag = false;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!flag) {
            int lenght = (int) fileSplit.getLength();
            byte bytes[] = new byte[lenght];
            Path path = fileSplit.getPath();
            FileSystem fileSystem = path.getFileSystem(configuration);
            FSDataInputStream fis =fileSystem.open(path);
            IOUtils.readFully(fis,bytes,0,lenght);
            bytesWritable.set(bytes,0,lenght);

            flag = true;
            return flag;

        }
        return false;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return bytesWritable;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
