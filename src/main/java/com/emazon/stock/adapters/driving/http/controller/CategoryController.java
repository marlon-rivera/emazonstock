package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.CategoryRequest;
import com.emazon.stock.adapters.driving.http.mapper.request.ICategoryRequestMapper;
import com.emazon.stock.configuration.exceptionhandler.ExceptionResponse;
import com.emazon.stock.domain.api.ICategoryServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryServicePort categoryServicePort;
    private final ICategoryRequestMapper categoryRequestMapper;

    @Operation(summary = "Create a new category", description = "This endpoint allows you to create a new category")
    @ApiResponse(responseCode = "201", description = "Category created correctly", content = @Content)
    @ApiResponse(responseCode = "400", description = "Incorrect category creation request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
    @PostMapping("/")
    public ResponseEntity<Void> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        categoryServicePort.saveCategory(categoryRequestMapper.toCategory(categoryRequest));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}