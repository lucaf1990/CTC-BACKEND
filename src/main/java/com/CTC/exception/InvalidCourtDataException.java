package com.CTC.exception;

public class InvalidCourtDataException extends RuntimeException {
    public InvalidCourtDataException(String message) {
        super(message);
    }
}