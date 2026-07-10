package com.yourcompany.interestcalc.repository;

import com.yourcompany.interestcalc.model.RecurringDepositInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringDepositRepository extends JpaRepository<RecurringDepositInterest, Long> {
}
