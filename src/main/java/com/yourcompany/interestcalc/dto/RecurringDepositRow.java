package com.yourcompany.interestcalc.dto;

import java.math.BigDecimal;

/**
 * One row of the RD projection table: a single month's deposit,
 * the running balance after that deposit, and the interest earned
 * on that balance for the month.
 */
public class RecurringDepositRow {

    private int month;
    private BigDecimal deposit;
    private BigDecimal runningBalance;
    private BigDecimal interest;

    public RecurringDepositRow() { }

    public RecurringDepositRow(int month, BigDecimal deposit, BigDecimal runningBalance, BigDecimal interest) {
        this.month = month;
        this.deposit = deposit;
        this.runningBalance = runningBalance;
        this.interest = interest;
    }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public BigDecimal getDeposit() { return deposit; }
    public void setDeposit(BigDecimal deposit) { this.deposit = deposit; }

    public BigDecimal getRunningBalance() { return runningBalance; }
    public void setRunningBalance(BigDecimal runningBalance) { this.runningBalance = runningBalance; }

    public BigDecimal getInterest() { return interest; }
    public void setInterest(BigDecimal interest) { this.interest = interest; }
}
