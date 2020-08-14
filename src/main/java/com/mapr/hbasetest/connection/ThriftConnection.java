package com.mapr.hbasetest.connection;

import com.mapr.fs.proto.Security;
import com.mapr.login.MapRLoginException;
import com.mapr.security.JNISecurity;
import com.mapr.security.MutableInt;
import com.mapr.security.client.ClientSecurity;
import com.mapr.security.client.MapRClientSecurityException;
import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.hadoop.security.SaslRpcServer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSaslClientTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ThriftConnection {

  public ThriftConnection(boolean http) {
    this.http = http;
  }

  private TTransport transport;
  private THttpClient httpClient;
  private boolean http;

  public Hbase.Client createClient() throws TTransportException, SaslException, MapRClientSecurityException {
    if (http) {
//      String encoding = Base64.getEncoder().encodeToString("mapr:mapr".getBytes(Charset.forName("UTF-8")));
      GSSTokenGenerator tokenGenerator = new GSSTokenGenerator();
      byte[] token = tokenGenerator.createToken("HTTP/node10.cluster.com", "HTTP/node10.cluster.com@NODE3");
      String encoding = Base64.getEncoder().encodeToString(token);

      String url = "https://node10.cluster.com:9090";

      httpClient = new THttpClient(url);
//      httpClient.setCustomHeader  ("Authorization", "Basic " + encoding);
      httpClient.setCustomHeader  ("Authorization", "Negotiate " + encoding);
//      addMaprAuthHeader(httpClient);
      httpClient.open();
      TProtocol protocol = new TBinaryProtocol(httpClient);
      return new Hbase.Client(protocol);
    } else {
      Map<String, String> saslProperties = new HashMap<String, String>();
      saslProperties.put(Sasl.QOP, "auth-conf");

      transport = new TSocket("node5.cluster.com", 9090);// IP Host Name
      transport = new TSaslClientTransport("MAPR-SECURITY", null, null,
          SaslRpcServer.SASL_DEFAULT_REALM, saslProperties, null, transport);
//      transport = new TSaslClientTransport("GSSAPI", null, "mapr/node3.cluster.com",
//          "NODE3", saslProperties, null, transport);
      TProtocol protocol = new TBinaryProtocol(transport, true, true);// Note here
      transport.open();
      return new Hbase.Client(protocol);
    }
  }



  public void addMaprAuthHeader(THttpClient client) throws MapRClientSecurityException {
    ClientSecurity cs = new ClientSecurity("cyber.mapr.cluster");
    String challenge = cs.generateChallenge();
    String encodedChallenge = Base64.getEncoder().encodeToString(challenge.getBytes(Charset.forName("UTF-8")));
    client.setCustomHeader("Authorization", "MAPR-Negotiate " + challenge);
  }


  public static TTransport getTrustAllSSLSocket(String host, int port, int loginTimeout) throws TTransportException {
    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509ExtendedTrustManager() {
          @Override
          public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
          }
          @Override
          public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
          }
          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }
          @Override
          public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
          }
          @Override
          public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
          }
          @Override
          public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
          }
          @Override
          public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
          }
        }
    };
    SSLSocket socket;
    try {
      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      SSLSocketFactory factory = sslContext.getSocketFactory();
      socket = (SSLSocket) factory.createSocket(host, port);
      socket.setSoTimeout(loginTimeout);
    } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
      throw new TTransportException("Couldn't create Trust All SSL socket", e);
    }
    return new TSocket(socket);
  }

  public void close() {
    if (http) {
      httpClient.close();
    } else {
      transport.close();
    }
  }


}
