package com.gdu.cashbook.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

// 매퍼기능 에노테이션, 객체생성
@Mapper
public interface MemberMapper {
	
	// 멤버 총명수 구하기
	public int getTotalMember();
	
	// 멤버 한명 삭제.. 관리자기능
	public int removeByAdmin(String memberId);
	
	// 멤버 리스트 출력
	public List<Member> selectMemberListAll(int beginRow, int rowPerPage);
	
	// 아이디와 이멜일을 입력해서 리턴값이 있는지 없는지를 알아내는 메소드
	public int updateMemberPw(Member member); 
	
	// 멤버 pic을 리턴..
	public String selectMemberPic(String memberId);
	
	// 이름, 이메일, 전화번호를 입력받아 아이디값을 리턴하는 아이디검색메소드
	public String selectMemberIdByMember(Member member);
	
	// 삭제시 멤버 아이디가 memberid 테이블에 추가되는 메소드		MemberidMapper.java로 변경.
	// public int insertMemberId(String memberId);
	
	// 멤버 한명 정보 전체출력
	public Member selectMemberOneAll(LoginMember loginMember);
	
	// 멤버 삭제
	public int removeMember(LoginMember loginMember);
	
	// 멤버 수정
	public void modifyMember(Member member);
	
	// 아이디 중복확인.
	public String selectMemberId(String memberIdCheck);
	
	// 로그인 멤버 셀럭트하기. 리턴값으로 로그인멤버 매개변수로도 로그인멤버.
	public LoginMember selectLoginMember(LoginMember loginMember);
	
	// 멤버 추가 메소드. 서비스가 호출 할 메소드
	public void addMember(Member member);
	
	// 멤버 한명 정보 출력하기. (상세정보출력)
	public Member selectMemberOne(LoginMember loginMember);
}