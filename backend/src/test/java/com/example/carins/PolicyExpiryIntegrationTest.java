package com.example.carins;

import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.PolicyExpiryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class PolicyExpiryIntegrationTest {

    @Autowired
    private PolicyExpiryService policyExpiryService;

    @Autowired
    private InsurancePolicyRepository policyRepository;

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void scheduledTaskRunsWithoutErrors() {
        try {
            policyExpiryService.checkExpiredPolicies();
            assertTrue(true);
        } catch (Exception e) {
            throw new AssertionError("Scheduled task threw exception: " + e.getMessage(), e);
        }
    }
}