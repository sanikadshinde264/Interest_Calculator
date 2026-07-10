package com.yourcompany.interestcalc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RecurringDepositResponse {

    private Long id;
    private BigDecimal monthlyDeposit;
    private BigDecimal interestRate;
    private Integer months;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private List<RecurringDepositRow> rows;
    private BigDecimal principalAmount;
    private BigDecimal totalInterest;
    private BigDecimal maturityAmount;
    private LocalDateTime createdAt;

    public RecurringDepositResponse() { }

    public static RecurringDepositResponse of(Long id, BigDecimal monthlyDeposit, BigDecimal rate, Integer months,
                                               LocalDate startDate, LocalDate maturityDate,
                                               List<RecurringDepositRow> rows, BigDecimal principalAmount,
                                               BigDecimal totalInterest, BigDecimal maturityAmount,
                                               LocalDateTime createdAt) {
        RecurringDepositResponse r = new RecurringDepositResponse();
        r.id = id;
        r.monthlyDeposit = monthlyDeposit;
        r.interestRate = rate;
        r.months = months;
        r.startDate = startDate;
        r.maturityDate = maturityDate;
        r.rows = rows;
        r.principalAmount = principalAmount;
        r.totalInterest = totalInterest;
        r.maturityAmount = maturityAmount;
        r.createdAt = createdAt;
        return r;
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

    public List<RecurringDepositRow> getRows() { return rows; }
    public void setRows(List<RecurringDepositRow> rows) { this.rows = rows; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest = totalInterest; }

    public BigDecimal getMaturityAmount() { return maturityAmount; }
    public void setMaturityAmount(BigDecimal maturityAmount) { this.maturityAmount = maturityAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
