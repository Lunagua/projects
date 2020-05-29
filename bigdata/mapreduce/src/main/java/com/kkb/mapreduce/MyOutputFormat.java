package com.kkb.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyOutputFormat extends FileOutputFormat<Text, IntWritable> {
    @Override
    public RecordWriter<Text, IntWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        Configuration configuration = job.getConfiguration();
        FileSystem fileSystem = FileSystem.get(configuration);
        String path = configuration.get("mapred.output.dir");

        FSDataOutputStream fos1 = fileSystem.create(new Path(path + "\\good"));
        FSDataOutputStream fos2 = fileSystem.create(new Path(path + "\\bad"));
        return new MyRecordWriter(fos1, fos2);
    }

    static class MyRecordWriter extends RecordWriter<Text, IntWritable> {
        FSDataOutputStream good;
        FSDataOutputStream bad;

        public MyRecordWriter(FSDataOutputStream good, FSDataOutputStream bad) {
            this.good = good;
            this.bad = bad;
        }

        @Override
        public void write(Text key, IntWritable value) throws IOException, InterruptedException {
            byte[] bytes = (key.toString() + "\t" + value + "\t\n").getBytes();
            if (key.toString().matches("^[a-o].*")){
                good.write(bytes);
            } else {
                bad.write(bytes);
            }


        }

        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            if (good != null) {
                good.close();
            }
            if (bad != null) {
                bad.close();
            }
        }
    }
}
