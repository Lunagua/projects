package com.kkb.deduplication;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DedupliReduce extends Reducer<Text, NullWritable, Text, NullWritable> {
    private Map<String, String> map;
    private Text textKey;


    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        map = new HashMap<>();
        textKey = new Text();

    }

    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        map.clear();
        for (NullWritable value : values) {
            map.put(key.toString(), value.toString());
        }
        for (String mapKey : map.keySet()) {
            textKey.set(mapKey);
            context.write(textKey, NullWritable.get());
        }
    }

}
