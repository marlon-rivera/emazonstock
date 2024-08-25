package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.BrandRequest;
import com.emazon.stock.adapters.driving.http.dto.response.BrandResponse;
import com.emazon.stock.adapters.driving.http.dto.response.PaginationInfoResponse;
import com.emazon.stock.adapters.driving.http.mapper.request.IBrandRequestMapper;
import com.emazon.stock.adapters.driving.http.mapper.response.IBrandResponseMapper;
import com.emazon.stock.configuration.exceptionhandler.ExceptionResponse;
import com.emazon.stock.configuration.exceptionhandler.ValidationExceptionResponse;
import com.emazon.stock.domain.api.IBrandServicePort;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@Validated
public class BrandController {

    private final IBrandServicePort brandServicePort;
    private final IBrandRequestMapper brandRequestMapper;
    private final IBrandResponseMapper brandResponseMapper;

    @Operation(summary = "Create a new brand", description = "This endpoint allows you to create a new brand.")
    @ApiResponse(responseCode = "201", description = "Brand created correctly.", content = @Content)
    @ApiResponse(responseCode = "400", description = "Incorrect brand creation request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
    @PostMapping("/")
    public ResponseEntity<Void> saveBrand(@Valid @RequestBody BrandRequest brandRequest) {
        brandServicePort.saveBrand(brandRequestMapper.toBrand(brandRequest));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List all brands with pagination and sorting", description = "This endpoint allows you to list all availables brands. Query parameters can be used to control the pagination and order of the results.")
    @ApiResponse(responseCode = "200", description = "Correctly listed brands", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaginationInfoResponse.class))})
    @ApiResponse(responseCode = "404", description = "No brands found for listing", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
    @ApiResponse(responseCode = "400", description = "Invalid request due to incorrect parameters. Verify that the parameters sent comply with the expected format and are valid.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationExceptionResponse.class))})
    @GetMapping("/all")
    public ResponseEntity<PaginationInfoResponse<BrandResponse>> getAllBrands(
            @Parameter(description = "Page number to list", example = "0")
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_VALUE_NUMBER_PAGE) @Min(value = Constants.MIN_VALUE_PAGE, message = Constants.EXCEPTION_MIN_VALUE_PAGE) int page,
            @Parameter(description = "Number of categories by page", example = "10")
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_VALUE_SIZE_PAGE) @Min(value = Constants.MIN_VALUES_PER_PAGE, message = Constants.EXCEPTION_MIN_VALUES_PER_PAGE) int size,
            @Parameter(description = "Sort order (e.g. 'ASC' for ascending or 'DESC' for descending)", example = "ASC")
            @RequestParam(value = "order", defaultValue = Constants.ORDER_ASC) @Pattern(regexp = Constants.REGEX_ORDER, message = Constants.EXCEPTION_REGEX_ORDER) String order
    ){
        PaginationInfo<Brand> brands = brandServicePort.getAllBrands(page, size, order);
        return ResponseEntity.ok(brandResponseMapper.toPaginationInfoResponse(brands));
    }

}
