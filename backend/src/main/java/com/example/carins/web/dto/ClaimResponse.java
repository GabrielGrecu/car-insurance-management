package com.example.carins.web.dto;

import com.example.carins.model.Claim;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClaimResponse(
        Long id,
        LocalDate claimDate,
        String description,
        BigDecimal amount,
        Long carId
) {
    public static ClaimResponse fromEntity(Claim claim) {
        return new ClaimResponse(
                claim.getId(),
                claim.getClaimDate(),
                claim.getDescription(),
                claim.getAmount(),
                claim.getCar() != null ? claim.getCar().getId() : null
        );
    }
}