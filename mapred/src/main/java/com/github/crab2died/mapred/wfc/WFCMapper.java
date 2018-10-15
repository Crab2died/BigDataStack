package com.github.crab2died.mapred.wfc;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WFCMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] words = value.toString().split("\\s+");
        for (String word : words) {
            word = word.replaceAll("[^0-9a-zA-Z]+","");
            context.write(new Text(word), new LongWritable(1));
        }
    }
}
