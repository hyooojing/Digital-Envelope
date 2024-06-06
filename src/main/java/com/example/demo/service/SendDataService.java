package com.example.demo.service;

import com.example.demo.exception.CipherException;
import com.example.demo.exception.FileException;
import com.example.demo.exception.KeyLoadException;
import com.example.demo.exception.SigException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;

@Service
public class SendDataService {
    //전자봉투 생성해서 데이터 보내는 함수
    public void sendMessage(String senderId, String receiverId, String data) throws UnsupportedEncodingException, SigException, CipherException, FileException, KeyLoadException, NoSuchAlgorithmException, SignatureException, ClassNotFoundException {
        // 전자서명 생성
        PrivateKey privateKey;
        try {
            privateKey = (PrivateKey) GenerateKeyManager.getPrivateKey(senderId);
        } catch (KeyLoadException e) {
            throw new KeyLoadException("[요청자]: " + senderId + "가 존재하지 않습니다.");
        }
        byte[] byteData = data.getBytes();
        byte[] signature = new byte[0];
        try {
            // 올바른 receiver 들어왔을 때만 전자서명 생성
            if (FileManager.readFile(receiverId + ".key")) {
                signature = SigManager.sign(byteData, privateKey, senderId + receiverId + "_signature.bin");
            }
        } catch (IOException e) {
            throw new KeyLoadException("[수신자]: " + receiverId + "가 존재하지 않습니다.");
        }

        // 원본+전자서명+publicKey 암호화
        SecretKey secretKey = (SecretKey) GenerateKeyManager.getSecretKey(senderId + ".key");

        // 정산요청 정보 암호화
        byte[] encryptedData = EncryptManager.encrypt(byteData, secretKey);
        if(encryptedData.length==0){
            throw new CipherException("원문 암호화 중 오류");
        }

        // 전자서명 암호화
        String fileName = senderId + receiverId + "_encryptSignature.bin";
        //암호화한 전자서명 파일에 저장
        EncryptManager.encryptToFile(fileName, signature, secretKey);

        // 암호화에 사용한 대칭키 수신자의 publicKey로 암호화(전자봉투 생성)
        PublicKey receiverPublicKey = null;
        receiverPublicKey = GenerateKeyManager.getPublicKey(receiverId);

        byte[] envelope = EncryptManager.encrypt(secretKey.getEncoded(), receiverPublicKey);
        if(envelope.length==0){
            throw new CipherException("비밀키 공개키로 암호화 중 오류");
        }

        //4. 암호문과 전자봉투 묶어서 저장
        SendData sendData = new SendData(envelope, encryptedData, fileName, senderId);
        fileName = senderId + receiverId + ".data";
        boolean state = FileManager.writeObjectToFile(fileName, sendData);
        if(!state){ //파일 생성 오류(전송 오류)
            throw new FileException("전자봉투 묶어서 저장하는 중 오류");
        }
    }
}
