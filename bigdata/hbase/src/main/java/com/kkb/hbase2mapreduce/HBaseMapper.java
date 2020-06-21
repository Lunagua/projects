package com.kkb.hbase2mapreduce;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.List;

public class HBaseMapper extends TableMapper<Text, Put> {
    private Put put;
    private Text text;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {

        text = new Text();
    }

    //需求：==读取HBase当中myuser这张表的f1:name、f1:age数据，将数据写入到另外一张myuser2表的f1列族里面去==
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        byte[] rowKey_bites = key.get();
        text.set(rowKey_bites);
        put = new Put(rowKey_bites);
        List<Cell> cells = value.listCells();
        for (Cell cell : cells) {
            if ("f1".equals(Bytes.toString(CellUtil.cloneFamily(cell)))) {
                if ("name".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
                    put.add(cell);
                }
                if ("age".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
                    put.add(cell);
                }

            }
        }
        if(!put.isEmpty()){
            context.write(text,put);
        }

    }
}
