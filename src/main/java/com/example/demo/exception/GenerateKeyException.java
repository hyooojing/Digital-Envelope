package com.example.demo.exception;

public class GenerateKeyException extends Exception {
    public GenerateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    // KeySaveException 예외메세지 + 추가메시지를 포함
    public GenerateKeyException(String additionalMessage, KeySaveException cause) {
        super(additionalMessage + ": " + cause.getMessage(), cause);
    }
}
