package com.kkb.mybean;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
    private FlowBean flowBean;
    private Text text;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        flowBean = new FlowBean();
        text = new Text();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1363157985066	13726230503	00-FD-07-A4-72-B8:CMCC	120.196.100.82	i02.c.aliimg.com	游戏娱乐	24	27	2481	24681	200
        //6-9下标的数据对应flowbean的序列化数据
        String line = value.toString();
        String info[] = line.split("\t");
        String phoneNo = info[1];
        String upPackNum = info[6];
        String downPackNum = info[7];
        String upPayLoad = info[8];
        String downPayLoad = info[9];

        flowBean.setDownPayLoad(Integer.parseInt(downPayLoad));
        flowBean.setUpPackNum(Integer.parseInt(upPackNum));
        flowBean.setDownPackNum(Integer.parseInt(downPackNum));
        flowBean.setUpPayLoad(Integer.parseInt(upPayLoad));
        text.set(phoneNo);
        context.write(text, flowBean);
    }
}
