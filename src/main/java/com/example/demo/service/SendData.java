package com.example.demo.service;

import java.io.Serializable;

final class SendData implements Serializable{
    static final long serialVersionUID = 1L;
    // 전자봉투
    private byte[] envelope;
    // 암호화한 원문
    private byte[] encryptedData;
    // 전자서명
    private String encryptedSignFileName;
    private String senderId;

    SendData(){}
    SendData(byte[] envelope, byte[] encryptedData, String encryptedSignFileName, String senderId) {
        this.envelope = envelope;
        this.encryptedData = encryptedData;
        this.encryptedSignFileName = encryptedSignFileName;
        this.senderId = senderId;
    }

    String getEncryptedSignFileName() {
        return encryptedSignFileName;
    }
    byte[] getEnvelope() {
        return envelope;
    }
    byte[] getEncryptedData() {
        return encryptedData;
    }
}

