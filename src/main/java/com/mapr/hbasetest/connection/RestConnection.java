package com.mapr.hbasetest.connection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteAdmin;
import org.apache.hadoop.hbase.rest.client.Response;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

public class RestConnection {

  private Client restClient;
  private Configuration hbaseConfig;
  private RemoteAdmin remoteAdmin;


  public RestConnection(boolean sslEnabled) {
    restClient = getAuthorizedClient(sslEnabled);
    Configuration conf = new Configuration();
    conf.setClassLoader(HBaseConfiguration.class.getClassLoader());
    String hbaseSiteLocation = "/opt/mapr/hbase/hbase-1.4.12/conf/hbase-site.xml";
    conf.addResource(new Path(hbaseSiteLocation));

    hbaseConfig = HBaseConfiguration.create();
    hbaseConfig = conf;
    remoteAdmin = new RemoteAdmin(restClient, hbaseConfig);
  }

  private Client getAuthorizedClient(boolean sslEnabled) {
    Cluster cluster = new Cluster(Collections.singletonList("node5.cluster.com:8080"));
    Client restClient = new Client(cluster, sslEnabled);
//    GSSTokenGenerator tokenGenerator = new GSSTokenGenerator();
//    byte[] token = tokenGenerator.createToken("HTTP/node3.cluster.com", "HTTP/node3.cluster.com@NODE3");
//    String encoding = Base64.getEncoder().encodeToString(token);
    restClient.addBasicAuthExtraHeader("mapr", "mapr");
//    restClient.addExtraHeader ("Authorization", "Negotiate " + encoding);
    return restClient;
  }

  public Response get(String path) throws IOException {
    return restClient.get(path);
  }

  public RemoteAdmin getRemoteAdmin() {
    return remoteAdmin;
  }

  public Client getClient() {
    return restClient;
  }

  public void printResponse(Response response) {
    System.out.println("CODE = " + response.getCode());
    System.out.println("BODY = " + new String(response.getBody()));
  }

}
