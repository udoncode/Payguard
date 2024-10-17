package com.payguard.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.payguard.domain.Budget;
import com.payguard.domain.Transaction;
import com.payguard.persistence.BudgetRepository;
import com.payguard.persistence.TransactionRepository;
import com.payguard.transaction.dto.TransactionDTO;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
	
	private final TransactionRepository transactionRepository;
	private final BudgetRepository budgetRepository;
	
	public TransactionServiceImpl(TransactionRepository transactionRepository, BudgetRepository budgetRepository) {
		this.transactionRepository = transactionRepository;
		this.budgetRepository = budgetRepository;
	}
	
	@Override
	public Transaction insertTransaction(Transaction transaction) {
		// 거래 날짜에 해당하는 예산 조회
	    Optional<Budget> optionalBudget = budgetRepository.findByDate(transaction.getDate());
	    
	    // 해당 예산이 존재하지 않는 경우
	    Budget budget = optionalBudget.orElseThrow(() -> 
	        new RuntimeException("해당 거래 날짜에 맞는 예산 계획이 없습니다. 예산 계획을 먼저 등록하세요.")
	    );

	    // 거래의 예산 계획 설정
	    transaction.setBudget(budget);
	    
	    // 해당 거래의 잔여 금액 설정
	    budget.addTransaction(transaction);
		
		return transactionRepository.save(transaction);
	}
	
	
	@Override
	public Transaction updateTransaction(Long id, Transaction transactionDetails) {
		Transaction transaction = getTransactionById(id);
		
		// 기존의 날짜와 새로운 날짜를 비교
		LocalDate oldDate = transaction.getDate();
		LocalDate newDate = transactionDetails.getDate();
		
		// 거래의 필드 업데이트
		transaction.setAmount(transactionDetails.getAmount());
		transaction.setType(transactionDetails.getType());
		transaction.setCategory(transactionDetails.getCategory());
		transaction.setDescription(transactionDetails.getDescription());
		transaction.setDate(newDate);
		
		Budget budget = transaction.getBudget();
		
		// 거래 날짜 변경 시 새로운 예산 조회
		if(!oldDate.equals(newDate)) {
			// 새로운 예산 조회
			Optional<Budget> newOptionalBudget = budgetRepository.findByDate(newDate);
			
			// 새로운 예산이 존재하지 않을 경우 예외 처리
	        budget = newOptionalBudget.orElseThrow(() -> 
	        	new RuntimeException("해당 거래 날짜에 맞는 예산 계획이 없습니다.")
        	);
	        
	        // 새로운 예산으로 업데이트
	        transaction.setBudget(budget);
		}
		
		// 해당 거래의 잔여 금액 설정
		budget.updateTransaction(transaction, transactionDetails);
		
		return transactionRepository.save(transaction);
	}
	
	@Override
	public void deleteTransaction(Long id) {
		Transaction transaction = getTransactionById(id);
		
		Budget budget = transaction.getBudget();
		budget.removeTransaction(transaction);
		
		transactionRepository.delete(transaction);
	}
	
	@Override
	public Transaction getTransactionById(Long id) {
		return transactionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("해당 거래가 존재하지 않습니다."));
	}
	
	@Override
	public List<Transaction> getTransactionList() {
		List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream()
                      .flatMap(budget -> transactionRepository.findByBudgetId(budget.getId()).stream())
                      .collect(Collectors.toList());
	}
	
	@Override
	public Page<TransactionDTO> getAllTransactions(int page) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
	    Page<Transaction> transactions = transactionRepository.findAll(pageable);
	    
	    return transactions.map(TransactionDTO::new);
	}

}
