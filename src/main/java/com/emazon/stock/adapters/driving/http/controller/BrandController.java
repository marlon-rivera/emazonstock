package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.BrandRequest;
import com.emazon.stock.adapters.driving.http.mapper.request.IBrandRequestMapper;
import com.emazon.stock.configuration.exceptionhandler.ExceptionResponse;
import com.emazon.stock.domain.api.IBrandServicePort;
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
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final IBrandServicePort brandServicePort;
    private final IBrandRequestMapper brandRequestMapper;

    @Operation(summary = "Create a new brand", description = "This endpoint allows you to create a new brand.")
    @ApiResponse(responseCode = "201", description = "Brand created correctly.", content = @Content)
    @ApiResponse(responseCode = "400", description = "Incorrect brand creation request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
    @PostMapping("/")
    public ResponseEntity<Void> saveBrand(@Valid @RequestBody BrandRequest brandRequest) {
        brandServicePort.saveBrand(brandRequestMapper.toBrand(brandRequest));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
