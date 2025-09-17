package com.example.carins.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expired_policy_notification")
public class ExpiredPolicyNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_id", nullable = false, unique = true)
    private Long policyId;

    @Column(name = "notification_date", nullable = false)
    private LocalDate notificationDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ExpiredPolicyNotification() {
        this.createdAt = LocalDateTime.now();
    }

    public ExpiredPolicyNotification(Long policyId, LocalDate notificationDate) {
        this();
        this.policyId = policyId;
        this.notificationDate = notificationDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}