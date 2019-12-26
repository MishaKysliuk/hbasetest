package com.mapr.hbasetest;

import com.mapr.hbasetest.connection.WebUIConnection;

import java.io.IOException;

public class WebUITestMain {

  public static void main(String[] args) throws IOException {
    WebUIConnection con = new WebUIConnection();
    con.printResponse(con.getConnection());
  }
}
