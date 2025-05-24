package com.example.bookstore.exception;

public class RentalRequestNotValidException extends RuntimeException {
    public RentalRequestNotValidException(String message) {
        super(message);
    }
}
