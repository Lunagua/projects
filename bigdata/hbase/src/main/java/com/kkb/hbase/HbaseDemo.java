package com.kkb.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;


public class HbaseDemo {
    private Configuration configuration;
    private Connection connection;
    private Admin admin;
    private Table table;

    /**
     * 连接Hbase,创建connection对象,创建admin对象
     *
     * @throws IOException
     */
    public HbaseDemo() throws IOException {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "node01:2181,node02:2181,node03:2181");
        connection = ConnectionFactory.createConnection(configuration);
        admin = connection.getAdmin();
    }

    /**
     * 关闭资源
     *
     * @throws IOException
     */
    public void close() throws IOException {
        admin.close();
        connection.close();
        if (table != null) {
            table.close();
        }
    }

    /**
     * 创建表
     *
     * @param tbname      表名
     * @param familyName1 列族1
     * @param familyName2 列族2
     * @throws IOException
     */
    public void createTable(String tbname, String familyName1, String familyName2) throws IOException {
        TableName tableName = TableName.valueOf(tbname);
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
        HColumnDescriptor f1 = new HColumnDescriptor(familyName1);
        HColumnDescriptor f2 = new HColumnDescriptor(familyName2);
        f1.setMaxVersions(3);
        f2.setMaxVersions(2);
        hTableDescriptor.addFamily(f1);
        hTableDescriptor.addFamily(f2);
        admin.createTable(hTableDescriptor);
    }

    /**
     * 插入数据
     *
     * @param tbname     表名
     * @param rowKey     行键
     * @param familyName 列族
     * @param qualifier  列名
     * @param value      列值
     * @throws IOException
     */
    public void putData(String tbname, String rowKey, String familyName, String qualifier, String value) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Put put = new Put(rowKey.getBytes());
        put.addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes());
        table.put(put);
    }

    /**
     * 按照列族查询
     *
     * @param tbname     表名
     * @param rowKey     行键
     * @param familyName 列族
     * @throws IOException
     */
    public void getData(String tbname, String rowKey, String familyName) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Get get = new Get(rowKey.getBytes());
        get.addFamily(familyName.getBytes());
        Result result = table.get(get);
        showResult(result);
    }

    /**
     * 查询指定列族下的指定列
     *
     * @param tbname     表名
     * @param rowKey     行键
     * @param familyName 列族
     * @param qualifier  列名
     * @throws IOException
     */
    public void getData(String tbname, String rowKey, String familyName, String qualifier) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Get get = new Get(rowKey.getBytes());
        get.addColumn(familyName.getBytes(), qualifier.getBytes());
        Result result = table.get(get);
        showResult(result);
    }

    /**
     * 全表扫描
     *
     * @param tbname 表名
     * @return 扫描对象
     * @throws IOException
     */

    public ResultScanner scanData(String tbname) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        return results;
    }

    /**
     * 指定列族扫描
     *
     * @param tbname     表名
     * @param familyName 列族
     * @throws IOException
     */
    public ResultScanner scanData(String tbname, String familyName) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        scan.addFamily(familyName.getBytes());
        ResultScanner results = table.getScanner(scan);
        return results;
    }

    /**
     * 指定列扫描
     *
     * @param tbname     表名
     * @param familyName 列族
     * @param qualifier  列名
     * @throws IOException
     */
    public ResultScanner scanData(String tbname, String familyName, String qualifier) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        scan.addColumn(familyName.getBytes(), qualifier.getBytes());
        return table.getScanner(scan);
    }


    /**
     * 行键比较
     *
     * @param tbname    表名
     * @param rowKey    待比较行键
     * @param compareOp 比较运算符 有效符合集合(>,>=,<,<=,==,!=,<>)
     * @throws IOException
     */
    public void rowFilter(String tbname, String rowKey, String compareOp) throws IOException {
        //通过RowFilter过滤比rowKey  0003小的所有值出来
        table = connection.getTable(TableName.valueOf(tbname));
        CompareFilter.CompareOp equal;
        switch (compareOp) {
            case "==":
                equal = CompareFilter.CompareOp.EQUAL;
                break;
            case "<":
                equal = CompareFilter.CompareOp.LESS;
                break;
            case "<=":
                equal = CompareFilter.CompareOp.LESS_OR_EQUAL;
                break;
            case ">=":
                equal = CompareFilter.CompareOp.GREATER_OR_EQUAL;
                break;
            case "<>":
            case "!=":
                equal = CompareFilter.CompareOp.NOT_EQUAL;
                break;
            case ">":
                equal = CompareFilter.CompareOp.GREATER;
                break;
            default:
                equal = CompareFilter.CompareOp.NO_OP;
                System.out.println("比较运算符输入错误!");
                break;
        }
        Scan scan = new Scan();
        BinaryComparator binaryComparator = new BinaryComparator(rowKey.getBytes());
        RowFilter rowFilter = new RowFilter(equal, binaryComparator);
        scan.setFilter(rowFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showResult(result);
        }
    }

    /**
     * 列族名模糊查询
     *
     * @param tbname     表名
     * @param familyName 列族模糊字段
     * @throws IOException
     */
    public void familyFilter(String tbname, String familyName) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        SubstringComparator substringComparator = new SubstringComparator(familyName);
