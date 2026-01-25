# [Project] FinanceBridge 요구사항 정의서 (v1.0)

## 1. 프로젝트 개요 (Overview)

- **프로젝트명:** FinanceBridge (파이낸스 브릿지)
- **프로젝트 목표:**
    - 은행의 개입 없이 개인 간 대출(Loan)과 투자(Investment)를 연결하는 P2P 금융 플랫폼 구축.
    - 대규모 트래픽보다는 **데이터의 무결성(Integrity)**, **정확한 연산**, **동시성 제어**에 초점을 맞춘 고신뢰성 백엔드 시스템 구현.
- **개발 인원:** 1명 (Full-stack -> Backend Focus)
- **개발 기간: 6-7주**

## 2. 시스템 아키텍처 및 기술 스택 (Tech Stack)

- **Language:** Java 17 (LTS)
- **Framework:** Spring Boot 3.x
- **Database:** MySQL 8.0 (Main DB), Redis (Cache & Session & Lock)
- **ORM:** Spring Data JPA + QueryDSL
- **Security:** Spring Security + JWT (Json Web Token)
- **Build/Deploy:** Gradle, Docker, AWS (EC2/RDS) or Local Environment

## 3. 핵심 도메인 및 기능 명세 (Functional Requirements)

### A. 사용자 도메인 (User & Auth)

1. **회원가입/로그인:**
    - 이메일 기반 회원가입.
    - 패스워드는 단방향 암호화(`BCrypt`) 저장 필수.
    - JWT Access/Refresh Token 기반 인증 인가.
2. **내 정보 관리:**
    - 내 대출 내역, 내 투자 내역, 내 지갑 잔액 조회.

### B. 지갑 도메인 (Wallet) - *돈의 원천*

1. **충전(Deposit):**
    - 사용자는 자신의 가상 계좌에 금액을 충전할 수 있다. (실제 PG사 연동 대신 모의 충전 기능 구현)
    - *Check:* 충전 시 `Wallet` 테이블 잔액 증가 + `Transaction_Log` 기록 (Atomic 처리).
2. **출금(Withdraw):**
    - 잔액 범위 내에서 출금 가능.
    - *Check:* 동시 출금 요청 시 잔액이 마이너스가 되지 않도록 방어 로직 필수.

### C. 대출 도메인 (Loan)

1. **대출 신청:**
    - 사용자는 희망 금액, 금리, 기간을 입력하여 대출을 신청한다.
    - 초기 상태는 `PENDING`(심사중)으로 생성.
2. **대출 심사 및 승인 (Admin):**
    - 관리자가 승인하면 상태가 `APPROVED`(투자 모집중)로 변경되고 투자 상품 리스트에 노출된다.
3. **대출 집행 및 상환:**
    - 모집 금액이 100% 달성되면 대출자 지갑으로 입금됨.
    - 대출자는 매달 정해진 날짜에 원리금을 상환해야 함.

### D. 투자 도메인 (Invest) - *핵심 기술 파트*

1. **투자 상품 조회:**
    - `APPROVED` 상태인 상품만 조회 가능.
2. **투자하기 (Funding):**
    - 투자자는 상품에 원하는 금액만큼 투자할 수 있다. (최소 1만 원 단위)
    - **[Critical]** 투자 시 **모집 금액 한도(Limit)**를 초과할 수 없다. (선착순 동시성 제어)
    - 투자 성공 시: 투자자 지갑 잔액 차감 -> 투자 내역 생성 -> 상품 모집 금액 증가 (Transaction).

### E. 정산 시스템 (Batch/Scheduler)

1. **자정 정산 (Daily Settlement):**
    - 매일 밤 00:00, 상환된 원리금을 투자자들의 투자 비율에 맞춰 분배.
    - 수많은 투자자에게 일괄 지급해야 하므로 **Spring Batch**를 이용한 대용량 처리.

## 4. 비기능적 요구사항 (Non-Functional Requirements)

1. **데이터 무결성:** 모든 금전 거래는 로그(History)가 남아야 하며, 계산 오차가 0이어야 한다.
2. **동시성 제어:** 다수의 사용자가 동시에 하나의 상품에 투자하더라도 모집 금액을 초과해선 안 된다. (Redis Distributed Lock 또는 DB Lock 활용)
3. **보안:** 개인정보(이름, 폰번호) 및 비밀번호는 암호화되어야 하며, 본인이 아닌 경우 데이터 접근이 불가능해야 한다.