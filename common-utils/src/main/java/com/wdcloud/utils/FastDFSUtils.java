package com.wdcloud.utils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class FastDFSUtils {
    private static String md5(byte[] source) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        java.security.MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(source);
        byte tmp[] = md.digest();
        char str[] = new char[32];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            str[k++] = hexDigits[tmp[i] >>> 4 & 0xf];
            str[k++] = hexDigits[tmp[i] & 0xf];
        }

        return new String(str);
    }

    /**
     * get token for file URL
     *
     * @param filename the filename return by FastDFS server
     * @param ts              unix timestamp, unit: second
     * @param secretKey      the secret key
     * @return token string
     */
    public static String getToken(String filename, int ts, String secretKey) {
        byte[] bsFilename = filename.getBytes(StandardCharsets.UTF_8);
        byte[] bsKey = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] bsTimestamp = Integer.valueOf(ts).toString().getBytes(StandardCharsets.UTF_8);
        byte[] buff = new byte[bsFilename.length + bsKey.length + bsTimestamp.length];
        System.arraycopy(bsFilename, 0, buff, 0, bsFilename.length);
        System.arraycopy(bsKey, 0, buff, bsFilename.length, bsKey.length);
        System.arraycopy(bsTimestamp, 0, buff, bsFilename.length + bsKey.length, bsTimestamp.length);
        return md5(buff);
    }
}
