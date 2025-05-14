package com.example.bookstore.exception;

import java.util.List;

public class EntityDeletionException extends RuntimeException {

    public EntityDeletionException(String message) {
        super(message);
    }

    public EntityDeletionException(Long id, String message) {
        super("Error deleting entity with id [%s]. Details: %s".formatted(id, message));
    }

    public EntityDeletionException(String entityName, Long id, List<Long> dependentBookIds) {
        super("Error deleting %s with id [%s]. Details: books with ids %s depend on it".formatted(entityName, id, dependentBookIds));
    }


}
