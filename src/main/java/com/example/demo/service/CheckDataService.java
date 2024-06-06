package com.example.demo.service;

import com.example.demo.exception.*;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
// 전자봉투 개봉 및 검증
public class CheckDataService {
    String fileName;
    SendData sendData;
    Key secretKey;
    String plainData;

    // 요청 받은 정산 정보 해독
    public String decryptedData(String senderId, String receiverId) throws CipherException, KeyLoadException, SigException, FileException {
        // 파일에서 전자봉투 읽기
        String fileName = senderId + receiverId + ".data";
        try {
            sendData = (SendData) FileManager.readObjectFromFile(fileName);
        } catch (KeyLoadException e) {
            throw new KeyLoadException("해당 요청 정보와 관계 없는 사용자입니다.");
        }

        // 수신자의 개인키로 발신자의 비밀키 획득
        byte[] envelope = sendData.getEnvelope();
        PrivateKey privateKey = GenerateKeyManager.getPrivateKey(receiverId);
        byte[] keyArr = EncryptManager.decrypt(envelope, privateKey);
        if(keyArr.length==0){
            throw new CipherException(senderId + "의 비밀키 획득 도중 오류");
        }
        // 바이트 배열 Key 형태로
        secretKey = GenerateKeyManager.getKeyFromEncoded(keyArr);

        // 획득한 발신자의 비밀키로 정산 요청 정보 복호화
        byte[] encryptedData = sendData.getEncryptedData(); //암호화된 원본 데이터
        byte[] plainByteData = EncryptManager.decrypt(encryptedData, secretKey); //원본 데이터 복구
        String plainData = new String(plainByteData);
        System.out.println("정산정보: " + plainData);

        return plainData;
    }

    // 전자서명 검증
   public boolean verifyData(String plainData, String sender) throws KeyLoadException, SigException {
        // 암호화된 전자서명 파일
        fileName = sendData.getEncryptedSignFileName();
        byte[] sign = EncryptManager.decryptFromFile(fileName, secretKey);
        PublicKey publicKey = GenerateKeyManager.getPublicKey(sender);

        if (!SigManager.verify(plainData, publicKey, sign)) {
            return false;
        }
        return true;
    }
}
