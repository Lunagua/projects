package com.kkb.myinputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MergeSmallFile extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = super.getConf();
        //获取job对象
        Job job = Job.getInstance(configuration, "mergeSmallFile");
        //设置jar类
        job.setJarByClass(MergeSmallFile.class);
        //设置InputFormat
        job.setInputFormatClass(MyInputFormat.class);
//        MyInputFormat.addInputPath(job, new Path("file:///C:\\Users\\Thinkpad\\Documents\\2.第二课\\6、MapReduce直播(1)(3)\\1、数据&代码资料\\1、wordCount_input\\test2"));
        MyInputFormat.addInputPath(job,new Path(args[0]));
        //设置Mapper
        job.setMapperClass(MyMapper.class);
        //设置Mapper的keyvalue
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);
        //设置reduce的key/value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);
        //设置outputFormat的文件格式为SequenceFile
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
//        SequenceFileOutputFormat.setOutputPath(job, new Path("hdfs://node01:8020/nangua/sequence/file_" + System.currentTimeMillis()));
        SequenceFileOutputFormat.setOutputPath(job,new Path(args[1]));
        //job等待执行结束
        boolean n = job.waitForCompletion(true);
        return n ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        int run = ToolRunner.run(configuration,new MergeSmallFile(),args);
        System.exit(run);
    }
}
