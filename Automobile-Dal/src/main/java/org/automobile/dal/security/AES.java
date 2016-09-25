/*
 * Copyright (c) 2015 FC-POS Compulynx LTD.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are NOT permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COMPULYNX LTD BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.automobile.dal.security;

import org.apache.commons.codec.binary.Base64;
import org.automobile.dal.log.AutomobileLogger;
import org.automobile.dal.log.impl.AutomobileLoggerImpl;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

/**
 * @author Kushal
 */
public class AES {

   private static final String password = "1tqk7rbdamfrjqgi9l5kqur818jnb4sqphka3u8mifrs3fhdni5h";
   private static final String salt = "1uhv9hsujjit3g2ck5fnsgg4f9f2qf98kdj2seafsv8r7m4r9ij1";
   private static final String encryptIV = "trg35y4g36natuuv";
   private static final String secretKey = "PBKDF2WithHmacSHA1";

   private static final String cipherType = "AES/CBC/PKCS5Padding";
   private static final String encodingType = "UTF-8";
   private static final String encryptionStandard = "AES";

   private static final int passIterations = 65536;
   /*For Extra Security User Key Size 256, To Enable this in Java
   * visit links below:
   * http://stackoverflow.com/questions/19503926/java-security-aes-encryption-key-length
   * http://www.javamex.com/tutorials/cryptography/unrestricted_policy_files.shtml*/
   /*private static final int keySize = 256;*/
   private static final int keySize = 128;
   private final AutomobileLogger automobileLogger;

   public AES() {
      automobileLogger = new AutomobileLoggerImpl(getClass());
   }

   public static String decryptPWD(String input) throws Exception {
      try {
         Cipher cipher = Cipher.getInstance("AES");
         cipher.init(Cipher.DECRYPT_MODE, generateKey());
         String value = new String(cipher.doFinal(decodeString(input)));
         return value;
      } catch (Exception e) {
         e.printStackTrace();
         return "";
      }
   }

   // generates a secret key
   private static Key generateKey() throws Exception {
      try {
         byte[] keyval = "@compulynx#54321".getBytes();
         MessageDigest digest = MessageDigest.getInstance("SHA-1");
         keyval = digest.digest(keyval);
         keyval = Arrays.copyOf(keyval, 16);
         Key key = new SecretKeySpec(keyval, "AES");
         return key;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   private static byte[] decodeString(String output) {
      return Base64.decodeBase64(output);
   }

   public String encrypt(String plainText) throws Exception {

      byte[] saltBytes = salt.getBytes(encodingType);

      // Derive the key
      SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKey);
      PBEKeySpec spec = new PBEKeySpec(
         password.toCharArray(),
         saltBytes,
         passIterations,
         keySize
      );

      SecretKey secretKey = factory.generateSecret(spec);
      SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), encryptionStandard);

      //encrypt the message
      Cipher cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.ENCRYPT_MODE, secret, makeIv());

      byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes(encodingType));
      return new Base64().encodeAsString(encryptedTextBytes);
   }

   public String decrypt(String encryptedText) throws Exception {

      byte[] saltBytes = salt.getBytes(encodingType);

      byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText);

      // Derive the key
      SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKey);
      PBEKeySpec spec = new PBEKeySpec(
         password.toCharArray(),
         saltBytes,
         passIterations,
         keySize
      );

      SecretKey secretKey = factory.generateSecret(spec);
      SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), encryptionStandard);

      Cipher cipher;
      cipher = Cipher.getInstance(cipherType);
      cipher.init(Cipher.DECRYPT_MODE, secret, makeIv());

      byte[] decryptedTextBytes;
      try {
         decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
         return new String(decryptedTextBytes);
      } catch (IllegalBlockSizeException | BadPaddingException e) {
         automobileLogger.error("Error Decrypting string", e);
         return null;
      }

   }

   private AlgorithmParameterSpec makeIv() {
      try {
         return new IvParameterSpec(encryptIV.getBytes(encodingType));
      } catch (UnsupportedEncodingException e) {
         automobileLogger.error("Error creating IV key", e);
      }
      return null;
   }


}
