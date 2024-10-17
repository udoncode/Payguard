package com.payguard.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payguard.constant.Type;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Budget {
	@Id
	@GeneratedValue
	@Column(name="BUDGET_ID")
	private Long id;
	
	@Column(nullable = false)
	@NotBlank(message = "예산 계획 내용은 필수 입력 항목입니다.")
	private String name;
	
//    @Digits(integer = 10, fraction = 2, message = "총 예산 금액은 숫자여야 하며, 최대 10자리 숫자와 소수점 이하 2자리까지 허용됩니다.")
	@Column(nullable = false)
	@NotNull(message = "총 예산 금액은 필수 입력 항목입니다.")
	@Positive(message = "금액은 1 이상의 수를 입력해야 합니다.")
	private BigDecimal amount;
	
	@Column(nullable = false)
	@NotNull(message = "예산 시작일은 필수 입력 항목입니다.")
	private LocalDate startDate;
	
	@Column(nullable = false)
	@NotNull(message = "예산 종료일은 필수 입력 항목입니다.")
	private LocalDate endDate;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	@Column(nullable = false)
	private BigDecimal remainingAmount;
	
	@ManyToOne
	@JoinColumn(name="MEMBER_ID", nullable = false)
	@JsonIgnore // 순환 참조 방지
	private Member member;
	
	@OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
	private List<Transaction> transactions;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
		remainingAmount = this.amount;
	}
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	public void calculateRemainingAmount() {
		this.remainingAmount = amount.add(calculateTotalIncome()).subtract(calculateTotalSpent());
	}
	
	
	private BigDecimal calculateTotalIncome() {
		return transactions.stream()
				.filter(transaction -> transaction.getType() == Type.INCOME)
				.map(Transaction::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private BigDecimal calculateTotalSpent() {
		return transactions.stream()
				.filter(transaction -> transaction.getType() == Type.EXPENSE)
				.map(Transaction::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private void updateTransactionRemainingAmount() {
		// 거래가 없으면 종료
	    if (transactions.isEmpty()) {
	        return;
	    }

	    // 마지막 거래의 잔여 금액은 현재 잔여 금액으로 설정
	    transactions.get(transactions.size() - 1).setRemainingAmount(remainingAmount);

	    // 리스트를 역으로 순회하며 잔여 금액 계산
	    for (int i = transactions.size() - 2; i >= 0; i--) {
	        Transaction currentTransaction = transactions.get(i);
	        Transaction nextTransaction = transactions.get(i + 1);

	        // 다음 거래의 잔여 금액을 기반으로 현재 거래의 잔여 금액을 계산
	        if (nextTransaction.getType() == Type.EXPENSE) {
	            currentTransaction.setRemainingAmount(nextTransaction.getRemainingAmount().add(nextTransaction.getAmount()));
	        } else if (nextTransaction.getType() == Type.INCOME) {
	            currentTransaction.setRemainingAmount(nextTransaction.getRemainingAmount().subtract(nextTransaction.getAmount()));
	        }
	    }
	}
	
	public void addTransaction(Transaction transaction) {
		// 예산의 거래 리스트에 해당 거래 추가
		transactions.add(transaction);
//		transaction.setBudget(this);
		
		// 예산의 전체 잔여 금액 계산
		calculateRemainingAmount();
		
		// 잔여 금액 업데이트
		transaction.setRemainingAmount(remainingAmount);
	}
	
	public void removeTransaction(Transaction transaction) {
	    // 예산의 거래 리스트에서 해당 거래 삭제
	    transactions.remove(transaction);
	    
	    // 예산의 전체 잔여 금액 계산
	    calculateRemainingAmount();
	    
	    // 각 거래의 잔여 금액 업데이트
	    updateTransactionRemainingAmount();
	}
	
	public void updateTransaction(Transaction existingTransaction, Transaction newTransaction) {
	    // 기존 거래 업데이트
	    existingTransaction.setAmount(newTransaction.getAmount());
	    existingTransaction.setType(newTransaction.getType());
	    existingTransaction.setCategory(newTransaction.getCategory());
	    existingTransaction.setDescription(newTransaction.getDescription());
	    existingTransaction.setDate(newTransaction.getDate());

	    // 예산의 전체 잔여 금액 계산
	    calculateRemainingAmount();
	    
	    // 각 거래의 잔여 금액 업데이트
	    updateTransactionRemainingAmount();
	}

}
