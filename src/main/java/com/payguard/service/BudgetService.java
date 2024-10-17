package com.payguard.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.payguard.domain.Budget;
import com.payguard.domain.Member;

public interface BudgetService {
	
	Budget insertBudget(Budget budget);
	Budget updateBudget(Long id, Budget budgetDetails);
	void deleteBudget(Long id);
	Budget getBudgetById(Long id);
	List<Budget> getAllBudgets();
	Page<Budget> getBudgetList(Member member, int page);
	List<Budget> getRecentBudgets(int size);
}
