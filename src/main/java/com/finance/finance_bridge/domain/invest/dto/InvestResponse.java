package com.finance.finance_bridge.domain.invest.dto;

import com.finance.finance_bridge.domain.loan.entity.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class InvestResponse {
    private Long investId;
    private BigDecimal amount;
    private Long loanId;
    private LoanStatus loanStatus;

}
