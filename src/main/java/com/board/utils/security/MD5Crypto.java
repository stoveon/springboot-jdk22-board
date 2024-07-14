package com.board.utils.security;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class MD5Crypto {

    private static final Gson GSON = new Gson();

    /**
     * 평문을 받아 MD5로 암호화 적용 후 Hex 값으로 인코딩
     * @param plainText
     * @return
     */
    public static String encryptToHex(String plainText) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            log.error("[ERROR] {} | {}", e.getMessage(), e);
        }

        return HexUtil.bytesToHex2(md.digest());
    }

    /**
     * 평문을 받아 MD5로 암호화 적용 후 Base64 값으로 인코딩
     * @param plainText
     * @return
     */
    public static String encryptToBase64(String plainText) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            log.error("[ERROR] {} | {}", e.getMessage(), e);
        }

        return Base64.getEncoder().encodeToString(md.digest());
    }

}
