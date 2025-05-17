package com.example.bookstore.exception;

import jakarta.servlet.ServletException;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookInstanceNotAvailable.class)
    public ResponseEntity<ExceptionResponse> handleBookInstanceIsNotAvailable(BookInstanceNotAvailable e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withMessage(e.getMessage())
                .build();
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ExceptionResponse> handlePaymentFailedException(PaymentFailedException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withMessage(e.getMessage())
                .build();
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidTokenException(InvalidTokenException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.NOT_FOUND)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(EntityDeletionException.class)
    public ResponseEntity<ExceptionResponse> handleEntityDeletionException(EntityDeletionException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.CONFLICT)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(ResourceAlreadyUsedException.class)
    public ResponseEntity<ExceptionResponse> handleResourceAlreadyUsedException(ResourceAlreadyUsedException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.CONFLICT)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final ExceptionResponse exceptionResponse = this.getBadRequestResponseBuilder(ex.getBindingResult())
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    private ExceptionResponse.ExceptionResponseBuilder getBadRequestResponseBuilder(BindingResult result) {
        final String message = result.getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> String.join(" ", StringUtils.capitalize(fieldError.getField()), fieldError.getDefaultMessage()))
                .orElse(null);
        return ExceptionResponse.builder()
                .withMessage(message)
                .withStatus(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ExceptionResponse> handleException(ServletException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(RentalRequestNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleRentalRequestNotValidException(RentalRequestNotValidException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withMessage(e.getMessage())
                .withStatus(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(PathElementException.class)
    public ResponseEntity<ExceptionResponse> handlePathElementException(PathElementException e) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage(e.getMessage())
                .build();
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }

}
