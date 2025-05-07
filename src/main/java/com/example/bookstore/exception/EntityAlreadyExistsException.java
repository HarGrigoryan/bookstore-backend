package com.example.bookstore.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String entityName, Long id) {
        super("%s with id '%s' already exists".formatted(entityName, id));
    }
}
