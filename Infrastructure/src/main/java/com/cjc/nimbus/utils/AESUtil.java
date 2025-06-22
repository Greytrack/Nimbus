package com.cjc.nimbus.utils;

import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author CJC
 * @version 2.0
 * @description AES工具类
 * @date 2024/9/18 17:13
 */
@Slf4j
public class AESUtil {

    private AESUtil() {
    }

    /**
     * 加密模式之 GCM，算法/模式/补码方式
     */
    private static final String ENCRYPTION_ALGO = "AES/GCM/NoPadding";
    /**
     * GCM 标签长度
     */
    private static final int GCM_TAG_LENGTH = 16;
    /**
     * GCM IV 长度
     */
    private static final int GCM_IV_LENGTH = 12;

    /**
     * <h2>加密 - 模式 GCM</h2>
     *
     * @param text 需要加密的文本内容
     * @param key  加密的密钥 key
     */
    public static String encrypt(String text, String key) {
        if (StringUtils.isBlank(text) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            // 创建AES加密器
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGO);
            // 生成 AES 密钥
            SecretKeySpec secretKeySpec = getSecretKeySpec(key);

            // 生成一个安全的随机 IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv));

            // 加密字节数组
            byte[] encryptedBytes = cipher.doFinal(getBytes(text));

            // 将 IV 和密文连接在一起
            byte[] encryptedDataWithIv = new byte[GCM_IV_LENGTH + encryptedBytes.length];
            System.arraycopy(iv, 0, encryptedDataWithIv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedBytes, 0, encryptedDataWithIv, GCM_IV_LENGTH, encryptedBytes.length);
            // 将密文转换为 Base64 编码字符串
            return Base64.getEncoder().encodeToString(encryptedDataWithIv);
        } catch (Exception e) {
            log.error("AESUtil.encrypt error: {}", e.getMessage());
            throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * <h2>解密 - 模式 ECB</h2>
     *
     * @param text 需要解密的文本内容
     * @param key  解密的密钥 key
     */
    public static String decrypt(String text, String key) {
        if (StringUtils.isBlank(text) || StringUtils.isBlank((key))) {
            return null;
        }

        // 将密文转换为16字节的字节数组
        byte[] encryptedDataWithIv = Base64.getDecoder().decode(text);

        try {
            // 创建AES加密器
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGO);
            // 生成 AES 密钥
            SecretKeySpec secretKeySpec = getSecretKeySpec(key);

            // 从密文中提取 IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(encryptedDataWithIv, 0, iv, 0, GCM_IV_LENGTH);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

            // 从密文中提取加密数据
            byte[] encryptedData = new byte[encryptedDataWithIv.length - GCM_IV_LENGTH];
            System.arraycopy(encryptedDataWithIv, GCM_IV_LENGTH, encryptedData, 0, encryptedData.length);
            byte[] decryptedData = cipher.doFinal(encryptedData);
            // 将解密数据转换为字符串
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AESUtil.decrypt error: {}", e.getMessage());
            throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * <h2>获取一个 AES 密钥规范</h2>
     */
    public static SecretKeySpec getSecretKeySpec(String key) {
        return new SecretKeySpec(getBytes(key), "AES");
    }

    /***
     * <h2>String 转 byte</h2>
     * @param str 需要转换的字符串
     */
    public static byte[] getBytes(String str) {
        if (StringUtils.isBlank(str)) {
            return new byte[0];
        }

        try {
            return str.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AESUtil.getBytes error: {}", e.getMessage());
            throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h2>测试</h2>
     */
    public static void main(String[] args) {
        String key = "ae2_hYl8Ts_12345";
        String random = StringUtils.random(32);
        String text = random + "::eyueqy7y328e19";
        log.info("Origin Data: {}", text);

        String encryptedData = encrypt(text, key);
        log.info("Encrypted Data: {}", encryptedData);

        String decryptedText = decrypt(encryptedData, key);
        log.info("Decrypted Data: {}", decryptedText);
    }

}
