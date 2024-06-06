package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.io.*;

import java.security.*;

@Service
public class EncryptManager {
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

    public static byte[] decrypt(byte[] encryptedData, Key key) {
        Cipher ci;
        try {
            ci = Cipher.getInstance(key.getAlgorithm());
            ci.init(Cipher.DECRYPT_MODE, key);
            return ci.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new byte[0];
    }


    public static void encryptToFile(String fName, byte[] data, Key key) {
        Cipher ci;
        try {
            ci = Cipher.getInstance(key.getAlgorithm());
            ci.init(Cipher.ENCRYPT_MODE, key);
            try(CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(fName), ci)){
                cos.write(data);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static byte[] decryptFromFile(String fname, Key key) {
        Cipher ci;
        try {
            ci = Cipher.getInstance(key.getAlgorithm());
            ci.init(Cipher.DECRYPT_MODE, key);

            //3. CipherInputStream
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}