package com.kkb.myTests.secondSort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;

/**
 * - 如一个简单的关于员工工资的记录；每条记录如下，有3个字段，分别表示name、age、salary
 * <p>
 * nancy	22	8000 *
 * - 使用MR处理记录，实现结果中 *
 * - 按照工资从高到低的降序排序
 * - 若工资相同，则按年龄升序排序
 * 思路:
 * 1.因为需要二次排序,需要自定义key的类,进行排序规则设定.需要实现WritableComparer接口,
 * 2.InputFormat使用TextInputFormat按行读取数据
 * 3.Mapper<Longwritable,Text,自定义的key类,Text>
 * 4.reduce<自定义key,Text,Text,NullWritable>
 * 5.OutputFormat使用TextOutputFormat
 */
public class SecondSortDemo extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = super.getConf();
        Job job = Job.getInstance(configuration, "自定义排序");
        job.setJarByClass(SecondSortDemo.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(SecondSortMapper.class);
        job.setMapOutputKeyClass(SecondSortKey.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setReducerClass(SecondSortReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1] + File.separator + "SecondSort_" + System.currentTimeMillis()));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new SecondSortDemo(), args);
        System.exit(run);
    }
}

class SecondSortReducer extends Reducer<SecondSortKey, NullWritable, Text, NullWritable> {
    private Text text;
    private NullWritable nullValue;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        text = new Text();
        nullValue = NullWritable.get();
    }

    @Override
    protected void reduce(SecondSortKey key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        String out = key.getName() + "\t" + key.getAge() + "\t" + key.getSalary();
        for (NullWritable value : values) {
            text.set(out);
            context.write(text, nullValue);
        }
    }
}

class SecondSortMapper extends Mapper<LongWritable, Text, SecondSortKey, NullWritable> {
    private SecondSortKey sortKey;
    private NullWritable nullValue;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        sortKey = new SecondSortKey();
        nullValue = NullWritable.get();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] res = value.toString().split("\\s+");
        sortKey.setName(res[0]);
        sortKey.setAge(Integer.parseInt(res[1]));
        sortKey.setSalary(Double.parseDouble(res[2]));

        context.write(sortKey, nullValue);
    }
}

class SecondSortKey implements WritableComparable<SecondSortKey> {
    private String name;
    private Integer age;
    private Double salary;


    @Override
    public int compareTo(SecondSortKey o) {
        int compareResult = this.salary.compareTo(o.salary);
        if (compareResult == 0) {
            return this.age.compareTo(o.age);
        }
        return -compareResult;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
        out.writeDouble(salary);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.name = in.readUTF();
        this.age = in.readInt();
        this.salary = in.readDouble();
    }

    public SecondSortKey() {

    }

    public SecondSortKey(String name, Integer age, Double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "SecondSortKey{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
