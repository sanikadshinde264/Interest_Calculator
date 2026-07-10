package com.yourcompany.interestcalc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recurring_deposit_interest")
public class RecurringDepositInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monthly_deposit", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyDeposit;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer months;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Column(name = "total_interest", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalInterest;

    @Column(name = "maturity_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal maturityAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getMonthlyDeposit() { return monthlyDeposit; }
    public void setMonthlyDeposit(BigDecimal monthlyDeposit) { this.monthlyDeposit = monthlyDeposit; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }

    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest = totalInterest; }

    public BigDecimal getMaturityAmount() { return maturityAmount; }
    public void setMaturityAmount(BigDecimal maturityAmount) { this.maturityAmount = maturityAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
