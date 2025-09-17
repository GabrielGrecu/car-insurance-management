package com.example.carins.repo;

import com.example.carins.model.ExpiredPolicyNotification;
import com.example.carins.model.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpiredPolicyNotificationRepository extends JpaRepository<ExpiredPolicyNotification, Long> {
    @Query("SELECT p FROM InsurancePolicy p WHERE p.endDate = :currentDate")
    List<InsurancePolicy> findExpiredPolicies(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT p FROM InsurancePolicy p WHERE p.endDate < :currentDate")
    List<InsurancePolicy> findAllExpiredPolicies(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT p FROM InsurancePolicy p WHERE p.endDate BETWEEN :startDate AND :endDate")
    List<InsurancePolicy> findPoliciesExpiringBetween(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(e) > 0 FROM ExpiredPolicyNotification e WHERE e.policyId = :policyId AND e.notificationDate = :notificationDate")
    boolean isAlreadyNotified(@Param("policyId") Long policyId, @Param("notificationDate") LocalDate notificationDate);
}