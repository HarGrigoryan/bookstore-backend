package com.example.bookstore.exception;

public class BookInstanceNotAvailable extends RuntimeException {
    public BookInstanceNotAvailable(String message) {
        super(message);
    }
}
