package com.kkb.findGrand;

import com.kkb.deduplication.Dedupli;
import com.kkb.deduplication.DedupliMapper;
import com.kkb.deduplication.DedupliReduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.ArrayList;

/**
 * 下面给出一个child-parent的表格，要求挖掘其中的父子辈关系，给出祖孙辈关系的表格。
 * 思路:
 * 1.InputFormat按行读取数据,LongWritable,Text
 * 2.map将child 和 parent 做完KV对输出,Text,Text
 * 3.reduce,寻找存在于child的parent,然后将其child和parent组合成一对,输出Text,NullWritable
 */
public class FindGrand extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(super.getConf(), "FindGrand");
        job.setJarByClass(Dedupli.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(FindGrandMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(FindGrandReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new FindGrand(), args);
        System.exit(run);
    }
}
