package com.finance.finance_bridge.domain.wallet.entity;

import com.finance.finance_bridge.domain.common.BaseEntity;
import com.finance.finance_bridge.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Wallet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // 지갑을 조회할 때 유저 정보까지 다 가져오지 마라(지연 로딩)
    @JoinColumn(name = "user_id", nullable = false, unique = true) // FK 설정
    private User user;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO; //돈은 무조건 BigDecimal

    @Version // Optimistic Lock을 위한 버전 관리
    private Long version;

    public void addBalance(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("충전 금액은 0원보다 커야 합니다.");
        }
        this.balance = this.balance.add(amount);
    }

}

