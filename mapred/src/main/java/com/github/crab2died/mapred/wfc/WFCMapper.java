package com.github.crab2died.mapred.wfc;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WFCMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    // map过程
    // 文件按行读取得到每一行字符串
    // key：为行号  value：为每一行的字符串
    // 将每一行的字符串按空格分割成单词，每个单词为key，value为1(单词出现一次)，作为输入传给reduce过程
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] words = value.toString().split("\\s+");
        for (String word : words) {
            // 过滤特殊字符
            word = word.replaceAll("[^0-9a-zA-Z\\-]+","");
            context.write(new Text(word), new LongWritable(1));
        }
    }
}
