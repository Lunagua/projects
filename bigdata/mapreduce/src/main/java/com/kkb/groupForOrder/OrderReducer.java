package com.kkb.groupForOrder;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class OrderReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {
    @Override
    protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        int num = 0;
        for (NullWritable n : values) {
            context.write(key, n);
            num++;
            if(num == 1){
                break;
            }
        }

    }
}
