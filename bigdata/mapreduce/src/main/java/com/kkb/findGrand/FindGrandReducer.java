package com.kkb.findGrand;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;

public class FindGrandReducer extends Reducer<Text, Text, Text, NullWritable> {
    private Text text;
    private ArrayList<String> grandChilds;
    private ArrayList<String> grandParents;
    private int count = 0;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        text = new Text();
        grandChilds = new ArrayList<>();
        grandParents = new ArrayList<>();
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        count++;
        if (count <= 1) {
            text.set("grandChild\tgrandParent");
            context.write(text, NullWritable.get());
        }
        grandChilds.clear();
        grandParents.clear();
        for (Text value : values) {
            if (value.toString().startsWith("C")) {
                grandChilds.add(value.toString().split("\t")[1]);
            } else {
                grandParents.add(value.toString().split("\t")[1]);
            }
        }
        for (String child : grandChilds) {
            for (String grandparent : grandParents) {
                text.set(child + "\t" + grandparent);
                context.write(text, NullWritable.get());
            }
        }

    }
}
