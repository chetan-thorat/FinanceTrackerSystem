package com.financetracker.util;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionUtil {
    
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    
    @Value("${jasypt.encryptor.password}")
    private String encryptionPassword;
    
    private StringEncryptor stringEncryptor;
    
    private StringEncryptor getEncryptor() {
        if (stringEncryptor == null) {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
            config.setPassword(encryptionPassword);
            config.setAlgorithm("PBEWithMD5AndDES");
            encryptor.setConfig(config);
            stringEncryptor = encryptor;
        }
        return stringEncryptor;
    }
    
    /**
     * Encrypt sensitive financial data using AES-256
     */
    public String encrypt(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        try {
            return getEncryptor().encrypt(data);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    /**
     * Decrypt sensitive financial data
     */
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }
        try {
            return getEncryptor().decrypt(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    /**
     * Encrypt payment method information
     */
    public String encryptPaymentMethod(String paymentMethod) {
        return encrypt(paymentMethod);
    }
    
    /**
     * Decrypt payment method information
     */
    public String decryptPaymentMethod(String encryptedPaymentMethod) {
        return decrypt(encryptedPaymentMethod);
    }
}

