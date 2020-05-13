package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;

	// 아이디 중복확인
	@PostMapping("/checkMemberId")
	public String checkMemberId(@RequestParam("memberIdCheck") String memberIdCheck, Model model) {
		String memberId = memberService.CheckMemberId(memberIdCheck);
		if(memberId == null) {
			// 아이디 사용 가능
			System.out.println("아이디 사용 가능");
			model.addAttribute("memberIdCheck", memberIdCheck);
		} else {
			// 아이디 사용 불가
			System.out.println("아이디 사용 불가");
			model.addAttribute("msg", "사용 할 수 없는 아이디입니다.");
		}
		return "addMember";
	}
	
	// 겟방식 페이지요청
	@GetMapping("/addMember")
	public String addMember(HttpSession session) {
		// 로그인상태일때
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		// 로그인 상태가 아닐때
		return "addMember";
	}
	
	// 포스트방식. 오버로딩(매개변수) 인서트 액션
	@PostMapping("/addMember")
	public String addMember(Member member) {	// 커맨드객체 폼에서 입력되는값을 하나의 데이터타입으로 받기위한 객체.. 도메인객체
		System.out.println(member);	// 디버깅  .. toString() 메소드떄문에 member만 입력해도 다 출력된다.
		memberService.addMember(member);
		return "redirect:/index";
	}
	
	// 로그인 페이지요청 폼요청
	@GetMapping("/login")
	public String login(HttpSession session) {
		// 로그인상태일때
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		
		// 로그인상태가 아닐때
		return "login";
	}
	
	// 로그인 액션
	@PostMapping("/login")
	public String login(LoginMember loginMember, HttpSession session, Model model) {	// HttpSession session = request.getSession();
		
		System.out.println(loginMember);
		LoginMember returnLoginMember = memberService.login(loginMember);
		System.out.println("returnLoginMember : " + returnLoginMember);
		if(returnLoginMember == null) {	// 로그인 실패시
			model.addAttribute("msg", "아이디와 비밀번호를 확인하세요");
			return "login";
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