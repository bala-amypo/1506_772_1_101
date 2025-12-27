package com.example.demo.exception;

public class StockRecordAlreadyExistsException extends RuntimeException {
    public StockRecordAlreadyExistsException(String message) {
        super(message);
    }
}