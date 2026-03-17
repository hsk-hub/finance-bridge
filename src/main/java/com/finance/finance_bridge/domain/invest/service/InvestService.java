package com.finance.finance_bridge.domain.invest.service;

import com.finance.finance_bridge.domain.invest.dto.InvestResponse;
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
import java.util.List;

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
        Wallet wallet = walletRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new IllegalArgumentException("지갑을 찾을 수 없습니다."));

        Loan loan = loanRepository.findByIdWithLock(loanId)
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

    @Transactional(readOnly = true)
    public List<InvestResponse> getMyInvestments(Long userId) {
        // 1. repository에서 유저 ID로 Invest 목록을 가져온다.
        List<Invest> invests = investRepository.findByInvestorIdWithLoan(userId);

        // 2. Invest 엔티티를 InvestResponse DTO로 변환하여 반환한다.
        // (주의: invest.getLoan().getStatus() 처럼 Loan 엔티티의 정보에 접근해야 N+1이 발생한다)
        return invests.stream()
                .map(invest -> new InvestResponse(
                        invest.getId(),
                        invest.getAmount(),
                        invest.getLoan().getId(),
                        invest.getLoan().getStatus()//연관된 Loan의 상태 조회
                ))
                .toList();
    }
}
