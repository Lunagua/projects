package com.kkb.HDFSsystem;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsPermission;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

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

 * 要求：在实验报告中，给出实验过程的一些必要截图，并附上源代码
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
     * @param srcFilePath HDFS文件系统中的文件
     * @param dstFilePath 本地路径
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

    /**
     * 将HDFS中指定文件的内容输出到终端中
     */
    public void printFileContentFromHdfs(String srcFilePath) throws IOException {
        FSDataInputStream fis = fileSystem.open(new Path(srcFilePath));
        int len;
        byte[] bytes = new byte[1024];
        while ((len = fis.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
        fis.close();

    }

    /**
     * 显示HDFS中指定的文件的读写权限、大小、创建时间、路径等信息；
     *
     * @param filePath HDFS文件路径
     */
    public void showFileInfoFromHdfs(String filePath) throws IOException {

        FileStatus fileStatus = fileSystem.getFileStatus(new Path(filePath));
        FsPermission filePermission = fileStatus.getPermission();
        Path path = fileStatus.getPath();
        long len = fileStatus.getLen();
        String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(fileStatus.getAccessTime());
        System.out.printf("文件路径: %s\n文件创建时间: %s\n文件大小: %s\n权限: %s", path, time, len, filePermission);
    }

    /**
     * 给定HDFS中某一个目录，输出该目录下的所有文件的读写权限、大小、创建时间、路径等信息，
     * 如果该文件是目录，则递归输出该目录下所有文件相关信息；
     */
    public void showDirInfoFromHdfs(String dirPath) throws IOException {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(dirPath));

        for (FileStatus s : fileStatuses) {
            if (s.isDirectory()) {
                showDirInfoFromHdfs(s.getPath().toString());
            } else {
                System.out.println(
                        s.getPath().getName() + "\t" +
                                s.getPermission() + "\t" +
                                s.getLen() + "\t" +
                                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(s.getAccessTime()) + "\t" +
                                s.getPath());
            }
        }
    }
    /**
     * 提供一个HDFS内的文件的路径，对该文件进行创建和删除操作。如果文件所在目录不存在，则自动创建目录；
     */
    public void createFileToHdfs(String filePath,String commend) throws IOException {
        Path path = new Path(filePath);
        Path dir = path.getParent();
        switch (commend){
            case "delete":
                fileSystem.deleteOnExit(path);
                break;
            case "create":
                if(!fileSystem.exists(dir)){
                    fileSystem.mkdirs(dir);
                    fileSystem.create(path);
                    System.out.println("新建文件夹后新建");
                }else {
                    fileSystem.create(path,true);
                    System.out.println("直接新建");
                }
                break;
            default:
                System.out.println("please input create or delete commend");
                break;
        }
    }
    /**
     * 在HDFS中，将文件从源路径移动到目的路径。
     */
    public void moveFileInHdfs(String srcFile, String dstFile) throws IOException {
        Path srcPath = new Path(srcFile);
        Path dstPath = new Path(dstFile);
        fileSystem.rename(srcPath, dstPath);

    }
}

class MainTest {
    public static void main(String[] args) throws IOException {
        HdfsDemo hdfsDemo = new HdfsDemo("hdfs://node01:8020");
//        hdfsDemo.putFileToHdfs("C:\\Users\\Thinkpad\\Documents\\smallfile\\file1.txt", "/nangua/file1.txt", true);
//        hdfsDemo.downloadFromHdfs("/nangua/file1.txt","C:\\Users\\Thinkpad\\Documents\\smallfile\\file.txt");
//        hdfsDemo.printFileContentFromHdfs("/nangua/datas/1.txt");
//        hdfsDemo.showFileInfoFromHdfs("/nangua/datas/1.txt");
//        hdfsDemo.showDirInfoFromHdfs("/nangua/datas");
//        hdfsDemo.createFileToHdfs("/nangua/datas/data2/2.txt","create");
//        hdfsDemo.createFileToHdfs("/nangua/datas/data2/3.txt","create");
//        hdfsDemo.createFileToHdfs("/nangua/datas/data2/2.txt","delete");
        hdfsDemo.moveFileInHdfs("/nangua/datas/3.txt","/nangua/datas/data2/5.txt");
    }
}
