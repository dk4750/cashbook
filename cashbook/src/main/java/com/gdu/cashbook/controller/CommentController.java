package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.CommentService;
import com.gdu.cashbook.vo.Comment;

@Controller
public class CommentController {
	@Autowired private CommentService commentService;
	
	// 댓글 추가
	@PostMapping("/addComment")
	public String addComment(HttpSession session, Comment comment) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		// 디버깅
		System.out.println(comment + " <== 입력받은 댓글 내용");
		
		// 인서트하기
		commentService.addComment(comment);
		
		// getBoardOne 으로 리다이렉트 위해서 boardNo 받아와서 보내주기
		int boardNo = comment.getBoardNo();
		System.out.println(boardNo);
		
		return "redirect:/getBoardOne?boardNo="+boardNo;
	}
	
	// 댓글 수정
	@PostMapping("/modifyComment")
	public String modifyComment(HttpSession session, Comment comment) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		// 들어온 값 디버깅
		System.out.println(comment);
		System.out.println(comment.getBoardNo());
		int boardNo = comment.getBoardNo();
		// 업데이트
		commentService.modifyComment(comment);
		
		return "redirect:/getBoardOne?boardNo="+boardNo;
	}
	
	// 댓글 삭제
	@PostMapping("/removeComment")
	public String removeComment(HttpSession session, Comment comment) {
		// 세션검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		
		// 들어온 값 디버깅
		System.out.println(comment);
		System.out.println(comment.getBoardNo());
		int boardNo = comment.getBoardNo();
		
		// 삭제
		commentService.removeComment(comment);
		
		//페이지요청
		return "redirect:/getBoardOne?boardNo="+boardNo;
	}
}
