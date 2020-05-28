package com.kkb.myinputformat;

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
    private FileSplit fileSplit;
    private boolean flag = false;
    private BytesWritable bytesWritable;

    /**
     * 初始化的方法  只在初始化的时候调用一次.只要拿到了文件的切片，就拿到了文件的内容
     *
     * @param inputSplit         输入的文件的切片
     * @param taskAttemptContext
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        configuration = context.getConfiguration();
        fileSplit = (FileSplit) split;
        bytesWritable = new BytesWritable();

    }

    /**
     * 读取数据
     * 返回值boolean  类型，如果返回true，表示文件已经读取完成，不用再继续往下读取了
     * 如果返回false，文件没有读取完成，继续读取下一行
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!flag) {
            int lenght = (int) fileSplit.getLength();
            byte[] bytes = new byte[lenght];
            Path path = fileSplit.getPath();
            FileSystem fileSystem = path.getFileSystem(configuration);
            FSDataInputStream fis = fileSystem.open(path);
            IOUtils.readFully(fis, bytes, 0, lenght);
            bytesWritable.set(bytes, 0, lenght);
            flag = true;
            return true;
        }
        return false;
    }
    /**
     * 获取数据的key1
     * @return
     * @throws IOException
     * @throws InterruptedException
     * */
    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    /**
     * 获取数据的value
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
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
