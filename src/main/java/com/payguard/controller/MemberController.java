package com.payguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.payguard.domain.Member;
import com.payguard.service.MemberService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("member", new Member()); // 빈 회원 객체 추가
		return "/member/signup"; // 회원가입 폼으로 이동
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute Member member, BindingResult bindingResult, Model model) {
		// 유효성 검사 결과 처리
		if (bindingResult.hasErrors()) {
			model.addAttribute("member", member);
			return "member/signup";
		}
		
		if (memberService.findByEmail(member.getEmail()) == null) { // 이메일 중복 확인
			memberService.insertMember(member); // 회원 등록
			return "redirect:/login";	
		} else {
			model.addAttribute("error", "이미 등록된 이메일입니다.");
			return "/member/signup";
		}
	}
	
	@GetMapping("/my-info/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Member member = memberService.getMemberById(id);
		model.addAttribute("member", member);
		return "member/my-info";
	}
	
	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @ModelAttribute Member memberDetails) {
		memberService.updateMember(id, memberDetails); // 회원 정보 수정
		return "redirect:/budget";
	}
	
}
