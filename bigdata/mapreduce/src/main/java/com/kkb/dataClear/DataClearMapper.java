package com.kkb.dataClear;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class DataClearMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private Counter counter;
    private NullWritable nullValue = NullWritable.get();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        counter = context.getCounter("MR_COUNT", "Break_Data_Num");
    }

    public Counter getCounter() {
        return counter;
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] res = value.toString().split("\t");
        if (res.length == 6) {
            context.write(value, nullValue);
        } else {
            counter.increment(1L);
        }
    }
}
