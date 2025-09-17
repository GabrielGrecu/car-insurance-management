package com.example.carins.service;

import com.example.carins.model.ExpiredPolicyNotification;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.ExpiredPolicyNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyExpiryServiceTest {

    @Mock
    private ExpiredPolicyNotificationRepository notificationRepository;

    @InjectMocks
    private PolicyExpiryService policyExpiryService;

    private InsurancePolicy testPolicy;

    @BeforeEach
    void setUp() {
        testPolicy = new InsurancePolicy();
        testPolicy.setId(1L);
        testPolicy.setEndDate(LocalDate.now());
    }

    @Test
    void checkAndNotifyPolicy_withExpiredPolicy_shouldReturnTrue() {
        LocalDate today = LocalDate.now();

        when(notificationRepository.isAlreadyNotified(any(), any())).thenReturn(false);
        when(notificationRepository.save(any())).thenReturn(new ExpiredPolicyNotification());

        boolean result = policyExpiryService.checkAndNotifyPolicy(testPolicy, today);

        assertTrue(result);
        verify(notificationRepository, times(1)).isAlreadyNotified(any(), any());
        verify(notificationRepository, times(1)).save(any());
    }

    @Test
    void checkAndNotifyPolicy_withAlreadyNotifiedPolicy_shouldReturnFalse() {
        LocalDate today = LocalDate.now();

        when(notificationRepository.isAlreadyNotified(any(), any())).thenReturn(true);

        boolean result = policyExpiryService.checkAndNotifyPolicy(testPolicy, today);

        assertFalse(result);
        verify(notificationRepository, times(1)).isAlreadyNotified(any(), any());
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void checkExpiredPolicies_withNoExpiredPolicies_shouldNotNotify() {
        LocalDate today = LocalDate.now();

        when(notificationRepository.findExpiredPolicies(any())).thenReturn(Collections.emptyList());

        policyExpiryService.checkExpiredPolicies();

        verify(notificationRepository, times(1)).findExpiredPolicies(any());
        verify(notificationRepository, never()).isAlreadyNotified(any(), any());
        verify(notificationRepository, never()).save(any());
    }
}