package com.mapr.hbasetest;

import com.mapr.hbasetest.connection.HbaseTestConnection;
import org.apache.hadoop.hbase.client.ClusterConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

public class HbaseJavaApiTestMain {

  public static void main(String[] args) throws IOException {
    HbaseTestConnection testConnection = new HbaseTestConnection();
    try(Connection connection = ConnectionFactory.createConnection(testConnection.getHbaseConfig())) {
      try(HBaseAdmin admin = new HBaseAdmin(connection)) {
//        admin.normalize();
      }
    }
    System.out.println(testConnection.isHbaseAvailable());
  }

}
