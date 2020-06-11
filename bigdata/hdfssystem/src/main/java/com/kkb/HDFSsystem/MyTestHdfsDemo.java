package com.kkb.HDFSsystem;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * *  * 在完成以下作业之前，请认真阅读“大数据课程学生服务站”的学习指南栏目中的相关内容，具体请参见《大数据技术原理与应用 第三章 Hadoop分布式文件系统 学习指南》，
 * *  * 访问地址： http://dblab.xmu.edu.cn/blog/290-2/
 * *  * （1）编写一个Java程序，打开一个HDFS中的文件，并读取其中的数据，输出到标准输出；
 * *  * （2）编写一个Java程序，新建一个HDFS文件，并向其中写入你的名字；
 * *  * （3）编写一个Java程序，判断HDFS上是否存在某个文件？
 */
public class MyTestHdfsDemo {
    private FileSystem fileSystem;

    public MyTestHdfsDemo(String URI) throws URISyntaxException, IOException {
        this.fileSystem = FileSystem.get(new URI(URI), new Configuration());
    }

    public void printInShell(String filePath) throws IOException {
        FSDataInputStream fis = fileSystem.open(new Path(filePath));
        int length = 0;
        byte[] bytes = new byte[1024];
        while ((length = fis.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, length));
        }
        fis.close();
    }

    public void createFileForHdfs(String filePath) throws IOException {
        Path path = new Path(filePath);
        FSDataOutputStream fos = fileSystem.create(path, true);
        System.out.println("输入你的名字,换行后用,在Windows环境下，需要输入Ctrl+Z；在Linux/Unix/MAC环境下，需要输入Ctrl+D结束输入");
        InputStream fis = System.in;
        IOUtils.copy(fis,fos);
        IOUtils.closeQuietly(fis);
        IOUtils.closeQuietly(fos);
    }


}

class TestMain {
    public static void main(String[] args) throws IOException, URISyntaxException {
        MyTestHdfsDemo demo = new MyTestHdfsDemo("hdfs://node01:8020");
//        demo.printInShell("/nangua/datas/1.txt");
        demo.createFileForHdfs("/nangua/datas/name.txt");
    }
}