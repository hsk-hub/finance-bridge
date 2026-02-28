package com.finance.finance_bridge.domain.user.service;

import com.finance.finance_bridge.domain.user.dto.SignUpRequest;
import com.finance.finance_bridge.domain.user.dto.SignUpResponse;
import com.finance.finance_bridge.domain.user.entity.User;
import com.finance.finance_bridge.domain.user.repository.UserRepository;
import com.finance.finance_bridge.domain.wallet.repository.WalletRepository;
import com.finance.finance_bridge.domain.wallet.entity.Wallet;
import com.finance.finance_bridge.domain.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 조회만 가능하게 설정 (성능 최적화)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        // 1. 중복 이메일 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 회원 저장
        User savedUser = userRepository.save(request.toEntity(encodedPassword));

        // 4. 지갑 생성 위임 (WalletService 호출)
        walletService.createWallet(savedUser);

        // 5. 응답 반환
        return SignUpResponse.from(savedUser);
    }
}
