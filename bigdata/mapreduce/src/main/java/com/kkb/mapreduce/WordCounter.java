package com.kkb.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 这个类作为mr程序的入口类，这里面写main方法
 */
public class WordCounter extends Configured implements Tool {
    /**
     * 实现Tool接口之后，需要实现一个run方法，
     * 这个run方法用于组装我们的程序的逻辑，其实就是组装八个步骤
     *
     * @param args
     * @return
     * @throws Exception
     */
    @Override
    public int run(String[] args) throws Exception {
        //获取Job对象，组装我们的八个步骤，每一个步骤都是一个class类
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "mrdemo01");
        //实际工作当中，程序运行完成之后一般都是打包到集群上面去运行，打成一个jar包
        //如果要打包到集群上面去运行，必须添加以下设置
        job.setJarByClass(WordCounter.class);

        //第一步:通过InputFormat读取数据,并形成KV对,传输给第二步
        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job,8194304);
        //指定待处理文件所在的路径
        CombineTextInputFormat.addInputPath(job, new Path(args[0]));

        //第二步:将kv对输入Mymapper形成新的kv对输出
        job.setMapperClass(MyMapper.class);
        //设置mapper的输出key和value的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //3\4\5\6步省略:分区\排序\combine\分组

        //第七步:将kv对传入MyReducer,进行处理.并输出结果kv对
        job.setReducerClass(MyReducer.class);
        //设置输出key和value 的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //第八步:通过OutFormat的RecordWriter将结果写入文件系统
        job.setOutputFormatClass(MyOutputFormat.class);
        MyOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }


    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.set("hello","world");
        int run = ToolRunner.run(configuration,new WordCounter(),args);
        System.exit(run);
    }
}
