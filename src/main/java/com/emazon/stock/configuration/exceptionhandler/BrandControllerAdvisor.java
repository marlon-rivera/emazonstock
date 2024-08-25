package com.emazon.stock.configuration.exceptionhandler;

import com.emazon.stock.domain.exception.brand.*;
import com.emazon.stock.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class BrandControllerAdvisor {

    @ExceptionHandler(BrandNameBlankException.class)
    public ResponseEntity<ExceptionResponse> handleBrandNameBlankException(BrandNameBlankException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_BRAND_NAME_BLANK, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(BrandDescriptionBlankException.class)
    public ResponseEntity<ExceptionResponse> handleBrandDescriptionBlankException(BrandDescriptionBlankException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_BRAND_DESCRIPTION_BLANK, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(BrandAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleBrandAlreadyExistsException(BrandAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_BRAND_ALREADY_EXISTS, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(BrandMaximumNumberCharactersNameException.class)
    public ResponseEntity<ExceptionResponse> handleBrandMaximumNumberCharactersNameException(BrandMaximumNumberCharactersNameException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_NAME, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(BrandMaximumNumberCharactersDescriptionException.class)
    public ResponseEntity<ExceptionResponse> handleBrandMaximumNumberCharactersDescriptionException(BrandMaximumNumberCharactersDescriptionException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

}
