package com.yourcompany.interestcalc.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class MonthwiseRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be greater than 0")
    @Digits(integer = 3, fraction = 2, message = "Interest rate can have at most 2 decimal places")
    private BigDecimal interestRate;

    @NotNull(message = "Number of months is required")
    @Min(value = 1, message = "Number of months must be a positive integer")
    private Integer months;

    // --- Getters and setters ---

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }
}
