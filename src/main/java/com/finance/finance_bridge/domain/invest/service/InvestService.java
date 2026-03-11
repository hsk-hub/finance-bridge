package com.finance.finance_bridge.domain.invest.service;

import com.finance.finance_bridge.domain.invest.entity.Invest;
import com.finance.finance_bridge.domain.invest.repository.InvestRepository;
import com.finance.finance_bridge.domain.loan.entity.Loan;
import com.finance.finance_bridge.domain.loan.repository.LoanRepository;
import com.finance.finance_bridge.domain.wallet.entity.TransactionType;
import com.finance.finance_bridge.domain.wallet.entity.Wallet;
import com.finance.finance_bridge.domain.wallet.entity.WalletHistory;
import com.finance.finance_bridge.domain.wallet.repository.WalletHistoryRepository;
import com.finance.finance_bridge.domain.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InvestService {
    private final WalletRepository walletRepository;
    private final WalletHistoryRepository walletHistoryRepository;
    private final LoanRepository loanRepository;
    private final InvestRepository investRepository;

    @Transactional
    public void invest(Long userId, Long loanId, BigDecimal amount) {
        // 지갑, 대출 상품 조회
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("지갑을 찾을 수 없습니다."));

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("대출 상품을 찾을 수 없습니다."));

        // 지갑에서 출금
        wallet.withdraw(amount);

        // 출금 영수증 발행
        WalletHistory history = WalletHistory.builder()
                .wallet(wallet)
                .amount(amount)
                .balanceSnapshot(wallet.getBalance())
                .type(TransactionType.INVEST)
                .build();
        walletHistoryRepository.save(history);

        // 대출 상품에 투자금 추가
        loan.addInvestedAmount(amount);

        Invest invest = Invest.builder()
                .investor(wallet.getUser())
                .loan(loan)
                .amount(amount)
                .build();

        investRepository.save(invest);
    }
}
