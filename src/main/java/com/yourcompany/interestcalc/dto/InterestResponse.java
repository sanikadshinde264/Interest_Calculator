package com.yourcompany.interestcalc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Common response shape for both day-wise and month-wise results.
 * Fields not relevant to a given type (e.g. dates for month-wise) are left null.
 */
public class InterestResponse {

    private Long id;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private LocalDate startDate;   // day-wise only
    private LocalDate endDate;     // day-wise only
    private Integer actualDays;    // day-wise only
    private Integer months;        // month-wise only
    private BigDecimal interestAmount;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    public InterestResponse() { }

    public static InterestResponse dayWise(Long id, BigDecimal amount, BigDecimal rate,
                                            LocalDate start, LocalDate end, Integer days,
                                            BigDecimal interest, BigDecimal total, LocalDateTime createdAt) {
        InterestResponse r = new InterestResponse();
        r.id = id;
        r.amount = amount;
        r.interestRate = rate;
        r.startDate = start;
        r.endDate = end;
        r.actualDays = days;
        r.interestAmount = interest;
        r.totalAmount = total;
        r.createdAt = createdAt;
        return r;
    }

    public static InterestResponse monthWise(Long id, BigDecimal amount, BigDecimal rate,
                                              Integer months, BigDecimal interest,
                                              BigDecimal total, LocalDateTime createdAt) {
        InterestResponse r = new InterestResponse();
        r.id = id;
        r.amount = amount;
        r.interestRate = rate;
        r.months = months;
        r.interestAmount = interest;
        r.totalAmount = total;
        r.createdAt = createdAt;
        return r;
    }

    // --- Getters and setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getActualDays() { return actualDays; }
    public void setActualDays(Integer actualDays) { this.actualDays = actualDays; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }

    public BigDecimal getInterestAmount() { return interestAmount; }
    public void setInterestAmount(BigDecimal interestAmount) { this.interestAmount = interestAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
