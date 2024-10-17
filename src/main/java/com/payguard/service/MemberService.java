package com.payguard.service;

import com.payguard.domain.Member;

public interface MemberService {
	
	Member getMemberById(Long id);
	Member findByEmail(String email);
	Member insertMember(Member member);
	Member updateMember(Long id, Member memberDetails);
	void deleteMember(Long id);
	
}
