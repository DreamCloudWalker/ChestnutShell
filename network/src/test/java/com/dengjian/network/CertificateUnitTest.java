package com.dengjian.network;

import org.junit.Test;

import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.CertificatePinner;

public class CertificateUnitTest {

    @Test
    public void pinTest() {
        String path = "https://restapi.amap.com/";

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(path).openConnection();
            connection.connect();
            for (Certificate certificate : connection.getServerCertificates()) {
                X509Certificate x509Certificate = (X509Certificate) certificate;
                // 输出证书指纹
                System.out.println(CertificatePinner.pin(x509Certificate));
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
