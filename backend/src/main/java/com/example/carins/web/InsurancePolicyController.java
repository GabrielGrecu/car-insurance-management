package com.example.carins.web;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
public class InsurancePolicyController {

    private final InsurancePolicyRepository policyRepository;

    public InsurancePolicyController(InsurancePolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyResponse> updatePolicy(@PathVariable Long id, @Valid @RequestBody InsurancePolicy policy) {
        if (!policyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        policy.setId(id);
        InsurancePolicy updatedPolicy = policyRepository.save(policy);

        InsurancePolicy fullPolicy = policyRepository.findById(updatedPolicy.getId())
                .orElseThrow(() -> new RuntimeException("Policy not found after update"));

        return ResponseEntity.ok(InsurancePolicyResponse.fromEntity(fullPolicy));
    }

    @PostMapping
    public ResponseEntity<InsurancePolicyResponse> createPolicy(@Valid @RequestBody InsurancePolicy policy) {
        InsurancePolicy savedPolicy = policyRepository.save(policy);

        InsurancePolicy fullPolicy = policyRepository.findWithDetailsById(savedPolicy.getId())
                .orElseThrow(() -> new RuntimeException("Policy not found after save"));

        return ResponseEntity.ok(InsurancePolicyResponse.fromEntity(fullPolicy));
    }
}