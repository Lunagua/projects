package com.kkb.dataClear;

import com.kkb.deduplication.Dedupli;
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
 * - 现有一批日志文件，日志来源于用户使用搜狗搜索引擎搜索新闻，并点击查看搜索结果过程；
 * - 但是，日志中有一些记录损坏，现需要使用MapReduce来将这些**损坏记录**（如记录中少字段、多字段）从日志文件中删除，此过程就是传说中的**数据清洗**。
 * - 并且在清洗时，要**统计**损坏的记录数。
 *
 * 思路:
 * 1.读取数据,TextInputFormat,
 * 2.Mapper读取数据后,按清洗规则直接筛选清洗垃圾数据,LongWritable,Text,Text,NullWritable
 * 3.Reducer接收数据后直接写入文件
 */
public class DataClearMain extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(super.getConf(), "数据清洗====================");
        job.setJarByClass(Dedupli.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(DataClearMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1]+ "\\dataClear_"+System.currentTimeMillis()));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new DataClearMain(), args);

        System.exit(run);
    }
}
