package com.example.carins.web.dto;

import java.time.LocalDate;
import java.util.List;

public record CarHistoryResponse(
        Long carId,
        List<HistoryEvent> events
) {
    public record HistoryEvent(
            String type,
            LocalDate date,
            String description,
            Object details
    ) {}
}