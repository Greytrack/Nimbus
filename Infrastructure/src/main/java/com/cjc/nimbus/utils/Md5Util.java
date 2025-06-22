package com.cjc.nimbus.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * MD5工具类
 *
 * @author CJC
 * @date 2024/8/8
 * @description MD5工具类
 */
@Slf4j
public class Md5Util {

    private Md5Util() {
    }

    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    public static String md5Encode(String origin, String charset) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charset == null || charset.isEmpty()) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charset)));
            }
        } catch (Exception e) {
            log.error("MD5加密失败", e);
        }
        return resultString;
    }

}
