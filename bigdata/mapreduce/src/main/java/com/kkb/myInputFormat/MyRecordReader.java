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
    private FileSplit fileSplit;
    private Configuration configuration;
    private BytesWritable bytesWritable;
    private boolean flag = false;   //读取文件的标识

    /**
     * 初始化的方法  只在初始化的时候调用一次.只要拿到了文件的切片，就拿到了文件的内容
     *
     * @param inputSplit         输入的文件的切片
     * @param taskAttemptContext 任务的上下文
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        this.fileSplit = (FileSplit) inputSplit;
        this.configuration = taskAttemptContext.getConfiguration();
        this.bytesWritable = new BytesWritable();
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
            int length = (int) fileSplit.getLength();
            byte[] bytes = new byte[length];
            //获取文件切片以后,需要将文件的内容读取出来
            Path path = fileSplit.getPath(); //获取文件切片路径
            FileSystem fileSystem = path.getFileSystem(configuration);//获取文件系统
            FSDataInputStream fis = fileSystem.open(path);  //打开文件的输入流
            //inputStream => bytes[] => bytesWritable
            IOUtils.readFully(fis, bytes, 0, length);
            bytesWritable.set(bytes, 0, length);
            flag = true;
            return true;
        }
        return false;
    }

    /**
     * 获取数据的key1
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    /**
     * 获取数据的value1
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return bytesWritable;
    }

    /**
     * 读取文件的进度，没什么用
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    /**
     * 关闭资源
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {

    }
}
