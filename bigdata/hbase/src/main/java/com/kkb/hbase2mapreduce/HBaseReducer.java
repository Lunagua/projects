package com.kkb.hbase2mapreduce;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * * @param <KEYIN>  The type of the input key.
 * * @param <VALUEIN>  The type of the input value.
 * * @param <KEYOUT>  The type of the output key.
 */
public class HBaseReducer extends TableReducer<Text, Put, ImmutableBytesWritable> {
    private ImmutableBytesWritable immutableBytesWritable;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        immutableBytesWritable = new ImmutableBytesWritable();
    }

    @Override
    protected void reduce(Text key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
        immutableBytesWritable.set(key.toString().getBytes());
        for (Put put : values) {
            context.write(immutableBytesWritable,put);
        }
    }
}
