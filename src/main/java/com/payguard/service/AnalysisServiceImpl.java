package com.payguard.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.payguard.constant.Category;
import com.payguard.constant.Type;
import com.payguard.domain.Budget;
import com.payguard.domain.Transaction;

@Service
public class AnalysisServiceImpl implements AnalysisService {
	private final BudgetService budgetService;
	
	
	public AnalysisServiceImpl(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	@Override
	public List<Budget> getBudgetList() {
        return budgetService.getAllBudgets();
    }
	
	@Override
	public BigDecimal getBudgetAmount(Long id) {
		Budget budget = budgetService.getBudgetById(id);
		return budget.getAmount();
	}
	
	@Override
	public BigDecimal getBudgetExpense(Long id) {
		Budget budget = budgetService.getBudgetById(id);
		List<Transaction> transactions = budget.getTransactions();
		return transactions.stream()
				.filter(transaction -> transaction.getType() == Type.EXPENSE)
				.map(Transaction::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	@Override
	public BigDecimal getBudgetRemainingAmount(Long id) {
		Budget budget = budgetService.getBudgetById(id);
		return budget.getRemainingAmount();
	}
	
	@Override
	public List<Object[]> getTransactionsAmount(Long id) {
		Budget budget = budgetService.getBudgetById(id);
		List<Transaction> transactions = budget.getTransactions();
		List<Object[]> amountList = transactions.stream()
				.map(transaction -> new Object[] {transaction.getDate().toString(), transaction.getAmount()})
				.collect(Collectors.toList());
		return amountList;
	}
	
	@Override
	public List<Object[]> getRecentBudgetLists() {
		List<Budget> budgetList = budgetService.getRecentBudgets(5);
		List<Object[]> budgetData = new ArrayList<>();
		
		for (Budget budget : budgetList) {
			budgetData.add(new Object[]{budget.getName(), budget.getAmount(), "color: #1446FF"});
		}
		
		return budgetData;
	}
	
	@Override
	public List<Object[]> getCategoryCount(Long id) {
		Budget budget = budgetService.getBudgetById(id);
		List<Transaction> transactions = budget.getTransactions();
		
		long living = transactions.stream()
				.filter(transaction -> transaction.getCategory() == Category.LIVING)
				.count();
		
		long leisure = transactions.stream()
				.filter(transaction -> transaction.getCategory() == Category.LEISURE_CULTURE)
				.count();
		
		long savings = transactions.stream()
				.filter(transaction -> transaction.getCategory() == Category.SAVINGS_INVESTMENT)
				.count();
		
		long medical = transactions.stream()
				.filter(transaction -> transaction.getCategory() == Category.MEDICAL_EDUCATION)
				.count();
		
		List<Object[]> categoryData = new ArrayList<>();
	    categoryData.add(new Object[]{"생활비", living});
	    categoryData.add(new Object[]{"여가 및 문화비", leisure});
	    categoryData.add(new Object[]{"저축 및 투자비", savings});
	    categoryData.add(new Object[]{"의료 및 교육비", medical});
	    
	    return categoryData;
	}
}
