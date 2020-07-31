package com.mapr.hbasetest.connection;

import org.apache.hadoop.hbase.thrift.generated.ColumnDescriptor;
import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.hadoop.hbase.thrift.generated.Mutation;
import org.apache.hadoop.hbase.thrift.generated.TRegionInfo;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.thrift.TException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HBaseThriftUtil {

    private Hbase.Client client;

    public HBaseThriftUtil(Hbase.Client client) {
        this.client = client;
    }

    public void createTable(String tableName, List<String> columnFamilies) throws TException {
        if (isTableExists(tableName)) {
            deleteTable(tableName);
        }
        client.createTable(convertStringToByteBuffer(tableName),
                convertToColumnDescriptorsList(columnFamilies));
    }

    public boolean isTableExists(String tableName) throws TException {
        List<String> tablesMatchingRegex = client.getTableNamesByPath(tableName);
        for (String name: tablesMatchingRegex) {
            if (name.equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTableEnabled(String tableName) throws TException {
        return client.isTableEnabled(convertStringToByteBuffer(tableName));
    }

    public void disableTable(String tableName) throws TException {
        if (isTableEnabled(tableName)) {
            client.disableTable(convertStringToByteBuffer(tableName));
        }
    }

    public void deleteTable(String tableName) throws TException {
        if (isTableExists(tableName)) {
            disableTable(tableName);
            client.deleteTable(convertStringToByteBuffer(tableName));
        }
    }

    public Map<String, ColumnDescriptor> getColumnDescriptors(String tableName) throws TException {
        return client.getColumnDescriptors(convertStringToByteBuffer(tableName)).entrySet().stream()
                .collect(Collectors.toMap(entry -> Bytes.toString(entry.getKey().array()), Map.Entry::getValue));
    }

//    public void putDataIntoTable(String tableName, HBaseRow row) throws TException {
//        List<Mutation> mutations = Collections.singletonList(new Mutation()
//                .setColumn(convertStringToByteBuffer(row.getFullColumnName()))
//                .setValue(convertStringToByteBuffer(row.getValue())));
//
//        client.mutateRow(convertStringToByteBuffer(tableName), convertStringToByteBuffer(row.getKey()),
//                mutations, Collections.emptyMap());
//    }

    public TRegionInfo getRegionInfo(String key) throws TException {
        return client.getRegionInfo(convertStringToByteBuffer(key));
    }

    private ByteBuffer convertStringToByteBuffer(String tableName) {
        return ByteBuffer.wrap(Bytes.toBytes(tableName));
    }
    private List<ColumnDescriptor> convertToColumnDescriptorsList(List<String> columnFamilies) {
        List<ColumnDescriptor> result = new ArrayList<>();
        for (String cf: columnFamilies) {
            ColumnDescriptor cd = new ColumnDescriptor();
            cd.setName(Bytes.toBytes(cf));
            result.add(cd);
        }
        return result;
    }

}
