package com.kkb.groupForOrder;

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

public class GroupMain extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = super.getConf();
        Job job = Job.getInstance(configuration, "GroupForOrder");
        //第一步,通过InputFormat的RecordReader读取文件内容,并以KV对的方式传递给Mapper
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));
        job.setJarByClass(GroupMain.class);
        //第二步,Mapper将第一步传递过来的KV对处理过后,用KV传递出去
        job.setMapperClass(OrderGroupMapper.class);
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        //第三步,分区,分区按照reduce数量来定,默认为1,可以手动设置分区数量和reduce数量
        job.setPartitionerClass(OrderPartitoner.class);
        //第四步,排序.已经通过OrderBean做了
        //第五步,规约,略
        //第六步,分组将数据按照一定的规则分组,以便reduce进行进一步处理
        job.setGroupingComparatorClass(OrderGroup.class);

        //第七步,reduce,合并计算结果,并将最终结果以KV对输出
        job.setReducerClass(OrderReducer.class);
        job.setNumReduceTasks(3);
        job.setOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(Text.class);

        //第八步,outputFormat通过RecordWriter将reduce传递来的结果记入文件系统
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean b = job.waitForCompletion(true);

        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new GroupMain(), args);
        System.exit(run);
    }
}
