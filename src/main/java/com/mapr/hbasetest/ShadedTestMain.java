package com.mapr.hbasetest;

import org.apache.hadoop.hbase.shaded.org.apache.http.HttpEntity;
import org.apache.hadoop.hbase.shaded.org.apache.http.HttpHeaders;
import org.apache.hadoop.hbase.shaded.org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.hadoop.hbase.shaded.org.apache.http.client.methods.HttpGet;
import org.apache.hadoop.hbase.shaded.org.apache.http.impl.client.CloseableHttpClient;
import org.apache.hadoop.hbase.shaded.org.apache.http.impl.client.HttpClients;
import org.apache.hadoop.hbase.shaded.org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

public class ShadedTestMain {

    public static void main(String[] args) throws IOException {
        String url = "https://node11.cluster.com:16010/master-status";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            // add request headers
            String authString = "mapr:mapr";
            String encoding = Base64.getEncoder().encodeToString(authString.getBytes(Charset.forName("UTF-8")));
            String authHeader = "Basic " + encoding;
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);

            try(CloseableHttpResponse response = httpClient.execute(request)) {
                // Get HttpResponse Status
                System.out.println(response.getProtocolVersion());              // HTTP/1.1
                System.out.println(response.getStatusLine().getStatusCode());   // 200
                System.out.println(response.getStatusLine().getReasonPhrase()); // OK
                System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                }
            }
        }
    }
}
