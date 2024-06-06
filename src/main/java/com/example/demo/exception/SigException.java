package com.example.demo.exception;

public class SigException extends Exception {
    public SigException(String message, Throwable cause) {
        super(message, cause);
    }

    public SigException(String s) {
        super(s);
    }
}
