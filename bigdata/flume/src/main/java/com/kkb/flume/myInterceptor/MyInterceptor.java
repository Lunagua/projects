package com.kkb.flume.myInterceptor;


import com.google.common.base.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MyInterceptor implements Interceptor {
    private final String encrypted_field_index;     //需要加密字段下标
    private final String out_index;     //需要排除字段下标

    /**
     * 构造方法,接受配置文件参数
     */
    public MyInterceptor(String encryted_field_index, String out_index) {
        this.encrypted_field_index = encryted_field_index;
        this.out_index = out_index;
    }


    /**
     * 单个event拦截
     */
    @Override
    public Event intercept(Event event) {
        if (event == null) {
            return null;
        }
        try {
            String line = new String(event.getBody(), Charsets.UTF_8);
            String[] fields = line.split(",");
            String newLine = "";
            for (int i = 0; i < fields.length; i++) {
                if (i == Integer.parseInt(encrypted_field_index)) {
                    newLine += md5(fields[i]) + ",";
                } else if (i != Integer.parseInt(out_index)) {
                    newLine += fields[i] + ",";
                }
            }

            newLine = newLine.substring(0, newLine.length() - 1);
            event.setBody(newLine.getBytes(Charsets.UTF_8));
            return event;
        } catch (Exception e) {
            return event;
        }
    }

    /**
     * 批量拦截event逻辑
     */
    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> out = new ArrayList<>();
        for (Event event : events) {
            Event outEvent = intercept(event);
            if (outEvent != null) {
                out.add(outEvent);
            }
        }
        return out;
    }

    /**
     * 定义md5加密方法
     */
    public static String md5(String plainText) {
        byte[] secretBytes = null;      //定义字节数组用于加密使用
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");    //生成MD5加密计算摘要
            md.update(plainText.getBytes());        //对字符串加密
            secretBytes = md.digest();      //获得加密后的数据
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);   //16进制数字
        for (int i = 0; i < 32 - md5code.length(); i++) {       //对未满32位的数字前面补0
            md5code = "0" + md5code;
        }
        return md5code;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void close() {

    }

    /**
     * 相当于自定义Interceptor的工厂类
     * 在flume采集配置文件中通过指定该Builder来创建Interceptor对象
     * 可以在Builder中获取、解析flume采集配置文件中的拦截器Interceptor的自定义参数：
     * 指定需要加密的字段下标 指定需要剔除的对应列的下标等
     */
    public static class MyBuilder implements Interceptor.Builder {
        private String encrypted_fields_index;  //需要加密字段下班
        private String out_index;       //需要排除字段下标


        @Override
        public MyInterceptor build() {
            return new MyInterceptor(encrypted_fields_index, out_index);
        }

        @Override
        public void configure(Context context) {
            this.encrypted_fields_index = context.getString("encrypted_field_index");
            this.encrypted_fields_index = context.getString("out_index");
        }
    }
}
