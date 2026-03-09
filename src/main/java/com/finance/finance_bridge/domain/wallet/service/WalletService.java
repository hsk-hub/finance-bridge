package com.finance.finance_bridge.domain.wallet.service;

import com.finance.finance_bridge.domain.user.entity.User;
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
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletHistoryRepository walletHistoryRepository;

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        // 1. 유저의 지갑 조회
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("지갑을 찾을 수 없습니다."));

        // 2. 잔액 증가 (엔티티 내부의 비즈니스 로직 호출)
        wallet.addBalance(amount);

        // 3. 거래 내역 영수증 발행
        WalletHistory history = WalletHistory.builder()
                .wallet(wallet)
                .amount(amount)
                .balanceSnapshot(wallet.getBalance()) // 거래 직후의 잔액 기록
                .type(TransactionType.DEPOSIT)
                .build();

        // 4. 영수증 저장 (지갑 잔액은 @Transactional에 의해 자동 변경 감지되어 UPDATE 됨)
        walletHistoryRepository.save(history);
    }

    @Transactional
    public void createWallet(User user) {
        Wallet newWallet = Wallet.builder()
                .user(user)
                .build();
        walletRepository.save(newWallet);
    }
}
