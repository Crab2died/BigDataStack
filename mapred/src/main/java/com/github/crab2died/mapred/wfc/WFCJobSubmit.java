package com.github.crab2died.mapred.wfc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

import java.io.IOException;

// 词频统计
public class WFCJobSubmit {

    public static void main(String... args) throws IOException, ClassNotFoundException, InterruptedException {
        // 设置hadoop用户权限
        System.setProperty("HADOOP_USER_NAME", "crab2died");
        // Path类为hadoop API定义，创建两个Path对象，一个输入文件的路径，一个输入结果的路径
        Path outPath = new Path("hdfs://crab2died:9000/out");
        // 输入hadoop的文件路径
        Path inPath = new Path("/home/crab2died/file.txt");
        // 创建默认的Configuration对象
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://crab2died:9000/");
        conf.set("mapreduce.job.jar", "mapred/target/mapred-1.0-SNAPSHOT.jar");
        conf.set("mapreduce.framework.name", "yarn");
        conf.set("yarn.resourcemanager.hostname", "crab2died");
        conf.set("mapreduce.app-submission.cross-platform", "true");
        // 根据地址和conf得到hadoop的文件系统独享
        // 如果输入路径已经存在则删除
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }
        // 根据conf创建一个新的Job对象，代表要提交的作业，作业名为JSubmit.class.getSimpleName()
        Job job = Job.getInstance(conf, "Word Frequency Count");

        // 解决找不到类错误
        job.setJarByClass(WFCJobSubmit.class);

        // 1.1
        // FileInputFormat类设置要读取的文件路径
        FileInputFormat.setInputPaths(job, inPath);
        // setInputFormatClass设置读取文件时使用的格式化类
        job.setInputFormatClass(TextInputFormat.class);

        // 1.2调用自定义的Mapper类的map方法进行操作
        // 设置处理的Mapper类
        job.setMapperClass(WFCMapper.class);
        // 设置Mapper类处理完毕之后输出的键值对的数据类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        // 1.3分区，下面的两行代码写和没写都一样，默认的设置
        job.setPartitionerClass(HashPartitioner.class);
        job.setNumReduceTasks(1);
        // 1.4排序，分组

        // 1.5归约，这三步都有默认的设置，如果没有特殊的需求可以不管
        // 2.1将数据传输到对应的Reducer

        // 2.2使用自定义的Reducer类操作
        // 设置Reducer类
        job.setReducerClass(WFCReducer.class);
        // 设置Reducer处理完之后 输出的键值对 的数据类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 2.3将结果输出
        // FileOutputFormat设置输出的路径
        FileOutputFormat.setOutputPath(job, outPath);
        // setOutputFormatClass设置输出时的格式化类
        job.setOutputFormatClass(TextOutputFormat.class);

        // 将当前的job对象提交
        job.waitForCompletion(true);

    }

}
