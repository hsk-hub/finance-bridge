package com.finance.finance_bridge.domain.invest.repository;

import com.finance.finance_bridge.domain.invest.entity.Invest;
import com.finance.finance_bridge.domain.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestRepository extends JpaRepository<Invest, Long> {
    // 특정 유저의 투자 내역을 모두 가져오는 쿼리 메서드
    //List<Invest> findByInvestorId(Long investorId);

    //Fetch Join 적용한 새로운 쿼리 메서드
    @Query("SELECT i FROM Invest i JOIN FETCH i.loan WHERE i.investor.id = :investorId")
    List<Invest> findByInvestorIdWithLoan(@Param("investorId") Long investorId);
}
