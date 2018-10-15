package com.github.crab2died.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class HdfsApp {

    public static void main(String[] args) {

        FileSystem fileSystem = null;

        // set user
        // System.setProperty("HADOOP_USER_NAME", "crab2died");

        Configuration conf = new Configuration();
        //conf.set("fs.defaultFS", "hdfs://crab2died:9000");

        // To solve the problem: File /hdfs/file.txt could only be replicated to 0 nodes instead of minReplication (=1).  There are 1
        conf.set("dfs.client.use.datanode.hostname", "true");

        try {
            // fileSystem = FileSystem.get(conf);
            fileSystem = FileSystem.get(new URI("hdfs://crab2died:9000"), conf, "crab2died");

            Path dir = new Path("/hdfs");
            if (!fileSystem.exists(dir))
                fileSystem.mkdirs(new Path("/hdfs"));

            FSDataOutputStream out = fileSystem.create(new Path("/home/crab2died/file.txt"));

            InputStream in = new FileInputStream("LICENSE");

            IOUtils.copyBytes(in, out, 1024, true);

//             fileSystem.copyFromLocalFile(false, new Path("file.txt"), new Path("/hdfs/file2.txt"));
//
//             fileSystem.delete(dir, true);

        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (null != fileSystem) {
                try {
                    fileSystem.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
