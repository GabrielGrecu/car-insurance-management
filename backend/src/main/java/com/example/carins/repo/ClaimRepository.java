package com.example.carins.repo;

import com.example.carins.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByCarIdOrderByClaimDateDesc(Long carId);

//    @Query("SELECT c FROM Claim c WHERE c.car.id = :carId ORDER BY c.claimDate DESC")
//    List<Claim> findClaimsByCarId(@Param("carId") Long carId);
//
//    boolean existsByCarId(Long carId);
}