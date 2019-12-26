package com.mapr.hbasetest;

import com.mapr.hbasetest.connection.ThriftConnection;
import com.mapr.security.client.MapRClientSecurityException;
import com.mapr.security.maprsasl.MaprSaslProvider;
import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.thrift.TException;

import javax.security.sasl.SaslException;
import java.nio.ByteBuffer;
import java.security.Security;

public class HBaseThriftTestMain {

  public static void main(String[] args) throws SaslException, TException, MapRClientSecurityException {

    Security.addProvider(new MaprSaslProvider());
    System.load("/opt/mapr/hadoop/hadoop-2.7.0/lib/native/libMapRClient.so");

    ThriftConnection connection = new ThriftConnection(true);

    Hbase.Client client = connection.createClient();
    System.out.println("scanning tables...");
    for (ByteBuffer buffer : client.getTableNames()) {
      System.out.println(new String(buffer.array()));
    }
//    for (TRowResult rowResult : client.getRows(ByteBuffer.wrap(args[0].getBytes()), null, null)) {
//      System.out.println(rowResult);
//    }

    Runtime.getRuntime().addShutdownHook(new Thread(connection::close));
  }
}
