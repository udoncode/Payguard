package com.payguard.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.payguard.domain.Budget;
import com.payguard.domain.Transaction;
import com.payguard.transaction.dto.TransactionDTO;

public interface TransactionService {
	
	Transaction insertTransaction(Transaction transaction);
	Transaction updateTransaction(Long id, Transaction transactionDetails);
	void deleteTransaction(Long id);
	Transaction getTransactionById(Long id);
	List<Transaction> getTransactionList();
	Page<TransactionDTO> getAllTransactions(int page);
	
}
