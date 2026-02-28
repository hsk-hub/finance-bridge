package com.finance.finance_bridge.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 진짜 테이블이 아닌 부모 클래스(속성만 상속)임을 명시
@EntityListeners(AuditingEntityListener.class)// JPA에게 이 클래스를 감시하라고 명령
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false) // 생성일은 수정되면 안됨
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
