package com.kkb.myTests.mergeSmallFile;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.File;
import java.io.IOException;

public class MergeFileDemo extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = super.getConf();
        Job job = Job.getInstance(configuration, "combine small files to sequencefile");
        job.setJarByClass(MergeFileDemo.class);

        job.setInputFormatClass(MergeFileInputFormat.class);
        MergeFileInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(MergeFileMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1] + File.separator + "mergeFile_" + System.currentTimeMillis()));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new MergeFileDemo(), args);
        System.exit(run);
    }
}

class MergeFileMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {
    private Text text;
    private FileSplit split;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        text = new Text();
        split = null;
    }

    @Override
    protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
        split = (FileSplit) context.getInputSplit();
        text.set(split.getPath().getName());
        context.write(text, value);
    }
}

class MergeFileInputFormat extends FileInputFormat<NullWritable, BytesWritable> {
    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        MegerFileRecordReader reader = new MegerFileRecordReader();
        reader.initialize(split, context);
        return reader;
    }
}

class MegerFileRecordReader extends RecordReader<NullWritable, BytesWritable> {
    private BytesWritable bytesWritable = new BytesWritable();
    private FileSplit fileSplit;
    private boolean flag = false;
    private Configuration configuration;
    private FSDataInputStream fis = null;

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        fileSplit = (FileSplit) split;
        configuration = context.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!flag) {
            Path path = fileSplit.getPath();
            FileSystem fileSystem = path.getFileSystem(configuration);
            int length = (int) fileSplit.getLength();
            byte[] bytes = new byte[length];
            try {
                fis = fileSystem.open(path);
                IOUtils.readFully(fis, bytes, 0, length);
            } finally {
                IOUtils.closeQuietly(fis);
            }
            bytesWritable.set(bytes, 0, length);
            flag = true;
            return true;
        }
        return false;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return bytesWritable;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}