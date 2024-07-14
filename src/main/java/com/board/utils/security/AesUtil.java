package com.board.utils.security;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class AesUtil {

    private static final Gson GSON = new Gson();

    // 128(16byte), 192(24byte), 256(32byte)
    private static final String PASSWORD_KEY = "P0ryv1te2At8Mix7KeY9ecU4Ri6tY3ha";

    private static final String AES = "AES";

    private static final String KEY_PADDING = "AES/CBC/PKCS5Padding";

    public static String getAes128Enc(String str) {
        return getAesEnc(str, "128");
    }

    public static String getAes128Dec(String str) {
        return getAesDec(str, "128");
    }

    public static String getAes192Enc(String str) {
        return getAesEnc(str, "192");
    }

    public static String getAes192Dec(String str) {
        return getAesDec(str, "192");
    }


    public static String getAes256Enc(String str) {
        return getAesEnc(str, "256");
    }

    public static String getAes256Dec(String str) {
        return getAesDec(str, "256");
    }

    public static String getAesEnc(String str, String type) {
        byte[] encryptedData = null;

        String aes256EncKey = PASSWORD_KEY;
        if (type.equals("128")) {
            aes256EncKey = aes256EncKey.substring(0, 16);
        } else if (type.equals("192")) {
            aes256EncKey = aes256EncKey.substring(0, 24);
        }

        String aes256lv = aes256EncKey.substring(0, 16);

        byte[] aes256EncKeyByte = aes256EncKey.getBytes(StandardCharsets.UTF_8);
        byte[] aes256lvByte = aes256lv.getBytes(StandardCharsets.UTF_8);

        try {
            SecretKey aesKey = new SecretKeySpec(aes256EncKeyByte, AES);
            IvParameterSpec iv = new IvParameterSpec(aes256lvByte);

            Cipher cipher = Cipher.getInstance(KEY_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);
            encryptedData = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("[ERROR] {} | {}", e.getMessage(), e);
        }
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    private static String getAesDec(String str, String type) {
        byte[] decryptedData = null;

        String aes256EncKey = PASSWORD_KEY;
        if (type.equals("128")) {
            aes256EncKey = aes256EncKey.substring(0, 16);
        } else if (type.equals("192")) {
            aes256EncKey = aes256EncKey.substring(0, 24);
        }

        String aes256lv = aes256EncKey.substring(0, 16);

        byte[] aes256EncKeyByte = aes256EncKey.getBytes(StandardCharsets.UTF_8);
        byte[] aes256lvByte = aes256lv.getBytes(StandardCharsets.UTF_8);

        try {
            SecretKey aesKey = new SecretKeySpec(aes256EncKeyByte, AES);
            IvParameterSpec iv = new IvParameterSpec(aes256lvByte);

            Cipher cipher = Cipher.getInstance(KEY_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, iv);
            decryptedData = cipher.doFinal(Base64.getDecoder().decode(str));
        } catch (Exception e) {
            log.error("[ERROR] {} | {}", e.getMessage(), e);
        }
        return new String(decryptedData);
    }
}