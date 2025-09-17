package com.example.carins.service;

import com.example.carins.model.ExpiredPolicyNotification;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.ExpiredPolicyNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PolicyExpiryService {

    private static final Logger log = LoggerFactory.getLogger(PolicyExpiryService.class);

    private final ExpiredPolicyNotificationRepository notificationRepository;

    public PolicyExpiryService(ExpiredPolicyNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    private boolean shouldNotifyForPolicy(InsurancePolicy policy, LocalDate today) {
        return policy.getEndDate().isEqual(today) &&
                !notificationRepository.isAlreadyNotified(policy.getId(), today);
    }

    private void notifyExpiredPolicy(InsurancePolicy policy, LocalDate today) {
        try {
            log.warn("Policy {} for car {} expired on {}",
                    policy.getId(),
                    policy.getCar() != null ? policy.getCar().getId() : "unknown",
                    policy.getEndDate());

            ExpiredPolicyNotification notification = new ExpiredPolicyNotification(
                    policy.getId(), today);
            notificationRepository.save(notification);

            log.info("Notification saved for policy {} expired on {}", policy.getId(), today);

        } catch (Exception e) {
            log.error("Failed to process expired policy notification for policy {}: {}",
                    policy.getId(), e.getMessage());
        }
    }

    public boolean checkAndNotifyPolicy(InsurancePolicy policy, LocalDate checkDate) {
        if (shouldNotifyForPolicy(policy, checkDate)) {
            notifyExpiredPolicy(policy, checkDate);
            return true;
        }
        return false;
    }

    @Scheduled(initialDelay = 1000, fixedRate = 60000)
    public void checkExpiredPolicies() {
        LocalDate today = LocalDate.now();

        log.info("Checking for policies that expired on: {}", today);

        List<InsurancePolicy> expiredPolicies = notificationRepository.findExpiredPolicies(today);
        log.info("Found {} policies expiring today", expiredPolicies.size());

        if (!expiredPolicies.isEmpty()) {
            log.info("Policies expiring today:");
            for (InsurancePolicy policy : expiredPolicies) {
                log.info("   - Policy {} (Car {}): {} to {}",
                        policy.getId(),
                        policy.getCar() != null ? policy.getCar().getId() : "unknown",
                        policy.getStartDate(),
                        policy.getEndDate());
            }
        }

        for (InsurancePolicy policy : expiredPolicies) {
            if (shouldNotifyForPolicy(policy, today)) {
                notifyExpiredPolicy(policy, today);
            }
        }

        List<InsurancePolicy> allExpired = notificationRepository.findAllExpiredPolicies(today);
        log.info("Total expired policies (including past): {}", allExpired.size());
    }
}