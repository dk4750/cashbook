package com.gdu.cashbook.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.BoardService;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	// 게시글 삭제 포스트액션
	@PostMapping("/removeBoard")
	public String removeBoard(HttpSession session, @RequestParam(value="boardNo") int boardNo) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 디버깅
		System.out.println(boardNo + " <== boardNo from removeBoard");
		
		// 삭제
		boardService.removeBoard(boardNo);
		
		// 페이지요청
		return "redirect:/getBoardList";
	}
	
	// 게시글 수정 포스트
	@PostMapping("/modifyBoard")
	public String modifyBoard(HttpSession session, Board board) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		//들어온 값 디버깅
		System.out.println(board + " <== board FROM modifyBoard");
		
		// 수정 실행
		boardService.modifyBoard(board);
		
		// 페이지요청
		return "redirect:/getBoardOne?boardNo="+board.getBoardNo();
	}
	
	// 게시글 수정
	@GetMapping("/modifyBoard")
	public String modifyBoard(HttpSession session, @RequestParam(value="boardNo") int boardNo, Model model) {
		// 디버깅
		System.out.println(boardNo + " <== boardNo from modify");
		
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 멤버 한명 상세정보 출력
		Map<String, Object> map = boardService.getBoardOne(boardNo);
		model.addAttribute("board", map.get("board"));
		
		// 페이지 요청
		return "modifyBoard";
	}
	
	// 게시글 추가
	@PostMapping("/addBoard")
	public String addBoard(HttpSession session, Board board) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 들어온 값 디버깅
		System.out.println(board);
		
		// 인서트 실행
		boardService.addBoard(board);
		
		// 리다이렉트
		return "redirect:/getBoardList";
	}
	
	// 게시글 추가.. 작성자는 본인
	@GetMapping("/addBoard")
	public String addBoard(HttpSession session, Model model) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 멤버아이디 고정.로그인아이디로
		String memberId = ((LoginMember)(session.getAttribute("loginMember"))).getMemberId();
		model.addAttribute("memberId", memberId);
		
		// 페이지요청
		return "addBoard";
	}
	
	// 게시글 상세정보
	@GetMapping("/getBoardOne")
	public String getBoardOne(HttpSession session, Model model, @RequestParam(value="boardNo") int boardNo, @RequestParam(value="currentPage", defaultValue = "1") int currentPage) {
		// 들어온 값 디버깅
		System.out.println(boardNo);
		System.out.println(currentPage);
		
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// 로그인멤버아이디
		String loginMemberId = ((LoginMember)(session.getAttribute("loginMember"))).getMemberId();
		
		// 상세정보 출력, 모델에 담아서 보내주기
		Map<String, Object> map = boardService.getBoardOne(boardNo);
		System.out.println(map.get("board"));
		System.out.println(map.get("lastBoardNo"));
		model.addAttribute("loginMemberId", loginMemberId);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("board", map.get("board"));
		model.addAttribute("firstBoardNo", map.get("firstBoardNo"));
		model.addAttribute("lastBoardNo", map.get("lastBoardNo"));
		model.addAttribute("nextNo", map.get("nextNo"));
		model.addAttribute("previousNo", map.get("previousNo"));
		
		
		// 페이지요청
		return "getBoardOne";
	}
	
	// 게시판 리스트출력, 페이징
	@GetMapping("/getBoardList")
	public String getBoardList(HttpSession session, Model model,@RequestParam(value="currentPage", defaultValue = "1") int currentPage, @RequestParam(value="searchWord", defaultValue = "") String searchWord) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/index";
		}
		
		// searchWord 디버깅
		System.out.println(searchWord);
		
		// currentPage받아온 값 디버깅
		System.out.println(currentPage);
		
		// 리스트 디버깅
		Map<String, Object> map = boardService.getBoardList(currentPage, searchWord);
		System.out.println(map.get("list"));
		System.out.println(map.get("lastPage"));
		
		// model에 list 담아서 보내주기
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("lastPage", map.get("lastPage"));
		model.addAttribute("currentPage", currentPage);
		
		return "getBoardList";
	}
}
