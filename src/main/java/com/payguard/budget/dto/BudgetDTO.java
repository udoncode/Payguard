package com.payguard.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BudgetDTO {
	private Long id;
	
	@NotBlank(message = "예산 계획 내용은 필수 입력 항목입니다.")
    private String name;
	
	@NotNull(message = "총 예산 금액은 필수 입력 항목입니다.")
	@Positive(message = "금액은 1 이상의 수를 입력해야 합니다.")
    private BigDecimal amount;
	
	@NotNull(message = "예산 시작일은 필수 입력 항목입니다.")
    private LocalDate startDate;
	
	@NotNull(message = "예산 종료일은 필수 입력 항목입니다.")
    private LocalDate endDate;
}
