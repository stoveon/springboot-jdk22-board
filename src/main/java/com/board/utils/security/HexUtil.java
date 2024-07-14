package com.board.utils.security;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HexUtil {

    private static final Gson GSON = new Gson();

    /*
     * byte 배열을 Hex 값으로 변환
     */
    final static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String bytesToHex1(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    /*
     * byte 배열을 Hex 값으로 변환
     */
    public static String bytesToHex2(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    /*
     * byte 배열을 Hex 값으로 변환
     */
    public static String bytesToHex3(byte[] bytes) {
        String hexText = new java.math.BigInteger(bytes).toString(16);
        return hexText;
    }
}
