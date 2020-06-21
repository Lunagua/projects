package com.kkb.bulkLoad;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;

public class BulkLoadMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
    private ImmutableBytesWritable rowKey;
    private Put put;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] datas = value.toString().split("\\s+");
        System.out.println(Arrays.toString(datas));
        byte[] rowKey_bites = datas[0].getBytes();
        String name = datas[1];
        String age = datas[2];
        put = new Put(rowKey_bites);
        put.addColumn("f1".getBytes(), "name".getBytes(), name.getBytes());
        put.addColumn("f1".getBytes(), "age".getBytes(), age.getBytes());
        rowKey = new ImmutableBytesWritable(rowKey_bites);
        context.write(rowKey, put);
    }
}
