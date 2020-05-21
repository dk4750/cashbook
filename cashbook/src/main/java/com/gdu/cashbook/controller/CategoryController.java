package com.gdu.cashbook.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.CategoryService;
import com.gdu.cashbook.vo.Category;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CategoryController {
	@Autowired private CategoryService categoryService;
	
	// 카테고리 삭제 포스트매핑
	@PostMapping("/removeCategory")
	public String removeCategory(HttpSession session, @RequestParam(value="day", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate day, Category category) {
		// 날짜 디버깅
		System.out.println(day);
		System.out.println(category);
		
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 삭제 필요한정보들 가져오기
		String categoryName = category.getCategoryName();
		String memberId = ((LoginMember)(session.getAttribute("loginMember"))).getMemberId();
		System.out.println(memberId);
		System.out.println(categoryName);
		
		categoryService.removeCategory(categoryName, memberId);
		
		return "redirect:/addCash?day="+day;
	}
	
	// 카테고리 삭제 겟매핑
	@GetMapping("/removeCategory")
	public String removeCategory(HttpSession session, @RequestParam(value="day", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate day, Model model) {
		// 디버깅
		System.out.println(day);
		
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 멤버 아이디 받아오기.
		String memberId = ((LoginMember)(session.getAttribute("loginMember"))).getMemberId();
		System.out.println(memberId);
		List<Category> list = categoryService.getCategoryName(memberId);
		
		// 카테고리 목록과 날짜를 보내준다.
		model.addAttribute("list", list);
		model.addAttribute("day", day);
		
		// 페이지요청
		return "removeCategory";
	}
	
	// 카테고리 추가 겟매핑
	@GetMapping("/addCategory")
	public String addCategory(HttpSession session, @RequestParam(value="day", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate day, Model model) {
		// day 디버깅
		System.out.println(day);
		
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 모델에 날짜 담아서 보내주기.. post에서 받기위해서
		model.addAttribute("day", day);
		
		// 페이지 요청
		return "addCategory";
	}
	
	// 카테고리 추가 포스트매핑
	@PostMapping("/addCategory")
	public String addCategory(HttpSession session,Category category, @RequestParam(value="day", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate day) {
		// 날짜 디버깅
		System.out.println(day + " <== addCategory.post");
		System.out.println(category.getCategoryName());
		
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 멤버 아이디 받아오기. 해당 멤버의 카테고리가 추가되는것이기 때문에
		String memberId = ((LoginMember)(session.getAttribute("loginMember"))).getMemberId();
		System.out.println(memberId);
		
		// 폼에서 카테고리네임 받아오기
		String categoryName = category.getCategoryName();
		System.out.println(categoryName);
		
		// 카테고리 추가
		categoryService.addCategory(categoryName, memberId);
		
		return "redirect:/addCash?day="+day;
	}
}
