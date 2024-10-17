package com.payguard.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.payguard.constant.Category;
import com.payguard.constant.Type;
import com.payguard.domain.Transaction;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransactionDTO {
    private Long id;
    
    @NotNull(message = "거래 금액은 필수 입력 항목입니다.")
	@Positive(message = "금액은 1 이상의 수를 입력해야 합니다.")
    private BigDecimal amount;
    
    private BigDecimal remainingAmount;
    
    @NotNull(message = "거래 유형은 필수 입력 항목입니다.")
    private String type;
    
    @NotNull(message = "카테고리는 필수 입력 항목입니다.")
    private String category;
    
    @NotBlank(message = "거래 내용은 필수 입력 항목입니다.")
    private String description;
    
    @NotNull(message = "거래 날짜는 필수 입력 항목입니다.")
    private LocalDate date;
    
    private String budgetName;

    public TransactionDTO() {}
    
    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.remainingAmount = transaction.getRemainingAmount();
        this.type = transaction.getType().name();
        this.category = transaction.getCategory().name();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.budgetName = transaction.getBudget().getName();
    }
    
    public Transaction toEntity() {
        Transaction transaction = new Transaction();
        transaction.setAmount(this.amount);
        transaction.setRemainingAmount(this.remainingAmount);
        transaction.setType(Type.valueOf(this.type));
        transaction.setCategory(Category.valueOf(this.category));
        transaction.setDescription(this.description);
        transaction.setDate(this.date);
        return transaction;
    }
}
