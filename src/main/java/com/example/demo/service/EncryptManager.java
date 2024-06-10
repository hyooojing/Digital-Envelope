package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.io.*;

import java.security.*;

@Service
public class EncryptManager {
    // 암호화 후 바이트 배열
    static byte[] encrypt(byte[] data, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    // 복호화 후 바이트 배열
    public static byte[] decrypt(byte[] encryptedData, Key key) {
        try {
            Cipher ci = Cipher.getInstance(key.getAlgorithm());
            ci.init(Cipher.DECRYPT_MODE, key);
            return ci.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    // 암호화 후 파일에 저장
    public static void encryptToFile(String fName, byte[] data, Key key) {
        try {
            Cipher ci = Cipher.getInstance(key.getAlgorithm());
            ci.init(Cipher.ENCRYPT_MODE, key);

            try (FileOutputStream fos = new FileOutputStream(fName);
                 CipherOutputStream cos = new CipherOutputStream(fos, ci)) {
                cos.write(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    // 파일 읽은 후 복호화
    public static byte[] decryptFromFile(String fname, Key key) {
        try {
            Cipher ci = Cipher.getInstance(key.getAlgorithm());
            ci.init(Cipher.DECRYPT_MODE, key);

            try(FileInputStream fis = new FileInputStream(fname);
                CipherInputStream cis = new CipherInputStream(fis, ci);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();){
                int length;
                byte[] buffer = new byte[256];

                while ((length = cis.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, length);
                }
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}