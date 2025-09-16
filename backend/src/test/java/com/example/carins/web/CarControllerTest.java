package com.example.carins.web;

import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarService carService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CarService carService() {
            return mock(CarService.class);
        }
    }

    @Test
    void isInsuranceValid_WithValidParameters_ShouldReturnOk() throws Exception {
        when(carService.isInsuranceValid(eq(1L), any(LocalDate.class))).thenReturn(true);
        when(carService.validateAndParseDate("2025-06-01")).thenReturn(LocalDate.of(2025, 6, 1));

        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2025-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    void isInsuranceValid_WithNonExistingCar_ShouldReturnNotFound() throws Exception {
        when(carService.validateAndParseDate("2025-06-01")).thenReturn(LocalDate.of(2025, 6, 1));
        when(carService.isInsuranceValid(eq(999L), any(LocalDate.class)))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Car with ID 999 not found"));

        mockMvc.perform(get("/api/cars/999/insurance-valid")
                        .param("date", "2025-06-01"))
                .andExpect(status().isNotFound());
    }

    @Test
    void isInsuranceValid_WithInvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        when(carService.validateAndParseDate("2025/06/01"))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "Invalid date format. Please use ISO format YYYY-MM-DD"));

        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2025/06/01")) // Format invalid
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid date format. Please use ISO format YYYY-MM-DD"));
    }

    @Test
    void isInsuranceValid_WithDateTooFarInFuture_ShouldReturnBadRequest() throws Exception {
        when(carService.validateAndParseDate("2040-01-01"))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "Date cannot be more than 10 years in the future"));

        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2040-01-01")) // Prea departe în viitor
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Date cannot be more than 10 years in the future"));
    }
}