package com.yourcompany.interestcalc.repository;

import com.yourcompany.interestcalc.model.DaywiseInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaywiseInterestRepository extends JpaRepository<DaywiseInterest, Long> {
    // findAll(), findById(), save(), deleteById() come from JpaRepository.
    // Add custom queries here later if needed, e.g. findByCreatedAtBetween(...)
}
