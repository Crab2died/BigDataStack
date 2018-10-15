package com.github.crab2died.mapred.wfc;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WFCReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    // reduce过程
    // 接受map过程的结果，key为单词，value为出现单词1次
    // 相同的key的value值之和即为该单词出现的总频次
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException {
        long sum = 0;
        for (LongWritable v : values) {
            sum += v.get();
        }
        context.write(key, new LongWritable(sum));
    }
}

