package com.kkb.myTests.groupForTop2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 现有一个淘宝用户订单历史记录文件；每条记录有6个字段，分别表示 *
 * - userid、datetime、title商品标题、unitPrice商品单价、purchaseNum购买量、productId商品ID
 * 现使用MR编程，求出每个用户、每个月消费金额最多的两笔订单，花了多少钱 *
 * - 所以得相同用户、同一个年月的数据，分到同一组
 * <p>
 * 思路:
 * 1.TextInputFormat读取数据
 * 2.自定义JavaBean,包含数据的6个字段信息,实现CompareTo方法,排序规则:
 * - 先比较userid是否相等；若不相等，则userid升序排序
 * - 若相等，比较两个Bean的日期是否相等；若不相等，则日期升序排序
 * - 若相等，再比较总开销，降序排序
 * 3.自定义分区,userID相同的分配到一个分区.
 * 4.自定义Mapper,
 * - 输出key是当前记录对应的Bean对象
 * - 输出的value对应当前下单的总开销
 * 5.自定义分组类
 * - 决定userid相同、日期（年月）相同的记录，分到同一组中，被reduce()处理
 * 6.自定义Reduce类
 * - reduce()中求出当前一组数据中，开销头两笔的信息
 */

public class GroupForTop2Demo extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        //判断以下，输入参数是否是两个，分别表示输入路径、输出路径
        if (args.length != 2 || args == null) {
            System.out.println("please input Path!");
            System.exit(0);
        }
        Configuration configuration = super.getConf();
        Job job = Job.getInstance(configuration, "自定义分组");
        job.setJarByClass(GroupForTop2Demo.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(GroupForTop2Mapper.class);
        job.setMapOutputKeyClass(GroupForTop2OrderInfo.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        job.setPartitionerClass(GroupForTop2Partitioner.class);
        job.setGroupingComparatorClass(GroupForTop2Grouper.class);

        job.setReducerClass(GroupForTop2Reducer.class);
        job.setNumReduceTasks(1);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1] + File.separator + "Top2_" + new Date().getTime()));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new GroupForTop2Demo(), args);
        System.exit(run);
    }
}

class GroupForTop2Reducer extends Reducer<GroupForTop2OrderInfo, DoubleWritable, Text, NullWritable> {
    private GroupForTop2OrderInfo orderInfo;
    private Text text;
    private NullWritable nullV = NullWritable.get();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        orderInfo = new GroupForTop2OrderInfo();
        text = new Text();

    }

    @Override
    protected void reduce(GroupForTop2OrderInfo key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        int num = 0;
        for (DoubleWritable value : values) {
            num++;
            text.set(key.toString());
            context.write(text, nullV);
            if (num == 2) {
                break;
            }
        }
    }
}

class GroupForTop2Grouper extends WritableComparator {
    /**
     * 自定义分组类
     * * - 决定userid相同、日期（年月）相同的记录，分到同一组中，被reduce()处理
     */
    public GroupForTop2Grouper() {
        super(GroupForTop2OrderInfo.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        GroupForTop2OrderInfo infoA = (GroupForTop2OrderInfo) a;
        GroupForTop2OrderInfo infoB = (GroupForTop2OrderInfo) b;
        String aId = infoA.getUserID();
        String aDate = infoA.getDatetime().substring(0, 7);
        String bId = infoB.getUserID();
        String bDate = infoB.getDatetime().substring(0, 7);
        return aId.compareTo(bId) + aDate.compareTo(bDate);
    }
}

class GroupForTop2Mapper extends Mapper<LongWritable, Text, GroupForTop2OrderInfo, DoubleWritable> {
    private GroupForTop2OrderInfo orderInfo;
    private DoubleWritable doubleWritable;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        orderInfo = new GroupForTop2OrderInfo();
        doubleWritable = new DoubleWritable();
    }

    /**
     * - userid、datetime、title商品标题、unitPrice商品单价、purchaseNum购买量、productId商品ID
     * * - 输出key是当前记录对应的Bean对象
     * * - 输出的value对应当前下单的总开销
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] line = value.toString().split("\t");
        orderInfo.setUserID(line[0]);
        orderInfo.setDatetime(line[1]);
        orderInfo.setTitle(line[2]);
//        try {
        orderInfo.setUnitPrice(Double.parseDouble(line[3]));
        orderInfo.setPurchaseNum(Integer.parseInt(line[4]));
//        } catch (Exception e) {
//            orderInfo.setUnitPrice(1.0);
//            orderInfo.setPurchaseNum(1);
//        }
        orderInfo.setProductID(line[5]);
        doubleWritable.set(orderInfo.getUnitPrice() * orderInfo.getPurchaseNum());
        if (line[1].length() == 23) {
            context.write(orderInfo, doubleWritable);
        }
    }
}

class GroupForTop2Partitioner extends Partitioner<GroupForTop2OrderInfo, DoubleWritable> {

    @Override
    public int getPartition(GroupForTop2OrderInfo orderInfo, DoubleWritable doubleWritable, int numPartitions) {
        return (orderInfo.getUserID().hashCode() & Integer.MAX_VALUE) % numPartitions;
    }
}

class GroupForTop2OrderInfo implements WritableComparable<GroupForTop2OrderInfo> {
    /*- userid、datetime、title商品标题、unitPrice商品单价、purchaseNum购买量、productId商品ID
     *
     */
    private String userID;
    private String datetime;
    private String title;
    private Double unitPrice;
    private Integer purchaseNum;
    private String productID;


    /**
     * /*
     * *- 先比较userid是否相等；若不相等，则userid升序排序
     * * - 若相等，比较两个Bean的日期是否相等；若不相等，则日期升序排序 2014-12-01 02:20:42.000
     * * - 若相等，再比较总开销，降序排序
     */
    @Override
    public int compareTo(GroupForTop2OrderInfo o) {
        int compareId = this.userID.compareTo(o.userID);
        int compareDate = 1;

        compareDate = this.datetime.substring(0, 7).compareTo(o.datetime.substring(0, 7));

        if (compareId == 0) { //比较userID,相等则继续
            if (compareDate == 0) {  //比较date,相等则继续
                return -(int) (this.unitPrice * this.purchaseNum - o.purchaseNum * o.unitPrice);   //日期相等则按照
            } else {
                return compareDate;
            }
        }
        return compareId;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(userID);
        out.writeUTF(datetime);
        out.writeUTF(title);
        out.writeDouble(unitPrice);
        out.writeInt(purchaseNum);
        out.writeUTF(productID);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.userID = in.readUTF();
        this.datetime = in.readUTF();
        this.title = in.readUTF();
        this.unitPrice = in.readDouble();
        this.purchaseNum = in.readInt();
        this.productID = in.readUTF();
    }

    public GroupForTop2OrderInfo() {
    }

    public GroupForTop2OrderInfo(String userID, String datetime, String title, Double unitPrice, Integer purchaseNum, String productID) {
        super();
        this.userID = userID;
        this.datetime = datetime;
        this.title = title;
        this.unitPrice = unitPrice;
        this.purchaseNum = purchaseNum;
        this.productID = productID;
    }

    @Override
    public String toString() {
        return userID + "\t" + datetime + "\t" + title + "\t" + unitPrice + "\t" + purchaseNum + "\t" + productID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(Integer purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}