import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HadoopApiTest {




    @Test   //创建文件夹
    public void mkdirToHdfs() throws IOException {
        Configuration configuration = new Configuration();

        configuration.set("fs.defaultFS", "hdfs://node01:8020");

        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.mkdirs(new Path("/nangua"));
        fileSystem.close();
    }

    @Test   //创建文件
    public void touchFilrToHdfs() throws IOException {
        Configuration configuration = new Configuration();

        configuration.set("fs.defaultFS", "hdfs://node01:8020");

        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.create(new Path("/nangua/2.txt"));
        fileSystem.close();
    }

    @Test   //文件上传
    public void copyFileToHdfs() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://node01:8020");
        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.copyFromLocalFile(new Path("file:///c:\\Users\\Thinkpad\\Documents\\README.txt"), new Path("/nangua"));
        fileSystem.copyFromLocalFile(new Path("file:///c:\\Users\\Thinkpad\\Documents\\README.txt"), new Path("/nangua"));
        fileSystem.close();
    }

    @Test //文件下载
    public void copyFileToLocal() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://node01:8020");

        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.copyToLocalFile(new Path("/Scala.md"), new Path("file:///C:\\Users\\Thinkpad\\Documents\\newScala.md"));
        fileSystem.close();
    }

    @Test   //hdfs文件删除操作
    public void deleteFileForHdfs() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://node01:8020");

        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.deleteOnExit(new Path("/nangua/1.txt"));
        fileSystem.close();
    }

    @Test //hdfs文件重命名操作
    public void mvFileForHdfs() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://node01:8020");

        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.rename(new Path("/nangua/test1"), new Path("/nangua/test"));
        fileSystem.close();
    }

    @Test   //查看hdfs文件相信信息
    public void appendToHdfs() throws IOException, URISyntaxException {
        /*
         * 查看hdfs文件相信信息
         * 1获取文件系统
         * 2 获取文件详情
         *  // 输出详情 \文件名称 \长度 \分组 \获取存储的块信息 \获取块存储的主机节点
         * 3 关闭资源
         */
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), configuration);

        RemoteIterator<LocatedFileStatus> fileList = fileSystem.listFiles(new Path("/nangua"), false);
        while (fileList.hasNext()) {
            LocatedFileStatus status = fileList.next();
            System.out.println(status.getPath());
            System.out.println(status.getPath().getName());
//            System.out.println(status.getPath().getParent());
//            System.out.println(status.getGroup());
//            System.out.println(status.getLen());
//            System.out.println(status.getOwner());
//            System.out.println(status.getPermission());
//            System.out.println(status.getBlockSize());
            BlockLocation[] blockLocations = status.getBlockLocations();
            for (BlockLocation blockLocation : blockLocations) {
                String[] blocks = blockLocation.getHosts();
                for (String block : blocks) {
                    System.out.println(block);
                }
                String[] names = blockLocation.getNames();
                for (String name : names) {
                    System.out.println(name);
                }
            }
        }
        fileSystem.close();

    }


    @Test //通过io流进行数据上传操作
    public void putFileToHdfs() throws URISyntaxException, IOException {
        // 1 获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), configuration);
        // 2 创建输入流 不需要加file:///
        FileInputStream fis = new FileInputStream(new File("file:///C:\\Users\\Thinkpad\\Documents\\smallfile\\file1.txt"));
        // 3 获取输出流
        FSDataOutputStream fos = fileSystem.create(new Path("/nangua/file1.txt"));
        // 4 流对拷
        IOUtils.copy(fis, fos);
        // 5 关闭资源
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(fis);
        fileSystem.close();
    }

    @Test //通过IO流完成数据下载
    public void getFileFromHdfs() throws URISyntaxException, IOException {
        //获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), configuration);
        //创建本地输出流
        FileOutputStream fos = new FileOutputStream("file1.txt");
        //获取输入流
        FSDataInputStream fis = fileSystem.open(new Path("/nangua/file1.txt"));
        //流对拷
        IOUtils.copy(fis, fos);
        //关闭资源
        IOUtils.closeQuietly(fis);
        IOUtils.closeQuietly(fos);
        fileSystem.close();

    }

    @Test //小文件合并上传处理
    public void mergeFileToHdfs() throws URISyntaxException, IOException {
        //获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"),configuration);
        //获取输出流
        FSDataOutputStream fos = fileSystem.create(new Path("/nangua/newFile.txt"));

        //获取本地文件系统 localFileSystem
        LocalFileSystem localFileSystem = FileSystem.getLocal(configuration);
        //读取本地的文件
        FileStatus[] fileStatuses = localFileSystem.listStatus(new Path("file:///C:\\Users\\Thinkpad\\Documents\\smallfile"));
        for(FileStatus fileStatus : fileStatuses){
            //获取文件路径
            Path path = fileStatus.getPath();
            //读取文件,获取输入流
            FSDataInputStream fis  = localFileSystem.open(path);
            IOUtils.copy(fis,fos);
            IOUtils.closeQuietly(fis);
        }
        //关闭资源
        IOUtils.closeQuietly(fos);
        fileSystem.close();
        localFileSystem.close();
    }

    @Test //合并hdfs系统文件下载
    public void mergeFileToLocal() throws URISyntaxException, IOException {
        //获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"),configuration);
        //获取输出流
        FileOutputStream fos = new FileOutputStream("result.txt");
        //遍历目标路径
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/nangua"));
        for(FileStatus fileStatus : fileStatuses){
            //读取文件
            Path path = fileStatus.getPath();
            //获取输入流
            FSDataInputStream fis = fileSystem.open(path);
            //读取文件
            IOUtils.copy(fis,fos);
            IOUtils.closeQuietly(fis);
        }
        IOUtils.closeQuietly(fos);
        fileSystem.close();
    }

}
