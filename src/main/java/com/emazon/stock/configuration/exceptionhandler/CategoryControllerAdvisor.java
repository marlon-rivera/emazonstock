package com.emazon.stock.configuration.exceptionhandler;

import com.emazon.stock.domain.exception.category.*;
import com.emazon.stock.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CategoryControllerAdvisor {

    @ExceptionHandler(CategoryNameBlankException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryNameBlankException(CategoryNameBlankException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_CATEGORY_NAME_BLANK, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(CategoryDescriptionBlankException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryDescriptionBlankException(CategoryDescriptionBlankException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_CATEGORY_DESCRIPTION_BLANK, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_CATEGORY_ALREADY_EXISTS, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(CategoryMaximumNumberCharactersNameException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryMaximumNumberCharactersNameException(CategoryMaximumNumberCharactersNameException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_NAME, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(CategoryMaximumNumberCharactersDescriptionException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryMaximumNumberCharactersDescriptionException(CategoryMaximumNumberCharactersDescriptionException ex) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(CategoryNoDataFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryNoDataFoundException(CategoryNoDataFoundException ex){
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_CATEGORY_NO_DATA_FOUND, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }
}