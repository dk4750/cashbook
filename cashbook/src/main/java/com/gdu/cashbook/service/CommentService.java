package com.gdu.cashbook.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.CommentMapper;
import com.gdu.cashbook.vo.Comment;

@Service
@Transactional
public class CommentService {
	@Autowired private CommentMapper commentMapper;
	
	// 댓글 삭제하기
	public int removeComment(Comment comment) {
		return commentMapper.removeComment(comment);
	}
	
	// commentList 출력하기
	public List<Comment> getCommentList(Map<String, Object> map) {
		return commentMapper.getCommentList(map);
	}
	
	// 댓글 입력하기
	public int addComment(Comment comment) {
		return commentMapper.addComment(comment);
	}
	
	// 댓글 수정하기
	public int modifyComment(Comment comment) {
		System.out.println(comment.getCommentContent());
		return commentMapper.modifyComment(comment);
	}
}
