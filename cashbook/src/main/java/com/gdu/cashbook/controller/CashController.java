package com.gdu.cashbook.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired private CashService cashService;
	
	// 해당 회원의 날짜별 수입 지출 리스트 출력하는 폼 요청
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model) {
		// 세션 검사.. 로그인중이 아니라면 인덱스로 리턴
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 로그인 아이디와 날짜를 보내주어야 한다.
		// 아이디는 세션안에. 날짜는 캘린더 사용
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
		String loginMemberId = loginMember.getMemberId();
		System.out.println(loginMemberId + " <== loginMemberId");	// 디버깅
		
		// 날짜 .. subString 등 등 해야함
		Date today = new Date();
		System.out.println(today + " <== today");	// 디버깅
		
		// 오늘은 SimpleDateFormat을 사용
		// 원하는 날짜 형식을 정한 후 그 변수에 format(오늘 날짜)를 넣는다.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strToday = sdf.format(today);
		System.out.println(strToday + " <== strToday");
		
		//일회용  Cash 클래스 데이터타입
		Cash cash = new Cash();	// 세션안의 ID와 날짜타입 yyyy-MM-dd 입력
		cash.setMemberId(loginMemberId);
		cash.setCashDate(strToday);
		
		// 결과값 받은것을 모델에 담아서 페이지에 보내준다
		List<Cash> cashList = cashService.getCashListByDate(cash);
		System.out.println(cashList + " <== cashList");
		model.addAttribute("cashList", cashList);
		model.addAttribute("strToday", strToday);
		
		for(Cash c : cashList) {
			System.out.println(c);
		}
		
		// 페이지 요청
		return "getCashListByDate";
	}
}
