package com.example.carins.web.dto;

import com.example.carins.model.InsurancePolicy;

import java.time.LocalDate;

public record InsurancePolicyResponse(
        Long id,
        CarDto car,
        String provider,
        LocalDate startDate,
        LocalDate endDate
) {
    public static InsurancePolicyResponse fromEntity(InsurancePolicy policy) {
        if (policy == null) {
            return null;
        }

        return new InsurancePolicyResponse(
                policy.getId(),
                CarDto.fromEntity(policy.getCar()),
                policy.getProvider(),
                policy.getStartDate(),
                policy.getEndDate()
        );
    }
}