//        BinaryComparator binaryComparator = new BinaryComparator(familyName.getBytes());
//        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,binaryComparator);
        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL, substringComparator);
        scan.setFilter(familyFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showResult(result);
        }
    }

    /**
     * 列名模糊匹配
     *
     * @param tbname    表名
     * @param qualifier 列名
     * @throws IOException
     */
    public void qualifierFilter(String tbname, String qualifier) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        SubstringComparator substringComparator = new SubstringComparator(qualifier);
        QualifierFilter qualifierFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, substringComparator);
        scan.setFilter(qualifierFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showResult(result);
        }
    }

    /**
     * cell值模糊匹配
     *
     * @param tbname 表名
     * @param value  cell值
     * @throws IOException
     */
    public void valueFilter(String tbname, String value) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        SubstringComparator substringComparator = new SubstringComparator(value);
        ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL, substringComparator);
        scan.setFilter(valueFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showResult(result);
        }
    }

    /**
     * 单列值过滤
     *
     * @param tbname     表名
     * @param familyName 列族
     * @param qualifier  列名
     * @param value      列值
     * @throws IOException
     */
    public void singleColumnsValueFilter(String tbname, String familyName, String qualifier, String value) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        SingleColumnValueExcludeFilter singleFilter = new SingleColumnValueExcludeFilter(familyName.getBytes(), qualifier.getBytes(), CompareFilter.CompareOp.EQUAL, value.getBytes());
        scan.setFilter(singleFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showResult(result);
        }
    }
    public void rowkeyPrefixFilter(String tbname,String rowKey) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        PrefixFilter prefixFilter = new PrefixFilter(rowKey.getBytes());
        scan.setFilter(prefixFilter);
        ResultScanner results = table.getScanner(scan);
        for(Result result:results){
            showResult(result);
        }
    }

    /**
     * 打印查询结果
     *
     * @param result 查询结果对象
     */
    public void showResult(Result result) {
        if (result == null) {
            System.out.println("查询结果为空");
            return;
        }
        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            byte[] row = CellUtil.cloneRow(cell);
            byte[] family = CellUtil.cloneFamily(cell);
            byte[] qualifier = CellUtil.cloneQualifier(cell);
            byte[] bytes = CellUtil.cloneValue(cell);
            System.out.printf("\t%s\t\t%s\t\t%s\t\t%s\t\n",
                    Bytes.toString(row),
                    Bytes.toString(family),
                    Bytes.toString(qualifier),
                    Bytes.toString(bytes)
            );
        }
    }

    public static void main(String[] args) throws IOException {
        HbaseDemo hbaseDemo = new HbaseDemo();
        //建表
//        hbaseDemo.createTable("student", "info", "data");
//        String[] names = {"zhangsan", "lisi", "wangwu", "zhaoliu", "liuqi", "morongba", "hetianxia", "tangbei", "kailiang", "huanghe"};
//        //插入数据
//        for (int i = 0; i < 20; i++) {
//            hbaseDemo.putData("student", "00000" + i, "info", "name", names[i % 10]);
//            hbaseDemo.putData("student", "00000" + i, "info", "gender", Math.random() > 0.5 ? "man" : "female");
//            hbaseDemo.putData("student", "00000" + i, "info", "stu_id", "200521030" + i);
//            hbaseDemo.putData("student", "00000" + i, "data", "address", "beijing");
//            hbaseDemo.putData("student", "00000" + i, "info", "age", "" + (int) (20 + Math.random() * 10));
//        }

        //查询
//        hbaseDemo.getData("student","000001","info");
//        hbaseDemo.getData("student","000002","data");
//        hbaseDemo.getData("student","000003","info","name");
        //scan
//        hbaseDemo.scanData("student");
//        hbaseDemo.scanData("student","data");
//        hbaseDemo.scanData("student","info","name");
        //rowFilter
//        hbaseDemo.rowFilter("student", "0000012", "1");
        //familyFilter
//        hbaseDemo.familyFilter("student", "data");
        //qualifierFilter
//        hbaseDemo.qualifierFilter("student","er");
        //valueFilter
//        hbaseDemo.valueFilter("student", "tang");
        //singleFilter
//        hbaseDemo.singleColumnsValueFilter("student","info","name","zhaoliu");
        //prefixFilter
        hbaseDemo.rowkeyPrefixFilter("student","000001");
        hbaseDemo.close();
    }
}