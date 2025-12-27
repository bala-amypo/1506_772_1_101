package com.example.demo.exception;

public class InvalidConsumptionDateException extends RuntimeException {
    public InvalidConsumptionDateException(String message) {
        super(message);
    }
}