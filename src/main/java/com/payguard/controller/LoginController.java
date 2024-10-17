package com.payguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.payguard.domain.Member;
import com.payguard.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/login")
	public String login() {
		return "member/login";
	}
	
	@PostMapping("/login")
	public String loginMember(String email, String password, HttpServletRequest request, Model model) {
		Member member = memberService.findByEmail(email);
		
		if(member != null && member.getPassword().equals(password)) {
			// 로그인 성공
			HttpSession session = request.getSession();
			session.setAttribute("member", member);
			return "redirect:/budget";
		} else { 
			// 로그인 실패
			model.addAttribute("error", "잘못된 이메일 또는 비밀번호입니다.");
			return "/member/login";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if (session != null) {
			session.invalidate(); // 세션 종료
		}
		
		return "redirect:/";
	}
	
}
