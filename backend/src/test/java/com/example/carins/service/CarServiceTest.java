package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.Owner;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

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

    private Car testCar;
    private Owner testOwner;

    @BeforeEach
    void setUp() {
        testOwner = new Owner("Test Owner", "test@endava.com");
        testOwner.setId(1L);

        testCar = new Car("VIN123", "Dacia", "Logan", 2020, testOwner);
        testCar.setId(1L);
    }

    @Test
    void listCars_shouldReturnAllCars() {
        when(carRepository.findAll()).thenReturn(List.of(testCar));

        List<Car> result = carService.listCars();

        assertEquals(1, result.size());
        assertEquals("VIN123", result.get(0).getVin());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void isInsuranceValid_withValidCarAndDate_shouldReturnTrue() {
        Long carId = 1L;
        LocalDate date = LocalDate.now();

        when(carRepository.existsById(carId)).thenReturn(true);
        when(policyRepository.existsActiveOnDate(carId, date)).thenReturn(true);

        boolean result = carService.isInsuranceValid(carId, date);

        assertTrue(result);
        verify(carRepository, times(1)).existsById(carId);
        verify(policyRepository, times(1)).existsActiveOnDate(carId, date);
    }

    @Test
    void isInsuranceValid_withNonExistentCar_shouldThrowException() {
        Long carId = 999L;
        LocalDate date = LocalDate.now();

        when(carRepository.existsById(carId)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () ->
                carService.isInsuranceValid(carId, date));

        verify(carRepository, times(1)).existsById(carId);
        verify(policyRepository, never()).existsActiveOnDate(any(), any());
    }

    @Test
    void validateAndParseDate_withValidDate_shouldReturnDate() {
        String validDate = "2024-01-15";

        LocalDate result = carService.validateAndParseDate(validDate);

        assertEquals(LocalDate.of(2024, 1, 15), result);
    }

    @Test
    void validateAndParseDate_withInvalidDate_shouldThrowException() {
        String invalidDate = "invalid-date";

        assertThrows(ResponseStatusException.class, () ->
                carService.validateAndParseDate(invalidDate));
    }

    @Test
    void validateAndParseDate_withNullDate_shouldThrowException() {
        assertThrows(ResponseStatusException.class, () ->
                carService.validateAndParseDate(null));
    }

    @Test
    void validateAndParseDate_withDateBefore2000_shouldThrowException() {
        String oldDate = "1999-12-31";

        assertThrows(ResponseStatusException.class, () ->
                carService.validateAndParseDate(oldDate));
    }
}