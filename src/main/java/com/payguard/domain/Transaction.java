package com.payguard.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.payguard.constant.Category;
import com.payguard.constant.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Transaction {
	@Id
	@GeneratedValue
	@Column(name = "TRANSACTION_ID")
	private Long id;
	
	@Column(nullable = false)
	@NotNull(message = "거래 금액은 필수 입력 항목입니다.")
	@Positive(message = "금액은 1 이상의 수를 입력해야 합니다.")
	private BigDecimal amount;
	
	@Column(nullable = false)
	@NotNull(message = "거래 유형은 필수 입력 항목입니다.")
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull(message = "카태고리는 필수 입력 항목입니다.")
	private Category category;
	
	@Column(nullable = false)
	@NotBlank(message = "거래 내용은 필수 입력 항목입니다.")
	private String description;
	
	@Column(nullable = false)
	@NotNull(message = "거래 날짜는 필수 입력 항목입니다.")
	private LocalDate date;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "BUDGET_ID", nullable = false)
	private Budget budget;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	@Column(nullable = false) // 각 트랜잭션의 남은 예산
	private BigDecimal remainingAmount;
	
}
