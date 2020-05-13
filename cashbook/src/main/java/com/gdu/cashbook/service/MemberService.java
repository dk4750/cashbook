package com.gdu.cashbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

// 서비스 에노테이션
// 이 클래스를 실행하다가 하나라도 예외를 발생하면 모두 롤백 시키는 트랜잭션. (메소드 위에 트랜잭션 에노테이션이 있으면 그 메소드를 실행 예외발생에만 롤백)
@Service
@Transactional
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	
	// 멤버 로그인. 리턴값은 로그인멤버
	public LoginMember login(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);
	}
	
	// 컨트롤러가 호출 할 멤버 추가 메소드
	public void addMember(Member member) {
		memberMapper.addMember(member);
	}
}
