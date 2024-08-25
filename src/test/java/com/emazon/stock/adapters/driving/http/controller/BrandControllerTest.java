package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driven.jpa.mysql.repository.IBrandRepository;
import com.emazon.stock.adapters.driving.http.dto.request.BrandRequest;
import com.emazon.stock.adapters.driving.http.mapper.request.IBrandRequestMapper;
import com.emazon.stock.domain.api.IBrandServicePort;
import com.emazon.stock.domain.model.Brand;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BrandController.class)
@ExtendWith(MockitoExtension.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBrandServicePort brandServicePort;

    @MockBean
    private IBrandRequestMapper brandRequestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveBrand() throws Exception {
        BrandRequest brandRequest = new BrandRequest("Brand", "Description for brand");
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
        BrandRequest brandRequest = new BrandRequest(longBrandName, "Description for brand");

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest))
        ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_NAME)));
    }

    @Test
    void testSaveBrandWithLongDescriptionShouldFail() throws Exception {
        String longDescription = "This Description Is Way Too Long And Should Trigger A Validation Error Because it exceeds the 120 characters limit that has been imposed on this field.";
        BrandRequest brandRequest = new BrandRequest("Brand", longDescription);

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION)));
    }

    @Test
    void testSaveBrandWithEmptyNameShouldFail() throws Exception {
        BrandRequest brandRequest = new BrandRequest("", "Description for brand");

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_BRAND_NAME_BLANK)));
    }

    @Test
    void testSaveBrandWithEmptyDescriptionShouldFail() throws Exception {
        BrandRequest brandRequest = new BrandRequest("Brand", "");

        mockMvc.perform(post("/brand/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_BRAND_DESCRIPTION_BLANK)));
    }
}