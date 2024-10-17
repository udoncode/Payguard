package com.payguard.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payguard.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmail(String email);
}
