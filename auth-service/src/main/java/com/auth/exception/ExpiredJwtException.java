package com.auth.exception;

public class ExpiredJwtException extends RuntimeException{
    public ExpiredJwtException(String message) {
        super(message);
    }
}
