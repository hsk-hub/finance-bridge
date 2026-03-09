package com.finance.finance_bridge.domain.wallet.controller;

import com.finance.finance_bridge.domain.wallet.service.WalletService;
import com.finance.finance_bridge.global.common.ApiResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<ApiResponse<Void>> deposit(
            @PathVariable Long userId,
            @RequestBody DepositRequest request){
        walletService.deposit(userId, request.getAmount());
        return ResponseEntity.ok(ApiResponse.success());
    }

    // 간단한 DTO 내부 클래스
    @Getter
    @NoArgsConstructor
    public static class DepositRequest {
        private BigDecimal amount;
    }

}
