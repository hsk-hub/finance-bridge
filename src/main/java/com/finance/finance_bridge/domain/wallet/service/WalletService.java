package com.finance.finance_bridge.domain.wallet.service;

import com.finance.finance_bridge.domain.user.entity.User;
import com.finance.finance_bridge.domain.wallet.entity.Wallet;
import com.finance.finance_bridge.domain.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public void createWallet(User user) {
        Wallet newWallet = Wallet.builder()
                .user(user)
                .build();
        walletRepository.save(newWallet);
    }
}
