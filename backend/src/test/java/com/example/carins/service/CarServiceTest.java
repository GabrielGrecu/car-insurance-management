package com.example.carins.service;

import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private InsurancePolicyRepository policyRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void validateAndParseDate_WithValidDate_ShouldReturnDate() {
        LocalDate result = carService.validateAndParseDate("2025-06-01");
        assertEquals(LocalDate.of(2025, 6, 1), result);
    }

    @Test
    void validateAndParseDate_WithInvalidFormat_ShouldThrowException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            carService.validateAndParseDate("2025/06/01");
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    void validateAndParseDate_WithNullDate_ShouldThrowException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            carService.validateAndParseDate(null);
        });
        assertTrue(exception.getMessage().contains("Date parameter is required"));
    }

    @Test
    void validateAndParseDate_WithEmptyDate_ShouldThrowException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            carService.validateAndParseDate("");
        });
        assertTrue(exception.getMessage().contains("Date parameter is required"));
    }

    @Test
    void validateAndParseDate_WithDateTooFarInPast_ShouldThrowException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            carService.validateAndParseDate("1999-01-01");
        });
        assertTrue(exception.getMessage().contains("Date cannot be before"));
    }

    @Test
    void validateAndParseDate_WithDateTooFarInFuture_ShouldThrowException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            carService.validateAndParseDate("2040-01-01");
        });
        assertTrue(exception.getMessage().contains("Date cannot be more than 10 years in the future"));
    }

    @Test
    void isInsuranceValid_WithNonExistingCar_ShouldThrowNotFoundException() {
        when(carRepository.existsById(999L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            carService.isInsuranceValid(999L, LocalDate.now());
        });
        assertTrue(exception.getMessage().contains("not found"));
    }
}