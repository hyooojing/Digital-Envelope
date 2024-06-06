package com.example.demo.service;

import com.example.demo.exception.KeyLoadException;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

@Service
public class GenerateKeyManager {
    private static final String keyAlgorithm = "AES";
    private static final String keyPairAlgorithm = "RSA";

    // 사용자 비밀키 생성 및 저장
    public void createSecretKey(String id) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(keyAlgorithm);
        keyGen.init(256);
        Key key = keyGen.generateKey();
        FileManager.writeObjectToFile(id + ".key", key);
    }

    // 비밀키 가져오기
    public static Key getSecretKey (String rName) throws KeyLoadException {
        return (SecretKey) FileManager.readObjectFromFile(rName);
    }

    //사용자 id를 받아 키 쌍 생성 및 저장
    public void createKeyPair(String id) throws NoSuchAlgorithmException {
        String fName = id+".keyPair";
        KeyPairGenerator keyPairGen = null;
        keyPairGen = KeyPairGenerator.getInstance(keyPairAlgorithm);
        keyPairGen.initialize(2048);
        KeyPair keypair = keyPairGen.generateKeyPair();
        FileManager.writeObjectToFile(fName, keypair);
    }

    // 키 페어 가져오기
    public static KeyPair getKeyPair(String id) throws KeyLoadException {
        String fName = id + ".keyPair";
        KeyPair keyPair = (KeyPair) FileManager.readObjectFromFile(fName);
        return keyPair;
    }

    // 개인키 가져오기
    public static PrivateKey getPrivateKey(String id) throws KeyLoadException {
        return getKeyPair(id).getPrivate();
    }

    // 공개키 가져오기
    public static PublicKey getPublicKey(String id) throws KeyLoadException {
        return getKeyPair(id).getPublic();
    }

    public static Key getKeyFromEncoded(byte[] encoded) {
        return new SecretKeySpec(encoded, keyAlgorithm);
    }
}
