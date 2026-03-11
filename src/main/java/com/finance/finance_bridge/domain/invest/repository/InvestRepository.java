package com.finance.finance_bridge.domain.invest.repository;

import com.finance.finance_bridge.domain.invest.entity.Invest;
import com.finance.finance_bridge.domain.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestRepository extends JpaRepository<Invest, Long> {
}
