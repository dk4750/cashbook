package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	// 겟방식 페이지요청
	@GetMapping("/addMember")
	public String addMember() {
		return "addMember";
	}
	
	// 포스트방식. 오버로딩(매개변수)
	@PostMapping("/addMember")
	public String addMember(Member member) {	// 커맨드객체 폼에서 입력되는값을 하나의 데이터타입으로 받기위한 객체.. 도메인객체
		System.out.println(member);	// 디버깅  .. toString() 메소드떄문에 member만 입력해도 다 출력된다.
		memberService.addMember(member);
		return "redirect:/index";
	}
	
	// 로그인 페이지요청 폼요청
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	// 로그인 액션
	@PostMapping("/login")
	public String login(LoginMember loginMember, HttpSession session) {	// HttpSession session = request.getSession();
		
		System.out.println(loginMember);
		LoginMember returnLoginMember = memberService.login(loginMember);
		System.out.println("returnLoginMember : " + returnLoginMember);
		if(returnLoginMember == null) {	// 로그인 실패시
			return "redirect:/login";
		} else {	// 로그인 성공시
			session.setAttribute("loginMember", returnLoginMember);
			return "redirect:/index";
		}
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/index";
	}
}