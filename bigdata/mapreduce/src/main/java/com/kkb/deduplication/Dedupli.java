package com.kkb.deduplication;

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

/**
 * 对于两个输入文件，即文件A和文件B，请编写MapReduce程序，对两个文件进行合并，并剔除其中重复的内容，得到一个新的输出文件C。
 * 下面是输入文件和输出文件的一个样例供参考。
 * 输入文件A的样例如下：
 * 20150101     x
 * 20150102     y
 * 20150103     x
 * 20150104     y
 * 20150105     z
 * 20150106     x
 * <p>
 * 输入文件B的样例如下：
 * 20150101      y
 * 20150102      y
 * 20150103      x
 * 20150104      z
 * 20150105      y
 * <p>
 * 思路:
 * 1.读取文件
 * 2.map阶段,LongWriteable,Text,Text,Text
 * 3.分区/排序/规约/分组 略
 * 4.reduce阶段,Text,Texts[],Text,Text
 */
public class Dedupli extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(super.getConf(), "合并去重");
        job.setJarByClass(Dedupli.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(DedupliMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setReducerClass(DedupliReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new Dedupli(), args);
        System.exit(run);
    }
}
