package com.finance.finance_bridge.global.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final String code;
    private final String message;
    private final T data;

    // 성공 응답 (데이터가 있을 때)
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>("SUCCESS", "요청에 성공했습니다.", data);
    }

    // 성공 응답 (데이터가 없을 때)
    public static ApiResponse<Void> success(){
        return new ApiResponse<>("SUCCESS", "요청에 성공했습니다.", null);
    }

    // 에러 응답
    public static <T> ApiResponse<T> error(String code, String message){
        return new ApiResponse<>(code, message, null);
    }
}
