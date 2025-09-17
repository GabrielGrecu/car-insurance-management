package com.example.carins.service;

import com.example.carins.exception.NotFoundException;
import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.CarHistoryResponse;
import com.example.carins.web.dto.ClaimRequest;
import com.example.carins.web.dto.ClaimResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CarHistoryService {

    private final ClaimRepository claimRepository;
    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;

    public CarHistoryService(ClaimRepository claimRepository, CarRepository carRepository,
                             InsurancePolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
    }

    public CarHistoryResponse getCarHistory(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException("Car with ID " + carId + " not found."));

        List<CarHistoryResponse.HistoryEvent> events = new ArrayList<>();

        List<InsurancePolicy> policies = policyRepository.findByCarId(carId);
        for (InsurancePolicy policy : policies) {
            events.add(new CarHistoryResponse.HistoryEvent(
                    "INSURANCE_POLICY",
                    policy.getStartDate(),
                    "Insurance policy with " + policy.getProvider(),
                    Map.of(
                            "provider", policy.getProvider(),
                            "startDate", policy.getStartDate(),
                            "endDate", policy.getEndDate()
                    )
            ));
        }

        List<Claim> claims = claimRepository.findByCarIdOrderByClaimDateDesc(carId);
        for (Claim claim : claims) {
            events.add(new CarHistoryResponse.HistoryEvent(
                    "CLAIM",
                    claim.getClaimDate(),
                    "Insurance claim: " + claim.getDescription(),
                    Map.of(
                            "description", claim.getDescription(),
                            "amount", claim.getAmount()
                    )
            ));
        }

        events.sort(Comparator.comparing(CarHistoryResponse.HistoryEvent::date).reversed());

        return new CarHistoryResponse(carId, events);
    }

    @Transactional
    public ClaimResponse registerClaim(Long carId, ClaimRequest claimRequest) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException("Car with ID " + carId + " not found."));

        Claim claim = new Claim();
        claim.setClaimDate(claimRequest.claimDate());
        claim.setDescription(claimRequest.description());
        claim.setAmount(claimRequest.amount());
        claim.setCar(car);

        Claim savedClaim = claimRepository.save(claim);

        return ClaimResponse.fromEntity(savedClaim);
    }
}