package com.payguard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.payguard.domain.Budget;
import com.payguard.domain.Member;
import com.payguard.persistence.BudgetRepository;
import com.payguard.persistence.MemberRepository;

import jakarta.transaction.Transactional;


@SpringBootTest
public class BudgetRepositoryTest {
	
	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private BudgetRepository budgetRepo;
	
	@Test
	@DisplayName("예산 등록 테스트")
	public void testBudgetInsert() {
		Member member = memberRepo.findById(1L).orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
		
		for (int i = 1; i <= 50; i++) {
			Budget budget = new Budget();
			budget.setMember(member);
			
			budget.setName("에산 테스트" + i);
			
			BigDecimal amount = new BigDecimal(i + "00000");
			budget.setAmount(amount);
			
			LocalDate startDate = LocalDate.now().plusDays(i * 7);
			LocalDate endDate = startDate.plusDays(7 + i);
			budget.setStartDate(startDate);
			budget.setEndDate(endDate);
			
			budgetRepo.save(budget);
		}
	}
	
	@Test
	@DisplayName("예산 조회 테스트")
	@Transactional
	public void testBudgetFind() {
		Member member = memberRepo.findById(1L).orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
		
		List<Budget> budgets = member.getBudgets();
		
		for (Budget budget : budgets) {
			System.out.println(budget.getAmount());
		}
		
	}
	
	
}
