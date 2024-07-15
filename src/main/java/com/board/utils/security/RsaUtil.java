package com.board.utils.security;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RsaUtil {

    private static final Gson GSON = new Gson();

    private static final String RSA = "RSA";

    /*
     * 공개키와 개인키 한 쌍 생성
     */
    public static Map<String, String> getKeyPair() {
        Map<String, String> keyPairMap = new HashMap<>();
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048, secureRandom);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            keyPairMap.put("publicKey", publicKeyStr);
            keyPairMap.put("privateKey", privateKeyStr);
        } catch (Exception e) {
            log.error("{} | {}", e.getMessage(), e);
        }
        return keyPairMap;
    }

    /*
     * 암호화 : 공개키로 진행
     */
    public static String encrypt(String str, String publicKeyStr) {
        String resultText = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            byte[] bytePublicKey = Base64.getDecoder().decode(publicKeyStr.getBytes(StandardCharsets.UTF_8));
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);

            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // 만들어진 공개키 객체로 암호화 설정
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(str.getBytes());
            resultText = Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("{} | {}", e.getMessage(), e);
        }
        return resultText;
    }

    /*
     * 복호화 : 개인키로 진행
     */
    public static String decrypt(String str, String stringPrivateKey) {
        String resultText = null;
        try {
            // 평문으로 전달받은 공개키를 사용하기 위해 공개키 객체 생성
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            // 만들어진 공개키 객체로 복호화 설정
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 암호문을 평문화하는 과정
            byte[] encryptedBytes = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            resultText = new String(decryptedBytes);
        } catch (Exception e) {
            log.error("{} | {}", e.getMessage(), e);
        }

        return resultText;
    }

}
