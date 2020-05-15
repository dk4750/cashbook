package com.gdu.cashbook.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

// 서비스 에노테이션
// 이 클래스를 실행하다가 하나라도 예외를 발생하면 모두 롤백 시키는 트랜잭션. (메소드 위에 트랜잭션 에노테이션이 있으면 그 메소드를 실행 예외발생에만 롤백)
@Service
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private MemberidMapper memberidMapper;
	@Autowired
	private JavaMailSender javaMailSender; // @Component가 없어서 오토와이어드 불가능..
	// 멤버 비밀번호 찾기.
	public int getMemberPw(Member member) {
		
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
		
		String memberPw = uuid.toString().substring(0, 8);
		member.setMemberPw(memberPw);
		int row = memberMapper.updateMemberPw(member);
		if(row == 1) {
			// 업데이트 성공한 랜덤된 비밀번호를 전송. 메일을 보내는 메일 객체가 필요하다.
			// 매일객체 new JavaMailSender();
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(member.getMemberMail());
			simpleMailMessage.setFrom("s01036385479@gmail.com");
			simpleMailMessage.setSubject("cashbook 비밀번호 찾기 메일");
			simpleMailMessage.setText("변경된 비밀번호는 " + memberPw + " 입니다.");
			
			javaMailSender.send(simpleMailMessage);
		}
		return row;
	}
	
	// 멤버 아이디 찾기
	public String getMemberIdByMember(Member member) {
		return memberMapper.selectMemberIdByMember(member);
	}
	
	// 멤버 삭제 트랜잭션
	@Transactional
	public int removeMember(LoginMember loginMember) {
		String memberId = loginMember.getMemberId();
		if(memberMapper.removeMember(loginMember) == 1) {
			return memberidMapper.insertMemberId(memberId);
		}
		return 0;
	}
	
	// 멤버 정보 수정
	public void modifyMember(Member member) {
		memberMapper.modifyMember(member);
		return;
	}
	
	// 멤버 한명 상제정보 전체 출력
	public Member getMemberOneAll(LoginMember loginMember) {
		return memberMapper.selectMemberOneAll(loginMember);
	}
	
	// 멤버 한명 상세정보 출력
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	
	// 멤버 아이디 중복확인
	public String CheckMemberId(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck);
	}
	
	// 멤버 로그인. 리턴값은 로그인멤버
	public LoginMember login(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);
	}
	
	// 컨트롤러가 호출 할 멤버 추가 메소드
	public void addMember(Member member) {
		memberMapper.addMember(member);
		return;
	}
}
