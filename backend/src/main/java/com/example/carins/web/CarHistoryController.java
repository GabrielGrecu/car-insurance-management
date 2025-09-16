package com.example.carins.web;

import com.example.carins.service.CarHistoryService;
import com.example.carins.web.dto.CarHistoryResponse;
import com.example.carins.web.dto.ClaimRequest;
import com.example.carins.web.dto.ClaimResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/cars")
public class CarHistoryController {

    private final CarHistoryService service;

    public CarHistoryController(CarHistoryService service) {
        this.service = service;
    }

    @PostMapping("/{carId}/claims")
    public ResponseEntity<ClaimResponse> registerClaim(
            @PathVariable Long carId,
            @Valid @RequestBody ClaimRequest claimRequest) {

        ClaimResponse savedClaim = service.registerClaim(carId, claimRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedClaim.id())
                .toUri();

        return ResponseEntity.created(location).body(savedClaim);
    }

    @GetMapping("/{carId}/history")
    public ResponseEntity<CarHistoryResponse> getCarHistory(@PathVariable Long carId) {
        CarHistoryResponse history = service.getCarHistory(carId);
        return ResponseEntity.ok(history);
    }
}