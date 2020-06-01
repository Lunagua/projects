package com.kkb.myTests.myPatitioner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;


public class MyPartitionerDemo extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = super.getConf();

        //开启map输出进行压缩的功能
        configuration.set("mapreduce.map.output.compress", "true");
        //设置map输出的压缩算法是：BZip2Codec，它是hadoop默认支持的压缩算法，且支持切分
        configuration.set("mapreduce.map.output.compress.codec", "org.apache.hadoop.io.compress.BZip2Codec");
        //开启job输出压缩功能
        configuration.set("mapreduce.output.fileoutputformat.compress", "true");
        //指定job输出使用的压缩算法
        configuration.set("mapreduce.output.fileoutputformat.compress.codec", "org.apache.hadoop.io.compress.BZip2Codec");

        Job job = Job.getInstance(configuration, "自定义分区");
        job.setJarByClass(MyPartitionerDemo.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(MyPartMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setPartitionerClass(MyPartitiner.class);

        job.setCombinerClass(MyPartReducer.class);
        job.setReducerClass(MyPartReducer.class);
        job.setNumReduceTasks(4);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1] + "/Partitioner_" + System.currentTimeMillis()));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new MyPartitionerDemo(), args);
        System.exit(run);
    }
}

class MyPartMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private IntWritable intWritable;
    private Text text;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        text = new Text();
        intWritable = new IntWritable(1);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim().replaceAll("[\\.\"\',;!\\?]+", " ");
        String[] words = line.split("\\s+");
        for (String word : words) {
            text.set(word);
            context.write(text, intWritable);
        }
    }
}

class MyPartitiner extends Partitioner<Text, IntWritable> {
    @Override
    public int getPartition(Text text, IntWritable intWritable, int numPartitions) {
        if (text.toString().matches("([a-f]|[A-F]).*")) {
            return 0;
        } else if (text.toString().matches("([g-l]|[G-L]).*")) {
            return 1;
        } else if (text.toString().matches("([m-r]|[M-R]).*")) {
            return 2;
        } else {
            return 3;
        }
    }
}

class MyPartReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private IntWritable intWritable;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {

        intWritable = new IntWritable();
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for (IntWritable value : values) {
            count += value.get();
        }
        intWritable.set(count);
        context.write(key, intWritable);
    }
}