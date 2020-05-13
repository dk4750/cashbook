package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

// 매퍼기능 에노테이션
@Mapper
public interface MemberMapper {
	// 로그인 멤버 셀럭트하기. 리턴값으로 로그인멤버 매개변수로도 로그인멤버.
	public LoginMember selectLoginMember(LoginMember loginMember);
	
	// 멤버 추가 메소드. 서비스가 호출 할 메소드
	public void addMember(Member member);
}