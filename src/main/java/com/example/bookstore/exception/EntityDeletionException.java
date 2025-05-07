package com.example.bookstore.exception;

public class EntityDeletionException extends RuntimeException {
    public EntityDeletionException(String message) {
        super(message);
    }

    public EntityDeletionException(Long id, String message) {
        super("Error deleting Entity with id " + id + ". Details: "+ message);
    }
}
