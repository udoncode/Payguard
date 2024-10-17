package com.payguard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.payguard.constant.Category;
import com.payguard.constant.Type;
import com.payguard.domain.Budget;
import com.payguard.domain.Transaction;
import com.payguard.persistence.BudgetRepository;
import com.payguard.persistence.TransactionRepository;
import com.payguard.transaction.dto.TransactionDTO;

@SpringBootTest
public class TransactionRepositoryTest {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private BudgetRepository budgetRepository;
	
	@Test
	public void insertTransaction() {
		for (int i = 1; i <= 60; i++) {
			Budget budget = budgetRepository.findById(402L).get();
			
			Transaction transaction = new Transaction();
			BigDecimal amount = new BigDecimal(1000 * i);
			transaction.setAmount(amount);
			transaction.setRemainingAmount(budget.getRemainingAmount().add(amount));
			transaction.setType(Type.INCOME);
			transaction.setCategory(Category.LIVING);
			transaction.setDescription("테스트 " + i);
			transaction.setDate(LocalDate.now().plusDays(7));
			transaction.setBudget(budget);
			transactionRepository.save(transaction);
		}
	}
	
//	@Test
	public void deleteTransaction() {
		Transaction transaction = transactionRepository.findById(1L).get();
		transactionRepository.delete(transaction);
	}
	
//	@Test
	public void getAllTransactions() {
		List<Transaction> transactions = transactionRepository.findAll();
		List<TransactionDTO> transactionDTOs = transactions.stream()
				.map(TransactionDTO::new)
				.collect(Collectors.toList());
		
		System.out.println(transactionDTOs);
	}
	
	
}
