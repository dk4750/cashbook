package com.gdu.cashbook.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

// 매퍼기능 에노테이션, 객체생성
@Mapper
public interface MemberMapper {
	// 삭제시 멤버 아이디가 memberid 테이블에 추가되는 메소드
	public int insertMemberId(String memberId);
	
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