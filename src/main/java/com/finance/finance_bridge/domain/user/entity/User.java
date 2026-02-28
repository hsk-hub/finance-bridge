package com.finance.finance_bridge.domain.user.entity;

import com.finance.finance_bridge.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // MySQL 예약어 'user'와 겹치지 않도록 'users'로 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255) // 암호화된 비번 길이 고려
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING) // ENUM을 DB에 저장할 때 숫자가 아닌 '문자열'로 저장
    @Column(nullable = false)
    private UserRole role;

    //생성자나 비즈니스 로직 메서드 추가예정
}
