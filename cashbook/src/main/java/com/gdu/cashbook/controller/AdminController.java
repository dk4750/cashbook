package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.AdminService;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class AdminController {
	@Autowired private AdminService adminService;
	
	// 관리자 로그인 페이지요청
	@GetMapping("/adminLogin")
	public String adminLogin(HttpSession session) {
		// 로그인상태일때
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/index";
		}
		
		// 로그인상태가 아닐때
		return "adminLogin";
	}
	
	// 로그인 액션
	@PostMapping("/adminLogin")
	public String login(LoginMember loginMember, HttpSession session, Model model) {	// HttpSession session = request.getSession();
		/*
		 * System.out.println(loginMember); LoginMember returnLoginMember =
		 * memberService.login(loginMember); System.out.println("returnLoginMember : " +
		 * returnLoginMember); if(returnLoginMember == null) { // 로그인 실패시
		 * model.addAttribute("msg", "아이디와 비밀번호를 확인하세요"); return "login"; } else { //
		 * 로그인 성공시 session.setAttribute("adminLogin", returnLoginMember); return
		 * "redirect:/home"; }
		 */
		return null;
	}
}
