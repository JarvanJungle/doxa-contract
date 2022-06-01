package org.doxa.contract.controllers;

import java.util.ArrayList;
import java.util.List;

import org.doxa.contract.exceptions.*;
import org.doxa.contract.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            details.add(fieldName + " " +  error.getDefaultMessage());
        }
        ApiResponse error = new ApiResponse();
        error.setMessage("Validation Failed");
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setData(details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateObjectException.class)
    public ResponseEntity<ApiResponse> handleDuplicatedObject(DuplicateObjectException ex) {
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectDoesNotExistException.class)
    public ResponseEntity<ApiResponse> handleObjectNotFound(ObjectDoesNotExistException ex) {
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ObjectExistException.class)
    public ResponseEntity<ApiResponse> handleObjectNotFound(ObjectExistException ex) {
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex) {
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleDataDuplicated(EntityAlreadyExistsException ex) {
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException ex) {
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApproveException.class)
    public ResponseEntity<ApiResponse> handleApproveException(ApproveException ex) {
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnexpectedException(Exception ex) {
        ApiResponse error = new ApiResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessage(ex.getLocalizedMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
