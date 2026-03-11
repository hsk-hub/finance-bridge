package com.finance.finance_bridge.domain.loan.entity;

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
public class Loan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "loan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal loanAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal investedAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Version
    private Long version;

    @PrePersist
    public void prePersist(){
        if(this.investedAmount == null) {
            this.investedAmount = BigDecimal.ZERO;
        }
        if(this.version == null){
            this.version = 0L;
        }
    }

    public void addInvestedAmount(BigDecimal amount){
        // 모집 중인 상품인지 확인
        if(this.status != LoanStatus.RECRUITING) {
            throw new IllegalArgumentException("현재 투자할 수 없는 상품입니다");
        }

        // 투자금이 0원 이하인지 확인
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("투자 금액은 0원보다 커야 합니다.");
        }

        // 목표 금액 초과 여부 확인
        BigDecimal afterInvest = this.investedAmount.add(amount);
        if (afterInvest.compareTo(this.loanAmount) > 0) {
            throw new IllegalArgumentException("목표 모집 금액을 초과할 수 없습니다.");
        }

        // 투자금 증가
        this.investedAmount = afterInvest;

        // 100% 달성하면 상태 변경
        if (this.investedAmount.compareTo(this.loanAmount) == 0) {
            this.status = LoanStatus.FUNDING_COMPLETED;
        }
    }
}
