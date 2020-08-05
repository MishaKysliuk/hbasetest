package com.mapr.hbasetest;

import com.mapr.hbasetest.connection.RestConnection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;

import java.io.IOException;

public class HBaseRestTestMain {

  public static void main(String[] args) throws IOException {
    System.setProperty("javax.net.ssl.trustStore", "/opt/mapr/conf/ssl_truststore");

    RestConnection connection = new RestConnection(true);
    Client client = connection.getClient();

    String tableName = "/emp";
    tableName = tableName.replaceAll("/", "%2F");
    try (RemoteHTable rTable = new RemoteHTable(client, tableName)) {
      Put put = new Put("row1".getBytes());
      put.addColumn("cf1".getBytes(), "qual1".getBytes(), "value1".getBytes());

      rTable.put(put);
    }


    //connection.printResponse(connection.get("/version/cluster"));
  }

}
