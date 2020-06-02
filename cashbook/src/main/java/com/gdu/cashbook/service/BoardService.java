package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.mapper.CommentMapper;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Comment;

@Service
@Transactional
public class BoardService {
	@Autowired private BoardMapper boardMapper;
	@Autowired private CommentMapper commentMapper;
	
	// 게시글 삭제
	public int removeBoard(int boardNo) {
		return boardMapper.removeBoard(boardNo);
	}
	
	// 게시글 수정
	public int modifyBoard(Board board) {
		return boardMapper.modifyBoard(board);
	}
	
	// board 추가
	public int addBoard(Board board) {
		return boardMapper.addBoard(board);
	}
	
	// board 상세정보 출력
	public Map<String, Object> getBoardOne(Map<String, Object> mapp) {
		// 마지막 보드넘버랑 게시글상세보기를 맵에 담아서 보내주기..
		int boardNo = (int)mapp.get("boardNo");
		System.out.println(boardNo);
		
		// 댓글 페이징 커런트페이지
		int commentCurrentPage = (int)mapp.get("commentCurrentPage");
		System.out.println(commentCurrentPage);
		int rowPerPage = 3;
		int beginRow = (commentCurrentPage-1)*rowPerPage;
		int totalComment = commentMapper.totalComment(boardNo);
		int commentLastPage = totalComment / rowPerPage;
		if(totalComment%rowPerPage != 0) {
			commentLastPage += 1;
		}
		System.out.println(totalComment);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("beginRow", beginRow);
		map2.put("rowPerPage", rowPerPage);
		map2.put("boardNo", boardNo);
		
		List<Comment> commentList = commentMapper.getCommentList(map2);
		int firstBoardNo = boardMapper.firstBoardNo();
		int lastBoardNo = boardMapper.lastBoardNo();
		Board board = boardMapper.selectBoardOne(boardNo);
		int previousNo = boardMapper.previousNo(boardNo);
		int nextNo = boardMapper.nextNo(boardNo);
		
		
		// 디버깅
		System.out.println(firstBoardNo);
		System.out.println(lastBoardNo);
		System.out.println(board);
		System.out.println(previousNo + " <== 이전 컬럼 보드넘버");
		System.out.println(nextNo + " <== 다음 컬럼 보드넘버");
		System.out.println(commentList + " <== 댓글 리스트");
		
		Map<String, Object> map = new HashMap<>();
		map.put("commentList", commentList);
		map.put("previousNo", previousNo);
		map.put("nextNo", nextNo);
		map.put("firstBoardNo", firstBoardNo);
		map.put("lastBoardNo", lastBoardNo);
		map.put("board", board);
		map.put("commentLastPage", commentLastPage);
		
		return map;
	}
	
	// board리스트 출력
	public Map<String, Object> getBoardList(int currentPage, String searchWord) {
		// 디버깅
		System.out.println(searchWord);
		
		// 페이징을 위해서 필요한 값들을 map에 담아서 보내주기.
		int rowPerPage = 10;
		int beginRow = (currentPage-1)*rowPerPage;
		//일회용 맵
		Map<String, Object> map = new HashMap<>();
		map.put("searchWord", searchWord);
		map.put("beginRow", beginRow);
		map.put("rowPerPage", rowPerPage);
		
		// 라스트페이지 출력
		int totalRow = boardMapper.getTotalRow(searchWord);
		int lastPage = totalRow/rowPerPage;
		if(totalRow%rowPerPage != 0) {
			lastPage +=1;
		}
		
		// list랑 라스트페이지 맵에 담아서 리턴
		List<Board> list = boardMapper.selectBoardList(map);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("list", list);
		map2.put("lastPage", lastPage);
		
		return map2;
	}

}
