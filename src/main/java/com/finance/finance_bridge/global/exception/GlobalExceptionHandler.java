package com.finance.finance_bridge.global.exception;

import com.finance.finance_bridge.global.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 발생시킨 IllegalArgumentException을 잡아서 400 Bad Request로 변경
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 요청 발생: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 에러
                .body(ApiResponse.error("BAD_REQUEST", e.getMessage()));
    }

    // 그 외 예상치 못한 모든 에러 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("서버 내부 오류 발생", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 에러
                .body(ApiResponse.error("SERVER_ERROR", "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."));
    }
}
