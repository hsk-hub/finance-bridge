package com.finance.finance_bridge.domain.wallet.repository;

import com.finance.finance_bridge.domain.user.entity.User;
import com.finance.finance_bridge.domain.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(Long userId);
}
