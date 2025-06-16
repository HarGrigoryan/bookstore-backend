package com.example.bookstore.exception;

public class CouponNotValidException extends RuntimeException {
    public CouponNotValidException(String message) {
        super(message);
    }
}
