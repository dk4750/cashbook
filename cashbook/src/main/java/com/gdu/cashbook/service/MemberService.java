package com.gdu.cashbook.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.mapper.CategoryMapper;
import com.gdu.cashbook.mapper.CommentMapper;
import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;

// 서비스 에노테이션
// 이 클래스를 실행하다가 하나라도 예외를 발생하면 모두 롤백 시키는 트랜잭션. (메소드 위에 트랜잭션 에노테이션이 있으면 그 메소드를 실행 예외발생에만 롤백)
@Service
public class MemberService {
	@Autowired private MemberMapper memberMapper;
	@Autowired private MemberidMapper memberidMapper;
	@Autowired private JavaMailSender javaMailSender; // @Component가 없어서 오토와이어드 불가능..
	@Autowired private CashMapper cashMapper;
	@Autowired private BoardMapper boardMapper;
	@Autowired private CategoryMapper categoryMapper;
	@Autowired private CommentMapper commentMapper;
	
	@Value("C:\\Users\\gd7\\Documents\\workspace-spring-tool-suite-4-4.6.1.RELEASE\\maven.1590486053513\\cashbook\\src\\main\\resources\\static\\upload\\")
	private String path;
	
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
		// 멤버 이미지파일 삭제
		// 1-1 파일 이름 select.. member_pic FROM member
		String memberId = loginMember.getMemberId();
		String memberPic = memberMapper.selectMemberPic(memberId);
		// 1-2 파일 삭제
		File file = new File(path+memberPic);
		if(memberPic.equals("default.png")) {
			memberPic = "default.png";
		} else {
			if(file.exists()) {
				file.delete();
			}
		}
		// 2.. 삭제 결과값이 1일시 인서트..
		if((cashMapper.removeCashByMember(memberId) != 0 || cashMapper.removeCashByMember(memberId) == 0) 
				&& (categoryMapper.removeCategoryAll(memberId) != 0 || categoryMapper.removeCategoryAll(memberId) == 0)
				&& (commentMapper.removeCommentAll(memberId) != 0 || commentMapper.removeCommentAll(memberId) == 0)
				&& (boardMapper.removeBoardAll(memberId) != 0 || boardMapper.removeBoardAll(memberId) == 0)
				&& memberMapper.removeMember(loginMember) == 1) {
			return memberidMapper.insertMemberId(memberId);
		} else {
			return 0;
		}
	}
	
	// 멤버 정보 수정
	public boolean modifyMember(MemberForm memberForm) {
		String memberId = memberForm.getMemberId();
		String memberPic = memberMapper.selectMemberPic(memberId);
		if(memberPic.equals("default.png")) {
			memberPic = "default.png";
		} else {
			File file = new File(path+memberPic);
			if(memberPic.equals("default.png")) {
				memberPic = "default.png";
			} else {
				if(file.exists()) {
					file.delete();
				}
			}
		}
		
		
		Member member = new Member();
		
		// 멀리파트파일 생성
		// mf안에는 이미지, 이미지이름, 이미지속성 등이 들어있는데 그것을 분리할것이다..
		MultipartFile mf = memberForm.getMemberPic();
		// 파일의 실제 이름.. ###.jpg
		// 확장자를 구하기위해서 마지막.의 위치를 알아야한다.. substring
		String originName = mf.getOriginalFilename();
		System.out.println(originName + " <-- originName");
		
		// 업로드 파일이 image가 아닐 시 리 다이렉트
		System.out.println(memberForm.getMemberPic().getContentType() + " <--getCOntentType()");
		if(!originName.equals("")) {
			if(!memberForm.getMemberPic().getContentType().equals("image/png") && !memberForm.getMemberPic().getContentType().equals("image/jpg") && !memberForm.getMemberPic().getContentType().equals("image/jpeg") && !memberForm.getMemberPic().getContentType().equals("image/gif")) {
				return false;
			}
		}
		
		memberPic = "";
		if(originName.equals("")) {
			memberPic = "default.png";
		} else {
			int lastDot = originName.lastIndexOf(".");			// . 의 마지막 위치를 찾는것
			String extension = originName.substring(lastDot);	// 마지막 점에서부터 나머지것들을 나오게끔
			memberPic = memberForm.getMemberId()+extension;	// 아이디+확장자
			
			// 2. 파일 저장하다가 예외
			
			File file = new File(path + memberPic);
			
			try {
				mf.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();	// 자바의 예외 두가지
			}
		}
		
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberMail(memberForm.getMemberMail());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberPic(memberPic);
		
		memberMapper.modifyMember(member);
		return true;
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
	public boolean addMember(MemberForm memberForm) {
		// 멤버 생성
		Member member = new Member();
		// 멀리파트파일 생성
		// mf안에는 이미지, 이미지이름, 이미지속성 등이 들어있는데 그것을 분리할것이다..
		MultipartFile mf = memberForm.getMemberPic();
		// 파일의 실제 이름.. ###.jpg
		// 확장자를 구하기위해서 마지막.의 위치를 알아야한다.. substring
		String originName = mf.getOriginalFilename();
		System.out.println(originName + " <-- originName");
		
		// 업로드 파일이 image가 아닐 시 리 다이렉트
		System.out.println(memberForm.getMemberPic().getContentType() + " <--getCOntentType()");
		if(!originName.equals("")) {
			if(!memberForm.getMemberPic().getContentType().equals("image/png") && !memberForm.getMemberPic().getContentType().equals("image/jpg") && !memberForm.getMemberPic().getContentType().equals("image/jpeg") && !memberForm.getMemberPic().getContentType().equals("image/gif")) {
				return false;
			}
		}
		
		String memberPic = "";
		if(originName.equals("")) {
			memberPic = "default.png";
		} else {
			int lastDot = originName.lastIndexOf(".");			// . 의 마지막 위치를 찾는것
			String extension = originName.substring(lastDot);	// 마지막 점에서부터 나머지것들을 나오게끔
			memberPic = memberForm.getMemberId()+extension;	// 아이디+확장자
			
			// 2. 파일 저장하다가 예외
			
			File file = new File(path + memberPic);
			
			try {
				mf.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();	// 자바의 예외 두가지
			}
		}
		
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

		return true;
	}
}
