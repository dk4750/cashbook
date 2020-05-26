package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;

@Mapper
public interface BoardMapper {
	// 해당 게시글 넘버의 다음 컬럼 넘버를 가져오는 쿼리
	public int nextNo(int boardNo);
	
	// 해당 게시글 넘버의 이전 넘버 가져오는 쿼리
	public int previousNo(int boardNo);
	
	// 게시글 전체삭제 (회원탈퇴시 사용)
	public int removeBoardAll(String memberId);
	
	// 게시글 처음 번호 구해오기
	public int firstBoardNo();
	
	// 게시글 삭제
	public int removeBoard(int boardNo);
	
	// 게시글 업데이트
	public int modifyBoard(Board board);
	
	// 게시글 생성
	public int addBoard(Board board);
	
	// 이전 / 다음 게시글 출력을 위해 마지막 번호를 구해오기.
	public int lastBoardNo();
	
	// board리스트 출력
	public List<Board> selectBoardList(Map<String, Object> map);
	
	// lastPage 구하기위해서 totalRow 구하기
	public int getTotalRow(String searchWord);
	
	// boardNo 받아서 해당 게시글 정보 가져오기
	public Board selectBoardOne(int boardNo);
}
