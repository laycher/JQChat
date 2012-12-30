package com.jq.util;

import java.security.MessageDigest;

//MD5，用于加密的
public class MD5 {
    private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    
    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int t;
        for (int i = 0; i < 16; i++) {// 16 == bytes.length;
            t = bytes[i];
            if (t < 0)
                t += 256;
            sb.append(hexDigits[(t >>> 4)]);
            sb.append(hexDigits[(t % 16)]);
        }
        return sb.toString();
    }
    
    public static String code(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(System.getProperty(
                    "MD5.algorithm", "MD5"));
            return bytesToHex(md.digest(input.getBytes("utf-8")));
        } catch (Exception e) {
          //  e.printStackTrace();
           // throw new Exception("Could not found MD5 algorithm.", e);
          return null;
        }  
    }
}
