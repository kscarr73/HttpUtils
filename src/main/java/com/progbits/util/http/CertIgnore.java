package com.progbits.util.http;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * @author garnold
 *
 */
public class CertIgnore implements X509TrustManager
{

   // TrustManager Methods
   public void checkClientTrusted(X509Certificate[] chain, String authType) {
   }

   public void checkServerTrusted(X509Certificate[] chain, String authType) {
   }

   public X509Certificate[] getAcceptedIssuers() {
       return null;
   }
}
