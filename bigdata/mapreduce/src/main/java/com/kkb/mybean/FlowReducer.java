package com.kkb.mybean;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReducer extends Reducer<Text, FlowBean, Text, Text> {
    private int upPackNum;
    private int downPackNum;
    private int upPayLoad;
    private int downPayLoad;

    private Text valueOut;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        downPayLoad = 0;
        upPayLoad = 0;
        downPackNum = 0;
        upPackNum = 0;

        valueOut = new Text();
    }

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {

        for (FlowBean value : values) {
            upPackNum += value.getUpPackNum();
            downPackNum += value.getDownPackNum();
            upPayLoad += value.getUpPayLoad();
            downPayLoad += value.getDownPayLoad();
        }
        String res = "上行包数: " + upPackNum + "\t下行包数: " + downPackNum + "\t上行总流量: " + upPayLoad + "\t下行总流量: " + downPayLoad;
        valueOut.set(res);
        context.write(key, valueOut);
    }
}
