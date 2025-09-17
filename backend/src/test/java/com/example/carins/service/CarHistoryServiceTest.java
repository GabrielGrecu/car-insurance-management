package com.example.carins.service;

import com.example.carins.exception.NotFoundException;
import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.model.Owner;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.CarHistoryResponse;
import com.example.carins.web.dto.ClaimRequest;
import com.example.carins.web.dto.ClaimResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarHistoryServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private InsurancePolicyRepository policyRepository;

    @InjectMocks
    private CarHistoryService carHistoryService;

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
    void getCarHistory_withExistingCar_shouldReturnHistory() {
        Long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.of(testCar));
        when(policyRepository.findByCarId(carId)).thenReturn(List.of());
        when(claimRepository.findByCarIdOrderByClaimDateDesc(carId)).thenReturn(List.of());

        CarHistoryResponse result = carHistoryService.getCarHistory(carId);

        assertNotNull(result);
        assertEquals(carId, result.carId());
        assertTrue(result.events().isEmpty());
        verify(carRepository, times(1)).findById(carId);
        verify(policyRepository, times(1)).findByCarId(carId);
        verify(claimRepository, times(1)).findByCarIdOrderByClaimDateDesc(carId);
    }

    @Test
    void getCarHistory_withNonExistentCar_shouldThrowException() {
        Long carId = 999L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                carHistoryService.getCarHistory(carId));

        verify(carRepository, times(1)).findById(carId);
        verify(policyRepository, never()).findByCarId(any());
        verify(claimRepository, never()).findByCarIdOrderByClaimDateDesc(any());
    }

    @Test
    void registerClaim_withValidRequest_shouldReturnClaimResponse() {
        Long carId = 1L;
        ClaimRequest claimRequest = new ClaimRequest(
                LocalDate.now(),
                "Test claim",
                new BigDecimal("100.50")
        );

        when(carRepository.findById(carId)).thenReturn(Optional.of(testCar));
        when(claimRepository.save(any())).thenAnswer(invocation -> {
            Claim claim = invocation.getArgument(0);
            claim.setId(1L);
            return claim;
        });

        ClaimResponse result = carHistoryService.registerClaim(carId, claimRequest);

        assertNotNull(result);
        assertEquals("Test claim", result.description());
        verify(carRepository, times(1)).findById(carId);
        verify(claimRepository, times(1)).save(any());
    }
}