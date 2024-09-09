package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.CategoryRequest;
import com.emazon.stock.adapters.driving.http.dto.response.CategoryResponse;
import com.emazon.stock.adapters.driving.http.dto.response.PaginationInfoResponse;
import com.emazon.stock.adapters.driving.http.mapper.request.ICategoryRequestMapper;
import com.emazon.stock.adapters.driving.http.mapper.response.ICategoryResponseMapper;
import com.emazon.stock.configuration.exceptionhandler.ExceptionResponse;
import com.emazon.stock.configuration.exceptionhandler.ValidationExceptionResponse;
import com.emazon.stock.domain.api.ICategoryServicePort;
import com.emazon.stock.domain.model.Category;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final ICategoryServicePort categoryServicePort;
    private final ICategoryRequestMapper categoryRequestMapper;
    private final ICategoryResponseMapper categoryResponseMapper;

    @Operation(summary = "Create a new category", description = "This endpoint allows you to create a new category")
    @ApiResponse(responseCode = "201", description = "Category created correctly", content = @Content)
    @ApiResponse(responseCode = "400", description = "Incorrect category creation request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Void> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        categoryServicePort.saveCategory(categoryRequestMapper.toCategory(categoryRequest));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List all categories with pagination and sorting", description = "This endpoint allows you to list all available categories. Query parameters can be used to control the pagination and order of the results.")
    @ApiResponse(responseCode = "200", description = "Correctly listed categories", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaginationInfoResponse.class))})
    @ApiResponse(responseCode = "404", description = "No categories found for listing", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
    @ApiResponse(responseCode = "400", description = "Solicitud inválida debido a parámetros incorrectos. Verifique que los parámetros enviados cumplan con el formato esperado y sean válidos.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationExceptionResponse.class))})
    @GetMapping("/all")
    public ResponseEntity<PaginationInfoResponse<CategoryResponse>> getAllCategories(
            @Parameter(description = "Page number to list", example = "0")
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_VALUE_NUMBER_PAGE) @Min(value = Constants.MIN_VALUE_PAGE, message = Constants.EXCEPTION_MIN_VALUE_PAGE) int page,
            @Parameter(description = "Number of categories by page", example = "10")
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_VALUE_SIZE_PAGE) @Min(value = Constants.MIN_VALUES_PER_PAGE, message = Constants.EXCEPTION_MIN_VALUES_PER_PAGE) int size,
            @Parameter(description = "Sort order (e.g. 'ASC' for ascending or 'DESC' for descending)", example = "ASC")
            @RequestParam(value = "order", defaultValue = Constants.ORDER_ASC) @Pattern(regexp = Constants.REGEX_ORDER, message = Constants.EXCEPTION_REGEX_ORDER) String order
    ){
        PaginationInfo<Category> categories = categoryServicePort.getAllCategories(page, size, order);
        return ResponseEntity.ok(new PaginationInfoResponse<>(categoryResponseMapper.toPaginationInfo(categories)));
    }

}