package com.mapr.hbasetest.connection;

import com.mapr.security.client.ClientSecurity;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;

public class WebUIConnection {

  public HttpsURLConnection getConnection() throws IOException {
    String url = "https://node11.cluster.com:16010/master-status";
    URL obj = new URL(url);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
    String authString = "mapr:mapr";
    String encoding = Base64.getEncoder().encodeToString(authString.getBytes(Charset.forName("UTF-8")));
    String authHeader = "Basic " + encoding;
    ClientSecurity cs = new ClientSecurity("cyber.mapr.cluster");
    String challenge = cs.generateChallenge();
    String encodedChallenge = Base64.getEncoder().encodeToString(challenge.getBytes(Charset.forName("UTF-8")));
    con.setRequestProperty("Authorization", "MAPR-Negotiate " + challenge);
    con.setRequestProperty("Authorization", authHeader);
    return con;
  }

  public void printResponse(HttpsURLConnection con) throws IOException {
    int responseCode = con.getResponseCode();
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    System.out.println(response.toString());
  }

}
