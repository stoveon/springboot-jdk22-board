package com.board.utils.security;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Slf4j
public class ShaUtil {

    private static final Gson GSON = new Gson();

    private static final String SHA256 = "SHA-256";

    public static String getHashString(String str) {
        String result = "";
        if (str != null && !str.isEmpty()) {
            try {
                MessageDigest digest = MessageDigest.getInstance(SHA256);
                byte[] starByte = str.getBytes(StandardCharsets.UTF_8);
                result = toHexString(digest.digest(starByte));
            } catch (Exception e) {
                log.error("{} | {}", e, e.getMessage());
            }
        }
        return result;
    }

    /**
     * 16진수 문자열을 반환한다.
     *
     * @param block
     * @return
     */
    private static String toHexString(byte[] block) {

        StringBuffer buf = new StringBuffer();

        int len = block.length;

        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
        }

        return buf.toString();
    }

    /**
     * byte -> 16진수
     *
     * @param b
     * @param buf
     */
    private static void byte2hex(byte b, StringBuffer buf) {

        char[] hexChars = {
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F'
        };

        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);

        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

}
