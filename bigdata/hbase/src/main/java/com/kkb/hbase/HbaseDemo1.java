package com.kkb.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HbaseDemo1 {
    private Connection connection;
    private Admin admin;
    private Table table;

    /**
     * 构造函数,初始化连接
     */
    public HbaseDemo1() throws IOException {
        Configuration configuration = HBaseConfiguration.create();    //创建configuration对象
        configuration.set("hbase.zookeeper.quorum", "node01:2181,node02:2181,node03:2181"); //对象设置连接对象
        connection = ConnectionFactory.createConnection(configuration); //创建连接对象
    }

    /**
     * 关闭资源
     */
    public void close() throws IOException {
        if (connection != null) {
            connection.close();
        }
        if (admin != null) {
            admin.close();
        }
        if (table != null) {
            table.close();
        }
    }

    /**
     * 打印扫描结果
     */
    public void showData(Result result) {
        if (result == null) {
            System.out.println("查询结果为空!");
            return;
        }
        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            byte[] row = CellUtil.cloneRow(cell);
            byte[] family = CellUtil.cloneFamily(cell);
            byte[] qualifier = CellUtil.cloneQualifier(cell);
            byte[] value = CellUtil.cloneValue(cell);
            System.out.printf("\t%s\t\t%s\t\t%s\t\t%s\t\n",
                    Bytes.toString(row),
                    Bytes.toString(family),
                    Bytes.toString(qualifier),
                    Bytes.toString(value)
            );
        }
    }

    /**
     * 创建表
     */
    public void createTable(String tbname, String familyName1, String familyName2) throws IOException {
        admin = connection.getAdmin();
        TableName tableName = TableName.valueOf(tbname);
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
        HColumnDescriptor f1 = new HColumnDescriptor(familyName1);
        HColumnDescriptor f2 = new HColumnDescriptor(familyName2);
        f1.setVersions(1, 3);
        f2.setVersions(1, 3);
        hTableDescriptor.addFamily(f1);
        hTableDescriptor.addFamily(f2);
        admin.createTable(hTableDescriptor);
    }

    /**
     * 插入数据
     */
    public void putData(String tbname, String rowKey, String familyName, String qualifier, String value) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Put put = new Put(rowKey.getBytes());
        put.addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes());
        table.put(put);
    }


    /**
     * 指定行键查询
     */
    public void getData(String tbname, String rowKey) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Get get = new Get(rowKey.getBytes());
        Result result = table.get(get);
        showData(result);
    }

    /**
     * 指定行键和列族
     */
    public void getData(String tbname, String rowKey, String familyName) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Get get = new Get(rowKey.getBytes());
        get.addFamily(familyName.getBytes());
        Result result = table.get(get);
        showData(result);
    }

    /**
     * 指定行键,列族,列名
     */
    public void getData(String tbname, String rowKey, String familyName, String qualifier) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Get get = new Get(rowKey.getBytes());
        get.addColumn(familyName.getBytes(), qualifier.getBytes());
        Result result = table.get(get);
        showData(result);
    }

    /**
     * 全表扫描
     */
    public void scanData(String tbname) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 扫描列族
     */
    public void scanData(String tbanem, String familyName) throws IOException {
        table = connection.getTable(TableName.valueOf(tbanem));
        Scan scan = new Scan();
        scan.addFamily(familyName.getBytes());
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 扫描列
     */
    public void scanData(String tbanem, String familyName, String qualifier) throws IOException {
        table = connection.getTable(TableName.valueOf(tbanem));
        Scan scan = new Scan();
        scan.addColumn(familyName.getBytes(), qualifier.getBytes());
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 扫描行键范围内数据,左闭右开
     */
    public void scanDataFromRowArea(String tbanem, String minRowKey, String maxRowKey) throws IOException {
        table = connection.getTable(TableName.valueOf(tbanem));
        Scan scan = new Scan();
        scan.setStartRow(minRowKey.getBytes());
        scan.setStopRow(maxRowKey.getBytes());
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 扫描行键范围内,指定列族数据,左闭右开
     */
    public void scanDataFromRowArea(String tbanem, String familyName, String minRowKey, String maxRowKey) throws IOException {
        table = connection.getTable(TableName.valueOf(tbanem));
        Scan scan = new Scan();
        scan.addFamily(familyName.getBytes());
        scan.setStartRow(minRowKey.getBytes());
        scan.setStopRow(maxRowKey.getBytes());
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 扫描行键范围内,指定列族&列的数据,左闭右开
     */
    public void scanDataFromRowArea(String tbanem, String familyName, String qualifier, String minRowKey, String maxRowKey) throws IOException {
        table = connection.getTable(TableName.valueOf(tbanem));
        Scan scan = new Scan();
        scan.addColumn(familyName.getBytes(), qualifier.getBytes());
        scan.setStartRow(minRowKey.getBytes());
        scan.setStopRow(maxRowKey.getBytes());
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 列值模糊匹配过滤
     */
    public void valueFuzzyFilter(String tbname, String value) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        SubstringComparator substringComparator = new SubstringComparator(value);
        ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL, substringComparator);
        scan.setFilter(valueFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 列值精确匹配过滤
     */
    public void valueExactFilter(String tbname, String value) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        BinaryComparator substringComparator = new BinaryComparator(value.getBytes());
        ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL, substringComparator);
        scan.setFilter(valueFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 行键过滤器
     */
    public void rowKeyFilter(String tbname, String rowKey, String compareOp) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        CompareFilter.CompareOp op;
        Scan scan = new Scan();
        BinaryComparator substringComparator = new BinaryComparator(rowKey.getBytes());
        switch (compareOp) {
            case "==":
                op = CompareFilter.CompareOp.EQUAL;
                break;
            case "!=":
            case "<>":
                op = CompareFilter.CompareOp.NOT_EQUAL;
                break;
            case ">":
                op = CompareFilter.CompareOp.GREATER;
                break;
            case ">=":
                op = CompareFilter.CompareOp.GREATER_OR_EQUAL;
                break;
            case "<":
                op = CompareFilter.CompareOp.LESS;
                break;
            case "<=":
                op = CompareFilter.CompareOp.LESS_OR_EQUAL;
                break;
            default:
                op = CompareFilter.CompareOp.NO_OP;
                break;
        }
        RowFilter rowFilter = new RowFilter(op, substringComparator);
        scan.setFilter(rowFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 指定列族,进行行键过滤
     */
    public void rowKeyFilter(String tbname, String rowKey, String familyName, String compareOp) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        CompareFilter.CompareOp op;
        Scan scan = new Scan();
        scan.addFamily(familyName.getBytes());
        BinaryComparator substringComparator = new BinaryComparator(rowKey.getBytes());
        switch (compareOp) {
            case "==":
                op = CompareFilter.CompareOp.EQUAL;
                break;
            case "!=":
            case "<>":
                op = CompareFilter.CompareOp.NOT_EQUAL;
                break;
            case ">":
                op = CompareFilter.CompareOp.GREATER;
                break;
            case ">=":
                op = CompareFilter.CompareOp.GREATER_OR_EQUAL;
                break;
            case "<":
                op = CompareFilter.CompareOp.LESS;
                break;
            case "<=":
                op = CompareFilter.CompareOp.LESS_OR_EQUAL;
                break;
            default:
                op = CompareFilter.CompareOp.NO_OP;
                break;
        }
        RowFilter rowFilter = new RowFilter(op, substringComparator);
        scan.setFilter(rowFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 指定列族,列,进行行键过滤
     */
    public void rowKeyFilter(String tbname, String rowKey, String familyName, String qualifier, String compareOp) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        CompareFilter.CompareOp op;
        Scan scan = new Scan();
        scan.addColumn(familyName.getBytes(), qualifier.getBytes());
        BinaryComparator substringComparator = new BinaryComparator(rowKey.getBytes());
        switch (compareOp) {
            case "==":
                op = CompareFilter.CompareOp.EQUAL;
                break;
            case "!=":
            case "<>":
                op = CompareFilter.CompareOp.NOT_EQUAL;
                break;
            case ">":
                op = CompareFilter.CompareOp.GREATER;
                break;
            case ">=":
                op = CompareFilter.CompareOp.GREATER_OR_EQUAL;
                break;
            case "<":
                op = CompareFilter.CompareOp.LESS;
                break;
            case "<=":
                op = CompareFilter.CompareOp.LESS_OR_EQUAL;
                break;
            default:
                op = CompareFilter.CompareOp.NO_OP;
                break;
        }
        RowFilter rowFilter = new RowFilter(op, substringComparator);
        scan.setFilter(rowFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 列族过滤
     */
    public void familyNameFilter(String tbname, String familyName) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        SubstringComparator substringComparator = new SubstringComparator(familyName);
        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL, substringComparator);
        scan.setFilter(familyFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 列名过滤
     */
    public void qualifierFilter(String tbname, String qualifier) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        SubstringComparator substringComparator = new SubstringComparator(qualifier);
        QualifierFilter qualifierFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, substringComparator);
        scan.setFilter(qualifierFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 单列值过滤器
     */
    public void singleColumnsFilter(String tbname, String familyName, String qualifier, String value) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        SubstringComparator substringComparator = new SubstringComparator(value);
        SingleColumnValueFilter singleColumnValueExcludeFilter = new SingleColumnValueFilter(
                familyName.getBytes(), qualifier.getBytes(), CompareFilter.CompareOp.EQUAL, substringComparator);
        scan.setFilter(singleColumnValueExcludeFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }

    /**
     * 行键前缀过滤
     */
    public void rowPrefixFilter(String tbname, String rowKeyPrefix) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        PrefixFilter prefixFilter = new PrefixFilter(rowKeyPrefix.getBytes());
        scan.setFilter(prefixFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }
    /**
     * 列值和行键过滤
     */
    public void filterList(String tbname,String rowKeyPrefix,String value) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Scan scan = new Scan();
        PrefixFilter prefixFilter = new PrefixFilter(rowKeyPrefix.getBytes());
        ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL,new SubstringComparator(value));
        FilterList filterList = new FilterList();
        filterList.addFilter(prefixFilter);
        filterList.addFilter(valueFilter);
        scan.setFilter(filterList);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            showData(result);
        }
    }
    /**
     * 根据行键删除数据
     */
    public void deleteData(String tbname,String rowKey) throws IOException {
        table = connection.getTable(TableName.valueOf(tbname));
        Delete delete = new Delete(rowKey.getBytes());
        table.delete(delete);
    }
    /**
     * 删除表
     */
    public void dropTable(String tbname) throws IOException {
        admin = connection.getAdmin();
        TableName name = TableName.valueOf(tbname);
        admin.disableTable(name);
        admin.deleteTable(name);
    }
}


class Test {
    public static void main(String[] args) throws IOException {
        HbaseDemo1 hbaseDemo1 = new HbaseDemo1();
        String[] names = {"zhangsan", "lisi", "wangwu", "zhaoliu", "liuqi", "morongba", "hetianxia", "tangbei", "kailiang", "huanghe"};
        try {
            //create table
//            hbaseDemo1.createTable("score", "info", "data");
//            for (int i = 0; i < 10; i++) {
//                hbaseDemo1.putData("score", "rk0000" + i, "info", "s_id", "200521050301" + i);
//                hbaseDemo1.putData("score", "rk0000" + i, "info", "name", names[i]);
//                hbaseDemo1.putData("score", "rk0000" + i, "data", "c_id", "2020100032" + i);
//                hbaseDemo1.putData("score", "rk0000" + i, "data", "score", "" + (int) (Math.random() * 60 + 40));
//            }

//            hbaseDemo1.getData("score","rk00001");
//            hbaseDemo1.getData("score","rk00001","info");
//            hbaseDemo1.getData("score","rk00001","data");
//            hbaseDemo1.getData("score","rk00001","data","score");
//            hbaseDemo1.getData("score","rk00001","info","name");

//            hbaseDemo1.scanData("score");
//            hbaseDemo1.scanData("score","data");
//            hbaseDemo1.scanData("score","info");
//            hbaseDemo1.scanData("score", "data", "score");
//            hbaseDemo1.scanData("score", "info", "name");


//            hbaseDemo1.scanDataFromRowArea("score","rk00000","rk00005");
//            hbaseDemo1.scanDataFromRowArea("score", "data", "rk00000", "rk00005");
//            hbaseDemo1.scanDataFromRowArea("score", "info", "rk00000", "rk00005");
//            hbaseDemo1.scanDataFromRowArea("score", "info","name", "rk00000", "rk00005");
//            hbaseDemo1.scanDataFromRowArea("score", "data","score", "rk00000", "rk00005");

//            hbaseDemo1.valueFuzzyFilter("score","zhang");
//            hbaseDemo1.valueExactFilter("score","lisi");

//            hbaseDemo1.rowKeyFilter("score", "rk00001", ">");
//            hbaseDemo1.rowKeyFilter("score", "rk00001", "info",">");
//            hbaseDemo1.rowKeyFilter("score", "rk00001","data","score", ">");

//            hbaseDemo1.familyNameFilter("score","data");

//            hbaseDemo1.qualifierFilter("score", "scor");

//            hbaseDemo1.singleColumnsFilter("score","info","name","zhan");

//            hbaseDemo1.rowPrefixFilter("score","rk0001");

//            hbaseDemo1.filterList("score","rk0000","zhan");

//            hbaseDemo1.putData("score","kk5555","info","name","liujia");
//            hbaseDemo1.deleteData("score","kk5555");
//            hbaseDemo1.createTable("tableFoDelete","info","data");
            hbaseDemo1.dropTable("tableFoDelete");
        } finally {
            hbaseDemo1.close();
        }


    }
}