package com.payguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payguard.domain.Member;
import com.payguard.persistence.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	
	public MemberServiceImpl(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@Override
	@Transactional
	public Member insertMember(Member member) {
		return memberRepository.save(member);
	}

	@Override
	@Transactional
	public Member updateMember(Long id, Member memberDetails) {
		Member member = getMemberById(id);
		
		member.setEmail(memberDetails.getEmail());
		member.setPassword(memberDetails.getPassword());
		member.setName(memberDetails.getName());
		
		return memberRepository.save(member);
	}

	@Override
	@Transactional
	public void deleteMember(Long id) {
		Member member = getMemberById(id);
		memberRepository.delete(member);
	}
	
	@Override
	public Member getMemberById(Long id) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
		return member;
	}

	@Override
	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}
}
