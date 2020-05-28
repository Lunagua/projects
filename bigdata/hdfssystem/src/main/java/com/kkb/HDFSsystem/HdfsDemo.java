package com.kkb.HDFSsystem;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 7实验内容和要求
 * 首先，编程实现以下指定功能，并利用Hadoop提供的Shell命令完成相同任务：
 * （1）向HDFS中上传任意文本文件，如果指定的文件在HDFS中已经存在，由用户指定是追加到原有文件末尾还是覆盖原有的文件；
 * （2）从HDFS中下载指定文件，如果本地文件与要下载的文件名称相同，则自动对下载的文件重命名；
 * （3）将HDFS中指定文件的内容输出到终端中；
 * （4）显示HDFS中指定的文件的读写权限、大小、创建时间、路径等信息；
 * （5）给定HDFS中某一个目录，输出该目录下的所有文件的读写权限、大小、创建时间、路径等信息，如果该文件是目录，则递归输出该目录下所有文件相关信息；
 * （6）提供一个HDFS内的文件的路径，对该文件进行创建和删除操作。如果文件所在目录不存在，则自动创建目录；
 * （7）提供一个HDFS的目录的路径，对该目录进行创建和删除操作。创建目录时，如果目录文件所在目录不存在则自动创建相应目录；删除目录时，由用户指定当该目录不为空时是否还删除该目录；
 * （8）向HDFS中指定的文件追加内容，由用户指定内容追加到原有文件的开头或结尾；
 * （9）删除HDFS中指定的文件；
 * （10）删除HDFS中指定的目录，由用户指定目录中如果存在文件时是否删除目录；
 * （11）在HDFS中，将文件从源路径移动到目的路径。
 * 其次，编程实现一个类“MyFSDataInputStream”，该类继承“org.apache.hadoop.fs.FSDataInputStream”，要求如下：
 * （1）实现按行读取HDFS中指定文件的方法“readLine()”，如果读到文件末尾，则返回空，否则返回文件一行的文本。
 * （2）实现缓存功能，即利用“MyFSDataInputStream”读取若干字节数据时，首先查找缓存，如果缓存中有所需数据，则直接由缓存提供，否则向HDFS读取数据。
 * 查看Java帮助手册或其它资料，用“java.net.URL”和“org.apache.hadoop.fs.FsURLStreamHandlerFactory”编程完成输出HDFS中指定文件的文本到终端中。
 *
 *
 * 在完成以下作业之前，请认真阅读“大数据课程学生服务站”的学习指南栏目中的相关内容，具体请参见《大数据技术原理与应用 第三章 Hadoop分布式文件系统 学习指南》，
 * 访问地址： http://dblab.xmu.edu.cn/blog/290-2/
 * （1）编写一个Java程序，打开一个HDFS中的文件，并读取其中的数据，输出到标准输出；
 * （2）编写一个Java程序，新建一个HDFS文件，并向其中写入你的名字；
 * （3）编写一个Java程序，判断HDFS上是否存在某个文件？
 *       要求：在实验报告中，给出实验过程的一些必要截图，并附上源代码
 */
public class HdfsDemo {
    private Configuration configuration;
    private FileSystem fileSystem;
    private FileInputStream fis;
    private FSDataOutputStream fos;

    /**
     * 关闭资源
     *
     * @throws IOException
     */
    public void close() throws IOException {
        IOUtils.closeQuietly(fis);
        IOUtils.closeQuietly(fos);
        fileSystem.close();
    }

    //构造方法.获取指定URI的文件系统
    public HdfsDemo(String uri) {
        configuration = new Configuration();
        try {
            fileSystem = FileSystem.get(new URI(uri), configuration);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向HDFS中上传任意文本文件，如果指定的文件在HDFS中已经存在，
     * 由用户指定是追加到原有文件末尾还是覆盖原有的文件；
     *
     * @param srcFilePath 待上传的本地文件
     * @param dstFilePath 上传到HDFS的目的文件
     * @param isOverride  是否覆盖已有文件
     */
    public void putFileToHdfs(String srcFilePath, String dstFilePath, boolean isOverride) throws IOException {
        fis = new FileInputStream(srcFilePath);

        if (!fileSystem.exists(new Path(dstFilePath))) {    //文件不存在,直接上传文件
            fileSystem.copyFromLocalFile(new Path("file:///" + srcFilePath), new Path(dstFilePath));
            System.out.println("newfile");
            return;
        }

        if (isOverride) {   //覆盖上传文件
            fileSystem.copyFromLocalFile(false, isOverride, new Path("file:///" + srcFilePath), new Path(dstFilePath));
            System.out.println("override");
        } else {    //在文件尾部追加内容
            fos = fileSystem.append(new Path(dstFilePath));
            IOUtils.copy(fis, fos);
            System.out.println("append");
        }

        System.out.println("done");
    }

    /**
     * 从HDFS中下载指定文件，如果本地文件与要下载的文件名称相同，则自动对下载的文件重命名；
     *
     * @param srcFilePath   HDFS文件系统中的文件
     * @param dstFilePath   本地路径
     */
    public void downloadFromHdfs(String srcFilePath, String dstFilePath) throws IOException {
        String path[] = dstFilePath.split("\\.");
        int length = dstFilePath.length();
        int num = 1;
        while (new File(dstFilePath).exists()) {
            dstFilePath = path[0] + num + "." + path[1];
            num++;
        }
        System.out.println(dstFilePath);
        fileSystem.copyToLocalFile(new Path(srcFilePath), new Path("file:///" + dstFilePath));
        fileSystem.close();
    }


}

class MainTest {
    public static void main(String[] args) throws IOException {
        HdfsDemo hdfsDemo = new HdfsDemo("hdfs://node01:8020");
//        hdfsDemo.putFileToHdfs("C:\\Users\\Thinkpad\\Documents\\smallfile\\file1.txt", "/nangua/file1.txt", true);
        hdfsDemo.downloadFromHdfs("/nangua/file1.txt","C:\\Users\\Thinkpad\\Documents\\smallfile\\file.txt");

    }
}
