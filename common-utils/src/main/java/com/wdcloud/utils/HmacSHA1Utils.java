package com.wdcloud.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSHA1Utils {
    public static String genHmacSHA1WithEncodeBase64URLSafe(String data, String key) {
        try {
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signinKey);
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.encodeBase64URLSafe(rawHmac), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
