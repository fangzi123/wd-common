package com.wdcloud.utils.httpClient;

import org.apache.http.client.HttpClient;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.SSLInitializationException;

import javax.net.ssl.SSLContext;
import java.security.*;

/**
 * httpclient 4.3.x
 */
public class HttpClientFactory {

    public static HttpClient createTLSHttpClient() {
        try {
            SSLContext sslContext = SSLContexts.createDefault();
            SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext);
            return HttpClientBuilder.create().setSSLSocketFactory(sf).build();
        } catch (SSLInitializationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpClient createTLSHttpClient(int maxTotal, int maxPerRoute) {
        try {
            SSLContext sslContext = SSLContexts.createDefault();
            SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext);
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
            poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
            poolingHttpClientConnectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(15000).build());
            return HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager)
                    .setSSLSocketFactory(sf).build();
        } catch (SSLInitializationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Key store 类型HttpClient
     *
     * @param keystore
     * @param keyPassword
     * @param supportedProtocols
     * @return
     */
    public static HttpClient createKeyMaterialHttpClient(KeyStore keystore, String keyPassword, String[] supportedProtocols) {
        try {
            SSLContext sslContext = SSLContexts.custom().useProtocol("TLS").loadKeyMaterial(keystore, keyPassword.toCharArray()).build();
            SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext, supportedProtocols, null, new DefaultHostnameVerifier());
            return HttpClientBuilder.create().setSSLSocketFactory(sf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
