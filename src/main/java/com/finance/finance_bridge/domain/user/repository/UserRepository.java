package com.finance.finance_bridge.domain.user.repository;

import com.finance.finance_bridge.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //이메일로 중복 가입 체크를 위해 필요
    boolean existsByEmail(String email);

    //로그인할 때 이메일로 회원 찾기 위해 필요
    Optional<User> findByEmail(String email);
}
