package com.kkb.groupForOrder;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;


public class OrderPartitoner extends Partitioner<OrderBean, NullWritable> {

    @Override
    public int getPartition(OrderBean orderBean, NullWritable nullWritable, int numPartitions) {
        return orderBean.getOrderNo().hashCode() % numPartitions;
    }
}


