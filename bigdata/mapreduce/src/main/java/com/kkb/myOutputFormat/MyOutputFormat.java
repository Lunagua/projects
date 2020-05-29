package com.kkb.myOutputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyOutputFormat extends FileOutputFormat<Text, NullWritable> {
    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        Configuration configuration = job.getConfiguration();
        FileSystem fileSystem = FileSystem.get(configuration);
        String path = configuration.get("mapred.output.dir");
        String goodPath = path + "\\good.txt";
        String badPath = path + "\\bad.txt";
        FSDataOutputStream goodFos = fileSystem.create(new Path(goodPath));
        FSDataOutputStream badFos = fileSystem.create(new Path(badPath));

        return new MyRecordWriter(goodFos, badFos);
    }

    static class MyRecordWriter extends RecordWriter<Text, NullWritable> {
        private FSDataOutputStream goodFos;
        private FSDataOutputStream badFos;


        public MyRecordWriter(FSDataOutputStream goodFos, FSDataOutputStream badFos) {
            this.goodFos = goodFos;
            this.badFos = badFos;
        }

        @Override
        public void write(Text key, NullWritable value) throws IOException, InterruptedException {
            String content = key.toString();
            byte[] bytes = (content + "\t\n").getBytes();
            String[] split = content.split("\t");
            if (split[9].equals("0")) {
                goodFos.write(bytes);
            } else {
                badFos.write(bytes);
            }
        }

        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            if (goodFos != null) {
                goodFos.close();
            }
            if (badFos != null) {
                badFos.close();
            }
        }
    }
}
