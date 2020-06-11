package com.kkb.HDFSsystem;

import org.apache.hadoop.fs.FSDataInputStream;

import java.io.InputStream;

/**
 * * 其次，编程实现一个类“MyFSDataInputStream”，该类继承“org.apache.hadoop.fs.FSDataInputStream”，要求如下：
 *  * （1）实现按行读取HDFS中指定文件的方法“readLine()”，如果读到文件末尾，则返回空，否则返回文件一行的文本。
 *  * （2）实现缓存功能，即利用“MyFSDataInputStream”读取若干字节数据时，首先查找缓存，如果缓存中有所需数据，则直接由缓存提供，否则向HDFS读取数据。
 *  * 查看Java帮助手册或其它资料，用“java.net.URL”和“org.apache.hadoop.fs.FsURLStreamHandlerFactory”编程完成输出HDFS中指定文件的文本到终端中。
 */
public class MyFSDataInputStream  extends FSDataInputStream {
    public MyFSDataInputStream(InputStream in) {
        super(in);
    }
    public String myReadLine(String filePath){
//        new FSDataInputStream().read()
        return "";
    }
}
