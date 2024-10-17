package com.payguard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.payguard.domain.Member;
import com.payguard.persistence.MemberRepository;

@SpringBootTest
public class MemberRepositoryTest {
	
	@Autowired
	private MemberRepository memberRepo;
	
	@Test
	@DisplayName("회원 등록 테스트")
	public void insertMemberTest() {
		Member member = new Member();
		member.setEmail("abcd@test.com");
		member.setPassword("1234");
		member.setName("홍길동");
		memberRepo.save(member);
	}
	
	@Test
	@DisplayName("회원 수정 테스트")
	public void updateMemberTest() {
		Member member = memberRepo.findById(1L).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
		member.setEmail("asdf@test.com");
		member.setPassword("5678");
		member.setName("이몽룡");
		memberRepo.save(member);
		
		Assertions.assertEquals("asdf@test.com", member.getEmail());
		Assertions.assertEquals("5678", member.getPassword());
		Assertions.assertEquals("이몽룡", member.getName());
	}
}
