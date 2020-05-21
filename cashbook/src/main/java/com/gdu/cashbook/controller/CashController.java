package com.gdu.cashbook.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.service.CategoryService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.Category;
import com.gdu.cashbook.vo.DayAndPrice;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired private CashService cashService;
	@Autowired private CategoryService categoryService;
	
	// 거래내역 추가하는 폼 겟매핑
	@GetMapping("/addCash")
	public String addCash(HttpSession session, Model model, @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		System.out.println(day);
		// 세션검사.. 비로그인일시 인덱스로 리턴
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 카테테고리 이름들 리스트 불러와서 페이지로 보내주기
		List<Category> categoryList = categoryService.getCategoryName();
		System.out.println(categoryList + " <==categoryList");
		model.addAttribute("categoryList", categoryList);
		
		// 년도 표기때문에 모델에 담아서 보내줌
		// day는 값을 포스트로도 넘기기위해서.. 년도 저장
		int year = day.getYear();
		model.addAttribute("year", year);
		model.addAttribute("day", day);
		
		// 페이지요청
		return "addCash";
	}
	
	// 거래내역 인서트 액션.. 포스트매핑
	@PostMapping("/addCash")
	public String addCash(HttpSession session, Cash cash, @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		// 받아온 값들 디버깅
		System.out.println(cash + " <== cash data");
		System.out.println(day);
		
		// cash에 폼에서 직접 넘겨받지않은 데이터들 주입시키기
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		cash.setCashDate(day);
		cash.setMemberId(memberId);
		
		cashService.addCash(cash);
		
		// 페이지요청
		return "redirect:/getCashListByDate";
	}
	
	// 월별 달력출력하기. 캘린더타입이 필요하다.. 복잡한 날짜 계산은 캘린더가 좋다
	@GetMapping("/getCashListByMonth")
	public String getCashListByMonth(HttpSession session, Model model, @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		// 세션검사. 로그인되어있지 않다면 인덱스로 리다이렉트
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 입력된 값이 없을시 로컬시간으로 설정.
		Calendar calendarDay = Calendar.getInstance();
		if(day == null) {
			day = LocalDate.now();
		} else {
			// local -> calendar
			calendarDay.set(day.getYear(), day.getMonthValue()-1, day.getDayOfMonth());	// 오늘날짜에서 day날으로
		}
		
		// 날짜 디버깅
		System.out.println(day.getYear());
		System.out.println(day.getMonthValue()-1);
		System.out.println(day.getDayOfMonth() + " <== day.getDayOfMonth()");
		
		// 일자별 수입, 지출 합을 출력
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		int year = calendarDay.get(Calendar.YEAR);
		int month = calendarDay.get(Calendar.MONTH)+1;
		System.out.println(memberId);
		System.out.println(year);
		System.out.println(month);
		List<DayAndPrice> dayAndPriceList = cashService.getDayAndPriceList(memberId, year, month);
		System.out.println(dayAndPriceList);
		
		// 모델에 담아서 필요한 날짜를 보내주기
		model.addAttribute("dayAndPriceList", dayAndPriceList);
		model.addAttribute("day", day);
		model.addAttribute("year", calendarDay.get(Calendar.YEAR));
		model.addAttribute("month", calendarDay.get(Calendar.MONTH)+1);
		
		model.addAttribute("lastDay", calendarDay.getActualMaximum(Calendar.DATE));
		
		// 요일 구하는 메소드
		Calendar firstDay = calendarDay;
		firstDay.set(Calendar.DATE, 1); // 일을 1로 변경
		// firstDay.get(Calendar.DAY_OF_WEEK);	// 0:일 , 1:월, 2:화
		System.out.println(firstDay.get(Calendar.DAY_OF_WEEK) + " <== firstDay");
		model.addAttribute("firstDayOfWeek", firstDay.get(Calendar.DAY_OF_WEEK));
		
		// 날짜 보내주기
		System.out.println(calendarDay + " <== calendarDay");
		
		// 페이지 요청하가ㅣ
		return "getCashListByMonth";
	}
	
	// cash 수정 액션 .. POST Mapping
	@PostMapping("/modifyCash")
	public String modifyCash(HttpSession session, Cash cash) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 값 들어온거 디버깅
		System.out.println(cash);
		
		// 업데이트하기
		cashService.modifyCash(cash);
		
		// 페이지요청
		return "redirect:/getCashListByDate";
	}
	
	// cash 수정하는 겟매핑
	@GetMapping("/modifyCash")
	public String modifyCash(HttpSession session, @RequestParam("cashNo") int cashNo, @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day, Model model) {
		// 값 디버깅
		System.out.println(cashNo);
		System.out.println(day);
		
		// 년도 받아서 넘겨주기
		int year = day.getYear();
		model.addAttribute("year", year);
		
		// categoryName 리스트 받아오기
		List<Category> categoryList = categoryService.getCategoryName();
		System.out.println(categoryList);
		model.addAttribute("list", categoryList);
		
		// cashNo로 해당 정보 받아오기..
		Cash cash = cashService.getCashOne(cashNo);
		cash.setCashNo(cashNo);
		System.out.println(cash);
		model.addAttribute("cash", cash);
		
		// 페이지 요청
		return "modifyCash";
	}
	
	// cashNo를 받아서 해당 컬럼 삭제.. cash삭제 
	@GetMapping("/removeCash")
	public String removeCash(HttpSession session, @RequestParam("cashNo") int cashNo) {
		// 로그인 안되어있을 시 인덱스로 리턴
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		// get으로 받은 cashNo 디버깅, 삭제
		System.out.println(cashNo + " <== cashNo");
		cashService.removeCash(cashNo);
		
		// 다시 리스트로 리다이렉트
		return "redirect:/getCashListByDate";
	}
	
	// 해당 회원의 날짜별 수입 지출 리스트 출력하는 폼 요청
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model, @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		// day값을 넘겨받는데 넘겨받지 않을경우 now로 설정.
		if(day == null) {
			day = LocalDate.now();
		} else {
			
		}
		
		System.out.println(day + " <== day");
		// 세션 검사.. 로그인중이 아니라면 인덱스로 리턴
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 로그인 아이디와 날짜를 보내주어야 한다.
		// 아이디는 세션안에. 날짜는 캘린더 사용
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
		String loginMemberId = loginMember.getMemberId();
		System.out.println(loginMemberId + " <== loginMemberId");	// 디버깅
		
		/*
		// 날짜 .. subString 등 등 해야함
		Date day = new Date();
		System.out.println(day + " <== today");	// 디버깅
		
		// 오늘은 SimpleDateFormat을 사용
		// 원하는 날짜 형식을 정한 후 그 변수에 format(오늘 날짜)를 넣는다.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strToday = sdf.format(day);
		System.out.println(strToday + " <== strToday");
		*/
		
		//일회용  Cash 클래스 데이터타입
		Cash cash = new Cash();	// 세션안의 ID와 날짜타입 yyyy-MM-dd 입력
		cash.setMemberId(loginMemberId);
		cash.setCashDate(day);
		
		// 결과값 받은것을 모델에 담아서 페이지에 보내준다
		Map<String, Object> map = cashService.getCashListByDate(cash);
		model.addAttribute("cashList", map.get("cashList"));
		model.addAttribute("cashKindSum", map.get("cashKindSum"));
		model.addAttribute("day", day);
		model.addAttribute("memberId", loginMemberId);
		System.out.println(day.getYear());
		System.out.println(day);
		System.out.println(loginMemberId);
		
		/*	List 디버깅
		for(Cash c : cashList) {
			System.out.println(c);
		}
		*/
		
		// 페이지 요청
		return "getCashListByDate";
	}
}
