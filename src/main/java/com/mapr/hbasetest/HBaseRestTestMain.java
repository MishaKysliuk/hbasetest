package com.mapr.hbasetest;

import com.mapr.hbasetest.connection.RestConnection;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class HBaseRestTestMain {

  public static void main(String[] args) throws IOException {
    System.setProperty("javax.net.ssl.trustStore", "/opt/mapr/conf/ssl_truststore");

    RestConnection connection = new RestConnection(true);
    connection.printResponse(connection.get("/version/cluster"));
  }

}
