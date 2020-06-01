package com.kkb.dataClear.searchCount;

import com.kkb.dataClear.DataClearMain;
import com.kkb.dataClear.DataClearMapper;
import com.kkb.deduplication.Dedupli;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * 使用MR编程，统计sogou日志数据中，每个用户搜索的次数；结果写入HDFS
 * 日志格式：每行记录有6个字段；分别表示时间datetime、用户ID userid、
 * 新闻搜索关键字searchkwd、当前记录在返回列表中的序号retorder、用户点击链接的顺序cliorder、点击的URL连接cliurl
 */
public class SearchCountMain extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(super.getConf(), "用户点击次数====================");
        job.setJarByClass(Dedupli.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(SearchCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setReducerClass(SearchCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1] + "\\searchCount_" + System.currentTimeMillis()));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new SearchCountMain(), args);
        System.exit(run);
    }
}

class SearchCountMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private Text text;
    private NullWritable nullValue;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        text = new Text();
        nullValue = NullWritable.get();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line[] = value.toString().split("\t");
        text.set(line[1]);
        context.write(text, nullValue);
    }
}

class SearchCountReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
    private Text text;
    private NullWritable nullValue;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        nullValue = NullWritable.get();
        text = new Text();
    }

    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        int num = 0;
        for (NullWritable value : values) {
            num++;
        }
        text.set(key.toString() + "\t" + num);
        context.write(text, nullValue);
    }
}