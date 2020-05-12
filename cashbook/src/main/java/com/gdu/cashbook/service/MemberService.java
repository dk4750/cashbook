package com.gdu.cashbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.vo.Member;

@Service
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	
	public void addMember(Member member) {
		memberMapper.addMember(member);
	}
}
