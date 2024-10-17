package com.payguard.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.payguard.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
	Page<Budget> findAll(Pageable pageable);

	// 날짜를 통해 예산 조회
	@Query("SELECT b FROM Budget b WHERE (b.startDate <= :date AND b.endDate >= :date)")
	Optional<Budget> findByDate(@Param("date") LocalDate date);
	
	// 날짜 범위가 겹치는 Budget을 조회
    @Query("SELECT b FROM Budget b WHERE (b.startDate <= :endDate AND b.endDate >= :startDate)")
    List<Budget> findOverlappingBudgets(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // 최신순으로 예산 조회
    @Query("SELECT b FROM Budget b ORDER BY b.createdAt DESC")
    Page<Budget> findTopBudgets(Pageable pageable);
}
