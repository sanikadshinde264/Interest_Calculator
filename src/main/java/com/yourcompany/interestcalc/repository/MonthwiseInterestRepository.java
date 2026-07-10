package com.yourcompany.interestcalc.repository;

import com.yourcompany.interestcalc.model.MonthwiseInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthwiseInterestRepository extends JpaRepository<MonthwiseInterest, Long> {
}
