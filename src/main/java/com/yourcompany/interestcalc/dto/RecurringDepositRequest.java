package com.yourcompany.interestcalc.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RecurringDepositRequest {

    @NotNull(message = "Monthly deposit is required")
    @DecimalMin(value = "0.01", message = "Monthly deposit must be greater than 0")
    private BigDecimal monthlyDeposit;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be greater than 0")
    @Digits(integer = 3, fraction = 2, message = "Interest rate can have at most 2 decimal places")
    private BigDecimal interestRate;

    @NotNull(message = "Number of months is required")
    @Min(value = 1, message = "Number of months must be a positive integer")
    private Integer months;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    // --- Getters and setters ---

    public BigDecimal getMonthlyDeposit() { return monthlyDeposit; }
    public void setMonthlyDeposit(BigDecimal monthlyDeposit) { this.monthlyDeposit = monthlyDeposit; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}
