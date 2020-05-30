package com.kkb.groupForOrder;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class OrderGroupMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
    private OrderBean orderBean;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        orderBean = new OrderBean();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] values = value.toString().split("\t");
        orderBean.setOrderNo(values[0]);
        orderBean.setPrice(Double.parseDouble(values[2]));
        context.write(orderBean,NullWritable.get());
    }
}
