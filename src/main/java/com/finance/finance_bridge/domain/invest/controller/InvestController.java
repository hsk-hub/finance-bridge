package com.finance.finance_bridge.domain.invest.controller;

import com.finance.finance_bridge.domain.invest.dto.InvestResponse;
import com.finance.finance_bridge.domain.invest.service.InvestService;
import com.finance.finance_bridge.global.common.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/invests")
@RequiredArgsConstructor
public class InvestController {

    private final InvestService investService;

    @PostMapping("/{userId}/loan/{loanId}")
    public ResponseEntity<ApiResponse<Void>> invest(
            @PathVariable Long userId,
            @PathVariable Long loanId,
            @RequestBody InvestRequest request) {

        investService.invest(userId, loanId, request.getAmount());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Getter
    public static class InvestRequest {
        private BigDecimal amount;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<InvestResponse>>> getMyInvestments(@PathVariable Long userId){
        List<InvestResponse> responses = investService.getMyInvestments(userId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
