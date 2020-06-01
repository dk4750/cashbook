package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Comment;

@Mapper
public interface CommentMapper {
	// Comment List 출력
	public List<Comment> getCommentList(Map<String, Object> map);
	
	// 관리자 댓글입력
	public int addCommentByAdmin(Comment comment);
	
	// 댓글 입력
	public int addComment(Comment comment);
	
	// 댓글 수정
	public int modifyComment(Comment comment);
	
	// 댓글 삭제
	public int removeComment(Comment comment);
	
	// 해당 게시글의 댓글 총 갯수
	public int totalComment(int boardNo);
	
	// 해당 멤버의 댓글 전체삭제
	public int removeCommentAll(String memberId);
}
