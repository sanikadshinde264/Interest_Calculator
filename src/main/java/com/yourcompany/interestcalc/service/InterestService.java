package com.yourcompany.interestcalc.service;

import com.yourcompany.interestcalc.dto.DaywiseRequest;
import com.yourcompany.interestcalc.dto.InterestResponse;
import com.yourcompany.interestcalc.dto.MonthwiseRequest;
import com.yourcompany.interestcalc.dto.RecurringDepositRequest;
import com.yourcompany.interestcalc.dto.RecurringDepositResponse;
import com.yourcompany.interestcalc.dto.RecurringDepositRow;
import com.yourcompany.interestcalc.exception.InvalidDateRangeException;
import com.yourcompany.interestcalc.model.DaywiseInterest;
import com.yourcompany.interestcalc.model.MonthwiseInterest;
import com.yourcompany.interestcalc.model.RecurringDepositInterest;
import com.yourcompany.interestcalc.repository.DaywiseInterestRepository;
import com.yourcompany.interestcalc.repository.MonthwiseInterestRepository;
import com.yourcompany.interestcalc.repository.RecurringDepositRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class InterestService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal DAYS_IN_YEAR = BigDecimal.valueOf(365);
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private final DaywiseInterestRepository daywiseRepo;
    private final MonthwiseInterestRepository monthwiseRepo;
    private final RecurringDepositRepository recurringDepositRepo;

    public InterestService(DaywiseInterestRepository daywiseRepo,
                            MonthwiseInterestRepository monthwiseRepo,
                            RecurringDepositRepository recurringDepositRepo) {
        this.daywiseRepo = daywiseRepo;
        this.monthwiseRepo = monthwiseRepo;
        this.recurringDepositRepo = recurringDepositRepo;
    }

    // ---------- Day-wise ----------

    public InterestResponse calculateAndSaveDaywise(DaywiseRequest req) {
        long days = ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate());
        if (days <= 0) {
            throw new InvalidDateRangeException("End date must be after start date");
        }

        // Interest = (Principal * Rate * Days) / (100 * 365)
        BigDecimal interest = req.getAmount()
                .multiply(req.getInterestRate())
                .multiply(BigDecimal.valueOf(days))
                .divide(HUNDRED.multiply(DAYS_IN_YEAR), new MathContext(10))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = req.getAmount().add(interest).setScale(2, RoundingMode.HALF_UP);

        DaywiseInterest entity = new DaywiseInterest();
        entity.setAmount(req.getAmount());
        entity.setInterestRate(req.getInterestRate());
        entity.setStartDate(req.getStartDate());
        entity.setEndDate(req.getEndDate());
        entity.setActualDays((int) days);
        entity.setInterestAmount(interest);
        entity.setTotalAmount(total);

        DaywiseInterest saved = daywiseRepo.save(entity);

        return InterestResponse.dayWise(saved.getId(), saved.getAmount(), saved.getInterestRate(),
                saved.getStartDate(), saved.getEndDate(), saved.getActualDays(),
                saved.getInterestAmount(), saved.getTotalAmount(), saved.getCreatedAt());
    }

    public List<InterestResponse> getDaywiseHistory() {
        return daywiseRepo.findAll().stream()
                .map(e -> InterestResponse.dayWise(e.getId(), e.getAmount(), e.getInterestRate(),
                        e.getStartDate(), e.getEndDate(), e.getActualDays(),
                        e.getInterestAmount(), e.getTotalAmount(), e.getCreatedAt()))
                .toList();
    }

    // ---------- Month-wise ----------

    public InterestResponse calculateAndSaveMonthwise(MonthwiseRequest req) {
        // Interest = (Principal * Rate * Months) / (100 * 12)
        BigDecimal interest = req.getAmount()
                .multiply(req.getInterestRate())
                .multiply(BigDecimal.valueOf(req.getMonths()))
                .divide(HUNDRED.multiply(MONTHS_IN_YEAR), new MathContext(10))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = req.getAmount().add(interest).setScale(2, RoundingMode.HALF_UP);

        MonthwiseInterest entity = new MonthwiseInterest();
        entity.setAmount(req.getAmount());
        entity.setInterestRate(req.getInterestRate());
        entity.setMonths(req.getMonths());
        entity.setInterestAmount(interest);
        entity.setTotalAmount(total);

        MonthwiseInterest saved = monthwiseRepo.save(entity);

        return InterestResponse.monthWise(saved.getId(), saved.getAmount(), saved.getInterestRate(),
                saved.getMonths(), saved.getInterestAmount(), saved.getTotalAmount(), saved.getCreatedAt());
    }

    public List<InterestResponse> getMonthwiseHistory() {
        return monthwiseRepo.findAll().stream()
                .map(e -> InterestResponse.monthWise(e.getId(), e.getAmount(), e.getInterestRate(),
                        e.getMonths(), e.getInterestAmount(), e.getTotalAmount(), e.getCreatedAt()))
                .toList();
    }

    // ---------- Recurring Deposit ----------

    /**
     * Builds the month-by-month projection: each month's deposit is added to
     * the running balance, then that month's interest is earned on the new
     * balance (simple, non-compounding — matches the day-wise/month-wise
     * calculators' simple-interest style).
     * Interest per month = runningBalance * rate / 100 / 12.
     */
    private List<RecurringDepositRow> buildRows(BigDecimal monthlyDeposit, BigDecimal rate, int months) {
        List<RecurringDepositRow> rows = new ArrayList<>();
        BigDecimal runningBalance = BigDecimal.ZERO;
        for (int m = 1; m <= months; m++) {
            runningBalance = runningBalance.add(monthlyDeposit);
            BigDecimal interest = runningBalance
                    .multiply(rate)
                    .divide(HUNDRED.multiply(MONTHS_IN_YEAR), new MathContext(10))
                    .setScale(2, RoundingMode.HALF_UP);
            rows.add(new RecurringDepositRow(m, monthlyDeposit.setScale(2, RoundingMode.HALF_UP),
                    runningBalance.setScale(2, RoundingMode.HALF_UP), interest));
        }
        return rows;
    }

    public RecurringDepositResponse calculateAndSaveRecurringDeposit(RecurringDepositRequest req) {
        List<RecurringDepositRow> rows = buildRows(req.getMonthlyDeposit(), req.getInterestRate(), req.getMonths());

        BigDecimal totalInterest = rows.stream()
                .map(RecurringDepositRow::getInterest)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal principal = req.getMonthlyDeposit()
                .multiply(BigDecimal.valueOf(req.getMonths()))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal maturityAmount = principal.add(totalInterest).setScale(2, RoundingMode.HALF_UP);

        var maturityDate = req.getStartDate().plusMonths(req.getMonths());

        RecurringDepositInterest entity = new RecurringDepositInterest();
        entity.setMonthlyDeposit(req.getMonthlyDeposit());
        entity.setInterestRate(req.getInterestRate());
        entity.setMonths(req.getMonths());
        entity.setStartDate(req.getStartDate());
        entity.setMaturityDate(maturityDate);
        entity.setTotalInterest(totalInterest);
        entity.setMaturityAmount(maturityAmount);

        RecurringDepositInterest saved = recurringDepositRepo.save(entity);

        return RecurringDepositResponse.of(saved.getId(), saved.getMonthlyDeposit(), saved.getInterestRate(),
                saved.getMonths(), saved.getStartDate(), saved.getMaturityDate(), rows, principal,
                saved.getTotalInterest(), saved.getMaturityAmount(), saved.getCreatedAt());
    }

    public List<RecurringDepositResponse> getRecurringDepositHistory() {
        return recurringDepositRepo.findAll().stream()
                .map(e -> {
                    List<RecurringDepositRow> rows = buildRows(e.getMonthlyDeposit(), e.getInterestRate(), e.getMonths());
                    BigDecimal principal = e.getMonthlyDeposit()
                            .multiply(BigDecimal.valueOf(e.getMonths()))
                            .setScale(2, RoundingMode.HALF_UP);
                    return RecurringDepositResponse.of(e.getId(), e.getMonthlyDeposit(), e.getInterestRate(),
                            e.getMonths(), e.getStartDate(), e.getMaturityDate(), rows, principal,
                            e.getTotalInterest(), e.getMaturityAmount(), e.getCreatedAt());
                })
                .toList();
    }

    // ---------- Shared ----------

    public void deleteDaywise(Long id) {
        daywiseRepo.deleteById(id);
    }

    public void deleteMonthwise(Long id) {
        monthwiseRepo.deleteById(id);
    }

    public void deleteRecurringDeposit(Long id) {
        recurringDepositRepo.deleteById(id);
    }

    public void deleteAllDaywise() {
        daywiseRepo.deleteAll();
    }

    public void deleteAllMonthwise() {
        monthwiseRepo.deleteAll();
    }

    public void deleteAllRecurringDeposit() {
        recurringDepositRepo.deleteAll();
    }
}