package com.gdu.cashbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	// 겟방식
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
}