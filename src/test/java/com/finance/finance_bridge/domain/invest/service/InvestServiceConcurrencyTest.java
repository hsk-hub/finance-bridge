package com.finance.finance_bridge.domain.invest.service;

import com.finance.finance_bridge.domain.loan.entity.Loan;
import com.finance.finance_bridge.domain.loan.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class InvestServiceConcurrencyTest {
    @Autowired
    private InvestService investService;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    @DisplayName("100명이 동시에 1만원씩 투자")
    void invest_concurrency_test() throws InterruptedException {
        // given
        // 주의: 해당 테스트를 진행하기전, 유저(1번)의 지갑 잔액이 최소 100만원 이상 있어야합니다.
        // DB에서 직접 update 문으로 유저 1번의 지갑 잔액을 200만원 정도로 넉넉하게 늘려주세요.
        Long userId = 1L;
        Long loanId = 1L;
        int threadCount = 100;

        // 32개의 쓰레드 풀을 생성
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 100개의 작업이 모두 끝날 때까지 기다려 준다.
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(()->{
                try {
                    investService.invest(userId, loanId, BigDecimal.valueOf(10000));
                } catch(Exception e){
                    System.out.println("X 에러 발생: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 100명이 다 끝날 때까지 메인 쓰레드 대기

        // then
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        System.out.println("====================================");
        System.out.println(" 100명 투자 완료 후 총 모금액: " + loan.getInvestedAmount());
        System.out.println("====================================");

        //100명이 1만원씩 투자하였으니, 기존 모금액(1만원) + 100만원 = 101만원이 되어야한다.
    }
}
