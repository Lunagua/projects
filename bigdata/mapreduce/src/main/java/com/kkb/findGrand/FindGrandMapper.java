package com.kkb.findGrand;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class FindGrandMapper extends Mapper<LongWritable, Text, Text, Text> {
    private Text textKey;
    private Text textValue;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        textKey = new Text();
        textValue = new Text();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        String name = fileSplit.getPath().getName();
        String line[] = value.toString().split("\t");

        if (name.equals("1.txt")) {
            textKey.set(line[1]);
            textValue.set("C\t" + value.toString());
        } else {
            textKey.set(line[0]);
            textValue = value;
        }
        context.write(textKey, textValue);
    }
}
