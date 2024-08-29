package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.BrandRequest;
import com.emazon.stock.adapters.driving.http.dto.response.BrandResponse;
import com.emazon.stock.adapters.driving.http.dto.response.PaginationInfoResponse;
import com.emazon.stock.adapters.driving.http.mapper.request.IBrandRequestMapper;
import com.emazon.stock.adapters.driving.http.mapper.response.IBrandResponseMapper;
import com.emazon.stock.domain.api.IBrandServicePort;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BrandController.class)
@ExtendWith(MockitoExtension.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBrandServicePort brandServicePort;

    @MockBean
    private IBrandRequestMapper brandRequestMapper;

    @MockBean
    IBrandResponseMapper brandResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveBrand() throws Exception {
        BrandRequest brandRequest = new BrandRequest(1L, "Brand", "Description for brand");
        Brand brand = new Brand(null, "Brand", "Description for brand");

        when(brandRequestMapper.toBrand(any(BrandRequest.class))).thenReturn(brand);
        doNothing().when(brandServicePort).saveBrand(any(Brand.class));

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest))
        ).andExpect(status().isCreated());

        verify(brandRequestMapper).toBrand(any(BrandRequest.class));
        verify(brandServicePort).saveBrand(any(Brand.class));
    }

    @Test
    void testSaveBrandWithLongNameShouldFail() throws Exception {
        String longBrandName = "ThisBrandNameIsWayTooLongAndShouldFailCauseAValidationError";
        BrandRequest brandRequest = new BrandRequest(1L, longBrandName, "Description for brand");

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest))
        ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_NAME)));
    }

    @Test
    void testSaveBrandWithLongDescriptionShouldFail() throws Exception {
        String longDescription = "This Description Is Way Too Long And Should Trigger A Validation Error Because it exceeds the 120 characters limit that has been imposed on this field.";
        BrandRequest brandRequest = new BrandRequest(1L, "Brand", longDescription);

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION)));
    }

    @Test
    void testSaveBrandWithEmptyNameShouldFail() throws Exception {
        BrandRequest brandRequest = new BrandRequest(1L, "", "Description for brand");

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_BRAND_NAME_BLANK)));
    }

    @Test
    void testSaveBrandWithEmptyDescriptionShouldFail() throws Exception {
        BrandRequest brandRequest = new BrandRequest(1L, "Brand", "");

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_BRAND_DESCRIPTION_BLANK)));
    }

    @Test
    void testGetAllBrands() throws Exception{
        PaginationInfo<Brand> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(brandServicePort.getAllBrands(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        PaginationInfoResponse<BrandResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(brandResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/brand/all")
                        .param("page", "0")
                        .param("size", "10")
                        .param("order", "ASC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllBrandsWithIncorrectParamPageShouldFail() throws Exception {
        PaginationInfo<Brand> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(brandServicePort.getAllBrands(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);
        PaginationInfoResponse<BrandResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(brandResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/brand/all")
                .param("page", "-10")
                .param("size", "10")
                .param("order", "ASC")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_MIN_VALUE_PAGE)));
    }

    @Test
    void testGetAllBrandsWithIncorrectParamSizeShouldFail() throws Exception {
        PaginationInfo<Brand> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(brandServicePort.getAllBrands(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);
        PaginationInfoResponse<BrandResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(brandResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/brand/all")
                .param("page", "0")
                .param("size", "-10")
                .param("order", "ASC"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_MIN_VALUES_PER_PAGE)));
    }

    @Test
    void testGetAllBrandsWithIncorrectParamOrderShouldFail() throws Exception {
        PaginationInfo<Brand> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(brandServicePort.getAllBrands(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);
        PaginationInfoResponse<BrandResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(brandResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/brand/all")
                .param("page", "0")
                .param("size", "1")
                .param("order", "incorrectorder"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_REGEX_ORDER));
    }
}