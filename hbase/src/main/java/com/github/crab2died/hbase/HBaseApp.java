package com.github.crab2died.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HBaseApp {

    private static Connection connection;

    static {

        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "crab2died");
        configuration.set("hbase.master", "crab2died:600000");

        try {
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //createTable("student", new String[]{"course"});

//        addData("student", "10000", "course",
//                new String[]{"Hadoop", "Spark", "Flink"}, new String[]{"92", "98", "99"});

        Result result = getData("student", "course", "10000", "Hadoop", "Spark");
        for (Cell cell : result.listCells()) {
            System.out.println(Bytes.toString(cell.getFamilyArray()) + ":" + Bytes.toString(cell.getQualifierArray()) + "=>" + Bytes.toInt(cell.getValueArray()));
        }
    }


    public static void createTable(String tableName, String[] familyNames) throws IOException {

        Admin admin = connection.getAdmin();
        if (!admin.isTableAvailable(TableName.valueOf(tableName))) {

            TableDescriptorBuilder tableDescriptorBuilder =
                    TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
            for (String familyName : familyNames) {
                tableDescriptorBuilder.setColumnFamily(
                        ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyName)).build()
                );
            }
            admin.createTable(tableDescriptorBuilder.build());
        }
    }

    public static void addData(String tableName, String rowKey, String familyName,
                               String[] columns, String[] values) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        // 不写wlan日志
        put.setDurability(Durability.SKIP_WAL);
        // 获取表中的所有列族 client version 2.0.1
        ColumnFamilyDescriptor[] columnFamilyDescriptors = table.getDescriptor().getColumnFamilies();
        for (ColumnFamilyDescriptor columnFamilyDescriptor : columnFamilyDescriptors) {
            String familyDescriptorNameAsString = columnFamilyDescriptor.getNameAsString();
            if (familyDescriptorNameAsString.equals(familyName)) {
                for (int i = 0; i < columns.length; i++) {
                    // 列族
                    put.addColumn(
                            Bytes.toBytes(familyName),
                            // 列
                            Bytes.toBytes(columns[i]),
                            // 列的值
                            Bytes.toBytes(values[i])
                    );
                }
            }
        }
        table.put(put);
    }

    public static Result getData(String tableName, String familyName, String rowKey, String... columns)
            throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        //指定列
        if (null != columns && columns.length > 0) {
            for (String column : columns) {
                get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(column));
            }
        }
        return table.get(get);
    }

    public static void deleteData(String tableName, String... rowKeys)
            throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));
        List<Delete> deleteList = new ArrayList<>(rowKeys.length);
        Delete delete;
        for (String rowKey : rowKeys) {
            delete = new Delete(Bytes.toBytes(rowKey));
            deleteList.add(delete);
        }
        table.delete(deleteList);
    }

}
