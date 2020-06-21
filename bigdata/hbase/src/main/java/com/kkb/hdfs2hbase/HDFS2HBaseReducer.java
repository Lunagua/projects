package com.kkb.hdfs2hbase;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class HDFS2HBaseReducer extends TableReducer<Text, NullWritable, ImmutableBytesWritable> {
    private ImmutableBytesWritable rowKeyOut;
    private Put put;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        rowKeyOut = new ImmutableBytesWritable();
    }

    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        String[] datas = key.toString().split("\t");
        byte[] rowKey_bites = datas[0].getBytes();
        String name = datas[1];
        String age = datas[2];
        rowKeyOut.set(rowKey_bites);
        put = new Put(rowKey_bites);
        put.addColumn("f1".getBytes(), "name".getBytes(), name.getBytes());
        put.addColumn("f1".getBytes(), "age".getBytes(), age.getBytes());
        context.write(rowKeyOut, put);
    }
}
