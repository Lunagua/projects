package com.kkb.myTests.NCDCDataSort;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;

/**
 * 抽样、范围分区 *
 * - 全排序，防止数据倾斜 *
 * - 数据：气象站气象数据，来源美国国家气候数据中心（NCDC） *
 * - 气候数据record的格式如下
 * 0029227070999991902082606004+62167+030650FM-12+010299999V0201401N002119999999N0000001N9+01061+99999100711ADDGF108991999999999999999999
 * - 三种实现思路 *
 *   - 方案一：设置一个分区，在一个reduce中对结果进行排序；失去了MR框架并行计算的优势
 *   - 方案二：自定义分区，人为指定各温度区间的记录，落入哪个分区；但由于对整个数据集的气温分布不了解，可能某些分区的数据量大，其它的分区小
 *   - 方案三：
 *     - 通过对键空间采样
 *     - 只查看一小部分键，获得键的近似分布
 *     - 进而据此创建分区，尽可能的均匀的划分数据集；
 *     - Hadoop内置了采样器；InputSampler
 *
 * - 一、先将数据按气温对天气数据集排序。结果存储为sequencefile文件，气温作为输出键，数据行作为输出值 *
 * - 代码 *
 *   > 此代码处理原始日志文件 *   >
 *   > 结果用SequenceFile格式存储； *   >
 *   > 温度作为SequenceFile的key；记录作为value
 */
public class NCDCDataSortDemo extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        return 0;
    }

    public static void main(String[] args) {

    }
}
