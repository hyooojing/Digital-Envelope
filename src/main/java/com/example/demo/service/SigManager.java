package com.example.demo.service;
import com.example.demo.exception.KeyLoadException;
import com.example.demo.exception.SigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class SigManager {
    @Autowired
    private FileManager fileManager;
    private final String signAlgorithm ="SHA256withRSA";

    // 전자서명 생성
    public static byte[] sign(byte[] byteData, PrivateKey privateKey, String signFile) throws SigException, KeyLoadException {
        byte[] signature;
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(privateKey);
            sig.update(byteData);
            signature = sig.sign();
        } catch (SignatureException | NoSuchAlgorithmException e) {
            throw new SigException("정산정보의 전자서명 생성 과정 중 요류가 발생했습니다.");
        } catch (InvalidKeyException e) {
            throw new KeyLoadException();
        }

        //생성된 서명 출력
        FileManager.writeObjectToFile(signFile, signature);
        return signature;
    }

    //무결성 검증
    public static boolean verify(String plainData, PublicKey key, byte[] sign) throws SigException {
        Signature sig = null;
        try {
            sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(key);
            sig.update(plainData.getBytes());
            return sig.verify(sign);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new SigException("요청정보의 무결성 검증에 실패했습니다.");
        }
    }
}
