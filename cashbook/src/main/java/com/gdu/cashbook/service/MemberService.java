package com.gdu.cashbook.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;

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
	@Transactional
	public void addMember(MemberForm memberForm) {
		// 멤버 생성
		Member member = new Member();
		// 멀리파트파일 생성
		// mf안에는 이미지, 이미지이름, 이미지속성 등이 들어있는데 그것을 분리할것이다..
		MultipartFile mf = memberForm.getMemberPic();
		// 파일의 실제 이름.. ###.jpg
		// 확장자를 구하기위해서 마지막.의 위치를 알아야한다.. substring
		String originName = mf.getOriginalFilename();
		System.out.println(originName + " <-- originName");
		
		String memberPic = "";
		if(originName.equals("")) {
			memberPic = "default.png";
		} else {
			int lastDot = originName.lastIndexOf(".");			// . 의 마지막 위치를 찾는것
			String extension = originName.substring(lastDot);	// 마지막 점에서부터 나머지것들을 나오게끔
			memberPic = memberForm.getMemberId()+extension;	// 아이디+확장자
		}
		
		// 이미지파일이 아니라면 백
		/*
		if(mf.getContentType().equals("image/png") || mf.getContentType().equals("image/jpg")) {
			// 업로드
		} else {
			// 쓸 수 없다
		}
		*/
		// 1. db에 저장하다가 예외 	- @Transactional
		// MemberForm -> member 로 변환해주어야한다.
			member.setMemberId(memberForm.getMemberId());
			member.setMemberPw(memberForm.getMemberPw());
			member.setMemberName(memberForm.getMemberName());
			member.setMemberAddr(memberForm.getMemberAddr());
			member.setMemberMail(memberForm.getMemberMail());
			member.setMemberPhone(memberForm.getMemberPhone());
			member.setMemberPic(memberPic);
			System.out.println(member + " <-- MemberService.addMember:member");
			// 파일 -> 디스크로 물리적으로 저장
			memberMapper.addMember(member);
				
		// 2. 파일 저장하다가 예외
		String path = "C:\\Users\\GD7\\Documents\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload";
		File file = new File(path + "\\" + memberPic);
		
		try {
			mf.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();	// 자바의 예외 두가지
		}									// 1. 예외처리를 해야만 문법적으로 이상이 없는 예외
											// 2. 예외처리를 코드에서 구현하지않아도 아무문제 없는 예외... RuntimeException();
		
		return;
	}
}
