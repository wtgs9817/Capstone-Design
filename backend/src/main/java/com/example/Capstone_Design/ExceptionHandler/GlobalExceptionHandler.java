package com.example.Capstone_Design.ExceptionHandler;

import com.example.Capstone_Design.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("해당 사용자가 존재하지 않습니다."));
    }

    @ExceptionHandler(MajorCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMajorNotFound(MajorCodeNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("학과코드가 존재하지 않습니다."));
    }


}
