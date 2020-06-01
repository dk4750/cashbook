package com.gdu.cashbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.AdminMapper;
import com.gdu.cashbook.vo.Admin;

@Service
@Transactional
public class AdminService {
	@Autowired private AdminMapper adminMapper;
	
	// 관리자 로그인 셀렉트
	public Admin selectAdmin(Admin admin) {
		return adminMapper.selectAdmin(admin);
	}
}
