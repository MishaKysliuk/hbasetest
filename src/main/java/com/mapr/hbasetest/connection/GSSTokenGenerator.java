package com.mapr.hbasetest.connection;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

public class GSSTokenGenerator {

  private GSSCredential createCredential(Oid mechOid, String userName) {
    System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
    //    System.setProperty("java.security.auth.login.config", "bcsLogin.conf");

    GSSCredential clientGssCreds = null;
    try {
      GSSManager manager = GSSManager.getInstance();

      GSSName gssUserName = manager.createName(userName, GSSName.NT_USER_NAME, mechOid);

      clientGssCreds = manager
          .createCredential(gssUserName.canonicalize(mechOid), GSSCredential.INDEFINITE_LIFETIME, mechOid,
              GSSCredential.INITIATE_ONLY);

    } catch (GSSException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return clientGssCreds;
  }

  public byte[] createToken(String sUserName, String sServerSpn) {
    try {
      Oid mechOid = new Oid("1.2.840.113554.1.2.2");
      GSSCredential gsscredential = createCredential(mechOid, sUserName);

      if (gsscredential != null) {
        byte[] token = createToken(gsscredential, sServerSpn, mechOid);

        if (token != null) {
          return token;
        }
      }

    } catch (GSSException e) {
      e.printStackTrace();
    }
    throw new RuntimeException();
  }

  private byte[] createToken(GSSCredential clientGssCreds, String sServerSpn, Oid mechOid) {
    byte[] token = new byte[0];
    try {
      GSSManager manager = GSSManager.getInstance();

      // create target server SPN
      GSSName gssServerName = manager.createName(sServerSpn, GSSName.NT_USER_NAME);

      GSSContext clientContext = manager
          .createContext(gssServerName.canonicalize(mechOid), mechOid, clientGssCreds, GSSContext.DEFAULT_LIFETIME);

      // optional enable GSS credential delegation
      clientContext.requestCredDeleg(true);

      // create a SPNEGO token for the target server
      token = clientContext.initSecContext(token, 0, token.length);

    } catch (GSSException e) {
      e.printStackTrace();
    }
    return token;
  }
}
