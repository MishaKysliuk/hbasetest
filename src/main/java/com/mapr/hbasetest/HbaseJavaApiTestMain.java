package com.mapr.hbasetest;

import com.mapr.hbasetest.connection.HbaseTestConnection;

public class HbaseJavaApiTestMain {

  public static void main(String[] args) {
    HbaseTestConnection testConnection = new HbaseTestConnection();
    System.out.println(testConnection.isHbaseAvailable());
  }

}
