package Models;

import Interfaces.LoanCalculation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FixedRateLoan extends Loan implements LoanCalculation {

    // Constructor to initialize inherited fields from Loan class
    public FixedRateLoan(double loanAmount, double downPayment, double interestRate, int duration, String paymentFrequency, Customer c, Vehicle v) {
        super(loanAmount, downPayment, interestRate, duration, paymentFrequency, c, v);
    }

    @Override
    public double calculatePayment() {
        double principal = this.amount - this.douwnPayment; // The amount to be financed
        double interestRatePerPeriod = this.interestRate / 100 / 12; // Monthly interest rate

        int totalPeriods = this.duration; // Default to monthly payment periods

        // Payment frequency handling: Convert loan duration to correct periods (monthly, bi-weekly, weekly)
        if (this.frequency.equals("Weekly")) {
            totalPeriods = totalPeriods * 4; // Weekly payments
        } else if (this.frequency.equals("Bi-Weekly")) {
            totalPeriods = totalPeriods * 2; // Bi-weekly payments
        }

        // Loan payment calculation (using the standard fixed-rate loan formula)
        double payment = (principal * interestRatePerPeriod) / (1 - Math.pow(1 + interestRatePerPeriod, -totalPeriods));

        return payment;
    }

    @Override
    public List<LoanAmortization> generateAmortizationSchedule() {
        List<LoanAmortization> amortizationList = new ArrayList<>();
        double principal = this.amount - this.douwnPayment;
        double interestRatePerPeriod = this.interestRate / 100 / 12;  // Monthly interest rate
        int totalPeriods = this.duration;

        // Adjust for payment frequency
        if (this.frequency.equals("Weekly")) {
            totalPeriods = totalPeriods * 4; // Weekly payments
        } else if (this.frequency.equals("Bi-Weekly")) {
            totalPeriods = totalPeriods * 2; // Bi-weekly payments
        }

        double remainingBalance = principal;
        double paymentAmount = calculatePayment();

        // Start Date
        Calendar paymentDate = Calendar.getInstance();
        paymentDate.setTime(new Date());  // Starting from today

        for (int paymentNo = 1; paymentNo <= totalPeriods; paymentNo++) {
            double interestPayment = remainingBalance * interestRatePerPeriod;
            double principalPayment = paymentAmount - interestPayment;
            remainingBalance -= principalPayment;

            // Add the payment date based on the frequency
            if (this.frequency.equals("Weekly")) {
                paymentDate.add(Calendar.WEEK_OF_YEAR, 1);  // Move one week forward
            } else if (this.frequency.equals("Bi-Weekly")) {
                paymentDate.add(Calendar.WEEK_OF_YEAR, 2);  // Move two weeks forward
            } else if (this.frequency.equals("Monthly")) {
                paymentDate.add(Calendar.MONTH, 1);  // Move one month forward
            }

            LoanAmortization amortization = new LoanAmortization(
                    paymentNo,
                    paymentDate.getTime(),
                    paymentAmount,
                    interestPayment,
                    principalPayment,
                    remainingBalance
            );
            amortizationList.add(amortization);
        }
        return amortizationList;
    }

}
