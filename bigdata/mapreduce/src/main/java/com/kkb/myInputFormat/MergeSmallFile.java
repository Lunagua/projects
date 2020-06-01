package com.kkb.myInputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.File;

public class MergeSmallFile extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        //创建JOB对象
        Job job = Job.getInstance(super.getConf(), "mergeSmallFile");
        job.setJarByClass(MergeSmallFile.class);

        //设置InputFormat
        job.setInputFormatClass(MyInputFormat.class);
        MyInputFormat.addInputPath(job, new Path(args[0]));
        //设置Mapper
        job.setMapperClass(MyMapper.class);
        //设置mapout key \ value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);

        //设置reduce outkey \ value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);
        //设置输出文件格式
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1] + File.separator + "mergeFile_" + System.currentTimeMillis()));
        //job等待执行结束
        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        int run = ToolRunner.run(configuration, new MergeSmallFile(), args);
        System.exit(run);
    }
}
