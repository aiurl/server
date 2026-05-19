package io.theurl.framework.utility;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

/**
 * Cryptography utility class providing methods for encrypting and decrypting data using DES and AES algorithms.
 * It uses the Bouncy Castle library as a security provider for cryptographic operations.
 * The DES encryption method uses CBC mode with PKCS5 padding, while the AES encryption method uses ECB mode with PKCS5 padding.
 * The salt parameter is used as the key for both encryption and decryption processes, and it should be of appropriate length for the respective algorithms (8 bytes for DES and 16 bytes for AES).
 * The encrypted data is encoded in Base64 format for easier storage and transmission, and the decryption method decodes the Base64 string back to its original byte array before performing the decryption operation.
 *
 * @author Damon Chew
 */
@SuppressWarnings("unused")
public class Cryptography {
    static {
        /* Register provider */
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * DES encryption and decryption utility methods.
     * The encrypt method takes a plaintext string and a salt (key) to perform DES encryption, returning the encrypted data as a Base64-encoded string.
     * The decrypt method takes a Base64-encoded encrypted string and the same salt (key) to perform DES decryption, returning the original plaintext string.
     */
    public static class DES {
        private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";

        /**
         * Encrypts the given data using DES algorithm with the provided salt as the key.
         *
         * @param data The plaintext data to be encrypted.
         * @param salt The salt (key) used for encryption.
         * @return The encrypted data as a Base64-encoded string.
         * @throws Exception If an error occurs during encryption.
         */
        public static String encrypt(String data, String salt) throws Exception {
            DESKeySpec desKeySpec = new DESKeySpec(salt.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(salt.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        }

        /**
         * Decrypts the given Base64-encoded encrypted data using DES algorithm with the provided salt as the key.
         *
         * @param data The Base64-encoded encrypted data to be decrypted.
         * @param salt The salt (key) used for decryption.
         * @return The decrypted plaintext data.
         * @throws Exception If an error occurs during decryption.
         */
        public static String decrypt(String data, String salt) throws Exception {
            DESKeySpec desKeySpec = new DESKeySpec(salt.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(salt.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decodedData = Base64.getDecoder().decode(data);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        }

    }

    /**
     * AES encryption and decryption utility methods.
     * The encrypt method takes a plaintext string and a salt (key) to perform AES encryption, returning the encrypted data as a Base64-encoded string.
     * The decrypt method takes a Base64-encoded encrypted string and the same salt (key) to perform AES decryption, returning the original plaintext string.
     */
    public static class AES {

        /**
         * Encrypts the given data using AES algorithm with the provided salt as the key.
         *
         * @param data The plaintext data to be encrypted.
         * @param salt The salt (key) used for encryption.
         * @return The encrypted data as a Base64-encoded string.
         * @throws Exception If an error occurs during encryption.
         */
        public static String encrypt(String data, String salt) throws Exception {
            SecretKeySpec secretKeySpec = new SecretKeySpec(salt.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        }

        /**
         * Decrypts the given Base64-encoded encrypted data using AES algorithm with the provided salt as the key.
         *
         * @param data The Base64-encoded encrypted data to be decrypted.
         * @param salt The salt (key) used for decryption.
         * @return The decrypted plaintext data.
         * @throws Exception If an error occurs during decryption.
         */
        public static String decrypt(String data, String salt) throws Exception {
            SecretKeySpec secretKeySpec = new SecretKeySpec(salt.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decodedData = Base64.getDecoder().decode(data);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        }
    }
}
