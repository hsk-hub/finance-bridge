package com.finance.finance_bridge.domain.loan.repository;

import com.finance.finance_bridge.domain.invest.entity.Invest;
import com.finance.finance_bridge.domain.loan.entity.Loan;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    //비관적 락을 걸고 대출 상품을 조회하는 메서드 추가
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Loan l WHERE l.id = :id")
    Optional<Loan> findByIdWithLock(@Param("id") Long id);
}
