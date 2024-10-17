package com.payguard.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.payguard.domain.Budget;
import com.payguard.domain.Member;
import com.payguard.service.AnalysisService;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
	
	private final AnalysisService analysisService;
	
	public AnalysisController(AnalysisService analysisService) {
		this.analysisService = analysisService;
	}

	@GetMapping
	public String analysis(@SessionAttribute("member") Member member, Model model) {
		if(member != null) {
			List<Budget> budgetList = analysisService.getBudgetList();
			model.addAttribute("list", budgetList);
			return "analysis/analysis";
		} else {
			return "redirect:/login";
		}
	}
	
	
	@GetMapping("/budget")
	@ResponseBody
	public Map<String, Object> analyzeBudget(@RequestParam("id") Long budgetId) {
		BigDecimal amount = analysisService.getBudgetAmount(budgetId);
		BigDecimal expense = analysisService.getBudgetExpense(budgetId);
		BigDecimal remainingAmount = analysisService.getBudgetRemainingAmount(budgetId);
		
		int progressPercent = 0;
		if (amount.intValue() - remainingAmount.intValue() < 0) {
		    progressPercent = 0;
		} else {
		    progressPercent = Math.round((amount.intValue() - remainingAmount.intValue()) / (float)amount.intValue() * 100);
		}
		
		List<Object[]> transactionAmounts = analysisService.getTransactionsAmount(budgetId);
		List<Object[]> recentBudgets = analysisService.getRecentBudgetLists();
		List<Object[]> categoryCounts = analysisService.getCategoryCount(budgetId);
		
		Map<String, Object> response = new HashMap<>();
		response.put("amount", amount);
		response.put("expense", expense);
		response.put("remainingAmount", remainingAmount);
		response.put("progressPercent", progressPercent);
		response.put("transactionAmounts", transactionAmounts);
		response.put("recentBudgets", recentBudgets);
		response.put("categoryCounts", categoryCounts);
		
		return response;
	}
	
}
