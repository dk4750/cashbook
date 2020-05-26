package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Comment;

@Mapper
public interface CommentMapper {
	// Comment List 출력
	public List<Comment> getCommentList(int boardNo);
	
}
