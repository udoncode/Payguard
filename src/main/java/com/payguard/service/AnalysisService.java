package com.payguard.service;

import java.math.BigDecimal;
import java.util.List;

import com.payguard.domain.Budget;

public interface AnalysisService {
	
	List<Budget> getBudgetList();

	BigDecimal getBudgetAmount(Long id);

	BigDecimal getBudgetExpense(Long id);
	
	BigDecimal getBudgetRemainingAmount(Long id);

	List<Object[]> getTransactionsAmount(Long id);

	List<Object[]> getRecentBudgetLists();

	List<Object[]> getCategoryCount(Long id);

}