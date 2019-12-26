package com.mapr.hbasetest;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class GenerateSpnegoToken {

  public static void main(String[] args) throws GSSException, LoginException {

    /*GSSManager manager = GSSManager.getInstance();
    String serverPrinciple = "HTTP/node3.cluster.com@NODE3";
    GSSName serverName = manager.createName(serverPrinciple, null);
    Oid krb5Oid = new Oid("1.2.840.113554.1.2.2");
    GSSContext clientContext =
        manager.createContext(serverName, krb5Oid, (GSSCredential) null, GSSContext.DEFAULT_LIFETIME);
    clientContext.requestMutualAuth(true);
    clientContext.requestConf(true);
    clientContext.requestInteg(true);
    LoginContext lc = new LoginContext("MAPR_WEBSERVER_KERBEROS");
    lc.login();
    byte[] clientToken = clientContext.initSecContext(new byte[0], 0, 0);
    System.out.println(new String(clientToken));*/
//    GenerateSpnegoToken.createToken("mapr", "HTTP/node3.cluster.com@NODE3");
  }


  public static void outputToken(byte[] token) {
    System.out.println(new String(token));
  }

}
