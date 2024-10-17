package com.payguard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.payguard.budget.dto.BudgetDTO;
import com.payguard.domain.Budget;
import com.payguard.domain.Member;
import com.payguard.service.BudgetService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/budget")
public class BudgetController {
	
	private final BudgetService budgetService;
	
	public BudgetController(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	@GetMapping
	public String budget(
			@SessionAttribute("member") Member member,
			@RequestParam(value="page", defaultValue="0") int page, 
			Model model) {
		if(member != null) {
			Page<Budget> paging = budgetService.getBudgetList(member, page);
			model.addAttribute("list", paging);
			return "budget/budget";
		} else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/form")
	public String showBudgetForm(Model model) {
		model.addAttribute("budget", new Budget());
		return "budget/budget-form";
	}
	
	@GetMapping("/form/{id}")
	public String getBudgetDetail(@PathVariable Long id, Model model) {
		Budget budget = budgetService.getBudgetById(id);
		model.addAttribute("budget", budget);
		return "budget/budget-edit";
	}
	
	@PostMapping("/add")
	public String insertBudget(
			@SessionAttribute("member") Member member, 
			@Valid @ModelAttribute Budget budget, 
			BindingResult bindingResult, 
			Model model) {
		if(bindingResult.hasErrors()) {
			return "budget/budget-form";
		}
			budget.setMember(member);
			budgetService.insertBudget(budget);	
		
		return "redirect:/budget";
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBudget(@PathVariable Long id) {
		try {
			budgetService.deleteBudget(id);
			return ResponseEntity.ok("예산 삭제 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("예산 삭제 실패");
		}
	}
	
//	@PutMapping("/{id}")
//	public ResponseEntity<Object> updateBudget(@SessionAttribute("member") Member member, @PathVariable Long id, @Validated @RequestBody BudgetDTO budgetDTO, BindingResult bindingResult) {
//		if (bindingResult.hasErrors()) {
//	        Map<String, String> errorResponse = new HashMap<>();
//	        errorResponse.put("message", "입력값이 유효하지 않습니다.");
//	        return ResponseEntity.badRequest().body(errorResponse);
//	    }
//		
//		Budget budgetDetails = new Budget();
//		budgetDetails.setId(budgetDTO.getId());
//		budgetDetails.setName(budgetDTO.getName());
//		budgetDetails.setAmount(budgetDTO.getAmount());
//		budgetDetails.setRemainingAmount(budgetDTO.getAmount());
//		budgetDetails.setStartDate(budgetDTO.getStartDate());
//		budgetDetails.setEndDate(budgetDTO.getEndDate());
//		budgetDetails.setMember(member);
//		
//		try {
//			Budget updatedBudget = budgetService.updateBudget(id, budgetDetails);
//			return ResponseEntity.ok(updatedBudget);
//		} catch (Exception e) {
//			Map<String, String> errorResponse = new HashMap<>();
//			errorResponse.put("message", "예산 수정에 실패했습니다. 이전에 등록한 예산 날짜와 겹치지 않는지 확인해주세요.");
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//		}
//	}
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateBudget(@SessionAttribute("member") Member member, 
	                                            @PathVariable Long id, 
	                                            @RequestBody @Validated BudgetDTO budgetDTO) {
	    // 런타임 예외를 던지면 글로벌 핸들러가 처리함
	    Budget budgetDetails = new Budget();
	    budgetDetails.setId(budgetDTO.getId());
	    budgetDetails.setName(budgetDTO.getName());
	    budgetDetails.setAmount(budgetDTO.getAmount());
	    budgetDetails.setRemainingAmount(budgetDTO.getAmount());
	    budgetDetails.setStartDate(budgetDTO.getStartDate());
	    budgetDetails.setEndDate(budgetDTO.getEndDate());
	    budgetDetails.setMember(member);
	    
	    Budget updatedBudget = budgetService.updateBudget(id, budgetDetails);
	    return ResponseEntity.ok(updatedBudget);
	}
}
