package com.example.carins.web;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.ExpiredPolicyNotificationRepository;
import com.example.carins.web.dto.InsurancePolicyResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ExpiredInsurancePolicyController {
    private final ExpiredPolicyNotificationRepository notificationRepository;

    public ExpiredInsurancePolicyController(ExpiredPolicyNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/policies/expired")
    public List<InsurancePolicyResponse> getExpiredPolicies() {
        LocalDate today = LocalDate.now();
        List<InsurancePolicy> expiredPolicies = notificationRepository.findAllExpiredPolicies(today);

        return expiredPolicies.stream()
                .map(InsurancePolicyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/policies/expired/today")
    public List<InsurancePolicyResponse> getPoliciesExpiringToday() {
        LocalDate today = LocalDate.now();
        List<InsurancePolicy> expiredPolicies = notificationRepository.findExpiredPolicies(today);

        return expiredPolicies.stream()
                .map(InsurancePolicyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/policies/expired/range")
    public List<InsurancePolicyResponse> getPoliciesExpiringInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<InsurancePolicy> expiredPolicies = notificationRepository.findPoliciesExpiringBetween(startDate, endDate);

        return expiredPolicies.stream()
                .map(InsurancePolicyResponse::fromEntity)
                .collect(Collectors.toList());
    }
}