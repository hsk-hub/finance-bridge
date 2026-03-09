package com.finance.finance_bridge.domain.wallet.repository;

import com.finance.finance_bridge.domain.wallet.entity.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Long> {

}
