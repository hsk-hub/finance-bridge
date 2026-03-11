package com.finance.finance_bridge.domain.loan.repository;

import com.finance.finance_bridge.domain.invest.entity.Invest;
import com.finance.finance_bridge.domain.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
