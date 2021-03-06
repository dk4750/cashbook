package com.gdu.cashbook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	// 멤버 추방
	@GetMapping("/removeByAdmin")
	public String removeByAdmin(HttpSession session, @RequestParam("memberId") String memberId) {
		System.out.println(memberId);
		
		memberService.removeByAdmin(memberId);
		
		return "redirect:/getMemberList";
	}
	
	// 멤버 리스트 전체출력
	@GetMapping("/getMemberList")
	public String getMemberList(HttpSession session, Model model,@RequestParam(value="currentPage", defaultValue = "1") int currentPage, @RequestParam(value="searchWord", defaultValue = "") String searchWord) {
		// 세션검사
		if(session.getAttribute("admin") == null || session.getAttribute("loginMember") != null) {
			return "redirect:/home";
		}
		
		// 들어온 커런트페이지 디버깅
		System.out.println(currentPage + " <== currentPage");
		System.out.println(searchWord + " <== searchWord");
		
		Map<String, Object> mapp = new HashMap<String, Object>();
		mapp.put("currentPage", currentPage);
		mapp.put("searchWord", searchWord);
		
		// 리스트 출력.. lastPage때문에 맵타입으로 받음
		Map<String, Object> map = memberService.getMemberListAll(mapp);
		
		// 모델에 list 담아서 보내기.
		model.addAttribute("list", map.get("list"));
		model.addAttribute("lastPage", map.get("lastPage"));
		model.addAttribute("currentPage", currentPage);
		
		// 페이지요청
		return "getMemberList";
	}
	
	// 멤버 비밀번호 찾기 겟매핑
	@GetMapping("/findMemberPw")
	public String findMemberPw(HttpSession session) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		return "findMemberPw";
	}
	
	// 멤버 비밀번호 찾기 포스트액션
	@PostMapping("/findMemberPw")
	public String findMemberPw(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		int row = memberService.getMemberPw(member);
		String msg = "";
		String msg2 = "";
		if(row == 1) {
			msg2 = "해당 이메일로 임시비밀번호를 전송하였습니다. ";
			model.addAttribute("msg2", msg2);
		} else {
			msg = "회원님의 입력정보와 일치하는 정보가 없습니다.";
		}
		model.addAttribute("msg", msg);
		
		return "findMemberPw";
	}
	
	// 멤버 아이디 찾기 겟매핑
	@GetMapping("/findMemberId")
	public String findMemberId(HttpSession session) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		return "findMemberId";
	}
	
	// 멤버 아이디 찾기 포스트액션
	@PostMapping("/findMemberId")
	public String findMemberId(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		String subMemberId = memberService.getMemberIdByMember(member);
		if(subMemberId == null) {
			model.addAttribute("msg", "입력하신 정보와 일치하는 아이디가 없습니다.");
			return "findMemberId";
		}
		subMemberId = "회원님의 아이디는 " + subMemberId + " 입니다";
		model.addAttribute("subMemberId", subMemberId);
		return "findMemberId";
	}
	
	// 멤버 삭제 액션 (포스트)
	@PostMapping("/removeMember")
	public String removeMember(HttpSession session, Model model, LoginMember loginMember) {
		System.out.println(loginMember.getMemberPw() + " <== loginMemberPw");
		int result = memberService.removeMember(loginMember);
		System.out.println(result);
		if(result == 1) {
			session.invalidate();
			return "redirect:/index";
		} else {
			model.addAttribute("msg", "회원 정보가 일치하지 않습니다.");
			return "removeMember";
		}
	}
	
	// 멤버 삭제 폼 겟
	@GetMapping("/removeMember")
	public String removeMember(HttpSession session, LoginMember loginMember, Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/login";
		}
		model.addAttribute("loginMember", loginMember);
		
		return "removeMember";
	}
	
	// 멤버 정보 수정 폼 겟
	@GetMapping("/modifyMember")
	public String modifyMember(HttpSession session, Model model, LoginMember loginMember) {
		System.out.println(loginMember);
		String loginMemberId = loginMember.getMemberId();
		Member member = memberService.getMemberOneAll(loginMember);
		System.out.println(member);
		
		model.addAttribute("loginMemberId", loginMemberId);
		model.addAttribute("member", member);
		
		return "modifyMember";
	}
	
	// 멤버 수정 포스트
	@PostMapping("/modifyMember")
	public String modifyMember(MemberForm memberForm) {
		System.out.println("memberForm : " + memberForm);
		
		memberService.modifyMember(memberForm);
		
		return "redirect:/memberInfo";
	}
	
	// 멤버 한명의 상세정보 출력하는 폼
	@GetMapping("/memberInfo")
	public String memberInfo(HttpSession session, Model model) {
		// 비로그인 상태일시 로그인창으로 리디렉트
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/login";
		}
		
		// 리턴받은 멤버를 저장하고 모델에 담아서 보내주기
		// session.getAttribute 는 오브젝트타입이기때문에 형변환이 필요하다..
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
		Member member = memberService.getMemberOne(loginMember);
		model.addAttribute("member", member);
		
		// 멤버상세정보 폼으로 이동
		return "memberInfo";
	}
	
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
	public String addMember(MemberForm memberForm, Model model) {	// 커맨드객체 폼에서 입력되는값을 하나의 데이터타입으로 받기위한 객체.. 도메인객체
		// System.out.println(member);	// 디버깅  .. toString() 메소드떄문에 member만 입력해도 다 출력된다.
		
		System.out.println(memberForm + " <-- memberForm");
		boolean flag = memberService.addMember(memberForm);
		if(flag == false) {
			model.addAttribute("msg3", "이미지 파일만 업로드 하실 수 있습니다.");
			return "/addMember";
		}
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
			return "redirect:/home";
		}
	}
}