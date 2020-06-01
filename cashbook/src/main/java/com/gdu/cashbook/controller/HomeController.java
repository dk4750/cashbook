package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	// 홈 페이지요청
	@GetMapping("/home")
	public String home(HttpSession session) {
		if(session.getAttribute("loginMember") == null && session.getAttribute("admin") == null) {
			return "redirect:/login";
		}
		return "home";
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/index";
	}
}
