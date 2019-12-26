package com.mapr.hbasetest;

import com.mapr.hbasetest.connection.HbaseTestConnection;

public class AsyncHBaseMain {

  public static void main(String[] args) {
    System.out.println("Creating table '" + args[0] + "'");
    String tableName = args[0];
    HbaseTestConnection testConnection = new HbaseTestConnection();
//    HB
  }
}
