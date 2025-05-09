package com.example.bookstore.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String entityName, String... usedResources) {
        super("%s with %s already exists".formatted(
                entityName,
                String.join(" and ", usedResources)
        ));
    }
}
