package com.payguard.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.payguard.domain.Budget;
import com.payguard.domain.Member;
import com.payguard.persistence.BudgetRepository;
import com.payguard.persistence.MemberRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BudgetServiceImpl implements BudgetService {

	private final BudgetRepository budgetRepository;
	private final MemberRepository memberRepository;
	
	public BudgetServiceImpl(BudgetRepository budgetRepository, MemberRepository memberRepository) {
		this.budgetRepository = budgetRepository;
		this.memberRepository = memberRepository;
	}
	
	@Override
	public Budget insertBudget(Budget budget) {
	    // 입력받은 예산의 시작일과 종료일
	    LocalDate startDate = budget.getStartDate();
	    LocalDate endDate = budget.getEndDate();
	    
	    // 겹치는 예산이 있는지 체크
	    List<Budget> overlappingBudgets = budgetRepository.findOverlappingBudgets(startDate, endDate);
	    if (!overlappingBudgets.isEmpty()) {
	        throw new RuntimeException("해당 기간에 이미 존재하는 예산이 있습니다.");
	    }
	    
		return budgetRepository.save(budget);
	}

	@Override
	public Budget updateBudget(Long id, Budget budgetDetails) {
		Budget budget = getBudgetById(id);
		
		// 예산에 거래 내역이 존재한다면 수정 불가
		if (!budget.getTransactions().isEmpty()) {
			throw new RuntimeException("예산을 수정하려면, 해당 예산에 거래 내역이 없어야 합니다.");
		}
		
		LocalDate newStartDate = budgetDetails.getStartDate();
		LocalDate newEndDate = budgetDetails.getEndDate();
		
		LocalDate oldStartDate = budget.getStartDate();
		LocalDate oldEndDate = budget.getEndDate();
		
		// 예산 리스트에서 겹치는 날짜의 예산이 있는지 체크
		List<Budget> overlappingBudgets = budgetRepository.findOverlappingBudgets(newStartDate, newEndDate);
		
		// 겹치는 예산 리스트가 존재하는데
		if(!overlappingBudgets.isEmpty()) {
			// 해당 예산의 날짜가 수정하려는 예산의 날짜와 같지 않다면 예외처리!
			if(!(oldStartDate.isEqual(newStartDate) && oldEndDate.isEqual(newEndDate))) {
				throw new RuntimeException("해당 기간에 이미 존재하는 예산이 있습니다.");
			}
		}
		
		budget.setAmount(budgetDetails.getAmount());
		budget.setRemainingAmount(budgetDetails.getRemainingAmount());
		budget.setName(budgetDetails.getName());
		budget.setStartDate(newStartDate);
		budget.setEndDate(newEndDate);

		return budgetRepository.save(budget);
	}

	@Override
	public void deleteBudget(Long id) {
		Budget budget = getBudgetById(id);
		budgetRepository.delete(budget);
	}

	@Override
	public Budget getBudgetById(Long id) {
		return budgetRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("예산 계획이 존재하지 않습니다."));
	}
	
	@Override
	public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

	@Override
	public Page<Budget> getBudgetList(Member member, int page) {
		Member foundMember = memberRepository.findById(member.getId())
				.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

		Pageable pageable = PageRequest.of(page, 7, Sort.by("createdAt").descending());
		return budgetRepository.findAll(pageable);
	}

	@Override
	public List<Budget> getRecentBudgets(int size) {
		Page<Budget> budgetPage = budgetRepository.findTopBudgets(PageRequest.of(0, size));
		return budgetPage.getContent();
	}
}
