package Interfaces;

import Models.LoanAmortization;

import java.util.List;

public interface LoanCalculation {
    double calculatePayment();
    List<LoanAmortization> generateAmortizationSchedule();
}
