package com.example.demo.controller;

import com.example.demo.exception.*;
import com.example.demo.service.CheckDataService;
import com.example.demo.service.GenerateKeyManager;
import com.example.demo.service.SendDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/SecurityProject")
public class MainController {
    @Autowired
    GenerateKeyManager generateKeyManager;
    @Autowired
    SendDataService sendDataService;
    @Autowired
    CheckDataService checkDataService;

    // 키 생성 및 저장
    @PostMapping("/generateKey")
    public String generateKey(@RequestParam("userId") String userId) throws KeySaveException {
        try {
            // 비밀키 생성
            generateKeyManager.createSecretKey(userId);
            // 키페어 생성
            generateKeyManager.createKeyPair(userId);
        } catch (NoSuchAlgorithmException e) {
            throw new KeySaveException("사용자의 키를 생성하는데 실패했습니다.", e);
        }

        return userId + "의 키를 생성하였습니다.";
    }

    // 전자봉투 생성
    @PostMapping("/encryptedMsg")
    public String encryptedMsg(@RequestParam("sender") String sender,
                               @RequestParam("receiver") String receiver,
                               @RequestParam("message") String msg) {
        try {
            // 전자서명 및 암호화
            sendDataService.sendMessage(sender, receiver, msg);
        } catch (CipherException | SigException | FileException | UnsupportedEncodingException | KeyLoadException |
                 NoSuchAlgorithmException | SignatureException | ClassNotFoundException e) {
            return e.getMessage();
        }
        return "정산 요청 정보를 성공적으로 암호화했습니다.";
    }

    // 전자봉투 검증
    @PostMapping("/decryptedMsg")
    public Map<String, Object> decryptedMsg(@RequestParam("sender") String sender,
                               @RequestParam("receiver") String receiver) {
        String message;
        boolean rslt = true;
        Map<String, Object> response = new HashMap<>();
        try {
            // 복호화
            message = checkDataService.decryptedData(sender, receiver);
            System.out.println("정산 정보 " + message);
            // 얻은 요청 정보 원문을 포함하여 검증값 얻기
            rslt = checkDataService.verifyData(message, sender);
            System.out.println("검증결과 " + rslt);

        } catch (SigException | FileException | KeyLoadException e) {
            response.put("message", e.getMessage());
            response.put("signature", false);
            return response;
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }
        // 복호화 데이터와 검증 결과를 화면에 출력
        response.put("message", message);
        response.put("signature", rslt);
        return response;
    }
}
